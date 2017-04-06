package com.example.ankush.railway.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.ankush.railway.Constants;
import com.example.ankush.railway.DatabaseOpenHelper;
import com.example.ankush.railway.R;
import com.example.ankush.railway.Train;
import com.example.ankush.railway.TrainAdapter;

import java.util.ArrayList;

public class TrainListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Train> trains;
    TrainAdapter trainAdapter;
    final String TAG="TransportListTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        Log.i(TAG, "oncreate :");
        listView=(ListView) findViewById(R.id.results_listview);

        trains =new ArrayList<>();
        trainAdapter = new TrainAdapter(this, trains);
        listView.setAdapter(trainAdapter);

        String src_train,dest_train,date;
        Intent intent = getIntent();
        src_train = intent.getStringExtra(Constants.INTENT_TRAIN_SRC_STATION_CODE);
        dest_train = intent.getStringExtra(Constants.INTENT_TRAIN_DEST_STATION_CODE);
        date=intent.getStringExtra(Constants.INTENT_TRAIN_DATE);

        Log.i(TAG, "oncreate : src ="+src_train + ", dest = "+dest_train + "date = ,"+date);
        fetchTrainsFromDatabase(src_train,dest_train,date);
    }
    void fetchTrainsFromDatabase(String src,String dest,String date){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainListActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
//        Cursor c = db.rawQuery("SELECT * FROM train WHERE source_id = ? and " +
//                "destination_id = ?", new String[]{src,dest});
        Cursor c = db.rawQuery("SELECT * " +
                "FROM train,station as src,station as dest " +
                "WHERE source_id = src.station_id AND destination_id = dest.station_id " +
                "AND src.station_name = ? AND dest.station_name = ?",new String[]{src,dest});
        while (c.moveToNext()) {
            String tName = c.getString(c.getColumnIndex("t_name"));
            int tNo = c.getInt(c.getColumnIndex("train_no"));
            int acCost = c.getInt(c.getColumnIndex("ac_cost"));
            int genCost = c.getInt(c.getColumnIndex("gen_cost"));
            Train t = new Train();
            t.name = tName;
            t.number = tNo;
            t.acCost = acCost;
            t.genCost = genCost;
            t.src = src;
            t.dest = dest;
            t.arrival_time = "13:00";
            t.departure_time = "08:45";
            t.travel_time = "6:00";
            t.date = date;
            trains.add(t);
            trainAdapter.notifyDataSetChanged();
        }
    }

}
