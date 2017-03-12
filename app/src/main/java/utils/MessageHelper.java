package utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Aaron K on 3/12/2017.
 */

public class MessageHelper
{
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
}
