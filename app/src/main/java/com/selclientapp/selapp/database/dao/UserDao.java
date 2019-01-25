package com.selclientapp.selapp.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.selclientapp.selapp.database.entity.User;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void saveUser(User user);
}
