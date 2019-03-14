package com.example.airquality.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.example.airquality.R;
import com.example.airquality.View.MainActivity;

public class DialogUtil {
    public static void showDialog(Context context, @StringRes int message, DialogInterface.OnClickListener confirm) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, confirm)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
