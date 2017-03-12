package charge_points;

import android.util.Log;

import com.aarondevelops.f3c.MainActivity;

import java.util.List;

/**
 * Created by Aaron K on 3/12/2017.
 */

public class ChargerStation
{
    private ChargePoint rootPoint;
    private String nickname;
    private int availability;
    private String locationFormatted;
    private String timeAccessed;
    private String description;

    public ChargerStation(ChargePoint point)
    {
        this.rootPoint = point;
        setLocationNickname();
        setStationAvailability();
        setLocation();
        this.timeAccessed = point.getStationList().getTime();
        setDescription();
    }

    public ChargePoint getChargePoint()
    {
        return this.rootPoint;
    }

    public String getTimeAccessed() {
        return timeAccessed;
    }

    public int getAvailabilityNumber() {
        return availability;
    }

    public String getAvailabilityLabel()
    {
        return availability + " Available";
    }

    public String getLocationFormatted() {
        return locationFormatted;
    }

    public String getNickname() {
        return nickname;
    }

    private void setDescription()
    {
        Summary stationSummary = rootPoint.getStationList().getSummaries().get(0);
        this.description = stationSummary.getDescription();
    }

    private void setLocation()
    {
        StationList stationList = rootPoint.getStationList();
        List<Summary> allChargingPoints = stationList.getSummaries();

        Summary chargingPoint = allChargingPoints.get(0);
        List<String> names = chargingPoint.getStationName();

        String concatonatedNames = names.get(0);

        for(int i = 1; i < names.size(); i++)
        {
            concatonatedNames += (" / " + names.get(i));
        }

        this.locationFormatted = concatonatedNames;
    }

    private void setLocationNickname()
    {
        long  deviceNumber = rootPoint.getStationList().getSummaries().get(0).getDeviceId();

        if(deviceNumber == 122167)
        {
            this.nickname = "B Building";
        }
        else if(deviceNumber == 122169)
        {
            this.nickname = "Student Housing";
        }
        else if(deviceNumber == 122165)
        {
            this.nickname = "Main Deck";
        }
        else if(deviceNumber == 122219)
        {
            this.nickname = "Faculty Offices";
        }
        else
        {
            this.nickname = "GGC Parking";
        }
    }

    private void setStationAvailability()
    {
        StationList stationList = rootPoint.getStationList();
        List<Summary> allChargingPoints = stationList.getSummaries();

        int available = 0;

        for(Summary chargingStation : allChargingPoints)
        {
            available += chargingStation.getPortCount().getAvailable();
        }

        this.availability = available;
    }
}
