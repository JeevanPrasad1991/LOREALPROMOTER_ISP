package com.cpm.dailyentry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.StoreStockinGetterSetter;
import com.cpm.GetterSetter.StoreStockinPopupGetterSetter;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StockInActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener {
    boolean checkflag = true;
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    static int currentapiVersion = 1;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    TextView tvheader;
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild;
    private SharedPreferences preferences;
    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<StockNewGetterSetter> skuData;
    GSKDatabase db;
    String visit_date, username, intime;
    boolean ischangedflag = false;
    private Spinner sp_registered;
    String[] irep_registered = {"Select", "YES", "NO"};
    String spinner_irepregisterd = "";
    String store_cd, account_cd, city_cd, storetype_cd;
    StoreStockinGetterSetter storeStockinGetterSetter;
    ArrayList<JourneyPlanGetterSetter> jcplist;
    private LinearLayout bill_date;
    Dialog dialog;
    private TextView icon_date, add_date;
    String datePaid;
    int a = 0, b = 0;
    String dateset = "";
    StoreStockinPopupGetterSetter storeStockinPopupGetterSetter;
    int endyear = 0, endmonth = 0, endday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.midday_stock);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnSave = (Button) findViewById(R.id.save_btn);
        tvheader = (TextView) findViewById(R.id.txt_idealFor);
        sp_registered = (Spinner) findViewById(R.id.sp_brand);
        bill_date = (LinearLayout) findViewById(R.id.bill_date);
        icon_date = (TextView) findViewById(R.id.icon_date);
        add_date = (TextView) findViewById(R.id.add_date);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new GSKDatabase(getApplicationContext());
        db.open();

        storeStockinGetterSetter = new StoreStockinGetterSetter();
        storeStockinPopupGetterSetter = new StoreStockinPopupGetterSetter();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        account_cd = preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd = preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd = preferences.getString(CommonString.KEY_STORETYPE_CD, null);
        setTitle("Store Stock in - " + visit_date);

        //get instance calendar
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        datePaid = formattedDate;


        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, irep_registered);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_registered.setAdapter(aa3);
        sp_registered.setOnItemSelectedListener(this);

        jcplist = db.getJCPDataStore_id(store_cd);
        storeStockinGetterSetter = db.getStockInSpinnerData(store_cd);
        storeStockinPopupGetterSetter = db.getStockInPopupData(store_cd);

        if (!storeStockinPopupGetterSetter.getCurrent_date().equalsIgnoreCase("")) {
            add_date.setText(storeStockinPopupGetterSetter.getCurrent_date());
        } else {
            storeStockinPopupGetterSetter.setCurrent_date("");
        }

        prepareListData();
        // setting list adapter
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        btnSave.setOnClickListener(this);
        icon_date.setOnClickListener(this);

        expListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                expListView.clearFocus();
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
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

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
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

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
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });
    }

    // Preparing the list data
    private void prepareListData() {
        if (!storeStockinGetterSetter.getSelect_brand().equals("")) {
            if (storeStockinGetterSetter.getSelect_brand().equals("1")) {
                sp_registered.setSelection(1);
            } else {
                sp_registered.setSelection(2);
            }

        } else {
            if (jcplist.get(0).getInstock_allow().get(0).equals("1")) {
                sp_registered.setSelection(1);
                sp_registered.setEnabled(false);
            } else {
                if (jcplist.get(0).getInstock_allow().get(0).equals("0")) {
                    sp_registered.setSelection(2);
                    sp_registered.setEnabled(true);
                }
            }
        }

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        //update
        if (storeStockinGetterSetter.getSelect_brand().equals("1")) {
            bill_date.setVisibility(View.VISIBLE);
            expListView.setVisibility(View.VISIBLE);
        } else{
            expListView.setVisibility(View.GONE);
            bill_date.setVisibility(View.GONE);
        }

        brandData = db.getmappingStockInData();
        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {

                listDataHeader.add(brandData.get(i));
                skuData = db.getMiddayDataFromDatabase(store_cd, brandData.get(i).getBrand_cd());
                if (skuData.size() == 0 || (skuData.get(0).getEd_midFacing() == null) || (skuData.get(0).getEd_midFacing().equals(""))) {

                    skuData = db.getSkuMiddayData(brandData.get(i).getBrand_cd());

                } else {
                    btnSave.setText("Update");
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
        switch (id) {
            case R.id.save_btn:
                expListView.clearFocus();
                expListView.invalidateViews();
                if (storeStockinGetterSetter != null && !storeStockinGetterSetter.getSelect_brand().equals("")) {

                    if (storeStockinGetterSetter.getSelect_brand().equalsIgnoreCase("1")) {
                        if (billdate()) {
                            if (validateData(listDataChild, listDataHeader)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Are you sure you want to save")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                long id_;
                                                id_ = db.InsertSpinnereStockinData(storeStockinGetterSetter, store_cd, visit_date);
                                                if (id_ > 0) {
                                                    db.InsertPopupStockinData(storeStockinPopupGetterSetter, store_cd, visit_date);
                                                    db.InsertMidDayStockInlistData(store_cd, listDataChild, listDataHeader, spinner_irepregisterd, visit_date);
                                                    Snackbar.make(btnSave, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
                                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                                    finish();
                                                } else {
                                                    Snackbar.make(btnSave, "Data not saved", Snackbar.LENGTH_SHORT).show();
                                                }

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
                                Snackbar.make(expListView, "Please fill  Atleast one data", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(expListView, "Please Select Bill Date", Snackbar.LENGTH_SHORT).show();
                        }

                    } else if (storeStockinGetterSetter.getSelect_brand().equalsIgnoreCase("2")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Are you sure you want to save")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();
                                        long id_;
                                        id_ = db.InsertSpinnereStockinData(storeStockinGetterSetter, store_cd, visit_date);
                                        if (id_ > 0) {
                                            db.deleteStockinbilldate(store_cd);
                                            db.InsertMidDayStockInlistData(store_cd, listDataChild, listDataHeader, spinner_irepregisterd, visit_date);
                                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                            Snackbar.make(btnSave, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Snackbar.make(btnSave, "Data not saved", Snackbar.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } else {
                    listAdapter.notifyDataSetChanged();
                    Snackbar.make(expListView, "Please Select Spinner dropdown", Snackbar.LENGTH_SHORT).show();

                }
                break;
            case R.id.icon_date:
                a++;
                b = 0;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

        }

    }

    boolean validateData(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2,
                         List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = false;
        checkHeaderArray.clear();
        loop1:
        for (int i = 0; i < listDataHeader2.size(); i++) {

            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                String miday_stock = listDataChild.get(listDataHeader.get(i)).get(j).getEd_midFacing();
                if (miday_stock != null && !miday_stock.equalsIgnoreCase("") && !miday_stock.equalsIgnoreCase("0")) {
                    flag = true;
                    break;
                }

            }
            if (flag) {
                break;
            }
        }
        if (flag) {
            return checkflag = true;
        } else {

            return checkflag = false;
        }

    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_brand:
                if (position != 0) {
                    spinner_irepregisterd = String.valueOf(parent.getSelectedItemPosition());
                    if (spinner_irepregisterd.equals("1")) {
                        storeStockinGetterSetter.setSelect_brand(spinner_irepregisterd);
                        prepareListData();
                        // setting list adapter
                        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);
                    } else {

                        storeStockinGetterSetter.setSelect_brand(spinner_irepregisterd);
                        expListView.setVisibility(View.GONE);
                        bill_date.setVisibility(View.GONE);
                    }

                } else {
                    expListView.setVisibility(View.GONE);
                    bill_date.setVisibility(View.GONE);
                    storeStockinGetterSetter.setSelect_brand("");
                }
        }

    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<StockNewGetterSetter> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockNewGetterSetter> listDataHeader,
                                     HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listChildData) {
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

            final StockNewGetterSetter childText = (StockNewGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.midday_stock_entry, null);
                holder = new ViewHolder();

                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.etmidstock = (EditText) convertView.findViewById(R.id.ed_midStock);
                holder.txt_midOpeningStock = (TextView) convertView.findViewById(R.id.txt_midStock_openingStock);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();


            TextView txtListChild = (TextView) convertView.findViewById(R.id.txt_midStock_skuHeader);
            txtListChild.setText(childText.getSku());

            holder.etmidstock.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition).setEd_midFacing("");
                    } else {
                        ischangedflag = true;
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setEd_midFacing(value1);
                    }

                }
            });
            holder.etmidstock.setText(childText.getEd_midFacing());

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
        TextView txt_midOpeningStock;
        EditText etmidstock;
        CardView cardView;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockInActivity.this);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            AlertDialog.Builder builder = new AlertDialog.Builder(StockInActivity.this);
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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


    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datePickerDialog = null;
            try {

                final Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
                DatePicker dp = datePickerDialog.getDatePicker();

                dp.setMinDate(calendar.getTimeInMillis() - 86400000 * 3);
                dp.setMaxDate(calendar.getTimeInMillis());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            endyear = yy;
            endmonth = mm;
            endday = dd;
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            datePaid = year + "/" + month + "/" + day;
            if ((day < 10 && month < 10)) {
                dateset = "0" + month + "/" + "0" + day + "/" + year;
                storeStockinPopupGetterSetter.setCurrent_date(dateset);
                add_date.setText(dateset);

            } else if (day > 10 && month < 10) {
                dateset = "0" + month + "/" + day + "/" + year;
                storeStockinPopupGetterSetter.setCurrent_date(dateset);
                add_date.setText(dateset);
            } else if (day < 10 && month > 10) {
                dateset = month + "/" + "0" + day + "/" + year;
                add_date.setText(dateset);
            } else {
                dateset = month + "/" + day + "/" + year;
                storeStockinPopupGetterSetter.setCurrent_date(dateset);
                add_date.setText(dateset);

            }
        }

    }

    public boolean billdate() {
        boolean result = true;
        if (storeStockinPopupGetterSetter.getCurrent_date().equalsIgnoreCase("")) {
            result = false;
        }
        return result;
    }

}
