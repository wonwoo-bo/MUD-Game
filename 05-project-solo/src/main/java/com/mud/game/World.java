package com.mud.game;

public class World {
    private Room startingRoom;

    public World() {
        createWorld();
    }

    private void createWorld() {
        Room start = new Room("Start Room", "A small, dimly lit room with stone walls.");
        Room forest = new Room("Forest", "A dense forest with tall trees blocking the sunlight.");
        Room cave = new Room("Cave", "A dark, damp cave. Water drips from the ceiling.");

        // Connect rooms
        start.setExit("north", forest);
        forest.setExit("south", start);
        
        forest.setExit("east", cave);
        cave.setExit("west", forest);

        this.startingRoom = start;
    }

    public Room getStartingRoom() {
        return startingRoom;
    }
}
