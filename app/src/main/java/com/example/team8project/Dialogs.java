package com.example.team8project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class Dialogs {
    public Dialogs() {

    }

    public static void buildDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void intentDialog(String message, Context context, Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            context.startActivity(intent);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    public static boolean confirmDialog(String message, Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(message);
//        final int[] response = {0};
//        while(response[0] == 0) {
//            builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> {
//                response[0] = 1;
//            });
//
//            builder.setPositiveButton(R.string.cancel, (dialogInterface, i) -> {
//                response[0] = 2;
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//        return response[0] == 1;
//    }
}
