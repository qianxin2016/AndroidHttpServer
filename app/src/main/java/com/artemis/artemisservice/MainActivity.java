package com.artemis.artemisservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Xin Qian on 2019/1/17.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private View mTestDevicesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ArtemisService.class);
        startService(intent);

        mTestDevicesBtn = findViewById(R.id.test_devices);
        mTestDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        testDevicesAPIGet();
                        //testDevicesAPIPost();
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void testDevicesAPIGet() {
        try {
            String requestUrl = "http://localhost:6789/devices?id=1";
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                // Receive response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer result = new StringBuffer("");
                while ((line = reader.readLine()) != null) {
                    line = URLDecoder.decode(line, "utf-8");
                    result.append(line);
                }
                reader.close();
                Log.e(TAG, "Request result--->" + result);
            } else {
                Log.e(TAG, "Request failed");
            }

            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void testDevicesAPIPost() {
        try {
            String requestUrl = "http://localhost:6789/devices";
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            // Send JSON request
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("command", "query_device");
            String json = URLEncoder.encode(obj.toString(), "utf-8");
            out.writeBytes(json);
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
                // Receive response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer result = new StringBuffer("");
                while ((line = reader.readLine()) != null) {
                    line = URLDecoder.decode(line, "utf-8");
                    result.append(line);
                }
                reader.close();
                Log.e(TAG, "Request result--->" + result);
            } else {
                Log.e(TAG, "Request failed");
            }

            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
