package com.example.todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.model.Todo;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insertTodo(Todo todo);

    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("DELETE FROM todo")
    void deleteAllTodos();

    @Query("DELETE FROM todo WHERE categoryID = :categoryID")
    void deleteAllCurrentCategoryTodos(int categoryID);

    @Query("DELETE FROM todo WHERE isCompleted = 1")
    void deleteAllCompletedTodos();

    @Query("UPDATE todo SET isCompleted = 1 WHERE id = :todoID")
    void completeTask(int todoID);

    @Query("SELECT * FROM todo WHERE categoryID = :categoryID")
    LiveData<List<Todo>> getAllTodosByCategory(int categoryID);

    @Query("SELECT * FROM todo WHERE id = :todoID")
    LiveData<Todo> getTodoByID(int todoID);
}
