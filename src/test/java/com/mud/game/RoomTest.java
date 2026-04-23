package com.mud.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    private Room room;
    private Room northRoom;
    private Monster goblin;

    @BeforeEach
    void setUp() {
        room = new Room("Test Room", "A test room description");
        northRoom = new Room("North Room", "A room to the north");
        goblin = new Monster("Goblin", 50, 10);
    }

    @Test
    void testRoomInitialization() {
        assertNotNull(room);
        assertTrue(room.getDescription().contains("Test Room"));
        assertTrue(room.getDescription().contains("A test room description"));
        assertNull(room.getExit("north"));
        assertNull(room.getMonster());
    }

    @Test
    void testSetAndGetExit() {
        room.setExit("north", northRoom);
        assertEquals(northRoom, room.getExit("north"));
        assertEquals(northRoom, room.getExit("NORTH")); // 测试大小写不敏感
        assertNull(room.getExit("south"));
    }

    @Test
    void testSetAndGetMonster() {
        room.setMonster(goblin);
        assertEquals(goblin, room.getMonster());
        
        // 测试移除怪物
        room.setMonster(null);
        assertNull(room.getMonster());
    }

    @Test
    void testDescriptionWithMonster() {
        room.setMonster(goblin);
        String description = room.getDescription();
        assertTrue(description.contains("Goblin"));
        assertTrue(description.contains("There is a Goblin here!"));
    }

    @Test
    void testDescriptionWithExits() {
        room.setExit("north", northRoom);
        room.setExit("east", new Room("East Room", "A room to the east"));
        String description = room.getDescription();
        assertTrue(description.contains("Exits:"));
        assertTrue(description.contains("north"));
        assertTrue(description.contains("east"));
    }
}
