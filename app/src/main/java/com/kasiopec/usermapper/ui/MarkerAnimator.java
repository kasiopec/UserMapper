package com.kasiopec.usermapper.ui;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.util.Property;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimator {

    // Taken from Google documentation https://gist.github.com/broady/6314689
    public static void animateMarkerToICS(Marker marker, LatLng finalPosition) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                // Doing interpolation and returning new coords for the animator
                double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
                double lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude;
                return new LatLng(lat, lng);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(1000);
        animator.start();
    }
}


