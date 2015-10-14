package com.ndroid.checklocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapActivityFragment extends AppCompatActivity {

    GoogleMap googleMap;
    private LatLng startLoc = new LatLng(7.0722, 125.6131);
    private LatLng endtLoc;
    private Marker marker;

    //    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();


        LocPoitns point = new LocPoitns();
        point = CommonConstants.locList.get(0);
        startLoc = new LatLng(point.getLatitude(), point.getLongitude());


        point = CommonConstants.locList.get(CommonConstants.locList.size() - 1);
        endtLoc = new LatLng(point.getLatitude(), point.getLongitude());

        marker = googleMap.addMarker(new MarkerOptions().position(startLoc).title("Start").snippet("Start Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        googleMap.addMarker(new MarkerOptions().position(startLoc).title("Final").snippet("End Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        ;

        // zoom in the camera to 1st position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 15));

        // animate the zoom process
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        return view;
    }
}
