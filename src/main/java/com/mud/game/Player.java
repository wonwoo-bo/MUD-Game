package com.mud.game;

public class Player {
    private Room currentRoom;

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
}
