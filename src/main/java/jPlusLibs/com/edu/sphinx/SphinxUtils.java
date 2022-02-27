package jPlusLibs.com.edu.sphinx;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import jPlus.util.lang.StringUtils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class SphinxUtils {
    public static void copyBytes(byte[] source, byte[] target, int index) {
        for (byte b : source) target[index++] = b;
    }

    //***************************************************************//

    public static void writeAudioBliss(File file, byte[] bytes, AudioFormat audioFormat) {
        try (final ByteArrayInputStream bStream = new ByteArrayInputStream(bytes);
             final AudioInputStream aStream = new AudioInputStream(bStream, audioFormat, bytes.length)) {
            AudioSystem.write(aStream, AudioFileFormat.Type.WAVE, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //***************************************************************//

    private static StreamSpeechRecognizer streamRecognizer = null;
    public static Configuration conf = new Configuration();

    public static void disableSphinxLogs() {
        final String propName = "java.util.logging.config.file";
        if (System.getProperty(propName) == null)
            System.setProperty(propName, "ignoreAllSphinx4LoggingOutput");
    }

    public static void initAudioToTextStream() throws IOException {
        if (streamRecognizer == null) {
            conf.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            conf.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            if (StringUtils.isNullWhiteSpaceOrEmpty(conf.getGrammarPath())) {
                conf.setGrammarPath("resource:/grammars");
            }
            conf.setGrammarName("grammar");
            conf.setUseGrammar(true);
            streamRecognizer = new StreamSpeechRecognizer(conf);
        }
    }

    /**
     * Make sure to use file extension .wav.
     *
     * @param file
     * @return
     */
    public static String audioToTextBliss(File file) {
        try (final FileInputStream fs = new FileInputStream(file)) {
            return audioToText(fs);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }

    public static String audioToTextBliss(byte[] bytes, AudioFormat audioFormat) {
        try (final ByteArrayInputStream bStream = new ByteArrayInputStream(bytes);
             final AudioInputStream aStream = new AudioInputStream(bStream, audioFormat, bytes.length)) {
            return audioToText(aStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "Error";
    }

    protected static String audioToText(InputStream stream) throws IOException {
        initAudioToTextStream();

        streamRecognizer.startRecognition(stream);
        final SpeechResult result = streamRecognizer.getResult();
        streamRecognizer.stopRecognition();

        return result == null ? "NULL RESULT" : result.getHypothesis();
    }
}
