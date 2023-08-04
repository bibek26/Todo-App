package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String fragmentToLoad = intent.getStringExtra(("fragmentName"));

        if("TodoFragment".equals(fragmentToLoad)) {
            openTodoFragment();
        }
        else if("TodoFragmentForUpdate".equals(fragmentToLoad)) {
            openTodoFragmentForUpdate();
        }
        else if("CategoryListFragment".equals(fragmentToLoad)) {
            openCategoryListFragment();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void openTodoFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, TodoFragment.class, null)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void openCategoryListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryListFragment.class, null)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void openTodoFragmentForUpdate() {
        Intent intent = getIntent();

        TodoFragment todoFragment = new TodoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("todoID", intent.getStringExtra("todoID"));
        bundle.putString("todoTitle", intent.getStringExtra("todoTitle"));
        bundle.putString("todoDescription", intent.getStringExtra("todoDescription"));
        bundle.putString("todoDate", intent.getStringExtra("todoDate"));
        bundle.putBoolean("todoIsCompleted", intent.getBooleanExtra("todoIsCompleted", false));
        bundle.putString("todoPriority", intent.getStringExtra("todoPriority"));
        bundle.putString("todoCreatedDate", intent.getStringExtra("todoCreatedDate"));
        todoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, todoFragment, null)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
}