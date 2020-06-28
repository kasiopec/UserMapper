package com.kasiopec.usermapper;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface GeocodingEndpointAPI {
    //API key should be removed when releasing, access it on the server
    //TODO Put your api key here and also update the string inside google_maps_api.xml file.
    String KEY = "your geocoding api key";
    @GET("/maps/api/geocode/json")
    Call<JsonObject> getAddress(@Query("latlng") String location, @Query("key") String key);
}
