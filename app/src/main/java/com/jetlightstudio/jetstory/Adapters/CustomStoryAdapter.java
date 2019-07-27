package com.jetlightstudio.jetstory.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetlightstudio.jetstory.Models.Story;
import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.App;
import com.jetlightstudio.jetstory.ToolBox.FontAwesome;

import java.util.ArrayList;

public class CustomStoryAdapter extends BaseAdapter {
    ArrayList<Story> stories;

    public CustomStoryAdapter(ArrayList<Story> stories) {
        this.stories = stories;
    }

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
        LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_story_list_view, null);

        ImageView cover = view.findViewById(R.id.coverImage);
        TextView title = view.findViewById(R.id.titleText);
        FontAwesome author = view.findViewById(R.id.authorText);
        FontAwesome publishingDate = view.findViewById(R.id.yearText);
        TextView content = view.findViewById(R.id.contentText);
        FontAwesome categoryLabel = view.findViewById(R.id.storyListCategoryLabel);

        //cover.setBackgroundResource(stories.get(i).getAlbumId());
        title.setText(stories.get(i).getTitle());
        String authorName = App.getContext().getString(R.string.icon_author) + " by " + stories.get(i).getAuthor();
        author.setText(authorName);
        String date = App.getContext().getString(R.string.icon_publish_date) + " on " + stories.get(i).getDate();
        publishingDate.setText(date);
        content.setText(stories.get(i).getContent());
        categoryLabel.setText(stories.get(i).getCategory().name());
        return view;
    }

}
