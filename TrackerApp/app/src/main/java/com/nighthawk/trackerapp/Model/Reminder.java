package com.nighthawk.trackerapp.Model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nighthawk.trackerapp.Model.SingletonStorage;
import com.nighthawk.trackerapp.Model.TokenData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxkirazx on 13/3/2017.
 */

public class Reminder {
    private static final Reminder ourInstance = new Reminder();
    private List<String> list = new ArrayList<>();
    private String accID;
    public static Reminder getInstance() {
        return ourInstance;
    }

    private Reminder() {
    }

    public void setReminder(String string){
        accID = SingletonStorage.getInstance().getAccountID();
        DatabaseReference mRoofRef = FirebaseDatabase.getInstance().getReference();
        mRoofRef.child("Reminder").child(accID).child(string).setValue(true);
    }
}
