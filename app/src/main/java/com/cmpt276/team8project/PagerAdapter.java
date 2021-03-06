package com.cmpt276.team8project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//adapter to handle tabs
public class PagerAdapter extends FragmentStatePagerAdapter {

  int mNumOfTabs;

  public PagerAdapter(FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @Override
  public Fragment getItem(int position) {

    switch (position) {
      case 0:
        return new FriendsFragment();
      case 1:
        return new FriendsListFragment();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}