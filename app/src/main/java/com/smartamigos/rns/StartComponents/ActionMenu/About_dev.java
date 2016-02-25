package com.smartamigos.rns.StartComponents.ActionMenu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartamigos.rns.R;

/**
 * Created by CHARAN on 2/25/2016.
 */
public class About_dev extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_dev,container,false);

        return view;
    }
}
