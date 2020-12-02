package com.example.covidtrucking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtrucking.ui.organisations.EditOrganisationProfileActivity;
import com.example.covidtrucking.ui.organisations.OrganisationHelperClass;
import com.example.covidtrucking.ui.organisations.OrganisationsActivity;
import com.example.covidtrucking.ui.organisations.OrganisationsItemAdapter;
import com.example.covidtrucking.ui.organisations.UserListAdapter;
import com.example.covidtrucking.ui.organisations.VisitOrgHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

public class OrganisationActivity extends AppCompatActivity {
    OrganisationHelperClass current_org;
    private TextView nameView, descriptionView, locationView;
    ImageView logoView, img_gen_qr_code;
    Button btn_edit_org, btn_org_profile_gen_qr;
    DatabaseReference dbRef;
    UserHelperClass user;
    ArrayList<VisitOrgHelperClass> visitListList;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        logoView = findViewById(R.id.img_org_profile);
        nameView = findViewById(R.id.tv_org_name);
        descriptionView = findViewById(R.id.tv_org_description);
        locationView = findViewById(R.id.tv_org_location);
//        btn_visit_org = findViewById(R.id.btn_visit_org);
        btn_edit_org = findViewById(R.id.btn_edit_org);
//        btn_org_profile_gen_qr = findViewById(R.id.btn_org_profile_gen_qr);
        img_gen_qr_code = findViewById(R.id.qr_img_org_profile);


        final String org_name = getIntent().getStringExtra("org_name");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Organisations");

        dbRef = FirebaseDatabase.getInstance().getReference("Visits");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganisationHelperClass org;
                for (DataSnapshot snap:snapshot.getChildren()) {
                    org = snap.getValue(OrganisationHelperClass.class);
                    if (org.getName().equals(org_name)){
                        current_org = org;
                        nameView.setText(current_org.getName());
                        descriptionView.setText(current_org.getDescription());
                        locationView.setText(current_org.getLocation());


                        if(org.getLogoImageUri() == null) {
                            logoView.setImageResource(R.drawable.ic_baseline_image_24);
                        } else {
                            Picasso.with(OrganisationActivity.this)
                                    .load(org.getLogoImageUri())
                                    .into(logoView);
                        }
                        if(org.getQrCodeUrl() != null){
                            Picasso.with(OrganisationActivity.this)
                                    .load(org.getQrCodeUrl())
                                    .into(img_gen_qr_code);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.org_user_recycle_view);
        visitListList = new ArrayList<>();
        final UserListAdapter myAdapter = new UserListAdapter(
                getApplicationContext(), visitListList);

        DatabaseReference refVisitDb = FirebaseDatabase.getInstance().getReference().child("Visits");
        refVisitDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                visitListList.clear();
                for (DataSnapshot snap:snapshot.getChildren()) {
                    VisitOrgHelperClass org = snap.getValue(VisitOrgHelperClass.class);

                    visitListList.add(org);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database locked, please contact your app developer for assistance", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



//        btn_visit_org.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                final FirebaseUser loggedInUser = mAuth.getCurrentUser();
//
//                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//                userRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snap:snapshot.getChildren()) {
//                            UserHelperClass currentUser = snap.getValue(UserHelperClass.class);
//                            if (currentUser.getEmail().equals(loggedInUser.getEmail().toString())){
//                                user = currentUser;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
//                VisitOrgHelperClass visit = new VisitOrgHelperClass(current_org, user, new Date(), new Date(), true);
//                dbRef.child(String.valueOf(System.currentTimeMillis())).setValue(visit)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(OrganisationActivity.this, "Visit " +current_org.getName()+ " is successfully register." , Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//
//            }
//        });

        btn_edit_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganisationActivity.this, EditOrganisationProfileActivity.class));
            }
        });

//        btn_org_profile_gen_qr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//
//                try{
//                    BitMatrix bitMatrix = multiFormatWriter.encode(current_org.getName(), BarcodeFormat.QR_CODE, 1000, 1000);
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                    img_gen_qr_code.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}