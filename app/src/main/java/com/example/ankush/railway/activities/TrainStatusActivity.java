package com.example.ankush.railway.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.railway.Constants;
import com.example.ankush.railway.DatabaseOpenHelper;
import com.example.ankush.railway.R;

public class TrainStatusActivity extends AppCompatActivity {
    TextView tv;
    Button bookButton;
    int avlAC,avlGen;
    Spinner spinner;
    String TAG = "TrainStatusTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_status);
        tv = (TextView) findViewById(R.id.textview_train_status);
        bookButton = (Button) findViewById(R.id.button_book_now_activity_status);
        spinner = (Spinner)findViewById(R.id.spinner_train_class);
        Intent i = getIntent();
        final String date = i.getStringExtra(Constants.INTENT_TRAIN_DATE);
        final int trainNo = i.getIntExtra(Constants.INTENT_TRAIN_NO,1);
        Log.i(TAG, "train status "+date+" "+trainNo);
        fetchStatus(trainNo,date);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,spinner.getSelectedItem().toString());
                boolean isAC = spinner.getSelectedItem().toString().compareTo("AC")==0 ;
                if(isAC){
                    if(avlAC > 0){
                        Log.i(TAG, "ac booking");
                        bookTicket(date,trainNo);
                        decrementACSeats(date,trainNo);
                    }
                    else
                        Toast.makeText(TrainStatusActivity.this, "Seats Full!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(avlGen > 0){
                        Log.i(TAG, "gen booking");
                        decrementGenSeats(date,trainNo);
                    }
                    else
                        Toast.makeText(TrainStatusActivity.this, "Seats Full!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void bookTicket(String date, int trainNo) {

    }

    private void decrementGenSeats(String date, int trainNo) {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getWritableDatabase();;
//        db.rawQuery("UPDATE train_status " +
//                "SET a_ac_seats = a_ac_seats - 1 " +
//                "WHERE date = ? AND train_no = ?",new String[]{date,trainNo+""});
        ContentValues cv = new ContentValues();
        cv.put("a_gen_seats",avlGen-1);
        db.update("train_status",cv,"date = ? AND train_no = ?",new String[]{date,trainNo+""});
        fetchStatus(trainNo,date);
    }

    void decrementACSeats(String date,int trainNo){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getWritableDatabase();;
//        db.rawQuery("UPDATE train_status " +
//                "SET a_ac_seats = a_ac_seats - 1 " +
//                "WHERE date = ? AND train_no = ?",new String[]{date,trainNo+""});
        ContentValues cv = new ContentValues();
        cv.put("a_ac_seats",avlAC-1);
        db.update("train_status",cv,"date = ? AND train_no = ?",new String[]{date,trainNo+""});
        fetchStatus(trainNo,date);
    }
    void fetchStatus(int trainNo, String date){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM train INNER JOIN train_status " +
                "on train.train_no = train_status.train_no " +
                "WHERE date = ? and " +
                "train.train_no = ?", new String[]{date, "" + trainNo});
        int acCost,genCost;
        String trainName,output="";
        while(c.moveToNext()) {
            acCost = c.getInt(c.getColumnIndex("ac_cost"));
            genCost = c.getInt(c.getColumnIndex("gen_cost"));
            avlAC = c.getInt(c.getColumnIndex("a_ac_seats"));
            avlGen = c.getInt(c.getColumnIndex("a_gen_seats"));
            Log.i(TAG, "fetchStatus: "+avlAC);
            trainName = c.getString(c.getColumnIndex("t_name"));
            output = "Train Name : " + trainName + "\n" +
                    "Date : " + date + "\n" +
                    "Available Seats (AC) : " + avlAC + "\n" +
                    "Available Seats (GEN) : " + avlGen + "\n" +
                    "Cost (AC) : " + acCost + "\n" +
                    "Cost (GEN) : " + genCost;
        }
        tv.setText(output);
    }
}
