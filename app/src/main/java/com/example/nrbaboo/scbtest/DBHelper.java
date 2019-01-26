package com.example.nrbaboo.scbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private SQLiteDatabase sqLiteDatabase;

        public DBHelper(Context context) {
            super(context, "favorite_mobile_list.db", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_FAVORITE_MOBILE_TABLE  = String.format("CREATE TABLE %s " +
                            "(%s INTEGER PRIMARY KEY)",
                    FavoriteMobile.TABLE,
                    FavoriteMobile.Column.ID);
            db.execSQL(CREATE_FAVORITE_MOBILE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String DROP_FRIEND_TABLE = "DROP TABLE IF EXISTS " + FavoriteMobile.TABLE;
            db.execSQL(DROP_FRIEND_TABLE);
            //Log.i("707", "Upgrade Database from " + oldVersion + " to " + newVersion);
            onCreate(db);

        }

        public ArrayList<Integer> getFavoritedList() {
            ArrayList<Integer> favorite = new ArrayList<Integer>();
            sqLiteDatabase = this.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.query
                    (FavoriteMobile.TABLE, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {
                favorite.add(cursor.getInt(0));
                cursor.moveToNext();
            }

            sqLiteDatabase.close();
            return favorite;
        }

        public void addFavorite(FavoriteMobile favorite) {
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(FavoriteMobile.Column.ID, favorite.getId());

            sqLiteDatabase.insert(FavoriteMobile.TABLE, null, values);
            sqLiteDatabase.close();
        }
    public void delFavorite(int id) {

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(FavoriteMobile.TABLE, FavoriteMobile.Column.ID +"="+ id,null);
        sqLiteDatabase.close();
    }

}
