package com.x10host.dhanushpatel.barsafety;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
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
    SharedPreferences preferences = getSharedPreferences("MyContacts", Context.MODE_PRIVATE);

    // String googleMapsAddress=getLocation();

    /**
    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeChosen) {
       timeLimit = timeChosen;
    }
     **/
    public void getLocation() {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geo = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses = geo.getFromLocation(latitude, longitude, 5);
                    for(int i = 0;i<addresses.size();i++){
                        Log.d("DEBUG", "Address " + i + " - " + addresses.get(i));
                    }
                    _Location = addresses.get(1).getLocality();
                }catch(IOException e){e.printStackTrace();}
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }
}