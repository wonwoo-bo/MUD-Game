package com.mud.game;

public class Item {
    private String id;
    private String name;
    private String description;
    private int price;
    private int effectValue;
    private ItemType type;

    public enum ItemType {
        WEAPON,
        ARMOR,
        POTION,
        KEY
    }

    public Item(String id, String name, String description, int price, int effectValue, ItemType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.effectValue = effectValue;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isWeapon() {
        return type == ItemType.WEAPON;
    }

    public boolean isArmor() {
        return type == ItemType.ARMOR;
    }

    public boolean isPotion() {
        return type == ItemType.POTION;
    }

    public boolean isKey() {
        return type == ItemType.KEY;
    }
}
