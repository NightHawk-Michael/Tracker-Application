package com.nighthawk.trackerapp;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by zxkirazx on 21/2/2017.
 */

public class AddMemberDialog extends DialogFragment {
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_addreminder, container, false);
        return view;
    }
}
