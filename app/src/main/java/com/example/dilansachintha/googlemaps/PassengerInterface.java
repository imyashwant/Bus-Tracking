package com.example.dilansachintha.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PassengerInterface extends AppCompatActivity {

    private static final String TAG = "PassengerMap";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String[] bus_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passenger);

        Button btn_route_search = (Button) findViewById(R.id.btn_search_route);
        Button btn_sign_out = (Button) findViewById(R.id.btn_sign_out);
        final TextInputEditText txt_route = (TextInputEditText)findViewById(R.id.txt_route_no);
        final Button btn_map = (Button) findViewById(R.id.btn_passenger_Map);
        Button btnPay = (Button) findViewById(R.id.btn_pay_passenger);

        btn_map.setVisibility(View.INVISIBLE);

        btn_route_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String route = txt_route.getText().toString();

                if(route == ""){
                    Toast.makeText(PassengerInterface.this, "Enter a Route No", Toast.LENGTH_SHORT).show();
                }else {
                    db.collection("routes").document(route).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Map<String,Object> routeMap = task.getResult().getData();
                                    if (routeMap != null) {
                                        Set set = routeMap.keySet();
                                        Iterator itr = set.iterator();

                                        LinearLayout routeLayout = (LinearLayout) findViewById(R.id.linear_layout_bus);

                                        bus_id = new String[set.size()];
                                        int i = 0;

                                        while (itr.hasNext()) {
                                            TextView textView = new TextView(PassengerInterface.this);
                                            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                                            String bus = (String) itr.next();

                                            textView.setText(bus);
                                            textView.setPadding(30, 20, 20, 20);// in pixels (left, top, right, bottom)
                                            routeLayout.addView(textView);

                                            bus_id[i] = bus;
                                            i++;
                                        }
                                        btn_map.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(PassengerInterface.this, "No route available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PassengerInterface.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PassengerInterface.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerInterface.this, Pay.class);
                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }

    private void init(){

        if(isServicesOK()){
            Intent intent = new Intent(PassengerInterface.this, PassengerMap.class);
            intent.putExtra("bus_id", bus_id);
            startActivity(intent);
        }else{

        }
    }

    public boolean isServicesOK(){
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PassengerInterface.this);
        if(availability == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PassengerInterface.this,availability,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
