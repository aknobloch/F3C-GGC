package com.aarondevelops.f3c.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aarondevelops.f3c.R;
import com.aarondevelops.f3c.chargelocation.ChargePoint;
import com.aarondevelops.f3c.chargelocation.ChargerStation;
import com.aarondevelops.f3c.utils.ChargerListAdapter;
import com.aarondevelops.f3c.utils.HttpHelper;
import com.aarondevelops.f3c.utils.MessageHelper;
import com.aarondevelops.f3c.utils.NetworkHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// TODO: Package name and conventions?
public class MainActivity extends AppCompatActivity
{
    public static final String DEBUG_TAG = "F3CDebug";

    private ListView chargerList;
    private ChargerListAdapter listAdapter;
    private ArrayList<ChargerStation> chargerStations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // prepare the speaker for the list
        MessageHelper.initializeSpeaker(this);

        // define FAB functionality
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

        // creates listener for the list
        defineListAction();

        // do first fetch
        fetchData();
    }

    /***
     * Fetches data from the server. If network access cannot be established, displays a
     * toast alerting the user and then returns.
     */
    public void fetchData()
    {
        // If there isn't a network available, toast error.
        if( ! NetworkHelper.hasNetworkAccess(this))
        {
            MessageHelper.toastAlert(this, "Check network connection and try again.");
            return;
        }

        new ChargeLocationLoader().execute();
    }

    /***
     * Sets the list action for the listview
     */
    private void defineListAction()
    {
        chargerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ChargerStation stationSelected = chargerStations.get(position);
                outputInformation(view, stationSelected);
            }

            private void outputInformation(View view, ChargerStation stationSelected)
            {
                String longMessage = "There are " + stationSelected.getAvailabilityNumber() +
                        " stations available at the " + stationSelected.getNickname() +
                        " location. You can find this charger " + stationSelected.getDescription();

                String shortMessage = stationSelected.getAvailabilityNumber() + " available. " +
                        stationSelected.getDescription();

                MessageHelper.snackbarAlert(view, shortMessage);
                MessageHelper.speakMessage(getApplicationContext(), longMessage);
            }
        });
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

            MessageHelper.snackbarAlert(getCurrentFocus(), "Made for ITEC 4550 by Aaron K on March 12, 2017");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * An extension of AsyncTask to poll the ChargePoint.com server and fetch the available
     * stations on the GGC campus. After fetching, the ChargeLocationLoader will initialize
     * the chargerStations list with the stations found.
     */
    class ChargeLocationLoader extends AsyncTask<Void, Void, Void>
    {

        // the URL stations to poll for information
        // TODO: Why do I not have to handle redirects?
        String[] stationURLs = {
                "https://goo.gl/lp9Cpq",
                "https://goo.gl/mFQA6z",
                "https://goo.gl/xLHbtZ",
                "https://goo.gl/81r365"
        };

        /***
         * Attempts to populate the chargerStations list by fetching data from the stationURLs
         */
        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<ChargerStation> updatedList = getChargeStationList();

            if(updatedList.size() != 0)
            {
                chargerStations.clear();
                chargerStations.addAll(updatedList);
                Collections.sort(chargerStations);
            }

            return null;
        }

        /***
         * Gets an updated charge station list. If an error occurs while trying to poll the
         * database, an empty list will be returned and a toast will display.
         *
         * @return - A list of all the stations found, or an empty list if an error occurred or no
         * stations were found.
         */
        private ArrayList<ChargerStation> getChargeStationList()
        {
            ArrayList<ChargerStation> newList = new ArrayList<>();

            try
            {
                for(String stationURL : stationURLs)
                {
                    ChargerStation station = getStation(stationURL);
                    newList.add(station);
                }

            } catch (IOException e)
            {
                MessageHelper.toastAlert(getApplicationContext(), "Error Connecting to Database.");

                // clear the list just in case of partial success
                newList.clear();
                return newList;
            }

            return newList;
        }

        /***
         * Gets the station associated with the given URL
         * @param stationURL - The URL to poll for charger stations
         * @return - The ChargerStation representing the JSON response from the server.
         * @throws IOException - If the server does not respond.
         */
        private ChargerStation getStation(String stationURL) throws IOException
        {
            Gson parser = new Gson();
            String response = HttpHelper.downloadUrl(stationURL);
            ChargePoint stationPoint = parser.fromJson(response, ChargePoint.class);
            return new ChargerStation(stationPoint);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            listAdapter.notifyDataSetChanged();
        }
    }
}
