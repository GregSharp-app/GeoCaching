package com.app.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.geo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnGotoSignUp.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        binding.btnLogin.setOnClickListener(view -> processLogin());
    }

    private void processLogin() {

        String username = binding.inputLoginUname.getText().toString();
        String passwd = binding.inputLoginPasswd.getText().toString();
        if (username.length() < 5 || passwd.length() < 5) {
            Utils.showToast(this,"Fields Must have More than 5 characters !");
            return;
        }
        if (username.equals(Utils.getPreferenceData(this,"username")) && passwd.equals(Utils.getPreferenceData(this,"password"))) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            Utils.showToast(this,"Incorrect Credentials !");
        }

    }

}