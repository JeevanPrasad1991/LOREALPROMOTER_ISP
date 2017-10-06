package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.delegates.TableBean;
import com.cpm.himalaya.R;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.Audit_QuestionGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlHandler.XMLHandlers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyEntryScreen extends AppCompatActivity implements OnItemClickListener, LocationListener {

    Button pjpdeviation, callcycle;
    GSKDatabase database;
    ArrayList<JourneyPlanGetterSetter> jcplist;
    private SharedPreferences preferences;
    private String date, store_intime;
    ListView lv;
    String store_cd;
    private SharedPreferences.Editor editor = null;
    private Dialog dialog;
    String storeVisited = null;
    public static String currLatitude = "0.0";
    public static String currLongitude = "0.0";
    String user_type, username;
    CardView cardView;
    LinearLayout parent_linear, nodata_linear;
    ArrayList<CoverageBean> coverage;
    boolean result_flag = false, leaveflag = false;
    MyAdapter myAdapter;

    FloatingActionButton fab_button;
    JourneyPlanGetterSetter jcpgettersetter;

    boolean flag_deviation = false;

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

        setTitle("Store List - " + date);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(database.isSkuMasterDownloaded()){

                    if(isStoreInvalid()){
                        Snackbar.make(lv, "Please checkout from current store", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                    else{

                        if(database.getPJPDeviationData(date).size()>0){

                            showPJPDeviationStoreList();
                        }
                        else {
                            new PJPDownloadTask(DailyEntryScreen.this).execute();
                        }

                    }
                }
                else{
                    Snackbar.make(lv, "Please download data first", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    public void showPJPDeviationStoreList(){


        flag_deviation = true;
        jcplist = database.getPJPDeviationData(date);
        setCheckOutData();
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(DailyEntryScreen.this);
        lv.setVisibility(View.VISIBLE);
        setTitle("PJP Deviation " + date);
        fab_button.setVisibility(View.GONE);
        nodata_linear.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setListdata();
    }

    public void setListdata() {


        coverage = database.getCoverageData(date);

        if(database.isSkuMasterDownloaded()){
            if(isPJPDeviationInvalid()){
                //new PJPDownloadTask(DailyEntryScreen.this).execute();
                showPJPDeviationStoreList();
            }
            else{

                fab_button.setVisibility(View.VISIBLE);
                setTitle("Store List " + date);

                jcplist = database.getJCPData(date);

                if (jcplist.size() > 0) {
                    setCheckOutData();

                    myAdapter = new MyAdapter();
                    lv.setAdapter(myAdapter);
                    lv.setOnItemClickListener(this);
                    flag_deviation = false;

                } else{
                    lv.setVisibility(View.GONE);
                    parent_linear.setBackgroundColor((getResources().getColor(R.color.grey_light)));
                    nodata_linear.setVisibility(View.VISIBLE);
                }
            }
        }
        else{
            lv.setVisibility(View.GONE);
            parent_linear.setBackgroundColor((getResources().getColor(R.color.grey_light)));
            nodata_linear.setVisibility(View.VISIBLE);
        }

    }

    public void setCheckOutData() {
        for (int i = 0; i < jcplist.size(); i++) {
            String storeCd = jcplist.get(i).getStore_cd().get(0);

            if (!jcplist.get(i).getCheckOutStatus().get(0).equals(CommonString.KEY_C)
                    && !jcplist.get(i).getCheckOutStatus().get(0).equals(CommonString.KEY_VALID)) {

                /*if (database.isOpeningDataFilled(storeCd) && database.isPromotionDataFilled(storeCd)
                        && database.isAssetDataFilled(storeCd)) {*/

                boolean flag = true;

                if (database.getStockAvailabilityData(storeCd).size() > 0) {
                    if (database.isOpeningDataFilled(storeCd)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }

                if (flag) {
                    if (database.getPromotionBrandData(storeCd).size() > 0) {
                        if (database.isPromotionDataFilled(storeCd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                }

                if (flag) {
                    if (database.isStoreAssetDataFilled(storeCd)) {
                        if (database.getAssetCategoryData(storeCd).size() > 0) {
                            if (database.isAssetDataFilled(storeCd)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        }
                    }
                }

                if (flag) {

                    ArrayList<Audit_QuestionGetterSetter> cat_list = database.getCategoryQuestionData();

                    boolean audit_flag = true;

                    for (int j = 0; j < cat_list.size(); j++) {
                        if (database.getAuditQuestionData(cat_list.get(j).getCATEGORY_ID().get(0)).size() > 0) {
                            if (!database.isAuditDataFilled(storeCd, cat_list.get(j).getCATEGORY_ID().get(0))) {
                                audit_flag = false;
                                break;
                            }
                        }

                    }
                    if (audit_flag) {
                        flag = true;
                    } else {
                        flag = false;
                    }

                }

                if (flag) {
                    if (user_type.equals("Promoter")) {
                        if (database.isMiddayDataFilled(storeCd) && database.isClosingDataFilled(storeCd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                }


                if (flag) {

                    database.updateCoverageStatusNew(storeCd,CommonString.KEY_VALID);

                    if(flag_deviation){
                        database.updateDeviationStoreStatusOnCheckout(storeCd, date, CommonString.KEY_VALID);
                        jcplist = database.getPJPDeviationData(date);
                    }
                    else{
                        database.updateStoreStatusOnCheckout(storeCd, date, CommonString.KEY_VALID);
                        jcplist = database.getJCPData(date);
                    }

                }
                //}

                //<editor-fold desc="Previous Code">
                /*if (database.isOpeningDataFilled(storeCd) && database.getFacingCompetitorData(storeCd).size() > 0
                        && database.getPOIData(storeCd).size() > 0 && database.getCompetitionPOIData(storeCd).size() > 0
                        && database.getCompetitionPromotionData(storeCd).size() > 0) {

                    boolean flag = true;

                    if (database.getPromotionBrandData(storeCd).size() > 0) {
                        if (database.isPromotionDataFilled(storeCd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }

                    if (flag) {
                        if (user_type.equals("Promoter")) {
                            if (database.isClosingDataFilled(storeCd) && database.isMiddayDataFilled(storeCd)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        }
                    }

                    if (flag) {
                        if (database.getAssetCategoryData(storeCd).size() > 0) {
                            if (database.isAssetDataFilled(storeCd)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        }
                    }

                    if (flag) {
                        database.updateStoreStatusOnCheckout(storeCd, date, CommonString.KEY_VALID);
                        jcplist = database.getJCPData(date);
                    }
                }*/
                //</editor-fold>
            }
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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DailyEntryScreen.this);
                    builder.setMessage("Are you sure you want to Checkout")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {

                                    if (CheckNetAvailability()) {
                                        editor = preferences.edit();
                                        editor.putString(CommonString.KEY_STORE_CD, jcplist.get(position).getStore_cd().get(0));
                                        editor.putString(CommonString.KEY_STORE_NAME, jcplist.get(position).getStore_name().get(0));
                                        editor.commit();

                                        Intent i = new Intent(DailyEntryScreen.this, CheckOutStoreActivity.class);
                                        i.putExtra(CommonString.KEY_PJP_DEVIATION, flag_deviation);
                                        startActivity(i);
                                    } else {
                                        Snackbar.make(lv, "No Network", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                        //Toast.makeText(DailyEntryScreen.this, "No Network", Toast.LENGTH_SHORT).show();
                                    }

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
                }
            });

            String storecd = jcplist.get(position).getStore_cd().get(0);
            ArrayList<CoverageBean> coveragespecific = database.getCoverageSpecificData(storecd);


            if (jcplist.get(position).getUploadStatus().get(0).equals(CommonString.KEY_U)) {

                holder.img.setVisibility(View.VISIBLE);
                holder.img.setBackgroundResource(R.drawable.tick_u);
                holder.checkout.setVisibility(View.INVISIBLE);

            }
            // else if (preferences.getString(CommonString.KEY_STOREVISITED_STATUS + storecd, "").equals("No"))
            //else if (coverage.get(position).getStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE))
            //else if (isStoreCoverageLeave(storecd)) {
            else if (coveragespecific.size()>0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                holder.img.setBackgroundResource(R.drawable.leave_tick);
                holder.checkout.setVisibility(View.INVISIBLE);

            } else if ((jcplist.get(position).getCheckOutStatus().get(0).equals(CommonString.KEY_C))) {

                holder.checkinclose.setBackgroundResource(R.drawable.tick_c);
                holder.checkinclose.setVisibility(View.VISIBLE);
                holder.checkout.setVisibility(View.INVISIBLE);

            } else if (coveragespecific.size()>0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.KEY_VALID)) {

                holder.checkout.setBackgroundResource(R.drawable.checkout);
                holder.checkout.setVisibility(View.VISIBLE);
                holder.checkout.setEnabled(true);

            } else if (coveragespecific.size()>0 && coveragespecific.get(0).getStatus().equalsIgnoreCase(CommonString.KEY_INVALID)) {

                holder.checkout.setVisibility(View.INVISIBLE);
                holder.checkinclose.setBackgroundResource(R.drawable.checkin_ico);
                holder.checkinclose.setVisibility(View.VISIBLE);
                //	holder.checkinclose.setEnabled(false);
                //holder.checkinclose.setBackgroundResource(R.drawable.close);
                //.checkinclose.setVisibility(View.VISIBLE);

            } else {

                holder.checkout.setEnabled(false);
                holder.checkout.setVisibility(View.INVISIBLE);
                holder.checkinclose.setEnabled(false);
                holder.checkinclose.setVisibility(View.INVISIBLE);
            }
            holder.storename.setText(jcplist.get(position).getStore_name().get(0));
            holder.city.setText(jcplist.get(position).getCity().get(0));
            holder.keyaccount.setText(jcplist.get(position).getKey_account().get(0));


			/*if (list.get(position).getStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                holder.imgtick.setVisibility(View.VISIBLE);
				holder.imgtick.setBackgroundResource(R.drawable.leave_tick);
			}*/

            return convertView;
        }

        private class ViewHolder {
            TextView storename, city, keyaccount;
            ImageView img, checkinclose;

            Button checkout;
        }
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);*/

        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        store_cd = jcplist.get(position).getStore_cd().get(0);
        //final String store_cd = jcplist.get(position).getStore_cd().get(0);
        final String upload_status = jcplist.get(position).getUploadStatus().get(0);
        final String checkoutstatus = jcplist.get(position).getCheckOutStatus().get(0);

        if (upload_status.equals(CommonString.KEY_U)) {
            Snackbar.make(lv, "All Data Uploaded", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            //Toast.makeText(getApplicationContext(), "All Data Uploaded", Toast.LENGTH_SHORT).show();

        } else if (((checkoutstatus.equals(CommonString.KEY_C)))) {
            Snackbar.make(lv, "Store already checked out", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            //Toast.makeText(getApplicationContext(), "Store Checkout", Toast.LENGTH_SHORT).show();

        } else if (isStoreCoverageLeave(store_cd)) {

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
                                //jcplist = database.getJCPData(date);
                                setListdata();
                            } else {
                                // jcplist = database.getJCPData(date);
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

            //Snackbar.make(lv, "Store Already Closed", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            //Toast.makeText(getApplicationContext(), "Store Already Closed", Toast.LENGTH_SHORT).show();

        } else {
            /*if(jcplist.get(position).getCategory_type().get(0).equals("Food")){
                editor.putBoolean(CommonString.KEY_FOOD_STORE, true);
			}else{
				editor.putBoolean(CommonString.KEY_FOOD_STORE, false);
			}
			editor.commit();*/


            //Previous code commit gagan

            /*if (preferences.getString(CommonString.KEY_STOREVISITED_STATUS, "").equals("Yes")) {

                if (!preferences.getString(CommonString.KEY_STOREVISITED, "").equals(store_cd)) {

                    Snackbar.make(lv, "Please checkout from current store", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    //Toast.makeText(getApplicationContext(), "Please checkout from current store", Toast.LENGTH_SHORT).show();
                } else {
//                	editor.putString(CommonString.KEY_STORE_CD, store_cd+"");
//                    editor.putString(CommonString.KEY_STORE_NAME, jcplist.get(position).getStore_name().get(0));
//					editor.commit();

                    showMyDialog(store_cd, jcplist.get(position).getStore_name().get(0), "No", "",
                            jcplist.get(position).getCheckOutStatus().get(0));
                }

            } else {

                // PUT IN PREFERENCES
//                editor = preferences.edit();
//                editor.putString(CommonString.KEY_STORE_CD, store_cd);
//				editor.putString(CommonString.KEY_STORE_NAME, jcplist.get(position).getStore_name().get(0));
//				editor.putString(CommonString.KEY_VISIT_DATE, jcplist.get(position).getVISIT_DATE().get(0));
//
//				editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
//				editor.commit();

                showMyDialog(store_cd, jcplist.get(position).getStore_name().get(0), "Yes",
                        jcplist.get(position).getVISIT_DATE().get(0),
                        jcplist.get(position).getCheckOutStatus().get(0));
            }*/


            if (!setcheckedmenthod(store_cd)) {
                boolean enteryflag = true;

                if (coverage.size() > 0) {
                    int i;
                    for (i = 0; i < coverage.size(); i++) {
                        if (coverage.get(i).getInTime() != null) {

                            if (coverage.get(i).getOutTime() == null) {
                                if (!store_cd.equals(coverage.get(i).getStoreId())) {
                                    Snackbar.make(lv, "Please checkout from current store", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
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
                            jcplist.get(position).getCheckOutStatus().get(0));
                }
            } else {
                Snackbar.make(lv, "Data already filled ", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }


        //Toast.makeText(getApplicationContext(), store_name, Toast.LENGTH_SHORT).show();
        /*	Intent in=new Intent(getApplicationContext(),StoreEntry.class);

		startActivity(in);
		overridePendingTransition(R.anim.activity_in, R.anim.activity_out);*/
    }

    public String getCurrentTimeX() {

        Calendar m_cal = Calendar.getInstance();
        int hour = m_cal.get(Calendar.HOUR_OF_DAY);
        int min = m_cal.get(Calendar.MINUTE);

        String intime = "";

        if (hour == 0) {
            intime = "" + 12 + ":" + min + " AM";
        } else if (hour == 12) {
            intime = "" + 12 + ":" + min + " PM";
        } else {

            if (hour > 12) {
                hour = hour - 12;
                intime = "" + hour + ":" + min + " PM";
            } else {
                intime = "" + hour + ":" + min + " AM";
            }
        }
        return intime;
    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

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

    void showMyDialog(final String storeCd, final String storeName, final String status,
                      final String visitDate, final String checkout_status) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);
        // dialog.setTitle("About Android Dialog Box");


        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogrpvisit);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                if (checkedId == R.id.yes) {
                    /*Toast.makeText(getApplicationContext(), "choice: Yes",Toast.LENGTH_SHORT).show();*/

                    editor = preferences.edit();
                    editor.putString(CommonString.KEY_STOREVISITED, storeCd);
                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                    editor.putString(CommonString.KEY_LATITUDE, currLatitude);
                    editor.putString(CommonString.KEY_LONGITUDE, currLongitude);
                    editor.putString(CommonString.KEY_STORE_NAME, storeName);
                    editor.putString(CommonString.KEY_STORE_CD, storeCd);

                    if (!visitDate.equals("")) {
                        editor.putString(CommonString.KEY_VISIT_DATE, visitDate);
                    }

                    if (status.equals("Yes")) {
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                    }

                    //database.updateStoreStatusOnCheckout(storeCd, date, CommonString.KEY_INVALID);

                    editor.commit();

                    if (store_intime.equalsIgnoreCase("")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(CommonString.KEY_STORE_IN_TIME, getCurrentTime());
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                        //editor.putString(CommonString.KEY_STOREVISITED, store_id);
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
                        Intent in = new Intent(DailyEntryScreen.this, StoreImageActivity.class);
                        in.putExtra(CommonString.KEY_PJP_DEVIATION, flag_deviation);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else {
                        Intent in = new Intent(DailyEntryScreen.this, StoreEntry.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }

                    /*Intent in = new Intent(DailyEntryScreen.this, StoreImageActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);*/
                    //finish();

                } else if (checkedId == R.id.no) {
                    /*Toast.makeText(getApplicationContext(), "choice: No",Toast.LENGTH_SHORT).show();*/
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
                                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                        editor.putString(CommonString.KEY_LATITUDE, "");
                                        editor.putString(CommonString.KEY_LONGITUDE, "");
                                        editor.commit();


                                        Intent in = new Intent(DailyEntryScreen.this, NonWorkingReason.class);
                                        startActivity(in);
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
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                        editor.putString(CommonString.KEY_LATITUDE, "");
                        editor.putString(CommonString.KEY_LONGITUDE, "");
                        editor.commit();

                        Intent in = new Intent(DailyEntryScreen.this, NonWorkingReason.class);
                        startActivity(in);
                    }
                    //finish();
                }
            }

        });

		/*RadioButton yes = (RadioButton)  dialog.findViewById(R.id.yes);
        RadioButton no = (RadioButton)  dialog.findViewById(R.id.no);*/

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


		/*database.updateStoreStatusOnLeave(store_cd, visit_date,
                CommonString.KEY_N);*/

        database.updateStoreStatusOnCheckout(storeCd, jcplist.get(0).getVISIT_DATE().get(0),
                "N");

		/*Intent in  = new Intent(DailyEntryScreen.this, NonWorkingReason.class);
        startActivity(in);*/

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

            // NavUtils.navigateUpFromSameTask(this);
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


                //	Snackbar.make(lv, " Data Filled", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                //

            } else {
                //	showMyDialog(store_cd, jcplist.get(position).getStore_name().get(0), "Yes", jcplist.get(position).getVISIT_DATE().get(0), jcplist.get(position).getCheckOutStatus().get(0));

                result_flag = false;

                //	break;
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

    public boolean isStoreInvalid(){
        boolean flag_is_invalid = false;

        for(int i=0; i<coverage.size();i++){

            if(coverage.get(i).getStatus().equals(CommonString.KEY_INVALID)){
                flag_is_invalid = true;
                break;
            }
        }

        return flag_is_invalid;
    }

    public boolean isPJPDeviationInvalid(){
        boolean flag_is_invalid = false;

        for(int i=0; i<coverage.size();i++){

            if(coverage.get(i).getStatus().equals(CommonString.KEY_INVALID) || coverage.get(i).getStatus().equals(CommonString.KEY_VALID) ){
                if(coverage.get(i).isPJPDeviation())
                flag_is_invalid = true;

                break;
            }
        }

        return flag_is_invalid;
    }

    //Download PJP Deviation JCP

    private ProgressBar pb;
    private TextView percentage, message;

    Data data;
    int eventType;
    String str;

    boolean ResultFlag = true;

    private class PJPDownloadTask extends AsyncTask<Void, Data, String> {
        private Context context;

        PJPDownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom);
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resultHttp = "";
                data = new Data();

                data.value = 10;
                data.name = "PJP Deviation Downloading";
                publishProgress(data);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", username);
                request.addProperty("Type", "JOURNEY_DEVIATION");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result = envelope.getResponse();

                if (result.toString() != null) {
                    //InputStream stream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));

                    xpp.setInput(new StringReader(result.toString()));
                    // xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    // xpp.setInput(stream,"UTF-8");
                    xpp.next();
                    eventType = xpp.getEventType();

                    jcpgettersetter = XMLHandlers.JCPXMLHandler(xpp, eventType);

                    if (jcpgettersetter.getStore_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;

                    } else {
                        return "JOURNEY_DEVIATION";
                    }

                    data.value = 10;
                    data.name = "PJP Deviation Data Downloading";
                }
                publishProgress(data);

                database.open();
                database.insertPJPDeviationData(jcpgettersetter);

            } catch (MalformedURLException e) {

                ResultFlag = false;
                str = AlertMessage.MESSAGE_EXCEPTION;
                return AlertMessage.MESSAGE_EXCEPTION;
            } catch (SocketTimeoutException e) {
                ResultFlag = false;
                str = AlertMessage.MESSAGE_SOCKETEXCEPTION;
                return AlertMessage.MESSAGE_SOCKETEXCEPTION;
            } catch (InterruptedIOException e) {

                ResultFlag = false;
                str = AlertMessage.MESSAGE_EXCEPTION;
                return AlertMessage.MESSAGE_EXCEPTION;

            } catch (IOException e) {

                ResultFlag = false;
                str = AlertMessage.MESSAGE_SOCKETEXCEPTION;
                return AlertMessage.MESSAGE_SOCKETEXCEPTION;
            } catch (XmlPullParserException e) {
                ResultFlag = false;
                str = AlertMessage.MESSAGE_XmlPull;
                return AlertMessage.MESSAGE_XmlPull;
            } catch (Exception e) {
                ResultFlag = false;
                str = AlertMessage.MESSAGE_EXCEPTION;
                return AlertMessage.MESSAGE_EXCEPTION;
            }

            if (ResultFlag) {
                return "";
            } else {
                return str;
            }

        }

        @Override
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub

            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equalsIgnoreCase("")) {
                dialog.dismiss();
                showPJPDeviationStoreList();
                //showAlert(getString(R.string.data_downloaded_successfully));
            } else {
                dialog.dismiss();
                Snackbar.make(lv,"No PJP Deviation data found ",Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    class Data {
        int value;
        String name;
    }
}
