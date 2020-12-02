package com.example.covidtrucking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtrucking.ui.organisations.OrganisationHelperClass;
import com.example.covidtrucking.ui.organisations.VisitOrgHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.Date;

public class ScannerActivity extends AppCompatActivity {
    TextView tv_value, nameView, locationView;
    Button btn_scan, btn_visit_scan;
    ImageView img_scanned_value, myImageViewIcon, logoView;
    final int CAMERA_PERMISSION_CODE=101;
    DatabaseReference dbRef;
    OrganisationHelperClass current_org;
    MaterialCardView org_scan_card;
    UserHelperClass user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        tv_value = findViewById(R.id.tv_scan_value);
        btn_scan = findViewById(R.id.btn_scan);
        img_scanned_value = findViewById(R.id.myImageViewQRCodeScan);
        myImageViewIcon = findViewById(R.id.myImageViewIcon);
        nameView = findViewById(R.id.tv_scan_org_name);
        locationView = findViewById(R.id.tv_scan_org_location);
        logoView = findViewById(R.id.img_org_scan);
        org_scan_card = findViewById(R.id.org_scan_card);
        btn_visit_scan = findViewById(R.id.btn_visit_scan);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    if(checkPermission(Manifest.permission.CAMERA)){
                        openScanner();
                    } else {
                        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    }

                } else {
                    openScanner();
                }
            }
        });

        btn_visit_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitOrgHelperClass visit = new VisitOrgHelperClass(current_org, user, new Date(), new Date(), true);
                dbRef.child(String.valueOf(System.currentTimeMillis())).setValue(visit)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ScannerActivity.this, "Visit " +current_org.getName()+ " is successfully register." , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    private void openScanner() {
        new IntentIntegrator(ScannerActivity.this).initiateScan();
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result.getContents() != null) {
            if(result.getContents() == null){
                Toast.makeText(this, "Blank!..", Toast.LENGTH_SHORT).show();
            } else {
                String res[] = result.getContents().split("_");
                if(res.length == 2) {
                    final String organisation_id = (String) Array.get(res, 1);
//                tv_value.setText("Organisation: " + result.getContents());
//                img_scanned_value.setImageResource(R.drawable.qr_code_image_active);
//                myImageViewIcon.setVisibility(View.VISIBLE);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Organisations");

                    dbRef = FirebaseDatabase.getInstance().getReference("Visits");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            OrganisationHelperClass org;
                            Boolean isAvailable = false;
                            final Boolean[] isUserAvailable = {false};
                            for (DataSnapshot snap:snapshot.getChildren()) {
                                org = snap.getValue(OrganisationHelperClass.class);
                                if (org.getOrg_id().equals(organisation_id)){
                                    isAvailable = true;
                                    current_org = org;
                                    tv_value.setText("QR code valid.");
                                    img_scanned_value.setImageResource(R.drawable.qr_code_image_active);
                                    myImageViewIcon.setVisibility(View.VISIBLE);
                                    nameView.setText(current_org.getName());
                                    locationView.setText(current_org.getLocation());
                                    org_scan_card.setVisibility(View.VISIBLE);

                                    if(org.getLogoImageUri() == null) {
                                        logoView.setImageResource(R.drawable.logo_placeholder);
                                    } else {
                                        Picasso.with(ScannerActivity.this)
                                                .load(org.getLogoImageUri())
                                                .into(logoView);
                                    }
                                    break;
                                }
                            }

                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            final FirebaseUser loggedInUser = mAuth.getCurrentUser();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap:snapshot.getChildren()) {
                                        UserHelperClass currentUser = snap.getValue(UserHelperClass.class);
                                        if (currentUser.getEmail().equals(loggedInUser.getEmail().toString())){
                                            isUserAvailable[0] = true;
                                            user = currentUser;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            if(!isAvailable){
                                tv_value.setText("Invalid QR code!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(this, "Invalid QR code!", Toast.LENGTH_SHORT).show();
                    tv_value.setText("Invalid QR code!");
                }

            }
        }else {
            Toast.makeText(this, "Blank!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(ScannerActivity.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission, int code) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ScannerActivity.this, permission)){

        } else {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{permission},code);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }
        }
    }

//    public UserHelperClass getCurrentUser(){
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        final FirebaseUser loggedInUser = mAuth.getCurrentUser();
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snap:snapshot.getChildren()) {
//                    UserHelperClass currentUser = snap.getValue(UserHelperClass.class);
//                    if (currentUser.getEmail().equals(loggedInUser.getEmail().toString())){
//                        user = currentUser;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}