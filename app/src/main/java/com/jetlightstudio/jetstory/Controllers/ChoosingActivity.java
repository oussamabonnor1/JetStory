package com.jetlightstudio.jetstory.Controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jetlightstudio.jetstory.Adapters.CategoryChoosingAdapter;
import com.jetlightstudio.jetstory.Adapters.TimeGridViewAdapter;
import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.HelpFullFunctions;

import java.util.ArrayList;

public class ChoosingActivity extends AppCompatActivity {

    ArrayList<Story> stories = new ArrayList<>();
    RecyclerView categoriesView;
    GridView timeGridView;
    int timeIndex = -1;
    LinearLayout layoutToSelect;
    TextView textToSelect;
    CategoryChoosingAdapter customCategoriesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        categoriesView = findViewById(R.id.categoriesView);
        customCategoriesAdapter = new CategoryChoosingAdapter();
        categoriesView.setAdapter(customCategoriesAdapter);
        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        categoriesView.setLayoutManager(layoutManager);
        categoriesView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(5, 10, 5, 10);
            }
        });

        timeGridView = findViewById(R.id.timeGridView);
        timeGridView.setAdapter(new TimeGridViewAdapter());
        timeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (timeIndex > -1) {
                    HelpFullFunctions.setViewColor(layoutToSelect, "#FFFFFF");
                    textToSelect.setTextColor(Color.parseColor("#757575"));
                }
                layoutToSelect = view.findViewById(R.id.timeLayoutBackground);
                HelpFullFunctions.setViewColor(layoutToSelect, "#9AD945");
                layoutToSelect.setElevation(15);
                textToSelect = view.findViewById(R.id.timeTextView);
                textToSelect.setTextColor(Color.parseColor("#FFFFFF"));
                timeIndex = i;
            }
        });

        stories = (ArrayList<Story>) getIntent().getExtras().getSerializable("stories");
    }

    public void searchStory(View view) {
        ArrayList<Story> filteredStories = new ArrayList<>();
        ArrayList<String> filteredCategories = customCategoriesAdapter.getCategories();
        int time;
        switch (timeIndex) {
            case 9:
                time = 60;
                break;
            default:
                time = timeIndex + 1;
                break;
        }

        for (int i = 0; i < stories.size(); i++) {
            if (stories.get(i).getTime() <= time) {
                if (HelpFullFunctions.isStoryCategorySelected(stories.get(i).getCategory()
                        , filteredCategories)) {
                    filteredStories.add(stories.get(i));
                }
            }
        }

        Intent i = new Intent(this, StoryListActivity.class);
        i.putExtra("stories", filteredStories);
        i.putExtra("categories", customCategoriesAdapter.getUnfilteredCategories());

        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}

