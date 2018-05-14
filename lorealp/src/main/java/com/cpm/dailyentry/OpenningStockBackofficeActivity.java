package com.cpm.dailyentry;

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
import android.widget.ImageView;
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

public class OpenningStockBackofficeActivity extends AppCompatActivity implements View.OnClickListener {
    boolean validate = true, flagcoldroom = false, flagmccain = false, flagstoredf = false;
    List<Integer> checkValidHeaderArray = new ArrayList<Integer>();
    List<Integer> checkValidChildArray = new ArrayList<Integer>();
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    String str;
    boolean checkflag = true;
    static int currentapiVersion = 1;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    TextView tvheader;
    static int row_pos = 10000;
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild;
    private SharedPreferences preferences;
    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<StockNewGetterSetter> skuData;
    GSKDatabase db;
    String visit_date, username, intime;
    String Error_Message;
    Snackbar snackbar;
    ImageView img_camOpeningStock;
    boolean ischangedflag = false;
    String store_cd, account_cd, city_cd, storetype_cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openning_stock_backoffice);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // get the list view
        expListView = (ExpandableListView) findViewById(R.id.lvExp_backoffice);
        btnSave = (Button) findViewById(R.id.save_btn_backoffice);
        tvheader = (TextView) findViewById(R.id.txt_idealFor);
        img_camOpeningStock = (ImageView) findViewById(R.id.img_camOpeningStock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        str = CommonString.FILE_PATH;
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

        setTitle("Opening STK Backroom -" + visit_date);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
       // expListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        btnSave.setOnClickListener(this);
        expListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {


                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
                expListView.invalidateViews();
            }
        });

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null) {
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
                if (getWindow().getCurrentFocus() != null) {
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
        listDataChild = new HashMap<StockNewGetterSetter, List<StockNewGetterSetter>>();
        brandData = db.getHeaderStockBackOfficeImageData(store_cd, visit_date);
        if (!(brandData.size() > 0)) {

            brandData = db.getStockAvailabilityData(account_cd, city_cd, storetype_cd);
        }
        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));
                skuData = db.getOpeningStockBackOfficeDataFromDatabase(store_cd, brandData.get(i).getCategory_cd());
                if (!(skuData.size() > 0)) {
                    skuData = db.getStockBackOfficeSkuData(account_cd, city_cd, storetype_cd, brandData.get(i).getCategory_cd());
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

        if (id == R.id.save_btn_backoffice) {
            expListView.clearFocus();
            expListView.invalidateViews();
            if (snackbar == null || !snackbar.isShown()) {
                if (validateData1(listDataChild, listDataHeader)) {
                    if (condition()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Are you sure you want to save")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();
                                        //If data already inserted
                                        if (db.checkStockBackOffice(store_cd)) {
                                            //Update
                                            db.UpdateHeaderOpeningStockBackOfficelistData(store_cd, visit_date, listDataHeader);
                                            db.UpdateOpeningStockBackOfficelistData(store_cd, listDataChild, listDataHeader);
                                        } else {
                                            //Insert
                                            db.InsertHeaderOpeningStockBackOfficelistData(store_cd, visit_date, listDataHeader);
                                            db.InsertOpeningStockBackOfficelistData(store_cd, listDataChild, listDataHeader);
                                        }
                                        Snackbar.make(expListView, "Data has been saved", Snackbar.LENGTH_LONG).show();
                                        finish();
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
                        Snackbar.make(expListView, "Invalid Faceup, Faceup Should Be Less Than The Stock at line "
                                + row_pos, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(expListView, Error_Message, Snackbar.LENGTH_LONG).show();
                }
            }

        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockNewGetterSetter> _listDataHeader;
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

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final StockNewGetterSetter childText = (StockNewGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_openingbackofficestk, null);
                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.etOpening_Stock = (EditText) convertView.findViewById(R.id.etOpening_Stock);
                holder.etOpening_Stock_Facingg = (EditText) convertView.findViewById(R.id.etOpening_Stock_Facingg);

                holder.edt_stock1 = (EditText) convertView.findViewById(R.id.edt_stock1);
                holder.edt_stock2 = (EditText) convertView.findViewById(R.id.edt_stock2);
                holder.edt_stock3 = (EditText) convertView.findViewById(R.id.edt_stock3);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            final ViewHolder finalHolder = holder;
            txtListChild.setText(childText.getBrand() + " - " + childText.getSku());
            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setSku_cd(childText.getSku_cd());

            //upendra code
            holder.etOpening_Stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (!value.equals("")) {
                        childText.setStock1(value);
                    } else {
                        childText.setStock1("");
                    }
                }
            });


            holder.etOpening_Stock_Facingg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("");
                    } else {
                        if (!childText.getStock1().equalsIgnoreCase("") && Integer.parseInt(value1) <= (Integer.parseInt(childText.getStock1()))) {
                            ischangedflag = true;
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2(value1);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OpenningStockBackofficeActivity.this);
                            builder.setMessage("opening stock cannot be greater than facing stock")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("");
                                            finalHolder.etOpening_Stock_Facingg.setText("");
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
            holder.etOpening_Stock_Facingg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (!value.equals("")) {
                        childText.setStock2(value);
                    } else {
                        childText.setStock2("");
                    }
                }
            });
