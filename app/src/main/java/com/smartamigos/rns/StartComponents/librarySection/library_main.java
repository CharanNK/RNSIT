package com.smartamigos.rns.StartComponents.librarySection;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartamigos.rns.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class library_main extends Fragment implements View.OnClickListener {

    Button test;
    EditText inText;
    TextView outText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_main, container, false);
        test = (Button) view.findViewById(R.id.button);
        inText = (EditText) view.findViewById(R.id.editText);
        outText = (TextView) view.findViewById(R.id.textView4);
        test.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        create_json fetch_json = new create_json(this);
        String USN = inText.getText().toString();
        if (isNetworkConnected())
            fetch_json.execute("https://spreadsheets.google.com/tq?tq=select%20*%20where%20A=\"" + USN + "\"&key=15Flz1Y6Ef6AA4p8-9QeNJ_ICAjMlvs83uE2ydtmFOJk");
        else {
            Snackbar.make(v, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null)
                    .setAction("TURN ON", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                            startActivity(intent);
                        }
                    }).setActionTextColor(Color.YELLOW).show();
        }
    }

    public void onResult(JSONObject table) {
        String print;
        outText.setText(null);
        try {
            JSONArray rows = table.getJSONArray("rows");
            JSONArray c = rows.getJSONObject(0).getJSONArray("c");
            JSONObject v;
            int i;
            for (i = 1; i < c.length() - 1; i++) {
                v = c.getJSONObject(i);
                print = v.getString("v");
                outText.append(print);
                outText.append(" ");
            }
            JSONObject f = c.getJSONObject(i);
            String jDate = f.getString("f");
            outText.append(jDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

