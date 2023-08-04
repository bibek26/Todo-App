package com.example.todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.database.AppDatabase;
import com.example.todo.database.Repository;
import com.example.todo.model.Todo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final Repository repository;


    public TodoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }

    public void insertTodo(Todo todo) {
        repository.insertTodo(todo);
    }

    public void updateTodo(Todo todo) {
        repository.updateTodo(todo);
    }

    public void deleteTodo(Todo todo) {
        repository.deleteTodo(todo);
    }

    public void deleteAllTodos() {
        repository.deleteAllTodos();
    }

    public void deleteAllCurrentCategoryTodos(int categoryID) {
        repository.deleteAllCurrentCategoryTodos(categoryID);
    }

    public void deleteAllCompletedTodos() {
        repository.deleteAllCompletedTodos();
    }

    public void completeTask(int todoID) {
        repository.completeTask(todoID);
    }

    public LiveData<List<Todo>> getAllTodosByCategory(int categoryID) {
        return repository.getAllTodosByCategory(categoryID);
    }

    public LiveData<Todo> getTodoByID(int todoID) {
        return repository.getTodoByID(todoID);
    }
}
