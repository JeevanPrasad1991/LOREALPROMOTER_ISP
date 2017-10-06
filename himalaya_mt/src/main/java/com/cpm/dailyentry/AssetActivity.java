package com.cpm.dailyentry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.himalaya.R;
import com.cpm.xmlGetterSetter.AssetInsertdataGetterSetter;
import com.cpm.xmlGetterSetter.Audit_QuestionDataGetterSetter;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.ChecklistInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetChecklistGetterSetter;
import com.cpm.xmlGetterSetter.SkuGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AssetActivity extends AppCompatActivity implements OnClickListener {

    boolean checkflag = true;
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    List<AssetInsertdataGetterSetter> listDataHeader;
    HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> listDataChild;
    ArrayList<AssetInsertdataGetterSetter> categoryData;
    ArrayList<AssetInsertdataGetterSetter> skuData;
    AssetInsertdataGetterSetter insertData = new AssetInsertdataGetterSetter();

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

    ArrayList<ChecklistInsertDataGetterSetter> checklistInsertDataGetterSetters;
    ArrayList<MappingAssetChecklistGetterSetter> mappingChecklistDataGetterSetters;
    ArrayList<StockNewGetterSetter> listSkuData;
    ListView listView;

    String msg_error = "Please fill all the fields";

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

        setTitle("Paid Visibility - " + visit_date);

        str = CommonString.FILE_PATH;

        mappingChecklistDataGetterSetters = db.getMapingCheckListData();

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
        listDataChild = new HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>>();

		/*categoryData=db.getAssetUpload(store_cd);
        if(categoryData.size()<0){
		}*/

        categoryData = db.getAssetCategoryData(store_cd);

        if (categoryData.size() > 0) {

            // Adding child data
            for (int i = 0; i < categoryData.size(); i++) {
                List<AssetInsertdataGetterSetter> skulist = new ArrayList<>();
                listDataHeader.add(categoryData.get(i));

                skuData = db.getAssetDataFromdatabase(store_cd, categoryData.get(i).getCategory_cd());
                if (!(skuData.size() > 0) || (skuData.get(0).getAsset() == null) || (skuData.get(0).getAsset().equals(""))) {
                    skuData = db.getAssetData(categoryData.get(i).getCategory_cd(), store_cd);
                    for (int j = 0; j < skuData.size(); j++) {
                        skulist.add(skuData.get(j));
                    }
                } else {

                    for (int j = 0; j < skuData.size(); j++) {

                       /* ArrayList<StockNewGetterSetter> listSkuData = db.getAfterPaidVisibilitySkuData(skuData.get(j).getAsset_cd(),
                                store_cd, visit_date, categoryData.get(i).getCategory_cd());
                     */
                        ArrayList<SkuGetterSetter> listSkuData = db.getPaidVisibilitySkuListData(skuData.get(j).getKey_id());
                        if (listSkuData.size() > 0) {
                            skuData.get(j).setSkulist(listSkuData);
                        }

                        ArrayList<ChecklistInsertDataGetterSetter> check_list = db.getCheckListWithReasonData(skuData.get(j).getKey_id());

                        if (check_list.size() > 0) {
                            skuData.get(j).setChecklist(check_list);
                        }

                        skulist.add(skuData.get(j));
                    }

                    btnSave.setText("Update");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to save")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.open();
                                //getMid();

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
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("NewApi")
        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final AssetInsertdataGetterSetter childText = (AssetInsertdataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.asset_entry, null);
                holder = new ViewHolder();

                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.etremark = (EditText) convertView.findViewById(R.id.etremarks);
                holder.tbpresent = (ToggleButton) convertView.findViewById(R.id.tbpresent);
                holder.btn_cam = (Button) convertView.findViewById(R.id.btncam);
                holder.cam_layout = (LinearLayout) convertView.findViewById(R.id.cam_layout);
                holder.remark_layout = (LinearLayout) convertView.findViewById(R.id.remark_layout);
                holder.btn_checkList = (Button) convertView.findViewById(R.id.btn_checkList);
                holder.btn_skuList = (Button) convertView.findViewById(R.id.btn_skuList);
                holder.img_ref = (ImageButton) convertView.findViewById(R.id.img_ref);

                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();

            holder.img_ref.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPlanogram(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPlanogram_img());
                }
            });

            holder.tbpresent.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ischangedflag = true;
                    String val = ((ToggleButton) v).getText().toString();

                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPresent(val);

                    if (val.equals("NO")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setImg("");
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getChecklist().clear();
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSkulist().clear();

                       /* db.deleteCheckListInsertData(_listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition).getAsset_cd(), store_cd, visit_date);*/
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRemark("");
                    }

                    expListView.clearFocus();
                    expListView.invalidateViews();
                }
            });

            holder.btn_checkList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChecklistDialogue(childText.getAsset_cd(), childText.getCategory_cd(), _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                }
            });

            holder.btn_cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    child_position = childPosition;

                    _pathforcheck = store_cd + "Asset" + "Image" + visit_date.replace("/", "") +
                            getCurrentTime().replace(":", "") + ".jpg";

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
                            _listDataChild.get(listDataHeader.get(groupPosition))
                                    .get(childPosition).setRemark("");
                        } else {
                            ischangedflag = true;
                            _listDataChild.get(listDataHeader.get(groupPosition))
                                    .get(childPosition).setRemark(value1);
                        }
                    }
                }
            });
            holder.etremark.setText(childText.getRemark());

            holder.tbpresent.setChecked(_listDataChild.get(listDataHeader.get(groupPosition))
                    .get(childPosition).getPresent().equals("YES"));

            if (!img1.equalsIgnoreCase("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    //childText.get(childPosition).setCamera("YES");
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setImg(img1);
                    //childText.setImg(img1);
                    img1 = "";
                }
            }

            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("YES")) {
                //holder.etremark.setVisibility(View.INVISIBLE);
                holder.remark_layout.setVisibility(View.GONE);
                holder.cam_layout.setVisibility(View.VISIBLE);
               // holder.btn_skuList.setVisibility(View.VISIBLE);

                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg() != null &&
                        !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getImg().equals("")) {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera_done);
                } else {
                    holder.btn_cam.setBackgroundResource(R.drawable.camera);
                }

            } else if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("NO")) {
                //holder.etremark.setVisibility(View.VISIBLE);
                holder.remark_layout.setVisibility(View.VISIBLE);
                holder.cam_layout.setVisibility(View.GONE);
                holder.btn_skuList.setVisibility(View.GONE);
                holder.etremark.setText(childText.getRemark());
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getAsset());

            for (int m = 0; m < mappingChecklistDataGetterSetters.size(); m++) {

                if (mappingChecklistDataGetterSetters.get(m).getAsset_cd().get(0)
                        .equals(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAsset_cd())) {

                    if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPresent().equals("YES")) {
                        holder.btn_checkList.setVisibility(View.VISIBLE);
                    } else {
                        holder.btn_checkList.setVisibility(View.GONE);
                    }

                    break;
                } else {
                    holder.btn_checkList.setVisibility(View.GONE);
                }
            }


            holder.btn_skuList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSkuDialog(childText);
                }
            });

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

                    if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getChecklist().size() == 0) {
                        holder.btn_checkList.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        holder.btn_checkList.setTextColor(getResources().getColor(R.color.black));
                    }

                    if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSkulist().size() == 0) {
                        holder.btn_skuList.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        holder.btn_skuList.setTextColor(getResources().getColor(R.color.black));
                    }
                }

                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }

            }


            //holder.tvpromo.setText(childText.getAsset());
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
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            AssetInsertdataGetterSetter headerTitle = (AssetInsertdataGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
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
        EditText etremark;
        ToggleButton tbpresent;
        CardView cardView;
        LinearLayout cam_layout, remark_layout;
        Button btn_cam, btn_checkList, btn_skuList;
        ImageButton img_ref;
        //TextView tvpromo;
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

    boolean validateData(HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> listDataChild2,
                         List<AssetInsertdataGetterSetter> listDataHeader2) {
        //boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i))
                    .size(); j++) {

                ArrayList<ChecklistInsertDataGetterSetter> checklistInsertDataGetterSetter;
                ArrayList<SkuGetterSetter> skuaddedlist;

				/*String aspermccain = listDataChild2.get(listDataHeader2.get(i)).get(j).getAs_per_meccain();*/
                String present = listDataChild2.get(listDataHeader2.get(i)).get(j).getPresent();
                String remark = listDataChild2.get(listDataHeader2.get(i)).get(j).getRemark();
                String img = listDataChild2.get(listDataHeader2.get(i)).get(j).getImg();

                checklistInsertDataGetterSetter = listDataChild2.get(listDataHeader2.get(i)).get(j).getChecklist();
                skuaddedlist = listDataChild2.get(listDataHeader2.get(i)).get(j).getSkulist();

                String asset_cd = listDataChild2.get(listDataHeader2.get(i)).get(j).getAsset_cd();

                ArrayList<StockNewGetterSetter> listSkuData = listDataChild2.get(listDataHeader2.get(i)).get(j).getListSkuData();


                if (present.equalsIgnoreCase("NO")) {
                    if (remark.equalsIgnoreCase("")) {

                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        msg_error = "Please fill remark";
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

                        msg_error = "Please click image";
                        checkflag = false;

                        //flag = false;
                        break;

                    } else {

                        checkflag = true;
                        //flag = true;
                    }
                }

                if (checkflag && present.equalsIgnoreCase("YES")) {

                    for (int m = 0; m < mappingChecklistDataGetterSetters.size(); m++) {

                      /* if (mappingChecklistDataGetterSetters.get(m).getAsset_cd().get(0).equals(asset_cd)) {
                            checklistInsertDataGetterSetter = db.getCheckListInsertData(asset_cd, store_cd, visit_date);
                            if (!(checklistInsertDataGetterSetter.size() > 0)) {
                                checkflag = false;
                            }
                            break;
                        }*/


                    }

                    if (checklistInsertDataGetterSetter.size() > 0) {
                        checkflag = true;
                    } else {
                        msg_error = "Please fill Checklist data";
                        checkflag = false;
                        break;
                    }

                }

              /*  if (checkflag && present.equalsIgnoreCase("YES")) {

                    if (skuaddedlist.size() > 0) {
                        checkflag = true;
                    } else {
                        msg_error = "Please fill SKU data";
                        checkflag = false;
                        break;
                    }
                }*/

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

        //expListView.invalidate();

        return checkflag;

    }

    @Override
    public void onBackPressed() {
        //if (ischangedflag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssetActivity.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(AssetActivity.this);
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
                        //Toast.makeText(getApplicationContext(), ""+image1, Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    //CheckList
    public void showChecklistDialogue(final String asset_cd, final String category_cd, final AssetInsertdataGetterSetter asset_child) {
        boolean update_flag = false;
        checklistInsertDataGetterSetters = asset_child.getChecklist();
        //checklistInsertDataGetterSetters = db.getCheckListInsertData(asset_cd, store_cd, visit_date);
        if (!(checklistInsertDataGetterSetters.size() > 0)) {
            checklistInsertDataGetterSetters = db.getCheckListData(asset_cd);
        } else {
            update_flag = true;
        }

        if (checklistInsertDataGetterSetters.size() > 0) {
            final Dialog dialog = new Dialog(AssetActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.checklist_dialogue_layout);

            // set values for custom dialog components - text, image and button

            listView = (ListView) dialog.findViewById(R.id.lv_checklist);
            listView.setAdapter(new MyAdaptor());

            Button btnsave = (Button) dialog.findViewById(R.id.btn_save_checklist);
            Button btncancel = (Button) dialog.findViewById(R.id.btn_cancel_checklist);

            if (update_flag) {
                btnsave.setText("UPDATE");
            }

            btncancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.clearFocus();
                    boolean isvalid = true;
                    String error_msg = "";

                    for (int i = 0; i < checklistInsertDataGetterSetters.size(); i++) {

                        if (checklistInsertDataGetterSetters.get(i).getChecklist_type().equals("FREETEXT")
                                && checklistInsertDataGetterSetters.get(i).getChecklist_text().equals("")) {
                            isvalid = false;
                            error_msg = "Please fill text in text field";
                            break;
                        } else if (checklistInsertDataGetterSetters.get(i).getChecklist_type().equals("TOGGLE")
                                && checklistInsertDataGetterSetters.get(i).getChecklist_text().equalsIgnoreCase("NO")) {
                            if (checklistInsertDataGetterSetters.get(i).getReason_cd().equals("0")) {
                                isvalid = false;
                                error_msg = "Please select reason";
                                break;
                            }
                        }
                    }

                    if (isvalid) {
                        //db.insertAssetCheckListData(checklistInsertDataGetterSetters, asset_cd, visit_date, store_cd, category_cd);
                        asset_child.setChecklist(checklistInsertDataGetterSetters);
                        dialog.cancel();
                    } else {
                        //Snackbar.make(expListView,error_msg,Snackbar.LENGTH_SHORT).show();
                        Snackbar sb = Snackbar.make(listView, error_msg, Snackbar.LENGTH_SHORT);
                        sb.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                        sb.show();
                    }
                }
            });
            dialog.show();
        }
    }

    private class MyAdaptor extends BaseAdapter {

        @Override
        public int getCount() {

            return checklistInsertDataGetterSetters.size();
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
            ViewCheckHolder holder = null;
            //spinflag=false;
            //final String status="";

            if (convertView == null) {
                holder = new ViewCheckHolder();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.check_list_child, null);

                holder.dfavail = (TextView) convertView.findViewById(R.id.tv_checklist);
                holder.tbpresent = (ToggleButton) convertView.findViewById(R.id.toggle_checklist);
                holder.etremark = (EditText) convertView.findViewById(R.id.et_checklist);
                holder.stauslay = (LinearLayout) convertView.findViewById(R.id.status_layout);
                holder.reason_lay = (LinearLayout) convertView.findViewById(R.id.lay_reason);
                holder.spin_reason = (Spinner) convertView.findViewById(R.id.spin_reason);

                holder.etremark.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int position = v.getId();
                            final EditText Caption = (EditText) v;
                            String value1 = Caption.getText().toString();

                            if (value1.equals("")) {
                                checklistInsertDataGetterSetters.get(position).setChecklist_text("");
                            } else {
                                checklistInsertDataGetterSetters.get(position)
                                        .setChecklist_text(value1.toString().replaceAll("[&^<>{}'$]", ""));
                            }
                        }
                    }
                });


                holder.tbpresent.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String val = ((ToggleButton) v).getText().toString();

                        final int position = v.getId();

                        checklistInsertDataGetterSetters.get(position).setChecklist_text(val);
                        listView.invalidateViews();
                    }
                });
                convertView.setTag(holder);

            } else {
                holder = (ViewCheckHolder) convertView.getTag();
            }

            //holder.spinstat.setAdapter(dfAdapter);

            holder.dfavail.setId(position);
            holder.etremark.setId(position);
            holder.tbpresent.setId(position);
            //holder.spinstat.setId(position);

            holder.dfavail.setText(checklistInsertDataGetterSetters.get(position).getChecklist());
            holder.etremark.setText(checklistInsertDataGetterSetters.get(position).getChecklist_text());
            //status=deepFreezlist.get(position).getStatus();
            if (checklistInsertDataGetterSetters.get(position).getChecklist_type().equals("FREETEXT")) {
                holder.etremark.setVisibility(View.VISIBLE);
                holder.tbpresent.setVisibility(View.GONE);
                //dfAdapter.notifyDataSetChanged();
                // notifyDataSetChanged();
                //listView.invalidateViews();
            } else if (checklistInsertDataGetterSetters.get(position).getChecklist_type().equals("TOGGLE")) {

                holder.etremark.setVisibility(View.GONE);
                holder.tbpresent.setVisibility(View.VISIBLE);

                if (checklistInsertDataGetterSetters.get(position).getChecklist_text().equals("YES")) {
                    holder.tbpresent.setChecked(true);
                    holder.reason_lay.setVisibility(View.GONE);
                } else {

                    holder.tbpresent.setChecked(false);
                    holder.reason_lay.setVisibility(View.VISIBLE);
                    final ArrayList<AssetChecklistReasonGettersetter> reason_list = db.getAssetCheckListReasonData(checklistInsertDataGetterSetters.get(position).getChecklist_id());
                    AssetChecklistReasonGettersetter select = new AssetChecklistReasonGettersetter();
                    select.setReason("Select");
                    select.setReason_id("0");
                    reason_list.add(0, select);

                    holder.spin_reason.setAdapter(new ReasonSpinnerAdapter(AssetActivity.this, R.layout.spinner_text_view, reason_list));

                    for (int i = 0; i < reason_list.size(); i++) {
                        if (reason_list.get(i).getReason_id().equals(checklistInsertDataGetterSetters.get(position).getReason_cd())) {
                            holder.spin_reason.setSelection(i);
                            break;
                        }
                    }

                    holder.spin_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            AssetChecklistReasonGettersetter ans = reason_list.get(pos);
                            checklistInsertDataGetterSetters.get(position).setReason_cd(ans.getReason_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

            }

            return convertView;
        }

    }

    private class ViewCheckHolder {
        TextView dfavail;
        //Spinner spinstat;

        ToggleButton tbpresent;
        EditText etremark;
        LinearLayout stauslay, reason_lay;
        Spinner spin_reason;

    }

    //Sku List
   /* public void showSkuListDialogue(final AssetInsertdataGetterSetter asset) {

        final AssetInsertdataGetterSetter assets = asset;
        final String category_cd = assets.getCategory_cd();
        final String asset_cd = assets.getAsset_cd();

        boolean update_flag = false;
        //ArrayList<StockNewGetterSetter> listSkuData = db.getAfterPaidVisibilitySkuData(asset_cd, store_cd, visit_date, category_cd);
        ArrayList<StockNewGetterSetter> listSkuData = assets.getListSkuData();
        if (!(listSkuData.size() > 0)) {
            listSkuData = db.getPaidVisibilitySkuData(store_cd, category_cd);
        } else {
            update_flag = true;
        }

        final Dialog dialog = new Dialog(AssetActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sku_checklist_dialogue_layout);

        Button btnsave = (Button) dialog.findViewById(R.id.btn_save_checklist);
        Button btncancel = (Button) dialog.findViewById(R.id.btn_cancel_checklist);
        LinearLayout lin_no_data = (LinearLayout) dialog.findViewById(R.id.lin_no_data);

        if (listSkuData.size() > 0) {
            lin_no_data.setVisibility(View.GONE);

            listView = (ListView) dialog.findViewById(R.id.lv_checklist);
            listView.setAdapter(new SkuListAdaptor(listSkuData));

            if (update_flag) {
                btnsave.setText("UPDATE");
            }

            btncancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            final ArrayList<StockNewGetterSetter> finalListSkuData = listSkuData;
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.clearFocus();
                    boolean isvalid = false;

                    for (int i = 0; i < finalListSkuData.size(); i++) {
                        if (finalListSkuData.get(i).getChk_skuBox().equals("1")) {
                            isvalid = true;
                            break;
                        }
                    }

                    if (isvalid) {
                        //db.insertAssetSkuListData(finalListSkuData, asset_cd, visit_date, store_cd, category_cd);
                        assets.setListSkuData(finalListSkuData);
                        dialog.cancel();
                    } else {
                        Toast.makeText(AssetActivity.this, "Please fill atleast one sku", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        } else {
            btnsave.setVisibility(View.GONE);
            lin_no_data.setVisibility(View.VISIBLE);

            btncancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
        }

    }*/

    private class SkuListAdaptor extends BaseAdapter {
        List<StockNewGetterSetter> skuListData;

        public SkuListAdaptor(List<StockNewGetterSetter> skuListData) {
            this.skuListData = skuListData;
        }

        @Override
        public int getCount() {
            return skuListData.size();
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
            ViewSkuHolder holder = null;

            if (convertView == null) {
                holder = new ViewSkuHolder();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sku_check_list_child, null);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.chk_sku = (CheckBox) convertView.findViewById(R.id.chk_sku);

                convertView.setTag(holder);
            } else {
                holder = (ViewSkuHolder) convertView.getTag();
            }

            final StockNewGetterSetter skuStock = skuListData.get(position);

            holder.txt_skuName.setId(position);
            holder.txt_skuName.setText(skuStock.getSku());

            holder.chk_sku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        skuStock.setChk_skuBox("1");
                    } else {
                        skuStock.setChk_skuBox("0");
                    }
                }
            });

            holder.chk_sku.setChecked(skuStock.getChk_skuBox().equals("1"));

            return convertView;
        }
    }

    private class ViewSkuHolder {
        TextView txt_skuName;
        CheckBox chk_sku;
    }

    public class ReasonSpinnerAdapter extends ArrayAdapter<AssetChecklistReasonGettersetter> {
        List<AssetChecklistReasonGettersetter> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<AssetChecklistReasonGettersetter> list) {
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

            AssetChecklistReasonGettersetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getReason());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            AssetChecklistReasonGettersetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getReason());

            return view;
        }

    }

    public void showSkuDialog(final AssetInsertdataGetterSetter asset_child) {

        final ArrayList<SkuGetterSetter> skuAddedList = asset_child.getSkulist();

        final SkuGetterSetter[] sku_selected = new SkuGetterSetter[1];
        final BrandGetterSetter[] brand_selected = new BrandGetterSetter[1];

        final ArrayList<BrandGetterSetter> brandList = db.getBrandDataForPaidvisibility(store_cd, asset_child.getCategory_cd());
        BrandGetterSetter brand = new BrandGetterSetter();
        brand.setBrand("select");
        brand.setBrand_cd("0");
        brandList.add(0, brand);
        // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)

        final Dialog dialog = new Dialog(AssetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.t2p_sku_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        final Spinner spinner_brand = (Spinner) dialog.findViewById(R.id.spinner_brand);
        final Spinner spinner_sku = (Spinner) dialog.findViewById(R.id.spinner_sku);
        Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final EditText et_stock = (EditText) dialog.findViewById(R.id.et_stock);
        final RecyclerView rec_sku = (RecyclerView) dialog.findViewById(R.id.rec_sku);


        final ArrayList<SkuGetterSetter> sku_list = new ArrayList<>();

        if (skuAddedList.size() > 0) {

            rec_sku.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            SkuAddedAdapter skuAdapter = new SkuAddedAdapter(skuAddedList);
            rec_sku.setAdapter(skuAdapter);

        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDuplicate = false;
                if (brand_selected[0] == null || sku_selected[0] == null || et_stock.getText().toString().equals("")) {

                    Snackbar.make(v, getResources().getString(R.string.enter_the_values), Snackbar.LENGTH_SHORT).show();
                } else {
                    if (skuAddedList.size() > 0) {
                        for (int i = 0; i < skuAddedList.size(); i++) {
                            if (skuAddedList.get(i).getSKU_ID().equalsIgnoreCase(sku_selected[0].getSKU_ID()))
                            {
                                isDuplicate = true;
                                break;
                            }
                        }
                    }
                    if(isDuplicate)
                    {
                        Snackbar.make(v, getResources().getString(R.string.entry_already_exist), Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SkuGetterSetter sku = new SkuGetterSetter();
                        sku.setBRAND_ID(brand_selected[0].getBrand_cd().get(0));
                        sku.setBRAND(brand_selected[0].getBrand().get(0));
                        sku.setSKU(sku_selected[0].getSKU());
                        sku.setSKU_ID(sku_selected[0].getSKU_ID());
                        sku.setQUANTITY(et_stock.getText().toString().replaceFirst("^0+(?!$)", ""));

                        skuAddedList.add(sku);

                        rec_sku.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        SkuAddedAdapter skuAdapter = new SkuAddedAdapter(skuAddedList);
                        rec_sku.setAdapter(skuAdapter);

                        et_stock.setText("");
                        spinner_brand.setSelection(0);

                        SkuGetterSetter select = new SkuGetterSetter();
                        select.setSKU(getString(R.string.select));
                        sku_list.clear();
                        sku_list.add(select);
                        CustomSkuAdapter skuadapter = new CustomSkuAdapter(AssetActivity.this, R.layout.custom_spinner_item, sku_list);
                        spinner_sku.setAdapter(skuadapter);

                        spinner_sku.setSelection(0);

                        brand_selected[0] = null;
                        sku_selected[0] = null;
                    }

                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
                listAdapter.notifyDataSetChanged();
            }
        });

        // Create custom adapter object ( see below CustomAdapter.java )
        CustomAdapter adapter = new CustomAdapter(AssetActivity.this, R.layout.custom_spinner_item, brandList);
        // Set adapter to spinner
        spinner_brand.setAdapter(adapter);


        SkuGetterSetter select = new SkuGetterSetter();
        select.setSKU(getString(R.string.select));
        sku_list.add(select);
        CustomSkuAdapter skuadapter = new CustomSkuAdapter(AssetActivity.this, R.layout.custom_spinner_item, sku_list);
        spinner_sku.setAdapter(skuadapter);


        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    sku_list.clear();

                    brand_selected[0] = brandList.get(position);

                    String brand_id = brandList.get(position).getBrand_cd().get(0);

                    ArrayList<SkuGetterSetter> temp_list = db.getSkuDataForPaidvisibility(store_cd, brand_id);

                    for (int k = 0; k < temp_list.size(); k++) {
                        sku_list.add(temp_list.get(k));
                    }

                    SkuGetterSetter select = new SkuGetterSetter();
                    select.setSKU(getString(R.string.select));
                    select.setSKU_ID("0");
                    sku_list.add(0, select);
                    // Create custom adapter object ( see below CustomSkuAdapter.java )
                    CustomSkuAdapter skuadapter = new CustomSkuAdapter(AssetActivity.this, R.layout.custom_spinner_item, sku_list);
                    // Set adapter to spinner
                    spinner_sku.setAdapter(skuadapter);

                    spinner_sku.setSelection(0);

                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    sku_selected[0] = sku_list.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    public class SkuAddedAdapter extends RecyclerView.Adapter<SkuAddedAdapter.ViewHolder> {

        private ArrayList<SkuGetterSetter> list;

        public SkuAddedAdapter(ArrayList<SkuGetterSetter> skuList) {
            list = skuList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sku_added_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final SkuGetterSetter mItem = list.get(position);
            holder.tv_brand.setText(mItem.getBRAND());
            holder.tv_sku.setText(mItem.getSKU().trim());
            holder.tv_stock.setText(mItem.getQUANTITY());
            holder.btn_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    SkuAddedAdapter.this.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageButton btn_delete;
            public final LinearLayout parentLayout;
            public final TextView tv_brand, tv_sku, tv_stock;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                tv_brand = (TextView) mView.findViewById(R.id.tv_brand);
                tv_sku = (TextView) mView.findViewById(R.id.tv_sku);
                tv_stock = (TextView) mView.findViewById(R.id.tv_stock);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);
                btn_delete = (ImageButton) mView.findViewById(R.id.delete);
            }

        }
    }

    public class CustomSkuAdapter extends ArrayAdapter<String> {

        SkuGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSkuAdapter(
                AssetActivity activitySpinner,
                int textViewResourceId,
                ArrayList objects
        ) {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            data = objects;
            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.custom_spinner_item, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (SkuGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getSKU());
            }

            return row;
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        BrandGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(
                AssetActivity activitySpinner,
                int textViewResourceId,
                ArrayList objects

        ) {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            data = objects;
            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.custom_spinner_item, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (BrandGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getBrand().get(0));
            }

            return row;
        }
    }

    public void showPlanogram(String planogram_image) {

        final Dialog dialog = new Dialog(AssetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.planogram_dialog_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        //ArrayList<MAPPING_PLANOGRAM_DataGetterSetter> mp = db.getMappingPlanogramData("");

        WebView webView = (WebView) dialog.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        //String planogram_image = mp.get(0).getPLANOGRAM_IMAGE();
        //if (new File(str + planogram_image).exists()) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //String imagePath = "file://" + CommonString.FILE_PATH + "/" + planogram_image;
        //String imagePath = "file://" + CommonString.FILE_PATH + "/" + "image_ref.png";
        String html = "<html><head></head><body><img src=\"" + planogram_image + "\"></body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        dialog.show();
        // }

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
}
