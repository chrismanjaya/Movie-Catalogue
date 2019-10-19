package com.example.moviecatalogue5.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.moviecatalogue5.database.DatabaseContract.AUTHORITY;
import static com.example.moviecatalogue5.database.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class FilmProvider extends ContentProvider {

    private static final int SELECT_FAVORITE = 1;
    private static final int INSERT_FAVORITE = 1;
    private static final int DELETE_FAVORITE = 1;

    FavoriteHelper favoriteHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, SELECT_FAVORITE);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        favoriteHelper.open();
        Cursor cursor = null;
        if (sUriMatcher.match(uri) == SELECT_FAVORITE) {
            cursor = favoriteHelper.queryProvider();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        favoriteHelper.open();
        long added = 0;
        if (sUriMatcher.match(uri) == INSERT_FAVORITE) {
            added = favoriteHelper.insertProvider(contentValues);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        favoriteHelper.open();
        int deleted = 0;
        if (sUriMatcher.match(uri) == DELETE_FAVORITE) {
            deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
