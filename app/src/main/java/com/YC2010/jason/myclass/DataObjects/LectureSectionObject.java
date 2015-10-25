package com.YC2010.jason.myclass.DataObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Danny on 2015/10/17.
 */
public class LectureSectionObject implements Parcelable {
    private String number;
    private String section;
    private String time;
    private String professor;
    private String location;
    private String capacity;
    private String total;

    public LectureSectionObject(String number, String section, String time, String professor, String location, String capacity, String total) {
        this.number = number;
        this.section = section;
        this.time = time;
        this.professor = professor;
        this.location = location;
        this.capacity = capacity;
        this.total = total;
    }

    public LectureSectionObject() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(section);
        dest.writeString(time);
        dest.writeString(professor);
        dest.writeString(location);
        dest.writeString(capacity);
        dest.writeString(total);
    }

    public LectureSectionObject(Parcel src) {
        this.number = src.readString();
        this.section = src.readString();
        this.time = src.readString();
        this.professor = src.readString();
        this.location = src.readString();
        this.capacity = src.readString();
        this.total = src.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public LectureSectionObject createFromParcel(Parcel source) {
            return new LectureSectionObject(source);
        }

        @Override
        public LectureSectionObject[] newArray(int size) {
            return new LectureSectionObject[size];
        }
    };

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
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

    public String getProfessor() {
        return professor;
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
