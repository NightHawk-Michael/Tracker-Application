package com.nighthawk.trackerapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.nighthawk.trackerapp.Model.Reminder;

/**
 * Created by zxkirazx on 21/2/2017.
 */

public class AddReminderDialog extends DialogFragment {
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_addreminder, container, false);
        final Button addReminder = (Button)view.findViewById(R.id.btnAddReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });
        return view;
    }
    public void addReminder(){
        EditText med = (EditText)getView().findViewById(R.id.etMed);
        String medname = med.getText().toString();
        EditText quantity = (EditText)getView().findViewById(R.id.etQuantity);
        String q = quantity.getText().toString();
        String data = "";
        String frequency = "";
        String meal = "";
        CheckBox morning = (CheckBox)getView().findViewById(R.id.checkBoxMorning);
        CheckBox afternoon = (CheckBox)getView().findViewById(R.id.checkBoxAfternoon);
        CheckBox night = (CheckBox)getView().findViewById(R.id.checkBoxDinner);
        CheckBox before = (CheckBox)getView().findViewById(R.id.checkBoxBefore);
        CheckBox after = (CheckBox)getView().findViewById(R.id.checkBoxAfter);
        if(morning.isChecked()){
            frequency = "Morning ,";
        }
        if(afternoon.isChecked()){
            frequency += "Afternoon ,";
        }
        if(night.isChecked()){
            frequency += "Night ";
        }
        if(before.isChecked()){
            meal = "before";
        }
        if(after.isChecked()){
            meal += "after";
        }
        data = String.format("Take %s %s in the %s %s meal", q,medname,frequency,meal);
        Reminder.getInstance().setReminder(data);
        this.dismiss();
    }


}