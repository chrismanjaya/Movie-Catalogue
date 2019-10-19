package com.example.moviecatalogue5.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.model.FilmPageResult;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowSearchVM extends ViewModel {

    private static String TAG = TvShowSearchVM.class.getSimpleName();

    private MutableLiveData<ArrayList<Film>> listTvShow = new MutableLiveData<>();
    private static ArrayList<Film> allTvShows = new ArrayList<>();
    private int totalPage;
    private int currentPage;

    public MutableLiveData<ArrayList<Film>> getListTvShow() {
        return listTvShow;
    }

    public void setListSearchTvShow(String keyword) {
        ApiService tvShowApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<FilmPageResult> call = tvShowApiService.getSearchTv(keyword, BuildConfig.ApiKey);
        Log.d(TAG, "-URL: " + call.request().url().toString());

        call.enqueue(new Callback<FilmPageResult>() {
            @Override
            public void onResponse(Call<FilmPageResult> call, Response<FilmPageResult> response) {
                try {
                    assert response.body() != null;
                    ArrayList<Film> items = response.body().getFilmResult();
                    for (Film item : items) {
                        allTvShows.add(item);
                    }
                    listTvShow.postValue(items);
                    totalPage = response.body().getTotalPage();
                    currentPage = response.body().getPage();
                } catch (Exception e) {
                    Log.d(TAG + "-exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FilmPageResult> call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCountMovies() {
        return allTvShows.size();
    }
}

