package com.example.ankush.railway.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
            }
        });
    }

    private void fetchPNRStatus(String pnrNo) {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(PNRActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
//        Cursor c = db.rawQuery("SELECT * FROM train WHERE source_id = ? and " +
//                "destination_id = ?", new String[]{src,dest});
        Cursor c = db.rawQuery("SELECT * " +
                "FROM passenger " +
                "WHERE pnr = ?",new String[]{""+pnrNo});
        String t="";
        Log.i(TAG,"hey  ");
        while(c.moveToNext()){

            String name= c.getString(c.getColumnIndex("p_name"));
            String trainno=c.getString(c.getColumnIndex("train_no"));
            String seatno=c.getString(c.getColumnIndex("seat_no"));
            String bookeddate=c.getString(c.getColumnIndex("booked_date"));
            t="Passenger Name: "+name+"\n"+"Train no       :"+trainno+"\n"+"Seat No        :"+seatno+"\n"+"Booked Date    :"+bookeddate+"\n";
            Log.i(TAG,t);
         }
        Log.i(TAG,t);
        //String t=name+
        AlertDialog.Builder builder = new AlertDialog.Builder(PNRActivity.this);
        builder.setTitle("PNR Details");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.pnr_status_layout,null, false);
        TextView tv=(TextView)viewInflated.findViewById(R.id.pnr_det);
       if(t==null)
            tv.setText("Not found PNR");
        else
            tv.setText(t);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();

    }
}
