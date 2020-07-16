package com.example.covid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class track extends FragmentActivity implements OnMapReadyCallback {

    //private GoogleMap mMap;

    // private GoogleApiClient mGoogleApiClient;
    //private Location mlocation;
   // private LocationCallback mLocationCallback;
   // private LocationManager mlocationManager;
    //private LocationRequest mlocationRequest;
  //  private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 10000;
    private long FASTEST_INTERVAL = 15000;
   // private LocationManager locationManager;
   // private LatLng latLng;
   // private FusedLocationProviderClient fusedLocationProviderClient;
///    private boolean isPermission;

    ///
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;


    ////
    Task<Void> databaseReference;
    FirebaseDatabase firebaseDatabase;
    List<coordinates> distance_coordinatesList1=new ArrayList<>();
    long user_phone_no=0;
    List<Long> interacted_people=new ArrayList<>();
    private user user=new user();
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Intent intent = getIntent();
        userid=intent.getStringExtra("userid");

        FirebaseDatabase.getInstance().getReference().child("User").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(user.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
      }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL); // two minute interval
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }


                List<coordinates> coordinatesList=new ArrayList<>();

                //Place current location marker
                mGoogleMap.clear();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //coordinats class update data
                coordinates u1=new coordinates(latLng.latitude,latLng.longitude,Long.parseLong(userid));
                coordinatesList.add(u1);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Curre Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                mCurrLocationMarker.setTitle("ME");
                mCurrLocationMarker.showInfoWindow();

                //////


                /*LatLng latLng1 = new LatLng(location.getLatitude()+0.2, location.getLongitude()+0.3);

                //coordinats class update data
                coordinates u2=new coordinates(latLng1.latitude,latLng1.longitude,Long.parseLong(userid)+1);
                coordinatesList.add(u2);

                MarkerOptions markerOptions1 = new MarkerOptions();

                markerOptions1.position(latLng1);
                markerOptions1.title("Current1 Position");
                markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                //////




                Marker m1=mGoogleMap.addMarker(markerOptions1);

                LatLng latLng2 = new LatLng(location.getLatitude()+0.1, location.getLongitude()+0.1);


                //coordinats class update data
                coordinates u3=new coordinates(latLng2.latitude,latLng2.longitude,Long.parseLong(userid)+2);
                coordinatesList.add(u3);
                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions2.position(latLng2);
                markerOptions2.title("Current2 Position");
                markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                Marker m2=mGoogleMap.addMarker(markerOptions2);
                //mCurrLocationMarker.setTitle("ME");
                m2.showInfoWindow();
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f));
*/

                /////firebase update current location;
                //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                //FirebaseDatabase.getInstance().getReference().child("Coordinates");
                //FirebaseApp.initializeApp(track.this);
                FirebaseDatabase.getInstance().getReference().child("Coordinates").child(userid)
                        .setValue(u1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(track.this,"Location saved",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(track.this,"Location not saved",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                user_phone_no=u1.getPhoneNo();
                FirebaseDatabase.getInstance().getReference().child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d:dataSnapshot.getChildren())
                    {
                       coordinates c=new coordinates();

                       c=d.getValue(coordinates.class);

                        if(user_phone_no!=c.getPhoneNo())
                        {
                            distance_coordinatesList1.add(c);

                            LatLng latLng = new LatLng(c.getLat(),c.getLon());

                            MarkerOptions markerOptions1 = new MarkerOptions();

                            markerOptions1.position(latLng);
                            markerOptions1.title(c.getPhoneNo()+"");
                            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            Marker m1=mGoogleMap.addMarker(markerOptions1);

                        }
                      //  Toast.makeText(track.this,"ph==="+  c.getPhoneNo(),Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(track.this,"dist b/w "+distance_coordinatesList1,Toast.LENGTH_SHORT).show();
                for(coordinates i:distance_coordinatesList1)
                {
//                    Toast.makeText(track.this,"dist b/w "+u1.getLat()+"&"+i.getLat(),Toast.LENGTH_SHORT).show();

                    double dist=(distance(u1.getLat(),u1.getLon(),i.getLat(),i.getLon()))*1.60934;
                    Toast.makeText(track.this,"dist b/w "+u1.getPhoneNo()+"&"+i.getPhoneNo()+" is"+dist,Toast.LENGTH_SHORT).show();

                    if(dist<=0.0018288)
                    {
                        Toast.makeText(track.this,"dist b/w "+u1.getPhoneNo()+"&"+i.getPhoneNo()+" is very less",Toast.LENGTH_SHORT).show();
                        interacted_people.add(i.getPhoneNo());
                    }
                }
                if(interacted_people.size()!=0)
                {
                    if(user.getInteracted_user().size()!=0)
                    {
                        HashMap<Long,Long> hashMap=new HashMap<>();
                        for(int i=0;i<user.getInteracted_user().size();i++)
                        {
                            hashMap.put(user.getInteracted_user().get(i),user.getInteracted_user().get(i));
                        }
                        for(int i=0;i<interacted_people.size();i++)
                        {
                            if(!hashMap.containsKey(interacted_people.get(i)))
                            {
                                user.getInteracted_user().add(interacted_people.get(i));
                            }
                        }
                        Toast.makeText(track.this, "user list " + interacted_people, Toast.LENGTH_SHORT).show();
                        user user_in = new user(user_phone_no,user.getInteracted_user());
                        Toast.makeText(track.this, "user list " + user_in.getInteracted_user(), Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference().child("User").child(userid).setValue(user_in);
                    }
                    else
                    {
                        Toast.makeText(track.this, "user list empty", Toast.LENGTH_SHORT).show();

                    }
                }
                interacted_people.clear();
                distance_coordinatesList1.clear();
                /*for(final coordinates i:coordinatesList) {
                    coordinates t=new coordinates(i.getPhoneNo(),i.getLat(),i.getLon());
                    final long j=i.getPhoneNo();
                    FirebaseDatabase.getInstance().getReference().child("Coordinates").child(i.getPhoneNo()+"").setValue(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(track.this,"Location saved"+j,Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(track.this,"Location not saved",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }*/



            }
        }
    };
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    ////
    /*private double distance(double lt1,double lon1,double lt2,double lon2)
    {
        double theta=lon1-lon2;
        double dist=Math.sin(deg2rad(lt1))*Math.sin(deg2rad(lt2))
                *Math.cos(deg2rad(lt1))*Math.cos(deg2rad(lt2))
                *Math.cos(deg2rad(theta));
        dist =Math.acos(dist);
        dist=rad2deg(dist);
        dist=dist*60*1.1515;
        return  dist;
    }
    private double deg2rad(double deg)
    {
        return  (deg*Math.PI/180);
    }

    private double rad2deg(double rad)
    {
        return  (rad*180/Math.PI);
    }*/



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(track.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
