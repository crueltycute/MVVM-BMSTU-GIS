package com.park.smet_k.bauman_gis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoRoute {
    @SerializedName("points")
    private List<GoPoint> points;

    public GoRoute(List<GoPoint> points) {
        this.points = points;
    }

    public List<GoPoint> getPoints() {
        return points;
    }
}
