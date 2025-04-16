package com.example.pincat.api;

import com.example.pincat.model.CatImage;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatApiService {
    @GET("v1/images/search")
    Call<List<CatImage>> getRandomCatImages(
            @Query("limit") int limit,
            @Query("page") int page
    );

    @GET("v1/images/search")
    Call<List<CatImage>> getImagesByBreed(
            @Query("breed_ids") String breedId,
            @Query("limit") int limit,
            @Query("page") int page
    );

    @GET("v1/breeds")
    Call<List<CatImage.Breed>> getAllBreeds();
}