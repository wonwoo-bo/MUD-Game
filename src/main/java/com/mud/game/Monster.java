package com.mud.game;

public class Monster {
    private String name;
    private int health;
    private int damage;

    public Monster(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
