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
            stories.add(new Story("story " + (i + 1), "author " + (i + 1), "DD/MM/YYYY", i + 1, new Random().nextInt(10) + 1, category));
        }
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
}
