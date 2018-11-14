package com.goosegame;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static spark.RequestResponseFactory.create;

public class AppTest {

    private MockHttpServletRequest fakeRequest;
    private MockHttpServletResponse fakeResponse;
    private App app;

    @Before
    public void setUp() throws Exception {
        fakeRequest = new MockHttpServletRequest();
        fakeResponse = new MockHttpServletResponse();
        app = new App();
    }

    @Test
    public void add_new_player() {
        String responseContent = addPlayer("Piero", "Gooser");

        assertEquals(201, fakeResponse.getStatus());
        JSONObject jsonResponse = new JSONObject(responseContent);
        assertEquals("Piero", jsonResponse.getString("name"));
        assertEquals("Gooser", jsonResponse.getString("nickname"));
        assertEquals(4, UUID.fromString(jsonResponse.getString("id")).version());
    }

    @Test
    public void forbid_player_with_same_nickname_to_join() {
        addPlayer("Piero", "Gooser");

        String response = addPlayer("Piero", "Gooser");

        assertEquals(400, fakeResponse.getStatus());
        assertEquals("nickname already taken: Gooser", new JSONObject(response).getString("error"));
    }

    @Test
    public void cannot_add_more_than_four_players_to_the_same_game() {
        addPlayer("Piero", "Gooser");
        assertEquals(201, fakeResponse.getStatus());

        addPlayer("Paolo", "Looser");
        assertEquals(201, fakeResponse.getStatus());

        addPlayer("Samantha", "FancyGamer");
        assertEquals(201, fakeResponse.getStatus());

        addPlayer("Scott", "WinnieThePooh");
        assertEquals(201, fakeResponse.getStatus());

        String response = addPlayer("William", "laggard");
        assertEquals(400, fakeResponse.getStatus());
        assertEquals("too many players already: Piero, Paolo, Samantha, Scott", new JSONObject(response).getString("error"));
    }

    private String addPlayer(String name, String nickname) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name).put("nickname", nickname);
        fakeRequest.setContent(jsonObject.toString().getBytes());

        return app.createPlayer(create(fakeRequest), create(fakeResponse));
    }
}