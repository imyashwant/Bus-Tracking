package com.example.dilansachintha.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BusInterface extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private String user;

    private static final String TAG = "BusInterface";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_interface);

        user = mAuth.getCurrentUser().getEmail();

        Button btnMap = (Button) findViewById(R.id.btn_bus_map);
        Button btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        Button refresh = (Button) findViewById(R.id.refresh);

        final TextView Points = (TextView) findViewById(R.id.points);

        db.collection("driver").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if (document.getId().equals(user)) {
                                    String pointStr = "";
                                    if (document.getLong("Points") != null)
                                        pointStr = document.getLong("Points").toString();
                                    Points.setText("My Points: " + pointStr);

                                } else {}
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("driver").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        if (document.getId().equals(user)) {
                                            String pointStr = "";
                                            if (document.getLong("Points") != null)
                                                pointStr = document.getLong("Points").toString();
                                            Points.setText("My Points: " + pointStr);

                                        } else {}
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                MapUpdater.check = false;
                Intent intent = new Intent(BusInterface.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init(){

        if(isServicesOK()){
            Intent intent = new Intent(BusInterface.this, BusMap.class);
            startActivity(intent);
        }
    }

    public boolean isServicesOK(){
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(BusInterface.this);
        if(availability == ConnectionResult.SUCCESS){
            Toast.makeText(BusInterface.this,"Google Play Services are working",Toast.LENGTH_SHORT).show();
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(BusInterface.this,availability,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
