package com.cpm.dailyentry;

import android.app.Activity;
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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.himalaya.R;
import com.cpm.xmlGetterSetter.DeepFreezerTypeGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OpeningStock extends AppCompatActivity implements OnClickListener {
    boolean validate = true, flagcoldroom = false, flagmccain = false, flagstoredf = false;
    int valHeadCount, valChildCount;

    List<Integer> checkValidHeaderArray = new ArrayList<Integer>();
    List<Integer> checkValidChildArray = new ArrayList<Integer>();
    List<Integer> checkHeaderArray = new ArrayList<Integer>();

    private String image1 = "";
    String _pathforcheck, _path, str, img1 = "";

    static int grp_position = -1;
    boolean checkflag = true;
    static int currentapiVersion = 1;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Button btnSave;
    TextView tvheader;
    int saveBtnFlag = 0;
    int arrayEditText[] = {R.id.etAs_Per_Mccain, R.id.etactual_listed, R.id.etOpening_Stock_Cold_Room,
            R.id.etOpening_Stock_Mccain_Df, R.id.etTotal_Facing_McCain_DF,
            R.id.etOpening_Stock_Store_DF, R.id.etTotal_Facing_Store_DF,
            R.id.etmaterial_wellness_thawed_quantity};
    List<StockNewGetterSetter> listDataHeader;
    HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild;
    ArrayList<StockNewGetterSetter> openingStockData;

    private SharedPreferences preferences;
    String store_cd;

    ArrayList<StockNewGetterSetter> brandData;
    ArrayList<StockNewGetterSetter> skuData;
    StockNewGetterSetter insertData = new StockNewGetterSetter();

    GSKDatabase db;
    EditText et = null;
    String arrayData[] = new String[arrayEditText.length + 1];
    String sku_cd;
    //CustomKeyboardView mKeyboardView;
    //Keyboard mKeyboard;
    String visit_date, username, intime;

    private ArrayList<StockNewGetterSetter> stockData = new ArrayList<StockNewGetterSetter>();
    boolean dataExists = false;
    boolean openmccaindfFlag = false;
    String Error_Message;
    boolean ischangedflag = false;
    ArrayList<DeepFreezerTypeGetterSetter> deepFreezlist = new ArrayList<DeepFreezerTypeGetterSetter>();
    Snackbar snackbar;
    ImageView img_camOpeningStock;
    boolean isDialogOpen = true;

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
        //mKeyboard = new Keyboard(this, R.xml.keyboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        str = CommonString.FILE_PATH;

        /*mKeyboardView = (CustomKeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(this));*/

        db = new GSKDatabase(getApplicationContext());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        setTitle("Opening Stock - " + visit_date);

        // preparing list data
        prepareListData();

        openmccaindfFlag = preferences.getBoolean("opnestkmccaindf", false);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        btnSave.setOnClickListener(this);

        expListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*expListView.clearFocus();
                expListView.invalidateViews();*/
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
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

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
                /*if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.INVISIBLE);
                    *//*mKeyboardView.requestFocusFromTouch();*//*
                }*/

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
                /*Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition).getBrand()
                                + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSku(),
                        Toast.LENGTH_LONG).show();

                findViewById(R.id.lvExp).setVisibility(View.INVISIBLE);
                findViewById(R.id.entry_data).setVisibility(View.VISIBLE);
                tvheader.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSku());
                sku_cd = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSku_cd();
                saveBtnFlag = 1;*/

                return false;
            }
        });
    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<StockNewGetterSetter>();
        listDataChild = new HashMap<StockNewGetterSetter, List<StockNewGetterSetter>>();

        brandData = db.getHeaderStockImageData(store_cd, visit_date);
        if (!(brandData.size() > 0)) {
            brandData = db.getStockAvailabilityData(store_cd);
        }

        if (brandData.size() > 0) {
            // Adding child data
            for (int i = 0; i < brandData.size(); i++) {
                listDataHeader.add(brandData.get(i));

                skuData = db.getOpeningStockDataFromDatabase(store_cd, brandData.get(i).getCategory_cd());
                /*if (!(skuData.size() > 0) || (skuData.get(0).getEd_openingStock() == null) ||
                        (skuData.get(0).getEd_openingStock().equals(""))) {*/
                if (!(skuData.size() > 0)) {
                    skuData = db.getStockSkuData(store_cd, brandData.get(i).getCategory_cd());
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

                    if (validateImg()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Are you sure you want to save")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();
                                        //getMid();

                                        dataExists = db.checkStock(store_cd);
                                        if (dataExists) {
                                            db.UpdateHeaderOpeningStocklistData(store_cd, visit_date, listDataHeader);
                                            db.UpdateOpeningStocklistData(store_cd, listDataChild, listDataHeader);
                                        } else {
                                            db.InsertHeaderOpeningStocklistData(store_cd, visit_date, listDataHeader);
                                            db.InsertOpeningStocklistData(store_cd, listDataChild, listDataHeader);
                                        }

                                        Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), Error_Message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), Error_Message, Toast.LENGTH_LONG).show();
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
                holder.ed_openingStock = (EditText) convertView.findViewById(R.id.etOpening_Stock_total_stock);
                holder.ed_openingFacing = (EditText) convertView.findViewById(R.id.etOpening_Stock_Facing);
                holder.openmccaindf_layout = (LinearLayout) convertView.findViewById(R.id.openmccaindf_layaout);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getBrand() + " - " + childText.getSku());
            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setSku_cd(childText.getSku_cd());

            /*if (currentapiVersion >= 11) {
                holder.ed_openingStock.setTextIsSelectable(true);
                holder.ed_openingFacing.setTextIsSelectable(true);
                holder.ed_openingStock.setRawInputType(InputType.TYPE_CLASS_TEXT);
                holder.ed_openingFacing.setRawInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                holder.ed_openingStock.setInputType(0);
                holder.ed_openingFacing.setInputType(0);
            }*/

            holder.ed_openingStock.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    /*if (hasFocus) {
                        showKeyboardWithAnimation();
                    }

                    if (!hasFocus) {
                        hide();*/

                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    //String value1 = Caption.getText().toString().replace("^0+(?!$)&", "");
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    //final String stk_under = _listDataChild.get(listDataHeader.get(groupPosition)).get(position).getStock_under45days();
                    final String facing = _listDataChild.get(listDataHeader.get(groupPosition)).get(position).getEd_openingFacing();

                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingStock("");

                        //} else if (facing.equals("") && stk_under.equals("")) {
                    } else if (facing.equals("")) {
                        ischangedflag = true;
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingStock(value1);

                    } else {
                        if (!facing.equals("")) {

                            int totalstk = 0;
                            totalstk = Integer.parseInt(value1);
                            int fac = Integer.parseInt(facing);

                            if (fac > totalstk) {
                                _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing("");
                            }
                        }

                        /*if (!stk_under.equals("")) {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setStock_under45days("");
                        }*/

                        ischangedflag = true;
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingStock(value1);
                    }
                    //}


                    /*if (isDialogOpen) {
                        isDialogOpen = !isDialogOpen;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                        builder.setMessage(getString(R.string.check_stock))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finalHolder.stock.setText("");
                                        dialog.dismiss();
                                        isDialogOpen = !isDialogOpen;
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }*/
                }
            });

            final ViewHolder finalHolder = holder;
            holder.ed_openingFacing.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    /*if (hasFocus) {
                        showKeyboardWithAnimation();
                    }

                    if (!hasFocus) {
                        hide();*/
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    final String stock = _listDataChild.get(listDataHeader.get(groupPosition)).get(position).getEd_openingStock();

                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing("");

                    } else if (stock.equals("")) {
                        if (isDialogOpen) {
                            isDialogOpen = !isDialogOpen;
                            AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                            builder.setMessage("First fill Stock data")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            isDialogOpen = !isDialogOpen;

                                            _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing("");
                                            finalHolder.ed_openingFacing.setText("");
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        /*Snackbar.make(expListView, "First fill Stock data", Snackbar.LENGTH_LONG).show();
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing("");
                        finalHolder.ed_openingFacing.setText("");*/
                    } else {
                        int totalstk = 0;
                        totalstk = Integer.parseInt(stock);
                        int facing = Integer.parseInt(value1);

                        if (facing > totalstk) {
                            if (isDialogOpen) {
                                isDialogOpen = !isDialogOpen;
                                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningStock.this);
                                builder.setMessage("Facing cannot be greater than Stock")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                isDialogOpen = !isDialogOpen;
                                                _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing("");
                                                finalHolder.ed_openingFacing.setText("");
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            //Snackbar.make(expListView, "Facing cannot be greater than Total Stock", Snackbar.LENGTH_LONG).show();
                        } else {
                            ischangedflag = true;
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(position).setEd_openingFacing(value1);
                        }
                    }
                    //}
                }
            });

            holder.ed_openingStock.setId(childPosition);
            holder.ed_openingFacing.setId(childPosition);

            holder.ed_openingStock.setText(childText.getEd_openingStock());
            holder.ed_openingFacing.setText(childText.getEd_openingFacing());

            if (childText.getEd_openingStock().equals("0")) {
                holder.ed_openingFacing.setText("0");
                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setEd_openingFacing("0");
                holder.ed_openingFacing.setEnabled(false);
            } else {
                holder.ed_openingFacing.setText(childText.getEd_openingFacing());
                holder.ed_openingFacing.setEnabled(true);
            }


            if (!checkflag) {
                boolean tempflag = false;

                if (holder.ed_openingStock.getText().toString().equals("")) {
                    holder.ed_openingStock.setHintTextColor(getResources().getColor(R.color.red));
                    holder.ed_openingStock.setHint("Empty");
                    tempflag = true;
                } else {
                    //holder.ed_openingStock.setBackgroundColor(getResources().getColor(R.color.white));
                }

                if (holder.ed_openingFacing.getText().toString().equals("")) {
                    holder.ed_openingFacing.setHintTextColor(getResources().getColor(R.color.red));
                    holder.ed_openingFacing.setHint("Empty");
                    tempflag = true;
                } else {
                    //holder.ed_openingFacing.setBackgroundColor(getResources().getColor(R.color.white));
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

                        if (flagcoldroom) {
                            holder.ed_openingStock.setTextColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        } else {
                            holder.ed_openingStock.setTextColor(getResources().getColor(R.color.black));
                        }

                        if (flagmccain) {
                            holder.ed_openingFacing.setTextColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        } else {
                            holder.ed_openingFacing.setTextColor(getResources().getColor(R.color.black));
                        }

                        if (!flagcoldroom && !flagmccain && !flagstoredf) {
                            holder.ed_openingStock.setTextColor(getResources().getColor(R.color.black));
                            holder.ed_openingFacing.setTextColor(getResources().getColor(R.color.black));
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = false;
                        } else {
                            holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                            tempflag = true;
                        }
                    } else {
                        holder.ed_openingStock.setTextColor(getResources().getColor(R.color.black));
                        holder.ed_openingFacing.setTextColor(getResources().getColor(R.color.black));
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }

                } else {
                    holder.ed_openingStock.setTextColor(getResources().getColor(R.color.black));
                    holder.ed_openingFacing.setTextColor(getResources().getColor(R.color.black));
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

            //final int position = convertView.getId();
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            ImageView imgcam = (ImageView) convertView.findViewById(R.id.imgcamstk);

            imgcam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" + headerTitle.getCategory_cd() + "_OpeningStockImage"
                            + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;

                    startCameraActivity(0);
                }
            });

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getCategory());

            if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    //childText.get(childPosition).setCamera("YES");
                    headerTitle.setImg_cam(img1);
                    //childText.setImg(img1);
                    img1 = "";
                }
            }


            if (headerTitle.getImg_cam() != null && !headerTitle.getImg_cam().equals("")) {
                imgcam.setBackgroundResource(R.drawable.camera_done);
            } else {
                imgcam.setBackgroundResource(R.drawable.camera);
            }

            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            }

            if (!validate) {
                if (checkValidHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.light_teal));
                }
            }
            //convertView.setId(groupPosition);

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

        EditText ed_openingStock, ed_openingFacing;
        LinearLayout openmccaindf_layout;
        CardView cardView;

        public MutableWatcher mWatcher;
    }

    boolean validateData(HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2,
                         List<StockNewGetterSetter> listDataHeader2) {
        boolean flag = true;

        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {

                String openstocktotal = listDataChild2.get(listDataHeader2.get(i)).get(j).getEd_openingStock();
                String openfacing = listDataChild2.get(listDataHeader2.get(i)).get(j).getEd_openingFacing();

                if (openstocktotal.equalsIgnoreCase("") || (openfacing.equalsIgnoreCase(""))) {

                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }

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
                break;
            }
        }
        //expListView.invalidate();
        listAdapter.notifyDataSetChanged();

        return checkflag;
    }

    public boolean validateImg() {

        int count = 0;

        for (int i = 0; i < listDataHeader.size(); i++) {

            if (!(listDataHeader.get(i).getImg_cam() == null) && !listDataHeader.get(i).getImg_cam().equals("")) {
                count++;
            }
        }

        int target_count = 0;

        if (listDataHeader.size() > 5) {
            target_count = 5;
        } else {
            target_count = listDataHeader.size();
        }

        if (count >= target_count) {
            return true;
        } else {

            Error_Message = "Please click at least " + target_count + " images";
            return false;
        }

        //return true;

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
        //super.onBackPressed();

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

    class MutableWatcher implements TextWatcher {

        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                //  mUserDetails.set(mPosition, s.toString());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
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

        /*switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                _pathforcheck = "";
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        image1 = _pathforcheck;
                        img1 = _pathforcheck;
                        expListView.invalidateViews();
                        _pathforcheck = "";
                    }
                }
                break;
        }*/

        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            image1 = _pathforcheck;
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

            /*case 1:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            image1 = _pathforcheck;
                            img1 = _pathforcheck;
                            expListView.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //mKeyboardView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setVisibility(View.INVISIBLE);*/
    }

    /* Display the screen keyboard with an animated slide from bottom*/
    private void showKeyboardWithAnimation() {
        /*if (mKeyboardView.getVisibility() == View.GONE) {
            Animation animation = AnimationUtils
                    .loadAnimation(OpeningStock.this,
                            R.anim.slide_in_bottom);
            mKeyboardView.showWithAnimation(animation);
        } else if (mKeyboardView.getVisibility() == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }*/
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hide() {
        //mKeyboardView.setVisibility(View.INVISIBLE);
        /*	// mKeyboardView.clearFocus();
        mKeyboardView.requestFocusFromTouch();*/

    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        *//*if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
            *//**//*mKeyboardView.requestFocusFromTouch();*//**//*
        } else {*//*

        if (ischangedflag) {

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    OpeningStock.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {

                                    OpeningStock.this.finish();

                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            OpeningStock.this.finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        // }
    }*/

	/*@Override
    public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus==true){
			et=(EditText) v;
		}
	}*/

	/*public void saveData(){

		for (int i = 0; i < arrayEditText.length; i++) {
			View myLayout = findViewById(R.id.entry_data);
			arrayData[i]=((EditText) myLayout.findViewById(arrayEditText[i])).getText().toString();
			((EditText) myLayout.findViewById(arrayEditText[i])).setText("");
		}

		insertData.setSku_cd(sku_cd);
		insertData.setAs_per_meccain(arrayData[0]);
		insertData.setActual_listed(arrayData[1]);
		insertData.setOpen_stock_cold_room(arrayData[2]);
		insertData.setOpen_stock_mccaindf(arrayData[3]);
		insertData.setTotalfacing_mccaindf(arrayData[4]);
		insertData.setOpen_stock_store_df(arrayData[5]);
		insertData.setTotal_facing_storedf(arrayData[6]);
		insertData.setMaterial_wellness(arrayData[7]);

		db.insertOpeningStockData(insertData, sku_cd);

	}*/

/*boolean validateStockData(
            HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2,
			List<StockNewGetterSetter> listDataHeader2) {
		//boolean flag = true;

		if(brandData.size()>0){

			// Adding child data

			checkValidHeaderArray.clear();
			checkValidChildArray.clear();


			for(int i=0;i<brandData.size();i++){

				stockData=db.getClosingNMiddayStockDataFromDatabase(store_cd, brandData.get(i).getBrand_cd());

				for(int j=0;j<stockData.size();j++){

					String closing_coldroom = stockData.get(j).getClos_stock_cold_room();
					String mid_stock = stockData.get(j).getMidday_stock();
					String closing_mccain_df = stockData.get(j).getClos_stock_meccaindf();
					String closing_store_df = stockData.get(j).getClos_stock_storedf();

					String cold_room = listDataChild.get(listDataHeader.get(i)).get(j).getOpen_stock_cold_room();
					String mccain_df = listDataChild.get(listDataHeader.get(i)).get(j).getOpen_stock_mccaindf();
					String store_df = listDataChild.get(listDataHeader.get(i)).get(j).getOpen_stock_store_df();

					int midStock=Integer.parseInt(mid_stock);

					int opncold=Integer.parseInt(cold_room);
					int opnmccn=Integer.parseInt(mccain_df);
					int opnstore=+Integer.parseInt(store_df);

					int closecold=Integer.parseInt(closing_coldroom);
					int closemccn=Integer.parseInt(closing_mccain_df);
					int closestore=Integer.parseInt(closing_store_df);

					if(midStock==0){
						if(closecold>opncold){
							flagcoldroom=true;
							if(!checkValidChildArray.contains(j)){
								checkValidChildArray.add(j);
							}
							if(!checkValidHeaderArray.contains(i)){
								checkValidHeaderArray.add(i);
							}
						}
						if(closemccn>opnmccn){
							flagmccain=true;
							if(!checkValidChildArray.contains(j)){
								checkValidChildArray.add(j);
							}
							if(!checkValidHeaderArray.contains(i)){
								checkValidHeaderArray.add(i);
							}
						}
						if(closestore>opnstore){
							flagstoredf=true;
							if(!checkValidChildArray.contains(j)){
								checkValidChildArray.add(j);
							}
							if(!checkValidHeaderArray.contains(i)){
								checkValidHeaderArray.add(i);
							}
						}

						if(flagcoldroom == true || flagmccain == true || flagstoredf == true){
							validate=false;

							if(!checkValidChildArray.contains(j)){
								checkValidChildArray.add(j);
							}
							if(!checkValidHeaderArray.contains(i)){
								checkValidHeaderArray.add(i);
							}

							break;
						}
						else{
							validate=true;
						}

					}
					else{

						int total_stock = opncold + midStock + opnmccn + opnstore;
						int total_closing = closecold + closemccn + closestore;

						if ((total_stock>=total_closing)) {

							validate = true;
							//flag = true;

						} else {
							validate = false;
							valHeadCount=i;
							if(!checkValidChildArray.contains(j)){
								checkValidChildArray.add(j);
							}
							if(!checkValidHeaderArray.contains(i)){
								checkValidHeaderArray.add(i);
							}


							//flag = false;
							break;
						}

					}

				}

				if(validate==false){
					break;
				}

			}

		}

		return validate;
	}
	*/

    /*	void setBlank(
            HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> listDataChild2,
			List<StockNewGetterSetter> listDataHeader2){

		for (int i = 0; i < listDataHeader2.size(); i++) {
			for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i))
					.size(); j++) {

				if(!mccainFlag){
					listDataChild.get(listDataHeader2.get(i)).get(j).setEd_openingStock("");
					listDataChild.get(listDataHeader2.get(i)).get(j).setTotalfacing_mccaindf("");
				}

				if(!storeFlag){
					listDataChild.get(listDataHeader2.get(i)).get(j).setOpen_stock_store_df("");
					listDataChild.get(listDataHeader2.get(i)).get(j).setTotal_facing_storedf("");
				}

			}
		}

	}*/
}
