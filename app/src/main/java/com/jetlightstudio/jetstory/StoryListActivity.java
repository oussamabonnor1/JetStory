package com.jetlightstudio.jetstory;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Point;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    GridView grid;
    ArrayList<Story> stories; //used to hold the current stories shown (with or without refined search)
    ArrayList<Story> storiesHolder; //used so that the original story list is always saved
    ArrayList<Story> comedyStories;
    ArrayList<Story> actionStories;
    ArrayList<Story> romanceStories;
    ArrayList<Story> sadStories;
    ArrayList<Story> moralStories;
    CustomStoryAdapter c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        stories = (ArrayList<Story>) getIntent().getExtras().getSerializable("stories");
        storiesHolder = (ArrayList<Story>) stories.clone();
        actionStories = (ArrayList<Story>) getIntent().getExtras().getSerializable("actionStories");
        comedyStories = (ArrayList<Story>) getIntent().getExtras().getSerializable("comedyStories");
        romanceStories = (ArrayList<Story>) getIntent().getExtras().getSerializable("romanceStories");
        moralStories = (ArrayList<Story>) getIntent().getExtras().getSerializable("moralStories");
        sadStories = (ArrayList<Story>) getIntent().getExtras().getSerializable("sadStories");
        settingStoryList();

    }

    protected void settingStoryList() {
        c = new CustomStoryAdapter();
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(c);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    ArrayList<Story> storiesTemp = (ArrayList<Story>) storiesHolder.clone();
                    int i = 0;
                    while (i < storiesTemp.size()) {
                        if (!storiesTemp.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            storiesTemp.remove(i);
                        } else i++;
                    }
                    stories = (ArrayList<Story>) storiesTemp.clone();
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
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            view = getLayoutInflater().inflate(R.layout.custom_story_adapter, null);
            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height / 3));

            ImageView cover = view.findViewById(R.id.coverImage);
            TextView title = view.findViewById(R.id.titleText);
            TextView author = view.findViewById(R.id.authorText);
            TextView year = view.findViewById(R.id.yearText);

            cover.setBackgroundResource(stories.get(i).getAlbumId());
            title.setText(stories.get(i).getTitle());
            author.setText(stories.get(i).getAuthor());
            year.setText(stories.get(i).getDate());
            return view;
        }

    }
}
