package com.example.dilansachintha.googlemaps;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pay extends AppCompatActivity {
    private Long myPoint;
    private String user;
    private static final String TAG = "Pay";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        user = mAuth.getCurrentUser().getEmail();

        Button search = (Button) findViewById(R.id.search);
        final Button payBtn = (Button) findViewById(R.id.payBtn);

        final EditText route = (EditText) findViewById(R.id.route);

        final TextView result = (TextView) findViewById(R.id.result);
        final EditText amount = (EditText) findViewById(R.id.amount);
        final Spinner spinner1 = (Spinner) findViewById(R.id.planets_spinner);
        final TextView text = (TextView) findViewById(R.id.text);


        spinner1.setVisibility(View.INVISIBLE);
        amount.setVisibility(View.INVISIBLE);
        payBtn.setVisibility(View.INVISIBLE);
        result.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);

        final TextView Points = (TextView) findViewById(R.id.Points);

        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                        Toast.makeText(Pay.this, document.getId(), Toast.LENGTH_SHORT).show();

                                if (document.getId().equals(user)) {
//
                                    Points.setText("My Points: " + document.getLong("Points").toString());
                                    myPoint = document.getLong("Points");
                                } else {}

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(route.getText())) {


                    result.setText("Route is required!");
                    result.setVisibility(View.VISIBLE);
                    spinner1.setVisibility(View.INVISIBLE);
                    amount.setVisibility(View.INVISIBLE);
                    payBtn.setVisibility(View.INVISIBLE);

                    text.setVisibility(View.INVISIBLE);


                } else {
                    result.setVisibility(View.INVISIBLE);
//                    final List<String> bus = new ArrayList<String>();
                    final ArrayList<String> bus = new ArrayList<String>();


                    db.collection("driver").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());

//                                            Toast.makeText(Pay.this, document.getId(), Toast.LENGTH_SHORT).show();

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                if ((Objects.equals(document.getString("route"), route.getText().toString()))) {
    //                                                Toast.makeText(Pay.this, document.getString("BusNum"), Toast.LENGTH_SHORT).show();
                                                    bus.add(document.getString("BusNum"));
    //

                                                }
                                            }
                                        }
                                        if (!(bus.isEmpty())) {

                                            Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);


                                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                                    Pay.this, android.R.layout.simple_spinner_item, bus);

                                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                                    .simple_spinner_dropdown_item);
                                            spinner.setAdapter(spinnerArrayAdapter);

                                            payBtn.setVisibility(View.VISIBLE);
                                            spinner1.setVisibility(View.VISIBLE);
                                            amount.setVisibility(View.VISIBLE);
                                            text.setVisibility(View.VISIBLE);


                                            payBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    db.collection("users").get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            Log.d(TAG, document.getId() + " => " + document.getData());

//                                        Toast.makeText(Pay.this, document.getId(), Toast.LENGTH_SHORT).show();

                                                                            if (document.getId().equals(user)) {
//                                            Toast.makeText(Pay.this, "Found the Route No", Toast.LENGTH_SHORT).show();

//
                                                                                Points.setText("My Points: " + document.getLong("Points").toString());
                                                                                myPoint = document.getLong("Points");
                                                                            } else {
//                                            Toast.makeText(Pay.this, "Coudn't find a Route No", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }
                                                                    } else {
                                                                        Log.w(TAG, "Error getting documents.", task.getException());
                                                                    }
                                                                }
                                                            });


                                                    if (TextUtils.isEmpty(amount.getText())) {


                                                        result.setText("Amount is required!");
                                                        result.setVisibility(View.VISIBLE);


                                                    }
                                                    else if ((myPoint-(Long.parseLong(amount.getText().toString())))<0){
                                                        result.setText("Not Enough funds!");
                                                        result.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
//
                                                        final Long val = Long.parseLong(amount.getText().toString());
                                                        final Long res = myPoint - val;
                                                        Points.setText("My Points: " + res);

                                                        DocumentReference washingtonRef = db.collection("users").document(user);


                                                        washingtonRef
                                                                .update("Points", res)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error updating document", e);
                                                                    }
                                                                });


                                                        db.collection("driver").get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                        Toast.makeText(Pay.this, document.getId(), Toast.LENGTH_SHORT).show();

                                                                                if (document.getString("BusNum").equals(spinner1.getSelectedItem().toString())) {
//                                            Toast.makeText(Pay.this, "Found the Route No", Toast.LENGTH_SHORT).show();

                                                                                    Long val1 = Long.parseLong(document.getLong("Points").toString());
                                                                                    Long res1 = val + val1;


                                                                                    DocumentReference washingtonRef = db.collection("driver").document(document.getId());


                                                                                    washingtonRef
                                                                                            .update("Points", res1)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Log.w(TAG, "Error updating document", e);
                                                                                                }
                                                                                            });

                                                                                    result.setText("Successfully Paid To " + document.getString("BusNum").toString());
                                                                                    result.setVisibility(View.VISIBLE);

                                                                                } else {
//                                            Toast.makeText(Pay.this, "Coudn't find a Route No", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        } else {
                                                                            Log.w(TAG, "Error getting documents.", task.getException());
                                                                        }
                                                                    }
                                                                });

                                                    }


                                                }
                                            });


                                        } else {
                                            result.setText("Invalid Route or no active buses in the route!");
                                            result.setVisibility(View.VISIBLE);
                                            spinner1.setVisibility(View.INVISIBLE);
                                            amount.setVisibility(View.INVISIBLE);
                                            payBtn.setVisibility(View.INVISIBLE);

                                            text.setVisibility(View.INVISIBLE);

                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            }
        });


    }
}