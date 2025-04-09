package com.jaixlabs.pricestracker.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jaixlabs.pricestracker.Api.ApiClient;
import com.jaixlabs.pricestracker.Api.ApiService;
import com.jaixlabs.pricestracker.db.AppDatabase;
import com.jaixlabs.pricestracker.db.PriceDao;
import com.jaixlabs.pricestracker.model.LoginRequest;
import com.jaixlabs.pricestracker.model.LoginResponse;
import com.jaixlabs.pricestracker.model.Price;


import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.*;

public class PriceRepository {
    private final ApiService apiService;
    private final PriceDao transactionDao;
    private final SharedPreferences sharedPreferences;

    public PriceRepository(Context context) {
        this.apiService = ApiClient.getRetrofit().create(ApiService.class);
        AppDatabase db = AppDatabase.getInstance(context);
        this.transactionDao = db.transactionDao();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public LiveData<Response<LoginResponse>> login(String username, String password) {
        MutableLiveData<Response<LoginResponse>> result = new MutableLiveData<>();

        apiService.login(new LoginRequest(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                result.postValue(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                result.postValue(null);
            }
        });

        return result;
    }

    public LiveData<List<Price>> fetchTransactions(String token) {
        MutableLiveData<List<Price>> result = new MutableLiveData<>();

        apiService.getTransactions(token).enqueue(new Callback<List<Price>>() {
            @Override
            public void onResponse(Call<List<Price>> call, Response<List<Price>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Price> apiData = response.body();
                    result.postValue(apiData);

                    // ✅ Check if already synced using last_sync_id or hash
                    int lastSavedId = sharedPreferences.getInt("last_transaction_id", -1);
                    int newLastId = getMaxId(apiData);

                    if (newLastId != lastSavedId) {
                        // ✅ Insert into Room DB
                        Executors.newSingleThreadExecutor().execute(() -> {
                            transactionDao.insertAll(apiData);
                            sharedPreferences.edit().putInt("last_transaction_id", newLastId).apply();
                        });
                    }
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Price>> call, Throwable t) {
                result.postValue(null);
            }
        });

        return result;
    }

    private int getMaxId(List<Price> transactions) {
        int max = -1;
        for (Price t : transactions) {
            int id = t.getId(); // already int
            if (id > max) {
                max = id;
            }
        }
        return max;
    }

    public LiveData<List<Price>> getTransactionsFromDb() {
        return transactionDao.getAllTransactions();
    }
}
