package com.meetfriend.app.ui.fragments.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetfriend.app.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class MyLocationFragmet extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Context mContext;
    private GoogleMap mMap;
    private LocationManager locationManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        callPerMission();
    }

    private void callPerMission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED || checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 111);
            else {
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                if (checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("keshav", "Gps already enabled");
                    enableLoc();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                }
            }
        } else {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("keshav", "Gps already enabled");
                enableLoc();
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            }
        }
    }

    private void enableLoc() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
//                            status.startResolutionForResult(getActivity(), 222);
                            startIntentSenderForResult(status.getResolution().getIntentSender(), 222, null, 0, 0, 0, null);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        buildGoogleApiClient();
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasAllPermissionsGranted(grantResults)) {
            if (checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                return;
            }
            Log.e("location request", "reached");
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("keshav", "Gps already enabled");
                enableLoc();
            } else {

                buildGoogleApiClient();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            }
        } else {
            buildGoogleApiClient();
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,MyLocationFragmet.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        getaddress(new LatLng(location.getLatitude(), location.getLongitude()));
        //stop location updates
        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    private void getaddress(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String city = addresses.get(0).getAddressLine(0);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(city);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mCurrLocationMarker.showInfoWindow();
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("keshav", "Gps already enabled");
            enableLoc();
        } else {

            buildGoogleApiClient();
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("keshav", "Gps already enabled");
            enableLoc();
        } else {

            buildGoogleApiClient();
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
