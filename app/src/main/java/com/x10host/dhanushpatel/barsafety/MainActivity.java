package com.x10host.dhanushpatel.barsafety;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener{
    private int timeSelected;
    NumberPicker hours, minutes;
    private Toolbar toolbar;
    RadialPickerLayout rpl;
    private int timerMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button timeButton = (Button) findViewById(R.id.time_button);
        Button okayButton = (Button) findViewById(R.id.timeOkayButton);

        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar moment = Calendar.getInstance();
                TimePickerDialog timepicker = TimePickerDialog.newInstance(
                        MainActivity.this,
                        moment.get(Calendar.HOUR_OF_DAY),
                        moment.get(Calendar.MINUTE),
                        false
                );
                timepicker.setThemeDark(true);
                timepicker.setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("DEBUG", "the lil shit canceled the dialog >:(");
                    }
                });
                timepicker.show(getFragmentManager(), "Time Picker");
            }
        });        
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            //  Toast.makeText(this, "GPS is enabled on your device", Toast.LENGTH_SHORT).show();
        }
    }//
 
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "You picked the following time: "+hourString+"h"+minuteString;
        Log.d("DEBUG", time);
        Toast.makeText(this, time, Toast.LENGTH_LONG);
        int totalFinishTimeMinutes = (hourOfDay * 60) + minute;

        Time timeTime = new Time();
        timeTime.setToNow();
        Log.i("Current time: ", "" + timeTime.format("%k:%M:%S"));
        String currentTimeFormatted = timeTime.format("%k:%M:%S");
        int currentHour;
        if(currentTimeFormatted.substring(0,1).equals(" "))
        {
        currentHour = Integer.parseInt(currentTimeFormatted.substring(1, 2));
        }
        else {
              currentHour = Integer.parseInt(currentTimeFormatted.substring(0, 2));
        }
        Log.i("Current hour: ",""+currentHour);
        int currentMinutes = Integer.parseInt(currentTimeFormatted.substring(3, 5));
        Log.i("Current minutes: ",""+currentMinutes);
        int currentTotalMinutes = ((currentHour*60)+currentMinutes);
        Log.i("Current total minutes: ", "" + currentTotalMinutes);
       timerMinutes = Math.abs(totalFinishTimeMinutes-currentTotalMinutes);
        Log.i("Timer minutes: ", "" + timerMinutes);
    //    timeSelected(view, timerMinutes);
        // totalMinutesSet = (hourOfDay * 60) + minute;
    }


    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled on your device. Please enable it.")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void timeSelected(View view) {
        //int totalMinutes = hours.getValue() * 60 + minutes.getValue();
        if (timerMinutes == 0) {
            Toast.makeText(this,"Timer has not been set.", Toast.LENGTH_LONG);
        }
        else
        {
        Toast.makeText(this, "Timer: " + timerMinutes + " minutes", Toast.LENGTH_SHORT).show();

        GlobalVariables.timeLimit = timerMinutes;

        SharedPreferences namePref = getSharedPreferences("MyContacts", Context.MODE_PRIVATE);
        String check = namePref.getString("contactsSetStatus", "contactsNotSet");
        if (check.equals("contactsSet")) {
            Intent intent = new Intent(this, Countdown.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ChooseContacts.class);
            startActivity(intent);
        }
      }
    }
}