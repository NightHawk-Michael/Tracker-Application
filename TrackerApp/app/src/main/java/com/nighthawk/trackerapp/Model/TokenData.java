package com.nighthawk.trackerapp.Model;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 13/3/17.
 */

public class TokenData {
    private static TokenData instance = new TokenData();
    private Set<String> tokens = new HashSet<>();
    private SingletonStorage storage = SingletonStorage.getInstance();

    private TokenData(){}

    public static TokenData getInstance(){
        return instance;
    }

    public void updateTokens(String token){
        tokens.add(token);
    }

    public Set<String> getTokens(){
        return tokens;
    }


}
