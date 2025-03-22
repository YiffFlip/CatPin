package com.example.pincat.api;

import com.example.pincat.model.CatImage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface CatApiService {
    @GET("v1/images/search")
    Call<List<CatImage>> getRandomCatImages(@Query("limit") int limit);
}