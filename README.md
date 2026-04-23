# Java MUD Game (Sprint 1)

## Overview
This is a text-based adventure game engine developed for Sprint 1, featuring both console and web interfaces.

## System Architecture

```
+-------------------+
|   Game Application   |
+-------------------+
| - GameController   |
| - Game            |
+-------------------+
          |
          v
+-------------------+
|   Core Entities    |
+-------------------+
| - Player          |
| - Room            |
| - Monster         |
| - World           |
+-------------------+
          |
          v
+-------------------+
|   Interfaces       |
+-------------------+
| - Console (Main)  |
| - Web (REST API)  |
+-------------------+
```

## Core Business Modules

### Game
- **Responsibility**: Main game logic, command processing, and game state management
- **Key Features**: Command parsing, game loop, player interaction

### Player
- **Responsibility**: Manages player state and actions
- **Key Features**: Movement, combat, health management

### Room
- **Responsibility**: Represents game locations and connections
- **Key Features**: Room descriptions, exits management, monster containment

### Monster
- **Responsibility**: Represents hostile creatures
- **Key Features**: Health management, combat

### World
- **Responsibility**: Creates and manages the game world
- **Key Features**: Room creation, world initialization

### GameController
- **Responsibility**: Handles web API requests
- **Key Features**: REST endpoints, JSON serialization

## Local Development Environment Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Setup Steps
1. **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd MUD-Game-develop
    ```

2. **Build the project**:
    ```bash
    mvn clean package
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```
    
    The application will start on `http://localhost:8080`

4. **Run tests**:
    ```bash
    mvn test
    ```

## How to Run

### Console Mode
1. **Compile**:
    ```bash
    mkdir bin
    javac -d bin src/main/java/com/mud/game/*.java
    ```
2. **Run**:
    ```bash
    java -cp bin com.mud.game.Main
    ```

### Web Mode
1. **Run the Spring Boot application**:
    ```bash
    mvn spring-boot:run
    ```
2. **Open your browser**:
    Navigate to `http://localhost:8080`

## Features (MVP)
- **Command Parser**: Supports `go`, `look`, `help`, `quit`, `attack`, `status`.
- **World**: A simple 3-room map (Start, Forest, Cave).
- **Navigation**: Move between rooms using cardinal directions.
- **Combat**: Fight monsters in rooms.
- **Status**: View player health and location.
- **Web Interface**: Play the game through a web browser.

## Project Structure
- `src/main/java/com/mud/game`: Source code.
- `src/test/java/com/mud/game`: Unit tests.
- `docs/`: Documentation (AI Analysis, User Stories).
- `src/main/resources`: Static files and configuration.

## CI/CD Pipeline
The project includes a CI pipeline that automatically runs tests on each commit. The pipeline is configured in `.github/workflows/ci.yml`.
