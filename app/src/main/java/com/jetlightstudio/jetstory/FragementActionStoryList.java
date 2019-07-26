package com.jetlightstudio.jetstory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetlightstudio.jetstory.Controllers.ReadingActivity;
import com.jetlightstudio.jetstory.Models.Story;

import java.util.ArrayList;

public class FragementActionStoryList extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_action_story, container, false);
        grid = rootView.findViewById(R.id.grid);

        stories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("stories");
        storiesHolder = (ArrayList<Story>) stories.clone();
        actionStories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("actionStories");
        comedyStories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("comedyStories");
        romanceStories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("romanceStories");
        moralStories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("moralStories");
        sadStories = (ArrayList<Story>) getActivity().getIntent().getExtras().getSerializable("sadStories");
        settingStoryList(grid);

        return rootView;
    }

    protected void settingStoryList(GridView grid) {
        c = new CustomStoryAdapter();
        grid.setAdapter(c);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ReadingActivity.class);
                intent.putExtra("story", stories.get(i));
                startActivity(intent);
            }
        });
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
        }

        return super.onOptionsItemSelected(item);
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

        @SuppressLint("RestrictedApi")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            view = getLayoutInflater(null).inflate(R.layout.custom_story_list_view, null);
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

    {/*public class StoryListActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

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



        }


    }
    }
*/
    }
}
