package com.gdi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gdi.model.audit.BrandStandard.BrandStandardSection;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "record_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(DBConstant.CREATE_QUESTIONS_TABLE);
        Log.e("dbstructure:", "" + db.getAttachedDbs());

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS calls");
        // Create tables again
        onCreate(db);
    }

    public long insertRecordRow(String mobile, String dealerName, String contactPerson,
                                String address, String email, String dealingWith, String dealingName,
                                String dealerClassification, String productPiched,
                                String detailDiscussion, String date, String time, String remark) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add
        values.put(DBConstant.COLUMN_MOBILE, mobile);
        values.put(DBConstant.COLUMN_DEALER_NAME, dealerName);
        values.put(DBConstant.COLUMN_CONTACT_PERSON, contactPerson);
        values.put(DBConstant.COLUMN_ADDRESS, address);
        values.put(DBConstant.COLUMN_EMAIL, email);
        values.put(DBConstant.COLUMN_DEALING_WITH, dealingWith);
        values.put(DBConstant.COLUMN_DEALING_NAME, dealingName);
        values.put(DBConstant.COLUMN_DEALER_CLASSIFICATION, dealerClassification);
        values.put(DBConstant.COLUMN_PRODUCT_PICHED, productPiched);
        values.put(DBConstant.COLUMN_DETAIL_DISCUSSION, detailDiscussion);
        values.put(DBConstant.COLUMN_DATE, date);
        values.put(DBConstant.COLUMN_TIME, time);
        values.put(DBConstant.COLUMN_REMARK, remark);


        // insert row
        long id = db.insert(DBConstant.TABLE_NAME, null, values);
        Log.e("Record List Mobile", values.getAsString(DBConstant.COLUMN_MOBILE));
        Log.e("Record List Dealer Name", values.getAsString(DBConstant.COLUMN_DEALER_NAME));
        Log.e("Record List Contact", values.getAsString(DBConstant.COLUMN_CONTACT_PERSON));
        Log.e("Record List Address", values.getAsString(DBConstant.COLUMN_ADDRESS));
        Log.e("Record List Email", values.getAsString(DBConstant.COLUMN_EMAIL));
        Log.e("Record List Dealing", values.getAsString(DBConstant.COLUMN_DEALING_WITH));
        Log.e("Record List Dealing", values.getAsString(DBConstant.COLUMN_DEALING_NAME));
        Log.e("Record List Classifica", values.getAsString(DBConstant.COLUMN_DEALER_CLASSIFICATION));
        Log.e("Record List product", values.getAsString(DBConstant.COLUMN_PRODUCT_PICHED));
        Log.e("Record List detail", values.getAsString(DBConstant.COLUMN_DETAIL_DISCUSSION));
        Log.e("Record List date", values.getAsString(DBConstant.COLUMN_DATE));
        Log.e("Record List time", values.getAsString(DBConstant.COLUMN_TIME));
        Log.e("Record List remark", values.getAsString(DBConstant.COLUMN_REMARK));
        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public BrandStandardSection getQuestionList(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DBConstant.TABLE_NAME,
                new String[]{DBConstant.COLUMN_ID,
                        DBConstant.COLUMN_MOBILE,
                        DBConstant.COLUMN_DEALER_NAME,
                        DBConstant.COLUMN_CONTACT_PERSON,
                        DBConstant.COLUMN_ADDRESS,
                        DBConstant.COLUMN_EMAIL,
                        DBConstant.COLUMN_DEALING_WITH,
                        DBConstant.COLUMN_DEALING_NAME,
                        DBConstant.COLUMN_DEALER_CLASSIFICATION,
                        DBConstant.COLUMN_PRODUCT_PICHED,
                        DBConstant.COLUMN_DETAIL_DISCUSSION,
                        DBConstant.COLUMN_DATE,
                        DBConstant.COLUMN_TIME,
                        DBConstant.COLUMN_REMARK},
                DBConstant.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        /*BrandStandardSection data = new BrandStandardSection(
                cursor.getInt(cursor.getColumnIndex(DBConstant.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_MOBILE)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALER_NAME)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_CONTACT_PERSON)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALING_WITH)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALING_NAME)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALER_CLASSIFICATION)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_PRODUCT_PICHED)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DETAIL_DISCUSSION)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_REMARK)));*/

        // close the db connection
        cursor.close();

        return new BrandStandardSection();
    }

    public ArrayList<BrandStandardSection> getAllQuestionList() {
        ArrayList<BrandStandardSection> arrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME + " ORDER BY " +
                DBConstant.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BrandStandardSection data = new BrandStandardSection();
                /*data.setId(cursor.getInt(cursor.getColumnIndex(DBConstant.COLUMN_ID)));
                data.setMobile(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_MOBILE)));
                data.setDealerName(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALER_NAME)));
                data.setContactPerson(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_CONTACT_PERSON)));
                data.setAddress(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_ADDRESS)));
                data.setEmail(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_EMAIL)));
                data.setDealingWith(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALING_WITH)));
                data.setDealingWith(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALING_NAME)));
                data.setDealerClassification(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DEALER_CLASSIFICATION)));
                data.setProductPiched(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_PRODUCT_PICHED)));
                data.setDetailDiscussion(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DETAIL_DISCUSSION)));
                data.setDate(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_DATE)));
                data.setTime(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_TIME)));
                data.setRemark(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_REMARK)));*/

                arrayList.add(data);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return arrayList;
    }

    public int getRecordListCount() {
        String countQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateRecordData(BrandStandardSection data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        /*values.put(DBConstant.COLUMN_MOBILE, data.getMobile());
        values.put(DBConstant.COLUMN_DEALER_NAME, data.getDealerName());
        values.put(DBConstant.COLUMN_CONTACT_PERSON, data.getContactPerson());
        values.put(DBConstant.COLUMN_ADDRESS, data.getAddress());
        values.put(DBConstant.COLUMN_EMAIL, data.getEmail());
        values.put(DBConstant.COLUMN_DEALING_WITH, data.getDealingWith());
        values.put(DBConstant.COLUMN_DEALING_NAME, data.getDealingName());
        values.put(DBConstant.COLUMN_DEALER_CLASSIFICATION, data.getDealerClassification());
        values.put(DBConstant.COLUMN_PRODUCT_PICHED, data.getProductPiched());
        values.put(DBConstant.COLUMN_DETAIL_DISCUSSION, data.getDetailDiscussion());
        values.put(DBConstant.COLUMN_DATE, data.getDate());
        values.put(DBConstant.COLUMN_TIME, data.getTime());
        values.put(DBConstant.COLUMN_REMARK, data.getRemark());*/

        // updating row
        /*return db.update(DBConstant.TABLE_NAME, values, DBConstant.COLUMN_ID + " = ?",
                new String[]{String.valueOf(data.getId())});*/
        return 0;
    }

    public void deleteRecordData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBConstant.TABLE_NAME, null, null);
        db.close();
    }

}
