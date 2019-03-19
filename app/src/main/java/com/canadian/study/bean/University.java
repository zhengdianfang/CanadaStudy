package com.canadian.study.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zheng on 16/9/8.
 */

public class University extends RealmObject implements Parcelable{

    @PrimaryKey
    public  long id;

    public String chineseName;
    public String englishName;
    public String pingyingIndex;
    public String detail;
    public String zone;
    public String type;
    public String email;
    public String phonenum;
    public String netUrl;
    public String picPaths;

    public boolean isJoinShow;

    public RealmList<SchoolCity> schoolCities;

    public University() {
    }

    protected University(Parcel in) {
        id = in.readLong();
        chineseName = in.readString();
        englishName = in.readString();
        pingyingIndex = in.readString();
        detail = in.readString();
        zone = in.readString();
        type = in.readString();
        email = in.readString();
        phonenum = in.readString();
        netUrl = in.readString();
        picPaths = in.readString();
        isJoinShow = in.readByte() != 0;
    }



    public static final Creator<University> CREATOR = new Creator<University>() {
        @Override
        public University createFromParcel(Parcel in) {
            return new University(in);
        }

        @Override
        public University[] newArray(int size) {
            return new University[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(chineseName);
        parcel.writeString(englishName);
        parcel.writeString(pingyingIndex);
        parcel.writeString(detail);
        parcel.writeString(zone);
        parcel.writeString(type);
        parcel.writeString(email);
        parcel.writeString(phonenum);
        parcel.writeString(netUrl);
        parcel.writeString(picPaths);
        parcel.writeByte((byte) (isJoinShow ? 1 : 0));
    }
}
