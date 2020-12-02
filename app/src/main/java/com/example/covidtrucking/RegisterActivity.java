package com.example.covidtrucking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText fNameInput, otherNamesInput, phoneInput, emailInput, passwordInput, rePasswordInput;
    private TextView loginText;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootDb;
    private FirebaseUser user;
    private DatabaseReference ref;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // Dialog content
        dialog = new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        loginText = findViewById(R.id.tv_signin);
        fNameInput = findViewById(R.id.fname_signup_input);
        otherNamesInput = findViewById(R.id.other_names_signup_input);
        phoneInput = findViewById(R.id.phone_signup_input);
        emailInput = findViewById(R.id.email_signup_input);
        passwordInput = findViewById(R.id.password_signup_input);
        btnSignUp = findViewById(R.id.btn_sign_up);
        rePasswordInput = findViewById(R.id.re_password_signup_input);

        rootDb = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
        }

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fName = fNameInput.getText().toString().trim();
                final String otherName = otherNamesInput.getText().toString();
                final String phone = phoneInput.getText().toString().trim();
                final String email = emailInput.getText().toString().trim();
                final String password = passwordInput.getText().toString().trim();
                String rePassword = passwordInput.getText().toString().trim();
                final long time;
                time = System.currentTimeMillis();

                if(fName.isEmpty()){
                    fNameInput.setError("First name is required!");
                } else if(otherName.isEmpty()){
                    otherNamesInput.setError("Other names are required!");
                } else if(phone.isEmpty()){
                    phoneInput.setError("Phone number is required!");
                } else if(email.isEmpty()) {
                    emailInput.setError("Email is required!");
                } else if(password.isEmpty()){
                    passwordInput.setError("Password is require!");
                } else if(rePassword.isEmpty()){
                    rePasswordInput.setError("Please Retype your password correctly!");
                } else if(password.length() < 6) {
                    passwordInput.setError("The given password is invalid, Password should be at least 6 characters!");
                } else  {
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        ref = rootDb.getReference("users");
                                        ref = rootDb.getInstance().getReference("Users").child(mAuth.getUid());
                                        UserHelperClass newUser = new UserHelperClass("", fName, otherName, phone, email, "");
                                        ref.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> refState) {
                                                if(refState.isSuccessful()){
                                                    dialog.dismiss();
                                                    sendEmailVerification();
                                                    mAuth.signOut();
                                                }else {
                                                    dialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Authentication failed!..", Toast.LENGTH_SHORT).show();
                                                    mAuth.signOut();
                                                }
                                            }
                                        });
                                    } else {
                                        dialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Authentication failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendEmailVerification() {
        final FirebaseAuth user = FirebaseAuth.getInstance();

        if (user.getCurrentUser()!=null) {
            user.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Successfully registered, Please please verify your email.", Toast.LENGTH_SHORT).show();
                        user.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Verification email haven't been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}