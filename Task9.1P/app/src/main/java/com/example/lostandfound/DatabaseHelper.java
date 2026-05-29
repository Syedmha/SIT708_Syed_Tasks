package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "lostandfound.db";
    private static final int    DATABASE_VERSION  = 2; // bumped from 1 → 2
    private static final String TABLE_ITEMS       = "items";

    public static final String COL_ID         = "id";
    public static final String COL_TYPE       = "type";
    public static final String COL_NAME       = "name";
    public static final String COL_PHONE      = "phone";
    public static final String COL_DESC       = "description";
    public static final String COL_DATE       = "date";
    public static final String COL_LOCATION   = "location";
    public static final String COL_CATEGORY   = "category";
    public static final String COL_IMAGE_URI  = "image_uri";
    public static final String COL_TIMESTAMP  = "timestamp";
    public static final String COL_LATITUDE   = "latitude";   // NEW
    public static final String COL_LONGITUDE  = "longitude";  // NEW

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ITEMS + " ("
                + COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TYPE      + " TEXT, "
                + COL_NAME      + " TEXT, "
                + COL_PHONE     + " TEXT, "
                + COL_DESC      + " TEXT, "
                + COL_DATE      + " TEXT, "
                + COL_LOCATION  + " TEXT, "
                + COL_CATEGORY  + " TEXT, "
                + COL_IMAGE_URI + " TEXT, "
                + COL_TIMESTAMP + " TEXT, "
                + COL_LATITUDE  + " REAL, "
                + COL_LONGITUDE + " REAL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table and recreate with new columns
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // ── insertItem ───────────────────────────────────────────────
    public long insertItem(LostFoundItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TYPE,      item.getType());
        values.put(COL_NAME,      item.getName());
        values.put(COL_PHONE,     item.getPhone());
        values.put(COL_DESC,      item.getDescription());
        values.put(COL_DATE,      item.getDate());
        values.put(COL_LOCATION,  item.getLocation());
        values.put(COL_CATEGORY,  item.getCategory());
        values.put(COL_IMAGE_URI, item.getImageUri());
        values.put(COL_TIMESTAMP, item.getTimestamp());
        values.put(COL_LATITUDE,  item.getLatitude());   // NEW
        values.put(COL_LONGITUDE, item.getLongitude());  // NEW
        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return result;
    }

    // ── getAllItems ──────────────────────────────────────────────
    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COL_TIMESTAMP + " DESC", null);
        if (cursor.moveToFirst()) {
            do { list.add(cursorToItem(cursor)); } while (cursor.moveToNext());
        }
        cursor.close(); db.close();
        return list;
    }

    // ── getItemsByCategory ───────────────────────────────────────
    public List<LostFoundItem> getItemsByCategory(String category) {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COL_CATEGORY + " = ?",
                new String[]{category});
        if (cursor.moveToFirst()) {
            do { list.add(cursorToItem(cursor)); } while (cursor.moveToNext());
        }
        cursor.close(); db.close();
        return list;
    }

    // ── deleteItem ───────────────────────────────────────────────
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ── private helper: map Cursor row → LostFoundItem ──────────
    private LostFoundItem cursorToItem(Cursor cursor) {
        LostFoundItem item = new LostFoundItem();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)));
        item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
        item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)));
        item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)));
        item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
        item.setImageUri(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URI)));
        item.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
        item.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
        item.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
        return item;
    }
}
