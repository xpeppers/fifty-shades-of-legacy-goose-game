package com.goosegame;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AppTest {

  private static final String PLAYER_LUCA_GOOSER =
      "{ \"name\": \"Luca\", \"nickname\": \"gooser\"}";
  private static final String PLAYER_PAOLO_GOOSER =
      "{ \"name\": \"Paolo\", \"nickname\": \"gooser\"}";
  private static final int STATUS_CODE_201 = 201;
  private static final int STATUS_CODE_400 = 400;
  public static final String CREATE_PLAYER_ENDPOINT = "/players";

  @BeforeEach
  void setup() {
    main.main(new String[] {});
  }

  @AfterEach
  void teardown() throws InterruptedException {
    main.stop();
  }

  @Test
  void should_not_allow_users_with_the_same_name() {
    createPlayerRequest(STATUS_CODE_201, PLAYER_PAOLO_GOOSER);

    createPlayerRequest(STATUS_CODE_400, PLAYER_LUCA_GOOSER);
  }

  @Test
  void should_not_allow_more_than_4_players() {
    createPlayerRequest(STATUS_CODE_201, PLAYER_PAOLO_GOOSER);

    createPlayerRequest(STATUS_CODE_201, "{ \"name\": \"Luca\", \"nickname\": \"player2\"}");

    createPlayerRequest(STATUS_CODE_201, "{ \"name\": \"Alessandro\", \"nickname\": \"player3\"}");

    createPlayerRequest(STATUS_CODE_201, "{ \"name\": \"Piero\", \"nickname\": \"player4\"}");

    createPlayerRequest(STATUS_CODE_400, "{ \"name\": \"Ivan\", \"nickname\": \"player5\"}");
  }

  @Test
  void returnGameNotStarted() {
    String uuidPlayer =
        given().body(PLAYER_PAOLO_GOOSER).when().post(CREATE_PLAYER_ENDPOINT).body().path("uuid");
    given()
        .when()
        .post("/players/" + uuidPlayer + "/roll")
        .then()
        .statusCode(400)
        .body("error", Matchers.equalTo("Game not started, waiting for more players"));
  }
  
  private void createPlayerRequest(int statusCode201, String playerPaoloGooser) {
    given()
        .body(playerPaoloGooser)
        .when()
        .post(CREATE_PLAYER_ENDPOINT)
        .then()
        .statusCode(statusCode201);
  }
}
