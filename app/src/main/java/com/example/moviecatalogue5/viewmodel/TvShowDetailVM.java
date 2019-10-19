package com.example.moviecatalogue5.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.model.TvShowDetail;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailVM extends ViewModel {

    private static String TAG = TvShowDetailVM.class.getSimpleName();
    private MutableLiveData<TvShowDetail> dataTvShowDetail = new MutableLiveData<>();

    public MutableLiveData<TvShowDetail> getTvShowDetail() {
        return dataTvShowDetail;
    }

    public void setDataTvShowDetail(int id) {
        ApiService tvShowDetailApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<TvShowDetail> call = tvShowDetailApiService.getTvShowDetail(id, BuildConfig.ApiKey);

        call.enqueue(new Callback<TvShowDetail>() {
            @Override
            public void onResponse(Call<TvShowDetail> call, Response<TvShowDetail> response) {
                try {
                    assert response.body() != null;
                    dataTvShowDetail.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG + "-onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
