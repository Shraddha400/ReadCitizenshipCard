package com.example.readcitizenshipcard.network;

import com.example.readcitizenshipcard.model.ImageResponse;
import com.example.readcitizenshipcard.model.TextResponse;
import com.example.readcitizenshipcard.utils.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @Multipart
    @POST(Constants.FACE)
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part part, @Part("citizenshipfront") RequestBody requestBody,
                                    @Part MultipartBody.Part part2, @Part("photo") RequestBody requestBody2);
    @Multipart
    @POST(Constants.TEXT)
    Call<TextResponse> uploadText(@Part MultipartBody.Part part, @Part("citizenshipback") RequestBody requestBody);
}
