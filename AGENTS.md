# AGENTS.md

## Project Overview
Java MUD Game - 文字冒险游戏引擎，支持控制台和 Web 双界面

## Directory Structure
```
MUD-Game-develop/
├── src/main/java/com/mud/game/    # 游戏核心代码
├── src/test/java/com/mud/game/    # 单元测试
├── src/main/resources/            # 静态资源
└── docs/                          # 文档
```

## Core Modules
- **Game**: 主游戏逻辑、命令处理
- **GameController**: Web API 控制器
- **Player**: 玩家状态和行为
- **Room**: 房间管理
- **Monster**: 怪物逻辑
- **World**: 世界构建
- **Item/Inventory/TradeController**: 物品和交易系统（新增）

## Coding Standards
- Java 17+ / Spring Boot 3.2
- 包名 `com.mud.game.*`
- 类名使用 PascalCase，方法和变量使用 camelCase
- 测试文件放在 `src/test/java/com/mud/game/`，命名为 `*Test.java`

## Forbidden Actions
- 不要直接修改静态资源，使用 `src/main/resources/`
- 不要删除已有测试，除非功能已废弃
- 不要硬编码游戏参数，使用配置管理
- 不要破坏现有 API 兼容性
- 不要跳过测试直接提交代码

## Quick Start
```bash
mvn test          # 运行测试
mvn spring-boot:run  # 启动应用
```

## Key Notes
- Web 服务默认端口 8080
- 所有 API 变更需同步更新测试
- 遵循现有代码风格和架构模式
