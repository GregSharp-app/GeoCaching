package com.app.geocashing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginStuff();
    }

    void LoginStuff() {
        findViewById(R.id.btn_login).setOnClickListener(e -> {
            String uname = ((EditText) findViewById(R.id.input_login_uname)).getText().toString();
            String passwd = ((EditText) findViewById(R.id.input_login_passwd)).getText().toString();
            if (uname.length() < 5 || passwd.length() < 5) {
                makeToast("Fields Must have More than 5 caracters !");
                return;
            }

            // todo check shared preferences
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            String saved_uname = prefs.getString("username", "ksjkldghklsj");
            String saved_passwd = prefs.getString("password", "skjfhdjkhl");
            if (uname.equals(saved_uname) && passwd.equals(saved_passwd)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                makeToast("Incorrect Credentials !");
                return;
            }

        });

        findViewById(R.id.btn_goto_sign_up).setOnClickListener(e ->
        {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
    }

    void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}