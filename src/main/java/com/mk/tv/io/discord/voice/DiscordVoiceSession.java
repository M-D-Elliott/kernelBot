package com.mk.tv.io.discord.voice;

import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

import static jPlusLibs.com.edu.sphinx.SphinxUtils.audioToTextBliss;

public class DiscordVoiceSession {

    protected final byte[] building = new byte[MAX_SEGMENTS * BYTES_PER_SEGMENT];
    protected final User user;

    protected long expiration = System.currentTimeMillis();
    protected int segmentIndex = 0;

    public DiscordVoiceSession(User user) {
        this.user = user;
    }

    //***************************************************************//

    public void store(byte[] newData) {
        SphinxUtils.copyBytes(newData, building, BYTES_PER_SEGMENT * segmentIndex++);
    }

    public String result() {
        final byte[] portion = Arrays.copyOfRange(building, 0, segmentIndex * BYTES_PER_SEGMENT);
        return audioToTextBliss(portion, AudioReceiveHandler.OUTPUT_FORMAT);
    }


    public boolean isFull() {
        return segmentIndex >= MAX_SEGMENTS;
    }

    public boolean check() {
        expiration = System.currentTimeMillis() + 100;
        return segmentIndex % 10 == 0;
    }

    public boolean isActive() {
        return segmentIndex > 0;
    }

    public void end() {
        segmentIndex = 0;
    }

    //***************************************************************//

    public static final int BYTES_PER_SEGMENT = 3840;
    private static final int MAX_SEGMENTS = 2000 / 20;
}
