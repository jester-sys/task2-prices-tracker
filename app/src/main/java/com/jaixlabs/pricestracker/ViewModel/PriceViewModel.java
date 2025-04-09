package com.jaixlabs.pricestracker.ViewModel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.jaixlabs.pricestracker.Repository.PriceRepository;
import com.jaixlabs.pricestracker.model.LoginResponse;
import com.jaixlabs.pricestracker.model.Price;

import java.util.List;

import retrofit2.Response;

public class PriceViewModel extends ViewModel {
    private PriceRepository repository;
    private MutableLiveData<List<Price>> transactions = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable priceUpdater;

    public PriceViewModel(PriceRepository repo) {
        this.repository = repo;
    }

    public LiveData<Response<LoginResponse>> login(String username, String password) {
        return repository.login(username, password);
    }

    public void loadTransactions(String token) {
        repository.fetchTransactions(token).observeForever(transactions::setValue);
    }

    public LiveData<List<Price>> getTransactions() {
        return transactions;
    }
    public LiveData<List<Price>> getLocalTransactions() {
        return repository.getTransactionsFromDb();
    }
    public void startRealTimeUpdates(String token) {
        priceUpdater = new Runnable() {
            @Override
            public void run() {
                repository.fetchTransactions(token).observeForever(prices -> {
                    if (prices != null) {
                        transactions.setValue(prices);
                    }
                });
                handler.postDelayed(this, 5000); // Every 5 seconds
            }
        };
        handler.post(priceUpdater);
    }

    public void stopRealTimeUpdates() {
        if (handler != null && priceUpdater != null) {
            handler.removeCallbacks(priceUpdater);
        }
    }

}
