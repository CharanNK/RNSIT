package com.smartamigos.rns.StartComponents.calendarOfEvents;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartamigos.rns.R;

/**
 * Created by SRINIVAS on 2/11/2016.
 */
public class calendarOfEvents extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_of_events,container,false);
        return view;
    }
}
