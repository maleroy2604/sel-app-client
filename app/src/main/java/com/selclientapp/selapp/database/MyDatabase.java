package com.selclientapp.selapp.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.selclientapp.selapp.database.dao.ExchangeDao;
import com.selclientapp.selapp.database.dao.UserDao;
import com.selclientapp.selapp.database.entity.Exchange;
import com.selclientapp.selapp.database.entity.User;

@Database(entities = {User.class, Exchange.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile MyDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();

    public abstract ExchangeDao exchangeDao();

}
