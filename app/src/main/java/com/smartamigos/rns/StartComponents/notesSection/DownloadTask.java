package com.smartamigos.rns.StartComponents.notesSection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.smartamigos.rns.StartComponents.MainActivity;

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

public class DownloadTask extends AsyncTask<String, Integer, String> {
    String extension = ".pdf";
    String usn = "1RN13ISxxx"; //USN can be replaced here
    String subject;
    private ProgressDialog mPDialog;
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private File mTargetFile;
    //Constructor parameters :
    // @context (current Activity)
    // @targetFile (File object to write,it will be overwritten if exist)
    // @dialogMessage (message of the ProgresDialog)
    public DownloadTask(Context context,String subject,String dialogMessage) {
        this.mContext = context;
        this.subject=subject;
        mPDialog = new ProgressDialog(context);

        mPDialog.setMessage(dialogMessage);
        mPDialog.setIndeterminate(true);
        mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPDialog.setCancelable(true);
        // reference to instance to use inside listener
        final DownloadTask me = this;
        mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                me.cancel(true);
            }
        });
        Log.i("DownloadTask", "Constructor done");
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
            Log.i("DownloadTask","Response " + connection.getResponseCode());

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

            File file = new File(downloadDirectory, usn+subject+extension);

            if(file.exists()){
                openPdf();
            }
            else{
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
            }
        }
        catch (Exception e) {
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
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        mPDialog.show();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mPDialog.setIndeterminate(false);
        mPDialog.setMax(100);
        mPDialog.setProgress(progress[0]);

    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("DownloadTask", "Work Done! PostExecute");
        mWakeLock.release();
        mPDialog.dismiss();
        if (result != null)
            Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
        else {
            new AlertDialog.Builder(mContext)
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


        File file = new File("/sdcard/RNS/"+usn+subject+extension);
//            File file = new File(Environment.getExternalStorageDirectory(), "RNS" + usn + subject + extension);

        Uri path = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setPackage("com.adobe.reader");
        intent.setDataAndType(path, "application/pdf");
//        intent.setAction(notes_2.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext.getApplicationContext(), "No application found",
                    Toast.LENGTH_SHORT).show();
        }

    }
}