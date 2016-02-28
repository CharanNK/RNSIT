package com.smartamigos.rns.StartComponents.notesSection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.smartamigos.rns.R;

/**
 * Created by CHARAN on 2/27/2016.
 */

public class Sem_navigator extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.sem_navigator,container,false);

        Spinner sem_spinner = (Spinner)view.findViewById(R.id.sem_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.semarray, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
// Apply the adapter to the spinner
        sem_spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
