package com.nighthawk.trackerapp.Model;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 13/3/17.
 */

public class TokenData {
    private static TokenData instance = new TokenData();
    private ArrayList<String> tokens = new ArrayList<>();
    private SingletonStorage storage = SingletonStorage.getInstance();
    DatabaseReference mRoofRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference accountType = mRoofRef.child(storage.getAccountID());
    DatabaseReference deviceGroup = mRoofRef.child("Families");

    private TokenData(){}

    public static TokenData getInstance(){
        return instance;
    }

    public void updateTokens(String token){
        tokens.add(token);
    }

    public ArrayList<String> getTokens(){
        return tokens;
    }

    public void getAllTokenForNotification(){
        final List<String> familylist = new ArrayList<>();

        accountType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    if(!key.equals("Type")){
                        familylist.add(key);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        for(String item : familylist){
            DatabaseReference tmp = deviceGroup.child(item);
            tmp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        if(!key.equals("size")){
                            String value = ds.getValue().toString();
                            tokens.add(value);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
