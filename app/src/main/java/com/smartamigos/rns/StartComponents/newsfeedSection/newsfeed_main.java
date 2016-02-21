package com.smartamigos.rns.StartComponents.newsfeedSection;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


public class newsfeed_main extends Fragment {
    TextView news;
    ProgressBar progressBar;
    static int serverVersion ,localVersion ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_feed, container, false);


        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        news = (TextView)view.findViewById(R.id.news);

        new NewsVersion().execute("https://googledrive.com/host/0B4MrAIPM8gwfWEJiVGVmYkxodkE/n_version.json");

        SharedPreferences preferences = getActivity().getSharedPreferences("news_version", Context.MODE_PRIVATE);
        localVersion = preferences.getInt("version", 0);

        return view;
    }


    public class NewsVersion extends AsyncTask<String,String,String>{
        HttpURLConnection connection;
        BufferedReader reader;
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine())!= null){
                    builder.append(line);
                }
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parsing the Json file
            try {
                JSONObject parent = new JSONObject(s);
                JSONObject news_version = parent.getJSONObject("news_version");

                serverVersion = news_version.getInt("version");

                SharedPreferences preferences = getActivity().getSharedPreferences("news_version", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("version", serverVersion);
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!Objects.equals(localVersion, serverVersion)) {
                new newsFetch().execute("https://googledrive.com/host/0B4MrAIPM8gwfWEJiVGVmYkxodkE/news.json");
            }else {
                loadJsonFile();
            }


        }
    }



    public class newsFetch extends AsyncTask<String,String,String>{

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
                saveJsonFile(str);
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

    private void saveJsonFile(String data) {
        FileOutputStream stream = null;
        try {
            File path = new File("/data/data/com.smartamigos.rns/news.json");
            stream = new FileOutputStream(path);
            stream.write(data.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(stream != null)
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadJsonFile() {
        String ret = null;
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(new File("/data/data/com.smartamigos.rns/news.json"));

             reader= new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            ret = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        JSONObject parent;
        try {
            parent = new JSONObject(ret);
            JSONArray newsArray = parent.getJSONArray("news");
            StringBuilder parse = new StringBuilder();
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject child = newsArray.getJSONObject(i);
                parse.append(child.getString("title")).append(" ").append(child.getString("color")).append(" ").append(child.getString("desc")).append("\n");
            }
            news.setText(parse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
