package com.example.ankush.railway;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ankushbabbar on 04-Apr-17.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public  final static String DATABASE_NAME = "rail.db";
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
}
