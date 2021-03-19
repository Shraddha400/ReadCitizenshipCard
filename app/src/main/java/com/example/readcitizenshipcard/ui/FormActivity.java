package com.example.readcitizenshipcard.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.readcitizenshipcard.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FormActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, AdapterView.OnItemSelectedListener {
    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    private static final String TAG = FormActivity.class.getCanonicalName();
    protected LatLng start = null;
    protected LatLng end = null;
    String n;
    String a;
    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    boolean locationPermission = false;
    double lat1 = 0, long1 = 0, lat2 = 0, long2 = 0;
    double startlat = 0, startlong = 0, endlat = 0, endlong = 0;
    int flag = 0;
    Bitmap bmp, scaledBMP,bmp2,scaledBmp2;
    DateFormat dateFormat;
    private TextInputEditText nameInputTextField;
    private TextInputEditText addressInputEditText;
    private GoogleMap mMap;
    //polyline object
    private List<Polyline> polylines = null;
    private int pageWidth = 1200;
    private int pageHeight = 2010;
    private String sType;
    private String education;
    private Spinner spinnerEDU;
    //current and destination location objects
    private EditText etSource, etDistination;
    private EditText year, month, day;
    private TextView distanceText;
    private Button submitButton;
    private TextInputEditText citizenshipNumber;
    private TextInputEditText pdaddressInputEditText, td;
    private TextInputEditText panameInputTextField, ta;
    private TextInputEditText pwaddressInputEditText, tw;
    private Date dateObj;
    //polyline object
    private String[] informationArray = new String[]{"Citizenship Number",
            "Full Name",
            "Birth District",
            "Birth Municipality",
            "Birth Ward",
            "Permanent District",
            "Permanent Municipality",
            "Permanent Ward",
            "Birth Year",
            "Birth Month",
            "Birth Day"};

    private ArrayAdapter spinnerarrayAdapter, spinnerarrayAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ReadCitizenshipCard);
        setContentView(R.layout.activity_form_activity);
        getSupportActionBar().hide();
        //request location permission.
        requestPermision();
        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
        //27.658143,85.3199503
        //27.667491,85.3208583
        spinnerEDU = findViewById(R.id.education_spinner);
        spinnerarrayAdapter = ArrayAdapter.createFromResource(this, R.array.education, R.layout.spinner_text_view);
        spinnerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEDU.setAdapter(spinnerarrayAdapter);
        spinnerEDU.setOnItemSelectedListener(this);

        distanceText = findViewById(R.id.distance_text);
        citizenshipNumber = findViewById(R.id.citizenship_number);
        nameInputTextField = findViewById(R.id.name_citz);
        pdaddressInputEditText = findViewById(R.id.permanent_district_ctiz);
        panameInputTextField = findViewById(R.id.permanent_municipality_address_ctiz);
        pwaddressInputEditText = findViewById(R.id.permanent_ward_address_ctiz);
        td = findViewById(R.id.temporary_district_ctiz);
        ta = findViewById(R.id.temporary_municipality_address_ctiz);
        tw = findViewById(R.id.temporary_ward_address_ctiz);
        year = findViewById(R.id.edit_date_year);
        month = findViewById(R.id.edit_date_month);
        year = findViewById(R.id.edit_date_year);
        day = findViewById(R.id.edit_date_day);
        submitButton = findViewById(R.id.submit_button);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.title_back);
        scaledBMP = Bitmap.createScaledBitmap(bmp, pageWidth, 518, false);
        bmp2 = BitmapFactory.decodeResource(getResources(),R.drawable.boy1);
        scaledBmp2 =  Bitmap.createScaledBitmap(bmp2, 320, 300, false);
        //store data in string
        String citznumber_str = getIntent().getStringExtra("citizenshipnumber");
        String sex_str = getIntent().getStringExtra("sex");
        String fullname_str = getIntent().getStringExtra("fullname");
        String dobyear_str = getIntent().getStringExtra("dobyear");
        String dobmonth_str = getIntent().getStringExtra("dobmonth");
        String dobday_str = getIntent().getStringExtra("dobday");
        String birthpalcedistrict_str = getIntent().getStringExtra("birthpalcedistrict");
        String birthplacearea_str = getIntent().getStringExtra("birthplacearea");
        String birthplaceward_str = getIntent().getStringExtra("birthplaceward");
        String permanentaddressdistrict_str = getIntent().getStringExtra("permanentaddressdistrict");
        String permanentaddressarea_str = getIntent().getStringExtra("permanentaddressarea");
        String permanentaddressward_str = getIntent().getStringExtra("permanentaddressward");
// set Into view
        citizenshipNumber.setText(citznumber_str);
        nameInputTextField.setText(fullname_str);
        pdaddressInputEditText.setText(permanentaddressdistrict_str);
        panameInputTextField.setText(permanentaddressarea_str);
        pwaddressInputEditText.setText(permanentaddressward_str);
        year.setText(dobyear_str);
        month.setText(dobmonth_str);
        day.setText(dobday_str);
        td.setText(birthpalcedistrict_str);
        ta.setText(birthplacearea_str);
        tw.setText(birthplaceward_str);

