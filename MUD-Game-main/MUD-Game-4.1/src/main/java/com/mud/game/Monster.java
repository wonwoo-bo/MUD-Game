package com.mud.game;

import java.util.Random;

public class Monster {
    private String name;
    private int hp;
    private Random random;

    public Monster(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.random = new Random();
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public boolean takeDamage(int damage) {
        if (random.nextInt(100) < 30) {
            return false;
        }
        this.hp -= damage;
        return true;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }
}