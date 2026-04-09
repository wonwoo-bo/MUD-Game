# Mud泥巴游戏 详细设计说明书（核心控制类）
## 1. 核心控制类：CommandController（玩家指令解析）
### 1.1 类职责
接收玩家输入指令（move/attack/openBag），通过命令模式分发到对应业务模块。
### 1.2 核心方法 PAD 图：parsePlayerCommand()
```text
┌──────────────────────────────────────────────────────────────┐
│              PAD：CommandController.parsePlayerCommand()       │
├──────────────────────────────────────────────────────────────┤
│ 输入：rawInput:String, ctx:PlayerContext                       │
│ 输出：result:CommandResult                                     │
├──────────────────────────────────────────────────────────────┤
│ 1. raw ← trim(rawInput)                                        │
│ 2. IF raw 为空 THEN                                            │
│      result ← CommandResult(FAIL, "empty command")             │
│      RETURN result                                             │
│    ENDIF                                                       │
│ 3. tokens ← split(raw, 空白符)                                 │
│ 4. cmd ← lower(tokens[0])                                      │
│ 5. args ← tokens[1..]                                          │
│ 6. IF cmd 是别名(go→move, kill→attack) THEN                     │
│      cmd ← normalize(cmd)                                      │
│    ENDIF                                                       │
│ 7. SELECT cmd                                                  │
│      CASE "move":                                              │
│        IF args 为空 THEN                                       │
│           result ← CommandResult(FAIL, "missing direction")    │
│        ELSE                                                    │
│           c ← new MoveCommand(args[0])                          │
│           result ← c.execute(ctx)                               │
│        ENDIF                                                   │
│      CASE "attack":                                            │
│        IF args 为空 THEN                                       │
│           result ← CommandResult(FAIL, "missing target")       │
│        ELSE                                                    │
│           c ← new AttackCommand(args[0])                        │
│           result ← c.execute(ctx)                               │
│        ENDIF                                                   │
│      CASE "openBag":                                           │
│        c ← new OpenBagCommand()                                 │
│        result ← c.execute(ctx)                                  │
│      CASE "help":                                              │
│        c ← new HelpCommand()                                    │
│        result ← c.execute(ctx)                                  │
│      CASE "quit":                                              │
│        c ← new QuitCommand()                                    │
│        result ← c.execute(ctx)                                  │
│      DEFAULT:                                                  │
│        result ← CommandResult(FAIL, "unknown command: " + cmd) │
│    ENDSELECT                                                   │
│ 8. RETURN result                                               │
└──────────────────────────────────────────────────────────────┘
```

