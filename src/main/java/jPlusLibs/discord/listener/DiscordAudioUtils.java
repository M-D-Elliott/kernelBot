package jPlusLibs.discord.listener;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DiscordAudioUtils {
    public static void copyBytes(byte[] source, byte[] target, int index) {
        for (byte b : source) target[index++] = b;
    }

    //***************************************************************//

    public static void saveAudioBliss(File file, byte[] bytes) {
        try (final ByteArrayInputStream bStream = new ByteArrayInputStream(bytes);
             final AudioInputStream aStream = new AudioInputStream(bStream, AudioReceiveHandler.OUTPUT_FORMAT, bytes.length)) {

            AudioSystem.write(aStream, AudioFileFormat.Type.WAVE, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //***************************************************************//

    private static StreamSpeechRecognizer streamRecognizer = null;

    public static String audioToTextBliss(File file) {
        try (final FileInputStream fs = new FileInputStream(file)) {
            initAudioToTextStream();
            streamRecognizer.startRecognition(fs);
            final SpeechResult result = streamRecognizer.getResult();
            streamRecognizer.stopRecognition();
            return result.getHypothesis();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }


    public static void initAudioToTextStream() throws IOException {
        if (streamRecognizer == null) {
            Configuration conf = new Configuration();
            conf.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            conf.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            conf.setGrammarPath("resource:/grammars");
            conf.setGrammarName("grammar");
            conf.setUseGrammar(true);
            conf.setSampleRate((int) AudioReceiveHandler.OUTPUT_FORMAT.getSampleRate());
            streamRecognizer = new StreamSpeechRecognizer(conf);
        }
    }

    public static String audioToTextBliss(byte[] bytes) {
        try (final ByteArrayInputStream bStream = new ByteArrayInputStream(bytes);
             final AudioInputStream aStream = new AudioInputStream(bStream, AudioReceiveHandler.OUTPUT_FORMAT, bytes.length)) {
            initAudioToTextStream();

            streamRecognizer.startRecognition(aStream);
            final SpeechResult result = streamRecognizer.getResult();

            streamRecognizer.stopRecognition();
            return result.getHypothesis();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "Error";
    }
}
