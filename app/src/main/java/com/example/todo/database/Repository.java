package com.example.todo.database;

import androidx.lifecycle.LiveData;

import com.example.todo.model.Category;
import com.example.todo.model.Todo;
import com.example.todo.model.User;

import java.util.List;

public class Repository {
    private final TodoDao todoRepository;
    private final CategoryDao categoryRepository;
    private final UserDao userRepository;


    public Repository(AppDatabase appDatabase) {
        todoRepository = appDatabase.todoDao();
        categoryRepository = appDatabase.categoryDao();
        userRepository = appDatabase.userDao();
    }

    // Todo section
    public void insertTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.insertTodo(todo);
        });
    }

    public void updateTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.updateTodo(todo);
        });
    }

    public void deleteTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteTodo(todo);
        });
    }

    public void deleteAllTodos() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAllTodos();
        });
    }

    public void deleteAllCurrentCategoryTodos(int categoryID) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAllCurrentCategoryTodos(categoryID);
        });
    }

    public void deleteAllCompletedTodos() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAllCompletedTodos();
        });
    }

    public void completeTask(int todoID) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.completeTask(todoID);
        });
    }

    public LiveData<List<Todo>> getAllTodosByCategory(int categoryID) {
        return todoRepository.getAllTodosByCategory(categoryID);
    }

    public LiveData<Todo> getTodoByID(int todoID) {
        return todoRepository.getTodoByID(todoID);
    }


    // Category section
    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepository.insertCategory(category);
        });
    }

    public void updateCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepository.updateCategory(category);
        });
    }

    public void deleteCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepository.deleteCategory(category);
        });
    }

    public void deleteAllCategories() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepository.deleteAllCategories();
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public LiveData<List<Category>> getCategoryByID(int categoryID) {
        return categoryRepository.getCategoryByID(categoryID);
    }


    // User section
    public void registerUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userRepository.registerUser(user);
        });
    }

    public LiveData<User> getUserData(String username) {
        return userRepository.getUserData(username);
    }
}
