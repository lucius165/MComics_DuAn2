package com.champhay.Model.handler.backgroundtask;

import com.champhay.Model.handler.social.Comics;
import com.champhay.Model.handler.social.ComicsKind;
import com.champhay.Model.handler.social.Content;
import com.champhay.Model.handler.social.FacebookContent;
import com.champhay.Model.other.Show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HoangTP
 */

public class ParserJSON {
    public ArrayList<Comics> getComicArray(String JSON) throws JSONException {
        ArrayList<Comics> comicsArray = new ArrayList<>();
        if (JSON == null) return comicsArray;
        JSONArray jsonArray = new JSONArray(JSON);
        for (int x = 0; x < jsonArray.length(); x++) {
            comicsArray.add(getComic(jsonArray.getJSONObject(x)));
        }
        return comicsArray;
    }

    public Comics getComic(JSONObject jsonObject) throws JSONException {
        return new Comics(jsonObject.getInt("ID"),
                jsonObject.getString("COMICS_NAME"),
                jsonObject.getString("COMICS_TYPE"),
                jsonObject.getString("THUMBNAIL"),
                jsonObject.getInt("VIEWS"),
                jsonObject.getString("AUTHOR"),
//                jsonObject.getString("episodes"),
                "",
                jsonObject.getString("CONTENT"));
    }

    public ArrayList<ComicsKind> getComicKindArray(String json) throws JSONException {
        ArrayList<ComicsKind> comicKindArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int x = 0; x < jsonArray.length(); x++) {
            comicKindArray.add(getComicKind(jsonArray.getJSONObject(x)));
        }
        return comicKindArray;
    }

    public ComicsKind getComicKind(JSONObject jsonObject) throws JSONException {
        return new ComicsKind(jsonObject.getInt("ID"),
                jsonObject.getString("COMICS_TYPE"));
    }

    public ArrayList<Integer> getChapterArray(String json) throws JSONException {
        ArrayList<Integer> chapterArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int x = 0; x < jsonArray.length(); x++) {
            chapterArray.add(jsonArray.getJSONObject(x).getInt("chapter_number"));
        }
        return chapterArray;
    }

    public ArrayList<Content> getPage(String json) throws JSONException {
        ArrayList<Content> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int x = 0; x < jsonArray.length(); x++) {
            int page = Integer.parseInt(jsonArray.getJSONObject(x).getString("page_number"));
            String link = jsonArray.getJSONObject(x).getString("link");
            list.add(new Content(page, link));
        }
        return list;
    }

    public FacebookContent getFacebookContentInfo(String JSON) throws JSONException {
        JSONObject jsonObject = new JSONArray(JSON).getJSONObject(0);
        Show.log("getFbContentInfo", jsonObject.toString());
        return new FacebookContent(
                jsonObject.getString("comics_master_id"),
                jsonObject.getString("fb_short_id"),
                jsonObject.getString("fb_long_id"),
                jsonObject.getString("fb_link"));
    }
}
