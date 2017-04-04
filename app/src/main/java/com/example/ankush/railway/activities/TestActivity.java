package com.example.ankush.railway.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.railway.DatabaseOpenHelper;
import com.example.ankush.railway.R;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity {
    Button add,view;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        add = (Button) findViewById(R.id.button_add_test);
        view = (Button) findViewById(R.id.button_view_test);
        tv = (TextView) findViewById(R.id.textview_test);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDB();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchFromDB();
            }
        });
    }

    private void fetchFromDB() {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TestActivity.this);
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            //Cursor c = db.query("student",null, null, null, null, null, null);
            Cursor c = db.rawQuery("select * from train_status " +
                    "where date = '20-04' and train_no = 13",null);
            String output = "";
            while (c.moveToNext()) {
                int no = c.getInt(c.getColumnIndex("train_no"));
                String date = c.getString(c.getColumnIndex("date"));
                output += no + "  " + date + "\n";
            }
            tv.setText(output);
        }catch (SQLiteCantOpenDatabaseException e){
            Toast.makeText(this, "cannot open database", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToDB() {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(TestActivity.this);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i=1;i<=25;i++){
            for(int j=1;j<30;j++){
                cv.put("train_no",i);
                String date;
                if(j<10)
                    date = "0"+j+"-04";
                else
                    date = j+"-04";
                cv.put("date",date);
                cv.put("ac_seats",200);
                cv.put("gen_seats",500);
                cv.put("a_ac_seats",200);
                cv.put("a_gen_seats",500);
                db.insert("train_status",null,cv);
            }
        }
    }
}
