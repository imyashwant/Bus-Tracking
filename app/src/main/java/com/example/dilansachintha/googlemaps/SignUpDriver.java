package com.example.dilansachintha.googlemaps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignUpDriver extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_driver);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final EditText txt_email = (EditText) findViewById(R.id.edit_text_email_driver);
        final EditText txt_password = (EditText) findViewById(R.id.edit_text_password_driver);
        final EditText txt_password2 = (EditText) findViewById(R.id.edit_text_confirm_password_driver);
        final EditText txt_route = (EditText) findViewById(R.id.edit_text_route);
        final EditText txt_seats = (EditText) findViewById(R.id.edit_text_seats);
        final EditText txt_bus_no = (EditText) findViewById(R.id.edit_text_bus_no);
        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up_driver2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
               String password2 = txt_password2.getText().toString();
                final String route = txt_route.getText().toString();
                final String seats = txt_seats.getText().toString();
                final String busNo = txt_bus_no.getText().toString();

                if(password.equals(password2)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpDriver.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> driver = new HashMap<>();
                                        driver.put("Location",new GeoPoint(0,0));
                                        driver.put("BusNum",busNo);
                                        driver.put("route",route);
                                        driver.put("Seats",seats);
                                        driver.put("Points", 0);

                                        db.collection("driver").document(email)
                                                .set(driver, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SignUpDriver.this,"Signed Up Successfully",Toast.LENGTH_SHORT).show();

                                                        Map<String,Object> routes = new HashMap<>();
                                                        routes.put(email,"");

                                                        db.collection("routes").document(route).set(routes,SetOptions.merge());

                                                        Intent intent = new Intent(SignUpDriver.this, SignInActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpDriver.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(SignUpDriver.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(SignUpDriver.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
