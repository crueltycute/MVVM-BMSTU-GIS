package com.park.smet_k.bauman_gis.model;

import com.google.gson.annotations.SerializedName;

//{
//        "id": 19,
//        "name": "TP",
//        "cabinet": true,
//        "stair": false,
//        "x": 160,
//        "y": 679,
//        "level": 1,
//        "node_id": {}
//},

public class GoPoint {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("cabinet")
    private Boolean cabinet;
    @SerializedName("stair")
    private Boolean stair;
    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;
    @SerializedName("level")
    private Integer level;
    @SerializedName("node_id")
    private GoNode node_id;

    public GoPoint(Integer id, String name, Boolean cabinet, Boolean stair, Integer x, Integer y, Integer level, GoNode node_id) {
        this.id = id;
        this.name = name;
        this.cabinet = cabinet;
        this.stair = stair;
        this.x = x;
        this.y = y;
        this.level = level;
        this.node_id = node_id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getCabinet() {
        return cabinet;
    }

    public Boolean getStair() {
        return stair;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getLevel() {
        return level;
    }

    public GoNode getNode_id() {
        return node_id;
    }
}
