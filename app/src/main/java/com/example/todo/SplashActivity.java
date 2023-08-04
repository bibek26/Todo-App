package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
            String login = sharedPreferences.getString("LoginToken", null);
            Intent intent;

            if(Objects.isNull(login)) {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }

            startActivity(intent);

            SplashActivity.this.finish();
        }, 2000);
    }
}