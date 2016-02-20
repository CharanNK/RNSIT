package com.smartamigos.rns.StartComponents.librarySection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartamigos.rns.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class library_main extends Fragment implements View.OnClickListener{

    Button test;
    TextView text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_main,container,false);
        test = (Button) view.findViewById(R.id.button);
        text = (TextView) view.findViewById(R.id.textView4);
        test.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        create_json cre = new create_json(this);
        cre.execute("https://spreadsheets.google.com/tq?key=15Flz1Y6Ef6AA4p8-9QeNJ_ICAjMlvs83uE2ydtmFOJk");
    }

    public void onResult (JSONObject table) {
        String print;
        try {
            JSONArray rows = table.getJSONArray("rows");
            JSONArray c = rows.getJSONObject(0).getJSONArray("c");
            JSONObject v = c.getJSONObject(0);
            print = v.getString("v");
            text.setText(print);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

