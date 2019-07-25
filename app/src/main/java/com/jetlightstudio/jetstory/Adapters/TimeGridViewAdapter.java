package com.jetlightstudio.jetstory.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.jetlightstudio.jetstory.R;
import com.jetlightstudio.jetstory.ToolBox.App;

import java.util.ArrayList;
import java.util.Arrays;

public class TimeGridViewAdapter extends BaseAdapter {
    ArrayList<String> times;

    public TimeGridViewAdapter() {
        this.times = new ArrayList<>();
        times.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "+10"));
    }

    @Override
    public int getCount() {
        return times.size();
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
        WindowManager wm = (WindowManager) App.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 120;
        view = inflater.inflate(R.layout.custom_time_selection_view, null);
        view.setLayoutParams(new GridView.LayoutParams(width / 5, width / 5));

        TextView timeTextView = view.findViewById(R.id.timeTextView);
        timeTextView.setText(times.get(i));

        return view;
    }
}
