package com.example.team8project;

import android.content.Context;
import android.support.v4.app.Fragment;
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

//code adapted from https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
public class TableAdapter extends ArrayAdapter<Users> implements View.OnClickListener {

  private ArrayList<Users> dataSet;
  Context mContext;
  Context mDialog;
  private Users current;
  private Realm realm;

  // View lookup cache
  private static class ViewHolder {

    TextView txtName;
    TextView txtElo;
    ImageView mDelete;
  }

  public TableAdapter(ArrayList<Users> data, Context context, Context contextTwo, Users cur) {
    super(context, R.layout.row_item, data);
    this.dataSet = data;
    this.mContext = context;
    this.mDialog = contextTwo;
    this.current = cur;


  }

  @Override
  public void onClick(View v) {

    int position = (Integer) v.getTag();
    Users dataModel = getItem(position);
    if (v.getId() == R.id.item_delete) {
      String message =
          "Are you sure you want to remove : " + current.getFriend(position).getUserName()
              + " as a friend?";
      AlertDialog.Builder builder = new AlertDialog.Builder(mDialog);
      builder.setMessage(message);

      builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
          Users tempOther = realm.where(Users.class).equalTo("_id", current.getFriend(position).getUserName()).findFirst();
          tempOther.removeFriend(current);
          Users temp = realm.where(Users.class).equalTo("_id", current.getUserName()).findFirst();
          temp.removeFriend(current.getFriend(position));
        });
        realm.close();
      });

      builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

      });

      AlertDialog dialog = builder.create();
      dialog.show();
    }
  }

  private int lastPosition = -1;

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    Users dataModel = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    ViewHolder viewHolder; // view lookup cache stored in tag

    final View result;

    if (convertView == null) {

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

    viewHolder.txtName.setText(dataModel.getUserName());
    viewHolder.txtElo.setText(strElo);
    viewHolder.mDelete.setOnClickListener(this);
    viewHolder.mDelete.setTag(position);

    // Return the completed view to render on screen
    return convertView;
  }
}
