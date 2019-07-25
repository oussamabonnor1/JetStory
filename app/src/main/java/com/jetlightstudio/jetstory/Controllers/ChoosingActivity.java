package com.jetlightstudio.jetstory.Controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.FontAwesome;
import com.jetlightstudio.jetstory.ToolBox.StoryApiManager;
import com.jetlightstudio.jetstory.ToolBox.StoryDataBase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChoosingActivity extends AppCompatActivity {

    //Spinner spinnerCategory;
    //Spinner spinnerLength;
    ArrayList<Story> stories = new ArrayList<>();
    ArrayList<Story> comedyStories;
    ArrayList<Story> actionStories;
    ArrayList<Story> romanceStories;
    ArrayList<Story> sadStories;
    ArrayList<Story> moralStories;
    ProgressBar progressBar;
    RecyclerView categoriesView;
    //LinearLayout choicePanel;
    String[] colors = {"#FFD453", "#FFC153", "#FF8A54", "#FFA754", "#FFAB88", "#FF6C54", "#B05F6D", "#DE8275"};
    String[] darkColors = {"#23221D", "#1E1E1E", "#28241C", "#514438", "#302F2E", "#331510", "#331B1F", "#351F1C"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);
        /*spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(arr);

        spinnerLength = (Spinner) findViewById(R.id.spinnerLength);
        ArrayAdapter<CharSequence> arr2 = ArrayAdapter.createFromResource(this, R.array.length, android.R.layout.simple_spinner_item);
        arr2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLength.setAdapter(arr2);*/

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //choicePanel = (LinearLayout) findViewById(R.id.choicePanel);
        //choicePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        categoriesView = findViewById(R.id.categoriesView);
        categoriesView.setAdapter(new CustomCategoryAdapter());
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        categoriesView.setLayoutManager(layoutManager);

       /* StoryApiManager storyData = new StoryApiManager();
        try {
            storyData.execute();
            stories = storyData.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/
        categoriesView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void searchStory(View view) {
        actionStories = new ArrayList<>();
        comedyStories = new ArrayList<>();
        sadStories = new ArrayList<>();
        moralStories = new ArrayList<>();
        romanceStories = new ArrayList<>();
        int timeIndex = 0;
        int categoryIndex = 0;

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


    //region comments
    class CustomCategoryAdapter extends RecyclerView.Adapter<CustomCategoryAdapter.ViewHolder> {
        ArrayList<String> categories;

        CustomCategoryAdapter() {
            this.categories = new ArrayList<>();
            categories.add("Moral");
            categories.add("Action");
            categories.add("Romance");
            categories.add("Comedy");
            categories.add("Fantasy");
            categories.add("Sad");
            categories.add("Kids");
            categories.add("Thriller");
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            FontAwesome categoryLabel;
            LinearLayout layoutBackground;

            ViewHolder(View itemView) {
                super(itemView);
                categoryLabel = itemView.findViewById(R.id.categoryLabel);
                layoutBackground = itemView.findViewById(R.id.layoutBackground);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.custom_cetegory_adapter, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String text = categories.get(position) + getResources().getString(R.string.icon_plus);
            holder.categoryLabel.setText(text);
        }


        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

    }
    //endregion
}

