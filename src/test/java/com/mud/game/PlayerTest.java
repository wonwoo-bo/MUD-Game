package com.mud.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Room startingRoom;
    private Room northRoom;
    private Monster goblin;

    @BeforeEach
    void setUp() {
        // 创建测试房间
        startingRoom = new Room("Start Room", "A starting room");
        northRoom = new Room("North Room", "A room to the north");
        
        // 连接房间
        startingRoom.setExit("north", northRoom);
        northRoom.setExit("south", startingRoom);
        
        // 创建怪物
        goblin = new Monster("Goblin", 50, 10);
        startingRoom.setMonster(goblin);
        
        // 创建玩家
        player = new Player(startingRoom);
    }

    @Test
    void testPlayerInitialization() {
        assertEquals(100, player.getHealth());
        assertEquals(20, player.getDamage());
        assertEquals(startingRoom, player.getCurrentRoom());
        assertTrue(player.isAlive());
    }

    @Test
    void testPlayerMovement() {
        // 测试成功移动
        player.move("north");
        assertEquals(northRoom, player.getCurrentRoom());
        
        // 测试移动到无效方向
        player.move("west"); // 不存在的方向
        assertEquals(northRoom, player.getCurrentRoom()); // 应该保持在原地
    }

    @Test
    void testPlayerAttack() {
        // 测试攻击怪物
        player.attack(goblin);
        assertEquals(30, goblin.getHealth()); // 50 - 20 = 30
        assertEquals(90, player.getHealth()); // 100 - 10 = 90
        
        // 测试攻击已死亡的怪物
        goblin.takeDamage(30); // 杀死怪物
        player.attack(goblin); // 应该不会造成伤害
        assertEquals(0, goblin.getHealth());
        assertEquals(90, player.getHealth());
    }

    @Test
    void testPlayerTakeDamage() {
        player.takeDamage(30);
        assertEquals(70, player.getHealth());
        
        // 测试伤害超过当前生命值
        player.takeDamage(80);
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
    }
}
