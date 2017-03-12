package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Aaron K on 3/12/2017.
 */

public class ToastHelper
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
}
