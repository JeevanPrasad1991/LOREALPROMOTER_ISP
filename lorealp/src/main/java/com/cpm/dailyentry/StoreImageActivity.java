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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.cpm.Constants.AlertandMessages;
import com.cpm.Constants.CommonString;
import com.cpm.GeoTag.GeoTagActivity;
import com.cpm.GeoTag.GeoTagStoreList;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.message.AlertMessage;
import com.cpm.upload.Base64;
import com.crashlytics.android.Crashlytics;
import com.example.deepakp.customcamera.customCamera.CustomCameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CustomCap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class StoreImageActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    ImageView img_cam, img_clicked, img_cam2, img_clicked2;
    Button btn_save;
    String _pathforcheck, _pathforcheck2, _path, str;
    String store_cd, visit_date, username, date, selfie_image;
    private SharedPreferences preferences;
    AlertDialog alert;
    String img_str, img_str2;
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
    Bitmap bmp;
    Bitmap dest;
    int size = 10;
    Runnable run;

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
        img_cam2 = (ImageView) findViewById(R.id.img_selfie2);
        img_clicked2 = (ImageView) findViewById(R.id.img_cam_selfie2);
        btn_save = (Button) findViewById(R.id.btn_save_selfie);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        selfie_image = preferences.getString(CommonString.KEY_SELFIE_IMAGE, null);
        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
        str = CommonString.FILE_PATH;
        database = new GSKDatabase(this);
        database.open();
        coverage_list = database.getCoverageData(date);
        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        img_clicked2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        // Create an instance of GoogleAPIClient
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

        run = new Runnable() {
            @Override
            public void run() {
/*
                if (img_str != null && !img_str.equalsIgnoreCase("")) {
                    File image1 = new File(str + img_str);
                    if (image1.exists()) {
                        image1.delete();
                    }


                }
*/
                if (img_str2 != null && !img_str2.equalsIgnoreCase("")) {
                    File image2 = new File(str + img_str2);
                    if (image2.exists()) {
                        image2.delete();
                    }
                }
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertandMessages.backpressedAlertForStoreImage(StoreImageActivity.this, run);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertandMessages.backpressedAlertForStoreImage(StoreImageActivity.this, run);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_cam_selfie:
                _pathforcheck = store_cd + "_STORE_IMG_SELFIE_" + username + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                startCameraActivity();

                break;
            case R.id.img_cam_selfie2:
                _pathforcheck2 = store_cd + "_STORE_IMG_GROOMING_" + username + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck2;
                startCameraActivity();

                break;
            case R.id.btn_save_selfie:
                //  if (img_str != null && img_str2 != null) {
                if (img_str2 != null) {
                    if (checkNetIsAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreImageActivity.this).setTitle("Parinaam");
                        builder.setMessage("Are you sure you want to save data ?")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                        CoverageBean cdata = new CoverageBean();
                                        cdata.setStoreId(store_cd);
                                        cdata.setVisitDate(visit_date);
                                        cdata.setUserId(username);
                                        cdata.setInTime(getCurrentTime());
                                        cdata.setReason("");
                                        cdata.setReasonid("0");
                                        cdata.setLatitude(lat);
                                        cdata.setLongitude(lon);
                                        cdata.setImage(selfie_image);
                                        cdata.setImage1(img_str2);
                                        cdata.setRemark("");
                                        cdata.setStatus(CommonString.KEY_INVALID);
                                        cdata.setPJPDeviation(flag_deviation);
                                        database.open();
                                        database.InsertCoverageData(cdata);
                                        database.updatecheckoutStore(store_cd, CommonString.KEY_INVALID);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                        editor.commit();

                                        new selfiesUpload(StoreImageActivity.this).execute();
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
                        Snackbar.make(btn_save, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(btn_save, "Please click Grooming Long Shot image", Snackbar.LENGTH_SHORT).show();
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
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck2 != null && !_pathforcheck2.equals("")) {
                    if (new File(str + _pathforcheck2).exists()) {
                        bmp = convertBitmap(str + _pathforcheck2);
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
                                    new FileOutputStream(new File(str + _pathforcheck2)));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        try {
                            bmp = convertBitmap(str + _pathforcheck2);
                            Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / size, bmp.getHeight() / size, true);
                            bmp.recycle();
                            img_cam2.setImageBitmap(bitmapsimplesize);
                            img_clicked2.setVisibility(View.GONE);
                            img_cam2.setVisibility(View.VISIBLE);
                            img_str2 = _pathforcheck2;
                            _pathforcheck2 = "";
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        }

                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
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
                                + "[IMAGE_URL1]" + coverageBeanlist.get(i).getImage1() + "[/IMAGE_URL1]"
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
                final AlertMessage message = new AlertMessage(
                        StoreImageActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "store_checking", ex);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });

            } catch (Exception ex) {
                Crashlytics.logException(ex);
                final AlertMessage message = new AlertMessage(
                        StoreImageActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "store_checking", ex);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });

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

            } else if (!result.equals(CommonString.KEY_SUCCESS)) {
                AlertMessage message = new AlertMessage(StoreImageActivity.this, AlertMessage.MESSAGE_DATA_NOT
                        + result, getResources().getString(R.string.failure), null);
                message.showMessage();

            } else if (!result.equals("")) {

            }

        }

    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public static Bitmap convertBitmap(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options ourOptions = new BitmapFactory.Options();
        ourOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        ourOptions.inDither = false;
        ourOptions.inPurgeable = true;
        ourOptions.inInputShareable = true;
        ourOptions.inTempStorage = new byte[32 * 1024];
        File file = new File(path);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, ourOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
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
