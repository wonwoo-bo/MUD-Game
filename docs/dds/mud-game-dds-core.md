# Mud泥巴游戏 详细设计说明书（核心控制类）
## 1. 核心控制类：CommandController（玩家指令解析）
### 1.1 类职责
接收玩家输入指令（move/attack/openBag），通过命令模式分发到对应业务模块。
### 1.2 核心方法 PAD 图：parsePlayerCommand()
![指令解析PAD图](pad-ns-diagrams/CommandController_PAD.png)

## 2. 核心控制类：RoleStateMachine（角色状态机）
### 2.1 类职责
通过状态模式管理玩家角色状态（空闲→战斗→死亡→回城）。
### 2.2 核心方法 N-S 盒图：switchRoleState()
![角色状态N-S图](pad-ns-diagrams/RoleStateMachine_NS.png)

## 3. 核心控制类：MonsterFactory（怪物工厂）
### 3.1 类职责
根据场景ID，生成对应类型怪物（普通怪/BOSS怪）。
### 3.2 核心方法 PAD 图：createMonsterBySceneId()
![怪物生成PAD图](pad-ns-diagrams/MonsterFactory_PAD.png)