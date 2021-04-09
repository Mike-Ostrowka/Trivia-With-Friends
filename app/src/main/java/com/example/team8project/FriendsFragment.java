package com.example.team8project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.realm.Realm;

public class FriendsFragment extends Fragment implements OnClickListener {
  private Button friendButton;
  private Realm realm;
  private Users current;
  private loginPreferences session;
  private String username;
  public FriendsFragment() {
// Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
// Inflate the layout for this fragment
    View myView = inflater.inflate(R.layout.fragment_friends, container, false);

    //open a realm and find logged in user
    session = new loginPreferences(getActivity().getApplicationContext());
    username = session.getusername();
    realm = Realm.getDefaultInstance();
    current = realm.where(Users.class).equalTo("_id", username).findFirst();
    realm.close();

    //add listener to button
    friendButton = (Button) myView.findViewById(R.id.btn_new_friend);
    friendButton.setOnClickListener(this);
    return myView;

  }

  @Override
  public void onClick(View view) {
      EditText friendField = view.findViewById(R.id.tf_new_friend);
      String friendName = friendField.getText().toString();
      if (friendName == null) {
        return;
      }
      realm = Realm.getDefaultInstance();
      Users checkFriend = realm.where(Users.class).equalTo("_id", friendName).findFirst();
      if (checkFriend == null) {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.user_failed), Toast.LENGTH_LONG)
            .show();
        realm.close();
        return;
      }
      realm.executeTransaction(transactionRealm -> {
        Users temp = realm.where(Users.class).equalTo("_id", username).findFirst();
        temp.addFriend(friendName);
      });
      realm.close();
  }
}