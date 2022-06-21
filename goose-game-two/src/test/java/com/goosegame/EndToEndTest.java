package com.goosegame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

class EndToEndTest {

    @BeforeEach
    public void setup() {
        main.main(new String[]{});
    }

    @AfterEach
    public void stop() throws InterruptedException {
        main.stop();
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
        with().body("{ \"name\": \"Paolo1\", \"nickname\": \"gooser1\"}").
                when().request("POST", "/players").then().statusCode(201);
        with().body("{ \"name\": \"Paolo2\", \"nickname\": \"gooser2\"}").
                when().request("POST", "/players").then().statusCode(201);
        with().body("{ \"name\": \"Paolo3\", \"nickname\": \"gooser3\"}").
                when().request("POST", "/players").then().statusCode(201);
        with().body("{ \"name\": \"Paolo4\", \"nickname\": \"gooser4\"}").
                when().request("POST", "/players").then().statusCode(201);

        with().body("{ \"name\": \"Paolo5\", \"nickname\": \"gooser5\"}").
                when().request("POST", "/players").then().statusCode(400).assertThat().body("error", equalTo("too many players already: Paolo1, Paolo2, Paolo3, Paolo4"));
    }


}