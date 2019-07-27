package com.jetlightstudio.jetstory.Controllers;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jetlightstudio.jetstory.Adapters.CustomStoryListAdapter;
import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.FontAwesome;
import com.jetlightstudio.jetstory.ToolBox.HelpFullFunctions;
import com.jetlightstudio.jetstory.ToolBox.StoryDataBase;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity
        implements View.OnClickListener {
    ListView storyList;
    LinearLayout categoriesView;
    ArrayList<String> categories;
    ArrayList<Story> stories; //used to hold the current stories shown (with or without refined search)
    ArrayList<Story> storiesHolder; //used so that the original story list is always saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stories = (ArrayList<Story>) getIntent().getExtras().getSerializable("stories");
        categories = (ArrayList<String>) getIntent().getExtras().getSerializable("categories");
        storiesHolder = (ArrayList<Story>) stories.clone();
        settingStoryList();
        settingCategories();
    }

    protected void settingCategories() {
        categoriesView = findViewById(R.id.categoriesView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 0, 10, 0);
        for (int i = 0; i < categories.size(); i++) {
            FontAwesome category = new FontAwesome(getApplicationContext());
            category.setBackground(getDrawable(R.drawable.category_panel_round_edge));
            HelpFullFunctions.setViewColor(category, "#FE9025");
            category.setTextColor(Color.parseColor("#FFFFFF"));
            category.setPadding(50, 25, 50, 25);
            category.setText(categories.get(i));
            category.setTextSize(22);
            category.setLayoutParams(params);
            category.setElevation(15);
            categoriesView.addView(category, 0);
        }
    }

    protected void settingStoryList() {
        storyList = findViewById(R.id.storyList);
        storyList.setAdapter(new CustomStoryListAdapter(stories));
        storyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StoryListActivity.this, ReadingActivity.class);
                intent.putExtra("story", stories.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        settingStoryList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        if (menuItem != null) {
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    stories.clear();
                    int i = 0;
                    while (i < storiesHolder.size()) {
                        if (storiesHolder.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            stories.add(storiesHolder.get(i));
                            i++;
                        } else i++;
                    }
                    settingStoryList();
                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    stories = (ArrayList<Story>) storiesHolder.clone();
                    settingStoryList();
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.searchMenu) {
            return true;
        } else if (id == R.id.sortStoriesByDate) {
            StoryDataBase storyDataBase = new StoryDataBase(getApplicationContext(), null);
            stories = storyDataBase.loadStories();
            settingStoryList();
            Toast.makeText(getApplicationContext(), "Favorite stories loaded", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
