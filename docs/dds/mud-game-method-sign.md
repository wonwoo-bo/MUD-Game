# Mud泥巴游戏 跨类调用方法签名规范
## 通用规则
1. 方法名：小驼峰，动词+游戏业务名词（如parsePlayerCommand）；
2. 参数：必须标注游戏业务含义，非空校验；
3. 返回值：统一封装`MudResult<T>`（状态码+提示语+业务数据）。

## 核心跨类调用签名（泥巴游戏专属）
### 1. CommandController → RoleStateMachine
```java
/**
 * 切换玩家角色状态
 * @param roleId 玩家角色ID
 * @param targetState 目标状态(IDLE/FIGHT/DEAD/TOWN)
 * @return MudResult<Boolean> 状态切换结果
 */
public MudResult<Boolean> switchRoleState(Long roleId, String targetState);