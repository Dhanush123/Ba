package com.x10host.dhanushpatel.barsafety;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by dhanush on 2/8/15.
 */
public class Verify extends ActionBarActivity {

    EditText numberAnswer;
    Button numberInputted;
    TextView equationShow;
    int tries;
    int answer;
    CountDownTimer timer;
    TextView countdownTime2;
    TextView timeHere;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        numberAnswer = (EditText)findViewById(R.id.numberHere);
        numberInputted = (Button)findViewById(R.id.numberInputted);
        equationShow = (TextView)findViewById(R.id.mathEquation);
        countdownTime2 = (TextView)findViewById(R.id.countdownTime2);
        timeHere = (TextView)findViewById(R.id.timeleft2);
        randomQuestionGenerator();
        getLocation();
        int secPerMin = 60;
        timer = new CountDownTimer((GlobalVariables.timeLimit)*secPerMin*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                String time = seconds / 60 + ":" + seconds % 60;
                countdownTime2.setText("" + time);
            }

            @Override
            public void onFinish() {
                text();
                Toast.makeText(Verify.this,"Time is over. Emergency contacts are being texted.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Verify.this,CheckStatus.class);
                startActivity(intent);
            }
        };
        timer.start();
    }

    public void randomQuestionGenerator() {
        int firstN = (int) (Math.random() * (101 - 0)) + 0; //you can just do new Random().nextInt(101) lololol
        int secondN = (int) (Math.random() * (101 - 0)) + 0;

        String addOrSubtract="";
        Random r = new Random();
        boolean choose = r.nextBoolean();
        Log.i("boolean is:",""+choose);
        if (choose == true) {
            addOrSubtract = "+";
            answer = firstN + secondN;
        }
            if (choose == false) {
                addOrSubtract = "-";
                if(firstN<secondN)
                {
                    answer = secondN - firstN;
                }
                else {
                    answer = firstN - secondN;
                }
            }
        Log.i("Answer set: ",""+answer);
        if(choose==false && firstN<secondN)
        {
            Log.i("Went inside IF,","");
            equationShow.setText(secondN + " " + addOrSubtract + " " + firstN + " = ?");
        }
        else {
            Log.i("Went inside ELSE","");
            equationShow.setText(firstN + " " + addOrSubtract + " " + secondN + " = ?");
        }
    }

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

    public void numberInputted(View view) {
        Log.i("Answer inputted: ",numberAnswer.getText().toString());
        if(numberAnswer.getText().toString().equals(String.valueOf(answer)))
        {
            Toast.makeText(this,"Verification complete. Current timer finished.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Verify.this,MainActivity.class);
            startActivity(intent);

        }
        else
        {
            Log.i("Should be 0 Tries: ",""+tries);
            tries++;
            if(tries==1) {
                Log.i("Should be 1 Tries: ",""+tries);
                Toast.makeText(this, "Incorrect answer. Try again. 2 tries left.", Toast.LENGTH_LONG).show();
            }
            if(tries==2) {
                Log.i("Should be 2 Tries: ",""+tries);
                Toast.makeText(this, "Incorrect answer. Try again. 1 tries left.", Toast.LENGTH_LONG).show();
            }
            if(tries==3) {
                Log.i("Should be 3 Tries: ",""+tries);
                text();
                Toast.makeText(Verify.this, "Too many tries. Emergency contacts are being texted.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Verify.this, MainActivity.class);
                startActivity(intent);
            }


        }
    }
    public void text() {
        SmsManager sm = SmsManager.getDefault();//GlobalVariables.googleMapsAddress

        String msg = "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location;
        if(GlobalVariables.contactOne+""!=null) {
            sm.sendTextMessage((String.valueOf(GlobalVariables.contactOne)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
        }
        if((GlobalVariables.contactTwo+"")!=null) {
            sm.sendTextMessage((String.valueOf(GlobalVariables.contactTwo)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
        }
        if((GlobalVariables.contactThree+"")!=null) {
            sm.sendTextMessage((String.valueOf(GlobalVariables.contactThree)), null, "I am drunk and in need of assistance. I am at: "+GlobalVariables._Location, null, null);
        }
    }

    public void onPause() {
        Log.i("Timer got cancelled"," @ onPause");
        timer.cancel();
        super.onPause();
    }

    public void onDestroy() {
        timer.cancel();
        super.onPause();
    }
}