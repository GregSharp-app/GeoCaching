package com.app.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.geo.databinding.ActivityLoginBinding;
import com.app.geo.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnGotoSignIn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
        binding.btnRegister.setOnClickListener(view -> processRegistration());
    }

    private void processRegistration(){
        String username = binding.inputRegisterUname.getText().toString();
        String passwd = binding.inputRegisterPasswd.getText().toString();
        String passwdConfirmed = binding.inputRegisterPasswd2.getText().toString();

        if (username.length() < 5 || passwd.length() < 5) {
            Utils.showToast(this,"Fields Must have More than 5 characters !");
            return;
        }
        if (!passwd.equals(passwdConfirmed)) {
            Utils.showToast(this,"The password and its confirmation do not match !");
            return;
        }

        SharedPreferences sh = getSharedPreferences(Utils.PREFERENCE_CODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.putString("username", username);
        editor.putString("password", passwd);
        editor.apply();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }


}