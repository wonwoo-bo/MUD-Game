package com.mud.game;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private Game game;
    private Player player;

    public GameController() {
        // 初始化游戏
        game = new Game();
        // 通过反射获取player实例
        try {
            java.lang.reflect.Field playerField = Game.class.getDeclaredField("player");
            playerField.setAccessible(true);
            player = (Player) playerField.get(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/command")
    public GameResponse processCommand(@RequestBody CommandRequest request) {
        List<String> output = new ArrayList<>();
        String command = request.getCommand();

        // 处理命令并收集输出
        // 这里需要修改Game类，使其能够捕获输出
        // 暂时使用模拟响应
        simulateCommandResponse(command, output);

        // 获取玩家状态
        int health = player.getHealth();
        int damage = player.getDamage();
        String roomName = player.getCurrentRoom().getDescription().split("\\n")[0].substring(8);

        return new GameResponse(output, health, damage, roomName);
    }

    @GetMapping("/status")
    public GameStatus getStatus() {
        int health = player.getHealth();
        int damage = player.getDamage();
        String roomName = player.getCurrentRoom().getDescription().split("\\n")[0].substring(8);
        return new GameStatus(health, damage, roomName);
    }

    private void simulateCommandResponse(String command, List<String> output) {
        // 这里应该调用game.processCommand()并捕获输出
        // 暂时使用模拟响应
        String[] parts = command.trim().toLowerCase().split("\\s+");
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case "go":
            case "move":
                if (arg.isEmpty()) {
                    output.add("Go where? (north, south, east, west)");
                } else {
                    player.move(arg);
                    // 模拟输出
                    output.add("You go " + arg + ".");
                    output.add(player.getCurrentRoom().getDescription());
                }
                break;
            case "look":
                output.add(player.getCurrentRoom().getDescription());
                break;
            case "attack":
                player.attack(player.getCurrentRoom().getMonster());
                // 模拟输出
                Monster monster = player.getCurrentRoom().getMonster();
                if (monster != null && monster.isAlive()) {
                    output.add("You attack the " + monster.getName() + " for " + player.getDamage() + " damage!");
                    monster.takeDamage(player.getDamage());
                    if (!monster.isAlive()) {
                        output.add("You killed the " + monster.getName() + "!");
                        player.getCurrentRoom().setMonster(null);
                    } else {
                        int monsterDamage = monster.getDamage();
                        player.takeDamage(monsterDamage);
                        output.add("The " + monster.getName() + " attacks you for " + monsterDamage + " damage!");
                        if (!player.isAlive()) {
                            output.add("You died!");
                        }
                    }
                } else {
                    output.add("There's nothing to attack here.");
                }
                break;
            case "status":
                output.add("Player Status:");
                output.add("Health: " + player.getHealth() + "/100");
                output.add("Damage: " + player.getDamage());
                output.add("Current Room: " + player.getCurrentRoom().getDescription().split("\\n")[0].substring(8));
                break;
            case "help":
                output.add("Available commands:");
                output.add("  go [direction] - Move: one-line e.g. 'go north' or two-step:");
                output.add("                   type 'go' then enter direction (north, south, east, west)");
                output.add("  look           - Look around the current room");
                output.add("  attack         - Attack the monster in the current room");
                output.add("  status         - Show your current status");
                output.add("  help           - Show this help message");
                output.add("  quit           - Exit the game");
                break;
            case "quit":
            case "exit":
                output.add("Goodbye!");
                break;
            default:
                output.add("I don't understand that command.");
        }
    }

    // 请求和响应类
    public static class CommandRequest {
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    public static class GameResponse {
        private List<String> output;
        private int health;
        private int damage;
        private String room;

        public GameResponse(List<String> output, int health, int damage, String room) {
            this.output = output;
            this.health = health;
            this.damage = damage;
            this.room = room;
        }

        public List<String> getOutput() {
            return output;
        }

        public int getHealth() {
            return health;
        }

        public int getDamage() {
            return damage;
        }

        public String getRoom() {
            return room;
        }
    }

    public static class GameStatus {
        private int health;
        private int damage;
        private String room;

        public GameStatus(int health, int damage, String room) {
            this.health = health;
            this.damage = damage;
            this.room = room;
        }

        public int getHealth() {
            return health;
        }

        public int getDamage() {
            return damage;
        }

        public String getRoom() {
            return room;
        }
    }
}
