package com.x10host.dhanushpatel.barsafety;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by dhanush on 2/8/15.
 */
public class CheckStatus extends ActionBarActivity  {
    TextView countdownTime;
    boolean onTime = false;
    Button fineButton;
    String time;
    MediaPlayer mPlayer;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkstatus);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getLocation();
        mPlayer = MediaPlayer.create(CheckStatus.this, R.raw.alarm_sound);
        final float volume = (float) (1 - (Math.log(100-50) / Math.log(100)));
        mPlayer.setVolume(volume, volume);
        mPlayer.start();
        mPlayer.setLooping(true);
        countdownTime = (TextView) findViewById(R.id.statusConfirmTime);
        fineButton = (Button) findViewById(R.id.fineButton);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                time = seconds / 60 + ":" + seconds % 60;
                countdownTime.setText("" + time);
            }

            public void onFinish() {

                if (onTime == false) {
                    if(mPlayer.isPlaying()||mPlayer.isLooping())
                    {
                         mPlayer.setLooping(false);
                         mPlayer.stop();
                    }
                    SmsManager sm = SmsManager.getDefault();//GlobalVariables.googleMapsAddress
                    if(GlobalVariables.contactOne+""!=null)
                    {
                        sm.sendTextMessage((String.valueOf(GlobalVariables.contactOne)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
                    }
                    if((GlobalVariables.contactTwo+"")!=null)
                    {
                        sm.sendTextMessage((String.valueOf(GlobalVariables.contactTwo)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
                    }
                    if((GlobalVariables.contactThree+"")!=null)
                    {
                        sm.sendTextMessage((String.valueOf(GlobalVariables.contactThree)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
                    }
                    Toast.makeText(CheckStatus.this, "Time is over. Emergency contacts are being texted.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CheckStatus.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // intent = new Intent(CheckStatus.this,Verify.class);
                }

            }
        }.start();
    }

    public void getLocation()
    {
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
                    GlobalVariables._Location = addresses.get(1).getLocality();
                }catch(IOException e){e.printStackTrace();}
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

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

    public void okaySelected(View view) {

        if (Integer.parseInt(time.replace("0:","")) > 0) {
            Toast.makeText(CheckStatus.this, "Going to verification.", Toast.LENGTH_LONG).show();
            onTime = true;
            Intent intent = new Intent(CheckStatus.this, Verify.class);
            startActivity(intent);
            // Perform action on click
        }
        else
        {
        }

    }

    public void onDestroy() {
        if(mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        super.onDestroy();
    }
    public void onPause() {
        if(mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        super.onPause();
    }
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(CheckStatus.this,"Success.",Toast.LENGTH_SHORT);
            }

        }
    }


}



