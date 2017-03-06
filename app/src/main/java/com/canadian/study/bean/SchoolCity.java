package com.canadian.study.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zheng on 16/9/8.
 */

public class SchoolCity extends RealmObject implements Parcelable {

    @PrimaryKey
    public long id;

    public String universityName;
    public String joinCity;
    public String siteNum;

    public University universitie;

    public SchoolCity() {
    }

    protected SchoolCity(Parcel in) {
        id = in.readLong();
        universityName = in.readString();
        joinCity = in.readString();
        siteNum = in.readString();
        universitie = in.readParcelable(University.class.getClassLoader());
    }

    public static final Creator<SchoolCity> CREATOR = new Creator<SchoolCity>() {
        @Override
        public SchoolCity createFromParcel(Parcel in) {
            return new SchoolCity(in);
        }

        @Override
        public SchoolCity[] newArray(int size) {
            return new SchoolCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(universityName);
        parcel.writeString(joinCity);
        parcel.writeString(siteNum);
        parcel.writeParcelable(universitie, i);
    }
}
