package com.example.raisecure.home.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.raisecure.home.model.entity.Secret;

import java.util.List;

@Dao
public interface SecretDao {

    @Query("delete  from secret where secretId =:id")
    void deleteItem(long id);

    @Update
    void updateItem(Secret secret);

    @Query("select * from secret where secretId=:id ")
    Secret getItem(long id);

    @Query("select * from secret ")
    List<Secret> getAllItem();

    @Insert
    long insertItem(Secret secret);
}
