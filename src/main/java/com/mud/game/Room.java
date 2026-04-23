package com.mud.game;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private Monster monster;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
    }

    public void setExit(String direction, Room room) {
        exits.put(direction.toLowerCase(), room);
    }

    public Room getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are in ").append(name).append(".\n");
        sb.append(description).append("\n");
        if (monster != null && monster.isAlive()) {
            sb.append("There is a " + monster.getName() + " here!\n");
        }
        sb.append("Exits: ");
        if (exits.isEmpty()) {
            sb.append("None");
        } else {
            for (String direction : exits.keySet()) {
                sb.append(direction).append(" ");
            }
        }
        return sb.toString();
    }
}
