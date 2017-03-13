package com.nighthawk.trackerapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Type;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference accountType = mRootRef.child("AccountType");

    @Override
    public void onCreate(Bundle saveInstanceBundle){
        super.onCreate(saveInstanceBundle);
        setContentView(R.layout.fragment_register);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Inflate the layout for this fragment
        spinner.setOnItemSelectedListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        // TODO Auto-generated method stub
        this.spinner.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        this.spinner.setSelection(0);
    }

    public Boolean register(String id, String password){
        if(id.equals("")||password.equals("")){
            CharSequence error = "IC or Password cannot be empty!";
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, error, Toast.LENGTH_SHORT);
            toast.show();
            return Boolean.FALSE;
        }
        else if(id.length() < 6 || password.length() < 6){
            CharSequence error = "IC or Password must be more than 6 Characters!";
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, error, Toast.LENGTH_SHORT);
            toast.show();
            return Boolean.FALSE;
        }
        else{
            String idInput = id.toLowerCase() + "@mydomain.com";
            mFirebaseAuth.createUserWithEmailAndPassword(idInput,password);
            return Boolean.TRUE;
        }
    }

    protected void writeItemToDatabase(DatabaseReference parent, HashMap<String,String> hashMap){
        for(String key: hashMap.keySet()){
            String id = key;
            String value = hashMap.get(key);
            parent.child(id).child("Type").setValue(value);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        final Button register = (Button)findViewById(R.id.btRegister);
//        Intent loginIntent = new Intent(this,LoginFragment.class);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*do something*/
                TextView icView = (TextView)findViewById(R.id.etRIC);
                TextView passView = (TextView)findViewById(R.id.etRPassword);
                String ic = icView.getText().toString().toLowerCase();
                String pass = passView.getText().toString();
                if(register(ic,pass)){
                    // Prepare to write to database (ic : account type)
                    String selection = spinner.getSelectedItem().toString();
                    HashMap<String,String> database = new HashMap<String, String>();
                    database.put(ic,selection);
                    writeItemToDatabase(accountType,database);

                    CharSequence success = "Register Successful";
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, success, Toast.LENGTH_SHORT);
                    toast.show();

                    finish();
                }
            }
        });
    }

}
