package com.kasiopec.usermapper.ui;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kasiopec.usermapper.presenter.Contract;
import com.kasiopec.usermapper.presenter.Presenter;

import com.kasiopec.usermapper.R;
import com.kasiopec.usermapper.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Contract.View {

    private GoogleMap mMap;
    Presenter presenter;
    HashMap<User, Marker> markedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        presenter = new Presenter(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        presenter.loadUsers();
    }

    /**
     * Method which updates user marker position and address text
     * **/
    @Override
    public void updateUsersMarkers(User updatedUser) {
        LatLng newMarkerPos = new LatLng(updatedUser.getLat(), updatedUser.getLng());
        for (Map.Entry<User, Marker> entry : markedUsers.entrySet()) {
            if(updatedUser.getId() == entry.getKey().getId()){
                // Moving marker to a new position
                MarkerAnimator.animateMarkerToICS(entry.getValue(), newMarkerPos);
                // Setting marker position to the new position
                entry.getValue().setPosition(newMarkerPos);
                // Updating Marker info box with the new address
                updateMarkerAddress(updatedUser.getAddress(), entry.getValue());
                // Setting user address to a new address
                entry.getKey().setAddress(updatedUser.getAddress());
                // Check if the info box is open and redraw it with the new data
                if (entry.getValue() != null && entry.getValue().isInfoWindowShown()) {
                    entry.getValue().hideInfoWindow();
                    entry.getValue().showInfoWindow();
                }
            }
        }
    }

    /**
     * Method which renders markers for the first time.
     **/
    @Override
    public void displayMarkers(List<User> userList) {
        // Creating markers and mapping them to the users
        markedUsers = new HashMap<>();
        for (int i = 0; i < userList.size(); i++) {
            LatLng userCoords = new LatLng(userList.get(i).getLat(), userList.get(i).getLng());
            final Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(userCoords)
                    .title(userList.get(i).getName())
            );
            marker.setTag(userList.get(i).getImage());
            markedUsers.put(userList.get(i), marker);
        }
        // Getting location of the first user and center the screen on it
        if(userList.size() > 0){
            LatLng firstUserLocation = new LatLng(userList.get(0).getLat(), userList.get(0).getLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstUserLocation, 16));
        }
    }

    /**
     * Method that splits the address string into pieces and adds them to the infobox as a snippet
     * **/
    private void updateMarkerAddress(String address, Marker marker){
        if(address == null){
            marker.setSnippet("Address was not found");
            return;
        }
        String[] fields = address.split(",");
        String street = fields[0];
        String region = fields[1];
        String city = fields[2];
        String zipCode = fields[3];
        String country = fields[4];

        marker.setSnippet("Address \n" + street +
                "\n" + region + ", " + city + "\n" + zipCode + "\n" + country);
    }
}
