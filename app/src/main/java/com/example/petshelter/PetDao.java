package com.example.petshelter;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PetDao {

    @Insert
    void insert(Pet... pets);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet... pets);

    @Query("DELETE FROM pet_table")
    void deleteAll();

    @Query("SELECT * FROM pet_table")
    LiveData<List<Pet>> getAllPet();
}
