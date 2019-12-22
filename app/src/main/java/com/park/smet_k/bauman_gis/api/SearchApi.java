package com.park.smet_k.bauman_gis.api;

import com.park.smet_k.bauman_gis.model.GoRoute;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {
    //    String BASE_URL = "http://95.163.214.14:8000/api/map/search?from=1&to=kek";
    String BASE_URL = "http://95.163.214.14:8000/api";


    @GET("/map/search")
    Call<GoRoute> getRoute(@Query("from") String from, @Query("to") String to);
}
