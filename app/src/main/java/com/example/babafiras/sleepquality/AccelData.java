package com.example.babafiras.sleepquality;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class AccelData implements Serializable{
    private long date;
    private double x;
    private double y;
    private double z;

    public AccelData(long date, double x, double y, double z) {
        this.date =date;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public String toString()
    {
        return "x="+x+", y="+y+", z="+z;
    }


}