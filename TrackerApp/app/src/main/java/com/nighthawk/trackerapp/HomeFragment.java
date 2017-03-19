package com.nighthawk.trackerapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nighthawk.trackerapp.Helper.HTTPRequest;
import com.nighthawk.trackerapp.Model.SingletonStorage;
import com.nighthawk.trackerapp.Model.TokenData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TokenData tokenData = TokenData.getInstance();
    private SingletonStorage storage = SingletonStorage.getInstance();
    DatabaseReference mRoofRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference accountType = mRoofRef.child("AccountType").child(storage.getAccountID()).child("Family");
    DatabaseReference deviceGroup = mRoofRef.child("DeviceGroup");
    private int flag = 0;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle  savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
//        FirebaseMessaging.getInstance().subscribeToTopic("SOS");
//        String token = FirebaseInstanceId.getInstance().getToken();
        ImageButton helpButton = (ImageButton) getView().findViewById(R.id.imageButton4);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNotification();
            }
        });
    }

    public void sendNotification(){
        String message = "HELP!!!";
        StringBuilder tokenBuilder = new StringBuilder();
        for(String item : tokenData.getTokens()){
            tokenBuilder.append(item);
            tokenBuilder.append(",");
        }

        tokenBuilder.deleteCharAt(tokenBuilder.length() - 1);
        String token = tokenBuilder.toString();
        String[] data = new String[]{message, token};
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.execute(data);
    }

    public void postNotification(){
        final List<String> familylist = new ArrayList<>();
        flag = 0;
        accountType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    if(!key.equals("Type")){
                        familylist.add(key);
                    }
                }
                tokenData.clearTokens();
                getAllTokenForNotification(familylist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllTokenForNotification(List<String> familylist){
        for(String item : familylist){
            DatabaseReference tmp = deviceGroup.child("Families").child(item).child("Group");
            tmp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        if(!key.equals("size")){
                            String value = ds.getValue().toString();
                            tokenData.updateTokens(value);
                        }
                    }
                    sendNotification();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
