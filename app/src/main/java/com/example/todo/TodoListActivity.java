package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.TodoAdapter;
import com.example.todo.model.Todo;
import com.example.todo.viewmodel.TodoViewModel;

public class TodoListActivity extends AppCompatActivity {
    private TodoViewModel todoViewModel;

    private TodoAdapter todoAdapter;

    private int categoryID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCategoryIdFromIntent();

        // Initialize recycler view
        RecyclerView recyclerView = findViewById(R.id.activity_todo_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        // Initialize TodoAdapter
        todoAdapter = new TodoAdapter();
        recyclerView.setAdapter(todoAdapter);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getAllTodosByCategory(categoryID).observe(this, todos -> todoAdapter.setTodos(todos));

        performActionWhenSwiped(recyclerView, todoAdapter);

        performActionOnItemClick();
    }


    // Method to get category id from the intent
    private void getCategoryIdFromIntent() {
        Intent intent = getIntent();
        if(intent != null) categoryID = Integer.parseInt(intent.getStringExtra("categoryID"));
    }


    // Method to perform action based on swipe direction
    private void performActionWhenSwiped(RecyclerView recyclerView, TodoAdapter todoAdapter) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT) {
                    // Delete todo
                    todoViewModel.deleteTodo(todoAdapter.getTodoAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(getApplicationContext(), "Todo deleted!", Toast.LENGTH_SHORT).show();
                }
                else if(direction == ItemTouchHelper.LEFT) {
                    // Complete item
                    int position = viewHolder.getAdapterPosition();

                    Todo todo = todoAdapter.getTodoAt(position);
                    todoViewModel.completeTask(todo.getId());
//                    todo.setCompleted(true);
//                    todoViewModel.updateTodo(todo);
                    todoAdapter.notifyDataSetChanged();
                    Toast.makeText(TodoListActivity.this, "Task set to complete!", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }


    // Method to update todo item
    private void performActionOnItemClick() {
        // Update todo
        todoAdapter.setOnItemClickListener(todo -> {
            Intent intent = new Intent(TodoListActivity.this, MainActivity.class);
            intent.putExtra("fragmentName", "TodoFragmentForUpdate");
            intent.putExtra("todoID", String.valueOf(todo.getId()));
            intent.putExtra("todoTitle", todo.getTitle());
            intent.putExtra("todoDescription", todo.getDescription());
            intent.putExtra("todoDate", String.valueOf(todo.getTodoDate()));
            intent.putExtra("todoIsCompleted", todo.isCompleted());
            intent.putExtra("todoPriority", String.valueOf(todo.getPriority()));
            intent.putExtra("todoCreatedDate", String.valueOf(todo.getCreatedOn()));
            startActivity(intent);
        });
    }


    // Method to show options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_todo_menu, menu);
        return true;
    }


    // Method to perform action when certain menu item is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_new_todo) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragmentName", "TodoFragment");
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.delete_all_todos) {
            if(todoAdapter.getItemCount() > 0) {
                todoViewModel.deleteAllTodos();
                Toast.makeText(this, "All todos deleted!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "No todos to delete!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if(item.getItemId() == R.id.delete_all_completed_todos) {
            todoViewModel.deleteAllCompletedTodos();
            Toast.makeText(this, "All completed todos deleted!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(item.getItemId() == R.id.delete_all_todos_of_current_category) {
            todoViewModel.deleteAllCurrentCategoryTodos(categoryID);
            Toast.makeText(this, "All todos deleted in current category!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.clear();
            spEditor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragmentName", "CategoryListFragment");
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}