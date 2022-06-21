package com.goosegame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.*;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        App app = new App();
        port(8080);
        before((req, res) -> logger.info("Request: {} {} - body: {}", req.requestMethod(), req.uri(), req.body()));
        after((req, res) -> logger.info("Response {} - body: {}", res.status(), res.body()));

        post("/players", app::createPlayer);
        post("/players/:id/roll", app::roll);
    }

    public static void stop() throws InterruptedException {
        Spark.stop();
        Thread.sleep(2000L);
    }

}
