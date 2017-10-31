package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.fantastic.bookxchange.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.fabric.sdk.android.Fabric;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 10001;
    private AppCompatEditText etEmail;
    private AppCompatEditText etPassword;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private FirebaseAuth auth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (etEmail.getText().toString().isEmpty()) {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError("Please, enter your email");
            } else {
                tilEmail.setErrorEnabled(false);
            }
        });


        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etEmail.getText().toString().isEmpty()) {
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

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (me != null) {
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
        startProgress();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this::handleLoginTask);
    }

    public void openRegistration(View view) {
        startActivity(RegistrationActivity.class);
    }

    public void loginWithGoogle(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new Builder(this)
                .enableAutoManage(this, connectionResult -> snakebar(etEmail, "Google Play Services error"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                snakebar(etEmail, R.string.auth_failed);
                Log.e(TAG, result.getStatus().toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        startProgress();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, this::handleLoginTask);
    }

    private void handleLoginTask(Task task) {
        if (task.isSuccessful()) {
            me = auth.getCurrentUser();
            startActivity(NearMeActivity.class);
        } else {
            snakebar(etEmail, R.string.auth_failed);
            Log.e(TAG, task.getException().getMessage(), task.getException());
        }
        doneProgress();
    }
}

