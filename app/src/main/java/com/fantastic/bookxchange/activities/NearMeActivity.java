package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.fantastic.bookxchange.Manifest;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.NearListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DataTest;
import com.fantastic.bookxchange.utils.MapUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class NearMeActivity extends BaseActivity implements BaseBookListFragment.BookListClickListener, BaseBookListFragment.BookListReadyListener {

    //Map BaseFragment related
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private long UPDATE_INTERVAL = 10000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private final static String KEY_LOCATION = "location";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    
    //Data
    
    List<User> users;
    HashMap<Book, List<Marker>> books;
    private String TAG = "NearMeActivity";

    private List<Marker> previousMarkers = new ArrayList<>();
    private NearListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
            // is not null.
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        setupMap();

    }

    private void populateData() {
        users = DataTest.fakeData();

        books = new HashMap<>();

        for (User u : users) {
            if (!u.getShareBooks().isEmpty()){
                Marker marker = MapUtils.addMarker(map, u);
                for (Book b : u.getShareBooks()) {
                    saveMarker(marker, b);
                }

                for (Book b : u.getExchangeBooks()) {
                    saveMarker(marker, b);
                }
            }
        }

        fragment = NearListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flBooks, fragment)
                .commit();
    }

    private void saveMarker(Marker marker, Book b) {
        if (books.containsKey(b)) {
            books.get(b).add(marker);
        } else {
            List<Marker> markersList = new ArrayList<>();
            markersList.add(marker);
            books.put(b, markersList);
        }
    }


    private void setupMap() {
        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(map1 -> loadMap(map1));
        } else {
            toast(R.string.map_null_error);
        }

    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            NearMeActivityPermissionsDispatcher.getMyLocationWithCheck(this);
            NearMeActivityPermissionsDispatcher.startLocationUpdatesWithCheck(this);
            map.setOnMarkerClickListener(marker -> {
                User u = (User)marker.getTag();

                Intent i = new Intent(this, UserActivity.class);
                i.putExtra("user", Parcels.wrap(u));
                startActivity(i);

                return true;
            });
            populateData();
        } else {
            toast(R.string.map_null_error);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NearMeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        //noinspection MissingPermission
        map.setMyLocationEnabled(true);

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        //noinspection MissingPermission
        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, e.getMessage(),e);
                });
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Display the connection status

        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.moveCamera(cameraUpdate);
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        } else {
            toast(R.string.current_location_null);
        }
        NearMeActivityPermissionsDispatcher.startLocationUpdatesWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        //noinspection MissingPermission
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            return;
        }

        // Report to the UI that the location was updated

        mCurrentLocation = location;

        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        map.moveCamera(cameraUpdate);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClickListener(Book book) {

        //Reset the color of previous books
        if(!previousMarkers.isEmpty()){
            for(Marker m : previousMarkers){
                m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
        }
        List<Marker> bookMarkers = books.get(book);
        previousMarkers = bookMarkers;
        for(Marker m : bookMarkers){
            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

    }

    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {
        fragment.pushData(new ArrayList(books.keySet()));
    }
}

