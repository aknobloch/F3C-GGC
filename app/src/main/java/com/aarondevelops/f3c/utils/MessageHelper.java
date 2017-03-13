package com.aarondevelops.f3c.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MessageHelper
{

    private static TextToSpeech speaker = null;
    private static final String SPEAKER_ID = "MessageHelperAlert";
    private static boolean speakerInitialized = false;

    /***
     * Creates a toast with the given message and displays it in the context
     * @param context - The context to display the message
     * @param message - The message to toast.
     */
    public static void toastAlert(Context context, String message)
    {
        Toast errorToast = Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT);

        errorToast.show();
    }

    /***
     * Creates a snackbar with the given message and displays it in the view
     * @param view - The view to display the message
     * @param message - The message to toast.
     */
    public static void snackbarAlert(View view, String message)
    {
        Snackbar displayMessage = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        displayMessage.show();
    }

    /***
     * Reads the given message through the phone speaker
     * @param context - The context this method was called from.
     * @param message - The message to read aloud.
     */
    public static void speakMessage(Context context, String message)
    {
        if( ! speakerInitialized)
        {
            toastAlert(context, "Speaker not ready.");
            return;
        }

        speaker.speak(message, TextToSpeech.QUEUE_FLUSH, null, SPEAKER_ID);
    }

    /***
     * Initializes the speaker.
     * @param context - The context this method was called from.
     */
    public static void initializeSpeaker(Context context)
    {
        speaker = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if(status == TextToSpeech.ERROR)
                {
                    return;
                }

                speakerInitialized = true;
                setSpeakerVoice("en-gb-x-fis#male_1-local");
            }
        });

    }

    /***
     * Sets the speaker voice to specified name. If name cannot be found,
     * the default voice will be used. This method then sets the speech rate
     * of the voice to slow down the default speed.
     */
    private static void setSpeakerVoice(String voiceName)
    {
        try
        {
            // find the male Great Britain voice
            for(Voice voice : speaker.getVoices())
            {
                if(voice.getName().equals(voiceName))
                {
                    speaker.setVoice(voice);
                }
            }

            // set speech rate a tad slower
            speaker.setSpeechRate(.95f);
        }
        catch(NullPointerException npe)
        {
            Log.e("RuntimeError", "No voices found.");
        }

    }
}
