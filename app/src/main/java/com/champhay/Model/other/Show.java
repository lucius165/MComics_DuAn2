package com.champhay.Model.other;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lucius on 1/11/2017.
 */

public final class Show {
    public static void toastSHORT(Context Context, String text) {
        Toast.makeText(Context, text + "", Toast.LENGTH_SHORT).show();
    }

    public static void toastLONG(Context Context, String text) {
        Toast.makeText(Context, text + "", Toast.LENGTH_LONG).show();
    }

    public static void log(String tag, String content) {
//        Log.e(tag, content + "");
    }

}
