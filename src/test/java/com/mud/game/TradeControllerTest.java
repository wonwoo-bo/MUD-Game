package com.mud.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeController tradeController;

    @Test
    void testGetShopItems() throws Exception {
        when(tradeController.getShopItems()).thenReturn(
            new TradeController.ShopResponse(Collections.emptyList(), 100)
        );

        mockMvc.perform(get("/api/game/shop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.playerGold").value(100));
    }

    @Test
    void testBuyItemSuccess() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(true, "Purchased 1 Health Potion", 1, 80)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.remainingGold").value(80));
    }

    @Test
    void testBuyItemNotEnoughGold() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Not enough gold", 0, 5)
        );

        String requestJson = "{\"itemId\":\"sword\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Not enough gold"));
    }

    @Test
    void testBuyInvalidQuantity() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Invalid quantity", 0, 100)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":0}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid quantity"));
    }

    @Test
    void testBuyItemNotFound() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Item not found", 0, 100)
        );

        String requestJson = "{\"itemId\":\"nonexistent\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Item not found"));
    }

    @Test
    void testSellItemSuccess() throws Exception {
        when(tradeController.sellItem(any(TradeController.SellRequest.class))).thenReturn(
            new TradeController.TradeResponse(true, "Sold 1 items for 10 gold", 0, 110)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/sell")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testSellItemNotInInventory() throws Exception {
        when(tradeController.sellItem(any(TradeController.SellRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Item not in inventory", 0, 100)
        );

        String requestJson = "{\"itemId\":\"nonexistent\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/sell")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Item not in inventory"));
    }

    @Test
    void testGetInventory() throws Exception {
        when(tradeController.getInventory()).thenReturn(
            new TradeController.InventoryResponse(Collections.emptyList(), 100)
        );

        mockMvc.perform(get("/api/game/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.gold").value(100));
    }

    @Test
    void testBuyMultipleItems() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(true, "Purchased 3 Health Potion", 3, 40)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":3}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.remainingGold").value(40));
    }

    @Test
    void testSellPartialQuantity() throws Exception {
        when(tradeController.sellItem(any(TradeController.SellRequest.class))).thenReturn(
            new TradeController.TradeResponse(true, "Sold 1 items for 10 gold", 1, 110)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":1}";

        mockMvc.perform(post("/api/game/sell")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testBuyNegativeQuantity() throws Exception {
        when(tradeController.buyItem(any(TradeController.BuyRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Invalid quantity", 0, 100)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":-1}";

        mockMvc.perform(post("/api/game/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid quantity"));
    }

    @Test
    void testSellNegativeQuantity() throws Exception {
        when(tradeController.sellItem(any(TradeController.SellRequest.class))).thenReturn(
            new TradeController.TradeResponse(false, "Invalid quantity", 0, 100)
        );

        String requestJson = "{\"itemId\":\"potion\",\"quantity\":-1}";

        mockMvc.perform(post("/api/game/sell")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid quantity"));
    }

    @Test
    void testShopContainsAllItems() throws Exception {
        List<TradeController.ItemInfo> items = new ArrayList<>();
        items.add(new TradeController.ItemInfo("sword", "Steel Sword", "A sharp steel sword", 50, "WEAPON"));
        items.add(new TradeController.ItemInfo("shield", "Iron Shield", "A sturdy iron shield", 40, "ARMOR"));
        items.add(new TradeController.ItemInfo("potion", "Health Potion", "Restores 30 health", 20, "POTION"));
        items.add(new TradeController.ItemInfo("key", "Dungeon Key", "Opens locked doors", 30, "KEY"));

        when(tradeController.getShopItems()).thenReturn(
            new TradeController.ShopResponse(items, 100)
        );

        mockMvc.perform(get("/api/game/shop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4));
    }
}
