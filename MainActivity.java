package de.hfu.digisign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import AniketaSharma.rohit.R;


public class MainActivity extends Activity {
    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user clicks the Send the File button
     */
    public void sender(View view) {
        Intent intent = new Intent(this, SignSendFileActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Verify the File button
     */
     public void receiver(View view) {
        Intent intent = new Intent(this, VerifyFileActivity.class);
        startActivity(intent);
     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyFile(View view) {
    }
}