## 2. 核心控制类：RoleStateMachine（角色状态机）
### 2.1 类职责
通过状态模式管理玩家角色状态（空闲→战斗→死亡→回城）。
### 2.2 核心方法 N-S 盒图：switchRoleState()
```text
┌──────────────────────────────────────────────────────────────────────────────┐
│                  N-S：RoleStateMachine.switchRoleState(event)               │
├──────────────────────────────────────────────────────────────────────────────┤
│ 输入：event:RoleEvent                                                        │
│ 输出：newState:RoleState                                                     │
├──────────────────────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────────────────────┐ │
│ │ IF currentState == DEAD THEN                                              │ │
│ │   IF event == REVIVE THEN                                                 │ │
│ │     onExit(DEAD)                                                          │ │
│ │     currentState ← IDLE                                                   │ │
│ │     onEnter(IDLE)                                                         │ │
│ │   ELSE                                                                    │ │
│ │     保持 DEAD（忽略事件）                                                  │ │
│ │   ENDIF                                                                   │ │
│ │ ELSE                                                                      │ │
│ │   IF event == DIE THEN                                                    │ │
│ │     onExit(currentState)                                                  │ │
│ │     currentState ← DEAD                                                   │ │
│ │     onEnter(DEAD)                                                         │ │
│ │   ELSE                                                                    │ │
│ │     ┌──────────────────────────────────────────────────────────────────┐ │ │
│ │     │ SELECT currentState                                               │ │ │
│ │     │  CASE IDLE:                                                       │ │ │
│ │     │    IF event == ENTER_BATTLE THEN                                  │ │ │
│ │     │      onExit(IDLE)                                                 │ │ │
│ │     │      currentState ← BATTLE                                        │ │ │
│ │     │      onEnter(BATTLE)                                              │ │ │
│ │     │    ELSEIF event == BACK_HOME THEN                                 │ │ │
│ │     │      onExit(IDLE)                                                 │ │ │
│ │     │      currentState ← HOME                                          │ │ │
│ │     │      onEnter(HOME)                                                │ │ │
│ │     │    ELSE                                                           │ │ │
│ │     │      保持 IDLE                                                     │ │ │
│ │     │    ENDIF                                                          │ │ │
│ │     │  CASE BATTLE:                                                     │ │ │
│ │     │    IF event == LEAVE_BATTLE THEN                                  │ │ │
│ │     │      onExit(BATTLE)                                               │ │ │
│ │     │      currentState ← IDLE                                          │ │ │
│ │     │      onEnter(IDLE)                                                │ │ │
│ │     │    ELSEIF event == BACK_HOME THEN                                 │ │ │
│ │     │      onExit(BATTLE)                                               │ │ │
│ │     │      currentState ← HOME                                          │ │ │
│ │     │      onEnter(HOME)                                                │ │ │
│ │     │    ELSE                                                           │ │ │
│ │     │      保持 BATTLE                                                   │ │ │
│ │     │    ENDIF                                                          │ │ │
│ │     │  CASE HOME:                                                       │ │ │
│ │     │    IF event == LEAVE_HOME THEN                                    │ │ │
│ │     │      onExit(HOME)                                                 │ │ │
│ │     │      currentState ← IDLE                                          │ │ │
│ │     │      onEnter(IDLE)                                                │ │ │
│ │     │    ELSEIF event == ENTER_BATTLE THEN                              │ │ │
│ │     │      onExit(HOME)                                                 │ │ │
│ │     │      currentState ← BATTLE                                        │ │ │
│ │     │      onEnter(BATTLE)                                              │ │ │
│ │     │    ELSE                                                           │ │ │
│ │     │      保持 HOME                                                     │ │ │
│ │     │    ENDIF                                                          │ │ │
│ │     │  DEFAULT:                                                         │ │ │
│ │     │    currentState ← IDLE（防御性回退）                                │ │ │
│ │     │ ENDSELECT                                                         │ │ │
│ │     └──────────────────────────────────────────────────────────────────┘ │ │
│ │   ENDIF                                                                   │ │
│ │ ENDIF                                                                     │ │
│ └──────────────────────────────────────────────────────────────────────────┘ │
│ RETURN currentState                                                          │
└──────────────────────────────────────────────────────────────────────────────┘
```

## 3. 核心控制类：MonsterFactory（怪物工厂）
### 3.1 类职责
根据场景ID，生成对应类型怪物（普通怪/BOSS怪）。
### 3.2 核心方法 PAD 图：createMonsterBySceneId()
```text
┌──────────────────────────────────────────────────────────────┐
│          PAD：MonsterFactory.createMonsterBySceneId()         │
├──────────────────────────────────────────────────────────────┤
│ 输入：sceneId:int                                              │
│ 输出：m:Monster                                                │
├──────────────────────────────────────────────────────────────┤
│ 1. cfg ← SceneConfigRepository.find(sceneId)                   │
│ 2. IF cfg 不存在 THEN                                          │
│      cfg ← SceneConfig(默认普通怪)                             │
│    ENDIF                                                       │
│ 3. IF cfg.isBoss == true THEN                                  │
│      m ← new BossMonster(type=cfg.monsterType)                 │
│      m.setSkillList(cfg.bossSkills)                            │
│    ELSE                                                        │
│      m ← new NormalMonster(type=cfg.monsterType)               │
│    ENDIF                                                       │
│ 4. m.setHp(cfg.hp)                                             │
│ 5. m.setAttack(cfg.atk)                                        │
│ 6. m.setDefense(cfg.def)                                       │
│ 7. m.setDropTable(cfg.dropTable)                               │
│ 8. RETURN m                                                    │
└──────────────────────────────────────────────────────────────┘
```
