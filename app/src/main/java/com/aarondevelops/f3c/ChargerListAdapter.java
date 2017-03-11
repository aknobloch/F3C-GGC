package com.aarondevelops.f3c;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import charge_points.ChargePoint;

/**
 * Created by Aaron K on 3/11/2017.
 */

public class ChargerListAdapter extends ArrayAdapter<ChargePoint>
{

    public ChargerListAdapter(Context context, int resource, int viewID, List<ChargePoint> objects) {
        super(context, resource, viewID, objects);
    }
}
