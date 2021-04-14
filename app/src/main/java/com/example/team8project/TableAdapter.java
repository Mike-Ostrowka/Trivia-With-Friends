package com.example.team8project;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import io.realm.Realm;
import java.util.ArrayList;

//code adapted from
// Anupam Chugh, Android ListView with Custom Adapter Example Tutorial
// JournalDev [Online], Available: https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
// Accessed April 10, 2021

//class handles array of users for listView table, and adds click listener to delete button
public class TableAdapter extends ArrayAdapter<Users> implements View.OnClickListener {

  private final ArrayList<Users> dataSet;
  private final Users current;
  Context mContext;
  Context mDialog;
  private Realm realm;
  private int lastPosition = -1;

  public TableAdapter(ArrayList<Users> data, Context context, Context contextTwo, Users cur) {
    super(context, R.layout.row_item, data);
    this.dataSet = data;
    this.mContext = context;
    this.mDialog = contextTwo;
    this.current = cur;


  }

  @Override
  public void onClick(View v) {

    //get position of deleted item in table
    int position = (Integer) v.getTag();
    if (v.getId() == R.id.item_delete) {
      String message =
          "Are you sure you want to remove :    " + current.getFriend(position).getUserName()
              + " as a friend?";
      AlertDialog.Builder builder = new AlertDialog.Builder(mDialog);
      builder.setMessage(message);

      //confirm delete button
      builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> {
        realm = Realm.getDefaultInstance();

        //deletes friend and user from each others friends lists
        realm.executeTransaction(transactionRealm -> {
          Users tempOther = realm.where(Users.class)
              .equalTo("_id", current.getFriend(position).getUserName()).findFirst();
          if (tempOther != null) {
            tempOther.removeFriend(current);
          }

          Users temp = realm.where(Users.class).equalTo("_id", current.getUserName()).findFirst();
          if (temp != null) {
            temp.removeFriend(current.getFriend(position));
          }
        });
        realm.close();
      });

      //cancel delete button, does nothing
      builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
      });

      //build dialog
      AlertDialog dialog = builder.create();
      dialog.show();
    }
  }


  //processes a given row
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    // Get the data item for this position
    Users dataModel = getItem(position);

    // Check if an existing view is being reused, otherwise inflate the view
    ViewHolder viewHolder; // view lookup cache stored in tag

    final View result;

    if (convertView == null) {

      //sets Views for each of the table elements
      viewHolder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.row_item, parent, false);
      viewHolder.txtName = convertView.findViewById(R.id.table_username);
      viewHolder.txtElo = convertView.findViewById(R.id.table_elo);
      viewHolder.mDelete = convertView.findViewById(R.id.item_delete);

      result = convertView;

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
      result = convertView;
    }

    //create scrolling animation
    Animation animation = AnimationUtils
        .loadAnimation(mContext,
            (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
    result.startAnimation(animation);
    lastPosition = position;

    int elo = dataModel.getElo();
    String strElo = Integer.toString(elo);

    //sets values for the row
    viewHolder.txtName.setText(dataModel.getUserName());
    viewHolder.txtElo.setText(strElo);
    viewHolder.mDelete.setOnClickListener(this);
    viewHolder.mDelete.setTag(position);

    // Return the completed view to render on screen
    return convertView;
  }

  // View lookup cache
  private static class ViewHolder {

    TextView txtName;
    TextView txtElo;
    ImageView mDelete;
  }
}
