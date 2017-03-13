package com.nighthawk.trackerapp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nighthawk.trackerapp.Helper.LoginInfo;
import com.nighthawk.trackerapp.Model.LoginModel;
import com.nighthawk.trackerapp.Model.SingletonStorage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private SingletonStorage storage = SingletonStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        final Intent registerIntent = new Intent(this,RegisterFragment.class);
        Button login = (Button)findViewById(R.id.btLogin);
        final CheckBox autologinCheckBox = (CheckBox)findViewById(R.id.autoLogin);
        final Button register = (Button)findViewById(R.id.etRegister);

        final TextView icView = (TextView)findViewById(R.id.etIC);
        final TextView passView = (TextView)findViewById(R.id.etPassword);
//        LoginModel loginModel = LoginInfo.readFromFile(this);
//        icView.setText(loginModel.getId());
//        passView.setText(loginModel.getPassword());


        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//              startActivity(mainIntent);
                String ic = icView.getText().toString() + "@mydomain.com";
                String pass = passView.getText().toString();
                if(autologinCheckBox.isChecked()){
                    //LoginInfo.writeToFile(ic,pass,getApplicationContext());
                }
                signIn(ic,pass);
            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(registerIntent);
            }
        });
    }



    public void signIn(final String id, String password){
        final Intent mainActivityIntent = new Intent(this, MainActivity.class);
        if(id.equals("")||password.equals("")){
            CharSequence success = "Email or Password cannot be empty!";
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, success, Toast.LENGTH_SHORT);
            toast.show();

        }
        else{

            mFirebaseAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Context context = getApplicationContext();
                    if(task.isSuccessful()) {
                        CharSequence success = "Suceess Verification";
                        Toast toast = Toast.makeText(context, success, Toast.LENGTH_SHORT);
                        toast.show();
                        FirebaseMessaging.getInstance().subscribeToTopic("SOS");
                        String token = FirebaseInstanceId.getInstance().getToken();
                        String idInput = id.substring(0,id.indexOf('@'));
                        storage.updateAccountID(idInput.toLowerCase());
                        storage.updateTokenID(token);
                        mainActivityIntent.putExtra("IC",idInput);
                        startActivity(mainActivityIntent);
                    }
                    else{
                        CharSequence error = "Unsuccessful Log In!";
                        Toast toast = Toast.makeText(context,error, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

        }
    }

}
