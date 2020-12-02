package com.example.covidtrucking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    //Variables declaration
    private TextView tvSignup, tvForgotPassword;
    private Button btnLogin;
    private EditText emailInput, passwordInput;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Dialog content
        dialog = new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // check the use if he/she is logged in the app
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            finish();
            startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
        }


        //Assign values to there variables
        tvSignup = findViewById(R.id.tv_signup);
        btnLogin = findViewById(R.id.btn_login);
        emailInput = findViewById(R.id.email_login_input);
        emailInput = findViewById(R.id.email_login_input);
        passwordInput = findViewById(R.id.password_login_input);


        // EventListeners
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = passwordInput.getText().toString();
                String email = emailInput.getText().toString();

                if(email.isEmpty()){
                    emailInput.setError("Please add your email address!");
                } else if (password.isEmpty()) {
                    passwordInput.setError("Please add your password!");
                }  else {
                        dialog.show();
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        dialog.dismiss();
                                        if(task.isSuccessful()){
                                            checkEmailVerification();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Authentication failed, please try again.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                }
            }
        });




    }

    //Helper methods

    private void checkEmailVerification() {
        FirebaseAuth user = FirebaseAuth.getInstance();
        Boolean emailFlag = user.getCurrentUser().isEmailVerified();

        if(emailFlag){
            finish();
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
        } else {
            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show();
            user.signOut();
        }
    }
}