package com.champhay.mcomics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.champhay.Model.Util;
import com.champhay.Model.custom.adapter.ChapterListAdapter;
import com.champhay.Model.custom.component.NavigationDrawer;
import com.champhay.Model.handler.backgroundtask.CheckInternet;
import com.champhay.Model.handler.backgroundtask.LoadJsonInBackground;
import com.champhay.Model.handler.backgroundtask.ParserJSON;
import com.champhay.Model.handler.eventlistener.DownloadEvent;
import com.champhay.Model.handler.social.FacebookAPI;
import com.champhay.mcomics.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * HoangTP
 */

public class ComicChaptersActivity extends AppCompatActivity implements DownloadEvent {
    private GridView gridView;
    private String comicId;
    private NavigationDrawer navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckInternet.check(this)) {
            setContentView(R.layout.view_connect_fail);
            return;
        }
        setContentView(R.layout.view_navigation);
        navigationDrawer = new NavigationDrawer(this, R.layout.activity_comic_chapters, (ViewGroup) (findViewById(R.id.root).getParent()));

        comicId = getIntent().getStringExtra("id");
        LoadJsonInBackground loadJson = new LoadJsonInBackground();
        loadJson.setOnFinishEvent(this);
        loadJson.execute(Util.BASE_URL + "/comicsApi.php/getListChapterComics?id=" + comicId);
    }

    @Override
    public void onLoadFinish(String string) {
        ParserJSON parserJSON = new ParserJSON();
        try {
            final ArrayList<Integer> list = parserJSON.getChapterArray(string);
            gridView = ((GridView) findViewById(R.id.gridView));
            gridView.setAdapter(new ChapterListAdapter(this, list, comicId));
            gridView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(getBaseContext(), ComicsReadingActivity.class);
                intent.putExtra("id", comicId);
                intent.putExtra("chapter", position + 1);
                startActivity(intent);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
