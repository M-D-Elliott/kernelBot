package jPlusLibs.discord.listener.voice;

import jPlusLibs.com.edu.sphinx.SphinxUtils;

public class VoiceSession {

    public final byte[] building = new byte[MAX_SEGMENTS * BYTES_PER_SEGMENT];
    public int segmentIndex = 0;

    //***************************************************************//

    public void store(byte[] newData) {
        SphinxUtils.copyBytes(newData, building, BYTES_PER_SEGMENT * segmentIndex++);
    }

    public byte[] read() {
        segmentIndex = 0;
        return building;
    }

    public boolean isFull() {
        return segmentIndex >= MAX_SEGMENTS;
    }

    //***************************************************************//

    public static final int BYTES_PER_SEGMENT = 3840;
    private static final int MAX_SEGMENTS = 1000 / 20;
}
