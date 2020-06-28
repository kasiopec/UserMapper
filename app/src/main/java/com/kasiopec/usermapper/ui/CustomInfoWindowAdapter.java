package com.kasiopec.usermapper.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.kasiopec.usermapper.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View view;

    /**
     * Custom adapter for the Info window of the marker. Displays various information of the user.
     * **/
    CustomInfoWindowAdapter(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderInfo(Marker marker, View view){

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);
        ImageView userImage = (ImageView) view.findViewById(R.id.userImage);

        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        String imageUrl = Objects.requireNonNull(marker.getTag()).toString();

        if(!title.equals("")) {
            tvTitle.setText(title);
        }
        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }
        // Calls picasso for the image on with the callback to update the image when loaded.
        Picasso.get()
                .load(imageUrl)
                .into(userImage, new MarkerCallback(marker, imageUrl, userImage));
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderInfo(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderInfo(marker, view);
        return view;
    }

   static class MarkerCallback implements Callback {
        private Marker marker;
        private String URL;
        private ImageView userPhoto;

        MarkerCallback(Marker marker, String URL, ImageView userPhoto) {
            this.marker=marker;
            this.URL = URL;
            this.userPhoto = userPhoto;
        }

        @Override
        public void onSuccess() {
            // Updates the marker image
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                Picasso.get()
                        .load(URL)
                        .into(userPhoto);
                marker.showInfoWindow();
            }
        }

        @Override
        public void onError(Exception e) {
            // We can show placeholder image here
        }
    }
}


