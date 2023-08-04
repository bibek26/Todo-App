package com.example.todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.database.AppDatabase;
import com.example.todo.database.Repository;
import com.example.todo.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Category>> allCategories;


    public CategoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
        allCategories = repository.getAllCategories();
    }

    public void insertCategory(Category category) {
        repository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        repository.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        repository.deleteCategory(category);
    }

    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return repository.getAllCategories();
    }

    public LiveData<List<Category>> getCategoryByID(int categoryID) {
        return repository.getCategoryByID(categoryID);
    }
}
