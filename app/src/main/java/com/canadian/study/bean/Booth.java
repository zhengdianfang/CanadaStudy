package com.canadian.study.bean;

import android.content.Context;
import android.graphics.RectF;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zheng on 16/9/8.
 */

public class Booth extends RealmObject {

    @PrimaryKey
    public long id;

    public String mark;
    public int x;
    public int y;
    public int width;
    public int height;

    @Ignore
    public RectF rect;

    public String joinCity;

    public RectF createRect(Context context, float scale) {
        float densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        densityDpi = densityDpi/320;
        rect = new RectF(x * densityDpi, y * densityDpi, (x + width) * densityDpi, (y + height)*densityDpi);
        return rect;
    }

}
