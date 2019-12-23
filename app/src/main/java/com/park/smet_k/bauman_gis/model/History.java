package com.park.smet_k.bauman_gis.model;

public class History {
    private String from_point;
    private String to_point;

    public History(String from_point, String to_point) {
        this.from_point = from_point;
        this.to_point = to_point;
    }

    public String getFrom() {
        return from_point;
    }

    public String getTo() {
        return to_point;
    }

    public String setFrom(String new_from) {
        this.from_point = new_from;
        return new_from;
    }

    public String setTo(String new_to) {
        this.to_point = new_to;
        return new_to;
    }
}
