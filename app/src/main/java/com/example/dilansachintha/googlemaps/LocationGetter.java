package com.example.dilansachintha.googlemaps;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class LocationGetter {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Activity activity;
    private FirebaseFirestore dbase;
    private String[] bus_id;
    private GoogleMap gMap;

    public LocationGetter(FusedLocationProviderClient mFusedLocationProvider, Activity activity, FirebaseFirestore dbase, String[] bus_id, GoogleMap map){
        this.bus_id = bus_id;
        this.mFusedLocationProviderClient = mFusedLocationProvider;
        this.activity = activity;
        this.dbase = dbase;
        this.gMap = map;
    }

    public void getter(){
        for(int i=0;i<bus_id.length;i++){
            final DocumentReference docRef = dbase.collection("driver").document(bus_id[i]);

            final String mail = bus_id[i];

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

                final Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(6.5,79)).title(mail).visible(false).icon(BitmapDescriptorFactory.fromResource(R.mipmap.newbus)));

                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(activity, "Location get failed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        Map<String,Object> data = snapshot.getData();
                        GeoPoint loc = (GeoPoint) data.get("Location");

                        marker.setPosition(new LatLng(loc.getLatitude(), loc.getLongitude()));
                        marker.setVisible(true);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

}
