package com.fantastic.bookxchange.utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dgohil on 10/29/17.
 */
@FunctionalInterface
public interface DefaultValueEventListener extends ValueEventListener {
    String TAG = DefaultValueEventListener.class.getSimpleName();

    @Override
    void onDataChange(DataSnapshot dataSnapshot);

    @Override
    default void onCancelled(DatabaseError databaseError) {
        Log.i(TAG, "onCancelled: ");
    }
}
