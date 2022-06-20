package com.goosegame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.servlet.SparkApplication;

import static spark.Spark.*;

public class main {
    private static Logger logger = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) {
        App app = new App();
        port(8080);
        before((req, res) -> logger.info("Request: {} {} - body: {}", req.requestMethod(), req.uri(), req.body()));
        after((req, res) -> logger.info("Response {} - body: {}", res.status(), res.body()));

        post("/players", (req, res) -> app.createPlayer(req, res));
        post("/players/:id/roll", (req, res) -> app.roll(req, res));
    }

    public static void stop() throws InterruptedException {
        Spark.stop();
        Thread.sleep(2000L);
    }

}
