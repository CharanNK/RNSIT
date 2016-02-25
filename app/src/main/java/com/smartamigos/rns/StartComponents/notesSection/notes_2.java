package com.smartamigos.rns.StartComponents.notesSection;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.smartamigos.rns.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CHARAN on 2/25/2016.
 */

public class notes_2 extends Fragment implements View.OnClickListener {
    String extension = ".pdf";
    String usn = "1RN13ISxxx"; //USN can be replaced here
    String subject = "CN-2";
    Button downloader;
    //call progress dialog class
    ProgressDialog mProgressDialog;
    //myHTTPUrl is the address of the file to be downloaded
    String myHTTPUrl = "http://sjbit.edu.in/app/course-material/ISE/VI/COMPUTER%20NETWORKS-II%20[10CS64]/ISE-VI-COMPUTER%20NETWORKS-II%20[10CS64]-NOTES.pdf";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_main, container, false);
        downloader = (Button) view.findViewById(R.id.downloader);
        downloader.setOnClickListener(this);

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
        }
    }

}


