package jPlusLibs.discord.listener.voice;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.entities.User;

import static jPlusLibs.com.edu.sphinx.SphinxUtils.audioToTextBliss;

public class VoiceSession {

    protected final byte[] building = new byte[MAX_SEGMENTS * BYTES_PER_SEGMENT];
    protected final User user;

    protected long lastReceivedTime = System.currentTimeMillis();
    protected int segmentIndex = 0;
    protected int receivedCount = 0;
    protected final int sleepTime = 100;

    protected Thread th = null;

    protected final Runnable waitAndEndRunnable = () -> {
        try {
            Thread.sleep(100);
            if (lastReceivedTime + sleepTime - 1 < System.currentTimeMillis())
                this.end();
            th = null;
        } catch (InterruptedException ignored) {
        }
    };

    private Receivable1<IAPIWrapper> onEnd = (api) -> {
    };

    public VoiceSession(User user) {
        this.user = user;
    }

    //***************************************************************//

    public void store(byte[] newData) {
        SphinxUtils.copyBytes(newData, building, BYTES_PER_SEGMENT * segmentIndex++);
        lastReceivedTime = System.currentTimeMillis();
        receivedCount++;
    }

    public void end() {
        final String voiceCommand = audioToTextBliss(building, AudioReceiveHandler.OUTPUT_FORMAT);
        System.out.println(voiceCommand);

        final IAPIWrapper api = new VoiceChannelOutWrapper(user, voiceCommand);
        onEnd.receive(api);

        segmentIndex = 0;
    }


    public boolean isFull() {
        return segmentIndex >= MAX_SEGMENTS;
    }

    public void waitAndEnd() {
        if(th != null) th.interrupt();
        th = new Thread(waitAndEndRunnable);
        th.setDaemon(true);
        th.start();
    }

    //***************************************************************//

    public void setOnEnd(Receivable1<IAPIWrapper> onEnd) {
        this.onEnd = onEnd;
    }

    //***************************************************************//

    public static final int BYTES_PER_SEGMENT = 3840;
    private static final int MAX_SEGMENTS = 2000 / 20;
}
