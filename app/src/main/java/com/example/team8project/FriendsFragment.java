package com.example.team8project;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private Button friendButton;
    private Realm realm;
    private Users current;
    private loginPreferences session;
    private String username;
    private View mView;

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
        mView = myView;
        return myView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //add listener to button
        friendButton = view.findViewById(R.id.btn_new_friend);
        friendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText friendField = mView.findViewById(R.id.tf_new_friend);
        if (friendField.getText() == null) {
            return;
        }
        String friendName = friendField.getText().toString();
        realm = Realm.getDefaultInstance();
        Users checkFriend = realm.where(Users.class).equalTo("_id", friendName).findFirst();
        if (checkFriend == null) {
            Context context = getActivity();
            Dialogs.buildDialog(getString(R.string.user_failed), context);
            realm.close();
            return;
        }
        if (current.friendExists(checkFriend)) {
            Context context = getActivity();
            Dialogs.buildDialog(getString(R.string.friend_exists), context);
            realm.close();
            return;
        }
        realm.executeTransaction(transactionRealm -> {
            Users temp = realm.where(Users.class).equalTo("_id", username).findFirst();
            temp.addFriend(checkFriend);
            Users tempOther = realm.where(Users.class).equalTo("_id", checkFriend.getUserName()).findFirst();
            tempOther.addFriend(current);
        });
        realm.close();
        friendField.setText("");
        Dialogs.buildDialog(getString(R.string.friend_added), getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }
}