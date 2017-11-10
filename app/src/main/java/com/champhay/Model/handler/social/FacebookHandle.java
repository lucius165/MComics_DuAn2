package com.champhay.Model.handler.social;

import com.champhay.Model.handler.eventlistener.DownloadEvent;
import com.champhay.Model.other.Show;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by lucius on 1/11/2017.
 */

public class FacebookHandle {
    public static final String COMMENTS = "comments";
    public static final String LIKES = "likes";
    public static final String SHAREDPOSTS = "sharedposts";

    public void getCount(String id, String objectName, final DownloadEvent downloadEvent) {
        Show.log("getCount.id", id);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/" + objectName,
                null,
                HttpMethod.GET,
                response -> {
                    Show.log("getCount.response", response.toString());
                    try {
                        int count = response.getJSONObject().getJSONArray("data").length();
                        Show.log("getCount", count + "");
                        downloadEvent.onLoadFinish(response.getJSONObject().getJSONArray("data").length() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).executeAsync();
    }

    public ArrayList<FaceBookComment> getFbCommentList(JSONArray array) throws JSONException {
        ArrayList<FaceBookComment> list = new ArrayList<>();
        for (int x = 0; x < array.length(); x++) {
            JSONObject data = array.getJSONObject(x);
            JSONObject user = data.getJSONObject("from");
            list.add(new FaceBookComment(
                    data.getString("created_time"),
                    user.getString("id"),
                    user.getString("name"),
                    data.getString("message"),
                    data.getString("id")));
        }
        return list;
    }


}
