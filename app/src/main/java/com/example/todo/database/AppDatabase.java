package com.example.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.todo.model.Category;
import com.example.todo.model.Todo;
import com.example.todo.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(exportSchema = false, version = 1, entities = {Todo.class, Category.class, User.class})
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "todo_database")
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

    public abstract TodoDao todoDao();

    public abstract CategoryDao categoryDao();

    public abstract UserDao userDao();
}
