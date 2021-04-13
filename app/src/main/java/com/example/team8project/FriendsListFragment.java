package com.example.team8project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import io.realm.Realm;
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
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View myView = inflater.inflate(R.layout.fragment_friends_list, container, false);

    //open a realm and find logged in user
    session = new loginPreferences(getActivity().getApplicationContext());
    username = session.getusername();
    realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();
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
    adapter = new TableAdapter(dataModels, getActivity().getApplicationContext(), getActivity(), current);
    listView.setAdapter(adapter);

  }

  public void updateTable() {
    adapter.clear();
    realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();
    ArrayList<Users> temp = new ArrayList<>();
    for (int i = 0; i < current.getSizeFriends(); i++) {
      temp.add(current.getFriend(i));
    }
    adapter.addAll(temp);
    adapter.notifyDataSetChanged();
  }
}