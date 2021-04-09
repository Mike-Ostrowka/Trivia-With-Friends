package com.example.team8project;

import android.content.Context;
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
}
