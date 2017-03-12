package com.aarondevelops.f3c;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import charge_points.ChargerStation;

/**
 * Created by Aaron K on 3/11/2017.
 */

public class ChargerListAdapter extends ArrayAdapter<ChargerStation>
{

    private final Activity context;
    private List<ChargerStation> objects;


    public ChargerListAdapter(Activity context, int resource, int viewID, List<ChargerStation> objects) {

        super(context, resource, viewID, objects);
        this.context = context;
        this.objects = objects;
    }

    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View layoutView = inflater.inflate(R.layout.list_display_layout, null, true);

        return initializeView(layoutView, objects.get(position));
    }

    // TODO: Can I specify that this View param must be an instance of my custom layout?
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
