package com.YC2010.jason.myclass.DataObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Danny on 2015/10/17.
 */
public class TestObject implements Parcelable {
    private String section;
    private String time;
    private String location;
    private String capacity;
    private String total;

    public TestObject() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(capacity);
        dest.writeString(total);
    }

    public TestObject(Parcel src) {
        this.section = src.readString();
        this.time = src.readString();
        this.location = src.readString();
        this.capacity = src.readString();
        this.total = src.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public TestObject createFromParcel(Parcel source) {
            return new TestObject(source);
        }

        @Override
        public TestObject[] newArray(int size) {
            return new TestObject[size];
        }
    };

    // Setters

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

    public void setLocation(String location) {
        this.location = location;
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
