package com.example.androidqbackgroundlocation;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Common {
    public static final String KEY_REQUESTING_LOCATION_UPDATES = "LocationUpdateEnable";

    //Keys
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_LATITUDE = "Latitude";


    public static String getLocationText(Location mLocation) {

        //Cloud Firestore

        String latitude =new StringBuilder()
                .append(mLocation.getLatitude())
                .toString();
        String longitude =new StringBuilder()
                .append(mLocation.getLongitude())
                .toString();;

//        Toast.makeText(getApplicationContext(),latitude,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),longitude,Toast.LENGTH_SHORT).show();

        Map<String, Object> user = new HashMap<>();

        user.put(KEY_LONGITUDE,longitude);
        user.put(KEY_LATITUDE,latitude);

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CriminalName")
                .document("Locations")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(getApplicationContext(),"Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Access a Cloud Firestore instance from your Activity
        // FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CriminalName")
                .document("Locations")
                .collection("LocationHistory")
                .document()
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(getApplicationContext(),"Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return mLocation == null ? "Unknown Location" : new StringBuilder()
                .append(mLocation.getLatitude())
                .append("/")
                .append(mLocation.getLongitude())
                .toString();
    }

    public static CharSequence getLocationTitle(MyBackgroundService myBackgroundService) {
        return String.format("Location Update: %1$s", DateFormat.getDateInstance().format(new Date()));
    }

    public static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.
                getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES,value)
                .apply();
    }

    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES,false);
    }
}
