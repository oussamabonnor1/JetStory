package com.jetlightstudio.jetstory.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.StoryApiManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SplashScreen extends AppCompatActivity {
    ArrayList<Story> stories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            StoryApiManager storyData = new StoryApiManager();
            storyData.execute();
            stories = storyData.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent choosingActivityIntent = new Intent(getApplicationContext(), ChoosingActivity.class);
                choosingActivityIntent.putExtra("stories", stories);
                startActivity(choosingActivityIntent);
                finish();
            }
        }, 1500);
    }

}


