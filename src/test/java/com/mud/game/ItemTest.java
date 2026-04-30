package com.mud.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    private Item sword;
    private Item potion;

    @BeforeEach
    void setUp() {
        sword = new Item("sword", "Steel Sword", "A sharp steel sword", 50, 25, Item.ItemType.WEAPON);
        potion = new Item("potion", "Health Potion", "Restores 30 health", 20, 30, Item.ItemType.POTION);
    }

    @Test
    void testItemInitialization() {
        assertEquals("sword", sword.getId());
        assertEquals("Steel Sword", sword.getName());
        assertEquals("A sharp steel sword", sword.getDescription());
        assertEquals(50, sword.getPrice());
        assertEquals(25, sword.getEffectValue());
        assertEquals(Item.ItemType.WEAPON, sword.getType());
    }

    @Test
    void testItemTypeCheck() {
        assertTrue(sword.isWeapon());
        assertFalse(sword.isArmor());
        assertFalse(sword.isPotion());
        assertFalse(sword.isKey());

        assertTrue(potion.isPotion());
        assertFalse(potion.isWeapon());
        assertFalse(potion.isArmor());
        assertFalse(potion.isKey());
    }

    @Test
    void testItemGetters() {
        assertEquals("sword", sword.getId());
        assertEquals("Steel Sword", sword.getName());
        assertEquals(50, sword.getPrice());
        assertEquals(25, sword.getEffectValue());
    }
}
