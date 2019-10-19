package com.example.moviecatalogue5.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.model.Credit;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditVM extends ViewModel {

    private static String TAG = CreditVM.class.getSimpleName();
    private MutableLiveData<Credit> dataCredit = new MutableLiveData<>();

    public MutableLiveData<Credit> getCredit() {
        return dataCredit;
    }

    public void setDataCredit(int id, boolean isMovie) {
        ApiService creditApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<Credit> call;
        if (isMovie) {
            call = creditApiService.getMovieCredit(id, BuildConfig.ApiKey);
        } else {
            call = creditApiService.getTvShowCredit(id, BuildConfig.ApiKey);
        }

        call.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {
                try {
                    assert response.body() != null;
                    dataCredit.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG + "-onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
