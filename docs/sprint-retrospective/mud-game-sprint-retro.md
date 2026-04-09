# Sprint Retrospective（Sprint 2）

## 1. Sprint 目标
- 将 Sprint 1 的高耦合 CLI 引擎进行解耦重构，降低“上帝类”风险与跨层耦合。
- 保持核心 MVP 指令可跑通（go/look/help/quit），并为后续扩展（门/锁/战斗/物品）预留稳定扩展点。

## 2. 重构前后的关键变化

### 2.1 设计模式/OO 基石落地
- Command Pattern：将命令解析与命令执行拆分为 `CommandParser` + `CommandRegistry` + 各命令类（如 [GoCommand.java]。
- 分层解耦（View 与 Domain 分离）：将输出集中到 [Renderer.java]，领域对象不再直接 `println`。
- 多态扩展点（Strategy/Polymorphism）：从 `Room -> Room` 的硬连接，演进为 `Room -> Exit -> Room`（[Exit.java], [OpenExit.java]。
- 强类型（封装）：方向由 String 演进为 [Direction.java]，集中校验与展示。

### 2.2 责任边界变化（对照）
- Before（Sprint 1）：`Game.processCommand()` 同时负责解析/路由/输出；`Player.move(String)` 既做业务又做输出。
- After（Sprint 2）：`Game` 只负责循环与协调；命令由各 `Command` 执行；移动结果通过 `MoveResult` 回传，由 `Renderer` 输出。

## 3. McCabe（圈复杂度）对比

说明：使用 `McCabe = 1 + 决策点(if/for/while/case/catch/&&/||/?:)` 的近似计算；脚本位置：[mccabe_java.py]。

### 3.1 Before（commit: 3059f465…）
| Class.Method | McCabe |
|---|---:|
| Game.processCommand | 10 |
| Player.move | 2 |

### 3.2 After（当前工作区）
| Class.Method | McCabe |
|---|---:|
| Game.handleInput | 3 |
| Game.executeParsed | 3 |
| Game.handleGoDirection | 7 |
| GoCommand.execute | 5 |
| Player.move | 3 |

结论：
- “核心入口方法”复杂度下降：`Game.processCommand(10)` → `Game.handleInput(3)`，主控流程更清晰。
- 复杂度被分散到职责更明确的对象中（命令类/出口对象），降低单点臃肿风险。
- 领域层复杂度略上升（Player.move 2→3），但换来跨层解耦（不再直接输出）与可测试性提升。

## 4. 做得好的地方（Keep）
- 能以“可运行”为硬约束持续重构：重构过程中始终保留最小可验证路径（go/look/help/quit）。
- 先插入扩展点，再追加功能：通过 Exit/Direction/Command 让未来迭代不需要大改主干。
- 用 OOA/DFD 对质驱动决策：坏味道（God Class / SRP 违反 / Stringly Typed）对应到具体改造点。

## 5. 需要改进的地方（Problem）
- 命令交互状态仍存在分支（如 go 的两步输入），需要更系统的“输入状态机”或“会话对象”来进一步降低 `handleGoDirection` 复杂度。
- 缺少自动化回归测试：目前主要靠手工/脚本输入验证，未来需要最小测试集保障重构安全。

## 6. 下个 Sprint 的行动项（Try）
- 引入“输入会话状态机”（Normal / AwaitDirection / …）并以策略对象替换 if 分支。
- 为 Command/Move/Exit 增加单元测试（至少覆盖：合法/非法方向、无出口、退出、未知命令）。
- 增加 `DoorExit`（带条件进入）作为多态扩展的验证样例。
