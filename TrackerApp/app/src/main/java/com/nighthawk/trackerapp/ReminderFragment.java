package com.nighthawk.trackerapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nighthawk.trackerapp.Model.SingletonStorage;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_reminder, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ImageButton add = (ImageButton)getView().findViewById(R.id.ibAddReminder);
        final List<String> a = new ArrayList<String>();
        final ListView listView = (ListView)getView().findViewById(R.id.ReminderListView);
        DatabaseReference mRoofRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reminder = mRoofRef.child("Reminder").child(SingletonStorage.getInstance().getAccountID());
        reminder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                a.add(dataSnapshot.getKey());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,a);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                a.add(dataSnapshot.getKey());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,a);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addMember(view);
            }
        });
    }

    public void addMember(View view){
        FragmentManager manager = getFragmentManager();
        AddReminderDialog myDialog = new AddReminderDialog();
        myDialog.show(manager,"MyDialog");
    }
}
