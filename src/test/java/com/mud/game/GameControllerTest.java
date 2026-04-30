package com.mud.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProcessGoCommand() throws Exception {
        String requestJson = "{\"command\":\"go north\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").isArray());
    }

    @Test
    void testProcessLookCommand() throws Exception {
        String requestJson = "{\"command\":\"look\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").isArray());
    }

    @Test
    void testProcessAttackCommand() throws Exception {
        String requestJson = "{\"command\":\"attack\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").isArray());
    }

    @Test
    void testProcessHelpCommand() throws Exception {
        String requestJson = "{\"command\":\"help\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").isArray());
    }

    @Test
    void testProcessStatusCommand() throws Exception {
        String requestJson = "{\"command\":\"status\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health").exists())
                .andExpect(jsonPath("$.damage").exists())
                .andExpect(jsonPath("$.room").exists());
    }

    @Test
    void testProcessUnknownCommand() throws Exception {
        String requestJson = "{\"command\":\"unknown\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output[0]").value("I don't understand that command."));
    }

    @Test
    void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/game/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health").exists())
                .andExpect(jsonPath("$.damage").exists())
                .andExpect(jsonPath("$.room").exists());
    }

    @Test
    void testGoCommandWithInvalidDirection() throws Exception {
        String requestJson = "{\"command\":\"go west\"}";

        mockMvc.perform(post("/api/game/command")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").isArray());
    }
}
