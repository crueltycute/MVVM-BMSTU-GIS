package com.park.smet_k.bauman_gis.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoRoute implements Serializable {
    @SerializedName("points")
    private List<GoPoint> points;

    public GoRoute() {
    }

    public GoRoute(List<GoPoint> points) {
        this.points = points;
    }

    public List<GoPoint> getPoints() {
        return points;
    }
}
