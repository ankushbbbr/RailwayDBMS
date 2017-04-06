package com.example.ankush.railway.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.railway.Constants;
import com.example.ankush.railway.DatabaseOpenHelper;
import com.example.ankush.railway.Person;
import com.example.ankush.railway.R;
import com.example.ankush.railway.Train;

public class TrainStatusActivity extends AppCompatActivity {
    TextView tv;
    Button bookButton;
    int avlAC,avlGen;
    Spinner spinner;
    int pnr;
    String TAG = "TrainStatusTag";
    Train train;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_status);
        tv = (TextView) findViewById(R.id.textview_train_status);
        bookButton = (Button) findViewById(R.id.button_book_now_activity_status);
        spinner = (Spinner)findViewById(R.id.spinner_train_class);
        Intent i = getIntent();
        final String date = i.getStringExtra(Constants.INTENT_TRAIN_DATE);
        train = (Train) i.getSerializableExtra(Constants.INTENT_TRAIN);
        fetchStatus(train ,date);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,spinner.getSelectedItem().toString());
                boolean isAC = spinner.getSelectedItem().toString().compareTo("AC")==0 ;
                if(isAC){
                    if(avlAC > 0){
                        Log.i(TAG, "ac booking");
                        bookTicket(date,train,avlAC,Train.TRAIN_SEAT_TYPE_AC);
                    }
                    else
                        Toast.makeText(TrainStatusActivity.this, "Seats Full!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(avlGen > 0){
                        Log.i(TAG, "gen booking");
                        bookTicket(date,train,avlGen,Train.TRAIN_SEAT_TYPE_GEN);
                    }
                    else
                        Toast.makeText(TrainStatusActivity.this, "Seats Full!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bookTicket(final String date, final Train train, final int seatNo,final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TrainStatusActivity.this);
        builder.setTitle("Enter Details");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null, false);
        final EditText nameET = (EditText) viewInflated.findViewById(R.id.edittext_user_name);
        final EditText ageET = (EditText) viewInflated.findViewById(R.id.edittext_user_age);
        final EditText genderET = (EditText) viewInflated.findViewById(R.id.edittext_user_gender);

        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name,gender;
                name = nameET.getText().toString();
                String aget=ageET.getText().toString();
                gender=genderET.getText().toString();
                int age;
                if(nameET.length()==0||genderET.length()==0||ageET.length()==0){
                    Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        age = Integer.parseInt(aget);
                        Person person = new Person(name, age, gender);
                        addPassengerToDatabase(person, train, seatNo);
                        dialog.dismiss();
                        flash(person, train, pnr + 1);
                        if(type == Train.TRAIN_SEAT_TYPE_AC){
                            decrementACSeats(date,train);
                        }
                        else if(type == Train.TRAIN_SEAT_TYPE_GEN){
                            decrementGenSeats(date, train);
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(),"Enter valid age",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.show();

    }
    void addPassengerToDatabase(Person p,Train train,int seatNo){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * " +
                "FROM passenger ",null);
        pnr=c.getCount();
        db = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String pass_name=p.name;
        int pass_age=p.age;
        String pass_gender=p.gender;
        cv.put("p_name",pass_name);
        cv.put("p_age",pass_age);
        cv.put("p_gender",pass_gender);
        cv.put("train_no",train.number);
        cv.put("booked_date",train.departure_time);
        cv.put("p_status","confirmed");
        cv.put("seat_no",seatNo);
        cv.put("pnr",pnr+1);
        db.insert("passenger",null,cv);
        Toast.makeText(TrainStatusActivity.this,"kkkk",Toast.LENGTH_SHORT).show();
        openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        db = openHelper.getReadableDatabase();
        c = db.rawQuery("SELECT * " +
                "FROM passenger ",null);

        while (c.moveToNext()) {
            String tName = c.getString(c.getColumnIndex("pnr"));
            Log.i(TAG,tName);
            Log.i(TAG,c.getString(c.getColumnIndex("p_name")));
        }
    }
    private void flash(Person p,Train train,int pnrNo){
        AlertDialog.Builder builder = new AlertDialog.Builder(TrainStatusActivity.this);
        builder.setTitle("Ticket Details");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.ticket_and_pnr_details_layout,null, false);
        TextView passname=(TextView)viewInflated.findViewById(R.id.pass_name);
        TextView pnr=(TextView)viewInflated.findViewById(R.id.pnr);
        TextView trainno=(TextView)viewInflated.findViewById(R.id.train_name);
        TextView status=(TextView)viewInflated.findViewById(R.id.status);
        TextView depdate=(TextView)viewInflated.findViewById(R.id.dep_date) ;
        passname.setText(p.name);
        pnr.setText(""+pnrNo);
        trainno.setText(""+train.number);
        status.setText("Confirmed");
        depdate.setText(train.date+ " "+ train.departure_time);
        builder.setView(viewInflated);
        builder.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void decrementGenSeats(String date, Train train) {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getWritableDatabase();;
//        db.rawQuery("UPDATE train_status " +
//                "SET a_ac_seats = a_ac_seats - 1 " +
//                "WHERE date = ? AND train_no = ?",new String[]{date,trainNo+""});
        ContentValues cv = new ContentValues();
        cv.put("a_gen_seats",avlGen-1);
        db.update("train_status",cv,"date = ? AND train_no = ?",new String[]{date,train.number+""});
        fetchStatus(train,date);
    }

    void decrementACSeats(String date,Train train){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getWritableDatabase();
//        db.rawQuery("UPDATE train_status " +
//                "SET a_ac_seats = a_ac_seats - 1 " +
//                "WHERE date = ? AND train_no = ?",new String[]{date,trainNo+""});
        ContentValues cv = new ContentValues();
        cv.put("a_ac_seats",avlAC-1);
        db.update("train_status",cv,"date = ? AND train_no = ?",new String[]{date,train.number+""});
        fetchStatus(train,date);
    }
    void fetchStatus(Train train, String date){
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TrainStatusActivity.this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM train_status " +
                "WHERE date = ? and " +
                "train_no = ?", new String[]{date, "" + train.number});
        String output="";
        while(c.moveToNext()) {
            avlAC = c.getInt(c.getColumnIndex("a_ac_seats"));
            avlGen = c.getInt(c.getColumnIndex("a_gen_seats"));
            Log.i(TAG, "fetchStatus: "+avlAC);
            output = "Train Name : " + train.name + "\n" +
                    "Date : " + date + "\n" +
                    "From : " + train.src + "\n" +
                    "To : " + train.dest + "\n" +
                    "Available Seats (AC) : " + avlAC + "\n" +
                    "Available Seats (GEN) : " + avlGen + "\n" +
                    "Cost (AC) : " + train.acCost + "\n" +
                    "Cost (GEN) : " + train.genCost;
        }
        tv.setText(output);
    }
}
