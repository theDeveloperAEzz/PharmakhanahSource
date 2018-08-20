package com.pharmakhanah.hp.pharmakhanahsource.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.pharmakhanah.hp.pharmakhanahsource.model.MedicineObjectModel;
import com.pharmakhanah.hp.pharmakhanahsource.model.UserInformation;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    int z;
    private static final String DATABASE_NAME = "PharmaDatabase";
    private static final int DATABASE_VERSION = 12;
    private static final String USERS_INFORMATION_TABLE_NAME = "UsersInformation";
    private static final String KEY_ID = "id";
    private static final String KEY_AVATAR = "avatarPath";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String MEDICINE_OBJECT_MODEL_TABLE_NAME = "MedicineObjectModel";
    private static final String KEY_GENERIC_NAME = "generic_name";
    private static final String KEY_TRADE_NAME = "trade_name";
    private static final String KEY_DOSAGE_FORM = "dosage_form";
    private static final String KEY_ROUTE_OF_ADMINISTRATION = "route_of_administration";
    private static final String KEY_PACKAGE_TYPE = "package_type";
    private static final String KEY_PRODUCT_CONTROL = "product_control";
    private static final String KEY_SHELF_LIFE_MONTH = "shelf_life_month";
    private static final String KEY_STORAGE_CONDITIONS = "storage_conditions";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERSINFORMATION_TABLE = " CREATE TABLE " + USERS_INFORMATION_TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_AVATAR + " TEXT , "
                + KEY_NAME + " TEXT , "
                + KEY_EMAIL + " TEXT , "
                + KEY_PHONE + " TEXT , "
                + KEY_CITY + " TEXT " + ")";
        String CREATE_MEDICINE_OBJECT_MODEL_TABLE = " CREATE TABLE " + MEDICINE_OBJECT_MODEL_TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_GENERIC_NAME + " TEXT , "
                + KEY_TRADE_NAME + " TEXT , "
                + KEY_DOSAGE_FORM + " TEXT , "
                + KEY_ROUTE_OF_ADMINISTRATION + "TEXT ,"
                + KEY_PACKAGE_TYPE + " TEXT , "
                + KEY_PRODUCT_CONTROL + " TEXT , "
                + KEY_SHELF_LIFE_MONTH + " TEXT , "
                + KEY_STORAGE_CONDITIONS + " TEXT " + ");";
        db.execSQL(CREATE_MEDICINE_OBJECT_MODEL_TABLE);
        db.execSQL(CREATE_USERSINFORMATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINE_OBJECT_MODEL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_INFORMATION_TABLE_NAME);
        onCreate(db);
    }

    public void insertUsersInformation(UserInformation userInformation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AVATAR, userInformation.getAvatar());
        values.put(KEY_NAME, userInformation.getName());
        values.put(KEY_EMAIL, userInformation.getEmail());
        values.put(KEY_PHONE, userInformation.getPhone());
        values.put(KEY_CITY, userInformation.getCity());
        // Inserting Row
        int x = (int) db.insert(USERS_INFORMATION_TABLE_NAME, null, values);
        Log.d("oldtable", x + "");
        db.close();
    }

    public void insertMedicineObjectModel(MedicineObjectModel medicineObjectModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GENERIC_NAME, medicineObjectModel.getGeneric_name());
        values.put(KEY_TRADE_NAME, medicineObjectModel.getTrade_name());
        values.put(KEY_DOSAGE_FORM, medicineObjectModel.getDosage_form());
        values.put(KEY_ROUTE_OF_ADMINISTRATION, medicineObjectModel.getRoute_of_administration());
        values.put(KEY_PACKAGE_TYPE, medicineObjectModel.getPackage_type());
        values.put(KEY_PRODUCT_CONTROL, medicineObjectModel.getProduct_control());
        values.put(KEY_SHELF_LIFE_MONTH, medicineObjectModel.getShelf_life_month());
        values.put(KEY_STORAGE_CONDITIONS, medicineObjectModel.getStorage_conditions());
        // Inserting Row
        z = (int) db.insert(MEDICINE_OBJECT_MODEL_TABLE_NAME, null, values);
        Log.d("newtable", z + "");
        db.close();
    }

    public ArrayList<UserInformation> selectUserInformation() {
        ArrayList<UserInformation> userInformationArrayList = new ArrayList<UserInformation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USERS_INFORMATION_TABLE_NAME;

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

    public ArrayList<MedicineObjectModel> selectMedicineObjectModelArrayList() {
        ArrayList<MedicineObjectModel> medicineObjectModelArrayList = new ArrayList<MedicineObjectModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MEDICINE_OBJECT_MODEL_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MedicineObjectModel medicineObjectModel = new MedicineObjectModel();
                medicineObjectModel.setGeneric_name(cursor.getString(1));
                medicineObjectModel.setTrade_name(cursor.getString(2));
                medicineObjectModel.setDosage_form(cursor.getString(3));
                medicineObjectModel.setRoute_of_administration(cursor.getString(4));
                medicineObjectModel.setPackage_type(cursor.getString(5));
                medicineObjectModel.setProduct_control(cursor.getString(6));
                medicineObjectModel.setShelf_life_month(cursor.getString(7));
                medicineObjectModel.setStorage_conditions(cursor.getString(8));
                // Adding contact to list
                medicineObjectModelArrayList.add(medicineObjectModel);
            } while (cursor.moveToNext());
        }

        Log.i("all contacs = ", medicineObjectModelArrayList.size() + "");
        // return contact list
        return medicineObjectModelArrayList;
    }

    public ArrayList<UserInformation> selectAvatar() {
        ArrayList<UserInformation> userInformations = new ArrayList<UserInformation>();
        String SELECT_QUERY = " SELECT " + KEY_AVATAR + " FROM " + USERS_INFORMATION_TABLE_NAME;
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

    public void clearUsersInformationTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERS_INFORMATION_TABLE_NAME);
        db.close();
    }

    public void clearMedicineObjectModelTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERS_INFORMATION_TABLE_NAME);
        db.close();
    }


}