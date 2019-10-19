package com.example.moviecatalogue5.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.activity.MovieDetailActivity;
import com.example.moviecatalogue5.adapter.GridFilmAdapter;
import com.example.moviecatalogue5.custom.RecyclerViewExtended;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.viewmodel.FavoriteVM;
import java.util.ArrayList;
import static com.example.moviecatalogue5.model.Favorite.TYPE_MOVIE;

public class FavMovieFragment extends Fragment {

    private Context context;
    private GridFilmAdapter gridAdapter;
    private RecyclerViewExtended rvMovies;
    private ArrayList<Film> listMovie;
    private FavoriteVM favoriteVM;

    private ProgressBar progressBar;
    private static String TYPE =  TYPE_MOVIE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_fav_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovies = view.findViewById(R.id.rv_fav_movie);
        progressBar = view.findViewById(R.id.progress_bar_fav_movie_grid);
        progressBar.setVisibility(View.VISIBLE);

        gridAdapter = new GridFilmAdapter();
        gridAdapter.setOnItemClickCallback(new GridFilmAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Film film) {
                showSelectedMovie(film);
            }
        });

        rvMovies.setHasFixedSize(true);
        rvMovies.setItemViewCacheSize(20);
        rvMovies.setDrawingCacheEnabled(true);
        rvMovies.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvMovies.setAdapter(gridAdapter);
        favoriteVM = new FavoriteVM();
        listMovie = new ArrayList<>();
        loadFavorite();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorite();
    }

    private void loadFavorite() {
        progressBar.setVisibility(View.VISIBLE);
        listMovie = favoriteVM.getAllFavoriteByType(true, context);
        gridAdapter.setData(listMovie);
        progressBar.setVisibility(View.GONE);
    }

    private void showSelectedMovie(Film film) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, film.getId());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TITLE, film.getTitle());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSTER, film.getPosterPath());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DATE, film.getReleaseDate());
        startActivity(intent);
    }
}
