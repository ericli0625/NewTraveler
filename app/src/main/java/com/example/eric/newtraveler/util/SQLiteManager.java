package com.example.eric.newtraveler.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sInstance = null;
    private static Object sInstanceLock = new Object();

    private final static String DATABASE_NAME = "favorite_spot.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "favorite";
    private final static String FIELD_Id = "_id";
    public final static String FIELD_Name = "name";
    public final static String FIELD_Category = "category";
    public final static String FIELD_Address = "address";
    public final static String FIELD_Telephone = "telephone";
    public final static String FIELD_Content = "content";

    public final static String FIELD_Longitude = "longitude";
    public final static String FIELD_Latitude = "latitude";

    public static void createInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (sInstanceLock) {
                // Create instance in this sInstanceLock block
                if (sInstance == null) {
                    if (context == null) {
                        throw new IllegalArgumentException("The context can not be null");
                    }
                    sInstance = new SQLiteManager(context.getApplicationContext());
                }
            }
        }
    }

    public static SQLiteManager getInstance() {
        if (sInstance == null) {
            synchronized (sInstanceLock) {
                // Ensure instance is not created in this sInstanceLock block
                if (sInstance == null) {
                    throw new IllegalStateException("MiniViewerManager is not created");
                }
            }
        }
        return sInstance;
    }

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + " ( " + FIELD_Id
                + " INTEGER primary key autoincrement, " + FIELD_Name
                + " text," + FIELD_Category + " text," + FIELD_Address
                + " text," + FIELD_Telephone + " text," + FIELD_Longitude
                + " text," + FIELD_Latitude + " text," + FIELD_Content
                + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor matchData(String name, String category, String address,
            String telephone, String longitude, String latitude, String content) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from " + TABLE_NAME + " where " + FIELD_Name
                + " like ? and " + FIELD_Category + " like ? and "
                + FIELD_Address + " like ? and " + FIELD_Telephone
                + " like ? and " + FIELD_Content + " like ? ";

        Cursor cursor = db.rawQuery(sql,
                new String[]{name, category, address, telephone, content});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor findSpot(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from " + TABLE_NAME + " where " + FIELD_Name + " like ? ";

        Cursor cursor = db.rawQuery(sql,
                new String[]{name});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_Name + " = ?";
        String[] whereValues = {name};
        db.delete(TABLE_NAME, where, whereValues);
    }

    public long insert(String name, String category, String address,
            String telephone, String longitude, String latitude, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIELD_Name, name);
        cv.put(FIELD_Category, category);
        cv.put(FIELD_Address, address);
        cv.put(FIELD_Telephone, telephone);
        cv.put(FIELD_Longitude, longitude);
        cv.put(FIELD_Latitude, latitude);
        cv.put(FIELD_Content, content);

        return db.insert(TABLE_NAME, null, cv);
    }
}
