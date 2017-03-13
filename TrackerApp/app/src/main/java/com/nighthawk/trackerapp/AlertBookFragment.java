package com.nighthawk.trackerapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nighthawk.trackerapp.Model.SingletonStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlertBookFragment extends Fragment {
    public AlertBookFragment() {
        // Required empty public constructor
    }
    private Spinner spinner;
    private SingletonStorage storage = SingletonStorage.getInstance();
    final ArrayList<String> listFamily = new ArrayList<>();
    final List<String> members = new ArrayList<>();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference deviceGroup = mRootRef.child("DeviceGroup");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_alertbook, container, false);
        Button createGroup = (Button) view.findViewById(R.id.CreateFamily);
        createGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                createGroup(view);
            }
        });
        Button registerFamily = (Button) view.findViewById(R.id.registerToFamily);

        registerFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFamily(v);
            }
        });
        this.spinner = (Spinner) view.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                String name = spinner.getSelectedItem().toString();
                ListView membersListView = (ListView) getView().findViewById(R.id.memberList);
                createListOfMembers(membersListView, name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setPrompt("Select a Family Group");
            }
        });
        return view;
    }

    public void createListOfMembers(final ListView memberListView, final String familyName){
        DatabaseReference group = deviceGroup.child("Families").child(familyName);
        group.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateListViewData(dataSnapshot,memberListView,members);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateListViewData(dataSnapshot,memberListView,members);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateListViewData(dataSnapshot,memberListView,members);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    };

    public void updateListViewData(DataSnapshot dataSnapshot,ListView listView, List<String> list){
        list.clear();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            String name = ds.getKey();
            if(!name.equals("size")){
                list.add(name);
            }
        }
        Log.d("LISTVIEWCHECK",Integer.toString(list.size()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

    public void updateSpinnerData(DataSnapshot dataSnapshot, List<String> list){
        list.clear();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            String familyName = ds.getKey();
            list.add(familyName);
        }
        Log.d("CHECK", (Integer.toString(list.size())));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        deviceGroup.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateSpinnerData(dataSnapshot,listFamily);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateSpinnerData(dataSnapshot,listFamily);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateSpinnerData(dataSnapshot,listFamily);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createGroup(View view){
        FragmentManager manager = getFragmentManager();
        CreateGroupDialog myDialog = new CreateGroupDialog();
        myDialog.show(manager,"MyDialog");
    }

    private String getSpinnerSelectedItem(){
        String family = null;
        try {
            family = spinner.getSelectedItem().toString();
        }catch (Exception e){
            Context context = getContext();
            String error = "Please Select a Family Group!";
            Toast toast = Toast.makeText(context,error,Toast.LENGTH_SHORT);
            toast.show();
            getSpinnerSelectedItem();
        }
        return family;
    }

    public void addToFamily(View view){
        final String family = getSpinnerSelectedItem();
        final DatabaseReference familyGroup = deviceGroup.child("Families").child(family).child("Group");
        familyGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount() - 1; //Account for the "Size" as an item in the group
                familyGroup.child("size").setValue(size);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        familyGroup.child(storage.getAccountID()).setValue(storage.getTokenID());
        Context context = getContext();
        String success = "Registered Successfully!";
        Toast toast = Toast.makeText(context,success,Toast.LENGTH_SHORT);
        toast.show();

        // Update the AccountType Table
        updateFamilyToAccountType(storage.getAccountID(), family);
    }

    public void updateFamilyToAccountType(String accountID, String registeredFamily){
        DatabaseReference accountType = mRootRef.child("AccountType").child(accountID).child("Family");
        accountType.child(registeredFamily).setValue(true);
    }
}
