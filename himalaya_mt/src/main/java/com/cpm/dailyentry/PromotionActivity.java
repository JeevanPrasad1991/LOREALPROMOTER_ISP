package com.cpm.dailyentry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.himalaya.R;
import com.cpm.xmlGetterSetter.PromotionInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.io.File;
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
    PromotionInsertDataGetterSetter insertData = new PromotionInsertDataGetterSetter();
    GSKDatabase db;
    String store_cd, visit_date, username, intime;
    private SharedPreferences preferences;
    ImageView img;
    boolean ischangedflag = false;
    String _pathforcheck, _path, str;
    private String image1 = "";
    String img1 = "";
    static int grp_position = -1;
    static int child_position = -1;
    String errorMessage = "";

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

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

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
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                expListView.invalidateViews();
            }

        });

    }

    // Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<StockNewGetterSetter>();
        listDataChild = new HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>>();

        brandData = db.getPromotionBrandData(store_cd);

        if (brandData.size() > 0) {

            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));

                skuData = db.getPromotionDataFromDatabase(store_cd, brandData.get(i).getBrand_cd());
                if (!(skuData.size() > 0) || (skuData.get(0).getPromotion_txt() == null)
                        || (skuData.get(0).getPromotion_txt().equals(""))) {
                    skuData = db.getPromotionSkuData(brandData.get(i).getBrand_cd(), store_cd);
                } else {
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

        public ExpandableListAdapter(Context context, List<StockNewGetterSetter> listDataHeader,
                                     HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;

        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("NewApi")
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final PromotionInsertDataGetterSetter childText = (PromotionInsertDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.promotion_entry, null);
                holder = new ViewHolder();

                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
               /* holder.etremark = (EditText) convertView.findViewById(R.id.etremarks);
                holder.btn_cam = (Button) convertView.findViewById(R.id.btncam);
                holder.tbpresent = (ToggleButton) convertView.findViewById(R.id.tbpresent);
                holder.tvpromo = (TextView) convertView.findViewById(R.id.lblListItem);
                holder.cam_layout = (LinearLayout) convertView.findViewById(R.id.cam_layout);
                holder.remark_layout = (LinearLayout) convertView.findViewById(R.id.remark_layout);*/

                holder.txt_brandSkuName = (TextView) convertView.findViewById(R.id.txt_brandSkuName);
                holder.img_camera = (ImageView) convertView.findViewById(R.id.img_camera);
                holder.lin_camera = (LinearLayout) convertView.findViewById(R.id.lin_camera);
                holder.toggle_promoStock = (ToggleButton) convertView.findViewById(R.id.toggle_promoStock);
                holder.toggle_promoTalker = (ToggleButton) convertView.findViewById(R.id.toggle_promoTalker);
                holder.toggle_runningPOS = (ToggleButton) convertView.findViewById(R.id.toggle_runningPOS);

                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();

            //<editor-fold desc="Previous code">
            /*holder.tbpresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ischangedflag = true;

                    String val = ((ToggleButton) v).getText().toString();
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPresent(val);

                    if (val.equals("NO")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setImg("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRemark("");
                    }

                    expListView.invalidateViews();
                }
            });

            holder.btn_cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    child_position = childPosition;

                    _pathforcheck = store_cd + "Promoter" + "Image" + visit_date.replace("/", "")
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity();
                }
            });

            holder.etremark.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value1 = Caption.getText().toString().trim();
                        value1 = value1.replaceAll("[&^<>{}'$]", "");

                        if (value1.equals("") || value1.substring(0).equals(".") || value1.substring(0).equals(",")) {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRemark("");
                        } else {
                            ischangedflag = true;
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRemark(value1);
                        }
                    }

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

                if (_listDataChild.get(listDataHeader.get(groupPosition))
                        .get(childPosition).getImg() != null && !_listDataChild
                        .get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {

                    holder.btn_cam.setBackgroundResource(R.drawable.camera_done);
                } else {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera);
                }
            } else if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("NO")) {
                holder.remark_layout.setVisibility(View.VISIBLE);
                holder.cam_layout.setVisibility(View.GONE);
                holder.etremark.setText(childText.getRemark());
            }
            holder.tvpromo.setText(childText.getPromotion_txt());


            if (!checkflag) {
                boolean tempflag = false;

                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("NO")) {
                    if (holder.etremark.getText().toString().equals("")) {
                        holder.etremark.setHintTextColor(getResources().getColor(R.color.green));
                        holder.etremark.setHint("Empty");
                        tempflag = true;
                    }
                } else {
                    if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {
                        holder.btn_cam.setBackgroundResource(R.drawable.camera_not_done);
                    } else {
                        holder.btn_cam.setBackgroundResource(R.drawable.camera_done);
                    }
                }

                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            }*/
            //</editor-fold>

            holder.txt_brandSkuName.setText(childText.getPromotion_txt());

            holder.lin_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    child_position = childPosition;

                    _pathforcheck = store_cd + "Promoter" + "Image" + visit_date.replace("/", "")
                            + getCurrentTime().replace(":", "") + ".jpg";
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
                holder.img_camera.setBackgroundResource(R.drawable.camera_done);
            } else {
                holder.img_camera.setBackgroundResource(R.drawable.camera);
            }


            holder.toggle_promoStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childText.setPromoStock("1");
                    } else {
                        childText.setPromoStock("0");
                    }
                }
            });
            holder.toggle_promoStock.setChecked(childText.getPromoStock().equals("1"));


            holder.toggle_promoTalker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childText.setPromoTalker("1");
                    } else {
                        childText.setPromoTalker("0");
                    }
                }
            });
            holder.toggle_promoTalker.setChecked(childText.getPromoTalker().equals("1"));


            holder.toggle_runningPOS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childText.setRunningPOS("1");
                    } else {
                        childText.setRunningPOS("0");
                    }
                }
            });
            holder.toggle_runningPOS.setChecked(childText.getRunningPOS().equals("1"));

            if (!checkflag) {
                boolean tempflag = false;

                /*if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("NO")) {
                    if (holder.etremark.getText().toString().equals("")) {
                        holder.etremark.setHintTextColor(getResources().getColor(R.color.green));
                        holder.etremark.setHint("Empty");
                        tempflag = true;
                    }
                } else {
                    if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {
                        holder.btn_cam.setBackgroundResource(R.drawable.camera_not_done);
                    } else {
                        holder.btn_cam.setBackgroundResource(R.drawable.camera_done);
                    }
                }*/

                if (childText.getPromoStock().equals("1") || childText.getPromoTalker().equals("1")) {
                    if (childText.getCamera().equals("")) {
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
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
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
        EditText etremark;
        ToggleButton tbpresent;
        TextView tvpromo;
        LinearLayout cam_layout, remark_layout;
        Button btn_cam;

        CardView cardView;
        TextView txt_brandSkuName;
        LinearLayout lin_camera;
        ImageView img_camera;
        ToggleButton toggle_promoStock, toggle_promoTalker, toggle_runningPOS;

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
                                //getMid();

                                db.deletePromotionData(store_cd);
                                db.InsertPromotionData(store_cd, listDataChild, listDataHeader);

                                Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                /*Intent DailyEntryMenu = new Intent(PromotionActivity.this,StoreEntry.class);
                                startActivity(DailyEntryMenu);*/
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
                        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/
                AlertDialog alert = builder.create();
                alert.show();
                //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
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
                String promoStock = listDataChild2.get(listDataHeader2.get(i)).get(j).getPromoStock();
                String promoTalker = listDataChild2.get(listDataHeader2.get(i)).get(j).getPromoTalker();

                if (promoStock.equals("1") || promoTalker.equals("1")) {
                    if (camera.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        errorMessage = "Please click the camera image ";
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

        /*
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String present = listDataChild2.get(listDataHeader2.get(i)).get(j).getPresent();
                String remark = listDataChild2.get(listDataHeader2.get(i)).get(j).getRemark();
                String img = listDataChild2.get(listDataHeader2.get(i)).get(j).getImg();

                if (present.equalsIgnoreCase("NO")) {
                    if (remark.equalsIgnoreCase("")) {

                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        checkflag = false;

                        //flag = false;
                        break;

                    } else {

                        checkflag = true;
                        //flag = true;
                    }
                } else if (present.equalsIgnoreCase("YES")) {
                    if (img.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        checkflag = false;
                        //flag = false;
                        break;
                    } else {
                        checkflag = true;
                        //flag = true;
                    }
                }
            }

            if (checkflag == false) {
                break;
            }
        }*/

        //expListView.invalidate();
        return checkflag;
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(this, StoreEntry.class);
        startActivity(i);*/

        //if (ischangedflag) {
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
        /*} else {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }*/
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
                            // NavUtils.navigateUpFromSameTask(this);
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
                        //cam.setBackgroundResource(R.drawable.camera_list_tick);
                        image1 = _pathforcheck;
                        img1 = _pathforcheck;
                        expListView.invalidateViews();
                        _pathforcheck = "";
                        //	Toast.makeText(getApplicationContext(), ""+image1, Toast.LENGTH_LONG).show();
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

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

        return cdate;

    }
}
