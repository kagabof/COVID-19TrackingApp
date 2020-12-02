package com.example.covidtrucking.ui.organisations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.covidtrucking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganisationsFragment extends Fragment {

    private OrganisationsActivity.OrganisationsViewModel mViewModel;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ProgressDialog dialog;
    ArrayList<OrganisationHelperClass> orgList;

    public static OrganisationsFragment newInstance() {
        return new OrganisationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.organisations_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyleView);
        fab = root.findViewById(R.id.floating_action_button_organisation);

//        dialog.setTitle("Loading");
//        dialog.setMessage("Please wait...");
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
        orgList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recyleView);
        fab = root.findViewById(R.id.floating_action_button_organisation);

        final OrganisationsItemAdapter myAdapter = new OrganisationsItemAdapter(
                getContext(), orgList);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Organisations");

//        dialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orgList.clear();
                for (DataSnapshot snap:snapshot.getChildren()) {
                    OrganisationHelperClass org = snap.getValue(OrganisationHelperClass.class);
                    orgList.add(org);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database locked, please contact your app developer for assistance", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateOrganisationActivity.class));
            }
        });
        return root;
    }

}