package com.canadian.study.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zheng on 16/9/8.
 */

public class NetData implements Parcelable {

    public String name;
    public String url;

    protected NetData(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<NetData> CREATOR = new Creator<NetData>() {
        @Override
        public NetData createFromParcel(Parcel in) {
            return new NetData(in);
        }

        @Override
        public NetData[] newArray(int size) {
            return new NetData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
