package com.mk.tv.auth.botusers;

import com.mk.tv.kernel.generic.JacksonRepo;
import jPlus.lang.callback.Retrievable;

import java.util.LinkedHashMap;

public class BotUserRepo extends JacksonRepo<BotUser> {
    public BotUserRepo() {
        this("repos/users.txt");
    }

    public BotUserRepo(String path) {
        super(path, BotUser.typeRef(), BotUserRepo::newUserMap);
    }

    public BotUserRepo(Retrievable<LinkedHashMap<String, BotUser>> newInstance) {
        this("repos/users.txt", newInstance);
    }

    public BotUserRepo(String path, Retrievable<LinkedHashMap<String, BotUser>> newInstance) {
        super(path, BotUser.typeRef(), newInstance);
    }

    private static LinkedHashMap<String, BotUser> newUserMap() {
        final LinkedHashMap<String, BotUser> ret = new LinkedHashMap<>();
        final String userName = "myDiscordUserName";
        ret.put(userName, new BotUser(userName));
        return ret;
    }
}
