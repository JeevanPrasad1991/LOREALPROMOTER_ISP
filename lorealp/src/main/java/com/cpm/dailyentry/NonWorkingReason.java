package com.cpm.dailyentry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.cpm.Constants.CommonString;

import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.NonWorkingReasonGetterSetter;


public class NonWorkingReason extends AppCompatActivity implements OnItemSelectedListener, OnClickListener {
    private Spinner reasonspinner;
    private GSKDatabase database;
    String reasonname, reasonid, entry_allow, image, reason_reamrk, intime;
    Button save;
    protected String _path, str;
    protected String _pathforcheck = "";
    private String image1 = "";
    private SharedPreferences preferences;
    String _UserId, visit_date, store_id;
    protected boolean status = true;
    EditText text;
    AlertDialog alert;
    ImageButton camera;
    RelativeLayout reason_lay, rel_cam;
    private ArrayAdapter<CharSequence> reason_adapter;
    ArrayList<NonWorkingReasonGetterSetter> reasondata = new ArrayList<>();
    ArrayList<JourneyPlanGetterSetter> jcp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nonworking);
        reasonspinner = (Spinner) findViewById(R.id.spinner2);
        camera = (ImageButton) findViewById(R.id.imgcam);
        save = (Button) findViewById(R.id.save);
        text = (EditText) findViewById(R.id.reasontxt);
        reason_lay = (RelativeLayout) findViewById(R.id.layout_reason);
        rel_cam = (RelativeLayout) findViewById(R.id.relimgcam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        store_id = preferences.getString(CommonString.KEY_STORE_CD, "");
        database = new GSKDatabase(this);
        database.open();
        //remove
        jcp = database.getJCPData(visit_date);
        if (jcp.size() > 0) {
            try {
                for (int i = 0; i < jcp.size(); i++) {
                    boolean flag = false;
                    if (jcp.get(i).getUploadStatus().get(0).equals(CommonString.KEY_U) ||
                            jcp.get(i).getUploadStatus().get(0).equals(CommonString.KEY_D)
                            || jcp.get(i).getUploadStatus().get(0).equals(CommonString.STORE_STATUS_LEAVE)) {
                        flag = true;
                        reasondata.clear();
                        reasondata = database.getNonWorkingData(flag);
                        break;
                    } else {
                        reasondata = database.getNonWorkingData(flag);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        str = CommonString.FILE_PATH;
        intime = getCurrentTime();
        camera.setOnClickListener(this);
        save.setOnClickListener(this);
        reason_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        reason_adapter.add("-Select Reason-");
        for (int i = 0; i < reasondata.size(); i++) {
            reason_adapter.add(reasondata.get(i).getReason().get(0));
        }
        reasonspinner.setAdapter(reason_adapter);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonspinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.spinner2:
                if (position != 0) {
                    reasonname = reasondata.get(position - 1).getReason().get(0);
                    reasonid = reasondata.get(position - 1).getReason_cd().get(0);
                    entry_allow = reasondata.get(position - 1).getEntry_allow().get(0);
                    if (reasonname.equalsIgnoreCase("Store Closed")) {
                        rel_cam.setVisibility(View.VISIBLE);
                        image = "true";
                    } else {
                        rel_cam.setVisibility(View.GONE);
                        image = "false";
                    }
                    reason_reamrk = "true";
                    if (reason_reamrk.equalsIgnoreCase("true")) {
                        reason_lay.setVisibility(View.VISIBLE);
                    } else {
                        reason_lay.setVisibility(View.GONE);
                    }
                } else {
                    reasonname = "";
                    reasonid = "";
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
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

                        //jee
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
                        Bitmap dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
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
//endjee

                        camera.setImageDrawable(getResources().getDrawable(R.drawable.camera_green));
                        image1 = _pathforcheck;
                    }
                }
                break;
        }

    }

    public boolean imageAllowed() {
        boolean result = true;
        if (image.equalsIgnoreCase("true")) {
            if (image1.equalsIgnoreCase("")) {
                result = false;

            }
        }
        return result;
    }

    public boolean textAllowed() {
        boolean result = true;
        if (text.getText().toString().equalsIgnoreCase("")) {
            result = false;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgcam) {
            _pathforcheck = store_id + "_NONWORKING_IMG_" + _UserId + ".jpg";
            _path = CommonString.FILE_PATH + _pathforcheck;
            startCameraActivity();
        }
        if (v.getId() == R.id.save) {
            if (validatedata()) {
                if (imageAllowed()) {
                    if (textAllowed()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NonWorkingReason.this);
                        builder.setMessage("Do you want to save the data ")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                        if (entry_allow.equals("0")) {
                                            database.deleteAllTables();
                                            jcp = database.getJCPData(visit_date);
                                            for (int i = 0; i < jcp.size(); i++) {
                                                String stoteid = jcp.get(i).getStore_cd().get(0);
                                                CoverageBean cdata = new CoverageBean();
                                                cdata.setStoreId(stoteid);
                                                cdata.setVisitDate(visit_date);
                                                cdata.setUserId(_UserId);
                                                cdata.setInTime(intime);
                                                cdata.setOutTime(getCurrentTime());
                                                cdata.setReason(reasonname);
                                                cdata.setReasonid(reasonid);
                                                cdata.setLatitude("0.0");
                                                cdata.setLongitude("0.0");
                                                cdata.setImage(image1);
                                                cdata.setRemark(text.getText().toString().replaceAll("[&^<>{}'$]", " "));
                                                cdata.setStatus(CommonString.STORE_STATUS_LEAVE);
                                                database.InsertCoverageData(cdata);
                                                database.updateStoreStatusOnLeave(store_id, visit_date, CommonString.STORE_STATUS_LEAVE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString(CommonString.KEY_STOREVISITED_STATUS + stoteid, "No");
                                                editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                                editor.commit();
                                            }

                                        } else {
                                            CoverageBean cdata = new CoverageBean();
                                            cdata.setStoreId(store_id);
                                            cdata.setVisitDate(visit_date);
                                            cdata.setUserId(_UserId);
                                            cdata.setInTime(intime);
                                            cdata.setOutTime(getCurrentTime());
                                            cdata.setReason(reasonname);
                                            cdata.setReasonid(reasonid);
                                            cdata.setLatitude("0.0");
                                            cdata.setLongitude("0.0");
                                            cdata.setImage(image1);
                                            cdata.setRemark(text.getText().toString().replaceAll("[&^<>{}'$]", " "));
                                            cdata.setStatus(CommonString.STORE_STATUS_LEAVE);
                                            database.InsertCoverageData(cdata);
                                            database.updateStoreStatusOnLeave(store_id, visit_date, CommonString.STORE_STATUS_LEAVE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString(CommonString.KEY_STOREVISITED_STATUS + store_id, "No");
                                            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                            editor.commit();
                                        }
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });

                        alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(save, "Please enter required remark reason", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(save, "Please Capture Image", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(save, "Please Select a Reason", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validatedata() {
        boolean result = false;
        if (reasonid != null && !reasonid.equalsIgnoreCase("")) {
            result = true;
        }
        return result;

    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

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
}
