package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.fantastic.bookxchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private AppCompatEditText etEmail;
    private AppCompatEditText etPassword;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchStatusBarColor();
        setContentView(R.layout.activity_login);
        init();

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etEmail.getText().toString().isEmpty()){
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError("Please, enter your email");
                } else {
                    tilEmail.setErrorEnabled(false);
                }
            }
        });


        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etEmail.getText().toString().isEmpty()){
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError("Please, enter your email");
                } else {
                    tilEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidEmail(s);

            }
        });


        Fabric.with(this, new Crashlytics());
        auth = FirebaseAuth.getInstance();



    }

    public final static boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(NearMeActivity.class);
        }
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tilEmail = findViewById(R.id.container_email);
        tilPassword = findViewById(R.id.container_password);

    }

    public void login(View view) {
        Log.i(TAG, "login: ");
        if (!validate(etEmail, etPassword)) {
            return;
        }
        String email = getText(etEmail);
        String password = getText(etPassword);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        startActivity(NearMeActivity.class);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        toast(R.string.auth_failed);
                    }
                });
    }


    public void openRegistration(View view) {
        startActivity(RegistrationActivity.class);
    }
}

