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

public class MovieVM extends ViewModel {

    private static String TAG = MovieVM.class.getSimpleName();

    private MutableLiveData<ArrayList<Film>> listMovie = new MutableLiveData<>();
    private static ArrayList<Film> allMovies = new ArrayList<>();
    private ArrayList<Integer> listPage = new ArrayList<>();
    private int totalPage;
    private int currentPage;

    public MutableLiveData<ArrayList<Film>> getListMovie() {
        return listMovie;
    }

    public void setListMovie(int page) {
        if (!listPage.contains(page)) {
            ApiService movieApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
            Call<FilmPageResult> call = movieApiService.getMovieList(page, BuildConfig.ApiKey);
            Log.d(TAG, "-URL: " + call.request().url().toString());

            call.enqueue(new Callback<FilmPageResult>() {
                @Override
                public void onResponse(Call<FilmPageResult> call, Response<FilmPageResult> response) {
                    try {
                        assert response.body() != null;
                        ArrayList<Film> items = response.body().getFilmResult();
                        for (Film item : items) {
                            allMovies.add(item);
                        }
                        listMovie.postValue(items);
                        totalPage = response.body().getTotalPage();
                        currentPage = response.body().getPage();
                        listPage.add(page);
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
    }

    public static ArrayList<Film> getAllMovies() {
        return allMovies;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCountMovies() {
        return allMovies.size();
    }
}
