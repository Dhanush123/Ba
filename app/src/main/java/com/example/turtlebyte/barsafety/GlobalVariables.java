package com.example.turtlebyte.barsafety;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by  on 2/7/15.
 */
public class GlobalVariables extends Application {

     static int timeLimit;
    static long contactOne;
    static long contactTwo;
    static long contactThree;
    static double mLatitudeText;
    static double mLongitudeText;
    static String mAddress;
    static String _Location;
    // String googleMapsAddress=getLocation();

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeChosen) {
       timeLimit = timeChosen;
    }
    public void getLocation()
    {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if(null!=locations && null!=providerList && providerList.size()>0){
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder((GlobalVariables)this.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                     _Location = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}