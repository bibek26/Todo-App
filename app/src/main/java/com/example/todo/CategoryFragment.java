package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo.model.Category;
import com.example.todo.viewmodel.CategoryViewModel;

public class CategoryFragment extends Fragment {
    private CategoryViewModel categoryViewModel;

    private TextView textViewBanner;
    private EditText etCategory;
    private Button btnAddCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        assignWidgets(v);

        // Get data from another fragment and update it
        Bundle bundle = getArguments();
        if(bundle != null) {
            textViewBanner.setText("Update the Category");
            btnAddCategory.setText("Update category");

            String categoryName = bundle.getString("categoryName");
            int categoryID = bundle.getInt("categoryID");

            etCategory.setText(categoryName);

            btnAddCategory.setOnClickListener(view -> {
                updateCategory(categoryID, etCategory.getText().toString());
            });
        }
        else {
            // When add category button is clicked
            btnAddCategory.setOnClickListener(view -> {
                Category category = new Category();

                if(etCategory.getText().toString().trim().isEmpty()) {
                    etCategory.setError("Category cannot be empty!");
                    return;
                }

                category.setCategory(etCategory.getText().toString());

                categoryViewModel.insertCategory(category);

                // Return to previous fragment
                getParentFragmentManager().popBackStack();
                Toast.makeText(getActivity(), "Category added!", Toast.LENGTH_SHORT).show();
            });
        }

        return v;
    }


    // Method to assign widgets
    private void assignWidgets(View view) {
        textViewBanner = view.findViewById(R.id.fragment_category_tv_category);
        etCategory = view.findViewById(R.id.fragment_category_et_category);
        btnAddCategory = view.findViewById(R.id.fragment_category_btn_add_category);
    }


    // Method to update category
    private void updateCategory(int categoryID, String categoryName) {
        if(categoryName.trim().isEmpty()) {
            etCategory.setError("Category cannot be empty!");
            return;
        }

        Category category = new Category();
        category.setCategoryID(categoryID);
        category.setCategory(categoryName);
        categoryViewModel.updateCategory(category);

        getParentFragmentManager().popBackStack();
        Toast.makeText(getActivity(), "Category updated!", Toast.LENGTH_SHORT).show();
    }
}