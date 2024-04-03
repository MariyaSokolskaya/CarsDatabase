package com.example.carsdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cars.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_ID_TYPE = "id_type";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_POWER = "power";
    public static final String TABLE_TYPE = "car_type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_COEFF = "coefficient";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_CARS + "(_id integer PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID_TYPE + " integer, "
                + COLUMN_YEAR + " integer, "
                + COLUMN_MODEL + " text, "
                + COLUMN_POWER + " real, "
                + COLUMN_PRICE + " real);";
        sqLiteDatabase.execSQL(query);
        query = "CREATE TABLE " + TABLE_TYPE + "(_id integer PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " text, "
                + COLUMN_COUNTRY + " text, "
                + COLUMN_COEFF + " real);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
