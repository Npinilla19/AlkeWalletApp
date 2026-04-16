package com.example.alkewalletapp.data.api;

import com.example.alkewalletapp.model.Transaction;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("transactions")
    Call<List<Transaction>> getTransactions();

    @POST("transactions")
    Call<Transaction> createTransaction(@Body Transaction transaction);
}