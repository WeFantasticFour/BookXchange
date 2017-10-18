package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.fantastic.bookxchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchStatusBarColor();
        setContentView(R.layout.activity_login);
        init();
        Fabric.with(this, new Crashlytics());
        auth = FirebaseAuth.getInstance();
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

