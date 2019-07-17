package com.jetlightstudio.jetstory;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.net.URL;
import java.util.ArrayList;

public class ChoosingActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    Spinner spinnerLength;
    ArrayList<Story> stories = new ArrayList<>();
    ArrayList<Story> comedyStories;
    ArrayList<Story> actionStories;
    ArrayList<Story> romanceStories;
    ArrayList<Story> sadStories;
    ArrayList<Story> moralStories;
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        choicePanel = (LinearLayout) findViewById(R.id.choicePanel);
        choicePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final RetrieveStoryData storyData = new RetrieveStoryData();
        storyData.execute();
    }

    public void searchStory(View view) {
        actionStories = new ArrayList<>();
        comedyStories = new ArrayList<>();
        sadStories = new ArrayList<>();
        moralStories = new ArrayList<>();
        romanceStories = new ArrayList<>();
        int timeIndex = spinnerLength.getSelectedItemPosition();
        int categoryIndex = spinnerCategory.getSelectedItemPosition();

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

        for (int i = 0; i < stories.size(); i++) {
            if (stories.get(i).getTime() <= time) {
                if (stories.get(i).getCategory() == Story.Category.action)
                    actionStories.add(stories.get(i));
                else if (stories.get(i).getCategory() == Story.Category.comedy)
                    comedyStories.add(stories.get(i));
                else if (stories.get(i).getCategory() == Story.Category.sad)
                    sadStories.add(stories.get(i));
                else if (stories.get(i).getCategory() == Story.Category.moral)
                    moralStories.add(stories.get(i));
                else if (stories.get(i).getCategory() == Story.Category.romance)
                    romanceStories.add(stories.get(i));
            }
        }

        Intent i = new Intent(this, StoryListActivity.class);
        i.putExtra("tabIndex", categoryIndex);

        switch (categoryIndex) {
            case 0:
                i.putExtra("stories", actionStories);
                break;
            case 1:
                i.putExtra("stories", comedyStories);
                break;
            case 2:
                i.putExtra("stories", romanceStories);
                break;
            case 3:
                i.putExtra("stories", moralStories);
                break;
            case 4:
                sadStories = new StoryDataBase(getApplicationContext(), null).loadStories();
                i.putExtra("stories", sadStories);
                break;
        }
        i.putExtra("actionStories", actionStories);
        i.putExtra("comedyStories", comedyStories);
        i.putExtra("romanceStories", romanceStories);
        i.putExtra("moralStories", moralStories);
        i.putExtra("sadStories", sadStories);
        startActivity(i);
    }

    public void openFacebook(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/JetLightstudio"));
        startActivity(browserIntent);
    }

    public void openPlayStore(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.JetLightstudio.JetStory"));
        startActivity(intent);
    }

    public void sharingApp(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Try downloading this amazing app!");
        i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.JetLightstudio.JetStory");
        startActivity(Intent.createChooser(i, "Share via"));
    }

    public class RetrieveStoryData extends AsyncTask<Void, Void, Void> {
        String data;
        String path = "http://27ed29f5.ngrok.io";

        @Override
        protected Void doInBackground(Void... voids) {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
                choicePanel.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}

