package utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Aaron K on 3/12/2017.
 */

public class MessageHelper
{

    private static TextToSpeech speaker = null;
    private static final String SPEAKER_ID = "MessageHelperAlert";

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
        if(speaker == null)
        {
            initializeSpeaker(context, message);
        }
        else
        {
            speaker.speak(message, TextToSpeech.QUEUE_FLUSH, null, SPEAKER_ID);
        }
    }

    /***
     * Initializes the speaker, and reads the given message once it is ready.
     * @param context - The context this method was called from.
     * @param message - The message to read aloud.
     */
    private static void initializeSpeaker(Context context, final String message)
    {
        speaker = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                speaker.speak(message, TextToSpeech.QUEUE_FLUSH, null, SPEAKER_ID);
            }
        });
    }
}
