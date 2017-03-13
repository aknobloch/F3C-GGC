
package com.aarondevelops.f3c.chargelocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChargePoint {

    @SerializedName("station_list")
    @Expose
    private StationList stationList;

    public StationList getStationList() {
        return stationList;
    }

    public void setStationList(StationList stationList) {
        this.stationList = stationList;
    }

    public String toString()
    {
        return stationList.getTime();
    }

}
