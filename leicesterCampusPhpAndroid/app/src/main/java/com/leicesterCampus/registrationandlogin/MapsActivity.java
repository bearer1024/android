package com.leicesterCampus.registrationandlogin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerDragListener{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private static final LatLng uniOfLeicester = new LatLng(52.62113745, -1.1245235);
    private static final LatLng uniLibrary = new LatLng(52.6205083, -1.1245617);
    private static final LatLng uniBennetBuilding = new LatLng(52.62314004599841,-1.1229104921221733);
    private static final LatLng uniStudentUnion = new LatLng(52.62172914614219,-1.1251498013734818);
    private static final LatLng uniKenEdwards = new LatLng(52.6212795292897,-1.125790849328041);
    private TextView topTextView;
    private Marker mUniOfLeicester;

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            if (marker.equals(mUniOfLeicester)) {
                badge = R.drawable.uniofleicester;
            } else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 10, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        topTextView = (TextView)findViewById(R.id.topTextView);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        topTextView.setText("University map");
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
        //set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMylocation();
        mMap.setOnMarkerDragListener(this);

        // Add a marker in leicester and move the camera
        mUniOfLeicester = mMap.addMarker(new MarkerOptions().position(uniOfLeicester)
                .title("This is the university of leicester")
                .snippet("Campus-based academic institution,\n " +
                        "founded in 1921,\n with 3 high-rise buildings and 23,000 students.")
                .draggable(true));
        mMap.addMarker(new MarkerOptions().position(uniLibrary).title("David Wilson Library")
                .snippet("Simple and modern self-service cafe\n " +
                        "offering straightforward snacks and light meals"));
        mMap.addMarker(new MarkerOptions().position(uniBennetBuilding).title("Bennet Building")
                .snippet(""));
        mMap.addMarker(new MarkerOptions().position(uniStudentUnion).title("Students' Union")
                .snippet(""));
        mMap.addMarker(new MarkerOptions().position(uniKenEdwards).title("Ken Edwards Building")
                .snippet(""));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uniOfLeicester,17));
    }

    private void enableMylocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            //permission to access the location is misssing
            PermissionUtils.requestPermission(this,LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,true);
        }else if(mMap!=null){
            //Access to the location has been granted to the map
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResult){
        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE){
            return ;
        }

        if(PermissionUtils.isPermissionGranted(permissions,grantResult,Manifest.permission.ACCESS_FINE_LOCATION)){
            //Enable the my location layer if the permission has been granted.
            enableMylocation();
        }else{
            //Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments(){
        super.onResumeFragments();
        if(mPermissionDenied){
            //Permission was not granted, display error dialog
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /*
    display a dialog with error message explaining that the location permission is missing
     */
    private void showMissingPermissionError(){
        PermissionUtils.PermissionDeniedDialog.newInstance(true).
                show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        topTextView.setText("Ready? go!");
    }

    @Override
    public void onMarkerDrag(Marker marker){
        topTextView.setText("Current Position is:"+marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        topTextView.setText("Finished Dragging, Current Position is:"+marker.getPosition());
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
