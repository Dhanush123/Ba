package com.example.turtlebyte.barsafety;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

/**
 * Created by chapatel on 2/15/15.
 */
public class ChooseContacts extends Activity{
    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    private int timeSelected;
    Spinner spinner;
    private MobileServiceClient mClient;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contacts);




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
    public void firstContactSelected(View view) {

 int   MAX_PICK_CONTACT = 10;
 //GlobalVariables mApp = ((GlobalVariables)getApplicationContext());

 Intent pickContactIntent1 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
 pickContactIntent1.putExtra("additional", "phone-multi");
 pickContactIntent1.putExtra("maxRecipientCount", MAX_PICK_CONTACT);
 pickContactIntent1.putExtra("FromMMS", true);
 pickContactIntent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
 startActivityForResult(pickContactIntent1, PICK_CONTACT_REQUEST);


    }
    public void secondContactSelected(View view) {

        int   MAX_PICK_CONTACT = 10;

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.putExtra("additional", "phone-multi");
        pickContactIntent.putExtra("maxRecipientCount", MAX_PICK_CONTACT);
        pickContactIntent.putExtra("FromMMS", true);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    public void thirdContactSelected(View view) {

        int   MAX_PICK_CONTACT = 10;

        Intent pickContactIntent3 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent3.putExtra("additional", "phone-multi");
        pickContactIntent3.putExtra("maxRecipientCount", MAX_PICK_CONTACT);
        pickContactIntent3.putExtra("FromMMS", true);
        pickContactIntent3.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent3, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                if((GlobalVariables.contactOne==0))
                {
                    GlobalVariables.contactOne = Long.valueOf(number.replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
                }
                else if(GlobalVariables.contactTwo==0&&GlobalVariables.contactOne!=0)
                {
                    GlobalVariables.contactTwo = Long.valueOf(number.replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
                }
               else if(GlobalVariables.contactThree==0&&GlobalVariables.contactOne!=0&&GlobalVariables.contactTwo!=0)
                {
                    GlobalVariables.contactThree = Long.valueOf(number.replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
                }

                Log.i("Contact:", number);
                // Do something with the phone number...
                Item item = new Item();
                item.Txt = GlobalVariables.contactOne;
                item.time = GlobalVariables.timeLimit;
                // item.id = "TestID";
                Log.i("time", GlobalVariables.timeLimit+"");
                /**
                mClient.getTable(Item.class).insert(item, new TableOperationCallback<Item>() {
                    public void onCompleted(Item entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            // Insert succeeded
                        } else {
                            // Insert failed
                        }
                    }
                });
                 */

                if(GlobalVariables.contactOne!=0&&GlobalVariables.contactTwo!=0&&GlobalVariables.contactThree!=0)
                {
                    Intent intent = new Intent(ChooseContacts.this, Countdown.class);
                    startActivity(intent);
                }

            }
        }
    }
    public class Item {
        public int time;
        public long Txt;
        public String id;
    }
}

