package com.example.ankush.railway.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.railway.DatabaseOpenHelper;
import com.example.ankush.railway.Person;
import com.example.ankush.railway.R;

public class PNRActivity extends AppCompatActivity {
    Button searchButton;
    EditText pnrNoEditText;
    TextView pnrStatusTV;
    String TAG="PNRActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr);
        searchButton = (Button)findViewById(R.id.search_pnr_status_button);
        pnrNoEditText = (EditText)findViewById(R.id.pnr_no_edittext);
        pnrStatusTV = (TextView)findViewById(R.id.pnr_status_textview);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pnr = pnrNoEditText.getText().toString();
                fetchPNRStatus(pnr);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
    }

    private void fetchPNRStatus(String pnrNo) {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(PNRActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT p_name,t.train_no,s.station_name as src_name,c.station_name as dest_name,t_name,seat_no,booked_date " +
                "FROM passenger as p,train as t,station as s ,station as c " +
                "WHERE p.pnr = ? AND p.train_no=t.train_no " +
                "AND c.station_id = t.destination_id AND s.station_id=t.source_id", new String[]{"" + pnrNo});

        String t="";
        while(c.moveToNext()){
            String name= c.getString(c.getColumnIndex("p_name"));
            String trainName=c.getString(c.getColumnIndex("t_name"));
            String seatNo=c.getString(c.getColumnIndex("seat_no"));
            String bookedDate=c.getString(c.getColumnIndex("booked_date"));

            String src = c.getString(c.getColumnIndex("src_name"));
            String dest = c.getString(c.getColumnIndex("dest_name"));
            t = "Passenger Name: "+name+"\n"+
                    "Train name:"+trainName+"\n"+
                    "From           :"+src+"\n"+
                    "To             :"+dest+"\n"+
                    "Seat No        :"+seatNo+"\n"+
                    "Booked Date    :"+bookedDate;
            pnrStatusTV.setText(t);
        }
        Log.i(TAG,t);
        if(t.compareTo("")==0)
            Toast.makeText(this, "PNR not found", Toast.LENGTH_SHORT).show();
    }
}
