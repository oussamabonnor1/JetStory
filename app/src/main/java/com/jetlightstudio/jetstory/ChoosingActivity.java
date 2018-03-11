package com.jetlightstudio.jetstory;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import java.util.ArrayList;
import java.util.Random;

public class ChoosingActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    Spinner spinnerLength;
    ArrayList<Story> stories;
    JSONObject json;
    ProgressBar progressBar;
    LinearLayout choicePanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(arr);

        spinnerLength = (Spinner) findViewById(R.id.spinnerLength);
        ArrayAdapter<CharSequence> arr2 = ArrayAdapter.createFromResource(this, R.array.length, android.R.layout.simple_spinner_item);
        arr2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLength.setAdapter(arr2);
        stories = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        choicePanel = (LinearLayout) findViewById(R.id.choicePanel);
        choicePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final RetreiveStoryData storyData = new RetreiveStoryData();
        storyData.execute();
    }

    public void searchStory(View view) {
        ArrayList<Story> storiesTemp = (ArrayList<Story>) stories.clone();
        int timeIndex = spinnerLength.getSelectedItemPosition();
        int categoryIndex = spinnerCategory.getSelectedItemPosition();
        Story.Category category = Story.Category.action;
        int time = 0;

        switch (timeIndex) {
            case 0:
                time = 3;
                break;
            case 1:
                time = 5;
                break;
            case 2:
                time = 10;
                break;
            case 3:
                time = 15;
                break;
            case 4:
                time = Integer.MAX_VALUE;
                break;
        }
        switch (categoryIndex) {
            case 0:
                category = Story.Category.action;
                break;
            case 1:
                category = Story.Category.comedy;
                break;
            case 2:
                category = Story.Category.romance;
                break;
            case 3:
                category = Story.Category.moral;
                break;
            case 4:
                category = Story.Category.sad;
                break;
        }
        int index = 0;
        while (index < storiesTemp.size()) {
            if (storiesTemp.get(index).getTime() > time || storiesTemp.get(index).getCategory() != category) {
                storiesTemp.remove(index);
            } else {
                index++;
            }
        }
        Intent i = new Intent(this, StoryListActivity.class);
        i.putExtra("stories", storiesTemp);
        startActivity(i);
    }

    public class RetreiveStoryData extends AsyncTask<Void, Void, Void> {
        String data;
        int index = 0;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.myjson.com/bins/j5f6b");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = "";
                while (s != null) {
                    s = bufferedReader.readLine();
                    data += s;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                data = data.replace("null","");
                JSONArray jsonArray = new JSONArray(data);
                jsonArray.get(0);
                json = (JSONObject) jsonArray.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 25; i++) {

                int c = new Random().nextInt(5);
                Story.Category category = Story.Category.action;
                switch (c) {
                    case 0:
                        category = Story.Category.action;
                        break;
                    case 1:
                        category = Story.Category.comedy;
                        break;
                    case 2:
                        category = Story.Category.romance;
                        break;
                    case 3:
                        category = Story.Category.moral;
                        break;
                    case 4:
                        category = Story.Category.sad;
                        break;
                }
                stories.add(new Story("story " + (stories.size()), "author " + stories.size(), "DD/MM/YYYY", stories.size(), new Random().nextInt(10) + 1, category));
                try {
                    stories.get(stories.size() - 1).setContent(json.getString("contact"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            choicePanel.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            /*try {
                stories.get(stories.size() - 1).setContent(json.get("value").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            index++;
            if(index < 25) this.execute();*/
        }
    }
}

