package com.android.flipmapapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    double latitude;
    double longitude;
    TextView textView;
    String name;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textView = (TextView) findViewById(R.id.latlon);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latitude=Double.parseDouble(intent.getStringExtra("Latitude"));
        longitude = Double.parseDouble(intent.getStringExtra("Longitude"));
        name = intent.getStringExtra("Name");

        Log.d("Flip",latitude + "" + longitude);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng map = new LatLng(latitude, longitude);
        textView.setText("Latitude: "+latitude +"   Longitude: "+longitude);
        mMap.addMarker(new MarkerOptions().position(map).title(name));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
        if(latitude<90&&longitude<90){
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    map, 15);
            mMap.animateCamera(location);
        }
        else
        {
            Toast.makeText(this, "Range is above 90 ", Toast.LENGTH_SHORT).show();
             mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }



    }


}
