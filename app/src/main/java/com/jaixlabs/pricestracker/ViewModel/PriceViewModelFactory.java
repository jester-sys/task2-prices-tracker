package com.jaixlabs.pricestracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jaixlabs.pricestracker.Repository.PriceRepository;


public class PriceViewModelFactory implements ViewModelProvider.Factory {
    private final PriceRepository repository;

    public PriceViewModelFactory(PriceRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PriceViewModel.class)) {
            return (T) new PriceViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
