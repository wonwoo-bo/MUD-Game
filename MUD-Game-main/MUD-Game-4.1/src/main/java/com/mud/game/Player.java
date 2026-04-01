package com.mud.game;

public class Player {
    private Room currentRoom;
    private int attackPower = 10;

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
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

    public void attack(String targetName) {
        Monster target = currentRoom.getMonster();

        if (target == null || !target.getName().equalsIgnoreCase(targetName)) {
            System.out.println("There is no " + targetName + " here to attack.");
            return;
        }

        System.out.println("You attack the " + target.getName() + "!");
        boolean hit = target.takeDamage(attackPower);

        if (hit) {
            System.out.println("Hit! You dealt " + attackPower + " damage.");
            if (target.isDead()) {
                System.out.println("You killed the " + target.getName() + "!");
                currentRoom.removeMonster();
            } else {
                System.out.println("The " + target.getName() + " has " + target.getHp() + " HP left.");
            }
        } else {
            System.out.println("Miss! The " + target.getName() + " dodged your attack.");
        }
    }
}