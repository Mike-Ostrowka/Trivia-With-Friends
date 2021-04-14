package com.cmpt276.team8project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class Dialogs {

  public Dialogs() {
    //required empty constructor
  }

  //builds a dialog at given context with a message, no action on close
  public static void buildDialog(String message, Context context) {

    //sets message and context for the dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(message);

    //adds a button that says OK
    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {

    });

    //builds and shows the dialog
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  //creates dialog that navigates to given activity after clicking ok
  public static void intentDialog(String message, Context context, Class<?> cls) {

    //sets the message and context for the dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(message);

    //sets a button that changes activity after clicking ok
    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
      Intent intent = new Intent();
      intent.setClass(context, cls);
      context.startActivity(intent);
    });

    //builds dialog
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
