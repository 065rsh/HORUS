package com.horus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static TextView firstTemp, secondTemp, firstSmoke, secondSmoke, sensor_TV;
    View tempWarningBG_V, smokeWarningBG_V;
    ImageView tempWarningClipart_IV, smokeWarningClipart_IV;
    public static Boolean firstTempWarn = false, secondTempWarn = false, firstSmokeWarn = false, secondSmokeWarn = false;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

//        View view = getSupportActionBar().getCustomView();
        firstTemp = findViewById(R.id.firstTemp);
        secondTemp = findViewById(R.id.secondTemp);
        firstSmoke = findViewById(R.id.firstSmoke);
        secondSmoke = findViewById(R.id.secondSmoke);
        sensor_TV = findViewById(R.id.sensor_TV);

        tempWarningBG_V = findViewById(R.id.tempWarningBG_V);
        smokeWarningBG_V = findViewById(R.id.smokeWarningBG_V);

        tempWarningClipart_IV = findViewById(R.id.tempWarningClipart_IV);
        smokeWarningClipart_IV = findViewById(R.id.smokeWarningClipart_IV);

        Button about_B = findViewById(R.id.about_B);
        Button demo_B = findViewById(R.id.demo_B);

        demo_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        Typeface spectralBold = Typeface.createFromAsset(getAssets(), "fonts/Spectral-Bold.ttf");
//        Typeface spectralRegular = Typeface.createFromAsset(getAssets(), "fonts/Spectral-Regular.ttf");
//        Typeface comfortaaBold = Typeface.createFromAsset(getAssets(), "fonts/Comfortaa-Regular.ttf");
//
//        titleTeam.setTypeface(spectralBold);
//        titleHorus.setTypeface(spectralRegular);
//        demo_B.setTypeface(comfortaaBold);
//        about_B.setTypeface(spectralBold);

        about_B.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(about_B.getContext(), R.drawable.ic_bubble_chart_black_24dp), null, null, null);

//        ImageButton imageButton = view.findViewById(R.id.action_bar_back);
        fetchData process = new fetchData();
        process.execute();
        if(firstTempWarn){
            tempWarningBG_V.setVisibility(View.VISIBLE);
            tempWarningClipart_IV.setVisibility(View.VISIBLE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
        } else {
            tempWarningBG_V.setVisibility(View.GONE);
            tempWarningClipart_IV.setVisibility(View.GONE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
        } if(secondTempWarn){
            tempWarningBG_V.setVisibility(View.VISIBLE);
            tempWarningClipart_IV.setVisibility(View.VISIBLE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
        } else {
            tempWarningBG_V.setVisibility(View.GONE);
            tempWarningClipart_IV.setVisibility(View.GONE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
        } if(firstSmokeWarn){
            smokeWarningBG_V.setVisibility(View.VISIBLE);
            smokeWarningClipart_IV.setVisibility(View.VISIBLE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
        } else {
            smokeWarningBG_V.setVisibility(View.GONE);
            smokeWarningClipart_IV.setVisibility(View.GONE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
        } if(secondSmokeWarn){
            smokeWarningBG_V.setVisibility(View.VISIBLE);
            smokeWarningClipart_IV.setVisibility(View.VISIBLE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
        } else {
            smokeWarningBG_V.setVisibility(View.GONE);
            smokeWarningClipart_IV.setVisibility(View.GONE);
            sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
        }

        final Handler handler = new Handler();
// Define the code block to be executed

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                fetchData process = new fetchData();
                process.execute();

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

                if(firstTempWarn){
                    tempWarningBG_V.setVisibility(View.VISIBLE);
                    tempWarningClipart_IV.setVisibility(View.VISIBLE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
                } else {
                    tempWarningBG_V.setVisibility(View.GONE);
                    tempWarningClipart_IV.setVisibility(View.GONE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
                } if(secondTempWarn){
                    tempWarningBG_V.setVisibility(View.VISIBLE);
                    tempWarningClipart_IV.setVisibility(View.VISIBLE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
                } else {
                    tempWarningBG_V.setVisibility(View.GONE);
                    tempWarningClipart_IV.setVisibility(View.GONE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
                } if(firstSmokeWarn){
                    smokeWarningBG_V.setVisibility(View.VISIBLE);
                    smokeWarningClipart_IV.setVisibility(View.VISIBLE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
                } else {
                    smokeWarningBG_V.setVisibility(View.GONE);
                    smokeWarningClipart_IV.setVisibility(View.GONE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
                } if(secondSmokeWarn){
                    smokeWarningBG_V.setVisibility(View.VISIBLE);
                    smokeWarningClipart_IV.setVisibility(View.VISIBLE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorSensorWarning));
                } else {
                    smokeWarningBG_V.setVisibility(View.GONE);
                    smokeWarningClipart_IV.setVisibility(View.GONE);
                    sensor_TV.setTextColor(getResources().getColor(R.color.colorAppText));
                }
//                CU#Idea@
                handler.postDelayed(this, 2000);
            }
        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void warningSelector(View viewWarningBG, View viewWarningClipart, int visibility, int color){
        viewWarningBG.setVisibility(visibility);
        viewWarningClipart.setVisibility(visibility);
        sensor_TV.setTextColor(color);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else  if (location1 != null) {
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();

            } else  if (location2 != null) {
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();

            }else{
                Log.i("OX_HORUS", "Unable to Trace your location");
//                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // Add a marker in currentLocation and move the camera
        final Marker marker = googleMap.addMarker(new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        // to make the car center as the origin for the marker
                        .position(new LatLng(latitude,longitude))
                        // set the latitude and longitude parameters in the position function
                        .flat(true)
                        // marker will retain its size when the map is zoomed in or zoomed out
                        // get the green_car image and resize it using the function resizeImage which will return Bitmap image
                        .title("Your Vehicle")
                // this title will appear when user clicks on the marker
        );
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longitude))      // Sets the center of the map to Shambhavi hostel
                .zoom(15)                   // Sets the zoom
                .tilt(0)                    // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker.setPosition(new LatLng(latitude,longitude));
    }

}

// Todo: Bring elements into proper structure
// Todo: Write code for retrieve from both local and cloud base
// Todo: Include Google maps in a fragment
// Todo: