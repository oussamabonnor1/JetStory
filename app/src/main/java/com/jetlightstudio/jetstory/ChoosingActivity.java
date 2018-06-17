package com.jetlightstudio.jetstory;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    ArrayList<Story> comedyStories;
    ArrayList<Story> actionStories;
    ArrayList<Story> romanceStories;
    ArrayList<Story> sadStories;
    ArrayList<Story> moralStories;
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        choicePanel = (LinearLayout) findViewById(R.id.choicePanel);
        choicePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final RetreiveStoryData storyData = new RetreiveStoryData();
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

        Intent i = new Intent(this, MainStoryActivity.class);
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

    public class RetreiveStoryData extends AsyncTask<Void, Void, Void> {
        String data;

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
            stories = new ArrayList<>();
            if (data != null) {
                try {
                    data = data.replace("null", "");
                    JSONArray jsonArray = new JSONArray(data);
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
                        stories.get(stories.size() - 1).setAlbumId(changeAlbumIcon(stories.get(stories.size() - 1).getCategory()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
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
                    stories.get(stories.size() - 1).setAlbumId(changeAlbumIcon(stories.get(stories.size() - 1).getCategory()));
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

    protected int changeAlbumIcon(Story.Category category) {
        if (category == Story.Category.action) {
            return R.drawable.book_red_action;

        } else if (category == Story.Category.comedy) {
            return R.drawable.book_red_comedy;

        } else if (category == Story.Category.romance) {
            return R.drawable.book_red_romance;

        } else if (category == Story.Category.sad) {
            return R.drawable.book_red_sad;

        } else if (category == Story.Category.moral) {
            return R.drawable.book_red_moral;
        } else return R.drawable.book_red;
    }
}

