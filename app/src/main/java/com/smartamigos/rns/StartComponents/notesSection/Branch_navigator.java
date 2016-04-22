package com.smartamigos.rns.StartComponents.notesSection;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartamigos.rns.R;

/**
 * Created by CHARAN on 2/27/2016.
 */
public class Branch_navigator extends Fragment implements View.OnClickListener {
    private RelativeLayout rootView;
    Button branch_civil;
    final private String[] civil4={"M-4","CT","SA-1","SURVEY-2","HHM","BPD","http://sjbit.edu.in/app/course-material/CIVIL/IV/SURVEYING-II%20[10CV44]/CIVIL-IV-SURVEYING-II%20[10CV44]-NOTES.pdf",
    "http://sjbit.edu.in/app/course-material/CIVIL/IV/ENGINEERING%20MATHEMATICS%20-%20IV%20[10MAT41]/CIVIL-IV-ENGINEERING%20MATHEMATICS%20-%20IV%20[10MAT41]-NOTES.pdf"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.branch_navigator,container,false);
        branch_civil=(Button)view.findViewById(R.id.branch_civil);
        branch_civil.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        if(!isNetworkConnected()){
            Snackbar snackbar = Snackbar.make(getView(),"No Internet Connection!",Snackbar.LENGTH_LONG).setAction("TURN ON", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        super.onResume();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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
            mSpinner.setOnItemSelectedListener(new OnSemSpinnerItemClicked(alertDialog,"civil"));
        }

        if(v.getId() == R.id.sem_submit_button) {
        }
    }

    public class OnSemSpinnerItemClicked implements AdapterView.OnItemSelectedListener{
        AlertDialog dialog;
        String branch;
        public OnSemSpinnerItemClicked(AlertDialog alertDialog, String branch) {
            dialog=alertDialog;
            this.branch=branch;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.subject_navigator, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setTitle("Enter the Subject !");
            final AlertDialog alertDialog = alertDialogBuilder.create();

            final Spinner mSpinner= (Spinner) promptsView
                    .findViewById(R.id.sem_spinner);
            ArrayAdapter<CharSequence> adapter;

            switch (position){
                case 3:
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.civil3,R.layout.spinner_item);

                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    mSpinner.setOnItemSelectedListener(new OnSubjectSpinnerItemClicked(alertDialog,dialog,branch,3));

                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                case 4:

                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.civil4,R.layout.spinner_item);

                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    mSpinner.setOnItemSelectedListener(new OnSubjectSpinnerItemClicked(alertDialog,dialog, branch,4));

                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSubjectSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        AlertDialog alertDialog,dialog;
        String branch;
        String sem;
        public OnSubjectSpinnerItemClicked(AlertDialog alertDialog, AlertDialog dialog, String branch,int sem) {
            this.alertDialog=alertDialog;
            this.dialog=dialog;
            this.branch=branch;
            this.sem=String.valueOf(sem);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                    case 1:
                        Fragment fragment;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        fragment = new notes_2(branch,sem,civil4[0],civil4[6]);
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
