package com.example.team8project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import io.realm.OrderedCollectionChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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
    if(realm == null) {
      realm = Realm.getDefaultInstance();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View myView = inflater.inflate(R.layout.fragment_friends_list, container, false);

    //open a realm and find logged in user
    session = new loginPreferences(getActivity().getApplicationContext());
    username = session.getusername();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    addChangeListenerToRealm(realm);
    mView = myView;
    return myView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    listView = mView.findViewById(R.id.list);
    dataModels = new ArrayList<>();
    for (int i = 0; i < current.getSizeFriends(); i++) {
      dataModels.add(current.getFriend(i));
    }
    adapter = new TableAdapter(dataModels, getActivity().getApplicationContext(), getActivity(),
        current);
    listView.setAdapter(adapter);
  }

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
    // all tasks in the realm
    RealmResults<Users> tasks = realm.where(Users.class).equalTo("_id", current.getUserName()).findAll();
    tasks.addChangeListener(users -> {
      updateTable();
      tasks.removeAllChangeListeners();
      updateListener();
    });
  }

  private void updateListener() {
    addChangeListenerToRealm(realm);
  }

  @Override
  public void onPause() {
    super.onPause();
    if(realm != null) {
      realm.close();
    }
  }

}