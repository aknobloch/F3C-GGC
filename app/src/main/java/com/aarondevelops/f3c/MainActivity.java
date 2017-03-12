package com.aarondevelops.f3c;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import charge_points.ChargePoint;
import charge_points.ChargerStation;
import utils.HttpHelper;
import utils.NetworkHelper;

public class MainActivity extends AppCompatActivity
{

    public static final String DEBUG_TAG = "F3CDebug";

    ListView chargerList;
    ChargerListAdapter listAdapter;
    ArrayList<ChargerStation> chargerStations = new ArrayList<>();

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

        // creates and initializes the adapter for the listview
        chargerList = (ListView) findViewById(R.id.displayList);
        listAdapter = new ChargerListAdapter(this, R.layout.list_display_layout,
                R.id.nickname, chargerStations);

        chargerList.setAdapter(listAdapter);

    }

    /***
     * An extension of AsyncTask to poll the ChargePoint.com server and fetch the available
     * stations on the GGC campus. After fetching, the ChargeLocationLoader will initialize
     * the chargerStations list with the stations found.
     */
    class ChargeLocationLoader extends AsyncTask<Void, Void, Void>
    {

        // the URL stations to poll for information
        String[] stationURLs = {
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}",
                "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}"
        };

        /***
         * First clears the current chargerStations. Then, iterates through the stationURLs field,
         * polling each one and parsing the JSON response into POJOs. Using this object,
         * it then creates a ChargerStation and adds them to the MainActivity chargerStations.
         */
        @Override
        protected Void doInBackground(Void... params) {

            chargerStations.clear();

            try
            {
                Gson parser = new Gson();

                for(String stationURL : stationURLs)
                {
                    String response = HttpHelper.downloadUrl(stationURL);
                    ChargePoint stationPoint = parser.fromJson(response, ChargePoint.class);
                    ChargerStation station = new ChargerStation(stationPoint);
                    chargerStations.add(station);
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
