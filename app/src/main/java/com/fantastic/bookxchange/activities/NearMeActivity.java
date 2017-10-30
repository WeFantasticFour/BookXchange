package com.fantastic.bookxchange.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.Manifest;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.NearListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.rest.BookClient;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vistrav.flow.Flow;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.fantastic.bookxchange.R.id.ivProfile;
import static com.fantastic.bookxchange.utils.MapUtils.addMarker;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class NearMeActivity extends BaseActivity implements BaseBookListFragment.BookListClickListener,
        BaseBookListFragment.BookListReadyListener,
        NavigationView.OnNavigationItemSelectedListener {

    private final static String KEY_LOCATION = "location";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    Location mCurrentLocation;
    //Map BaseFragment related
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private List<User> users;
    private User currentUser;
    private HashMap<Book, List<Marker>> books;
    private String TAG = "NearMeActivity";

    private List<Marker> previousMarkers = new ArrayList<>();
    private NearListFragment fragment;

    private NavigationView navigationView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);
        navigationView = findViewById(R.id.nav_view);


        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        setupMap();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                prepareHeader(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void prepareHeader(View navigationView) {
        FirebaseUser current = auth.getCurrentUser();
        if (current != null) {
            TextView tvEmail = navigationView.findViewById(R.id.tv_email);
            tvEmail.setText(current.getEmail());
            TextView tvName = navigationView.findViewById(R.id.tv_name);
            tvName.setText(current.getDisplayName());
            ImageView imoji = navigationView.findViewById(ivProfile);

            FirebaseDatabase.getInstance().getReference("users").child(current.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    if (user == null || user.getUrlProfileImage() == null) {
                        return;
                    }
                    Glide.with(NearMeActivity.this)
                            .load(user.getUrlProfileImage())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.ic_person_24dp)
                            .into(new BitmapImageViewTarget(imoji) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imoji.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void populateData() {
        //TODO Query to firebase, get the closest users to your position

        books = new HashMap<>();

        getUsers();
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
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::loadMap);
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
                User u = (User) marker.getTag();
                Intent i = new Intent(this, UserActivity.class);
                i.putExtra("user", Parcels.wrap(u));
                startActivity(i);
                return true;
            });
            populateData();
            //getUsers();
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
    @SuppressLint("MissingPermission")
    void getMyLocation() {
        //noinspection MissingPermission
        map.setMyLocationEnabled(true);
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        //noinspection MissingPermission
        locationClient.getLastLocation()
                .addOnSuccessListener(this::onLocationChanged)
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.moveCamera(cameraUpdate);
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        } else {
//            toast(R.string.current_location_null);
        }
        NearMeActivityPermissionsDispatcher.startLocationUpdatesWithCheck(this);
    }

    @SuppressLint("MissingPermission")
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
        getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                }, Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        mCurrentLocation = location;
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        map.moveCamera(cameraUpdate);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClickListener(Book book) {

        for (Marker m : previousMarkers) {
            User userTag = (User) m.getTag();

            MapUtils.modifyMarker(this, m, userTag, Color.parseColor("#009688"));
        }

        List<Marker> bookMarkers = books.get(book);
        previousMarkers = bookMarkers;

        for (Marker m : bookMarkers) {
            User userTag = (User) m.getTag();
            MapUtils.modifyMarker(this, m, userTag, Color.parseColor("#FF5522"));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userTag.getLocation(), 10);
            map.moveCamera(cameraUpdate);

        }

    }

    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {
        if (books != null) {
            fragment.pushData(new ArrayList<>(books.keySet()));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                if (currentUser != null) {
                    Intent iUser = new Intent(NearMeActivity.this, UserActivity.class);
                    iUser.putExtra("user", Parcels.wrap(currentUser));
                    startActivity(iUser);
                } else {
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                     currentUser = dataSnapshot.getValue(User.class);
                                     Intent iUser = new Intent(NearMeActivity.this, UserActivity.class);
                                     iUser.putExtra("user", Parcels.wrap(currentUser));
                                     startActivity(iUser);
                                 }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 }
                    );
                }
                break;
            case R.id.nav_messages:
                Intent iMessage = new Intent(this, MessagesActivity.class);
                startActivity(iMessage);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(LoginActivity.class);
                break;
            case R.id.nav_add_book:
                startActivity(AddBookActivity.class);
                break;
            default:
                toast(item.getTitle().toString());
        }
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setCheckable(false);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //item.setChecked(true);
        //setTitle(item.getTitle());

        return true;
    }

    private void getUsers() {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> users = new ArrayList<>();
                        Flow.of(dataSnapshot
                                .getChildren())
                                .forEach(data -> {
                                    User user = data.getValue(User.class);
                                    users.add(user);
                                    loadUserLocation(user);
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void getUserBooks(User user, Marker marker) {
        FirebaseDatabase.getInstance()
                .getReference("user_book")
                .child(user.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Flow.of(dataSnapshot
                                .getChildren())
                                .forEach(data -> {
                                    getBook(user,
                                            data.child("isbn").getValue(String.class),
                                            Book.CATEGORY.valueOf(data.child("category").getValue(String.class)), marker);
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void getBook(User user, String isbn, Book.CATEGORY category, Marker marker) {
        FirebaseDatabase.getInstance()
                .getReference("books")
                .child(isbn)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        Log.i(TAG, "onDataChange: isbn " + isbn + " : " + book);
                        if (book != null) {
                            book.setCategory(category);
                            user.addBook(book);
                            saveMarker(marker, book);
                            fragment.pushData(new ArrayList<>(books.keySet()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getBook(User user, String isbn, Book.CATEGORY category) {
        FirebaseDatabase.getInstance()
                .getReference("books")
                .child(isbn)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        Log.i(TAG, "onDataChange: isbn " + isbn + " : " + book);
                        if (book != null) {
                            book.setCategory(category);
                            user.addBook(book);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void loadUserLocation(User user) {
        BookClient client = new BookClient();
        client.getLocation(user.getZip(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject object = (JSONObject) response.getJSONArray("results").get(0);
                    JSONObject locObject = object.getJSONObject("geometry").getJSONObject("location");
                    user.setLocation(new LatLng(locObject.getDouble("lat"), locObject.getDouble("lng")));
                    if (!user.getId().equals(auth.getUid())) {
                        addMarker(NearMeActivity.this, map, user, Color.parseColor("#009688"), marker -> getUserBooks(user, marker));
                    } else {
                        FirebaseDatabase.getInstance()
                                .getReference("user_book")
                                .child(user.getId())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Flow.of(dataSnapshot
                                                .getChildren())
                                                .forEach(data -> {
                                                    getBook(user,
                                                            data.child("isbn").getValue(String.class),
                                                            Book.CATEGORY.valueOf(data.child("category").getValue(String.class)));
                                                });
                                        currentUser = user;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });


    }
}

