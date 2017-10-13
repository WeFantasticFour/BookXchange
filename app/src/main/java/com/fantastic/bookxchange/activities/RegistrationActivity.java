package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fantastic.bookxchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends BaseActivity {


    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchStatusBarColor();
        setContentView(R.layout.activity_registration);
        init();
        auth = FirebaseAuth.getInstance();
    }

    private void init() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void gotoLogin(View view) {
        startActivity(LoginActivity.class);
    }

    public void register(View view) {
        if (!validate(etFirstName, etLastName, etEmail, etPassword)) {
            return;
        }
        String email = getText(etEmail);
        String password = getText(etPassword);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:success");
                FirebaseUser user = auth.getCurrentUser();
                startActivity(MainActivity.class);
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                toast(R.string.auth_failed);
            }
        });
    }
}
