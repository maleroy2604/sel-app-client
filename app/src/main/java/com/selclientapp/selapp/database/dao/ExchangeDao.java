package com.selclientapp.selapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.selclientapp.selapp.database.entity.Exchange;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExchangeDao {

    @Insert(onConflict = REPLACE)
    void saveExchange(Exchange exchange);

    @Query("SELECT * FROM exchange")
    LiveData<List<Exchange>> getAllExchange();
}



