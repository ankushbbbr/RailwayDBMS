package com.example.ankush.railway;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankush.railway.activities.TrainStatusActivity;

import java.util.ArrayList;

/**
 * Created by ankushbabbar on 03-Sep-16.
 */
public class TrainAdapter extends ArrayAdapter<Train>
{
    String TAG="AdapterTag";
    Context mContext;
    ArrayList<Train> mTrains;
    public TrainAdapter(Context context, ArrayList<Train> trains) {
        super(context, 0, trains);
        Log.i(TAG, "TrainAdapter: constructor");
        Log.i(TAG, "TrainAdapter: "+ trains.size());
        mContext=context;
        mTrains = trains;
    }
    public static class ViewHolder{
        TextView nameTextView;
        TextView timeTextView;
        TextView durationTextView;
        ImageView iconImageView;
        TextView priceTextView;
        Button bookButton;
        ViewHolder(TextView nameTextView, TextView timeTextView, TextView durationTextView,
                   TextView priceTextView, ImageView iconImageView,Button bookButton){
            this.nameTextView=nameTextView;
            this.timeTextView=timeTextView;
            this.durationTextView=durationTextView;
            this.priceTextView=priceTextView;
            this.iconImageView=iconImageView;
            this.bookButton = bookButton;
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView: "+ mTrains.size());
        if(convertView==null){
            convertView= View.inflate(mContext,R.layout.list_item_layout,null);
            TextView nameTextView=(TextView) convertView.findViewById(R.id.transport_name);
            TextView timeTextView=(TextView) convertView.findViewById(R.id.transport_time);
            TextView priceTextView=(TextView) convertView.findViewById(R.id.transport_price);
            TextView durationTextView=(TextView) convertView.findViewById(R.id.transport_duration);
            ImageView iconImageView=(ImageView) convertView.findViewById(R.id.transport_icon);
            Button bookButton = (Button) convertView.findViewById(R.id.button_book_now);
            ViewHolder vh = new ViewHolder(nameTextView,timeTextView,durationTextView,priceTextView
            ,iconImageView,bookButton);
            convertView.setTag(vh);
        }
        Train curr= mTrains.get(position);
        ViewHolder vh=(ViewHolder)convertView.getTag();
        vh.nameTextView.setText(curr.name);
        vh.timeTextView.setText(curr.departure_time + " - " + curr.arrival_time);
        vh.durationTextView.setText(curr.travel_time);
        vh.priceTextView.setText("Price = \n₹ " + curr.genCost);
        vh.iconImageView.setImageResource(R.drawable.train_logo);
        vh.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(mContext, TrainStatusActivity.class);
                i.putExtra(Constants.INTENT_TRAIN_DATE,mTrains.get(position).date);
                i.putExtra(Constants.INTENT_TRAIN,mTrains.get(position));
                mContext.startActivity(i);
            }
        });
        return convertView;
    }
}
