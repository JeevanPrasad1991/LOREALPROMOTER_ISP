package com.cpm.GeoTag;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.GeotaggingBeans;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.LoginActivity;
import com.cpm.lorealpromoter.R;
import com.cpm.message.AlertMessage;
import com.cpm.upload.Base64;
import com.cpm.xmlGetterSetter.FailureGetterSetter;
import com.cpm.xmlHandler.FailureXMLHandler;
/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;*/
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by upendra on 27-12-3018.
 */


public class GeoTagActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    String result, errormsg = "";
    private ProgressBar pb;
    private GoogleMap mMap;
    double latitude = 0.0;
    double longitude = 0.0;
    protected String diskpath = "", _path, _pathforcheck, img_str = "", status;
    private Location mLastLocation;
    private LocationManager locmanager = null;
    FloatingActionButton fab, fabcarmabtn;
    SupportMapFragment mapFragment;
    SharedPreferences preferences;
    String username, storeid, str, visitData;
    GSKDatabase db;
    LocationManager locationManager;
    Geocoder geocoder;
    boolean enabled;
    private Dialog dialog;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient=null;
    private LocationRequest mLocationRequest=null;
    private static int UPDATE_INTERVAL = 500; // 5 sec
    private static int FATEST_INTERVAL = 100; // 1 sec
    private static int DISPLACEMENT = 5; // 10 meters
    private static final String TAG = GeoTagActivity.class.getSimpleName();

    private int factor, k;
    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();
    ArrayList<GeotaggingBeans> geotaglistImage = new ArrayList<GeotaggingBeans>();
    Boolean markerflag = true;
    private TextView percentage, message;
    Bitmap bmp, dest;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        storeid = getIntent().getStringExtra(CommonString.KEY_STORE_ID);
        visitData = preferences.getString(CommonString.KEY_DATE, null);
        final PackageManager packageManager = getPackageManager();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabcarmabtn = (FloatingActionButton) findViewById(R.id.camrabtn);
        db = new GSKDatabase(GeoTagActivity.this);
        db.open();

        str = CommonString.FILE_PATH;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

        locmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GeoTagActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!img_str.equalsIgnoreCase("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(GeoTagActivity.this);
                    builder.setMessage("Are you sure you want to save")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.open();
                                    status = "N";
                                    db.InsertSTOREgeotag(storeid, latitude, longitude, img_str, status);
                                    img_str = "";
                                    new GeoTagUpload(GeoTagActivity.this).execute();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    Snackbar.make(view, getResources().getString(R.string.takeimage), Snackbar.LENGTH_LONG).setAction("Action", null).show();


                }

            }
        });
        fabcarmabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pathforcheck = storeid + "_GeoTag_" + username + "_" + visitData.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;

                startCameraActivity();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (client != null) {
            client.connect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notsuppoted)
                        , Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if(mGoogleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mGoogleApiClient != null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                mMap.setMyLocationEnabled(true);
                if (markerflag) {
                    // Add a marker of latest location and move the camera
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                }

            }
        }
        if (mGoogleApiClient != null) {
            startLocationUpdates();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    protected void onResume() {
        super.onResume();

       // if (!mGoogleApiClient.isConnected()) {
        if (mGoogleApiClient!= null) {
            mGoogleApiClient.connect();
        }
        locmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GeoTagActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        }

    }

/*
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        client.disconnect();
    }
*/

