package com.example.turtlebyte.barsafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

public class MainActivity extends Activity {
    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    private int timeSelected;
    Spinner spinner;
    private MobileServiceClient mClient;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mClient = new MobileServiceClient(
                    "https://barsafety123.azure-mobile.net/",
                    "RqdaUIglMwkUNyLtHwJMrgVMNakSqM30",
                    this
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        spinner = (Spinner) findViewById(R.id.timeSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void timeSelected(View view) {
        GlobalVariables.timeLimit=Integer.parseInt(spinner.getSelectedItem().toString());
        Intent intent = new Intent(this,ChooseContacts.class);
        startActivity(intent);
/**
        int   MAX_PICK_CONTACT = 10;
        //GlobalVariables mApp = ((GlobalVariables)getApplicationContext());
        timeSelected = Integer.valueOf(spinner.getSelectedItem().toString());
        GlobalVariables.timeLimit=timeSelected;
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.putExtra("additional", "phone-multi");
        pickContactIntent.putExtra("maxRecipientCount", MAX_PICK_CONTACT);
        pickContactIntent.putExtra("FromMMS", true);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
       // ((GlobalVariables) this.getApplicationContext()).setTimeLimit(timeSelected);
        // Do something in response to button click

 */
    }

}
