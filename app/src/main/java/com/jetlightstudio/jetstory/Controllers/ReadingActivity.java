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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.App;
import com.jetlightstudio.jetstory.ToolBox.FontAwesome;
import com.jetlightstudio.jetstory.ToolBox.StoryDataBase;

public class ReadingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Story story;
    TextView title;
    TextView content;
    FontAwesome authorName;
    FontAwesome publishingDate;
    FontAwesome timeToRead;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        story = (Story) getIntent().getExtras().getSerializable("story");
        title = findViewById(R.id.titleText);
        content = findViewById(R.id.contentText);
        authorName = findViewById(R.id.authorText);
        timeToRead = findViewById(R.id.timeText);
        publishingDate = findViewById(R.id.dateText);

        title.setText(story.getTitle());
        content.setText(story.getContent());
        String author = App.getContext().getString(R.string.icon_author) + " by " + story.getAuthor();
        authorName.setText(author);
        String date = App.getContext().getString(R.string.icon_publish_date) + " on " + story.getDate();
        publishingDate.setText(date);
        String time = App.getContext().getString(R.string.icon_time) + " " + story.getTime() + " mn";
        timeToRead.setText(time);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        ImageView navStoryAlbum = header.findViewById(R.id.nav_story_album);
        navStoryAlbum.setImageResource(R.drawable.book_green);
        TextView navStoryTitle = header.findViewById(R.id.nav_story_title);
        navStoryTitle.setText(story.getTitle());
        TextView navAuthorName = header.findViewById(R.id.nav_author_name);
        navAuthorName.setText(story.getAuthor());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reading_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
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
                    String text = content.getText().toString();

                    int searchWordIndex = text.indexOf(newText, 0);
                    Spannable WordtoSpan = new SpannableString(content.getText().toString());

                    for (int i = 0; i < text.length() && searchWordIndex != -1; i = searchWordIndex + 1) {
                        searchWordIndex = text.indexOf(newText, i);
                        if (searchWordIndex == -1)
                            break;
                        else {
                            WordtoSpan.setSpan(new BackgroundColorSpan(0xFFFFFF55), searchWordIndex, searchWordIndex + newText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            content.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                        }
                    }
                    return false;
                }
            });
        }

        //checking if story is already saved on database to decide which icon to show (save/delete)
        this.menu = menu;
        changeSaveIcon(this.menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.download) {
            StoryDataBase base = new StoryDataBase(getApplicationContext(), null);
            base.saveStory(story, getApplicationContext());
            changeSaveIcon(menu);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.developer) {
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

    protected void changeSaveIcon(Menu menu) {
        StoryDataBase db = new StoryDataBase(getApplicationContext(), null);
        MenuItem menuItem = menu.findItem(R.id.download);
        if (db.isStorySaved(story.getId())) {
            menuItem.setIcon(R.drawable.ic_delete_white_24dp);
        } else {
            menuItem.setIcon(R.drawable.ic_file_download_white_24dp);
        }
    }

}
