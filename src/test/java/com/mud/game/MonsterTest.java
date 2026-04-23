package com.mud.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MonsterTest {
    private Monster goblin;

    @BeforeEach
    void setUp() {
        goblin = new Monster("Goblin", 50, 10);
    }

    @Test
    void testMonsterInitialization() {
        assertEquals("Goblin", goblin.getName());
        assertEquals(50, goblin.getHealth());
        assertEquals(10, goblin.getDamage());
        assertTrue(goblin.isAlive());
    }

    @Test
    void testTakeDamage() {
        goblin.takeDamage(20);
        assertEquals(30, goblin.getHealth());
        assertTrue(goblin.isAlive());
        
        // 测试伤害超过当前生命值
        goblin.takeDamage(40);
        assertEquals(0, goblin.getHealth());
        assertFalse(goblin.isAlive());
    }

    @Test
    void testIsAlive() {
        assertTrue(goblin.isAlive());
        
        // 测试怪物死亡
        goblin.takeDamage(50);
        assertFalse(goblin.isAlive());
    }
}
