package com.jetlightstudio.jetstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Random;

public class ChoosingActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    Spinner spinnerLength;
    ArrayList<Story> stories;


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
        for (int i = 0; i < 25; i++) {
            stories.add(new Story("story " + (i + 1), "author " + (i + 1), "DD/MM/YYYY", i + 1, new Random().nextInt(10) + 1));
        }
    }

    public void searchStory(View view) {
        int timeIndex = spinnerLength.getSelectedItemPosition();
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
        int index = 0;
        while (index < stories.size()) {
            if (stories.get(index).getTime() > time) {
                stories.remove(index);
            } else {
                index++;
            }
        }

        Intent i = new Intent(this, StoryListActivity.class);
        i.putExtra("stories", stories);
        startActivity(i);
    }
}
