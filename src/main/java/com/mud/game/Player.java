package com.mud.game;

public class Player {
    private Room currentRoom;
    private int health;
    private int damage;

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
        this.health = 100;
        this.damage = 20;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You go " + direction + ".");
            System.out.println(currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }
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

    public void attack(Monster monster) {
        if (monster != null && monster.isAlive()) {
            monster.takeDamage(damage);
            System.out.println("You attack the " + monster.getName() + " for " + damage + " damage!");
            if (!monster.isAlive()) {
                System.out.println("You killed the " + monster.getName() + "!");
                currentRoom.setMonster(null);
            } else {
                int monsterDamage = monster.getDamage();
                takeDamage(monsterDamage);
                System.out.println("The " + monster.getName() + " attacks you for " + monsterDamage + " damage!");
                if (!isAlive()) {
                    System.out.println("You died!");
                }
            }
        } else {
            System.out.println("There's nothing to attack here.");
        }
    }
}
