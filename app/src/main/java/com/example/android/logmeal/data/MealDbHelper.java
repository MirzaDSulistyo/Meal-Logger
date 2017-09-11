package com.example.android.logmeal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.logmeal.data.MealContract.MealEntry;

/**
 * Created by MIRZA on 3/9/2017.
 */
public class MealDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MealDbHelper.class.getSimpleName();

    /* Nama file database */
    private static final String DATABASE_NAME = "logmeal.db";

    /* Versi database */
    private static final int DATABASE_VERSION = 1;

    public MealDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MEALS_TABLE = "CREATE TABLE " + MealEntry.TABLE_NAME + " ("
                + MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MealEntry.COLOMN_MEAL_NAME + " TEXT NOT NULL, "
                + MealEntry.COLOMN_MEAL_CALORIE + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MEALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
