package com.nighthawk.trackerapp;

/**
 * Created by zxkirazx on 22/2/2017.
 */
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CreateGroupDialog extends DialogFragment {
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference familyGroup = mRootRef.child("DeviceGroup").child("Families");
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_creategroup, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        final EditText groupName = (EditText) getView().findViewById(R.id.etGroupName);
        Button create = (Button) getView().findViewById(R.id.btnCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = groupName.getText().toString();
                familyGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(name).exists()){
                            Context context = getContext();
                            Toast toast = Toast.makeText(context,"Group name is existed!",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            familyGroup.child(name).child("Group").child("size").setValue(0);
                            dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
