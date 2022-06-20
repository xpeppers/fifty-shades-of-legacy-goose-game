package com.goosegame;

import org.json.JSONObject;

import java.util.Random;

public class DiceRollerService {

    public DiceRollerService() {
        // This service returns something like this:
        // {"success":true,"dice":[{"value":5,"type":"d6"},{"value":1,"type":"d6"}]}
    }

    public JSONObject roll() {
        Random random = new Random();
        int firstDie = random.nextInt(6) + 1;
        int secondDie = random.nextInt(6) + 1;
        String result = "{\"success\":true,\"dice\":[{\"value\":" + firstDie + ",\"type\":\"d6\"},{\"value\":" + secondDie + ",\"type\":\"d6\"}]}";
        return new JSONObject(result);
    }
}
