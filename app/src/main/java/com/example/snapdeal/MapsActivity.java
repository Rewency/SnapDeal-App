package com.example.snapdeal;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText addressText;
    LocationManager locManager;
    myLocationListner locListner;
    Button getLocation,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        addressText = (EditText) findViewById(R.id.editText);
        getLocation = (Button) findViewById(R.id.btn1);
        backBtn = (Button) findViewById(R.id.btn2);
        locListner = new myLocationListner(getApplicationContext());
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, locListner);
        } catch (SecurityException ex) {
            Toast.makeText(getApplicationContext(), "not allowed to access the location", Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.04441960, 31.235711600), 8));

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;
                Location loc = null;

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                catch (SecurityException x)
                {
                    Toast.makeText(getApplicationContext(), "you didn't allow to access the current location", Toast.LENGTH_LONG).show();
                }

                if(loc != null)
                {
                    LatLng myPosition=new LatLng(loc.getLatitude(),loc.getLongitude());

                    try
                    {
                        addressList=coder.getFromLocation(myPosition.latitude,myPosition.longitude,1);

                        if(!addressList.isEmpty())
                        {
                            String address="";
                            for(int i = 0; i <= addressList.get(0).getMaxAddressLineIndex();i++)
                                address += addressList.get(0).getAddressLine(i) + ", ";

                            mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").snippet(address)).setDraggable(true);
                            addressText.setText(address);

                        }
                    }
                    catch (IOException e)
                    {
                        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location"));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,15));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG).show();
                }
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MapsActivity.this,ConfirmActivity.class);
                        intent.putExtra("location",addressText.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }
            @Override
            public void onMarkerDrag(Marker marker) {
            }
            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                Geocoder coder=new Geocoder(getApplicationContext());
                List<Address>addressList;

                try
                {
                    addressList=coder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,1);

                    if(! addressList.isEmpty())
                    {
                        String address="";
                        for(int i = 0; i <= addressList.get(0).getMaxAddressLineIndex();i++)
                            address += addressList.get(0).getAddressLine(i) + ", ";
                        addressText.setText(address);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "no address for this location", Toast.LENGTH_LONG).show();
                        addressText.getText().clear();
                    }
                }
                catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), "cant get the address , check your network", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
