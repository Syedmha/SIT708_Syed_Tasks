package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database file name and version
    private static final String DATABASE_NAME = "lostandfound.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_ITEMS = "items";
    // Column names — these are the 'spreadsheet headings'
    public static final String COL_ID          = "id";
    public static final String COL_TYPE        = "type";       // "Lost" or "Found"
    public static final String COL_NAME        = "name";       // Item name
    public static final String COL_PHONE       = "phone";      // Contact phone
    public static final String COL_DESC        = "description";
    public static final String COL_DATE        = "date";
    public static final String COL_LOCATION    = "location";
    public static final String COL_CATEGORY    = "category";   // Electronics, Pets...
    public static final String COL_IMAGE_URI   = "image_uri";  // Path to photo
    public static final String COL_TIMESTAMP   = "timestamp";  // When saved


    // Constructor: called when any activity creates DatabaseHelper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate() runs ONCE the very first time the app opens
    // It creates the table if it doesn't exist

    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL to create the items table with all columns
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
                + COL_TIMESTAMP + " TEXT)";

        db.execSQL(CREATE_TABLE); // Execute the SQL command
    }

    // onUpgrade() runs when DATABASE_VERSION increases (e.g. from 1 to 2)
    // Here we simply drop the old table and recreate it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db); // Create fresh table
    }


    // Insert a new Lost/Found item into the database
    // Returns the row ID if successful, -1 if failed
    public long insertItem(LostFoundItem item) {

        SQLiteDatabase db = this.getWritableDatabase(); // Open for writing

        // ContentValues is like a Map: key = column name, value = data
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


        long result = db.insert(TABLE_ITEMS, null, values);
        db.close(); // Always close after use!
        return result;
    }


    // Get ALL items from the database
    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query all rows, ordered newest first
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COL_TIMESTAMP + " DESC",
                null
        );

        // Move through each row in the result
        if (cursor.moveToFirst()) {
            do {
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
                item.setTimestamp(cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    // Get items filtered by category (e.g. only 'Electronics')
    public List<LostFoundItem> getItemsByCategory(String category) {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // The '?' is a safe placeholder (prevents SQL injection)
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COL_CATEGORY + " = ?",
                new String[]{category}
        );

        if (cursor.moveToFirst()) {
            do {
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
                item.setTimestamp(cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Delete one item by its database ID
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete WHERE id = the given id
        db.delete(TABLE_ITEMS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

} // End of DatabaseHelper class


