package com.cmpt276.team8project;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
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

    // get eloList from the calling activity
    ArrayList<Integer> myList = (ArrayList<Integer>) getIntent()
        .getSerializableExtra("EXTRA_SESSION_ID");

    LineChartView lineChartView = findViewById(R.id.chart);
    String[] axisData = {"10", "9", "8", "7", "6", "5", "4", "3", "2", "1"};

    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();

    // set colour for the line we will be making
    Line line = new Line(yAxisValues).setColor(getResources().getColor(R.color.colorPrimary));

    // set values for x-axis
    for (int i = 0; i < axisData.length; i++) {
      axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
    }
    // enter elo data in order to generate the y-axis values from the library
    for (int i = 0; i < myList.size(); i++) {
      int j = myList.get(i);
      yAxisValues.add(new PointValue(i, (j)));
    }

    List lines = new ArrayList();
    lines.add(line);

    // set linechart data
    LineChartData data = new LineChartData();
    data.setLines(lines);
    lineChartView.setLineChartData(data);

    // set axis values and set x and y axis
    Axis axis = new Axis();
    axis.setValues(axisValues);
    data.setAxisXBottom(axis);
    Axis yAxis = new Axis();
    data.setAxisYLeft(yAxis);

    // set labels for the x and y axis as well as colour and text size
    axis.setTextColor(getResources().getColor(R.color.colorPrimary));
    yAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
    axis.setName(getResources().getString(R.string.xaxis));
    yAxis.setName(" ");
    axis.setTextSize(16);
    yAxis.setTextSize(16);

    // increase the upper limit of the graph
    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
    viewport.top = Collections.max(myList) + 50;
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

// Code above was modified from the following reference:
// Codedmin, “Android Line Chart - How to Draw Line Chart in Android,”
//  Coding Demos, 16-Jul-2018. [Online]. Available:
//  https://www.codingdemos.com/draw-android-line-chart/.
//  [Accessed: 11-Apr-2021].