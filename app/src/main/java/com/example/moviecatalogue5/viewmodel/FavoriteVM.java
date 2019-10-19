package com.example.moviecatalogue5.viewmodel;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.database.FavoriteHelper;
import com.example.moviecatalogue5.model.Favorite;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.widget.FavoriteFilmWidget;

import java.util.ArrayList;
import static com.example.moviecatalogue5.model.Favorite.TYPE_MOVIE;
import static com.example.moviecatalogue5.model.Favorite.TYPE_TV_SHOW;

public class FavoriteVM extends ViewModel {

    private FavoriteHelper favoriteHelper;
    public FavoriteVM() {
        super();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public boolean getFavoriteStatus(Favorite favorite, Context context) {
        boolean isFavorite = false;
        if (context != null) {
            favoriteHelper = FavoriteHelper.getInstance(context);
            favoriteHelper.open();
            if (favoriteHelper.isDatabaseOpen()) {
                isFavorite = favoriteHelper.selectIsExistFavorite(favorite);
            } else {
                isFavorite = false;
            }
        } else {
            isFavorite = false;
        }
        return isFavorite;
    }

    public boolean setFavoriteStatus(Favorite favorite, Boolean isFavorite, Context context) {

        if (context != null) {
            favoriteHelper = FavoriteHelper.getInstance(context);
            favoriteHelper.open();
            if (favoriteHelper.isDatabaseOpen()) {
                if (isFavorite) {
                    favoriteHelper.deleteFavorite(favorite);
                    isFavorite = false;
                } else {
                    favoriteHelper.insertFavorite(favorite);
                    isFavorite = true;
                }
                updateWidget(context);
                favoriteHelper.close();
            } else {
                // Error
            }
        } else {
            // Error
        }
        return isFavorite;
    }

    public ArrayList<Film> getAllFavoriteByType(boolean isMovie, Context context) {
        ArrayList<Film> listFilm = new ArrayList<>();
        String type = TYPE_MOVIE;
        if (context != null) {
            favoriteHelper = FavoriteHelper.getInstance(context);
            favoriteHelper.open();
            if (favoriteHelper.isDatabaseOpen()) {
                type = (isMovie) ? TYPE_MOVIE : TYPE_TV_SHOW;
                ArrayList<Favorite> favorites = favoriteHelper.getAllFavoriteByType(type);
                if (favorites != null) {
                    for (Favorite favorite : favorites) {
                        Film film = new Film();
                        film.setId(Integer.parseInt(favorite.getFilmId()));
                        film.setTitle(favorite.getTitle());
                        film.setPosterPath(favorite.getPosterPath());
                        film.setReleaseDate(favorite.getReleaseDate());
                        listFilm.add(film);
                    }
                    return listFilm;
                }
            }
        }
        return null;
    }

    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, FavoriteFilmWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }
}
