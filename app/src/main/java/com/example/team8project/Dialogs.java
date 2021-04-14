package com.example.team8project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class Dialogs {

  public Dialogs() {
    //required empty constructor
  }

  //builds a dialog at given context with a message, no action on close
  public static void buildDialog(String message, Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {

    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  //creates dialog that navigates to given activity after clicking ok
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

  public static void playAgain(Context context, Class<?> cls) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(R.string.play_again);
    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
      //TODO start new game
    });

    builder.setNegativeButton(R.string.main_menu, (dialogInterface, i) -> {
      Intent intent = new Intent();
      intent.setClass(context, cls);
      context.startActivity(intent);
    });

    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
