package com.fantastic.bookxchange.utils;

import android.util.Log;

import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.Review;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.rest.BookClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vistrav.flow.Flow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dgohil on 10/23/17.
 */

public class FirebaseUtils {

    private static final String TAG = FirebaseUtils.class.getSimpleName();
    private static final FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();


    public static void loadOneUser(String userId, Consumer<User> producer) {
        fDatabase.getReference("users")
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
        fDatabase.getReference("books")
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
        fDatabase.getReference("user_book")
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
        fDatabase.getReference("reviews")
                .child(reviewFor.getId())
                .child(review.getAuthor().getId())
                .setValue(review)
                .addOnCompleteListener(consumer::accept);
    }

    private void loadUsers(Consumer<List<User>> producer) {
        fDatabase.getReference("users")
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


    public static void saveMessage(String room, User user1, FirebaseUser me, String lastMessage) {


        Map<String, Object> values = new HashMap<>();
        values.put(user1.getId(), true);
        values.put(user1.getId() + "~" + user1.getName(), true);
        values.put(me.getUid(), true);
        values.put(me.getUid() + "~" + me.getDisplayName(), true);
        values.put("from", me.getUid());
        values.put("time", new Date().getTime());
        values.put("lastMessage", lastMessage);
        if (room == null) {
            room = FirebaseDatabase.getInstance()
                    .getReference("rooms")
                    .push().getKey();
        }
        values.put("roomId", room);

        fDatabase.getReference("rooms").child(room).updateChildren(values);

        Map<String, Object> message = new HashMap<>();
        message.put("from", me.getUid());
        message.put("fromName", me.getDisplayName());
        message.put("createdAt", new Date().getTime());
        message.put("message", lastMessage);

        fDatabase.getReference("messages")
                .child(room).push().setValue(message)
                .addOnCompleteListener(task -> {
                    Log.i(TAG, "Exception: " + task.getException());
                    Log.i(TAG, "getResult: " + task.getResult());
                    Log.i(TAG, "task: " + task);
                });
    }
}
