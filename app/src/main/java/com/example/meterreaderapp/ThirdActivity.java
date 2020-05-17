package com.example.meterreaderapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        final TextView end = findViewById(R.id.thirdText);
        Button submit = findViewById(R.id.sendButton);
        final RadioGroup radioG = findViewById(R.id.radio_group);
        final EditText meter_val = findViewById(R.id.meter_value);
        String token = null;

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT) == true) {
            token = intent.getStringExtra(Intent.EXTRA_TITLE);
            String email = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        final String finalToken = token;
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Integer radioID = radioG.getCheckedRadioButtonId();
                RadioButton radioB = findViewById(radioID);
                end.setText(radioB.getText().toString() + " " + radioID);

                // -- JUST HERE FOR TEST PURPOSE
                String urlString = "";

                // -- API URL BUILDER
                /*
                String apiCall_1 = "TO BE CHANGED";
                String apiCall_2 = "TO BE CHANGED";
                String apiCall_3 = "TO BE CHANGED";
                String keyArray[] = new String[]{"Id", "meter"};
                String valueArray[] = new String[]{radioID, meter_val.getText().toString()};
                String urlString = NetworkUtils.buildUrl(apiCall_1, apiCall_2, apiCall_3, keyArray, valueArray);
                */

                new setMeterData().execute(urlString, finalToken);
            }
        });

        // -- API URL BUILDER
        /*
        String apiCall_1 = "meters";
        String apiCall_2 = "connection";
        String apiCall_3 = "search";
        String keyArray[] = new String[]{"email"};
        String valueArray[] = new String[]{email};
        String urlString = NetworkUtils.buildUrl(apiCall_1, apiCall_2, apiCall_3, keyArray, valueArray);
        */

        // -- Temporary url json file
        String urlString = "https://jsonblob.com/api/jsonBlob/68081770-9775-11ea-a2b9-6d9f8f865edb";

        new getMeterCustomerApi().execute(urlString, token);
    }

    public class getMeterCustomerApi extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String token = params[1];

            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url, token);
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(String data) {
            JSONObject JObj = null;
            JSONObject JObj2 = null;
            JSONArray JArray = null;

            try {
                JObj = new JSONObject(data);
                TextView end = findViewById(R.id.thirdText);

                try {
                    if (JObj.get("success").toString() == "true")
                    {
                        JArray = (JSONArray) JObj.get("message");
                        RadioGroup radioG = findViewById(R.id.radio_group);

                        for (int i = 0; i < JArray.length(); i++)
                        {
                            JObj2 = JArray.getJSONObject(i);

                            RadioButton radio = new RadioButton(getApplicationContext());
                            radio.setText(JObj2.get("meter_id").toString());
                            radio.setId(Integer.parseInt(JObj2.get("id").toString()));
                            radioG.addView(radio);
                        }
                    }
                    else {
                        end.setText("Failed to Login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class setMeterData extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String token = params[1];

            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url, token);
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


// https://jsonblob.com/68081770-9775-11ea-a2b9-6d9f8f865edb