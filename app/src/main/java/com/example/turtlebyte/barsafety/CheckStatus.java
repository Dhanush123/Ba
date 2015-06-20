package com.example.turtlebyte.barsafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by dhanush on 2/8/15.
 */
public class CheckStatus extends Activity  {


    TextView countdownTime;
    boolean onTime = false;
    Button fineButton;
    String time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkstatus);

        countdownTime = (TextView) findViewById(R.id.statusConfirmTime);
        fineButton = (Button) findViewById(R.id.fineButton);
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                time = seconds / 60 + ":" + seconds % 60;
                countdownTime.setText("" + time);
            }

            public void onFinish() {

                if (onTime == false) {

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
                    Toast.makeText(CheckStatus.this, "Time is over. Emergency contact is being texted.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CheckStatus.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // intent = new Intent(CheckStatus.this,Verify.class);
                }

            }
        }.start();
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



