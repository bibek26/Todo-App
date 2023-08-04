package com.example.todo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo.model.User;
import com.example.todo.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirm_password;
    private Button btnRegister;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        assignWidgets();

        // Register button is clicked
        btnRegister.setOnClickListener(v -> validateAndRegister());

        // Cancel button is clicked
        btnCancel.setOnClickListener(v -> closeApp());
    }


    // Method to assign widgets
    private void assignWidgets() {
        etUsername = findViewById(R.id.activity_register_et_username);
        etPassword = findViewById(R.id.activity_register_et_password);
        etConfirm_password = findViewById(R.id.activity_register_et_confirm_password);
        btnRegister = findViewById(R.id.activity_login_btn_login);
        btnCancel = findViewById(R.id.activity_login_btn_register);
    }


    // Validate user data and register
    private void validateAndRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm_password = etConfirm_password.getText().toString().trim();

        if(username.isEmpty()) {
            etUsername.setError("Username cannot be empty!");
            return;
        }
        else if(password.isEmpty()) {
            etPassword.setError("Password cannot be empty!");
            return;
        }
        else if(confirm_password.isEmpty()) {
            etConfirm_password.setError("Confirm password cannot be empty!");
            return;
        }

        if(!password.equals(confirm_password)) {
            Toast.makeText(this, "Password must be same!", Toast.LENGTH_LONG).show();
        }
        else {
            User user = new User(username, password);
            userViewModel.registerUser(user);
            finish();
        }
    }


    // Method to close app
    private void closeApp() {
        // Close all activities.
        finishAffinity();
        System.exit(0);
    }


    // Method to handle menu items click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}