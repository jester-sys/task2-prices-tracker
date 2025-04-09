package com.jaixlabs.pricestracker.Api;


import com.jaixlabs.pricestracker.model.LoginRequest;
import com.jaixlabs.pricestracker.model.LoginResponse;
import com.jaixlabs.pricestracker.model.Price;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("prices")
    Call<List<Price>> getTransactions(@Header("Authorization") String token);
}
