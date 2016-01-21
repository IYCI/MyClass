package com.YC2010.MyClass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Danny on 2015/10/17.
 */
public class FinalObject implements Parcelable {
    private String section;
    private String time;
    private String location;
    private String date;
    private boolean isOnline;

    public FinalObject() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeByte((byte) (isOnline ? 1 : 0));
    }

    public FinalObject(Parcel src) {
        this.section = src.readString();
        this.time = src.readString();
        this.location = src.readString();
        this.date = src.readString();
        this.isOnline = src.readByte() == 1;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public FinalObject createFromParcel(Parcel source) {
            return new FinalObject(source);
        }

        @Override
        public FinalObject[] newArray(int size) {
            return new FinalObject[size];
        }
    };

    public void setSection(String section) {
        this.section = section;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getSection() {
        return section;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
