package com.champhay.mcomics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.champhay.Model.Util;
import com.champhay.Model.custom.component.NavigationDrawer;
import com.champhay.Model.handler.backgroundtask.CheckInternet;
import com.champhay.Model.handler.backgroundtask.LoadJsonInBackground;
import com.champhay.Model.handler.backgroundtask.ParserJSON;
import com.champhay.Model.handler.eventlistener.DownloadEvent;
import com.champhay.Model.handler.social.Comics;
import com.champhay.Model.handler.social.FacebookAPI;
import com.champhay.Model.handler.social.FacebookContent;
import com.champhay.Model.handler.social.FacebookHandle;
import com.champhay.Model.other.Show;
import com.champhay.mcomics.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * HoangTP
 */

public class ComicDetailActivity extends AppCompatActivity implements DownloadEvent {
    private Button btn_openComics;
    private boolean isShow;
    private TextView txv_readMoreTop, txv_readMoreBottom, txv_review;
    private NavigationDrawer navigationDrawer;
    private String id;
    private Comics comics;
    private LinearLayout ll_social, ll_login;
    private TextView like, share, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckInternet.check(this)) {
            setContentView(R.layout.view_connect_fail);
            return;
        }
        setContentView(R.layout.view_navigation);
        navigationDrawer = new NavigationDrawer(this, R.layout.activity_comics_detail, (ViewGroup) findViewById(R.id.root).getParent());
        getView();

        id = getIntent().getExtras().getString("id");
        Show.log("id", id);

        LoadJsonInBackground loadJson = new LoadJsonInBackground();
        loadJson.setOnFinishEvent(this);
        loadJson.execute(Util.BASE_URL + "/comicsApi.php/getComicsDetailById?id=" + id);

        btn_openComics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getBaseContext(), ComicChaptersActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });

        txv_readMoreBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideReview();
            }
        });
    }

    public void getView() {
        btn_openComics = (Button) findViewById(R.id.btnDoctruyen);
        txv_review = (TextView) findViewById(R.id.txv_review);
        txv_readMoreTop = (TextView) findViewById(R.id.txv_readMoreTop);
        txv_readMoreBottom = (TextView) findViewById(R.id.txv_readMoreBottom);
    }

    public void showHideReview() {
        if (isShow = !isShow) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        txv_review.setMaxLines(Integer.MAX_VALUE);
        txv_readMoreBottom.setText("Ẩn");
    }

    public void hide() {
        txv_review.setMaxLines(3);
        txv_readMoreBottom.setText("Đọc thêm");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigationDrawer.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoadFinish(String string) {
        try {
            comics = new ParserJSON().getComic(new JSONObject(string));
            Picasso.with(this).load(comics.getThumbnail()).resize(400, 600).error(R.mipmap.bia).into(((ImageView) findViewById(R.id.imv_cover)));
            ((TextView) findViewById(R.id.txv_name)).setText(comics.getComicsName());
            ((TextView) findViewById(R.id.txv_author)).setText(comics.getAuthor());
            ((TextView) findViewById(R.id.txv_kinds)).setText(comics.getKind());
            ((TextView) findViewById(R.id.txv_chapter)).setText(comics.getEpisodes());
            ((TextView) findViewById(R.id.txv_views)).setText(comics.getViews() + "");
            ((TextView) findViewById(R.id.txv_review)).setText(comics.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
