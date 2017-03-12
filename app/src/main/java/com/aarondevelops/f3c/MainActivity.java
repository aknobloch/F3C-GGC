package com.aarondevelops.f3c;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import charge_points.ChargePoint;
import utils.HttpHelper;
import utils.NetworkHelper;

public class MainActivity extends AppCompatActivity
{

    static final String DEBUG_TAG = "F3CDebug";

    ListView chargerList;
    ChargerListAdapter listAdapter;
    ArrayList<ChargePoint> chargerLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fetchData();
            }
        });

        chargerList = (ListView) findViewById(R.id.displayList);
        listAdapter = new ChargerListAdapter(this, R.layout.list_display_layout,
                R.id.nickname, chargerLocations);

        chargerList.setAdapter(listAdapter);

    }

    class ChargeLocationLoader extends AsyncTask<Void, Void, Void>
    {

        String[] stations = {
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}"
        };

        @Override
        protected Void doInBackground(Void... params) {

            chargerLocations.clear();

            try
            {
                Gson parser = new Gson();

                for(String stationString : stations)
                {
                    String response = HttpHelper.downloadUrl(stationString);
                    ChargePoint stationPoint = parser.fromJson(response, ChargePoint.class);
                    chargerLocations.add(stationPoint);
                }

            } catch (IOException e)
            {
                Toast errorToast = Toast.makeText(
                        getApplicationContext(), "Error Connecting to Database.", Toast.LENGTH_SHORT);
                errorToast.show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.d(DEBUG_TAG, "Done polling. Size: " + chargerLocations.size());
            listAdapter.notifyDataSetChanged();
        }
    }

    public void fetchData()
    {
        // If there isn't a network available, toast error.
        if( ! NetworkHelper.hasNetworkAccess(this))
        {
            Toast errorToast = Toast.makeText(
                    this,
                    "Check network connection and try again.",
                    Toast.LENGTH_SHORT);
            errorToast.show();
        }

        new ChargeLocationLoader().execute();
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

}
