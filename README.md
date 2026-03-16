# Java MUD Game (Sprint 1)

## Overview
This is a text-based adventure game engine developed for Sprint 1.

## How to Run
1.  **Compile**:
    ```bash
    mkdir bin
    javac -d bin src/main/java/com/mud/game/*.java
    ```
2.  **Run**:
    ```bash
    java -cp bin com.mud.game.Main
    ```

## Features (MVP)
- **Command Parser**: Supports `go`, `look`, `help`, `quit`.
- **World**: A simple 3-room map (Start, Forest, Cave).
- **Navigation**: Move between rooms using cardinal directions.

## Project Structure
- `src/main/java/com/mud/game`: Source code.
- `docs/`: Documentation (AI Analysis, User Stories).
