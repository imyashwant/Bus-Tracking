package com.example.dilansachintha.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;


public class availablebuses extends AppCompatActivity {

    private static final String TAG = "PassengerMap";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String[] bus_id;
    private Button reservebtn;
    private TextView txtseat;
    //Button newbtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_availablebuses);

        //Button btn_route_search = (Button) findViewById(R.id.search);
        //Button btn_sign_out = (Button) findViewById(R.id.btn_sign_out);
        reservebtn = (Button) findViewById(R.id.btnbook);
        txtseat = (TextView) findViewById(R.id.seats);
        //final TextInputEditText txt_route = (TextInputEditText)findViewById(R.id.txt_route_no);
        //final Button btn_map = (Button) findViewById(R.id.btn_passenger_Map);
//        reservebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                db.collection("users").document("")
//            }
//        });

        Intent intent = getIntent();
        final String str = intent.getStringExtra("parameter");

        //final String route = txt_route.getText().toString();

        db.collection("driver").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                //Toast.makeText(PassengerMap.this, document.getId(), Toast.LENGTH_SHORT).show();

                                Map<String,Object> data = document.getData();
                                //System.out.println(data.containsKey("Bus")+"fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

                                List<String> group = (List<String>) data.get(str);
                                Toast.makeText(availablebuses.this, "Found the Route No", Toast.LENGTH_SHORT).show();

                                Toast.makeText(availablebuses.this, group.get(0), Toast.LENGTH_SHORT).show();

                                LinearLayout routeLayout = (LinearLayout) findViewById(R.id.linear_layout_bus);

                                bus_id = new String[group.size()];
                                int j = 0;
                                for(int i=0; i< group.size()-1;i++){
                                    TextView textView = new TextView(availablebuses.this);
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    textView.setText(group.get(i));
                                    //addButton(group.get(i));
                                    textView.setPadding(30, 20, 20, 20);// in pixels (left, top, right, bottom)
                                    routeLayout.addView(textView);
                                    j=i;
                                    bus_id[i] =(String) group.get(i);
                                }

                                txtseat.setText("Remaining Seats : " + group.get(j+1));
//                                if(document.getId().equals(route)){
//
//                                    //btn_map.setVisibility(View.VISIBLE);
//
//
//                                }else{
//                                    Toast.makeText(availablebuses.this, "Coudn't find a Route No", Toast.LENGTH_SHORT).show();
//                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        //btn_map.setVisibility(View.INVISIBLE);
//        reservebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                txtseat.setText("Remaining Seats : ");
//            }
//        });
//        btn_route_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
////                if(route == ""){
////                    Toast.makeText(availablebuses.this, "Enter a Route No", Toast.LENGTH_SHORT).show();
////                }else {
////
////                }
//
//
//            }
//        });

//        btn_sign_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(availablebuses.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btn_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                init();
//            }
//        });



    }

//    public void addButton(final String name){
//        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_bus);
//        newbtn = new Button(this);
//        newbtn.setText(name);
//        layout.addView(newbtn);
//        newbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent bookintent = new Intent(PassengerInterface.this,availablebuses.class);
//                bookintent.putExtra("parameter", name);
//                startActivity(bookintent);
//            }
//        });
//    }


    private void init(){

        if(isServicesOK()){
            Intent intent = new Intent(availablebuses.this, PassengerMap.class);
            intent.putExtra("bus_id", bus_id);
            startActivity(intent);
        }else{

        }
    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking google services version");
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(availablebuses.this);
        if(availability == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            Toast.makeText(availablebuses.this,"Google Play Services are working",Toast.LENGTH_SHORT).show();
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(availablebuses.this,availability,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
