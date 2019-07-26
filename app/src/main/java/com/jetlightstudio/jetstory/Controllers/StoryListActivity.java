package com.jetlightstudio.jetstory.Controllers;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.App;
import com.jetlightstudio.jetstory.ToolBox.StoryDataBase;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ListView storyList;
    ArrayList<Story> stories; //used to hold the current stories shown (with or without refined search)
    ArrayList<Story> storiesHolder; //used so that the original story list is always saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        stories = (ArrayList<Story>) getIntent().getExtras().getSerializable("stories");
        storiesHolder = (ArrayList<Story>) stories.clone();
        settingStoryList();

    }

    protected void settingStoryList() {
        storyList = findViewById(R.id.storyList);
        storyList.setAdapter(new CustomStoryAdapter());
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story_list, menu);
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
        } else if (id == R.id.loadStories) {
            StoryDataBase storyDataBase = new StoryDataBase(getApplicationContext(), null);
            stories = storyDataBase.loadStories();
            settingStoryList();
            Toast.makeText(getApplicationContext(), "Favorite stories loaded", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.loadStories) {
            StoryDataBase storyDataBase = new StoryDataBase(getApplicationContext(), null);
            stories = storyDataBase.loadStories();
            settingStoryList();
            Toast.makeText(getApplicationContext(), "Favorite stories loaded", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.developer) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/JetLightstudio"));
            startActivity(i);
        } else if (id == R.id.appVersion) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.JetLightstudio.JetStory"));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class CustomStoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stories.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_story_list_view, null);

            ImageView cover = view.findViewById(R.id.coverImage);
            TextView title = view.findViewById(R.id.titleText);
            TextView author = view.findViewById(R.id.authorText);
            TextView publishingDate = view.findViewById(R.id.yearText);
            TextView content = view.findViewById(R.id.contentText);
            BootstrapLabel categoryLabel = view.findViewById(R.id.storyListCategoryLabel);

            //cover.setBackgroundResource(stories.get(i).getAlbumId());
            title.setText(stories.get(i).getTitle());
            String authorName = App.getContext().getString(R.string.icon_plus) + " by " + stories.get(i).getAuthor();
            author.setText(authorName);
            String date = App.getContext().getString(R.string.icon_publish_date) + " on " + stories.get(i).getDate();
            publishingDate.setText(date);
            content.setText(stories.get(i).getContent());
            categoryLabel.setText(stories.get(i).getCategory().name());
            return view;
        }

    }
}
