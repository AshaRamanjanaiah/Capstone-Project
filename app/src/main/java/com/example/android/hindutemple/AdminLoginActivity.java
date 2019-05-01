package com.example.android.hindutemple;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.hindutemple.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String EMAIL_ADDRESS = "email";
    private static final String PASSWORD = "password";

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextView textViewDisplayError;
    private ProgressBar progressBarLogin;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private String emailInput;
    private String passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        mToolbar = findViewById(R.id.admin_login_toolbar);
        setSupportActionBar(mToolbar);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        textViewDisplayError = findViewById(R.id.textView_display_error);
        progressBarLogin = findViewById(R.id.progressbar_login);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        emailInput = textInputEmail.getEditText().getText().toString().trim();
        passwordInput = textInputPassword.getEditText().getText().toString().trim();
        outState.putString(EMAIL_ADDRESS, emailInput);
        outState.putString(PASSWORD, passwordInput);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
            textInputEmail.getEditText().setText(savedInstanceState.getString(EMAIL_ADDRESS));
            textInputPassword.getEditText().setText(savedInstanceState.getString(PASSWORD));
    }

    public void loginUser(View view) {
        emailInput = textInputEmail.getEditText().getText().toString().trim();
        passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if(!Constants.validateEmail(textInputEmail, emailInput) | !Constants.validatePassword(textInputPassword, passwordInput)){
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminLoginActivity.this, UpdateTimingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    if(!task.getException().getMessage().isEmpty()) {
                        textViewDisplayError.setVisibility(View.VISIBLE);
                        textViewDisplayError.setText(task.getException().getMessage());
                    }
                }
                progressBarLogin.setVisibility(View.GONE);
            }
        });
    }
}
