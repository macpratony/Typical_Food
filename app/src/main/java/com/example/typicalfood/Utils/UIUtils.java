package com.example.typicalfood.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

public final class UIUtils {

    private UIUtils() {}

    public static void styleProgressBar(ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable()
                .setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       String positiveText, DialogInterface.OnClickListener positiveListener,
                                       String negativeText, DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .show();
    }
}
