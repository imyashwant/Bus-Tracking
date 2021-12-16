package com.example.dilansachintha.googlemaps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpPassenger extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_passenger);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final EditText txt_email = (EditText) findViewById(R.id.edit_text_email_passenger);
        final EditText txt_password = (EditText) findViewById(R.id.edit_text_password_passenger);
        final EditText txt_password2 = (EditText) findViewById(R.id.edit_text_confirm_password_passenger);
        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up_passenger2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                String password2 = txt_password2.getText().toString();

                if(password.equals(password2)){

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpPassenger.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Points", 100);

                                        db.collection("users").document(email)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SignUpPassenger.this,"Signed Up Successfully",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpPassenger.this, SignInActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpPassenger.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(SignUpPassenger.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(SignUpPassenger.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
