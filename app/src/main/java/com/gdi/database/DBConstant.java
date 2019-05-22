package com.gdi.database;

public class DBConstant {

    public static final String TABLE_NAME = "create_record";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LEAD_ID = "lead_id";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_DEALER_NAME = "dealer_name";
    public static final String COLUMN_CONTACT_PERSON = "contact_person";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DEALING_WITH = "dealing_with";
    public static final String COLUMN_DEALING_NAME = "dealing_name";
    public static final String COLUMN_DEALER_CLASSIFICATION = "dealer_classification";
    public static final String COLUMN_PRODUCT_PICHED = "product_piched";
    public static final String COLUMN_DETAIL_DISCUSSION = "detail_discussion";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REMARK = "remark";


    // Create table SQL query
    public static final String CREATE_QUESTIONS_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MOBILE + " TEXT,"
                    + COLUMN_DEALER_NAME + " TEXT,"
                    + COLUMN_CONTACT_PERSON + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_DEALING_WITH + " TEXT,"
                    + COLUMN_DEALING_NAME + " TEXT,"
                    + COLUMN_DEALER_CLASSIFICATION + " TEXT,"
                    + COLUMN_PRODUCT_PICHED + " TEXT,"
                    + COLUMN_DETAIL_DISCUSSION + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_TIME + " TEXT,"
                    + COLUMN_REMARK + " TEXT"
                    + ")";

}
