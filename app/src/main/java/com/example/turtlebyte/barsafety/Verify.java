package com.example.turtlebyte.barsafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dhanush on 2/8/15.
 */
public class Verify extends Activity {


   EditText numberAnswer;
    Button numberInputted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);
        numberAnswer = (EditText)findViewById(R.id.numberHere);
        numberInputted = (Button)findViewById(R.id.numberInputted);

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
        if(numberAnswer.getText().toString().equals("9"))
        {
            Toast.makeText(this,"Verification complete. Current timer finished.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Verify.this,MainActivity.class);
            startActivity(intent);

        }
    }



}




