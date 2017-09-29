package workoutguide.pentagramsoftware.pl.workoutguide.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import timber.log.Timber;
import workoutguide.pentagramsoftware.pl.workoutguide.utils.StringUtils;

/**
 * Created by Admin on 2016-10-15.
 */

public class TextToSpeechManager {

    private static TextToSpeechManager textToSpeechManager;
    private TextToSpeech textToSpeech;

    public static TextToSpeechManager getInstance() {
        if (textToSpeechManager == null) {
            textToSpeechManager = new TextToSpeechManager();
        }
        return textToSpeechManager;
    }

    /**
     * Speak the text
     */
    public void speakAndAddToCurrentSpeech(String text) {
        Timber.d("%s, speak add called", StringUtils.GLOBAL_TAG);
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, "randomIdHere");
    }

    public void speakAndDiscardCurentSpeech(String text) {
//        Timber.d("%s, speak flush called", StringUtils.GLOBAL_TAG);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "randomIdHere");
    }

    /**
     * Release the resources
     */
    public void terminate() {
        textToSpeech.shutdown();
    }

    public void initEngine(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.UK);
            }
        });
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }
}
