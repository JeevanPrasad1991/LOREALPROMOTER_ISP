package com.cpm.dailyentry;

import android.annotation.SuppressLint;
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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.NonPromotionReasonGetterSetter;
import com.cpm.xmlGetterSetter.PromotionInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PromotionActivity extends AppCompatActivity implements OnClickListener {
    boolean checkflag = true;
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> listDataChild;
    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<PromotionInsertDataGetterSetter> skuData;
    ArrayList<NonPromotionReasonGetterSetter> promotionReason = new ArrayList<>();
    GSKDatabase db;
    String store_cd, visit_date, username, intime;
    private SharedPreferences preferences;
    ImageView img;
    String _pathforcheck, _path, str;
    String img1 = "";
    static int grp_position = -1;
    static int child_position = -1;
    String errorMessage = "";
    boolean updateflag = false;
    String account_cd,city_cd,storetype_cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_layout);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnSave = (Button) findViewById(R.id.btn_save_promotion);
        img = (ImageView) findViewById(R.id.imgnodata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new GSKDatabase(getApplicationContext());
        db.open();
        promotionReason = db.getPromotionReasonData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        account_cd= preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd= preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd= preferences.getString(CommonString.KEY_STORETYPE_CD, null);
        setTitle("Promotion - " + visit_date);
        str = CommonString.FILE_PATH;
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        btnSave.setOnClickListener(this);
        expListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                expListView.invalidateViews();
                if (SCROLL_STATE_TOUCH_SCROLL == arg1) {
                    View currentFocus = getCurrentFocus();
                    if (currentFocus != null) {
                        currentFocus.clearFocus();
                    }
                }
            }
        });

    }

    // Preparing the list data
    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
       /* brandData = db.getPromotionBrandData(store_cd);*/
        brandData = db.getPromotionBrandData1(account_cd,city_cd,storetype_cd);

        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));
                skuData = db.getPromotionDataFromDatabase(store_cd, brandData.get(i).getBrand_cd());
                if (!(skuData.size() > 0) || (skuData.get(0).getPromotion_txt() == null) || (skuData.get(0).getPromotion_txt().equals(""))) {
                    skuData = db.getPromotionSkuData(brandData.get(i).getBrand_cd(),account_cd,city_cd,storetype_cd);
                } else {
                    updateflag = true;
                    btnSave.setText("Update");
                }
                List<PromotionInsertDataGetterSetter> skulist = new ArrayList<PromotionInsertDataGetterSetter>();
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

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockNewGetterSetter> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockNewGetterSetter> listDataHeader, HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> listChildData) {
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
            final PromotionInsertDataGetterSetter childText = (PromotionInsertDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.promotion_entry, null);
                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.txt_brandSkuName = (TextView) convertView.findViewById(R.id.txt_brandSkuName);
                holder.present_toggleV = (ToggleButton) convertView.findViewById(R.id.toggle_promoStock);
                holder.remark_spinV = (Spinner) convertView.findViewById(R.id.toggle_promoTalker);
                holder.promotion_img_ = (ImageView) convertView.findViewById(R.id.toggle_runningPOS);

                holder.rlP_camera = (LinearLayout) convertView.findViewById(R.id.rlP_camera);
                holder.rlp_remark = (LinearLayout) convertView.findViewById(R.id.rlp_remark);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.txt_brandSkuName.setText(childText.getPromotion_txt());
            final ArrayList<NonPromotionReasonGetterSetter> reason_list = db.getPromotionReasonData();
            NonPromotionReasonGetterSetter non = new NonPromotionReasonGetterSetter();
            non.setPreason("-Select Reason-");
            non.setPreason_cd("0");
            reason_list.add(0, non);

            holder.remark_spinV.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));
            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getPreason_cd().get(0).equals(childText.getReason_cd())) {
                    holder.remark_spinV.setSelection(i);
                    break;
                }
            }
            holder.remark_spinV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    NonPromotionReasonGetterSetter ans = reason_list.get(pos);
                    childText.setReason_cd(ans.getPreason_cd().get(0));
                    childText.setReason(ans.getPreason().get(0));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.promotion_img_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_PROMO_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity();
                }
            });

            if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    if (childPosition == child_position) {
                        childText.setCamera(img1);
                        img1 = "";
                    }
                }
            }


            if (childText.getCamera() != null && !childText.getCamera().equals("")) {
                holder.promotion_img_.setImageResource(R.drawable.camera_green);
            } else {
                holder.promotion_img_.setImageResource(R.drawable.camera_black);
            }

            final ViewHolder finalHolder = holder;
            holder.present_toggleV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        finalHolder.rlp_remark.setVisibility(View.GONE);
                        finalHolder.rlP_camera.setVisibility(View.VISIBLE);
                        childText.setPresentSpi("1");
                        childText.setReason("");
                        childText.setReason_cd("");
                        // expListView.invalidateViews();
                    } else {
                        finalHolder.rlP_camera.setVisibility(View.GONE);
                        finalHolder.rlp_remark.setVisibility(View.VISIBLE);
                        childText.setPresentSpi("0");
                        childText.setCamera("");
                    }
                }
            });

            if (childText.getPresentSpi().equals("0")) {
                holder.present_toggleV.setChecked(false);
                finalHolder.rlP_camera.setVisibility(View.GONE);
                finalHolder.rlp_remark.setVisibility(View.VISIBLE);
            } else {
                finalHolder.rlp_remark.setVisibility(View.GONE);
                finalHolder.rlP_camera.setVisibility(View.VISIBLE);
                holder.present_toggleV.setChecked(true);
            }

            if (!checkflag) {
                boolean tempflag = false;
                if (childText.getPresentSpi().equals("1")) {
                    if (childText.getCamera().equals("")) {
                        tempflag = true;
                    } else {
                        tempflag = false;
                    }
                }else{
                    if (childText.getReason_cd().equals("0")) {
                        tempflag = true;
                    } else {
                        tempflag = false;
                    }
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
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            StockNewGetterSetter headerTitle = (StockNewGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getBrand());

            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
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
        CardView cardView;
        TextView txt_brandSkuName;
        LinearLayout rlP_camera, rlp_remark;
        ImageView promotion_img_;
        Spinner remark_spinV;
        ToggleButton present_toggleV;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save_promotion) {
            if (validateData(listDataChild, listDataHeader)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to save")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.open();
                                db.deletePromotionData(store_cd);
                                db.InsertPromotionData(store_cd, listDataChild, listDataHeader);
                                Snackbar.make(expListView, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
                builder.setMessage(errorMessage)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    boolean validateData(HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> listDataChild2,
                         List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String camera = listDataChild2.get(listDataHeader2.get(i)).get(j).getCamera();
                String presentspinValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getPresentSpi();
                String reasonCdP = listDataChild2.get(listDataHeader2.get(i)).get(j).getReason_cd();

                if (presentspinValue.equals("1")) {
                    if (camera.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        errorMessage = "Please Click The Camera Image ";
                        flag = false;
                        break;
                    }
                } else if (presentspinValue.equals("0")) {
                    if (reasonCdP.equals("0"))
                    {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        errorMessage = "Please Select Reason";
                        flag = false;
                        break;
                    }
                }
            }
        }

        if (!flag) {
            checkflag = false;
        } else {
            checkflag = true;
        }
        return checkflag;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
            builder.setTitle("Parinam");
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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

                        img1 = _pathforcheck;
                        expListView.invalidateViews();
                        listAdapter.notifyDataSetChanged();
                        _pathforcheck = "";
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public class ReasonSpinnerAdapter extends ArrayAdapter<NonPromotionReasonGetterSetter> {
        List<NonPromotionReasonGetterSetter> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<NonPromotionReasonGetterSetter> list) {
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

            NonPromotionReasonGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getPreason().get(0));

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            NonPromotionReasonGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getPreason().get(0));

            return view;
        }

    }

}
