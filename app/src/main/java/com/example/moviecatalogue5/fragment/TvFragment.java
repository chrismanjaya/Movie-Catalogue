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
import com.example.moviecatalogue5.activity.SettingsActivity;
import com.example.moviecatalogue5.activity.TvShowDetailActivity;
import com.example.moviecatalogue5.adapter.GridFilmAdapter;
import com.example.moviecatalogue5.custom.RecyclerViewExtended;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.viewmodel.TvShowSearchVM;
import com.example.moviecatalogue5.viewmodel.TvShowVM;
import java.util.ArrayList;
import static android.content.Context.SEARCH_SERVICE;

public class TvFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private ProgressBar progressBar;

    private static String TAG = TvFragment.class.getSimpleName();
    private GridFilmAdapter gridAdapter;
    private Context context;
    private RecyclerViewExtended rvTvShow;
    private ArrayList<Film> listTvShow = new ArrayList<>();
    private ArrayList<Film> listTvShowSearch = new ArrayList<>();
    private TvShowVM tvShowVM;
    private TvShowSearchVM tvShowSearchVM;

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
        return inflater.inflate(R.layout.fragment_tv, container, false);
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

        rvTvShow = view.findViewById(R.id.rv_tv);
        progressBar = view.findViewById(R.id.progress_bar_tv_grid);
        progressBar.setVisibility(View.VISIBLE);

        tvShowVM = ViewModelProviders.of((FragmentActivity) context).get(TvShowVM.class);
        tvShowVM.getListTvShow().observe((LifecycleOwner) context, getTvShow);
        listTvShow.addAll(tvShowVM.getAllTvShows());

        tvShowSearchVM = ViewModelProviders.of((FragmentActivity) context).get(TvShowSearchVM.class);
        tvShowSearchVM.getListTvShow().observe((LifecycleOwner) context, getSearchTvShow);

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

        loadMore(rvTvShow);
        if (isSearch) {
            gridAdapter.setData(listTvShowSearch);
        } else {
            gridAdapter.setData(listTvShow);
            if (tvShowVM.getCountTvShows() == 0) tvShowVM.setListTvShow(1);
        }
    }

    private Observer<ArrayList<Film>> getTvShow = new Observer<ArrayList<Film>>() {

        @Override
        public void onChanged(ArrayList<Film> tvShows) {
            if (tvShows != null) {
                for (Film tvShow : tvShows) {
                    listTvShow.add(tvShow);
                }
                gridAdapter.setData(listTvShow);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private Observer<ArrayList<Film>> getSearchTvShow = new Observer<ArrayList<Film>>() {

        @Override
        public void onChanged(ArrayList<Film> tvShows) {
            if (tvShows != null) {
                for (Film tvShow : tvShows) {
                    listTvShowSearch.add(tvShow);
                }
                gridAdapter.setData(listTvShowSearch);
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
                    int page = tvShowVM.getCurrentPage();
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
                        tvShowVM.setListTvShow(page + 1);
                        gridAdapter.notifyItemInserted(totalItemCount);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void showSelectedTvShow(Film film) {
        Intent intent = new Intent(getActivity(), TvShowDetailActivity.class);
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_ID, film.getId());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_TITLE, film.getName());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_POSTER, film.getPosterPath());
        intent.putExtra(TvShowDetailActivity.EXTRA_TV_DATE, film.getFirstAirDate());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "-onCreateOptionsMenu");
        inflater.inflate(R.menu.options_menu_tv, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search_tv);
        searchMenuItem.setOnActionExpandListener(this);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_tv));

        if (searchKey != null && !searchKey.isEmpty()) {
            searchMenuItem.expandActionView();
            searchView.setQuery(searchKey, true);
        }

        SearchManager searchManager = (SearchManager) context.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint_tv_show));
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
        gridAdapter.setData(listTvShow);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        Log.d(TAG, "-onQueryTextSubmit");
        progressBar.setVisibility(View.VISIBLE);

        isSearch = true;
        searchKey = keyword;
        listTvShowSearch = new ArrayList<>();

        tvShowSearchVM.setListSearchTvShow(keyword);
        tvShowSearchVM.getListTvShow().observe((LifecycleOwner) context, getSearchTvShow);
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
        if (item.getItemId() == R.id.action_settings_tv) {
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
        listTvShowSearch = new ArrayList<>();
        isSearch = false;
        searchKey = null;
    }
}