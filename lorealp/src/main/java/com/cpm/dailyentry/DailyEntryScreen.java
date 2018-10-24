package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.GeotaggingBeans;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.download.CompleteDownloadActivity;

import com.cpm.upload.CheckoutNUpload;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyEntryScreen extends AppCompatActivity implements OnItemClickListener, LocationListener {
    GSKDatabase database;
    ArrayList<JourneyPlanGetterSetter> jcplist;
    private SharedPreferences preferences;
    private String date, store_intime;
    ListView lv;
    String store_cd, visit_date;
    private SharedPreferences.Editor editor = null;
    private Dialog dialog;
    String storeVisited = null;
    public static String currLatitude = "0.0";
    public static String currLongitude = "0.0";
    String user_type, username;
    CardView cardView;
    LinearLayout parent_linear, nodata_linear;
    ArrayList<CoverageBean> coverage;
    boolean result_flag = false;
    MyAdapter myAdapter;
    FloatingActionButton fab_button;
    boolean flag_deviation = false;

    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storelist_layout);
        lv = (ListView) findViewById(R.id.list);
        nodata_linear = (LinearLayout) findViewById(R.id.no_data_lay);
        parent_linear = (LinearLayout) findViewById(R.id.parent_linear);
        fab_button = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = new GSKDatabase(this);
        database.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        store_intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        editor = preferences.edit();
        setTitle("Store Name - " + date);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DailyEntryScreen.this);
                builder1.setTitle("Parinaam");
                builder1.setMessage("Do you want to download data")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (checkNetIsAvailable()) {
                                    if (database.isCoverageDataFilled(date)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DailyEntryScreen.this);
                                        builder.setTitle("Parinaam");
                                        builder.setMessage("Please Upload Previous Data First")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent startUpload = new Intent(DailyEntryScreen.this, CheckoutNUpload.class);
                                                        startActivity(startUpload);
                                                        finish();

                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        Intent startDownload = new Intent(getApplicationContext(), CompleteDownloadActivity.class);
                                        startActivity(startDownload);
                                        finish();
                                    }
                                } else {
                                    Snackbar.make(lv, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                                }

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder1.create();
                alert.show();

            }
        });
    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListdata();
    }

    public void setListdata() {
        database.open();
        coverage = database.getCoverageData(date);
        jcplist = database.getJCPData(date);
        if (jcplist.size() > 0) {
            myAdapter = new MyAdapter();
            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(this);
            lv.setVisibility(View.VISIBLE);
            fab_button.setVisibility(View.GONE);
            nodata_linear.setVisibility(View.GONE);
        } else {
            lv.setVisibility(View.GONE);
            parent_linear.setBackgroundColor((getResources().getColor(R.color.grey_light)));
            nodata_linear.setVisibility(View.VISIBLE);
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return jcplist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.storelistrow, null);
                holder.storename = (TextView) convertView.findViewById(R.id.tvstorename);
                holder.city = (TextView) convertView.findViewById(R.id.tvcity);
                holder.keyaccount = (TextView) convertView.findViewById(R.id.tvkeyaccount);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.checkout = (Button) convertView.findViewById(R.id.chkout);
                holder.checkinclose = (ImageView) convertView.findViewById(R.id.closechkin);
                holder.geotag = (ImageView) convertView.findViewById(R.id.geotag);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckNetAvailability()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DailyEntryScreen.this);
                        builder.setMessage("Are you sure you want to Checkout")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        Intent i = new Intent(DailyEntryScreen.this, CheckOutStoreActivity.class);
                                        i.putExtra(CommonString.KEY_STORE_CD, jcplist.get(position).getStore_cd().get(0));
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(lv, "Check internet connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                }
            });
            String storecd = jcplist.get(position).getStore_cd().get(0);
            ArrayList<CoverageBean> coveragespecific = database.getCoverageSpecificData(storecd);
            if (jcplist.get(position).getUploadStatus().get(0).equals(CommonString.KEY_U)) {
                holder.img.setVisibility(View.VISIBLE);
                holder.img.setBackgroundResource(R.drawable.store_tick_u);
                holder.checkout.setVisibility(View.INVISIBLE);

            } else if ((jcplist.get(position).getUploadStatus().get(0).equals(CommonString.KEY_D))) {
                // holder.img.setVisibility(View.INVISIBLE);
                holder.checkinclose.setBackgroundResource(R.drawable.tick_d);
                holder.checkinclose.setVisibility(View.VISIBLE);
                holder.checkout.setVisibility(View.INVISIBLE);
            } else if ((jcplist.get(position).getCheckOutStatus().get(0).equals(CommonString.KEY_C))) {
                holder.checkinclose.setBackgroundResource(R.drawable.tick_c);
                holder.checkinclose.setVisibility(View.VISIBLE);
                holder.checkout.setVisibility(View.INVISIBLE);

            } else if (coveragespecific.size() > 0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                holder.img.setBackgroundResource(R.drawable.leave_tick);
                holder.checkout.setVisibility(View.INVISIBLE);
            } else if (coveragespecific.size() > 0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.KEY_INVALID)) {
                holder.checkout.setVisibility(View.INVISIBLE);
                holder.checkinclose.setBackgroundResource(R.drawable.checkin_ico);
                holder.checkinclose.setVisibility(View.VISIBLE);

            } else if (coveragespecific.size() > 0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.KEY_VALID)) {
                holder.checkout.setBackgroundResource(R.drawable.checkout);
                holder.checkout.setVisibility(View.VISIBLE);
                holder.checkout.setEnabled(true);
            } else {
                holder.checkout.setEnabled(false);
                holder.checkout.setVisibility(View.INVISIBLE);
                holder.geotag.setVisibility(View.INVISIBLE);
                holder.checkinclose.setEnabled(false);
                holder.checkinclose.setVisibility(View.INVISIBLE);
            }

            holder.storename.setText(jcplist.get(position).getStore_name().get(0));
            holder.city.setText(jcplist.get(position).getCity().get(0)+"\n"+jcplist.get(position).getChannel().get(0));
            holder.keyaccount.setText(jcplist.get(position).getKey_account().get(0));
            return convertView;
        }

        private class ViewHolder {
            TextView storename, city, keyaccount;
            ImageView img, checkinclose, geotag;
            Button checkout;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        store_cd = jcplist.get(position).getStore_cd().get(0);
        final String upload_status = jcplist.get(position).getUploadStatus().get(0);
        final String checkoutstatus = jcplist.get(position).getCheckOutStatus().get(0);
        final String geotag = jcplist.get(position).getGeotagStatus().get(0);

       if (upload_status.equals(CommonString.KEY_U)) {
            Snackbar.make(lv, "All Data Uploaded", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        } else if (((checkoutstatus.equals(CommonString.KEY_C)))) {
            Snackbar.make(lv, "Store already checked out", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        } /*else if (isStoreCoverageLeave(store_cd)) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Parinaam");
            builder1.setMessage("Want to enter store, it is already closed. \nData will be deleted.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            ArrayList<CoverageBean> coveragespecific = database.getCoverageSpecificData(store_cd);
                            String reason_cd = coveragespecific.get(0).getReasonid();
                            String entry_allow = database.getNonEntryAllowReasonData(reason_cd);

                            if (entry_allow.equals("0")) {
                                database.deleteAllCoverage();
                                setListdata();
                            } else {
                                database.deleteSpecificCoverage(store_cd);
                                setListdata();
                            }

                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder1.create();
            alert.show();
        }*/ else {
            if (!setcheckedmenthod(store_cd)) {
                boolean enteryflag = true;
                if (coverage.size() > 0) {
                    int i;
                    for (i = 0; i < coverage.size(); i++) {
                        if (coverage.get(i).getInTime() != null) {
                            if (coverage.get(i).getOutTime() == null) {
                                if (!store_cd.equals(coverage.get(i).getStoreId())) {
                                    Snackbar.make(lv, "Please checkout from current store", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                    enteryflag = false;
                                }
                                break;
                            }
                        }
                    }
                }

                if (enteryflag) {
                    showMyDialog(store_cd, jcplist.get(position).getStore_name().get(0), "Yes",
                            jcplist.get(position).getVISIT_DATE().get(0),
                            jcplist.get(position).getCheckOutStatus().get(0),
                            jcplist.get(position).getKeyaccount_cd().get(0),
                            jcplist.get(position).getCity_cd().get(0),
                            jcplist.get(position).getStoretype_cd().get(0),
                            jcplist.get(position).getGeotagStatus().get(0),
                            jcplist.get(position).getChannel_cd().get(0),
                            jcplist.get(position).getFloor_status().get(0),
                            jcplist.get(position).getBackroom_status().get(0));
                }
            } else {
                Snackbar.make(lv, "Data already filled ", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }
    }


    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());


        return cdate;

    }

    public boolean CheckNetAvailability() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            // we are connected to a network
            connected = true;
        }
        return connected;
    }

    void showMyDialog(final String storeCd, final String storeName, final String status, final String visitDate, final String checkout_status,
                      final String keyaccount_cd, final String city_cd, final String store_ype_cd, final String geotag,final String channel_cd,
                      final  String floor_status,final  String backroom_status) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);

       final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogrpvisit);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes) {

                    database.open();
                    geotaglist = database.getinsertGeotaggingData(storeCd);
                    if (geotag.equals("Y") || geotaglist.size() > 0) {
                        editor = preferences.edit();
                        editor.putString(CommonString.KEY_STOREVISITED, storeCd);
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                        editor.putString(CommonString.KEY_STORE_NAME, storeName);
                        editor.putString(CommonString.KEY_KEYACCOUNT_CD, keyaccount_cd);
                        editor.putString(CommonString.KEY_CITY_CD, city_cd);
                        editor.putString(CommonString.KEY_STORETYPE_CD, store_ype_cd);
                        editor.putString(CommonString.KEY_STORE_CD, storeCd);
                        editor.putString(CommonString.KEY_CHANNEL_CD, channel_cd);
                        editor.putString(CommonString.KEY_FLOOR_STATUS, floor_status);
                        editor.putString(CommonString.KEY_BACKROOK_STATUS, backroom_status);

                        if (status.equals("Yes")) {
                            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                        }
                        editor.commit();

                        if (store_intime.equalsIgnoreCase("")) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(CommonString.KEY_STORE_IN_TIME, getCurrentTime());
                            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                            editor.commit();
                        }
                        dialog.cancel();
                        boolean flag = true;
                        if (coverage.size() > 0) {
                            for (int i = 0; i < coverage.size(); i++) {
                                if (store_cd.equals(coverage.get(i).getStoreId())) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag == true) {
                            Intent in = new Intent(DailyEntryScreen.this, SelfieActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {

                        Intent in = new Intent(DailyEntryScreen.this, StoreEntry.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                        }

                    } else {
                        Snackbar.make(radioGroup, "Please do the Geotag first", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                } else if (checkedId == R.id.no) {
                    dialog.cancel();
                    if (checkout_status.equals(CommonString.KEY_INVALID) || checkout_status.equals(CommonString.KEY_VALID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DailyEntryScreen.this);
                        builder.setMessage(CommonString.DATA_DELETE_ALERT_MESSAGE)
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        UpdateData(storeCd);

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(CommonString.KEY_STORE_CD, storeCd);
                                        editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                                        editor.putString(CommonString.KEY_STOREVISITED, "");
                                        editor.putString(CommonString.KEY_KEYACCOUNT_CD, keyaccount_cd);
                                        editor.putString(CommonString.KEY_CITY_CD, city_cd);
                                        editor.putString(CommonString.KEY_STORETYPE_CD, store_ype_cd);
                                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                        editor.putString(CommonString.KEY_CHANNEL_CD, channel_cd);
                                        editor.putString(CommonString.KEY_FLOOR_STATUS, floor_status);
                                        editor.putString(CommonString.KEY_BACKROOK_STATUS, backroom_status);

                                        editor.commit();
                                        Intent in = new Intent(DailyEntryScreen.this, NonWorkingReason.class);
                                        startActivity(in);
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        UpdateData(storeCd);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(CommonString.KEY_STORE_CD, storeCd);
                        editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                        editor.putString(CommonString.KEY_STOREVISITED, "");
                        editor.putString(CommonString.KEY_KEYACCOUNT_CD, keyaccount_cd);
                        editor.putString(CommonString.KEY_CITY_CD, city_cd);
                        editor.putString(CommonString.KEY_STORETYPE_CD, store_ype_cd);
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                        editor.putString(CommonString.KEY_CHANNEL_CD, channel_cd);
                        editor.putString(CommonString.KEY_FLOOR_STATUS, floor_status);
                        editor.putString(CommonString.KEY_BACKROOK_STATUS, backroom_status);
                        editor.commit();
                        Intent in = new Intent(DailyEntryScreen.this, NonWorkingReason.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                }
            }

        });


        dialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        currLatitude = Double.toString(location.getLatitude());
        currLongitude = Double.toString(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public void UpdateData(String storeCd) {
        database.open();
        database.deleteSpecificStoreData(storeCd);
        database.updateStoreStatusOnCheckout(storeCd, jcplist.get(0).getVISIT_DATE().get(0), "N");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean setcheckedmenthod(String store_cd) {
        for (int i = 0; i < coverage.size(); i++) {
            if (store_cd.equals(coverage.get(i).getStoreId())) {
                if (coverage.get(i).getOutTime() != null) {
                    result_flag = true;
                    break;
                }
            } else {
                result_flag = false;
            }
        }
        return result_flag;
    }


    public boolean isStoreCoverageLeave(String store_cd) {
        boolean result = false;
        for (int i = 0; i < coverage.size(); i++) {

            if (store_cd.equals(coverage.get(i).getStoreId())) {
                if (coverage.get(i).getStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                    result = true;
                    break;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    public boolean isStoreInvalid() {
        boolean flag_is_invalid = false;

        for (int i = 0; i < coverage.size(); i++) {

            if (coverage.get(i).getStatus().equals(CommonString.KEY_INVALID)) {
                flag_is_invalid = true;
                break;
            }
        }

        return flag_is_invalid;
    }

    public boolean isPJPDeviationInvalid() {
        boolean flag_is_invalid = false;

        for (int i = 0; i < coverage.size(); i++) {

            if (coverage.get(i).getStatus().equals(CommonString.KEY_INVALID) || coverage.get(i).getStatus().equals(CommonString.KEY_VALID)) {
                if (coverage.get(i).isPJPDeviation())
                    flag_is_invalid = true;

                break;
            }
        }

        return flag_is_invalid;
    }

}
