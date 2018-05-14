package com.cpm.dailyentry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.lorealpromoter.R;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ClosingStockBackofficeActvity extends AppCompatActivity implements View.OnClickListener {
    boolean checkflag = true;
    boolean validate = true;
    boolean flagcoldroom = false;
    boolean checkpopup = false;
    boolean flagmccain = false;
    boolean flagstoredf = false;
    int valHeadCount;
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    List<Integer> checkValidHeaderArray = new ArrayList<Integer>();
    List<Integer> checkValidChildArray = new ArrayList<Integer>();
    static int currentapiVersion = 1;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    TextView tvheader;
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild;
    boolean ischangedflag = false;
    private SharedPreferences preferences;
    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<StockNewGetterSetter> skuData;
    GSKDatabase db;

    String visit_date, username, intime;
    String store_cd,account_cd,city_cd,storetype_cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_stock_backoffice_actvity);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp_clback);
        btnSave = (Button) findViewById(R.id.save_btn_clback);
        tvheader = (TextView) findViewById(R.id.txt_idealFor);
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
        account_cd= preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd= preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd= preferences.getString(CommonString.KEY_STORETYPE_CD, null);
        setTitle("Closing STK Backroom -" + visit_date);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
       // expListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        btnSave.setOnClickListener(this);

        expListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                expListView.invalidateViews();
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }

        });

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });


    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        db.open();
        brandData = db.getStockAvailabilityData(account_cd,city_cd,storetype_cd);
        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));
                skuData = db.getClosingStockBackOfficeDataFromDatabase(account_cd, brandData.get(i).getCategory_cd(),store_cd);
                if (skuData.size()>0){
                    if (skuData.get(0).getClosing_stk_backroom()==null || skuData.get(0).getClosing_stk_backroom().equals("")){
                        btnSave.setText("Save");
                    }else {
                        btnSave.setText("Update");
                    }

                }

                List<StockNewGetterSetter> skulist = new ArrayList<StockNewGetterSetter>();
                for (int j = 0; j < skuData.size(); j++) {
                    skulist.add(skuData.get(j));
                }
                listDataChild.put(listDataHeader.get(i), skulist); // Header, Child data
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.save_btn_clback) {
            expListView.clearFocus();
            expListView.invalidateViews();
            if(!checkpopup){
                flagcoldroom = flagmccain = flagstoredf = false;
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to save")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.open();
                                    db.UpdateClosingStockBackOfficelistData(store_cd, listDataChild, listDataHeader);
                                    Snackbar.make(expListView, "Data has been saved", Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(expListView, "Please fill all the fields", Snackbar.LENGTH_LONG).show();
                }
            }

        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockNewGetterSetter> _listDataHeader; // header titles
        private HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockNewGetterSetter> listDataHeader, HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listChildData) {
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
            final StockNewGetterSetter childText = (StockNewGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.closing_stock_backoffice_entry, null);
                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.ed_closingStock = (EditText) convertView.findViewById(R.id.ed_closingStock_);
                holder.txt_skuHeader = (TextView) convertView.findViewById(R.id.txt_closingStock_skuHeader_);
                holder.txt_openingStock = (TextView) convertView.findViewById(R.id.txt_closingStock_openingStock_);
                holder.txt_closingStock_backroomfloreStock = (TextView) convertView.findViewById(R.id.txt_closingStock_backroomfloreStock);
                holder.txt_closingStock_midValue = (TextView) convertView.findViewById(R.id.txt_closingStock_midValue);
                holder.txt_closingStock_openingfloreStock = (TextView) convertView.findViewById(R.id.txt_closingStock_openingfloreStock);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.txt_skuHeader.setText(childText.getBrand() + " - " + childText.getSku());
           final  int closingStockfloor = Integer.parseInt(childText.getEd_closingFacing());

            holder.txt_openingStock.setText("OS-FLR :" + childText.getSumofSTOCK());
            holder.txt_closingStock_openingfloreStock.setText("OS-BR :" + childText.getOpening_stock_backroom());
            holder.txt_closingStock_backroomfloreStock.setText("CS-FLR :" + childText.getEd_closingFacing());
            holder.txt_closingStock_midValue.setText("STK-IN :" + childText.getEd_midFacing());


            final ViewHolder finalHolder = holder;

            holder.ed_closingStock.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    int sumOfAllValue = Integer.parseInt(childText.getSumofSTOCK()) + Integer.parseInt(childText.getOpening_stock_backroom()) + Integer.parseInt(childText.getEd_midFacing());
                    final int completeValue = (sumOfAllValue) - (closingStockfloor);

                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setClosing_stk_backroom("");
                    } else {
                        int closingSB = Integer.parseInt(value1);
                        if (closingSB <= completeValue) {
                            ischangedflag = true;
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setClosing_stk_backroom(value1);
                        } else {
                            checkpopup=true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(ClosingStockBackofficeActvity.this);
                            builder.setMessage("Closing stock- Backroom  cannot be greater than sum of Opening stock and Stock-in minus Closing Stock-Floor")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                           /* _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setClosing_stk_backroom("");
                                            finalHolder.ed_closingStock.setText("");*/
                                            checkpopup=false;
                                            expListView.invalidateViews();
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
                    }
                }

            });



/*
            holder.ed_closingStock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (value1.equals("")) {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setClosing_stk_backroom("");
                        } else {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setClosing_stk_backroom(value1);

                        }
                    }
                }

            });
*/

            holder.ed_closingStock.setText(childText.getClosing_stk_backroom());
            if (!checkflag) {
                boolean tempflag = false;

                if (holder.ed_closingStock.getText().toString().equals("")) {
                    holder.ed_closingStock.setHintTextColor(getResources().getColor(R.color.red));
                    holder.ed_closingStock.setHint("Empty");
                    tempflag = true;
                }

                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            }

            if (!validate) {
                if (checkValidHeaderArray.contains(groupPosition)) {
                    if (checkValidChildArray.contains(childPosition)) {
                        boolean tempflag = false;
                        holder.ed_closingStock.setTextColor(getResources().getColor(R.color.black));
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                    } else {
                        holder.ed_closingStock.setTextColor(getResources().getColor(R.color.black));
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                } else {
                    holder.ed_closingStock.setTextColor(getResources().getColor(R.color.black));
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
            lblListHeader.setText(headerTitle.getCategory());
            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
            }
            if (!validate) {
                if (checkValidHeaderArray.contains(groupPosition)) {
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
        TextView txt_skuHeader, txt_openingStock,txt_closingStock_backroomfloreStock,txt_closingStock_midValue,txt_closingStock_openingfloreStock;
        EditText ed_closingStock;
        CardView cardView;
    }

    boolean validateData(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2,
                         List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;

        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                String coldroom = listDataChild.get(listDataHeader.get(i)).get(j).getClosing_stk_backroom();

                if (coldroom==null || coldroom.equalsIgnoreCase("")) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }
                    flag = false;
                    break;
                } else {
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }

        if (flag) {
            checkflag = true;
        } else {
            checkflag = false;
        }
        return checkflag;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClosingStockBackofficeActvity.this);
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(ClosingStockBackofficeActvity.this);
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

        return super.onOptionsItemSelected(item);
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

}

