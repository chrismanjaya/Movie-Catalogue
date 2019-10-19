package com.example.moviecatalogue5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.moviecatalogue5.model.Favorite;
import java.util.ArrayList;
import static android.provider.BaseColumns._ID;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.COLUMN_DATE;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.COLUMN_FILM_ID;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.COLUMN_POSTER;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.COLUMN_TITLE;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.COLUMN_TYPE;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public boolean isDatabaseOpen() {
        return database.isOpen();
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public ArrayList<Favorite> getAllFavorite() {
        ArrayList<Favorite> arrayList = new ArrayList<>();
        Cursor cursor = queryProvider();
        cursor.moveToFirst();
        Favorite favorite;

        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setFilmId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILM_ID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                favorite.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));

                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
    }

    public ArrayList<Favorite> getAllFavoriteByType(String type) {
        ArrayList<Favorite> arrayList = new ArrayList<>();

        Cursor cursor = queryProviderByType(type);
        cursor.moveToFirst();
        Favorite favorite;

        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setFilmId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILM_ID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                favorite.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                favorite.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));

                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryProviderByType(String filmType) {
        String selection = COLUMN_TYPE + "=?";
        String[] selectionArgs = {filmType};
        return database.query(DATABASE_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                _ID + " ASC",
                null);
    }

    public long insertFavorite(Favorite favorite) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_FILM_ID, favorite.getFilmId());
        args.put(COLUMN_TITLE, favorite.getTitle());
        args.put(COLUMN_POSTER, favorite.getPosterPath());
        args.put(COLUMN_DATE, favorite.getReleaseDate());
        args.put(COLUMN_TYPE, favorite.getType());

        return insertProvider(args);
    }

    public long insertProvider(ContentValues args) {
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFavorite(Favorite favorite) {
        String whereClause = COLUMN_FILM_ID + "='" + favorite.getFilmId() + "' AND " + COLUMN_TYPE + "='" + favorite.getType() + "'";
        return deleteProvider(whereClause);
    }

    public int deleteProvider(String whereClause) {
        return database.delete(DATABASE_TABLE, whereClause, null);
    }

    public boolean selectIsExistFavorite(Favorite favorite) {
        String filmId = favorite.getFilmId();
        String filmType = favorite.getType();

        Cursor cursor = queryProviderById(filmId, filmType);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor queryProviderById(String filmId, String filmType) {
        String selection = COLUMN_FILM_ID + "=? AND " + COLUMN_TYPE + "=?";
        String[] selectionArgs = {filmId, filmType};
        return database.query(DATABASE_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );
    }
}