/*
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }
*/


    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {

                        bmp = convertBitmap(str + _pathforcheck);
                        dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

                        Canvas cs = new Canvas(dest);
                        Paint tPaint = new Paint();
                        tPaint.setTextSize(100);
                        tPaint.setColor(Color.RED);
                        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                        cs.drawBitmap(bmp, 0f, 0f, null);
                        float height = tPaint.measureText("yY");
                        cs.drawText(dateTime, 20f, height + 15f, tPaint);
                        try {
                            dest.compress(Bitmap.CompressFormat.JPEG, 100,
                                    new FileOutputStream(new File(str + _pathforcheck)));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        fabcarmabtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.camera_icon_done));
                        fabcarmabtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DB6AC")));
                        img_str = _pathforcheck;
                        _pathforcheck = "";
                        markerflag = false;

                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        private Context context;

        GeoTagUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_upload);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                GSKDatabase db = new GSKDatabase(GeoTagActivity.this);
                db.open();
                geotaglist = db.getinsertGeotaggingData(storeid);

                // uploading Geotag
                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                String geo_xml = "";
                if (geotaglist.size() > 0) {
                    for (int i = 0; i < geotaglist.size(); i++) {
                        String onXML = "[GeoTag_DATA][STORE_ID]"
                                + geotaglist.get(i).getStoreid()
                                + "[/STORE_ID]"
                                + "[LATTITUDE]"
                                + geotaglist.get(i).getLatitude()
                                + "[/LATTITUDE]"
                                + "[LONGITUDE]"
                                + geotaglist.get(i).getLongitude()
                                + "[/LONGITUDE]"
                                + "[FRONT_IMAGE]"
                                + geotaglist.get(i).getUrl1()
                                + "[/FRONT_IMAGE]"
                                + "[CREATED_BY]" + username
                                + "[/CREATED_BY][/GeoTag_DATA]";

                        geo_xml = geo_xml + onXML;

                    }
                    geo_xml = "[DATA]" + geo_xml + "[/DATA]";
                    SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                    request.addProperty("MID", "0");
                    request.addProperty("KEYS", "GEOTAG_DATA");
                    request.addProperty("USERNAME", username);
                    request.addProperty("XMLDATA", geo_xml);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                    androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                    Object result = (Object) envelope.getResponse();
                    if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                    } else {
                        return CommonString.METHOD_UPLOAD_XML;

                    }
                }

                return CommonString.KEY_SUCCESS;

            } catch (SocketException ex) {
                ex.printStackTrace();
                Intent intent = new Intent(GeoTagActivity.this, GeoTagStoreList.class);
                startActivity(intent);
                GeoTagActivity.this.finish();

            } catch (Exception ex) {
                Crashlytics.logException(ex);
                ex.printStackTrace();
                Intent intent = new Intent(GeoTagActivity.this, GeoTagStoreList.class);
                startActivity(intent);
                GeoTagActivity.this.finish();
            }
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String statusD = "Y";
            dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                db.open();
                db.updateGeoTagData(storeid, statusD);
                db.updateDataStatus(storeid, statusD);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.geotag), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GeoTagActivity.this, GeoTagStoreList.class);
                startActivity(intent);
                GeoTagActivity.this.finish();

            } else if (!result.equals(CommonString.KEY_SUCCESS)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.geotag_done), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GeoTagActivity.this, GeoTagStoreList.class);
                startActivity(intent);
                GeoTagActivity.this.finish();

            } else if (!result.equals("")) {

            }

        }

    }

    public String UploadGeoImage(String path, String folder) throws Exception {

        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(CommonString.FILE_PATH + path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(CommonString.FILE_PATH + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);

        request.addProperty("img", ba1);
        request.addProperty("name", path);
        request.addProperty("FolderName", folder);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);
        Object result = (Object) envelope.getResponse();
        if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
            new File(CommonString.FILE_PATH + path).delete();
        } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
            return CommonString.KEY_FALSE;
        } else {
            return CommonString.KEY_FAILURE;
        }

        return result.toString();
    }

    private boolean checkgpsEnableDevice() {
        boolean flag = true;
        if (!hasGPSDevice(GeoTagActivity.this)) {
            Toast.makeText(GeoTagActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(GeoTagActivity.this)) {
            //  enableLoc();
            flag = false;
        } else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(GeoTagActivity.this)) {
            flag = true;
        }
        return flag;
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    private void enableLoc() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

    }

    public static Bitmap convertBitmap(String path)   {
        Bitmap bitmap=null;
        BitmapFactory.Options ourOptions=new BitmapFactory.Options();
        ourOptions.inDither=false;
        ourOptions.inPurgeable=true;
        ourOptions.inInputShareable=true;
        ourOptions.inTempStorage=new byte[32 * 1024];
        File file=new File(path);
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null)
            {
                bitmap=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, ourOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


}
