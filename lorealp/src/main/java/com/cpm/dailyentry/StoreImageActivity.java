package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.GeoTag.GeoTagActivity;
import com.cpm.GeoTag.GeoTagStoreList;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.message.AlertMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


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
    String lat = "0.0", lon = "0.0";
    GoogleApiClient mGoogleApiClient;
    ArrayList<CoverageBean> coverage_list;
    Intent intent;
    boolean flag_deviation = false;
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private ArrayList<CoverageBean> coverageBeanlist = new ArrayList<CoverageBean>();
    String app_ver;

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
        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_cam_selfie:
                _pathforcheck = store_cd + "_STOREIMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
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
                                    cdata.setStatus(CommonString.KEY_INVALID);
                                    cdata.setPJPDeviation(flag_deviation);
                                    database.InsertCoverageData(cdata);
                                    database.updatecheckoutStore(store_cd, CommonString.KEY_INVALID);
                                    /*if(flag_deviation)
                                    database.InsertPJPDeviationData(store_cd, visit_date);*/
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                    editor.commit();

                                   new selfiesUpload(StoreImageActivity.this).execute();
                                   /* Intent in = new Intent(StoreImageActivity.this, StoreEntry.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    finish();*/
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
                    Snackbar.make(btn_save, "Please click the image", Snackbar.LENGTH_SHORT).show();
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
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
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

    public class selfiesUpload extends AsyncTask<Void, Void, String> {

        private Context context;

        selfiesUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
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

                GSKDatabase db = new GSKDatabase(StoreImageActivity.this);
                db.open();
                coverageBeanlist = database.getCoverageData(visit_date);
                // uploading store selfeis
                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                String onXML = "";
                if (coverageBeanlist.size() > 0) {
                    for (int i = 0; i < coverageBeanlist.size(); i++) {

                       onXML = "[DATA]" + "[USER_DATA]"
                                + "[STORE_CD]" + coverageBeanlist.get(i).getStoreId() + "[/STORE_CD]"
                                + "[VISIT_DATE]" + coverageBeanlist.get(i).getVisitDate() + "[/VISIT_DATE]"
                                + "[LATITUDE]" + coverageBeanlist.get(i).getLatitude() + "[/LATITUDE]"
                                + "[APP_VERSION]" + app_ver + "[/APP_VERSION]"
                                + "[LONGITUDE]" + coverageBeanlist.get(i).getLongitude() + "[/LONGITUDE]"
                                + "[IN_TIME]" + coverageBeanlist.get(i).getInTime() + "[/IN_TIME]"
                                + "[OUT_TIME]" + "" + "[/OUT_TIME]"
                                + "[UPLOAD_STATUS]" + "I" + "[/UPLOAD_STATUS]"
                                + "[USER_ID]" + username + "[/USER_ID]"
                                + "[IMAGE_URL]" + coverageBeanlist.get(i).getImage() + "[/IMAGE_URL]"
                                + "[REASON_ID]" + coverageBeanlist.get(i).getReasonid() + "[/REASON_ID]"
                                + "[REASON_REMARK]" + "" + "[/REASON_REMARK]"
                                + "[/USER_DATA]"
                                + "[/DATA]";

                        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE);
                        request.addProperty("onXML", onXML);
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);
                        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE, envelope);
                        Object result = (Object) envelope.getResponse();
                    }
                }


                return CommonString.KEY_SUCCESS;

            } catch (SocketException ex) {
                ex.printStackTrace();
                Intent intent = new Intent(StoreImageActivity.this, StoreImageActivity.class);
                startActivity(intent);
                StoreImageActivity.this.finish();

            } catch (Exception ex) {
                ex.printStackTrace();
                Intent intent = new Intent(StoreImageActivity.this, StoreImageActivity.class);
                startActivity(intent);
                StoreImageActivity.this.finish();
            }

            return "";

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equals(CommonString.KEY_SUCCESS)) {
                Intent in = new Intent(StoreImageActivity.this, StoreEntry.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                finish();
               // new GeoTagActivity.GeoTagImageUpload(GeoTagActivity.this).execute();

            } else if (!result.equals(CommonString.KEY_SUCCESS)) {
                AlertMessage message = new AlertMessage(
                        StoreImageActivity.this, AlertMessage.MESSAGE_DATA_NOT
                        + result, getResources().getString(R.string.failure), null);
                message.showMessage();

            } else if (!result.equals("")) {

            }

        }

    }

}
