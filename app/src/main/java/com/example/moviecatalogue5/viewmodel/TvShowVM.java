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

public class TvShowVM extends ViewModel {

    private MutableLiveData<ArrayList<Film>> listTvShow = new MutableLiveData<>();
    private static ArrayList<Film> allTvShows = new ArrayList<>();
    private ArrayList<Integer> listPage = new ArrayList<>();
    private int totalPage;
    private int currentPage;

    public MutableLiveData<ArrayList<Film>> getListTvShow() {
        return listTvShow;
    }

    public void setListTvShow(int page) {
        if (!listPage.contains(page)) {
            ApiService tvShowDataService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
            Call<FilmPageResult> call = tvShowDataService.getTvList(page, BuildConfig.ApiKey);
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
                        listPage.add(page);
                    } catch (Exception e) {
                        Log.d("-Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<FilmPageResult> call, Throwable t) {
                    Log.d("-onFailure", t.getMessage());
                }
            });
        }
    }

    public static ArrayList<Film> getAllTvShows() {
        return allTvShows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCountTvShows() {
        return allTvShows.size();
    }
}
