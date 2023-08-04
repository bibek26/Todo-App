package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo.viewmodel.UserViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        assignWidgets();

        // When register button is clicked
        btnRegister.setOnClickListener(v -> {
            etUsername.setText("");
            etPassword.setText("");

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // When login button is clicked
        btnLogin.setOnClickListener(v -> validateLoginData());
    }


    // Method to assign widgets
    private void assignWidgets() {
        etUsername = findViewById(R.id.activity_login_et_username);
        etPassword = findViewById(R.id.activity_login_et_password);
        btnLogin = findViewById(R.id.activity_login_btn_login);
        btnRegister = findViewById(R.id.activity_login_btn_register);
    }


    // Method to validate login data
    private void validateLoginData() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(username.isEmpty()) {
            etUsername.setError("Username cannot be empty!");
            return;
        }
        else if(password.isEmpty()) {
            etPassword.setError("Password cannot be empty!");
            return;
        }


        userViewModel.getUserData(username).observe(this, user -> {
            if(user != null) {
                String dbPassword = Objects.requireNonNull(user).getPassword();

                if(password.equals(dbPassword)) {
                    login();
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Password incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to login user
    private void login() {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("LoginToken", "LoggedIn");
        spEditor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}