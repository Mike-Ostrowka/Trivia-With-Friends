package com.example.team8project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import io.realm.Realm;

public class GraphActivity extends AppCompatActivity {

  Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graph);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_profile);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }

    //open a realm and find logged in user
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    realm = Realm.getDefaultInstance();
    Users current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();

    ArrayList<Integer> graphData = current.eloList();

    LineChartView lineChartView = findViewById(R.id.chart);
    String[] axisData = {"10", "9", "8", "7", "6", "5", "4", "3", "2", "1"};

    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();

    Line line = new Line(yAxisValues).setColor(getResources().getColor(R.color.colorPrimary));

    for (int i = 0; i < axisData.length; i++) {
      axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
    }
    for (int i = 0; i < graphData.size(); i++) {
      int j = graphData.get(i);
      yAxisValues.add(new PointValue(i, (j)));
    }

    List lines = new ArrayList();
    lines.add(line);

    LineChartData data = new LineChartData();
    data.setLines(lines);
    lineChartView.setLineChartData(data);

    Axis axis = new Axis();
    axis.setValues(axisValues);
    data.setAxisXBottom(axis);
    Axis yAxis = new Axis();
    data.setAxisYLeft(yAxis);

    axis.setTextColor(getResources().getColor(R.color.colorPrimary));
    yAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
    axis.setName(getResources().getString(R.string.xaxis));
    yAxis.setName(" ");
    axis.setTextSize(16);
    yAxis.setTextSize(16);
    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
    viewport.top = Collections.max(graphData)+50;
    lineChartView.setMaximumViewport(viewport);
    lineChartView.setCurrentViewport(viewport);

  }

  private void playMusic() {
    int song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (realm != null) {
      realm.close();
    }
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }
}
