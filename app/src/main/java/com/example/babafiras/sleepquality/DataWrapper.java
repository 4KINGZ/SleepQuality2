package com.example.babafiras.sleepquality;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {
    private ArrayList<AccelData> vals;

    public DataWrapper(ArrayList<AccelData> data) {
        this.vals = data;
    }

    public ArrayList<AccelData> getAccData() {
        return this.vals;
    }

}
