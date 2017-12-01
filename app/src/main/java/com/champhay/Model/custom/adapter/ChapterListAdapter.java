package com.champhay.Model.custom.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.champhay.mcomics.R;

import java.util.ArrayList;

/**
 * Created by Hoangtp
 */

public class ChapterListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Integer> list;
    private String id;

    public ChapterListAdapter(Activity activity, ArrayList<Integer> list) {
        this.activity = activity;
        this.list = list;
    }

    public ChapterListAdapter(Activity activity, ArrayList<Integer> list, String id) {
        this.activity = activity;
        this.list = list;
        this.id = id;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = (LayoutInflater.from(activity)).inflate(R.layout.view_chapter_item, parent, false);
        TextView textView = ((TextView) view.findViewById(R.id.text));
        textView.setText("Chapter " + list.get(position));
        return view;
    }


}
