package com.jetlightstudio.jetstory.ToolBox;

import android.os.AsyncTask;

import com.jetlightstudio.jetstory.Models.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StoryApiManager extends AsyncTask<Void, Void, ArrayList<Story>> {

    ArrayList<Story> stories = new ArrayList<>();
    String data;
    String path = "http://8e5d975c.ngrok.io";

    @Override
    protected ArrayList<Story> doInBackground(Void... voids) {
        try {
            URL url = new URL(path + "/api/stories");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            while (s != null) {
                s = bufferedReader.readLine();
                data += s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return creatingStoriesList(data);
    }

    ArrayList<Story> creatingStoriesList(String data) {
        if (data != null) {
            try {
                data = data.replace("null", "");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < 4; i++) {
                    JSONObject json = jsonArray.getJSONObject(i);

                    stories.add(new Story(json.getString("name"), json.getString("writer"),
                            json.getString("publishedDate"), json.getString("content"),
                            json.getInt("id"), json.getInt("time"), json.getString("category")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stories;
    }
}