//        Toast.makeText(this, "sex" + sex_str, Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onCreate: sexxxxxxxxxxxxxxxxxx"+ sex_str);
        etSource = findViewById(R.id.source);
        etDistination = findViewById(R.id.destination);
//Innitialize places
        Places.initialize(getApplicationContext(), "AIzaSyCQPMYiXn1VBW6MNcIkt-oe8MfVjx-gcnQ");
        //set edit text focusable
        etSource.setFocusable(false);
        etSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define type
                sType = "source";
                //Initialize place field list
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                ).build(FormActivity.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
        //set edit text non focusable
        etDistination.setFocusable(false);
        etDistination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Define type
                sType = "destination";
                // Initialize place field list
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG);
                //CREATE INTENT
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                ).build(FormActivity.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
        distanceText.setText("0.0 Kilometers");
//        if (savedInstanceState != null) {
//            n = savedInstanceState.getString("name");
//            nameInputTextField.setText(n);
//            a = savedInstanceState.getString("address");
//            pdaddressInputEditText.setText(a);
//        }
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        createPDF();
    }

    private void createPDF() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateObj = new Date();

                if (citizenshipNumber.getText().length() == 0 ||
                        nameInputTextField.getText().length() == 0 ||
                        pdaddressInputEditText.getText().length() == 0 ||
                        panameInputTextField.getText().length() == 0 ||
                        pwaddressInputEditText.getText().length() == 0 ||
                        year.getText().length() == 0 ||
                        month.getText().length() == 0 ||
                        day.getText().length() == 0 ||
                        tw.getText().length() == 0 ||
                        ta.getText().length() == 0 ||
                        td.getText().length() == 0) {
                    Toast.makeText(FormActivity.this, "Some field is Empty. Please check", Toast.LENGTH_LONG).show();
                } else {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();
                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                    PdfDocument.Page mypage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = mypage1.getCanvas();
                    canvas.drawBitmap(scaledBMP, 0, 0, myPaint);
                    canvas.drawBitmap(scaledBmp2,pageWidth-360,pageHeight-1820,myPaint);

//                    titlePaint.setTextAlign(Paint.Align.CENTER);
//                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    titlePaint.setTextSize(70);
//                    canvas.drawText("EasyKYC", pageWidth / 2, 270, titlePaint);

                    myPaint.setColor(Color.rgb(255, 255, 255));
                    myPaint.setTextSize(30f);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Call: +977-9843567568", 1160, 40, myPaint);
                    canvas.drawText("01-4339182", 1160, 80, myPaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(70);

                    canvas.drawText("FORM", pageWidth / 2, 600, titlePaint);
                    myPaint.setColor(Color.rgb(0, 113, 118));
                    myPaint.setTextSize(30f);
                    dateFormat = new SimpleDateFormat("dd/mm/yy");
                    canvas.drawText("Date: " + dateFormat.format(dateObj), pageWidth - 20, 600, myPaint);
                    dateFormat = new SimpleDateFormat("hh:mm:ss");
                    canvas.drawText("Time: " + dateFormat.format(dateObj), pageWidth - 20, 650, myPaint);

//for photo frame
//                    myPaint.setStyle(Paint.Style.STROKE);
//                    myPaint.setStrokeWidth(2);
//                    myPaint.setColor(Color.BLACK);
//                    canvas.drawRect(200, 700, myPageInfo1.getPageWidth() - 10, 300, myPaint);
//                    canvas.drawLine(85, 700, 85, 300, myPaint);
//                    canvas.drawLine(163, 700, 163, 300, myPaint);
//for photo text
//                    myPaint.setStrokeWidth(0);
//                    myPaint.setStyle(Paint.Style.FILL);
//                    myPaint.setColor(Color.BLACK);
//                    canvas.drawText("Photo", 35, 700, myPaint);
//                    canvas.drawText("Photo", 110, 700, myPaint);
//                    canvas.drawText("Photo", 190, 700, myPaint);
//for text inside strokes

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(60);
                    myPaint.setColor(Color.BLACK);

                    int startXPosition = 20;
                    int startYPosition = 800;
                    int endXPosition = myPageInfo1.getPageWidth() - 10;

                    for (int i = 0; i < 11; i++) {
                        canvas.drawText(informationArray[i], startXPosition, startYPosition, myPaint);

//                        canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, myPaint);
                        startYPosition += 60;
                    }
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(50);
                    myPaint.setColor(Color.BLACK);
                    myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                    int startRXposition = 800;
                    int startRYposition = 800;
                    canvas.drawText(":"+ citizenshipNumber.getText().toString(), startRXposition, startRYposition, myPaint);
                    canvas.drawText(":"+ nameInputTextField.getText().toString(), startRXposition, startRYposition + 60, myPaint);
                    canvas.drawText(":"+ td.getText().toString(), startRXposition, startRYposition+120, myPaint);
                    canvas.drawText(":"+ ta.getText().toString(), startRXposition, startRYposition+180, myPaint);
                    canvas.drawText(":"+ tw.getText().toString(), startRXposition, startRYposition+240, myPaint);
                    canvas.drawText(":"+ pdaddressInputEditText.getText().toString(), startRXposition, startRYposition+300, myPaint);
                    canvas.drawText(":"+ panameInputTextField.getText().toString(), startRXposition, startRYposition+360, myPaint);
                    canvas.drawText(":"+ pwaddressInputEditText.getText().toString(), startRXposition, startRYposition+420, myPaint);
                    canvas.drawText(":"+ year.getText().toString(), startRXposition, startRYposition+480, myPaint);
                    canvas.drawText(":"+ month.getText().toString(), startRXposition, startRYposition+540, myPaint);
                    canvas.drawText(":"+ day.getText().toString(), startRXposition, startRYposition+600, myPaint);



//                    canvas.drawLine(100, 92, 80, 190, myPaint);
                    myPdfDocument.finishPage(mypage1);
                    File file = new File(Environment.getExternalStorageDirectory(), "/KYC.pdf");
                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Your .pdf file is ready !! ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.GREEN);
                        snackbar.setDuration(2000);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myPdfDocument.close();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check Condition
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //when success
            //Initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //check condition
            if (sType.equals("source")) {
                //when type is source
                //increase flag value
                flag++;
                //set Address on edit text
                etSource.setText(place.getAddress());
                //get Latitude and longitude
                String sSource = String.valueOf(place.getLatLng());
                sSource = sSource.replaceAll("lat/lng:", "");
                sSource = sSource.replace("(", "");
                sSource = sSource.replace(")", "");
                String[] split = sSource.split(",");
                lat1 = Double.parseDouble(split[0]);
                long1 = Double.parseDouble(split[1]);
            } else {
                //when type is destination
                //increase flag value
                flag++;
                //set address on edit text
                etDistination.setText(place.getAddress());
                //get lat and long
                String sDestination = String.valueOf(place.getLatLng());
                sDestination = sDestination.replaceAll("lat/lng:", "");
                sDestination = sDestination.replace("(", "");
                sDestination = sDestination.replace(")", "");
                String[] split = sDestination.split(",");
                lat2 = Double.parseDouble(split[0]);
                long2 = Double.parseDouble(split[1]);
            }
            //check condition
            if (flag >= 2) {
                //when flag is greater than and eualas to 2
                //calculate distance
                distance(lat1, long1, lat2, long2);
            }


        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            //Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void distance(double lat1, double long1, double lat2, double long2) {
        //Calculate longitude difference
        double longDiff = long1 - long2;
        //calculate distance
        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        //Convert distance radian to degree
        distance = rad2deg(distance);
        //Distance in miles
        distance = distance * 60 * 1.1515;
        //Distance in km
        distance = distance * 1.609344;
        //set distance in textview
        distanceText.setText(String.format(Locale.US, "%2f Kilometers", distance));
        distanceText.setTextColor(Color.BLUE);

    }

    //convert redian to degree
    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    //Convert degree to radian
    private double deg2rad(double lat1) {
        return (lat1 * Math.PI / 180.0);
    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation = location;
                LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                mMap.animateCamera(cameraUpdate);

            }
        });
