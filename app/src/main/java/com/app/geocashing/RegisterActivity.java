package com.app.geocashing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterStuff();
    }

    void RegisterStuff() {
        findViewById(R.id.btn_register).setOnClickListener(e -> {
            String uname = ((EditText) findViewById(R.id.input_register_uname)).getText().toString();
            String passwd = ((EditText) findViewById(R.id.input_register_passwd)).getText().toString();
            String passwd2 = ((EditText) findViewById(R.id.input_register_passwd2)).getText().toString();

            if (uname.length() < 5 || passwd.length() < 5) {
                makeToast("Fields Must have More than 5 caracters !");
                return;
            }
            if (!passwd.equals(passwd2)) {
                makeToast("Password don't match !");
                return;
            }

            // todo save to shared preferences and go to dashboard
            SharedPreferences sh = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("username", uname);
            editor.putString("password", passwd);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));


        });

        findViewById(R.id.btn_goto_sign_in).setOnClickListener(e ->
        {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }

    void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}