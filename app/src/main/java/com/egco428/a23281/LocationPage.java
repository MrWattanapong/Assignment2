package com.egco428.a23281;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

public class LocationPage extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView title;

    private String user;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = getIntent().getStringExtra("Username");
        latitude = getIntent().getDoubleExtra("Latitude",0);
        longitude = getIntent().getDoubleExtra("Longitude",0);

        title = (TextView)findViewById(R.id.title_main);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latlng).title(user+"'s Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        title.setText(user+"'s Location");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){ //home is back button id (from line *)
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
