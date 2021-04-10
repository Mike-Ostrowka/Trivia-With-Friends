package com.example.team8project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//code adapted from https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
public class TableAdapter extends ArrayAdapter<Users> implements View.OnClickListener{

  private ArrayList<Users> dataSet;
  Context mContext;

  // View lookup cache
  private static class ViewHolder {
    TextView txtName;
    TextView txtElo;
    ImageView mDelete;
  }

  public TableAdapter(ArrayList<Users> data, Context context) {
    super(context, R.layout.row_item, data);
    this.dataSet = data;
    this.mContext=context;

  }

  @Override
  public void onClick(View v) {

    int position=(Integer) v.getTag();
    Users dataModel= getItem(position);

    if (v.getId() == R.id.item_delete) {
      //TODO delete
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
      viewHolder.txtName = (TextView) convertView.findViewById(R.id.table_username);
      viewHolder.txtElo = (TextView) convertView.findViewById(R.id.table_elo);
      viewHolder.mDelete = (ImageView) convertView.findViewById(R.id.item_delete);

      result=convertView;

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
      result=convertView;
    }

    Animation animation = AnimationUtils
        .loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
    result.startAnimation(animation);
    lastPosition = position;



    viewHolder.txtName.setText(dataModel.getUserName());
    viewHolder.txtElo.setText(dataModel.getElo());
    viewHolder.mDelete.setOnClickListener(this);
    viewHolder.mDelete.setTag(position);
    // Return the completed view to render on screen
    return convertView;
  }
}
