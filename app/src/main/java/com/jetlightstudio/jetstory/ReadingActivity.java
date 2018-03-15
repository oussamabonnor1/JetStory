package com.jetlightstudio.jetstory;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReadingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Story story;
    TextView title;
    TextView content;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        story = (Story) getIntent().getExtras().getSerializable("story");
        title = (TextView) findViewById(R.id.titleText);
        content = (TextView) findViewById(R.id.contentText);
        title.setText(story.getTitle());
        content.setText("This story is: " + story.getTitle() + " written by " + story.getAuthor()
                + " in the date " + story.getDate() + " the story's ID is " + story.getId() + " and it s readin time is " + story.getTime()
                + " the category of this story is: " + story.getCategory() + " content: "
                + story.getContent());
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
        getMenuInflater().inflate(R.menu.reading, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

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
        }else{
            menuItem.setIcon(R.drawable.ic_file_download_white_24dp);
        }
    }
}
