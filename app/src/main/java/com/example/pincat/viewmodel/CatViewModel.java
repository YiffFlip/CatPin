package com.example.pincat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pincat.api.CatApiService;
import com.example.pincat.model.CatImage;
import com.example.pincat.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatViewModel extends ViewModel {
    private MutableLiveData<List<CatImage>> catImages = new MutableLiveData<>();

    public LiveData<List<CatImage>> getCatImages() {
        return catImages;
    }

    public void fetchRandomCatImages(int limit) {
        CatApiService apiService = RetrofitClient.getClient().create(CatApiService.class);
        Call<List<CatImage>> call = apiService.getRandomCatImages(limit);
        call.enqueue(new Callback<List<CatImage>>() {
            @Override
            public void onResponse(Call<List<CatImage>> call, Response<List<CatImage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    catImages.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CatImage>> call, Throwable t) {
                // Обработка ошибок
            }
        });
    }

    public void loadMoreCatImages(int limit) {
        CatApiService apiService = RetrofitClient.getClient().create(CatApiService.class);
        Call<List<CatImage>> call = apiService.getRandomCatImages(limit);
        call.enqueue(new Callback<List<CatImage>>() {
            @Override
            public void onResponse(Call<List<CatImage>> call, Response<List<CatImage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CatImage> currentList = catImages.getValue();
                    if (currentList != null) {
                        currentList.addAll(response.body());
                        catImages.setValue(currentList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatImage>> call, Throwable t) {
                // Обработка ошибок
            }
        });
    }
}
