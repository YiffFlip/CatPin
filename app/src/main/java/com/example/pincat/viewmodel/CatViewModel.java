package com.example.pincat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pincat.api.CatApiService;
import com.example.pincat.model.CatImage;
import com.example.pincat.network.RetrofitClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatViewModel extends ViewModel {
    private MutableLiveData<List<CatImage>> catImages = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Map<String, String> breedNameToId = new HashMap<>();

    private String currentBreedId = null;
    private int currentPage = 1;

    public LiveData<List<CatImage>> getCatImages() { return catImages; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void loadInitialData(int limit) {
        currentPage = 1;
        if(currentBreedId != null) {
            loadBreedImages(currentBreedId, limit, currentPage);
        } else {
            loadRandomImages(limit, currentPage);
        }
    }

    public void searchByBreedName(String breedName, int limit) {
        currentBreedId = breedNameToId.get(breedName.toLowerCase());
        currentPage = 1;
        if(currentBreedId != null) {
            loadBreedImages(currentBreedId, limit, currentPage);
        } else {
            errorMessage.setValue("Порода не найдена");
        }
    }

    public void loadMoreImages(int limit) {
        currentPage++;
        if(currentBreedId != null) {
            loadBreedImages(currentBreedId, limit, currentPage);
        } else {
            loadRandomImages(limit, currentPage);
        }
    }

    private void loadRandomImages(int limit, int page) {
        CatApiService apiService = RetrofitClient.getClient().create(CatApiService.class);
        apiService.getRandomCatImages(limit, page).enqueue(new Callback<List<CatImage>>() {
            @Override
            public void onResponse(Call<List<CatImage>> call, Response<List<CatImage>> response) {
                handleResponse(response);
            }
            @Override
            public void onFailure(Call<List<CatImage>> call, Throwable t) {
                handleError("Ошибка загрузки");
            }
        });
    }

    private void loadBreedImages(String breedId, int limit, int page) {
        CatApiService apiService = RetrofitClient.getClient().create(CatApiService.class);
        apiService.getImagesByBreed(breedId, limit, page).enqueue(new Callback<List<CatImage>>() {
            @Override
            public void onResponse(Call<List<CatImage>> call, Response<List<CatImage>> response) {
                handleResponse(response);
            }
            @Override
            public void onFailure(Call<List<CatImage>> call, Throwable t) {
                handleError("Ошибка загрузки породы");
            }
        });
    }

    private void handleResponse(Response<List<CatImage>> response) {
        if(response.isSuccessful() && response.body() != null) {
            if(currentPage == 1) {
                catImages.setValue(response.body());
            } else {
                List<CatImage> current = catImages.getValue();
                current.addAll(response.body());
                catImages.setValue(current);
            }
        }
    }

    private void handleError(String message) {
        errorMessage.setValue(message);
    }

    public void fetchAllBreeds() {
        CatApiService apiService = RetrofitClient.getClient().create(CatApiService.class);
        apiService.getAllBreeds().enqueue(new Callback<List<CatImage.Breed>>() {
            @Override
            public void onResponse(Call<List<CatImage.Breed>> call, Response<List<CatImage.Breed>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    for(CatImage.Breed breed : response.body()) {
                        breedNameToId.put(breed.getName().toLowerCase(), breed.getId());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CatImage.Breed>> call, Throwable t) {
                errorMessage.setValue("Ошибка загрузки пород");
            }
        });
    }
}