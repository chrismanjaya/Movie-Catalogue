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
import com.example.moviecatalogue5.activity.TvShowDetailActivity;
import com.example.moviecatalogue5.adapter.GridFilmAdapter;
import com.example.moviecatalogue5.custom.RecyclerViewExtended;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.viewmodel.FavoriteVM;
import java.util.ArrayList;
import static com.example.moviecatalogue5.model.Favorite.TYPE_TV_SHOW;

public class FavTvFragment extends Fragment {

    private Context context;
    private GridFilmAdapter gridAdapter;
    private RecyclerViewExtended rvTvShow;
    private ArrayList<Film> listTvShow;
    private FavoriteVM favoriteVM;

    private ProgressBar progressBar;
    private static String TYPE =  TYPE_TV_SHOW;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_fav_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTvShow = view.findViewById(R.id.rv_fav_tv);
        progressBar = view.findViewById(R.id.progress_bar_fav_tv_grid);
        progressBar.setVisibility(View.VISIBLE);

        gridAdapter = new GridFilmAdapter();
        gridAdapter.setOnItemClickCallback(new GridFilmAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Film film) {
                showSelectedTvShow(film);
            }
        });

        rvTvShow.setHasFixedSize(true);
        rvTvShow.setItemViewCacheSize(20);
        rvTvShow.setDrawingCacheEnabled(true);
        rvTvShow.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvTvShow.setAdapter(gridAdapter);
        favoriteVM = new FavoriteVM();
        listTvShow = new ArrayList<>();
        loadFavorite();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorite();
    }

    private void loadFavorite() {
        progressBar.setVisibility(View.VISIBLE);
        listTvShow = favoriteVM.getAllFavoriteByType(false, context);
        gridAdapter.setData(listTvShow);
        progressBar.setVisibility(View.GONE);
    }

    private void showSelectedTvShow(Film film) {
        Intent intent = new Intent(getActivity(), TvShowDetailActivity.class);
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_ID, film.getId());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_TITLE, film.getTitle());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_POSTER, film.getPosterPath());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_DATE, film.getReleaseDate());
        startActivity(intent);
    }
}
