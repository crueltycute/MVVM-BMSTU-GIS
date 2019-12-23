package com.park.smet_k.bauman_gis.model;

import com.google.gson.annotations.SerializedName;

public class GoNode {
    @SerializedName("Int64")
    private Integer int64;
    @SerializedName("Valid")
    private Boolean valid;

    public GoNode(Integer int64, Boolean valid) {
        this.int64 = int64;
        this.valid = valid;
    }

    public Integer getInt64() {
        return int64;
    }

    public Boolean getValid() {
        return valid;
    }
}
