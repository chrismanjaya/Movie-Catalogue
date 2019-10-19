package com.example.moviecatalogue5.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.model.MovieDetail;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailVM extends ViewModel {

    private static String TAG = MovieDetailVM.class.getSimpleName();
    private MutableLiveData<MovieDetail> dataMovieDetail = new MutableLiveData<>();
    public MutableLiveData<MovieDetail> getMovieDetail() {
        return dataMovieDetail;
    }

    public void setDataMovieDetail(int id) {
        ApiService movieDetailApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<MovieDetail> call = movieDetailApiService.getMovieDetail(id, BuildConfig.ApiKey);

        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                try {
                    assert response.body() != null;
                    dataMovieDetail.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG + "-onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
