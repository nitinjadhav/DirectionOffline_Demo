package com.ndroid.checklocation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sony on 2015-10-04.
 */
public class LocPoitns implements Parcelable {

    double latitude;
    double longitude;

    public static final Parcelable.Creator<LocPoitns> CREATOR =
            new Parcelable.Creator<LocPoitns>() {
        @Override
        public LocPoitns createFromParcel(Parcel in) {
            return new LocPoitns(in);
        }

        @Override
        public LocPoitns[] newArray(int size) {
            return new LocPoitns[size];
        }
    };

    public LocPoitns() {

    }

    private LocPoitns(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

}
