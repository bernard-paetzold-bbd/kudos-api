package com.bbdgrads.kudos_api.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LogEvents {
    public static final BiMap<Integer, String> events = HashBiMap.create();

    static {
        events.put(0, "CreateUser");
        events.put(1, "DeleteUser");
        events.put(2, "UpdateUserTeam");
        events.put(3, "SentKudo");
        events.put(4, "DeletedKudo");
        events.put(5, "CreatedTeam");
        events.put(6, "DeletedTeam");
        events.put(7, "ReadKudo");
        events.put(8, "FlaggedKudo");
        events.put(9, "UpdatedKudoMessage");
    }
}