package com.champhay.mcomics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.champhay.Model.Util;
import com.champhay.Model.custom.adapter.AdapterImage;
import com.champhay.Model.custom.component.NavigationDrawer;
import com.champhay.Model.custom.component.SettingHandle;
import com.champhay.Model.handler.backgroundtask.CheckInternet;
import com.champhay.Model.handler.backgroundtask.LoadJsonInBackground;
import com.champhay.Model.handler.backgroundtask.ParserJSON;
import com.champhay.Model.handler.eventlistener.DownloadEvent;
import com.champhay.Model.handler.eventlistener.OrientationChangeListener;
import com.champhay.Model.handler.social.Content;
import com.champhay.Model.handler.social.FacebookAPI;
import com.champhay.mcomics.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * HoangTP
 */

public class ComicsReadingActivity extends AppCompatActivity implements OrientationChangeListener {
    private ArrayList<Content> urlList;
    private RecyclerView recyclerView;
    private String id;
    private String chapter;
    private NavigationDrawer navigationDrawer;
    private SettingHandle settingHandle;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckInternet.check(this)) {
            setContentView(R.layout.view_connect_fail);
            return;
        }
        setContentView(R.layout.view_navigation);
        navigationDrawer = new NavigationDrawer(this, R.layout.activity_comics_reading, (ViewGroup) (findViewById(R.id.root).getParent()));
        navigationDrawer.hideActionbar();

        settingHandle = navigationDrawer.getSettingHandle();
        settingHandle.setOrientationListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.cardView);
        layoutManager = new LinearLayoutManager(getApplication());
        layoutManager.setOrientation(settingHandle.getOrientation());
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        chapter = intent.getIntExtra("chapter", 0) + "";
        LoadJsonInBackground loadJsonInBackground = new LoadJsonInBackground();
        loadJsonInBackground.setOnFinishEvent(string -> load(string));
        loadJsonInBackground.execute(Util.BASE_URL + "/comicsApi.php/getComicsDetail?id=" + id + "&chapter=" + chapter);
    }

    public void load(String s) {
        ParserJSON parserJSON = new ParserJSON();
        try {
            urlList = parserJSON.getPage(s);
            showView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChanged(int orientation) {
        showView();
    }

    public void showView() {
        layoutManager.setOrientation(settingHandle.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        AdapterImage adapter = new AdapterImage(this, urlList);
        recyclerView.setAdapter(adapter);
    }
}
