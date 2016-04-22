package com.smartamigos.rns.StartComponents.notesSection;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartamigos.rns.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CHARAN on 2/25/2016.
 */

public class notes_2 extends Fragment implements View.OnClickListener {
    private WifiManager wifiManager;
    String extension = ".pdf";
    String usn = "1RN13ISxxx"; //USN can be replaced here
    String subject ;
    Button downloader;
    //call progress dialog class
    ProgressDialog mProgressDialog;
    //myHTTPUrl is the address of the file to be downloaded
    String myHTTPUrl;
    String branch,sem;

    public notes_2(String branch, String sem, String sub, String url) {
        subject=sub;
        myHTTPUrl=url;
        this.branch=branch;
        this.sem=sem;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_main, container, false);
        downloader = (Button) view.findViewById(R.id.downloader);
        downloader.setOnClickListener(this);

        TextView branch_indicator=(TextView)view.findViewById(R.id.branch_indicator);
        TextView sem_indicator=(TextView)view.findViewById(R.id.sem_indicator);
        TextView sub_indicator=(TextView)view.findViewById(R.id.sub_indicator);

        branch_indicator.setText(branch);
        sem_indicator.setText(sem);
        sub_indicator.setText(subject);

        //initialise the progress dialog class
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Downloading Notes");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.downloader) {
            if(isNetworkConnected())
            new AlertDialog.Builder(getActivity())
                    .setTitle("Download Confirmation")
                    .setMessage("Are you sure you want to download this notes?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(DialogInterface dialog, int which) {

                            new DownloadTask(getActivity(),subject,"Downloading Notes").execute(myHTTPUrl);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            else {
                Snackbar snackbar = Snackbar.make(getView(),"No Internet Connection!",Snackbar.LENGTH_LONG).setAction("TURN ON", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
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
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}


