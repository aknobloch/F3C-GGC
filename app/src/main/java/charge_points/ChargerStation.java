package charge_points;

import android.util.Log;

import com.aarondevelops.f3c.MainActivity;

import java.util.List;

/**
 * Created by Aaron K on 3/12/2017.
 */

/***
 * The ChargerStation is an encapsulation of relevant information
 * for a given ChargePoint. It contains a human-readable nickname and location
 * as well as other pertinent information for the charging station.
 */
public class ChargerStation implements Comparable<ChargerStation>
{
    private ChargePoint rootPoint;
    private String nickname;
    private int availability;
    private String locationFormatted;
    private String timeAccessed;
    private String description;

    /***
     * Creates a ChargerStation to represent the relevant information for
     * the given ChargePoint.
     * @param point - The point that should be encapsulated by this ChargerStation
     */
    public ChargerStation(ChargePoint point)
    {
        this.rootPoint = point;

        setLocationNickname();
        setStationAvailability();
        setLocation();
        this.timeAccessed = point.getStationList().getTime();
        setDescription();
    }

    public String getDescription()
    {
        return this.description;
    }

    public ChargePoint getChargePoint()
    {
        return this.rootPoint;
    }

    public String getTimeAccessed() {
        return timeAccessed;
    }

    /***
     * The numerical availability of cars at this location.
     */
    public int getAvailabilityNumber() {
        return availability;
    }

    /***
     * The literal representation of this availability, in the format
     * of "<Number Available> Available"
     */
    public String getAvailabilityString()
    {
        return availability + " Available";
    }

    public String getLocationFormatted() {
        return locationFormatted;
    }

    public String getNickname() {
        return nickname;
    }

    /***
     * Sets the description by traversing through the ChargePoint associated
     * with this ChargerStation
     */
    private void setDescription()
    {
        Summary stationSummary = rootPoint.getStationList().getSummaries().get(0);
        this.description = stationSummary.getDescription();
    }

    /***
     * Sets the location by traversing through the first Summary associated
     * with the root ChargePoint and concatenating all of the names into a single
     * String, seperated by forward slashes.
     */
    private void setLocation()
    {
        StationList stationList = rootPoint.getStationList();
        List<Summary> allChargingPoints = stationList.getSummaries();

        Summary chargingPoint = allChargingPoints.get(0);
        List<String> names = chargingPoint.getStationName();

        String concatenatedNames = names.get(0);

        for(int i = 1; i < names.size(); i++)
        {
            concatenatedNames += (" / " + names.get(i));
        }

        this.locationFormatted = concatenatedNames;
    }

    /***
     * Sets the location nickname by analyzing the device number associated with
     * the root ChargePoint and assigning a human-readable nickname.
     */
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

    /***
     * Sets the availability by finding all of the available stations associated
     * with the root ChargePoint object.
     */
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

    @Override
    /***
     * Sorts greatest to least based on the number of available chargers.
     */
    public int compareTo(ChargerStation otherStation)
    {
        return otherStation.getAvailabilityNumber() - this.getAvailabilityNumber();
    }
}
