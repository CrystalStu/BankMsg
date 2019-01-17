package gq.cstu.ccs.bankmsg;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

public class Tools  {
    public static void exceptionToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void exceptionSave(View view) {
        Snackbar.make(view, "保存失败", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void messageSending(View view) {
        Snackbar.make(view, "正在发信中，请等待“发送完成”框的出现，请不要本日内再次点击此按钮以避免重复发信。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void messageSent(View view) {
        Snackbar.make(view, "发送完成，请不要本日内再次点击此按钮以避免重复发信。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

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