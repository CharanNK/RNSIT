package com.smartamigos.rns.StartComponents.notesSection;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.smartamigos.rns.R;
import com.smartamigos.rns.StartComponents.AttendanceSection.attendance_main;
import com.smartamigos.rns.StartComponents.calendarOfEvents.calendarOfEvents;

/**
 * Created by CHARAN on 2/27/2016.
 */
public class Branch_navigator extends Fragment implements View.OnClickListener {
    Button branch_civil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.branch_navigator,container,false);
        branch_civil=(Button)view.findViewById(R.id.branch_civil);
        branch_civil.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(getActivity());

        View promptsView = li.inflate(R.layout.sem_navigator, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Enter the Sem !");

        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Spinner mSpinner= (Spinner) promptsView
                .findViewById(R.id.sem_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.semarray,R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        final Button sem_submit_button = (Button) promptsView.findViewById(R.id.sem_submit_button);

        if(R.id.branch_civil == v.getId()){
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
            mSpinner.setOnItemSelectedListener(new OnSemSpinnerItemClicked(alertDialog));
        }

        if(v.getId() == R.id.sem_submit_button) {
            Fragment fragment;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            fragment = new notes_2();
            ft.replace(R.id.content_main,fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public class OnSemSpinnerItemClicked implements AdapterView.OnItemSelectedListener{
        AlertDialog dialog;
        public OnSemSpinnerItemClicked(AlertDialog alertDialog) {
            dialog=alertDialog;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 2:
                    LayoutInflater li = LayoutInflater.from(getActivity());

                    View promptsView = li.inflate(R.layout.subject_navigator, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    alertDialogBuilder.setView(promptsView);

                    alertDialogBuilder.setTitle("Enter the Subject !");

                    final AlertDialog alertDialog = alertDialogBuilder.create();

                    final Spinner mSpinner= (Spinner) promptsView
                            .findViewById(R.id.sem_spinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.civil3,R.layout.spinner_item);

                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);


                    mSpinner.setOnItemSelectedListener(new OnSubjectSpinnerItemClicked(alertDialog,dialog));

                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
//                    sem_submit_button.setOnClickListener((View.OnClickListener) this);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSubjectSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        AlertDialog alertDialog,dialog;
        public OnSubjectSpinnerItemClicked(AlertDialog alertDialog, AlertDialog dialog) {
            this.alertDialog=alertDialog;
            this.dialog=dialog;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Button sem_submit_button = (Button)view.findViewById(R.id.sem_submit_button);
            switch (position){
                    case 1:
                        Fragment fragment;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        fragment = new notes_2();
                        ft.replace(R.id.content_main, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
                        alertDialog.dismiss();
                        dialog.dismiss();

                }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
