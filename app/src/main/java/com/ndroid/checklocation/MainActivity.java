package com.ndroid.checklocation;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{


    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private ArrayList<LocPoitns> waypoints=null;

    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 5;


    private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;
    private TextView showAllLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLocation = (TextView) findViewById(R.id.lblLocation);
        btnShowLocation = (Button) findViewById(R.id.buttonShowLocation);
        btnStartLocationUpdates = (Button) findViewById(R.id.buttonLocationUpdates);

        showAllLocations= (TextView)findViewById(R.id.showLocationArray);

        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocation();
            }
        });

        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodLocationUpdates();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if(mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()) {
          //  mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longtitude = mLastLocation.getLongitude();

            lblLocation.setText(latitude + ", " + longtitude);

            if(mRequestLocationUpdates) {
                showAllLocations.append("\n"+latitude + ", " + longtitude);

                if(waypoints == null) {
                    waypoints = new ArrayList<LocPoitns>();
                }


                LocPoitns locObj = new LocPoitns();
                locObj.setLatitude(latitude);
                locObj.setLongitude(longtitude);
                waypoints.add(locObj);
                Log.i(TAG,"1st : " +latitude+" - "+longtitude);
            }

        } else {
            lblLocation.setText("Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    private void togglePeriodLocationUpdates() {
        if(!mRequestLocationUpdates) {

            if(waypoints!=null){
//                waypoints.clear();
            }
            btnStartLocationUpdates.setText(getString(R.string.btn_stop_location_updates));
            mRequestLocationUpdates = true;

            startLocationUpdates();
        } else {
            btnStartLocationUpdates.setText(getString(R.string.btn_start_location_updates));

            mRequestLocationUpdates = false;
            stopLocationUpdates();
            if(waypoints!=null){

                Iterator<LocPoitns> it = waypoints.iterator();
                while (it.hasNext()){
                    LocPoitns pt = new LocPoitns();
                    pt = it.next();
                    Log.i(TAG,pt.getLatitude() + " >> " + pt.getLongitude());
                }

                if(CommonConstants.locList==null){
                    CommonConstants.locList = new ArrayList<LocPoitns>();
                }else
                    CommonConstants.locList.clear();

                CommonConstants.locList =(ArrayList<LocPoitns>)waypoints.clone();

                Intent intent =new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("wayPoints",waypoints);
                startActivity(intent);
                waypoints.clear();
                showAllLocations.setText("");

            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();

        if(mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }
}
