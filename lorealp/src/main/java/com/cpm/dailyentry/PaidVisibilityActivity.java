package com.cpm.dailyentry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.AssetInsertdataGetterSetter;
import com.cpm.xmlGetterSetter.AssetNonReasonGetterSetter;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PaidVisibilityActivity extends AppCompatActivity implements OnClickListener {

    boolean checkflag = true;
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    List<AssetInsertdataGetterSetter> listDataHeader;
    HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> listDataChild;
    ArrayList<AssetInsertdataGetterSetter> categoryData;
    ArrayList<AssetInsertdataGetterSetter> skuData;
    GSKDatabase db;
    private SharedPreferences preferences;
    String store_cd, visit_date, username, intime;
    ImageView img;
    boolean ischangedflag = false;
    String _pathforcheck, _path, str;
    private String image1 = "";
    String img1 = "";
    static int grp_position = -1;
    static int child_position = -1;
    String msg_error = "Please fill all the fields";
    String account_cd, city_cd, storetype_cd;
    Bitmap bmp, dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_layout);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnSave = (Button) findViewById(R.id.save_btn);
        img = (ImageView) findViewById(R.id.imgnodata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new GSKDatabase(getApplicationContext());
        db.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        account_cd = preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd = preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd = preferences.getString(CommonString.KEY_STORETYPE_CD, null);

        setTitle("Paid Visibility - " + visit_date);
        str = CommonString.FILE_PATH;
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        btnSave.setOnClickListener(this);
        expListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                expListView.invalidateViews();
            }

        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        categoryData = db.getAssetCategoryData(store_cd);


        // brandData = db.getPromotionBrandData1(account_cd,city_cd,storetype_cd);

        if (categoryData.size() > 0) {
            // Adding child data
            for (int i = 0; i < categoryData.size(); i++) {
                List<AssetInsertdataGetterSetter> skulist = new ArrayList<>();
                listDataHeader.add(categoryData.get(i));
                skuData = db.getAssetDataFromdatabase(store_cd, categoryData.get(i).getCategory_cd());
                if (!(skuData.size() > 0) || (skuData.get(0).getAsset() == null) || (skuData.get(0).getAsset().equals(""))) {
                    skuData = db.getAssetData(categoryData.get(i).getCategory_cd(), store_cd);


                } else {
                    btnSave.setText("Update");
                }
                for (int j = 0; j < skuData.size(); j++) {
                    skulist.add(skuData.get(j));
                }
                listDataChild.put(listDataHeader.get(i), skulist); // Header, Child data
            }
        } else {
            expListView.setVisibility(View.GONE);
            btnSave.setVisibility(View.INVISIBLE);
            img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.save_btn) {
            expListView.clearFocus();

            if (validateData(listDataChild, listDataHeader)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Parinaam");
                builder.setMessage("Are you sure you want to save data ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.open();
                                db.deleteAssetData(store_cd, db.getAssetHeaderData(store_cd));
                                db.InsertAssetData(store_cd, listDataChild, listDataHeader, visit_date);
                                Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                finish();
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
                listAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), msg_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<AssetInsertdataGetterSetter> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<AssetInsertdataGetterSetter> listDataHeader,
                                     HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("NewApi")
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final AssetInsertdataGetterSetter childText = (AssetInsertdataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.asset_entry, null);
                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.tbpresent = (ToggleButton) convertView.findViewById(R.id.tbpresent);
                holder.btn_cam = (Button) convertView.findViewById(R.id.btncam);
                holder.cam_layout = (LinearLayout) convertView.findViewById(R.id.cam_layout);
                holder.remark_layout = (LinearLayout) convertView.findViewById(R.id.remark_layout);
                holder.img_ref = (ImageButton) convertView.findViewById(R.id.img_ref);
                holder.asset_remark = (Spinner) convertView.findViewById(R.id.asset_remark);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            final ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.tbpresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ischangedflag = true;
                    String val = ((ToggleButton) v).getText().toString();
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPresent(val);
                    if (val.equals("NO")) {
                        finalHolder1.remark_layout.setVisibility(View.VISIBLE);
                        finalHolder1.cam_layout.setVisibility(View.GONE);
                        finalHolder.asset_remark.setSelection(0);

                        if (new File(CommonString.FILE_PATH + _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg()).exists()){
                            new File(CommonString.FILE_PATH + _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg()).delete();
                        }
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setImg("");

                    } else {
                        finalHolder1.remark_layout.setVisibility(View.GONE);
                        finalHolder1.cam_layout.setVisibility(View.VISIBLE);
                        finalHolder.asset_remark.setSelection(0);
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRemark("");
                        childText.setReason_cd("0");
                        childText.setReason("-Select Reason-");
                    }

                    expListView.invalidateViews();
                }
            });
            holder.btn_cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_PAID_VISIBILITY_IMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity();
                }
            });
            holder.tbpresent.setChecked(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("YES"));
            if (!img1.equalsIgnoreCase("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setImg(img1);
                    img1 = "";
                }
            }

            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("YES")) {
                holder.remark_layout.setVisibility(View.GONE);
                holder.cam_layout.setVisibility(View.VISIBLE);
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg() != null &&
                        !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera_green);
                } else {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera_orange);
                }
            } else if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("NO")) {
                holder.remark_layout.setVisibility(View.VISIBLE);
                holder.cam_layout.setVisibility(View.GONE);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getAsset());

            //for reason spinner
            final ArrayList<AssetNonReasonGetterSetter> reason_list = db.getAssetReasonData();
            AssetNonReasonGetterSetter non = new AssetNonReasonGetterSetter();
            non.setAreason("-Select Reason-");
            non.setAreason_cd("0");
            reason_list.add(0, non);

            holder.asset_remark.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));
            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getAreason_cd().get(0).equals(childText.getReason_cd())) {
                    holder.asset_remark.setSelection(i);
                    break;
                }
            }
            holder.asset_remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != 0) {
                        AssetNonReasonGetterSetter ans = reason_list.get(pos);
                        childText.setReason_cd(ans.getAreason_cd().get(0));
                        childText.setReason(ans.getAreason().get(0));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera_orange);
                } else {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera_green);
                }
                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            AssetInsertdataGetterSetter headerTitle = (AssetInsertdataGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getCategory());

            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    public class ViewHolder {
        ToggleButton tbpresent;
        CardView cardView;
        LinearLayout cam_layout, remark_layout;
        Button btn_cam;
        ImageButton img_ref;
        Spinner asset_remark;
    }

    public long checkMid() {
        return db.CheckMid(visit_date, store_cd);
    }

    public long getMid() {

        long mid = 0;

        mid = checkMid();

        if (mid == 0) {
            CoverageBean cdata = new CoverageBean();
            cdata.setStoreId(store_cd);
            cdata.setVisitDate(visit_date);
            cdata.setUserId(username);
            cdata.setInTime(intime);
            cdata.setOutTime(getCurrentTime());
            cdata.setReason("");
            cdata.setReasonid("0");
            cdata.setLatitude("0.0");
            cdata.setLongitude("0.0");
            cdata.setStatus(CommonString.KEY_CHECK_IN);
            mid = db.InsertCoverageData(cdata);

        }

        return mid;
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    boolean validateData(HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> listDataChild2,
                         List<AssetInsertdataGetterSetter> listDataHeader2) {
        //boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String present = listDataChild2.get(listDataHeader2.get(i)).get(j).getPresent();
                String img = listDataChild2.get(listDataHeader2.get(i)).get(j).getImg();
                String reasonCdP = listDataChild2.get(listDataHeader2.get(i)).get(j).getReason_cd();
                if (present.equalsIgnoreCase("NO")) {
                    if (reasonCdP.equalsIgnoreCase("0") || reasonCdP.equalsIgnoreCase("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        msg_error = "Please select dropdown remark";
                        checkflag = false;
                        break;

                    } else {
                        checkflag = true;
                    }
                } else if (present.equalsIgnoreCase("YES")) {
                    if (img.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        msg_error = "Please click image";
                        checkflag = false;
                        break;

                    } else {
                        checkflag = true;
                    }
                }
                if (!checkflag) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                        break;
                    }
                }
            }
            if (checkflag == false) {
                break;
            }

        }
        return checkflag;

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaidVisibilityActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PaidVisibilityActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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

                      // image1 = _pathforcheck;
                        img1 = _pathforcheck;
                        expListView.invalidateViews();
                        _pathforcheck = "";

                    }
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class ReasonSpinnerAdapter extends ArrayAdapter<AssetNonReasonGetterSetter> {
        List<AssetNonReasonGetterSetter> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<AssetNonReasonGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            AssetNonReasonGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAreason().get(0));

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            AssetNonReasonGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAreason().get(0));

            return view;
        }

    }

    public void showPlanogram(String planogram_image) {
        final Dialog dialog = new Dialog(PaidVisibilityActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.planogram_dialog_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);


        WebView webView = (WebView) dialog.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String html = "<html><head></head><body><img src=\"" + planogram_image + "\"></body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
        dialog.show();

        ImageView cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dialog.dismiss();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    public static Bitmap convertBitmap(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options ourOptions = new BitmapFactory.Options();
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
