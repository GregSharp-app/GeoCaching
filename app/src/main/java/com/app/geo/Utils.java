package com.app.geo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public abstract class Utils {

    public static final String PREFERENCE_CODE = "user";

    public static void showToast(Context context , String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getPreferenceData(Context context , String key){

        SharedPreferences prefs = context.getSharedPreferences(Utils.PREFERENCE_CODE, Context.MODE_PRIVATE);

        return prefs.getString(key, null);
    }
    public static Boolean getPreferenceBoolean(Context context , String key){

        SharedPreferences prefs = context.getSharedPreferences(Utils.PREFERENCE_CODE, Context.MODE_PRIVATE);

        return prefs.getBoolean(key, false);
    }

    public static double getDistanceBetweenLatns(LatLng source , LatLng destination){

         return SphericalUtil.computeDistanceBetween(source, destination);

    }

    public static boolean isAroundACircle(LatLng latLng , Circle circle ){

        float[] distance = new float[2];

        Location.distanceBetween( latLng.latitude, latLng.longitude,
                circle.getCenter().latitude, circle.getCenter().longitude,distance);

        return !(distance[0] > circle.getRadius());
    }

    public static void showDialog(Context context , String title , String message , String positiveTxt , Runnable callback) {

        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(title).setMessage(message)
                .setPositiveButton(positiveTxt, (dialogInterface, i) -> callback.run()).create().show();

    }

    public static String getAddressFromLat(Context context ,double lat , double lon) throws IOException {

        Geocoder geocoder =  new Geocoder(context, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

        if(addresses.size() > 0){

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            return "address : "+address+"\ncity : "+city+"\nstate : "+state+"\ncountry : "+country;
        }

        return "";

    }
}
