package com.x10host.dhanushpatel.barsafety;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dhanush on 2/7/15.
 */
public class Countdown extends ActionBarActivity {
    TextView countdownTime;
    CountDownTimer timer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        countdownTime = (TextView)findViewById(R.id.countdownTime);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasklist = am.getRunningTasks(10); // Number of tasks you want to get

        if(!tasklist.isEmpty())
        {
            int nSize = tasklist.size();
            for(int i = 0; i >= nSize;  i++)
            {
                ActivityManager.RunningTaskInfo taskinfo = tasklist.get(i);
                if(taskinfo.topActivity.getPackageName().equals("com.example.turtlebyte.barsafety"))
                {
                    am.moveTaskToFront(taskinfo.id, 0);
                }
            }
        }

        int secPerMin = 60;
        int millisecPerSec = 1000;
        timer = new CountDownTimer((GlobalVariables.timeLimit)*secPerMin*millisecPerSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                String time = seconds / 60 + ":" + seconds % 60;
                countdownTime.setText("" + time);
            }

            @Override
            public void onFinish() {
                //  countdownTime.setText("Time is over.");
                Toast.makeText(Countdown.this,"Time is over.",Toast.LENGTH_SHORT).show();

                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasklist =am.getRunningTasks(10); // Number of tasks you want to get
                if(!tasklist.isEmpty())
                {
                    int nSize = tasklist.size();
                    for(int i = 0; i >= nSize;  i++)
                    {
                        ActivityManager.RunningTaskInfo taskinfo = tasklist.get(i);
                        if(taskinfo.topActivity.getPackageName().equals("com.example.turtlebyte.barsafety"))
                        {
                            am.moveTaskToFront(taskinfo.id, 0);
                        }
                    }
                }
                //try { Thread.sleep(3000); } catch (Exception ex) {}
                Intent intent = new Intent(Countdown.this,CheckStatus.class);
                startActivity(intent);
            }
        };

        timer.start();
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
}