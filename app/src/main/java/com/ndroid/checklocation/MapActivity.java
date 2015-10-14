package com.ndroid.checklocation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
//    private LatLng startLoc = new LatLng(7.0722, 125.6131);
    private LatLng endtLoc,startLoc;
    private Marker marker;
    private ArrayList<LatLng> points;
    private String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        points= new ArrayList<LatLng>();
        ArrayList<LocPoitns> locList = (ArrayList<LocPoitns>) CommonConstants.locList.clone();


        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_act)).getMap();
        LocPoitns pointStart = new LocPoitns();
        int size = locList.size();
        Iterator<LocPoitns> iterator;
        iterator = locList.iterator();


        pointStart = locList.get(size - size);
        startLoc = new LatLng(pointStart.getLatitude(), pointStart.getLongitude());


        LocPoitns pointEnd = new LocPoitns();
        pointEnd = locList.get(size - 1);
        endtLoc = new LatLng(pointEnd.getLatitude(), pointEnd.getLongitude());

//        marker = googleMap.addMarker(new MarkerOptions().position(startLoc).title("Start").snippet("Start Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        int i=0;
        while (iterator.hasNext()) {
            LocPoitns p = new LocPoitns();
            p = iterator.next();

            LatLng point = new LatLng(p.getLatitude(), p.getLongitude());


            System.out.println(p.getLatitude() + "  -  " + p.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(startLoc).title("Marker : " + i).snippet("End Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//            googleMap.addPolyline(new PolylineOptions().geodesic(true)
//                    .add(new LatLng(p.getLatitude(), p.getLongitude())));
//            i++;


            // Instantiating the class MarkerOptions to plot marker on the map
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting latitude and longitude of the marker position
            markerOptions.position(point);

            // Setting titile of the infowindow of the marker
            markerOptions.title("Position : "+i );

            // Setting the content of the infowindow of the marker
            markerOptions.snippet("Latitude:"+p.getLatitude()+","+"Longitude:"+p.getLongitude());

            // Instantiating the class PolylineOptions to plot polyline in the map
            PolylineOptions polylineOptions = new PolylineOptions();

            // Setting the color of the polyline
            polylineOptions.color(Color.RED);

            // Setting the width of the polyline
            polylineOptions.width(3);

            // Adding the taped point to the ArrayList
            points.add(point);

            // Setting points of polyline
            polylineOptions.addAll(points);

            // Adding the polyline to the map
            googleMap.addPolyline(polylineOptions);

            // Adding the marker to the map
            googleMap.addMarker(markerOptions);

        }



//        // zoom in the camera to 1st position
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 15));
//
//        // animate the zoom process
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
//        googleMap.addPolyline(new PolylineOptions().geodesic(true)
//                .add(new LatLng(-33.866, 151.195))  // Sydney
//                .add(new LatLng(-18.142, 178.431))  // Fiji
//                .add(new LatLng(21.291, -157.821))  // Hawaii
//                .add(new LatLng(37.423, -122.091))  // Mountain View
//        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        LatLng mapCenter = new LatLng(startLoc.latitude,startLoc.longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));

        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction))
                .position(mapCenter)
                .flat(true)
                .rotation(245));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
    }


}
