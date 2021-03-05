package com.example.readcitizenshipcard.network;

import com.example.readcitizenshipcard.model.ImageResponse;
import com.example.readcitizenshipcard.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET(Constants.VOLUMES)
    Call<ImageResponse> getSearchBooks(@Query(("q")) String bookName,
                                       @Query(("maxResults")) int limit, @Query(("printType")) String printType);

}
