package com.fantastic.bookxchange.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fantastic.bookxchange.Manifest;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class RegistrationActivity extends BaseActivity {


    private static final String TAG = RegistrationActivity.class.getSimpleName();
    public LatLng latLng;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth auth;
    private EditText etZip;

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
        etZip = findViewById(R.id.etZip);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @SuppressLint("MissingPermission")
    void getMyLocation() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegistrationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RegistrationActivityPermissionsDispatcher.getMyLocationWithCheck(this);
    }

    public void gotoLogin(View view) {
        startActivity(LoginActivity.class);
    }

    public void register(View view) {
        if (!validate(etFirstName, etLastName, etEmail, etPassword, etZip)) {
            return;
        }
        String email = getText(etEmail);
        String password = getText(etPassword);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:success");
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(getText(etFirstName) + " " + getText(etLastName))
                            .build();
                    user.updateProfile(profileUpdates);
                    saveData();
                }
                startActivity(NearMeActivity.class);
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                toast(getString(R.string.auth_failed, task.getException().getMessage()));
            }
        });
    }

    private void saveData() {
        User user = new User();
        user.setName(getText(etFirstName) + " " + getText(etLastName));
        user.setZip(getText(etZip));
        user.setId(auth.getCurrentUser().getUid());
        //user.setLocation(latLng);
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(auth.getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(task -> {
                    doneProgress();
                    if (task.getException() != null) {
                        snakebar(etZip, task.getException().getMessage());
                    } else {
                        snakebar(etZip, "Your Content has been saved!");
                    }
                });

    }
}
