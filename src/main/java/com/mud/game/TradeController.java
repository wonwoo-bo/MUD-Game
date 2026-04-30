package com.mud.game;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class TradeController {
    private Inventory playerInventory;
    private Map<String, Item> shopItems;

    public TradeController() {
        this.playerInventory = new Inventory(100);
        this.shopItems = createShopItems();
    }

    private Map<String, Item> createShopItems() {
        java.util.HashMap<String, Item> items = new java.util.HashMap<>();
        items.put("sword", new Item("sword", "Steel Sword", "A sharp steel sword", 50, 25, Item.ItemType.WEAPON));
        items.put("shield", new Item("shield", "Iron Shield", "A sturdy iron shield", 40, 20, Item.ItemType.ARMOR));
        items.put("potion", new Item("potion", "Health Potion", "Restores 30 health", 20, 30, Item.ItemType.POTION));
        items.put("key", new Item("key", "Dungeon Key", "Opens locked doors", 30, 0, Item.ItemType.KEY));
        return items;
    }

    @GetMapping("/shop")
    public ShopResponse getShopItems() {
        List<ItemInfo> items = new ArrayList<>();
        for (Item item : shopItems.values()) {
            items.add(new ItemInfo(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getType().toString()));
        }
        return new ShopResponse(items, playerInventory.getGold());
    }

    @PostMapping("/buy")
    public TradeResponse buyItem(@RequestBody BuyRequest request) {
        String itemId = request.getItemId();
        int quantity = request.getQuantity();

        if (quantity <= 0) {
            return new TradeResponse(false, "Invalid quantity", 0, playerInventory.getGold());
        }

        Item item = shopItems.get(itemId);
        if (item == null) {
            return new TradeResponse(false, "Item not found", 0, playerInventory.getGold());
        }

        int totalPrice = item.getPrice() * quantity;
        if (!playerInventory.canAfford(totalPrice)) {
            return new TradeResponse(false, "Not enough gold", 0, playerInventory.getGold());
        }

        for (int i = 0; i < quantity; i++) {
            if (!playerInventory.addItem(item)) {
                return new TradeResponse(false, "Inventory full", 0, playerInventory.getGold());
            }
        }

        playerInventory.spendGold(totalPrice);
        return new TradeResponse(true, "Purchased " + quantity + " " + item.getName(), playerInventory.getItemCount(), playerInventory.getGold());
    }

    @PostMapping("/sell")
    public TradeResponse sellItem(@RequestBody SellRequest request) {
        String itemId = request.getItemId();
        int quantity = request.getQuantity();

        if (quantity <= 0) {
            return new TradeResponse(false, "Invalid quantity", 0, playerInventory.getGold());
        }

        int sellPrice = 0;
        for (int i = 0; i < quantity; i++) {
            if (!playerInventory.hasItem(itemId)) {
                if (i == 0) {
                    return new TradeResponse(false, "Item not in inventory", 0, playerInventory.getGold());
                }
                break;
            }
            Item item = playerInventory.getItem(itemId);
            sellPrice += item.getPrice() / 2;
            playerInventory.removeItem(itemId);
        }

        playerInventory.addGold(sellPrice);
        return new TradeResponse(true, "Sold " + quantity + " items for " + sellPrice + " gold", playerInventory.getItemCount(), playerInventory.getGold());
    }

    @GetMapping("/inventory")
    public InventoryResponse getInventory() {
        List<ItemInfo> items = new ArrayList<>();
        for (Item item : playerInventory.getAllItems().values()) {
            items.add(new ItemInfo(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getType().toString()));
        }
        return new InventoryResponse(items, playerInventory.getGold());
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory inventory) {
        this.playerInventory = inventory;
    }

    public static class BuyRequest {
        private String itemId;
        private int quantity;

        public BuyRequest() {}

        public BuyRequest(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class SellRequest {
        private String itemId;
        private int quantity;

        public SellRequest() {}

        public SellRequest(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class ItemInfo {
        private String id;
        private String name;
        private String description;
        private int price;
        private String type;

        public ItemInfo(String id, String name, String description, int price, String type) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getPrice() {
            return price;
        }

        public String getType() {
            return type;
        }
    }

    public static class ShopResponse {
        private List<ItemInfo> items;
        private int playerGold;

        public ShopResponse(List<ItemInfo> items, int playerGold) {
            this.items = items;
            this.playerGold = playerGold;
        }

        public List<ItemInfo> getItems() {
            return items;
        }

        public int getPlayerGold() {
            return playerGold;
        }
    }

    public static class TradeResponse {
        private boolean success;
        private String message;
        private int inventoryCount;
        private int remainingGold;

        public TradeResponse(boolean success, String message, int inventoryCount, int remainingGold) {
            this.success = success;
            this.message = message;
            this.inventoryCount = inventoryCount;
            this.remainingGold = remainingGold;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public int getInventoryCount() {
            return inventoryCount;
        }

        public int getRemainingGold() {
            return remainingGold;
        }
    }

    public static class InventoryResponse {
        private List<ItemInfo> items;
        private int gold;

        public InventoryResponse(List<ItemInfo> items, int gold) {
            this.items = items;
            this.gold = gold;
        }

        public List<ItemInfo> getItems() {
            return items;
        }

        public int getGold() {
            return gold;
        }
    }
}
