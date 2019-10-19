package com.example.moviecatalogue5.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.activity.MovieDetailActivity;
import com.example.moviecatalogue5.activity.SettingsActivity;
import com.example.moviecatalogue5.adapter.GridFilmAdapter;
import com.example.moviecatalogue5.custom.RecyclerViewExtended;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.viewmodel.MovieSearchVM;
import com.example.moviecatalogue5.viewmodel.MovieVM;
import java.util.ArrayList;
import static android.content.Context.SEARCH_SERVICE;

public class MovieFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private ProgressBar progressBar;

    private static String TAG = MovieFragment.class.getSimpleName();
    private GridFilmAdapter gridAdapter;
    private Context context;
    private RecyclerViewExtended rvMovies;
    private ArrayList<Film> listMovie = new ArrayList<>();
    private ArrayList<Film> listMovieSearch = new ArrayList<>();
    private MovieVM movieVM;
    private MovieSearchVM movieSearchVM;

    private int visibleThreshold = 5;
    private int previousTotalItemCount = 0;
    private int totalItemCount;
    private Boolean isLoading = true;

    private Boolean isSearch;
    private String searchKey;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "-onCreateView");
        context = container.getContext();
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            setSearch(savedInstanceState);
        } else {
            initSearch();
        }
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "-onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSearch", isSearch);
        outState.putString("searchKey", searchKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "-onViewCreated");

        if (savedInstanceState != null) {
            setSearch(savedInstanceState);
        } else {
            initSearch();
        }

        rvMovies = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar_movie_grid);
        progressBar.setVisibility(View.VISIBLE);

        movieVM = ViewModelProviders.of((FragmentActivity) context).get(MovieVM.class);
        movieVM.getListMovie().observe((LifecycleOwner) context, getMovies);
        listMovie.addAll(MovieVM.getAllMovies());

        movieSearchVM = ViewModelProviders.of((FragmentActivity) context).get(MovieSearchVM.class);
        movieSearchVM.getListMovie().observe((LifecycleOwner) context, getSearchMovies);

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

        loadMore(rvMovies);
        if (isSearch) {
            gridAdapter.setData(listMovieSearch);
        } else {
            gridAdapter.setData(listMovie);
            if (movieVM.getCountMovies() == 0) movieVM.setListMovie(1);
        }

    }

    private Observer<ArrayList<Film>> getMovies = new Observer<ArrayList<Film>>() {

        @Override
        public void onChanged(ArrayList<Film> movies) {
            if (movies != null) {
                for (Film movie : movies) {
                    listMovie.add(movie);
                }
                gridAdapter.setData(listMovie);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private Observer<ArrayList<Film>> getSearchMovies = new Observer<ArrayList<Film>>() {

        @Override
        public void onChanged(ArrayList<Film> movies) {
            if (movies != null) {
                for (Film movie : movies) {
                    listMovieSearch.add(movie);
                }
                gridAdapter.setData(listMovieSearch);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private void loadMore(RecyclerViewExtended recyclerViewExtended) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerViewExtended.getLayoutManager();
        recyclerViewExtended.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!isSearch) {
                    int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                    int page = movieVM.getCurrentPage();
                    totalItemCount = gridLayoutManager.getItemCount();

                    if (totalItemCount < previousTotalItemCount) {
                        previousTotalItemCount = totalItemCount;
                        if (totalItemCount == 0) {
                            isLoading = true;
                        }
                    }

                    if (isLoading && (totalItemCount > previousTotalItemCount)) {
                        isLoading = false;
                        previousTotalItemCount = totalItemCount;
                    }

                    if (!isLoading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                        movieVM.setListMovie(page + 1);
                        gridAdapter.notifyItemInserted(totalItemCount);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void showSelectedMovie(Film film) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, film.getId());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TITLE, film.getTitle());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSTER, film.getPosterPath());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DATE, film.getReleaseDate());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "-onCreateOptionsMenu");
        inflater.inflate(R.menu.options_menu_movie, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search_movie);
        searchMenuItem.setOnActionExpandListener(this);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_movie));

        if (searchKey != null && !searchKey.isEmpty()) {
            searchMenuItem.expandActionView();
            searchView.setQuery(searchKey, true);
        }

        SearchManager searchManager = (SearchManager) context.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint_movie));
        searchView.clearFocus();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        Log.d(TAG, "-onMenuItemActionExpand");
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        Log.d(TAG, "-onMenuItemActionCollapse");
        initSearch();
        gridAdapter.setData(listMovie);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        Log.d(TAG, "-onQueryTextSubmit");
        progressBar.setVisibility(View.VISIBLE);

        isSearch = true;
        searchKey = keyword;
        listMovieSearch = new ArrayList<>();

        movieSearchVM.setListSearchMovie(searchKey);
        movieSearchVM.getListMovie().observe((LifecycleOwner) context, getSearchMovies);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            Log.d(TAG, "Reset Search...");
            searchView.clearFocus();
            return true;
        }
        Log.d(TAG, "Do Search...");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings_movie) {
            Intent mIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(mIntent);
        }
        return true;
    }

    private void setSearch(Bundle bundle) {
        isSearch = bundle.getBoolean("isSearch");
        searchKey = bundle.getString("searchKey");
    }

    private void initSearch() {
        listMovieSearch = new ArrayList<>();
        isSearch = false;
        searchKey = null;
    }
}