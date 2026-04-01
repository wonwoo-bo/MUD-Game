# Sprint 1 - Core User Stories (MVP)

## Kanban Board (GitHub Projects 模拟)

### To Do
- [ ] **US-001: 游戏启动与欢迎**
  - **As a** 玩家
  - **I want** 运行程序时看到欢迎界面和第一段场景描述
  - **So that** 我知道游戏已经开始并了解当前处境
  - *Acceptance Criteria*: 控制台输出 "Welcome to the MUD Game!" 以及起始房间的描述。

- [ ] **US-002: 查看当前环境 (Look)**
  - **As a** 玩家
  - **I want** 输入 `look` 指令
  - **I want** 看到当前所在房间的详细描述和可见的出口
  - **So that** 我可以规划下一步的行动
  - *Acceptance Criteria*: 输入 `look` 后，显示房间描述和可见出口。如果房间内有怪物，必须在描述中追加提示（例如 "A goblin is here!"）。

- [ ] **US-003: 基础移动 (Movement)**
  - **As a** 玩家
  - **I want** 输入 `go [direction]` (如 `go north`)
  - **So that** 我可以移动到相邻的房间
  - *Acceptance Criteria*:
    - 如果方向有路，更新玩家位置并显示新房间描述。
    - 如果方向无路，提示 "You can't go that way."

- [ ] **US-004: 退出游戏 (Quit)**
  - **As a** 玩家
  - **I want** 输入 `quit` 或 `exit`
  - **So that** 我可以随时结束游戏程序
  - *Acceptance Criteria*: 程序终止运行，并显示 "Goodbye!"。

- [ ] **US-005: 获取帮助 (Help)**
  - **As a** 玩家
  - **I want** 输入 `help`
  - **So that** 我可以看到所有可用的指令列表
  - *Acceptance Criteria*: 列出 `go`, `look`, `attack`, `quit`, `help` 及其简要说明。

- [ ] **US-006: 基础战斗系统 (Combat)**
  - **As a** 玩家
  - **I want** 输入 `attack [target]` (如 `attack goblin`)
  - **So that** 我可以攻击当前房间内的怪物并推进游戏
  - *Acceptance Criteria*:
    - 如果目标不存在，提示 "There is no [target] here to attack."。
    - 攻击有一定概率被怪物闪避 (Miss)，并打印闪避提示。
    - 攻击命中 (Hit) 时，扣除怪物 HP，并显示怪物剩余血量。
    - 当怪物 HP 归零时，显示击杀提示，并将怪物从当前房间移除。