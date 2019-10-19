package com.example.moviecatalogue5.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.model.Trailer;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerVM extends ViewModel {
    private static String TAG = TrailerVM.class.getSimpleName();
    private MutableLiveData<Trailer> dataTrailer = new MutableLiveData<>();

    public MutableLiveData<Trailer> getTrailer() {
        return dataTrailer;
    }

    public void setDataTrailer(int id, boolean isMovie) {
        ApiService trailerApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<Trailer> call;
        if (isMovie) {
            call = trailerApiService.getMovieTrailer(id, BuildConfig.ApiKey);
        } else {
            call = trailerApiService.getTvShowTrailer(id, BuildConfig.ApiKey);
        }

        call.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                try {
                    assert response.body() != null;
                    dataTrailer.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG + "-onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
