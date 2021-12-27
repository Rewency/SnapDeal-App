package com.example.snapdeal;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class myLocationListner implements LocationListener
{
    private Context activityContext;

    public myLocationListner(Context cont)
    {
        activityContext = cont;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Toast.makeText(activityContext, location.getLatitude()+", " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    { }
    @Override
    public void onProviderEnabled(String s)
    {
        Toast.makeText(activityContext, "GPS Enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s)
    {
        Toast.makeText(activityContext, "GPS Disabled", Toast.LENGTH_LONG).show();
    }
}
