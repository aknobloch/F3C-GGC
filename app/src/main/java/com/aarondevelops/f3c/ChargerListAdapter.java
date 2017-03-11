package com.aarondevelops.f3c;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Aaron K on 3/11/2017.
 */

public class ChargerListAdapter extends ArrayAdapter<String>
{

    public ChargerListAdapter(Context context, int resource, int viewID, List<String> objects) {
        super(context, resource, viewID, objects);
    }
}
