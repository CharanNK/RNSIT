package com.smartamigos.rns.StartComponents.newsfeedSection;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartamigos.rns.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class newsfeed_main extends Fragment {
    TextView news;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_feed,container,false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        news = (TextView)view.findViewById(R.id.news);

        new NewsTask().execute("https://googledrive.com/host/0B4MrAIPM8gwfWEJiVGVmYkxodkE/news.json");
        return view;
    }


    public class NewsTask extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        BufferedReader reader;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine())!= null){
                    builder.append(line);
                }
                String str =  builder.toString();
                //Json Parsing start
                JSONObject parent = new JSONObject(str);
                JSONArray newsArray = parent.getJSONArray("news");
                StringBuilder parse = new StringBuilder();
                for(int i=0;i<newsArray.length();i++){
                    JSONObject child = newsArray.getJSONObject(i);
                    parse.append(child.getString("title")).append(" ").append(child.getString("color")).append(" ").append(child.getString("desc")).append("\n");
                }
                return parse.toString();


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            news.setText(s);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(s);
        }
    }
}
