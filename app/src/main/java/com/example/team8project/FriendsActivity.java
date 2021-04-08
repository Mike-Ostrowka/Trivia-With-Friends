package com.example.team8project;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class FriendsActivity extends AppCompatActivity {

  private Users current;
  private loginPreferences session;
  private String username;
  private int song;
  private int click_sound;
  ViewPager simpleViewPager;
  TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends);
// get the reference of ViewPager and TabLayout
    simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
    tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
// Create a new Tab named "First"
    TabLayout.Tab firstTab = tabLayout.newTab();
    firstTab.setText("Friends"); // set the Text for the first Tab
    firstTab.setIcon(R.drawable.ic_launcher_foreground); // set an icon for the
// first tab
    tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
// Create a new Tab named "Second"
    TabLayout.Tab secondTab = tabLayout.newTab();
    secondTab.setText("Inbox"); // set the Text for the second Tab
    secondTab.setIcon(R.drawable.ic_launcher_foreground); // set an icon for the second tab
    tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
//    tabLayout.bringToFront();

    PagerAdapter adapter = new PagerAdapter
        (getSupportFragmentManager(), tabLayout.getTabCount());
    simpleViewPager.setAdapter(adapter);
// addOnPageChangeListener event change the tab on slide
    simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(onTabSelectedListener(simpleViewPager));
  }

  private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
    return new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    };
  }
}