
package com.aarondevelops.f3c.chargelocation;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationList {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("watermark")
    @Expose
    private long watermark;
    @SerializedName("map_data_size")
    @Expose
    private long mapDataSize;
    @SerializedName("summaries")
    @Expose
    private List<Summary> summaries = null;
    @SerializedName("page_offset")
    @Expose
    private String pageOffset;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getWatermark() {
        return watermark;
    }

    public void setWatermark(long watermark) {
        this.watermark = watermark;
    }

    public long getMapDataSize() {
        return mapDataSize;
    }

    public void setMapDataSize(long mapDataSize) {
        this.mapDataSize = mapDataSize;
    }

    public List<Summary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<Summary> summaries) {
        this.summaries = summaries;
    }

    public String getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(String pageOffset) {
        this.pageOffset = pageOffset;
    }

}
