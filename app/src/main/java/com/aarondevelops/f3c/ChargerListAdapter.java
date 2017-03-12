package com.aarondevelops.f3c;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import charge_points.ChargePoint;
import charge_points.StationList;
import charge_points.Summary;

/**
 * Created by Aaron K on 3/11/2017.
 */

public class ChargerListAdapter extends ArrayAdapter<ChargePoint>
{

    private final Activity context;
    private List<ChargePoint> objects;

    public ChargerListAdapter(Activity context, int resource, int viewID, List<ChargePoint> objects) {

        super(context, resource, viewID, objects);
        this.context = context;
        this.objects = objects;
    }

    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View layoutView = inflater.inflate(R.layout.list_display_layout, null, true);

        TextView nicknameLabel = (TextView) layoutView.findViewById(R.id.nickname);
        TextView availabilityLabel = (TextView) layoutView.findViewById(R.id.availability);
        TextView locationLabel = (TextView) layoutView.findViewById(R.id.location);
        TextView timestampLabel = (TextView) layoutView.findViewById(R.id.timestamp);

        ChargePoint currentPoint = objects.get(position);

        // get relevant information
        String nickname, availability, location, timestamp;

        nickname = getLocationNickname(currentPoint);
        availability = getStationAvailability(currentPoint);
        location = getLocation(currentPoint);
        timestamp = currentPoint.getStationList().getTime();

        nicknameLabel.setText(nickname);
        availabilityLabel.setText(availability);
        locationLabel.setText(location);
        timestampLabel.setText(timestamp);

        setAvailabilityColor(availability, availabilityLabel);

        return layoutView;
    }

    private void setAvailabilityColor(String availability, TextView availabilityLabel)
    {
        int numberAvailable = Integer.parseInt(
                availability.substring(0, availability.indexOf(" ")));
        if(numberAvailable > 0)
        {
            availabilityLabel.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }
        else
        {
            availabilityLabel.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    private String getLocationNickname(ChargePoint currentPoint)
    {
        long  deviceNumber = currentPoint.getStationList().getSummaries().get(0).getDeviceId();

        if(deviceNumber == 122167)
        {
            return "B Building";
        }
        else if(deviceNumber == 122169)
        {
            return "Student Housing";
        }
        else if(deviceNumber == 122165)
        {
            return "Main Deck";
        }
        else if(deviceNumber == 122219)
        {
            return "Faculty Offices";
        }
        else
        {
            return "GGC Parking Lot";
        }
    }

    /***
     * Gets the location names of the charging station by grabbing the first index
     * in the Summary list and iterating through its names.
     * @param chargePoint - The charge point to find name for.
     * @return A string containing the names of the station, seperated by a forward slash
     */
    private String getLocation(ChargePoint chargePoint)
    {
        StationList stationList = chargePoint.getStationList();
        List<Summary> allChargingPoints = stationList.getSummaries();

        Summary chargingPoint = allChargingPoints.get(0);
        List<String> names = chargingPoint.getStationName();

        String concatonatedNames = names.get(0);

        for(int i = 1; i < names.size(); i++)
        {
            concatonatedNames += (" / " + names.get(i));
        }

        return concatonatedNames;
    }

    /***
     * Gets the total number of available charging points for the station
     * @param chargePoint - The charge point to find availability for
     * @return
     */
    private String getStationAvailability(ChargePoint chargePoint)
    {
        StationList stationList = chargePoint.getStationList();
        List<Summary> allChargingPoints = stationList.getSummaries();

        int available = 0;

        for(Summary chargingStation : allChargingPoints)
        {
            available += chargingStation.getPortCount().getAvailable();
        }

        return available + " Available";
    }

}