//get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end = latLng;

                mMap.clear();

                start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                //start route finding
                Findroutes(start, end);
            }
        });

    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(FormActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCQPMYiXn1VBW6MNcIkt-oe8MfVjx-gcnQ")//also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//    Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(FormActivity.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.teal_200));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }
        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("Source");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
        startlat = start.latitude;
        startlong = start.longitude;
        endlat = end.latitude;
        endlong = end.longitude;
        float[] results = new float[1];
        Location.distanceBetween(startlat, startlong, endlat, endlong, results);
        float distance = results[0];
        float kilometer = (float) (distance / 1000);
        distanceText.setText(String.format(Locale.US, "%2f Kilometers", kilometer));
        distanceText.setTextColor(Color.BLUE);

    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start, end);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getMyLocation();

    }

//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "Potrat mode", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "LandScape mode", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        n = nameInputTextField.getText().toString();
//        a = addressInputEditText.getText().toString();
//        outState.putString("name", n);
//        outState.putString("address", a);
//
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(FormActivity.this, "NO SERVICE", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            errorDialog.show();
        } else {
           // Toast.makeText(this, "All is good", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        education = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//         mMap = googleMap;
//        LatLng nepal = new LatLng(27.45,85.20);
//        mMap.addMarker(new MarkerOptions().position(nepal).title("Marker in Kathmandu"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(nepal));
//
//    }
}