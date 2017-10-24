package com.fantastic.bookxchange.utils;

import android.util.Log;

import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.Review;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.rest.BookClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vistrav.flow.Flow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dgohil on 10/23/17.
 */

public class FirebaseUtils {

    private static final String TAG = FirebaseUtils.class.getSimpleName();


    public static void loadOneUser(String userId, Consumer<User> producer) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        producer.accept(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private static void loadBooks(User user, String isbn, Book.CATEGORY category) {
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

    public static void loadUserBooks(User user) {
        FirebaseDatabase.getInstance()
                .getReference("user_book")
                .child(user.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Flow.of(dataSnapshot
                                .getChildren())
                                .forEach(data -> {
                                    loadBooks(user,
                                            data.child("isbn").getValue(String.class),
                                            Book.CATEGORY.valueOf(data.child("category").getValue(String.class)));
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private static void loadUserLocation(User user) {
        BookClient client = new BookClient();
        client.getLocation(user.getZip(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject object = (JSONObject) response.getJSONArray("results").get(0);
                    JSONObject locObject = object.getJSONObject("geometry").getJSONObject("location");
                    user.setLocation(new LatLng(locObject.getDouble("lat"), locObject.getDouble("lng")));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }

    public static void saveReview(User reviewFor, Review review, Consumer<Task> consumer) {
        FirebaseDatabase.getInstance()
                .getReference("reviews")
                .child(reviewFor.getId())
                .child(review.getAuthor().getId())
                .setValue(review)
                .addOnCompleteListener(consumer::accept);
    }

    private void loadUsers(Consumer<List<User>> producer) {
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
                                    loadUserBooks(user);
                                });
                        producer.accept(users);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
