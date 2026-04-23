# Sprint 3 - Git Flow 分支治理规范（落地版）

目标：所有开发必须走 `feature/*` → PR → `develop`，并在发起 PR 前完成“反向同步主干”的冲突预解决，形成可审计的评审记录。

## 1. 分支命名规范
- 主干：`develop`
- 功能分支：`feature/<scope>-<short-desc>`
  - 例：`feature/ci-maven`、`feature/openapi-contract`、`feature/mock-ui`
- 修复分支：`fix/<scope>-<short-desc>`（可选）

## 2. 标准开发流程（个人）

### 2.1 从 develop 拉分支
```bash
git checkout develop
git pull origin develop
git checkout -b feature/<scope>-<short-desc>
```

### 2.2 开发与提交
```bash
git add .
git commit -m "feat(scope): ..."
```

### 2.3 反向同步主干（发 PR 前必做）
任选一种方式（团队统一即可）：

#### A) Rebase（线性历史，更干净）
```bash
git fetch origin
git rebase origin/develop
```

#### B) Merge（保留合并提交）
```bash
git fetch origin
git merge origin/develop
```

冲突解决完成后继续：
```bash
git push -u origin feature/<scope>-<short-desc>
```

## 3. Pull Request 规范
- PR 目标分支：`develop`
- PR 必须通过 CI（Green Build）
- Reviewer：至少 1 名非作者 Approve 才能合并
- 合并策略：建议 Squash Merge（便于主干干净）

## 4. 两次完整 PR 评审记录（作业要求）
建议用两次不同主题 PR 来满足要求，例如：
- PR#1：CI + Maven + 单测
- PR#2：OpenAPI + Prism Mock + 前端页面

每次 PR 都保留：
- Review/Approve 截图
- CI 绿灯截图
- “反向同步主干”的证据（rebase/merge 的 commit 记录或 PR 中的提示）

