package com.example.moviecatalogue5.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmoviecatalogue";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " %s TEXT NOT NULL," // COLUMN FILM_ID
            + " %s TEXT NOT NULL," // COLUMN TITLE
            + " %s TEXT NOT NULL," // COLUMN POSTER
            + " %s TEXT NOT NULL," // COLUMN RELEASE DATE
            + " %s TEXT NOT NULL)",// COLUMN TYPE
            TABLE_NAME,
            DatabaseContract.FavoriteColumns._ID,
            DatabaseContract.FavoriteColumns.COLUMN_FILM_ID,
            DatabaseContract.FavoriteColumns.COLUMN_TITLE,
            DatabaseContract.FavoriteColumns.COLUMN_POSTER,
            DatabaseContract.FavoriteColumns.COLUMN_DATE,
            DatabaseContract.FavoriteColumns.COLUMN_TYPE
    );

    private static final String SQL_DROP_TABLE_FAVORITE = String.format("DROP TABLE IF EXISTS %s",
            TABLE_NAME
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE_FAVORITE);
        onCreate(sqLiteDatabase);
    }
}
