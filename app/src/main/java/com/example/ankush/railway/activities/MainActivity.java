package com.example.ankush.railway.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ankush.railway.R;

public class MainActivity extends AppCompatActivity {
    Button bookingButton,pnrButton,dataButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookingButton = (Button)findViewById(R.id.booking_button_activity_main);
        pnrButton = (Button) findViewById(R.id.pnr_button_activity_main);
        dataButton = (Button) findViewById(R.id.database_button_activity_main);

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this,BookingActivity.class);
                startActivity(i);
            }
        });
        pnrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this,PNRActivity.class);
                startActivity(i);
            }
        });
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this,TestActivity.class);
                startActivity(i);
            }
        });
    }
}
