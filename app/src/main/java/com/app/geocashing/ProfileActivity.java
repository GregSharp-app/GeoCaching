package com.app.geocashing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String uname = prefs.getString("username", "User");
        ((TextView) findViewById(R.id.txt_profile_username)).setText(uname);

        findViewById(R.id.btn_delete_account).setOnClickListener(e -> deleteAccount());
        findViewById(R.id.btn_profile_back).setOnClickListener(e -> {
            onBackPressed();
        });

    }

    void deleteAccount() {
        // todo remove from shared preferences
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}