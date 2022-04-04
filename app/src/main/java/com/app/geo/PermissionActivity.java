package com.app.geo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("Welcome")
                    .setMessage("Dear user ! \nThe application needs the following permission from you to be granted in order to function .\n -Location And GPS")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        Dexter.withContext(getBaseContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                            processToNext();
                                        } else {
                                            Utils.showToast(getBaseContext(), "Sorry! can't run the app");
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();
                    }).create().show();
        } else {
           processToNext();
        }
    }

        private void processToNext(){

            if(Utils.getPreferenceData(PermissionActivity.this,"username") != null){
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(PermissionActivity.this, MapsActivity.class));
                    finish();
                }, 300);
            }else {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
                    finish();
                }, 300);
            }

        }


    }
