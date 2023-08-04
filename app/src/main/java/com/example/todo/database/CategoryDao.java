package com.example.todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("DELETE FROM category")
    void deleteAllCategories();

    @Query("SELECT * FROM category ORDER BY category")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM category WHERE categoryID = :categoryID")
    LiveData<List<Category>> getCategoryByID(int categoryID);
}
