package com.mk.tv.auth.botusers;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;

public class BotUser {
    public String username;
    public String password = "";
    public String welcome = "Hey friend!";

    public BotUser() {
    }

    public BotUser(String userName) {
        this.username = userName;
    }

    @Override
    public String toString() {
        return username;
    }

    //***************************************************************//

    public static TypeReference<LinkedHashMap<String, BotUser>> typeRef() {
        return new TypeReference<>() {
        };
    }
}
