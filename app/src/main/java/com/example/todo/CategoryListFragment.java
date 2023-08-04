package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.CategoryAdapter;
import com.example.todo.viewmodel.CategoryViewModel;
import com.example.todo.viewmodel.TodoViewModel;

public class CategoryListFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private TodoViewModel todoViewModel;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_list, container, false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        // Initialize recycler view
        RecyclerView recyclerView = v.findViewById(R.id.fragment_category_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();

        // Initialize CategoryAdapter
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        // Initialize view models
        todoViewModel = new ViewModelProvider(getActivity()).get(TodoViewModel.class);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(getActivity(), categories -> categoryAdapter.setCategories(categories));

        performActionWhenSwiped(recyclerView, categoryAdapter);

        performActionOnItemClick();

        return v;
    }


    // Method to perform action based on swipe direction
    private void performActionWhenSwiped(RecyclerView recyclerView, CategoryAdapter categoryAdapter) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT) {
                    // Delete category
                    todoViewModel.deleteAllCurrentCategoryTodos(categoryAdapter.getCategoryAt(viewHolder.getAdapterPosition()).getCategoryID());
                    categoryViewModel.deleteCategory(categoryAdapter.getCategoryAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(getActivity(), "Category deleted!", Toast.LENGTH_SHORT).show();
                }
                else if(direction == ItemTouchHelper.LEFT) {
                    // Update category
                    position = viewHolder.getAdapterPosition();

                    CategoryFragment categoryFragment = new CategoryFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("categoryID", categoryAdapter.getCategoryAt(position).getCategoryID());
                    bundle.putString("categoryName", categoryAdapter.getCategoryAt(position).getCategory());
                    categoryFragment.setArguments(bundle);

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.activity_main_fragment_container, categoryFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }


    // Method to show todo lists of clicked category
    private void performActionOnItemClick() {
        categoryAdapter.setOnItemClickListener(category -> {
            Intent intent = new Intent(getActivity(), TodoListActivity.class);
            intent.putExtra("categoryID", String.valueOf(category.getCategoryID()));
            startActivity(intent);
        });
    }


    // Method to show options menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_category_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // Method to perform action when certain menu item is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.new_category_menu) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragment_container, CategoryFragment.class, null)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }
        else if(item.getItemId() == R.id.delete_all_categories) {
            if(categoryAdapter.getItemCount() > 0) {
                todoViewModel.deleteAllCurrentCategoryTodos(categoryAdapter.getCategoryAt(position).getCategoryID());
                categoryViewModel.deleteAllCategories();
                Toast.makeText(getActivity(), "All categories deleted!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "No categories to delete!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
//        else if (item.getItemId() == android.R.id.home) {
//            getActivity().finishAffinity();
//            return true;
//        }
        else if(item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.clear();
            spEditor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
