package com.app.geo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.app.geo.databinding.ActivityMaps2Binding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private LatLng userLng;
    private LocationManager locationManger;
    private LocationListener locationListener;

    // WayPoints
    private final LatLng lng_bab_chaafa = new LatLng(34.043950, -6.825168);
    private final LatLng lng_bab_mrisa = new LatLng(34.0329069, -6.8197752);
    private final LatLng lng_bab_sabta = new LatLng(34.0415435, -6.8210507);


    //Circles
    private Circle cl_lng_bab_sabta;
    private Circle cl_lng_bab_mrisa;
    private Circle cl_lng_bab_chaafa;

    // UI
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationManger = (LocationManager) getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("You reached the catch zone ...");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        initMarkers(mMap);
        if (isGPSEnabled()) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    mMap.clear();
                    initMarkers(mMap);
                    userLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLng));
                    if (Utils.isAroundACircle(userLng, cl_lng_bab_chaafa)) {
                        cl_lng_bab_chaafa.setFillColor(Color.RED);
                        new Handler().postDelayed(() -> {
                            if (!isFinishing()) {
                                progressDialog.show();
                            }
                            // go to questions activity for 1st catch
                            Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
                            i.putExtra("id_catch", 0);
                            startActivity(i);

                        }, 4000);
                    } else if (Utils.isAroundACircle(userLng, cl_lng_bab_sabta)) {
                        cl_lng_bab_sabta.setFillColor(Color.RED);
                        new Handler().postDelayed(() -> {
                            if (!isFinishing()) {
                                progressDialog.show();
                            }
                            // go to questions activity for 2nd catch
                            Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
                            i.putExtra("id_catch", 1);
                            startActivity(i);

                        }, 4000);
                    } else if (Utils.isAroundACircle(userLng, cl_lng_bab_mrisa)) {
                        cl_lng_bab_mrisa.setFillColor(Color.RED);
                        new Handler().postDelayed(() -> {
                            if (!isFinishing()) {
                                progressDialog.show();
                            }
                            // go to questions activity for 3rd catch
                            Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
                            i.putExtra("id_catch", 2);
                            startActivity(i);
                        }, 4000);

                    }
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

        } else {

            Utils.showToast(this, "Please enable GPS");

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }


    }

    public void initMarkers(GoogleMap googleMap) {

        cl_lng_bab_chaafa = googleMap.addCircle(new CircleOptions()
                .center(lng_bab_chaafa)
                .clickable(true)
                .radius(100)
                .strokeColor(Color.BLACK)
                .fillColor(Color.LTGRAY));

        cl_lng_bab_chaafa.setTag(0);

        cl_lng_bab_mrisa = googleMap.addCircle(new CircleOptions()
                .center(lng_bab_mrisa)
                .clickable(true)
                .radius(200)
                .strokeColor(Color.BLACK)
                .fillColor(Color.LTGRAY));

        cl_lng_bab_mrisa.setTag(0);

        cl_lng_bab_sabta = googleMap.addCircle(new CircleOptions()
                .center(lng_bab_sabta)
                .clickable(true)
                .radius(100)
                .strokeColor(Color.BLACK)
                .fillColor(Color.LTGRAY));

        cl_lng_bab_sabta.setTag(0);


        List<Circle> circles = new ArrayList<>();

        circles.add(cl_lng_bab_chaafa);
        circles.add(cl_lng_bab_mrisa);
        circles.add(cl_lng_bab_sabta);

        for (Circle m : circles) {
            LatLng currentItemLng = new LatLng(m.getCenter().latitude, m.getCenter().longitude);
            mMap.addCircle(new CircleOptions().center(currentItemLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentItemLng, 15));
        }

    }

    private boolean isGPSEnabled() {
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}