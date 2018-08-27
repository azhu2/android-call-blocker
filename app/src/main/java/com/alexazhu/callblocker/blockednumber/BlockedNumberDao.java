package com.alexazhu.callblocker.blockednumber;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BlockedNumberDao {
    @Query("SELECT * FROM blockednumbers")
    List<BlockedNumber> getAll();

    @Insert
    void insert(BlockedNumber number);

    @Delete
    void delete(BlockedNumber number);
}
