package com.mud.game;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Item> items;
    private int gold;

    public Inventory() {
        this.items = new HashMap<>();
        this.gold = 100;
    }

    public Inventory(int initialGold) {
        this.items = new HashMap<>();
        this.gold = initialGold;
    }

    public boolean addItem(Item item) {
        if (item == null || items.containsKey(item.getId())) {
            return false;
        }
        items.put(item.getId(), item);
        return true;
    }

    public boolean removeItem(String itemId) {
        if (!items.containsKey(itemId)) {
            return false;
        }
        items.remove(itemId);
        return true;
    }

    public Item getItem(String itemId) {
        return items.get(itemId);
    }

    public boolean hasItem(String itemId) {
        return items.containsKey(itemId);
    }

    public int getItemCount() {
        return items.size();
    }

    public Map<String, Item> getAllItems() {
        return new HashMap<>(items);
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    public boolean spendGold(int amount) {
        if (amount <= 0 || gold < amount) {
            return false;
        }
        this.gold -= amount;
        return true;
    }

    public boolean canAfford(int price) {
        return gold >= price;
    }

    public void clear() {
        items.clear();
    }
}
