package com.aarondevelops.f3c.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aarondevelops.f3c.R;
import com.aarondevelops.f3c.chargelocation.ChargerStation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/***
 * The ChargerListAdapter defines the formatting and handles the display
 * of information for the bound ListView.
 */
public class ChargerListAdapter extends ArrayAdapter<ChargerStation>
{

    private final Activity context;
    private ArrayList<ChargerStation> objects;


    // TODO: Can I specify that this View param must be an instance of my custom layout?
    public ChargerListAdapter(Activity context, int resource, int viewID, ArrayList<ChargerStation> objects) {

        super(context, resource, viewID, objects);

        if(resource != R.layout.list_display_layout)
        {
            throw new IllegalArgumentException();
        }
        this.context = context;
        this.objects = objects;
    }

    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View layoutView = inflater.inflate(R.layout.list_display_layout, null, true);

        return initializeView(layoutView, objects.get(position));
    }

    /***
     * Initializes the custom view with the appropriate values
     * @param rootView - The root view to be modified
     * @param station - The ChargerStation for which the view should display
     * @return - The rootView, with the appropriate fields modified to display the
     * pertinent information
     */
    private View initializeView(View rootView, ChargerStation station)
    {
        TextView nicknameLabel = (TextView) rootView.findViewById(R.id.nickname);
        TextView availabilityLabel = (TextView) rootView.findViewById(R.id.availability);
        TextView locationLabel = (TextView) rootView.findViewById(R.id.location);
        TextView timestampLabel = (TextView) rootView.findViewById(R.id.timestamp);

        nicknameLabel.setText(station.getNickname());
        availabilityLabel.setText(station.getAvailabilityString());
        locationLabel.setText(station.getLocationFormatted());
        timestampLabel.setText(station.getTimeAccessed());

        int availabilityColor = getAvailabilityColor(context, station.getAvailabilityNumber());
        availabilityLabel.setTextColor(availabilityColor);

        return rootView;
    }

    /***
     * Finds the int-represented color to display, based on the number of available chargers.
     * @param context - The context of this application, to determine the color.
     * @param numberAvailable - The number of available chargers for this station.
     * @return - An integer representation of the color to display for this station.
     */
    private int getAvailabilityColor(Activity context, int numberAvailable)
    {
        if(numberAvailable > 0)
        {
            return ContextCompat.getColor(context, android.R.color.holo_green_dark);
        }
        else
        {
            return ContextCompat.getColor(context, android.R.color.black);
        }
    }
}
