package com.example.moviecatalogue5.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.viewmodel.FavoriteVM;
import com.example.moviecatalogue5.widget.FavoriteFilmWidget;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import static com.example.moviecatalogue5.utility.utils.formatYear;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = StackRemoteViewsFactory.class.getSimpleName();

    private ArrayList<FavoriteBanner> favoriteBanners;
    private final Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        FavoriteVM favoriteVM = new FavoriteVM();
        favoriteBanners = new ArrayList<>();
        ArrayList<Film> movieItems = favoriteVM.getAllFavoriteByType(true, context);
        ArrayList<Film> tvShowItems = favoriteVM.getAllFavoriteByType(false, context);

        for (Film film : movieItems) {
            FavoriteBanner favoriteBanner = new FavoriteBanner();
            favoriteBanner.setFilm(film);
            favoriteBanner.setIsMovie(true);
            favoriteBanners.add(favoriteBanner);
        }

        for (Film film : tvShowItems) {
            FavoriteBanner favoriteBanner = new FavoriteBanner();
            favoriteBanner.setFilm(film);
            favoriteBanner.setIsMovie(false);
            favoriteBanners.add(favoriteBanner);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return favoriteBanners.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String filmTitle, filmDate, filmPoster;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        if (favoriteBanners.size() > 0) {
            FavoriteBanner favoriteBanner = this.favoriteBanners.get(position);
            filmPoster = favoriteBanner.getFilm().getPosterPath();
            filmTitle = favoriteBanner.getFilm().getTitle();
            filmDate = favoriteBanner.getFilm().getReleaseDate();

            try {
                Bitmap bitmap = Picasso.with(context).load(movieImagePathBuilder(filmPoster)).get();
                remoteViews.setImageViewBitmap(R.id.img_item_poster_widget, bitmap);

            } catch (IOException e) {
                Log.d(TAG + "-IOException", e.getMessage());
            }

            remoteViews.setTextViewText(R.id.text_title_widget, filmTitle);
            remoteViews.setTextViewText(R.id.text_date_widget, formatYear(filmDate));

            Bundle extras = new Bundle();
            extras.putBoolean(FavoriteFilmWidget.EXTRA_WIDGET_TYPE, favoriteBanner.isMovie);
            extras.putInt(FavoriteFilmWidget.EXTRA_WIDGET_ID, favoriteBanner.getFilm().getId());
            extras.putString(FavoriteFilmWidget.EXTRA_WIDGET_POSTER, filmPoster);
            extras.putString(FavoriteFilmWidget.EXTRA_WIDGET_TITLE, filmTitle);
            extras.putString(FavoriteFilmWidget.EXTRA_WIDGET_DATE, filmDate);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            remoteViews.setOnClickFillInIntent(R.id.img_item_poster_widget, fillInIntent);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private String movieImagePathBuilder(String imagePath) {
        String IMAGE_URL = "https://image.tmdb.org/t/p/";
        String POSTER_SIZE = "w500";
        return IMAGE_URL + POSTER_SIZE + imagePath;
    }

    class FavoriteBanner {

        public Film film;
        public boolean isMovie;

        public Film getFilm() {
            return film;
        }

        public void setFilm(Film film) {
            this.film = film;
        }

        public boolean isMovie() {
            return isMovie;
        }

        public void setIsMovie(boolean movie) {
            isMovie = movie;
        }
    }
}
