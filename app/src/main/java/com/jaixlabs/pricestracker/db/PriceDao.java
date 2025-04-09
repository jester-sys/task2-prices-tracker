package com.jaixlabs.pricestracker.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jaixlabs.pricestracker.model.Price;


import java.util.List;

@Dao
public interface PriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Price> transactions);

    @Query("SELECT * FROM price")
    LiveData<List<Price>> getAllTransactions();

    @Query("SELECT * FROM price")
    List<Price> getAllTransactionsOnce(); // for background sync
}
