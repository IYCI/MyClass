package com.YC2010.jason.myclass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Danny on 2015/10/17.
 */
public class TutorialObject implements Parcelable {
    private String number;
    private String section;
    private String time;
    private String location;
    private String capacity;
    private String total;

    public TutorialObject() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(section);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(capacity);
        dest.writeString(total);
    }

    public TutorialObject(Parcel src) {
        this.number = src.readString();
        this.section = src.readString();
        this.time = src.readString();
        this.location = src.readString();
        this.capacity = src.readString();
        this.total = src.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public TutorialObject createFromParcel(Parcel source) {
            return new TutorialObject(source);
        }

        @Override
        public TutorialObject[] newArray(int size) {
            return new TutorialObject[size];
        }
    };

    public void setTotal(String total) {
        this.total = total;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
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

    public String getCapacity() {
        return capacity;
    }

    public String getTotal() {
        return total;
    }
}