*/
            holder.etOpening_Stock.setText(childText.getStock1());
            holder.etOpening_Stock_Facingg.setText(childText.getStock2());


            holder.edt_stock1.setText(childText.getStock1());
            holder.edt_stock2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (!value.equals("")) {
                        childText.setStock2(value);
                    } else {
                        childText.setStock2("");
                    }
                }
            });
            holder.edt_stock2.setText(childText.getStock2());


            holder.edt_stock3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (!value.equals("")) {
                        childText.setStock3(value);
                    } else {
                        childText.setStock3("0");
                    }
                }
            });

            if (!checkflag) {
                boolean tempflag = false;
                if (holder.edt_stock1.getText().toString().equals("")) {
                    holder.edt_stock1.setHintTextColor(getResources().getColor(R.color.red));
                    holder.edt_stock1.setHint("Empty");
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
                        if (flagmccain) {
                            holder.edt_stock1.setTextColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        } else {
                            holder.edt_stock1.setTextColor(getResources().getColor(R.color.black));
                        }

                        if (!flagcoldroom && !flagmccain && !flagstoredf) {
                            holder.edt_stock1.setTextColor(getResources().getColor(R.color.black));
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = false;
                        } else {
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        }
                    } else {
                        holder.edt_stock1.setTextColor(getResources().getColor(R.color.black));
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }

                } else {
                    holder.edt_stock1.setTextColor(getResources().getColor(R.color.black));
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final StockNewGetterSetter headerTitle = (StockNewGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this
                        ._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_opening, null);
            }
            CardView card_view = (CardView) convertView.findViewById(R.id.card_view);
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expListView.isGroupExpanded(groupPosition)) {
                        expListView.collapseGroup(groupPosition);
                    } else {
                        expListView.expandGroup(groupPosition);
                    }
                }
            });
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
        EditText edt_stock1, edt_stock2, edt_stock3, etOpening_Stock, etOpening_Stock_Facingg;
        ;
        CardView cardView;
        TextView txt_date;

    }

    boolean validateData(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2, List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String company_cd = listDataChild2.get(listDataHeader2.get(i)).get(j).getCompany_cd();
                String date1 = listDataChild2.get(listDataHeader2.get(i)).get(j).getDate1();
                String date2 = listDataChild2.get(listDataHeader2.get(i)).get(j).getDate2();
                String date3 = listDataChild2.get(listDataHeader2.get(i)).get(j).getDate3();
                String stock1 = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock1();
                String stock2 = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock2();
                String stock3 = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock3();
                if ((stock1.equalsIgnoreCase(""))) {
                    checkflag = false;
                    flag = false;
                    Error_Message = "Please fill all the data";
                    break;
                } else if (!date1.equalsIgnoreCase("") && stock1.equalsIgnoreCase("")) {
                    checkflag = false;
                    flag = false;
                    Error_Message = "Please fill all the data";
                    break;
                } else {
                    checkflag = true;
                    flag = true;
                }
            }

            if (checkflag == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }
        }
        listAdapter.notifyDataSetChanged();
        return checkflag;
    }

    boolean validateData1(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2, List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String openingstock = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock1();

                if ((openingstock.equalsIgnoreCase(""))) {
                    checkflag = false;
                    flag = false;
                    Error_Message = "Please fill all the data";
                    break;

                } else {
                    checkflag = true;
                    flag = true;
                }
            }

            if (checkflag == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }
        }
        listAdapter.notifyDataSetChanged();
        return checkflag;
    }

    public boolean condition() {
        boolean result = true;
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<StockNewGetterSetter> stockdata = listDataChild.get(listDataHeader.get(i));
            for (int j = 0; j < stockdata.size(); j++) {
                if ((stockdata.get(j).getStock1() != null && !stockdata.get(j).getStock1().equals("")) && (stockdata.get(j).getStock2() != null && !stockdata.get(j).getStock2().equals(""))) {
                    if (Integer.parseInt(stockdata.get(j).getStock2()) > (Integer.parseInt(stockdata.get(j).getStock1()))) {
                        row_pos = j + 1;
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OpenningStockBackofficeActivity.this);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OpenningStockBackofficeActivity.this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

