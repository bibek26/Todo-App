package com.example.todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.database.AppDatabase;
import com.example.todo.database.Repository;
import com.example.todo.model.User;

public class UserViewModel extends AndroidViewModel {
    private final Repository repository;


    public UserViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }

    public void registerUser(User user) {
        repository.registerUser(user);
    }

    public LiveData<User> getUserData(String username) {
        return repository.getUserData(username);
    }
}
