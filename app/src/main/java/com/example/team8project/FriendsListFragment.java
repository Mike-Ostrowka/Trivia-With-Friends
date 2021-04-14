package com.example.team8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.huhx0015.hxaudio.audio.HXSound;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;


public class FriendsListFragment extends Fragment {

  ArrayList<Users> dataModels;
  ListView listView;
  private TableAdapter adapter;
  private View mView;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;


  public FriendsListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    realm = Realm.getDefaultInstance();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    //inflate layout
    View myView = inflater.inflate(R.layout.fragment_friends_list, container, false);

    //open a realm and find logged in user
    session = new loginPreferences(getActivity().getApplicationContext());
    username = session.getusername();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();

    //add change listener to update table on friend change
    addChangeListenerToRealm(realm);

    //return fragment to Friends Activity
    mView = myView;
    return myView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    listView = mView.findViewById(R.id.list);

    //populate dataModels with all friends from current user
    dataModels = new ArrayList<>();
    for (int i = 0; i < current.getSizeFriends(); i++) {
      dataModels.add(current.getFriend(i));
    }

    //create an adapter containing all friends
    adapter = new TableAdapter(dataModels, getActivity().getApplicationContext(), getActivity(),
        current);

    //pass adapter info to listView, which displays it
    listView.setAdapter(adapter);

    //listener to open friends profile
    listView.setOnItemClickListener((adapterView, view1, i, l) -> {
      Users friend = dataModels.get(i);
      HXSound.sound().load(R.raw.click).play(view1.getContext());

      //pass intent with friend's username to FriendsProfileActivity
      Intent intent = new Intent(getActivity(), FriendsProfileActivity.class);
      String sessionId = friend.getUserName();
      intent.putExtra("EXTRA_SESSION_ID", sessionId);
      startActivity(intent);
    });
  }

  //deletes and repopulates the adapter
  public void updateTable() {
    adapter.clear();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    ArrayList<Users> temp = new ArrayList<>();
    for (int i = 0; i < current.getSizeFriends(); i++) {
      temp.add(current.getFriend(i));
    }
    adapter.addAll(temp);
    adapter.notifyDataSetChanged();
  }

  //change listener on local realm
  private void addChangeListenerToRealm(Realm realm) {
    RealmResults<Users> tasks = realm.where(Users.class).equalTo("_id", current.getUserName())
        .findAll();

    //update friends list on realm change
    tasks.addChangeListener(users -> {
      updateTable();

      //reset listener on realm
      tasks.removeAllChangeListeners();
      updateListener();
    });
  }

  private void updateListener() {
    addChangeListenerToRealm(realm);
  }

  //ensure realm is closed
  @Override
  public void onPause() {
    super.onPause();
    if (realm != null) {
      realm.close();
    }
  }

}