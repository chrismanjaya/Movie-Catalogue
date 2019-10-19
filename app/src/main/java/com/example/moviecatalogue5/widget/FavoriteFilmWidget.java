package com.example.moviecatalogue5.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.activity.MovieDetailActivity;
import com.example.moviecatalogue5.activity.StackWidgetService;
import com.example.moviecatalogue5.activity.TvShowDetailActivity;
import com.example.moviecatalogue5.model.Film;

public class FavoriteFilmWidget extends AppWidgetProvider {

    public static final String FILM_DETAIL = "FILM_DETAIL";
    public static final String EXTRA_WIDGET_ID = "WIDGET_ID";
    public static final String EXTRA_WIDGET_TITLE = "WIDGET_TITLE";
    public static final String EXTRA_WIDGET_POSTER = "WIDGET_POSTER";
    public static final String EXTRA_WIDGET_DATE = "WIDGET_DATE";
    public static final String EXTRA_WIDGET_TYPE = "WIDGET_TYPE";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_film_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteFilmWidget.class);
        toastIntent.setAction(FavoriteFilmWidget.FILM_DETAIL);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        toastIntent.setData(Uri.parse(toastIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(FILM_DETAIL)) {
                Film film = new Film();
                film.setId(intent.getIntExtra(EXTRA_WIDGET_ID, 0));
                film.setTitle(intent.getStringExtra(EXTRA_WIDGET_TITLE));
                film.setPosterPath(intent.getStringExtra(EXTRA_WIDGET_POSTER));
                film.setReleaseDate(intent.getStringExtra(EXTRA_WIDGET_DATE));

                if (intent.getBooleanExtra(EXTRA_WIDGET_TYPE, true)) {
                    showSelectedMovie(context, film);
                } else {
                    showSelectedTvShow(context, film);
                }
            }
        }
    }

    private void showSelectedMovie(Context context, Film film) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, film.getId());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TITLE, film.getTitle());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSTER, film.getPosterPath());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DATE, film.getReleaseDate());
        context.startActivity(intent);
    }

    private void showSelectedTvShow(Context context, Film film) {
        Intent intent = new Intent(context, TvShowDetailActivity.class);
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_ID, film.getId());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_TITLE, film.getTitle());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_POSTER, film.getPosterPath());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_DATE, film.getReleaseDate());
        context.startActivity(intent);
    }
}

