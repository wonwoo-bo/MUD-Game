package com.mud.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;
    private Item sword;
    private Item potion;

    @BeforeEach
    void setUp() {
        inventory = new Inventory(100);
        sword = new Item("sword", "Steel Sword", "A sharp steel sword", 50, 25, Item.ItemType.WEAPON);
        potion = new Item("potion", "Health Potion", "Restores 30 health", 20, 30, Item.ItemType.POTION);
    }

    @Test
    void testInventoryInitialization() {
        assertEquals(100, inventory.getGold());
        assertEquals(0, inventory.getItemCount());
        assertTrue(inventory.getAllItems().isEmpty());
    }

    @Test
    void testAddItem() {
        assertTrue(inventory.addItem(sword));
        assertEquals(1, inventory.getItemCount());
        assertTrue(inventory.hasItem("sword"));
        assertEquals(sword, inventory.getItem("sword"));
    }

    @Test
    void testAddDuplicateItem() {
        inventory.addItem(sword);
        assertFalse(inventory.addItem(sword));
        assertEquals(1, inventory.getItemCount());
    }

    @Test
    void testAddNullItem() {
        assertFalse(inventory.addItem(null));
        assertEquals(0, inventory.getItemCount());
    }

    @Test
    void testRemoveItem() {
        inventory.addItem(sword);
        assertTrue(inventory.removeItem("sword"));
        assertEquals(0, inventory.getItemCount());
        assertFalse(inventory.hasItem("sword"));
    }

    @Test
    void testRemoveNonExistentItem() {
        assertFalse(inventory.removeItem("nonexistent"));
    }

    @Test
    void testGetNonExistentItem() {
        assertNull(inventory.getItem("nonexistent"));
    }

    @Test
    void testAddGold() {
        inventory.addGold(50);
        assertEquals(150, inventory.getGold());
    }

    @Test
    void testAddNegativeGold() {
        inventory.addGold(-30);
        assertEquals(100, inventory.getGold());
    }

    @Test
    void testSpendGold() {
        assertTrue(inventory.spendGold(30));
        assertEquals(70, inventory.getGold());
    }

    @Test
    void testSpendMoreGoldThanAvailable() {
        assertFalse(inventory.spendGold(150));
        assertEquals(100, inventory.getGold());
    }

    @Test
    void testSpendZeroGold() {
        assertFalse(inventory.spendGold(0));
        assertEquals(100, inventory.getGold());
    }

    @Test
    void testSpendNegativeGold() {
        assertFalse(inventory.spendGold(-10));
        assertEquals(100, inventory.getGold());
    }

    @Test
    void testCanAfford() {
        assertTrue(inventory.canAfford(50));
        assertTrue(inventory.canAfford(100));
        assertFalse(inventory.canAfford(150));
    }

    @Test
    void testGetAllItems() {
        inventory.addItem(sword);
        inventory.addItem(potion);
        Map<String, Item> items = inventory.getAllItems();
        assertEquals(2, items.size());
        assertTrue(items.containsKey("sword"));
        assertTrue(items.containsKey("potion"));
    }

    @Test
    void testClear() {
        inventory.addItem(sword);
        inventory.addItem(potion);
        inventory.clear();
        assertEquals(0, inventory.getItemCount());
        assertTrue(inventory.getAllItems().isEmpty());
    }

    @Test
    void testInventoryWithInitialGold() {
        Inventory customInventory = new Inventory(500);
        assertEquals(500, customInventory.getGold());
    }
}
