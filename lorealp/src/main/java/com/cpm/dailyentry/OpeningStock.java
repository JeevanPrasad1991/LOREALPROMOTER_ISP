package com.cpm.dailyentry;

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
import android.support.annotation.IntegerRes;
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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OpeningStock extends AppCompatActivity implements OnClickListener {
    boolean validate = true, flagcoldroom = false, flagmccain = false, flagstoredf = false;
    List<Integer> checkValidHeaderArray = new ArrayList<Integer>();
    List<Integer> checkValidChildArray = new ArrayList<Integer>();
    List<Integer> checkHeaderArray = new ArrayList<Integer>();
    boolean checkflag = true;
    static int currentapiVersion = 1;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    TextView tvheader;
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild;
    private SharedPreferences preferences;

    String store_cd,account_cd,city_cd,storetype_cd;

    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<StockNewGetterSetter> skuData;
    GSKDatabase db;
    String visit_date, username, intime;
    String Error_Message;
    Snackbar snackbar;
    ImageView img_camOpeningStock;
    boolean cam_flag = true;
    boolean header_flag = false;
    static int row_pos = 10000;
    boolean ischangedflag = false;
    String _pathforcheck, _path, str, img1 = "", img2 = "", img3 = "";
    static int grp_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_stock);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // get the list view
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnSave = (Button) findViewById(R.id.save_btn);
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

        account_cd= preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd= preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd= preferences.getString(CommonString.KEY_STORETYPE_CD, null);




        setTitle("Opening - " + visit_date);
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
                if (getWindow().getCurrentFocus() != null) {
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
                if (getWindow().getCurrentFocus() != null) {
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

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<StockNewGetterSetter, List<StockNewGetterSetter>>();
        brandData = db.getHeaderStockImageData(store_cd, visit_date);
        if (!(brandData.size() > 0)) {
            brandData = db.getmappingStockData(account_cd,city_cd,storetype_cd);
        }
        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));
                skuData = db.getOpeningStockDataFromDatabase(store_cd, brandData.get(i).getCategory_cd());
                if (!(skuData.size() > 0)) {
                   /* skuData = db.getStockSkuData(store_cd, brandData.get(i).getCategory_cd());*/
                    skuData = db.getStockSkuData(account_cd,city_cd,storetype_cd, brandData.get(i).getCategory_cd());
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

        if (id == R.id.save_btn) {
            expListView.clearFocus();
            expListView.invalidateViews();

            if (snackbar == null || !snackbar.isShown()) {
                if (validateData(listDataChild, listDataHeader)) {
                    if (condition()) {
                        if (condition2()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Are you sure you want to save")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.open();
                                            //If data already inserted
                                            if (db.checkStock(store_cd)) {
                                                //Update
                                                db.UpdateHeaderOpeningStocklistData(store_cd, visit_date, listDataHeader);
                                                db.UpdateOpeningStocklistData(store_cd, listDataChild, listDataHeader);
                                            } else {
                                                //Insert
                                                db.InsertHeaderOpeningStocklistData(store_cd, visit_date, listDataHeader);
                                                db.InsertOpeningStocklistData(store_cd, listDataChild, listDataHeader);
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

                        }


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
                convertView = infalInflater.inflate(R.layout.list_item_openingstk, null);
                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);

                holder.etOpening_Stock = (EditText) convertView.findViewById(R.id.etOpening_Stock);
                holder.etOpening_Stock_Facingg = (EditText) convertView.findViewById(R.id.etOpening_Stock_Facingg);
                holder.ed_openingFacing = (EditText) convertView.findViewById(R.id.etOpening_Stock_Facing);

                holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            final ViewHolder finalHolder = holder;
            txtListChild.setText(childText.getBrand() + " - " + childText.getSku());
            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setSku_cd(childText.getSku_cd());

            // holder.txt_date.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDate1());

            //upendra code
            holder.etOpening_Stock.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (!value.equals("")) {
                            childText.setStock1(value);
                            if (childText.getStock1().equalsIgnoreCase("0")) {
                                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("0");
                                expListView.invalidateViews();
                            } else if (!_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getStock2().equalsIgnoreCase("")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                                builder.setMessage("Facing cannot be greater then stock")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("");
                                                //finalHolder.etOpening_Stock_Facingg.setText("");
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
                        } else {
                            childText.setStock1("");
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("");
                            expListView.invalidateViews();
                        }
                    }
                }
            });

            holder.etOpening_Stock_Facingg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // int total = Integer.parseInt(childText.getSumofSTOCK());
                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (value1.equals("")) {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2("");
                        } else {

                            if (!childText.getStock1().equalsIgnoreCase("") && Integer.parseInt(value1) <= Integer.parseInt(childText.getStock1())) {
                                ischangedflag = true;
                                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock2(value1);
                            } else if (childText.getStock1().equalsIgnoreCase("")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                                builder.setMessage("Please fill stock first")
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
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                                builder.setMessage("Facing cannot be greater than stock")
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
                }

            });

            holder.etOpening_Stock.setText(childText.getStock1());
            holder.etOpening_Stock_Facingg.setText(childText.getStock2());


            if (!checkflag) {
                boolean tempflag = false;
                if (holder.etOpening_Stock.getText().toString().equals("")) {
                    holder.etOpening_Stock.setHintTextColor(getResources().getColor(R.color.red));
                    holder.etOpening_Stock.setHint("Empty");
                    tempflag = true;
                }
                if (holder.etOpening_Stock_Facingg.getText().toString().equals("")) {
                    holder.etOpening_Stock_Facingg.setHintTextColor(getResources().getColor(R.color.red));
                    holder.etOpening_Stock_Facingg.setHint("Empty");
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
                            holder.etOpening_Stock.setTextColor(getResources().getColor(R.color.red));
                            holder.etOpening_Stock_Facingg.setTextColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        } else {
                            holder.etOpening_Stock.setTextColor(getResources().getColor(R.color.black));
                            holder.etOpening_Stock_Facingg.setTextColor(getResources().getColor(R.color.black));
                        }

                        if (!flagcoldroom && !flagmccain && !flagstoredf) {
                            holder.etOpening_Stock.setTextColor(getResources().getColor(R.color.black));
                            holder.etOpening_Stock_Facingg.setTextColor(getResources().getColor(R.color.black));
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = false;
                        } else {
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        }
                    } else {
                        holder.etOpening_Stock.setTextColor(getResources().getColor(R.color.black));
                        holder.etOpening_Stock_Facingg.setTextColor(getResources().getColor(R.color.black));
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }

                } else {
                    holder.etOpening_Stock.setTextColor(getResources().getColor(R.color.black));
                    holder.etOpening_Stock_Facingg.setTextColor(getResources().getColor(R.color.black));
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
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_opening, null);
            }
            CardView card_view = (CardView) convertView.findViewById(R.id.card_view);
            card_view.setOnClickListener(new OnClickListener() {
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
            EditText catfacing = (EditText) convertView.findViewById(R.id.catfacing);
            //ImageView imgcamhim = (ImageView) convertView.findViewById(R.id.img_cam_himalaya);
            ImageView imgcamcat1 = (ImageView) convertView.findViewById(R.id.img_cam_cat1);
            //  ImageView imgcamcat2 = (ImageView) convertView.findViewById(R.id.img_cam_cat2);
           /* if (headerTitle.getHimalaya_camera().equals("1")) {
                imgcamhim.setVisibility(View.VISIBLE);
            } else {
                imgcamhim.setVisibility(View.INVISIBLE);
            }*/

            if (headerTitle.getCategory_camera().equals("1")) {
                imgcamcat1.setVisibility(View.VISIBLE);
            } else {
                imgcamcat1.setVisibility(View.INVISIBLE);
            }

          /*  if (headerTitle.getHimalaya_camera().equals("1")) {
                imgcamcat2.setVisibility(View.VISIBLE);
            } else {
                imgcamcat2.setVisibility(View.INVISIBLE);
            }*/

/*
            imgcamhim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" +
                            headerTitle.getCategory_cd() + "_DANONECHILLERIMG_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(0);
                }
            });
*/

            catfacing.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (!value.equals("")) {
                            headerTitle.setCatstock(value);
                        } else {
                            headerTitle.setCatstock("");
                        }
                    }

                }
            });

            imgcamcat1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" +
                            headerTitle.getCategory_cd() + "_CATEGORY_STOCK_IMAGE_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(1);
                    getCurrentFocus().clearFocus();
                }
            });

