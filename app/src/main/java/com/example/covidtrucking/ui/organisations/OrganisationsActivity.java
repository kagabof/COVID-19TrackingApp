package com.example.covidtrucking.ui.organisations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.covidtrucking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganisationsActivity<locations> extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<OrganisationHelperClass> orgList;
    String names[] = {"WHO", "UN", "AUCA", "MTN", "WHO", "UN", "AUCA", "MTN"};
    int images[] ={R.drawable.who,R.drawable.who,R.drawable.who,R.drawable.who, R.drawable.who,R.drawable.who, R.drawable.who,R.drawable.who};

    String orgLocations[] = {"Kigali", "kambala", "musanze", "nyamirambo", "Kigali", "kambala", "musanze", "nyamirambo"};
    FloatingActionButton fab;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisations);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        orgList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyleView);
        fab = findViewById(R.id.floating_action_button_organisation);

        final OrganisationsItemAdapter myAdapter = new OrganisationsItemAdapter(
                this, orgList);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Organisations");

        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orgList.clear();
                for (DataSnapshot snap:snapshot.getChildren()) {
                    OrganisationHelperClass org = snap.getValue(OrganisationHelperClass.class);
                    orgList.add(org);
                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrganisationsActivity.this, "Database locked, please contact your app developer for assistance", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateOrganisationActivity.class));
            }
        });

    }

    public static class OrganisationsViewModel extends ViewModel {
        // TODO: Implement the ViewModel
    }
}