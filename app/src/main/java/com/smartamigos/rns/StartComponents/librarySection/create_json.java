package com.smartamigos.rns.StartComponents.librarySection;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class create_json extends AsyncTask<String, Void, String> {
    library_main callback;

    public create_json(library_main callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to download the requested page.";
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();

            return convertStreamToString(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        // remove the unnecessary parts from the response and construct a JSON
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);
        try {
            JSONObject table = new JSONObject(jsonResponse);
            callback.onResult(table);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}