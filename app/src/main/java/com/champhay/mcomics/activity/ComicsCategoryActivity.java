package com.champhay.mcomics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.champhay.Model.Util;
import com.champhay.Model.custom.adapter.ComicListCustomAdapter;
import com.champhay.Model.custom.component.NavigationDrawer;
import com.champhay.Model.handler.backgroundtask.CheckInternet;
import com.champhay.Model.handler.backgroundtask.LoadJsonInBackground;
import com.champhay.Model.handler.backgroundtask.ParserJSON;
import com.champhay.Model.handler.eventlistener.DownloadEvent;
import com.champhay.Model.handler.social.Comics;
import com.champhay.Model.handler.social.FacebookAPI;
import com.champhay.mcomics.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * HoangTP
 */

public class ComicsCategoryActivity extends AppCompatActivity implements DownloadEvent {
    private GridView androidGridView;
    private TextView text;
    private FacebookAPI facebookAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckInternet.check(this)) {
            setContentView(R.layout.view_connect_fail);
            return;
        }
        facebookAPI = new FacebookAPI(this);
        facebookAPI.init();
        setContentView(R.layout.view_navigation);
        new NavigationDrawer(this, R.layout.activity_comics_category, (ViewGroup) (findViewById(R.id.root)).getParent());

        text = (TextView) findViewById(R.id.text);
        LoadJsonInBackground backgroundTask = new LoadJsonInBackground();
        backgroundTask.setOnFinishEvent(this);
        try {
            backgroundTask.execute(Util.BASE_URL + "/comicsApi.php/getComicsByKind?kind=" + getIntent().getExtras().getInt("id"));
        } catch (Exception ignored) {

        }
    }

    //button search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public void onLoadFinish(String string) {
        ParserJSON parserJSON = new ParserJSON();
        try {
            ArrayList<Comics> arrComics = parserJSON.getComicArray(string);
            ComicListCustomAdapter adapterViewAndroid = new ComicListCustomAdapter(ComicsCategoryActivity.this, arrComics);
            androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
            androidGridView.setAdapter(adapterViewAndroid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookAPI.onActivityResult(requestCode, resultCode, data);
    }
}
