package com.example.ankush.railway.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ankush.railway.R;

public class PNRActivity extends AppCompatActivity {
    Button searchButton;
    EditText pnrNoEditText;
    TextView pnrStatusTV;
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
    }
}
