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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        if(user !=null){
            getType(user);
        }else{
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }
    public void getType(FirebaseUser user){
        final FirebaseUser currentUser = user;

        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String email = currentUser.getEmail();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getId().equals(email)) {
                                    Intent intent = new Intent(MainActivity.this, PassengerInterface.class);
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
                            String email = currentUser.getEmail();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getId().equals(email)) {
                                    Intent intent = new Intent(MainActivity.this, BusInterface.class);
                                    startActivity(intent);
                                    return;
                                }
                            }
                        }
                    }
                });
    }
}
