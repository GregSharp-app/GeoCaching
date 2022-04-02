package com.app.geocashing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityTabBarStuff();
    }
    void MainActivityTabBarStuff()
    {
        findViewById(R.id.btn_main_profile).setOnClickListener(e->{
            startActivity(new Intent(getApplicationContext() ,ProfileActivity.class));

        });

        findViewById(R.id.btn_main_logout).setOnClickListener(e->{

            startActivity(new Intent(getApplicationContext() ,LoginActivity.class));
            finish();
        });

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String uname = prefs.getString("username", "User");
        ((TextView)findViewById(R.id.txt_profile_username)).setText(uname);



}
}