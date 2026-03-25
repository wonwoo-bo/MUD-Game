package com.mud.game;

import java.util.Scanner;

public class Game {
    private boolean running;
    private Scanner scanner;
    private Player player;
    private World world;
    private boolean awaitingDirection;

    public Game() {
        this.running = true;
        this.scanner = new Scanner(System.in);
        this.world = new World();
        this.player = new Player(world.getStartingRoom());
    }

    public void start() {
        System.out.println("Welcome to the MUD Game!");
        System.out.println("Type 'help' for a list of commands.");
        System.out.println(player.getCurrentRoom().getDescription());

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine();
            processCommand(input);
        }
        
        System.out.println("Goodbye!");
        scanner.close();
    }

    private void processCommand(String input) {
        if (awaitingDirection) {
            String dir = input.trim().toLowerCase();
            if (dir.isEmpty()) {
                System.out.println("Please enter a direction (north, south, east, west).");
            } else {
                player.move(dir);
                awaitingDirection = false;
            }
            return;
        }

        String[] parts = input.trim().toLowerCase().split("\\s+");
        if (parts.length == 0) return;

        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "quit":
            case "exit":
                running = false;
                break;
            case "help":
                showHelp();
                break;
            case "look":
                System.out.println(player.getCurrentRoom().getDescription());
                break;
            case "go":
            case "move":
                if (argument.isEmpty()) {
                    System.out.println("Go where? (north, south, east, west)");
                    awaitingDirection = true;
                } else {
                    player.move(argument);
                }
                break;
            default:
                System.out.println("I don't understand that command.");
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  go [direction] - Move: one-line e.g. 'go north' or two-step:");
        System.out.println("                   type 'go' then enter direction (north, south, east, west)");
        System.out.println("  look           - Look around the current room");
        System.out.println("  help           - Show this help message");
        System.out.println("  quit           - Exit the game");
    }
}
