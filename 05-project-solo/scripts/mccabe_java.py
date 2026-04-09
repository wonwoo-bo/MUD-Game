import argparse
import re
import subprocess
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Iterable, List, Optional, Tuple


DECISION_PATTERNS: List[Tuple[str, re.Pattern]] = [
    ("if", re.compile(r"\bif\b")),
    ("for", re.compile(r"\bfor\b")),
    ("while", re.compile(r"\bwhile\b")),
    ("case", re.compile(r"\bcase\b")),
    ("catch", re.compile(r"\bcatch\b")),
    ("&&", re.compile(r"&&")),
    ("||", re.compile(r"\|\|")),
    ("?:", re.compile(r"\?")),
]


def _strip_strings_and_comments(java: str) -> str:
    java = re.sub(r"//.*?$", "", java, flags=re.MULTILINE)
    java = re.sub(r"/\*.*?\*/", "", java, flags=re.DOTALL)
    java = re.sub(r"\"(?:\\.|[^\"\\])*\"", "\"\"", java)
    java = re.sub(r"'(?:\\.|[^'\\])+'", "''", java)
    return java


def _find_method_spans(java: str) -> List[Tuple[str, int, int]]:
    signature = re.compile(
        r"""
        (?P<prefix>\b(public|private|protected)\b[^{;]*?)
        (?P<name>\b[A-Za-z_][A-Za-z0-9_]*\b)
        \s*\(
            [^)]*
        \)
        \s*
        (?:throws\s+[^{]+)?
        \{
        """,
        re.VERBOSE,
    )

    spans: List[Tuple[str, int, int]] = []
    for m in signature.finditer(java):
        name = m.group("name")
        start = m.start()
        body_start = m.end() - 1
        depth = 0
        i = body_start
        while i < len(java):
            ch = java[i]
            if ch == "{":
                depth += 1
            elif ch == "}":
                depth -= 1
                if depth == 0:
                    spans.append((name, start, i + 1))
                    break
            i += 1
    return spans


def cyclomatic_complexity(java_method_source: str) -> int:
    cleaned = _strip_strings_and_comments(java_method_source)
    complexity = 1
    for _, pattern in DECISION_PATTERNS:
        complexity += len(pattern.findall(cleaned))
    return complexity


@dataclass(frozen=True)
class MethodComplexity:
    class_name: str
    method_name: str
    complexity: int


def analyze_source(java_source: str) -> List[MethodComplexity]:
    cleaned = _strip_strings_and_comments(java_source)
    class_match = re.search(r"\bclass\s+([A-Za-z_][A-Za-z0-9_]*)\b", cleaned)
    class_name = class_match.group(1) if class_match else "UnknownClass"

    results: List[MethodComplexity] = []
    for method_name, start, end in _find_method_spans(cleaned):
        method_src = cleaned[start:end]
        results.append(
            MethodComplexity(
                class_name=class_name,
                method_name=method_name,
                complexity=cyclomatic_complexity(method_src),
            )
        )
    return results


def read_file(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def git_show(commit: str, path: str) -> str:
    completed = subprocess.run(
        ["git", "show", f"{commit}:{path}"],
        check=True,
        capture_output=True,
        text=True,
    )
    return completed.stdout


def iter_java_files(root: Path) -> Iterable[Path]:
    yield from root.rglob("*.java")


def analyze_working_tree(root: Path) -> List[MethodComplexity]:
    results: List[MethodComplexity] = []
    for file_path in iter_java_files(root):
        results.extend(analyze_source(read_file(file_path)))
    return results


def analyze_git_commit(commit: str, paths: List[str]) -> List[MethodComplexity]:
    results: List[MethodComplexity] = []
    for p in paths:
        results.extend(analyze_source(git_show(commit, p)))
    return results


def format_markdown_table(rows: List[MethodComplexity], only: Optional[List[str]]) -> str:
    filtered = rows
    if only:
        only_set = set(only)
        filtered = [r for r in rows if f"{r.class_name}.{r.method_name}" in only_set]

    filtered.sort(key=lambda r: (r.class_name, r.method_name))
    lines = ["| Class.Method | McCabe |", "|---|---:|"]
    for r in filtered:
        lines.append(f"| {r.class_name}.{r.method_name} | {r.complexity} |")
    return "\n".join(lines)


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--root", default="src/main/java", help="source root (working tree mode)")
    ap.add_argument("--commit", default=None, help="git commit hash to analyze")
    ap.add_argument("--paths", nargs="*", default=[], help="java file paths (git mode)")
    ap.add_argument("--only", nargs="*", default=[], help="filter by Class.Method (optional)")
    args = ap.parse_args()

    if args.commit:
        if not args.paths:
            raise SystemExit("--paths is required when --commit is set")
        rows = analyze_git_commit(args.commit, args.paths)
    else:
        rows = analyze_working_tree(Path(args.root))

    print(format_markdown_table(rows, args.only if args.only else None))


if __name__ == "__main__":
    main()

