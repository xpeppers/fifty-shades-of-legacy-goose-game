package com.goosegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AppTest {

    @BeforeEach
    public void setUp() {
        com.goosegame.main.main(new String[]{});
    }

    @Test
    public void can_add_four_players_with_distinct_nicknames() {
        String ivanId = addPlayer("Ivan", "slow duck");
        String davideId = addPlayer("Davide", "smart duck");
        String edoId = addPlayer("Edo", "cool duck");
        addPlayer("Kent", "wise duck");

        given()
                .body("{\"name\": \"" + "Davide" + "\", \"nickname\": \"" + "pippo" + "\"}")
                .when()
                .post("/players")
                .then()
                .statusCode(400);
    }

    @Test
    public void cannot_add_player_with_same_nickname_twice() {
        addPlayer("Ivan", "pippo");

        given()
                .body("{\"name\": \"" + "Davide" + "\", \"nickname\": \"" + "pippo" + "\"}")
                .when()
                .post("/players")
                .then()
                .statusCode(400);
    }

    private String addPlayer(String name, String nickname) {
        String id = given()
                .body("{\"name\": \"" + name + "\", \"nickname\": \"" + nickname + "\"}")
                .when()
                .post("/players")
                .then()
                .statusCode(201)
                .body(containsString("\"name\": \"" + name + "\", \"nickname\": \"" + nickname + "\""))
                .extract().path("id");

        return id;
    }
}