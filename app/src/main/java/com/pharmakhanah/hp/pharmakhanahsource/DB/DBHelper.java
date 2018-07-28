package com.pharmakhanah.hp.pharmakhanahsource.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.pharmakhanah.hp.pharmakhanahsource.model.UserInformation;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PharmakhanahDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "UsersInformation";
    private static final String KEY_ID = "id";
    private static final String KEY_AVATAR = "avatarPath";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERSINFORMATION_TABLE = " CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_AVATAR + " TEXT , "
                + KEY_NAME + " TEXT , "
                + KEY_EMAIL + " TEXT , "
                + KEY_PHONE + " TEXT , "
                + KEY_CITY + " TEXT " + ")";
        db.execSQL(CREATE_USERSINFORMATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(UserInformation userInformation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AVATAR, userInformation.getAvatar()); // Contact Name
        values.put(KEY_NAME, userInformation.getName()); // Contact Name
        values.put(KEY_EMAIL, userInformation.getEmail()); // Contact Name
        values.put(KEY_PHONE, userInformation.getPhone()); // Contact Phone Number
        values.put(KEY_CITY, userInformation.getCity());
        // Inserting Row
        int x = (int) db.insert(TABLE_NAME, null, values);
        Log.i("insert row", x + "");
        db.close(); // Closing database connection
    }

    public ArrayList<UserInformation> selectUserInformation() {
        ArrayList<UserInformation> userInformationArrayList = new ArrayList<UserInformation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInformation = new UserInformation();
                userInformation.setAvatar(cursor.getString(1));
                userInformation.setName(cursor.getString(2));
                userInformation.setEmail(cursor.getString(3));
                userInformation.setPhone(cursor.getString(4));
                userInformation.setCity(cursor.getString(5));
                // Adding contact to list
                userInformationArrayList.add(userInformation);
            } while (cursor.moveToNext());
        }

        Log.i("all contacs = ", userInformationArrayList.size() + "");
        // return contact list
        return userInformationArrayList;
    }

    public ArrayList<UserInformation> selectAvatar() {
        ArrayList<UserInformation> userInformations = new ArrayList<UserInformation>();
        String SELECT_QUERY = " SELECT " + KEY_AVATAR + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInformation = new UserInformation();

                userInformations.add(userInformation);
            } while (cursor.moveToNext());

        }
        return userInformations;
    }

    //delete all data
    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }


}