package com.app.geo;

import androidx.fragment.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@SuppressLint({"SetTextI18n","MissingPermission"})
public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {

    // WayPoints
    private final LatLng lng_bab_chaafa = new LatLng(34.043950, -6.825168);
    private final LatLng lng_bab_mrisa = new LatLng(34.0329069, -6.8197752);
    private final LatLng lng_bab_sabta = new LatLng(34.0415435, -6.8210507);


    //Circles
    private Circle cl_lng_bab_sabta;
    private Circle cl_lng_bab_mrisa;
    private Circle cl_lng_bab_chaafa;

    //Utils
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private LatLng userLatlng;

    // UI
    private TextView userInfo;
    private ProgressDialog progressDialog;


    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        userInfo = findViewById(R.id.info);
        Button userBtn = findViewById(R.id.location);
        progressDialog = new ProgressDialog(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;

        setUpMap(mapFragment);

        mFusedLocationClient = new FusedLocationProviderClient(getBaseContext());

        Executors.newSingleThreadExecutor().execute(() -> {
            runOnUiThread(() -> {
                progressDialog.setMessage("Please wait while we set up the app");
                progressDialog.setCancelable(false);
                if(!isFinishing())
                   progressDialog.show();
            });
            try {
                getLastLocation();
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }
            runOnUiThread(() -> progressDialog.dismiss());
        });

        userBtn.setOnClickListener(view -> {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatlng,13));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatlng,13));
        });

    }

    private void setUpMap(SupportMapFragment mapFragment){
        try {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                initMarkers(mMap);
                mMap.setOnMarkerClickListener(this);

            });
        } catch (Exception exception) {
            Utils.showToast(getBaseContext(), "fatal error : "+exception.getLocalizedMessage());
            finish();
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        switch (marker.getTitle()){
            case "Bab Sabta":
                Utils.showDialog(this, "2nd Destination", "This place is called 'Bab Sabta'\nits the 2nd destination to explore the city of Salé", "ok",() -> {});
                break;
            case "Bab Mrissa":
                Utils.showDialog(this, "1st Destination", "This place is called 'Bab Mrissa'\nits the 1st destination to explore the city of Salé", "ok",() -> {});
                break;
            case "Bab Chaafa" :
                Utils.showDialog(this, "3rd Destination", "This place is called 'Bab Chaafa'\nits the 3rd destination to explore the city of Salé", "ok",() -> {});
                break ;
            case "Here you are" :
                Utils.showToast(this,"This is you !");
                break ;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void getLastLocation() {
        if (isLocationEnabled()) {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    try {
                        mMap.clear();
                        initMarkers(mMap);
                        final LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        userLatlng = currentLocation;
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Here you are").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10));
                        userInfo.setText(Utils.getAddressFromLat(this,currentLocation.latitude,currentLocation.longitude));
                        System.out.println("Location: "+location.toString());
                        checkProximity();
                    } catch (Exception exception) {
                        System.out.println("Error : "+exception.getLocalizedMessage());
                    }
                }
            });
        } else {
            Utils.showToast(this, "Please turn on your location...");
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            try {
                mMap.clear();
                initMarkers(mMap);
                final LatLng currentLocation = new LatLng(mLastLocation.getLongitude(), mLastLocation.getLongitude());
                userLatlng = currentLocation;
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Here you are").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                userInfo.setText(Utils.getAddressFromLat(getApplicationContext(),currentLocation.latitude,currentLocation.longitude));
                System.out.println("Location: "+mLastLocation.toString());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };

    private void checkProximity() {
        if(Utils.isAroundACircle(userLatlng,cl_lng_bab_chaafa)){
            System.out.print("You are around bab chafaa");
        }else if(Utils.isAroundACircle(userLatlng,cl_lng_bab_chaafa)){
            System.out.print("You are around bab chafaa");
        }else if(Utils.isAroundACircle(userLatlng,cl_lng_bab_mrisa)) {
            System.out.print("You are around bab mrisa");
        }else {
            System.out.println("You are out of circles");
        }
    }


}