/*
            imgcamcat2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" + headerTitle.getCategory_cd()
                            + "_CATSTKCHILLIMGTWO_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(1);
                }
            });
*/

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getCategory());


           /* if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    headerTitle.setImg_cam(img1);

                    img1 = "";
                }
            }*/

            if (!img2.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    headerTitle.setImg_cat_one(img2);
                    img2 = "";
                }
            }

           /* if (!img3.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    headerTitle.setImg_cat_two(img3);
                    img3 = "";
                }
            }

*/
          /*  if (headerTitle.getImg_cam() != null && !headerTitle.getImg_cam().equals("")) {
                imgcamhim.setBackgroundResource(R.mipmap.h_camera_orange);
            } else {
                imgcamhim.setBackgroundResource(R.mipmap.h_camera_white);
            }*/
            if (headerTitle.getImg_cat_one() != null && !headerTitle.getImg_cat_one().equals("")) {
                imgcamcat1.setBackgroundResource(R.mipmap.camera_green);
            } else {
                imgcamcat1.setBackgroundResource(R.mipmap.camera_white);
            }
            catfacing.setText(headerTitle.getCatstock());
           /* if (headerTitle.getCatstock() != null && !headerTitle.getCatstock().equals("")) {

            } else {

            }*/


           /* if (headerTitle.getImg_cat_two() != null && !headerTitle.getImg_cat_two().equals("")) {
                imgcamcat2.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                imgcamcat2.setBackgroundResource(R.mipmap.camera_white);
            }*/

            if (!checkflag || !cam_flag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
            }

            if (header_flag) {
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
        EditText ed_openingFacing, etOpening_Stock, etOpening_Stock_Facingg;
        CardView cardView;
        TextView txt_date;
    }

    boolean validateData(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2, List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            String category_camera = listDataHeader2.get(i).getCategory_camera();
            String catstock = listDataHeader2.get(i).getCatstock();
            String opningimage = listDataHeader2.get(i).getImg_cat_one();
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                //String company_cd = listDataChild2.get(listDataHeader2.get(i)).get(j).getCompany_cd();
                //String openfacing = listDataChild2.get(listDataHeader2.get(i)).get(j).getEd_openingFacing();

                String openingstock = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock1();
                String openingfacing = listDataChild2.get(listDataHeader2.get(i)).get(j).getStock2();

                if (category_camera.equals("1")) {
                    if (opningimage.equals("")) {
                        checkflag = false;
                        flag = false;
                        Error_Message = "Please click image";
                        break;
                    }
                }
                if (catstock.equals("")) {
                    checkflag = false;
                    flag = false;
                    Error_Message = "Please fill all category stock the data";
                    break;
                }


                if ((openingstock.equalsIgnoreCase("")) || (openingfacing.equalsIgnoreCase(""))) {
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

    public boolean condition2() {
        boolean result = true;
        header_flag = false;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<StockNewGetterSetter> list = listDataChild.get(listDataHeader.get(i));
            if (list.size() > 0) {
                int facingSum = 0;
                for (int j = 0; j < list.size(); j++) {
                    StockNewGetterSetter stockgetset = list.get(j);
                    if (!stockgetset.getStock2().equalsIgnoreCase("")) {
                        facingSum = facingSum + Integer.parseInt(stockgetset.getStock2());
                    }
                }
                if (!listDataHeader.get(i).getCatstock().equalsIgnoreCase("") && facingSum > Integer.parseInt(listDataHeader.get(i).getCatstock())) {
                    result = false;
                    header_flag = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                    final int finalI = i;
                    builder.setMessage("Sum of SKU facing for category cannot be greater then category facing")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    listDataHeader.get(finalI).setCatstock("");
                                    listAdapter.notifyDataSetChanged();
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
                    result = true;
                }
            } else {
                result = true;
            }

            if (result == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }
        }
        listAdapter.notifyDataSetChanged();
        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
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

    protected void startCameraActivity(int position) {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);


        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            img1 = _pathforcheck;
                            expListView.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;

            case 1:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            if (_pathforcheck.contains("ImgTwo")) {
                                img3 = _pathforcheck;
                            } else {
                                img2 = _pathforcheck;
                            }

                            expListView.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
