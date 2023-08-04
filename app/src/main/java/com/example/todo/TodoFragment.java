package com.example.todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo.model.Category;
import com.example.todo.model.Todo;
import com.example.todo.viewmodel.CategoryViewModel;
import com.example.todo.viewmodel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {
    private TodoViewModel todoViewModel;
    private CategoryViewModel categoryViewModel;

    private EditText etTitle;
    private EditText etDescription;
    private Spinner categorySpinner;
    private EditText etDate;
    private RadioGroup rg;
    private RadioButton rb;
    private CheckBox isCompleted;
    private Button btnAddTodo;

    private int todoID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        assignWidgets(view);


        Bundle bundle = getArguments();
        if(bundle != null) {
            btnAddTodo.setText("Update todo");

            // Get data
            getDataFromBundle(bundle, view);

            // When update todo button is clicked
            btnAddTodo.setOnClickListener(view1 -> {
                addUpdateTodo("update");
            });
        }
        else {
            // When add todo button is clicked
            btnAddTodo.setOnClickListener(view1 -> {
                addUpdateTodo("add");
            });
        }


        // Get categories and show to the spinner
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories) ;
                categorySpinner.setAdapter(adapter);
            }
        });

        // Choose date
        etDate.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    etDate.setText(day + "/" + (month+1) + "/" + year);
                }
            }, year, month, day);

            datePickerDialog.show();
        });

        return view;
    }


    // Method to assign widgets
    private void assignWidgets(View view) {
        etTitle = view.findViewById(R.id.fragment_todo_et_title);
        etDescription = view.findViewById(R.id.fragment_todo_et_description);
        categorySpinner = view.findViewById(R.id.fragment_todo_spinner);
        etDate = view.findViewById(R.id.fragment_todo_et_date);
        rg = view.findViewById(R.id.fragment_todo_rg);
        isCompleted = view.findViewById(R.id.fragment_todo_checkbox);
        btnAddTodo = view.findViewById(R.id.fragment_todo_btn_add_todo);
    }


    // Method to get data from bundle
    private void getDataFromBundle(Bundle bundle, View view) {
        todoID = Integer.parseInt(bundle.getString("todoID"));
        String title = bundle.getString("todoTitle");
        String description = bundle.getString("todoDescription");

        String completeDate = bundle.getString("todoDate");

        int priority = Integer.parseInt(bundle.getString("todoPriority"));
        boolean isComplete = bundle.getBoolean("todoIsCompleted");

        // Set data to widgets/controls
        setDataToWidgets(title, description, completeDate, isComplete, priority, view);
    }


    // Method to set data to widgets
    private void setDataToWidgets(String title, String description, String completeByDate, boolean isComplete, int priority, View view) {
        etTitle.setText(title);
        etDescription.setText(description);

        // Set Date - couldn't do it as of this moment

        // Set checkbox
        isCompleted.setChecked(isComplete);

        // Set radiobutton
        if(priority == 0) {
            rb = view.findViewById(R.id.fragment_todo_rb_high);
            rb.setChecked(true);
        }
        else if(priority == 1) {
            rb = view.findViewById(R.id.fragment_todo_rb_medium);
            rb.setChecked(true);
        }
        else {
            rb = view.findViewById(R.id.fragment_todo_rb_low);
            rb.setChecked(true);
        }
    }


    // Method to add or update todo
    private void addUpdateTodo(String operation) {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        Category category = (Category) categorySpinner.getSelectedItem();

        String todoDate = etDate.getText().toString();
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todoDateOn = null;
        try {
            todoDateOn = dateFormatter.parse(todoDate);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }

        Date createdOn = new Date();

        int priority = 0;
        int checkedRadio = rg.getCheckedRadioButtonId();
        if(checkedRadio == R.id.fragment_todo_rb_high) {
            priority = 0;
        }
        else if(checkedRadio == R.id.fragment_todo_rb_medium) {
            priority = 1;
        }
        else {
            priority = 2;
        }

        boolean isComplete = isCompleted.isChecked();


        if(title.isEmpty()) {
            etTitle.setError("Title cannot be empty!");
            return;
        }
        else if(description.isEmpty()) {
            etDescription.setError("Description cannot be empty!");
            return;
        }
        else if(todoDateOn == null) {
            etDate.setError("Date cannot be empty!");
            return;
        }
        else if(rg.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Choose a priority!", Toast.LENGTH_SHORT).show();
            return;
        }



        Todo todo = new Todo(title, description, todoDateOn, isComplete, priority, createdOn, category.getCategoryID());

        if(operation.equals("add")) {
            todoViewModel.insertTodo(todo);
            Toast.makeText(getActivity(), "Todo Added!", Toast.LENGTH_SHORT).show();
        }
        else if(operation.equals("update")) {
            todo.setId(todoID);
            todoViewModel.updateTodo(todo);
            Toast.makeText(getActivity(), "Todo Updated!", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getContext(), TodoListActivity.class);
        intent.putExtra("categoryID", String.valueOf(category.getCategoryID()));
        startActivity(intent);
    }
}