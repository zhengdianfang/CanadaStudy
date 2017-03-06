package com.canadian.study.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zheng on 16/9/18.
 */

public class Message extends RealmObject implements Parcelable{

    @PrimaryKey
    public long id;

    public long time;
    public String content;
    public String title;

    protected Message(Parcel in) {
        id = in.readLong();
        time = in.readLong();
        content = in.readString();
        title = in.readString();
    }

    public Message() {
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(time);
        parcel.writeString(content);
        parcel.writeString(title);
    }
}
