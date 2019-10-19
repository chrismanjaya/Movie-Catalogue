package com.example.moviecatalogue5.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.example.moviecatalogue5";
    private static final String SCHEME = "content";

    public static final class FavoriteColumns implements BaseColumns {
        static final String TABLE_NAME = "favorite";
        static final String COLUMN_FILM_ID = "film_id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_POSTER = "poster_path";
        static final String COLUMN_DATE = "release_date";
        static final String COLUMN_TYPE = "type";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
