package com.example.moviecatalogue5.database;

import android.database.Cursor;

public interface LoadFavoriteCallback {
    void preExecute();
    void postExecute(Cursor films);
}
