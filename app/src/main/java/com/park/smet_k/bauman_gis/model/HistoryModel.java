package com.park.smet_k.bauman_gis.model;

import com.google.gson.annotations.SerializedName;

public class HistoryModel {
    @SerializedName("id")
    private Integer ID;
    @SerializedName("to_point")
    private String to_point;
    @SerializedName("from_point")
    private String from_point;

    public HistoryModel(String from_point, String to_point) {
        this.from_point = from_point;
        this.to_point = to_point;
    }

    public Integer getID() {
        return ID;
    }

    public String getFrom() {
        return from_point;
    }

    public String getTo() {
        return to_point;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setTo(String new_to) {
        this.to_point = new_to;
    }

    public void setFrom(String new_from) {
        this.from_point = new_from;
    }
}
