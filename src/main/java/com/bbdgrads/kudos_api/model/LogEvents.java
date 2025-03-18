package com.bbdgrads.kudos_api.model;

import java.util.HashMap;
import java.util.Map;

public class LogEvents {

    public static final Map<Integer, String> events = new HashMap<>();

    static {
        events.put(0, "CreateUser");
        events.put(1, "DeleteUser");
        events.put(2, "UpdateUserTeam");
        events.put(3, "SentKudo");
        events.put(4, "DeletedKudo");
        events.put(5, "CreatedTeam");
        events.put(6, "DeletedTeam");
    }
}