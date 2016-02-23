package com.smartamigos.rns.StartComponents.notesSection;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
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


public class notes_main extends Fragment implements View.OnClickListener {
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
                        public void onClick(DialogInterface dialog, int which) {

                            //call asynctask pass mProgressDialog.setOnCancelListener to activate the progress dialog
                            final DownloadTask downloadTask = new DownloadTask(getActivity());
                            downloadTask.execute(myHTTPUrl);

                            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String extension = ".pdf";
        String usn = "1RN13ISxxx"; //USN can be replaced here
        String subject = "CN-2";
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                //Download destination will be /sdcard/RNS
                File downloadDirectory = new File(Environment.getExternalStorageDirectory(), "RNS");

                //if RNS directory does not exist create it
                if (!downloadDirectory.exists()) {
                    downloadDirectory.mkdirs();
                }

                //subject can be replaced here

                File file = new File(downloadDirectory, usn + subject + extension);

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(file);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {
                new AlertDialog.Builder(getActivity())
                        .setTitle("File Downloaded!")
                        .setMessage("Do you want to open it ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                openPdf();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        }

        private void openPdf() {

            File file = new File(Environment.getExternalStorageDirectory(), "RNS" + usn + subject + extension);
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(path);
            intent.setType("application/pdf");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No application found",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
