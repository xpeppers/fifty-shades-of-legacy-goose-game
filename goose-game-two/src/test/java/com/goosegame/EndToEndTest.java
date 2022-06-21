package com.goosegame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

class EndToEndTest {

    @BeforeEach
    public void setup() {
        Main.main(new String[]{});
    }

    @AfterEach
    public void stop() throws InterruptedException {
        Main.stop();
    }

    @Test
    public void newPlayerStatusCode201() {
        with().body("{ \"name\": \"Paolo\", \"nickname\": \"gooser\"}").
                when().request("POST", "/players").then().statusCode(201)
                .assertThat().body("name", equalTo("Paolo"))
                .body("nickname", equalTo("gooser"));
    }

    @Test
    public void maxFourPlayerAreAllowed() {
        insertNewPlayer("Paolo1","gooser1");
        insertNewPlayer("Paolo2","gooser2");
        insertNewPlayer("Paolo3","gooser3");
        insertNewPlayer("Paolo4","gooser4");

        with().body("{ \"name\": \"Paolo5\", \"nickname\": \"gooser5\"}").
                when().request("POST", "/players").then().statusCode(400)
                .assertThat()
                    .body("error", equalTo("too many players already: Paolo1, Paolo2, Paolo3, Paolo4"));
    }

    private void insertNewPlayer(String nome, String nickname) {
        with().body("{ \"name\": \""+nome+"\", \"nickname\": \""+nickname+"\"}").
                when().request("POST", "/players")
                .then().statusCode(201);;
    }

    @Test
    public void should_dont_allow_two_players_with_some_nickname() {
        insertNewPlayer("Paolo1","gooser1");

        with().body("{ \"name\": \"Paolo1\", \"nickname\": \"gooser1\"}").
                when().request("POST", "/players").then()
                .statusCode(400).assertThat()
                .body("error", equalTo("nickname already taken: gooser1"));
    }


}