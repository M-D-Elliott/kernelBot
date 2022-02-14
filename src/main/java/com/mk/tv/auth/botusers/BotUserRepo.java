package com.mk.tv.auth.botusers;

import com.fasterxml.jackson.core.type.TypeReference;
import jPlusLibs.jackson.JacksonRepo;
import jPlus.lang.callback.Retrievable;

import java.util.LinkedHashMap;

public class BotUserRepo extends JacksonRepo<BotUser> {
    public BotUserRepo() {
        this(path());
    }

    public BotUserRepo(String path) {
        super(path, new TypeReference<>() {
        }, BotUserRepo::newMap);
    }

    public BotUserRepo(Retrievable<LinkedHashMap<String, BotUser>> newInstance) {
        this(path(), newInstance);
    }

    public BotUserRepo(String path, Retrievable<LinkedHashMap<String, BotUser>> newInstance) {
        super(path, new TypeReference<>() {
        }, newInstance);
    }

    private static String path(){
        return "repos/users.txt";
    }

    private static LinkedHashMap<String, BotUser> newMap() {
        final LinkedHashMap<String, BotUser> ret = new LinkedHashMap<>();
        final String userName = "myDiscordUserName";
        ret.put(userName, new BotUser(userName));
        return ret;
    }
}
