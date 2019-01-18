package com.artemis.artemisservice;

import android.util.Log;

import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xin Qian on 2019/1/17.
 */

public class ArtemisHttpServer implements HttpServerRequestCallback {
    private static final String TAG = "ArtemisHttpServer";

    private static ArtemisHttpServer mInstance;

    public static int PORT_DEFALT = 6789;

    AsyncHttpServer mServer = new AsyncHttpServer();

    public static ArtemisHttpServer getInstance() {
        if (mInstance == null) {
            synchronized (ArtemisHttpServer.class) {
                if (mInstance == null) {
                    mInstance = new ArtemisHttpServer();
                }
            }
        }
        return mInstance;
    }

    public void start() {
        Log.d(TAG, "Starting http server...");
        mServer.get("[\\d\\D]*", this);
        mServer.post("[\\d\\D]*", this);
        mServer.listen(PORT_DEFALT);
    }

    public void stop() {
        Log.d(TAG, "Stopping http server...");
        mServer.stop();
    }

    private void sendResponse(AsyncHttpServerResponse response, JSONObject json) {
        // Enable CORS
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.send(json);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String uri = request.getPath();
        Log.d(TAG, "onRequest " + uri);

        Multimap params = null;
        if (request.getMethod().equals("GET")) {
            params = request.getQuery();
        } else if (request.getMethod().equals("POST")) {
            params = ((AsyncHttpRequestBody<Multimap>)request.getBody()).get();
        } else {
            Log.d(TAG,"Unsupported Method");
            return;
        }

        if (params != null) {
            Log.d(TAG, "params = " + params.toString());
        }

        switch (uri) {
            case "/devices":
                handleDevicesRequest(params, response);
                break;
            default:
                handleInvalidRequest(params, response);
                break;
        }
    }

    private void handleDevicesRequest(Multimap params, AsyncHttpServerResponse response) {
        try {
            JSONObject item1 = new JSONObject();
            item1.put("name", "D1");
            item1.put("id", "123");
            JSONObject item2 = new JSONObject();
            item2.put("name", "D2");
            item2.put("id", "456");

            JSONArray array = new JSONArray();
            array.put(item1);
            array.put(item2);

            JSONObject json = new JSONObject();
            json.put("devices", array.toString());

            sendResponse(response, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleInvalidRequest(Multimap params, AsyncHttpServerResponse response) {
        JSONObject json = new JSONObject();
        try {
            json.put("error", "Invalid API");
            sendResponse(response, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}