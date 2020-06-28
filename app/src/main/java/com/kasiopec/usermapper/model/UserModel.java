package com.kasiopec.usermapper.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kasiopec.usermapper.GeocodingEndpointAPI;
import com.kasiopec.usermapper.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserModel {
    private Presenter presenter;

    boolean isInitialized = false;
    public UserModel(Presenter presenter){
        this.presenter = presenter;
    }

    private List<User> userList = new ArrayList<>();

    /**
     * Does the firs initialization, creating list of User objects to work with
     * **/
    void initialize(String users){
        users = users.replace("USERLIST", "").trim();
        String[] rows = users.split(";");
        for(String row : rows){
            String[] fields = row.split(",");

            int id  = Integer.parseInt(fields[0]);
            String name = fields[1];
            String imageUrl = fields[2];
            double lat = Double.parseDouble(fields[3]);
            double lng = Double.parseDouble(fields[4]);
            User user = new User(id, name, imageUrl, lat, lng);
            userList.add(user);
        }
        presenter.dataInitialized(userList);
        updateUsersAddresses();
    }

    /**
     * Small method that updates user addresses
     * **/
    private void updateUsersAddresses(){
        for (int i = 0; i < userList.size(); i++) {
           setUserAddress(userList.get(i));
        }
    }

    /**
     * Updates user position data
     * **/
    void updateData(String update){
        update = update.replace("UPDATE", "").trim();
        String[] fields = update.split(",");

        int id = Integer.parseInt(fields[0]);
        double lat = Double.parseDouble(fields[1]);
        double lng = Double.parseDouble(fields[2]);

        LatLng newLocation = new LatLng(lat, lng);

        for (int i = 0; i < userList.size(); i++) {
            LatLng oldLocation = new LatLng(userList.get(i).getLat(), userList.get(i).getLng());
            // Checking if the user exists and if the new user location is different
            if(userList.get(i).getId() == id && !newLocation.equals(oldLocation)){
                userList.get(i).setLat(lat);
                userList.get(i).setLng(lng);
                setUserAddress(userList.get(i));
            }
        }
    }

    /**
     * Establishes server connection and keep fetching new data
     * **/
    public void startFetch(){
        new ConnectTask(this).execute("");
    }

    /**
     * Method that calls Geocoding API from Google and performs reverse geocoding to get the address
     * when address is received updates user data
     * **/
    private void setUserAddress(User user){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.google.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GeocodingEndpointAPI geocodingEndpointAPI = retrofit.create(GeocodingEndpointAPI.class);
        String location = user.getLat() + ","+user.getLng();
        Call<JsonObject> call = geocodingEndpointAPI.getAddress(location, geocodingEndpointAPI.KEY);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    Log.d("Retrofit", "Code: " +response.code());
                }
                JsonArray arr = Objects.requireNonNull(response.body()).getAsJsonArray("results");
                String userAddress = arr.get(0).getAsJsonObject().get("formatted_address").getAsString();
                user.setAddress(userAddress);
                presenter.updateUserData(user);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Retrofit", "failure: "+ t.getMessage());
            }
        });

    }

}
