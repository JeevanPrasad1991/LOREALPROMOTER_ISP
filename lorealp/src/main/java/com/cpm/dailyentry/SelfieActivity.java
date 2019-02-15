package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cpm.Constants.AlertandMessages;
import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelfieActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_cam, img_clicked;
    Button btn_save;
    String _pathforcheck, _path, str;
    String store_cd, visit_date, username, date;
    String selfie_imge;
    AlertDialog alert;
    String img_str;
    private GSKDatabase database;
    Bitmap bmp;
    Bitmap dest;
    int size = 10;
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    Runnable run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
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
        str = CommonString.FILE_PATH;
        database = new GSKDatabase(this);
        database.open();
        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        run = new Runnable() {
            @Override
            public void run() {
                if (selfie_imge != null && !selfie_imge.equalsIgnoreCase("")) {
                    File image1 = new File(str + selfie_imge);
                    if (image1.exists()) {
                        image1.delete();
                    }
                }
            }
        };


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

            case R.id.btn_save_selfie:
                if (img_str != null) {
                    if (checkNetIsAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SelfieActivity.this).setTitle("Parinaam");
                        builder.setMessage("Are you sure you want to save data ?")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                        editor = preferences.edit();
                                        editor.putString(CommonString.KEY_SELFIE_IMAGE, img_str);
                                        editor.commit();
                                        Intent in = new Intent(SelfieActivity.this, StoreImageActivity.class);
                                        startActivity(in);
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
                        Snackbar.make(btn_save, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(btn_save, "Please click Selfie image", Snackbar.LENGTH_SHORT).show();
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

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
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
                            dest.compress(Bitmap.CompressFormat.JPEG, 90,
                                    new FileOutputStream(new File(str + _pathforcheck)));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        try {
                            bmp = convertBitmap(str + _pathforcheck);
                            Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / size, bmp.getHeight() / size, true);
                            bmp.recycle();
                            img_cam.setImageBitmap(bitmapsimplesize);
                            img_clicked.setVisibility(View.GONE);
                            img_cam.setVisibility(View.VISIBLE);
                            img_str = _pathforcheck;
                            _pathforcheck = "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public void onBackPressed() {

        AlertandMessages.backpressedAlertForStoreImage(SelfieActivity.this, run);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertandMessages.backpressedAlertForStoreImage(SelfieActivity.this, run);
        }
        return super.onOptionsItemSelected(item);
    }


}
