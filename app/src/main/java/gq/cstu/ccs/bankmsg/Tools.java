package gq.cstu.ccs.bankmsg;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class Tools  {
    public static void exceptionToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /*
    public static void exceptionDialog(Context context, String title, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing.
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    */

    public static void exceptionDialog(Context context, String title, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}