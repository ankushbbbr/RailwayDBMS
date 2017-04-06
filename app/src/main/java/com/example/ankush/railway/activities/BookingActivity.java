package com.example.ankush.railway.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ankush.railway.Constants;
import com.example.ankush.railway.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    EditText dateEditText,srcEditText,destEditText;
    Button searchButton;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        findViews();
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BookingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }
    private void findViews(){
        dateEditText = (EditText)findViewById(R.id.date_edittext_booking_activity);
        searchButton= (Button)findViewById(R.id.button_search);
        srcEditText= (EditText) findViewById(R.id.from_edittext);
        destEditText=(EditText) findViewById(R.id.to_edittext);
    }
    private void updateLabel() {

        String myFormat = "dd-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }
    private void doSearch(){
        String src_train= srcEditText.getText().toString();
        String dest_train= destEditText.getText().toString();
        String date=dateEditText.getText().toString();

        Intent intent = new Intent();
        intent.setClass(BookingActivity.this,TrainListActivity.class);
        intent.putExtra(Constants.INTENT_TRAIN_SRC_STATION_CODE,src_train);
        intent.putExtra(Constants.INTENT_TRAIN_DEST_STATION_CODE,dest_train);
        intent.putExtra(Constants.INTENT_TRAIN_DATE,date);


        if(date.length() == 0 || src_train.length()==0 || dest_train.length() == 0){
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }
}
