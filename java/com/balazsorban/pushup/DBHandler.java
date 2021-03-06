package com.balazsorban.pushup;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "main.db",
            TABLE_NAME = "PushUps",
            COLUMN_ID = "_id",
            COLUMN_COUNT = "COUNT",
            COLUMN_DATE = "DATE",
            COLUMN_DURATION = "DURATION";

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                TABLE_NAME +"( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COUNT + " INTEGER, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_DURATION + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addSession(Sessions session) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNT, session.get_pushups());
        values.put(COLUMN_DATE, session.get_date());
        values.put(COLUMN_DURATION, session.get_duration());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void emptyDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    String databaseToString() {
        StringBuilder dbString = new StringBuilder();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        //check that moveToFirst returns true
        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex(COLUMN_COUNT)) != null) {
                    dbString.append("You did ");
                    dbString.append(c.getString(c.getColumnIndex(COLUMN_COUNT)));
                    dbString.append(" pushup(s) on ");
                    dbString.append(convertDate(c.getString(c.getColumnIndex(COLUMN_DATE)), "yyyy MMM dd @ HH:mm:ss, "));
                    dbString.append("\nand it took you ");
                    dbString.append(c.getString(c.getColumnIndex(COLUMN_DURATION)));
                    dbString.append(" seconds.\n\n");
                }
                c.moveToNext();
            }

        }
        c.close();
        db.close();
        return dbString.toString();
    }

}