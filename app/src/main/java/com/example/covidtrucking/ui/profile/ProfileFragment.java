package com.example.covidtrucking.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.covidtrucking.OrganisationActivity;
import com.example.covidtrucking.ProfileActivity;
import com.example.covidtrucking.R;
import com.example.covidtrucking.UserHelperClass;
import com.example.covidtrucking.ui.organisations.OrganisationHelperClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    Button edit_profile;
    UserHelperClass currentUser;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        edit_profile = root.findViewById(R.id.btn_edit_user_profile);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

//        NavigationView navigationView= (NavigationView) root.findViewById(R.id.action_settings);
//        navigationView.setNavigationItemSelectedListener();

        final MutableLiveData<String> currentEmail = new MutableLiveData<>();
        final MutableLiveData<String> currentUsername = new MutableLiveData<>();
        final MutableLiveData<String> currentPhone = new MutableLiveData<>();
        final MutableLiveData<String> currentNID = new MutableLiveData<>();
        final MutableLiveData<String> currentFName = new MutableLiveData<>();
        final MutableLiveData<String> currentOName = new MutableLiveData<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final  TextView profile_username = root.findViewById(R.id.tv_profile_username);
        final TextView tvFName = root.findViewById(R.id.tv_profile_fname);
        final TextView tv_profile_other_names = root.findViewById(R.id.tv_profile_other_names);
        final TextView profileEmail = root.findViewById(R.id.tv_profile_email);
        final TextView profilePhone = root.findViewById(R.id.tv_profile_phone);
        final  TextView tv_email = root.findViewById(R.id.tv_profile_email_text);
        final TextView tv_national_id = root.findViewById(R.id.tv_profile_nid);
        final ImageView profile_image_view = root.findViewById(R.id.profile_image_view);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()) {
                    currentUser = snap.getValue(UserHelperClass.class);
                    if (currentUser.getEmail().equals(mUser.getEmail())){
                        currentEmail.setValue(currentUser.getEmail());
                        currentUsername.setValue(currentUser.getFirst_name());
                        currentPhone.setValue(currentUser.getPhone());
                        currentFName.setValue(currentUser.getFirst_name());
                        currentOName.setValue(currentUser.getOther_names());
                        currentNID.setValue(currentUser.getPhone());

                        tvFName.setText(currentUser.getFirst_name());
                        tv_profile_other_names.setText(currentUser.getOther_names());
                        tv_national_id.setText(currentUser.getNational_id());
                        Picasso.with(getContext())
                                .load(currentUser.getImage_url())
                                .into(profile_image_view);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileViewModel.setCurrentEmail(currentEmail);
        profileViewModel.setCurrentUsername(currentUsername);
        profileViewModel.setCurrentNationalId(currentNID);
        profileViewModel.setCurrentPhoneNumber(currentPhone);


        profileViewModel.getCurrentUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profile_username.setText(s);

            }
        });


        profileViewModel.getCurrentEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profileEmail.setText(s);
                tv_email.setText(s);
            }
        });

        profileViewModel.getCurrentNationalId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profilePhone.setText(s);
            }
        });


        return root;
    }
}