package com.champhay.Model.handler.backgroundtask;

import android.os.AsyncTask;

import com.champhay.Model.handler.eventlistener.DownloadEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HoangTP
 */

public class LoadJsonInBackground extends AsyncTask<String, Integer, String> {
    private DownloadEvent downloadEvent;

    public void setOnFinishEvent(DownloadEvent finishEvent) {
        this.downloadEvent = finishEvent;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = params[0];
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            StringBuilder result = new StringBuilder();
            String temp;
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((temp = reader.readLine()) != null) {
                result.append(temp);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String String) {
        downloadEvent.onLoadFinish(String);
        super.onPostExecute(String);
    }
}

