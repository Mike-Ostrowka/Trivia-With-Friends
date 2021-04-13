package com.example.team8project;

import android.app.Application;
import io.realm.Realm;

public class AppStartup extends Application {


  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);

  }


}