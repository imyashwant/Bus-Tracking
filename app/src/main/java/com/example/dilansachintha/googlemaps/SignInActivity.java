package com.example.dilansachintha.googlemaps;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnSignUpDriver = (Button) findViewById(R.id.btn_sign_up_driver);
        Button btnSignUpPassenger = (Button) findViewById(R.id.btn_sign_up_passenger);
        final EditText txtEmail = (EditText) findViewById(R.id.txt_email);
        final EditText txtPassword = (EditText) findViewById(R.id.txt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SignInActivity.this, "Signing In...", Toast.LENGTH_SHORT).show();

                final String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    db.collection("users").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getId().equals(email)) {
                                                                Intent intent = new Intent(SignInActivity.this, PassengerInterface.class);
                                                                startActivity(intent);
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                    db.collection("driver").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getId().equals(email)) {
                                                                Intent intent = new Intent(SignInActivity.this, BusInterface.class);
                                                                startActivity(intent);
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignInActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnSignUpDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpDriver.class);
                startActivity(intent);
            }
        });
        btnSignUpPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpPassenger.class);
                startActivity(intent);
            }
        });
    }
}
