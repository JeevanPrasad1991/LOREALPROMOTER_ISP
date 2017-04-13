package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.himalaya.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class StoreImageActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView img_cam, img_clicked;
    Button btn_save;
    String _pathforcheck, _path, str;
    String store_cd, visit_date, username, intime, date;
    private SharedPreferences preferences;
    AlertDialog alert;
    String img_str;
    private GSKDatabase database;
    // private LocationManager locationManager;
    String lat, lon;
    GoogleApiClient mGoogleApiClient;
    ArrayList<CoverageBean> coverage_list;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_cam = (ImageView) findViewById(R.id.img_selfie);
        img_clicked = (ImageView) findViewById(R.id.img_cam_selfie);
        btn_save = (Button) findViewById(R.id.btn_save_selfie);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        str = CommonString.FILE_PATH;

        database = new GSKDatabase(this);
        database.open();

        coverage_list = database.getCoverageData(date);

        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

       /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000, 1, this);*/

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(this, DailyEntryScreen.class);
        startActivity(i);*/
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_cam_selfie:
                _pathforcheck = store_cd + "Store" + "Image" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();

                startCameraActivity();
                break;

            case R.id.btn_save_selfie:
                if (img_str != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreImageActivity.this);
                    builder.setMessage("Do you want to save the data ")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                    CoverageBean cdata = new CoverageBean();
                                    cdata.setStoreId(store_cd);
                                    cdata.setVisitDate(visit_date);
                                    cdata.setUserId(username);
                                    cdata.setInTime(intime);
                                    cdata.setReason("");
                                    cdata.setReasonid("0");
                                    cdata.setLatitude(lat);
                                    cdata.setLongitude(lon);
                                    cdata.setImage(img_str);
                                    cdata.setRemark("");
                                    cdata.setStatus(CommonString.KEY_CHECK_IN);
                                    // cdata.setImage01("");
                                    // cdata.setOutTime(getCurrentTime());

                                    database.InsertCoverageData(cdata);

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                    editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                                    editor.putString(CommonString.KEY_LATITUDE, "");
                                    editor.putString(CommonString.KEY_LONGITUDE, "");
                                    //editor.putString(CommonString1.KEY_STOREVISITED_STATUS + st, "No");
                                    editor.commit();

                                    Intent in = new Intent(StoreImageActivity.this, StoreEntry.class);
                                    startActivity(in);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please click the image", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);

        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {

                        // Decode the filepath with BitmapFactory followed by the position
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);

                        // Set the decoded bitmap into ImageView
                        img_cam.setImageBitmap(bmp);
                        img_clicked.setVisibility(View.GONE);
                        img_cam.setVisibility(View.VISIBLE);

                        img_str = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/
        return cdate;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
