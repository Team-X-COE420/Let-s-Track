package com.example.teamx.letstrack.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Define_Position_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button Submit;
    private com.example.teamx.letstrack.Application.LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_position);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Submit = findViewById(R.id.btnSubmit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitLocation();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = new com.example.teamx.letstrack.Application.LatLng(latLng);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title(getIntent()
                                .getStringExtra("Position")));
                mMap.addCircle(new CircleOptions()
                        .center(location.location)
                        .radius(100)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                //
//  Define and submit locations for labels on map                                                 //
//                                                                                                //
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void SubmitLocation() {
        Intent i = getIntent();
        String name = i.getStringExtra("Position");


        //FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid().toString()).set(map, SetOptions.merge());
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("Marker").draggable(true)
                    .snippet("Hello")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    //DO NOTHING
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    //DO NOTHING
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng loc = marker.getPosition();
                    location.setLocation(loc);
                }
            });

        }


    }

}

