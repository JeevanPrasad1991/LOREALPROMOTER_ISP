package com.cpm.dailyentry;

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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.lorealpromoter.R;
import com.cpm.database.GSKDatabase;
import com.cpm.xmlGetterSetter.AssetMasterGetterSetter;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.CategoryMasterGetterSetter;
import com.cpm.xmlGetterSetter.CompanyGetterSetter;
import com.cpm.xmlGetterSetter.MarketIntelligenceGetterSetter;
import com.cpm.xmlGetterSetter.PromoTypeGetterSetter;
import com.cpm.xmlGetterSetter.SubCategoryGetterSetter;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Additonalvisibility extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    GSKDatabase db;
    String store_cd, visit_date, username, intime, _pathforcheck, _path, str, image1 = "";
    private SharedPreferences preferences;
    LinearLayout layout_parentM, rl_content;
    NestedScrollView scroll;
    Button btn_add, save_fab;
    Spinner bran_spin, display_spin, categorytype_spin, subcat_spin;
    EditText desc_edit_txt;
    ImageView img_photoMar;
    CheckBox market_checkbox;
    RecyclerView marketinteligence_list;
    MyAdapter adapter;
    private ArrayAdapter<CharSequence> brand_adapter, display_adapter, category_type_adapter, subcetegory_type_adapter;
    ArrayList<BrandGetterSetter> brandmasterData = new ArrayList<>();
    ArrayList<SubCategoryGetterSetter> subcategoryData = new ArrayList<>();
    ArrayList<CategoryMasterGetterSetter> categorytypeData = new ArrayList<>();

    ArrayList<AssetMasterGetterSetter> categorymasterData = new ArrayList<>();

    ArrayList<MarketIntelligenceGetterSetter> inserteslistData = new ArrayList<>();
    String brand_cdSpinValue = "", brandSpinValue = "", category_cdSpinValue = "", categorySpinValue = "",
            categorytype_cdSpinValue = "", categorytype_SpinValue = "", subcat_cdspinValue = "", subcat_spinValue = "";
    boolean sampleaddflag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additonalvisibility);
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
        scroll = (NestedScrollView) findViewById(R.id.scroll);
        btn_add = (Button) findViewById(R.id.btn_add);
        save_fab = (Button) findViewById(R.id.save_fab);
        layout_parentM = (LinearLayout) findViewById(R.id.layout_parentM);
        rl_content = (LinearLayout) findViewById(R.id.rl_content);
        bran_spin = (Spinner) findViewById(R.id.bran_spin);
        display_spin = (Spinner) findViewById(R.id.display_spin);
        categorytype_spin = (Spinner) findViewById(R.id.categorytype_spin);
        subcat_spin = (Spinner) findViewById(R.id.subcat_spin);
        desc_edit_txt = (EditText) findViewById(R.id.desc_edit_txt);
        img_photoMar = (ImageView) findViewById(R.id.img_photoMar);
        market_checkbox = (CheckBox) findViewById(R.id.market_checkbox);
        marketinteligence_list = (RecyclerView) findViewById(R.id.marketinteligence_list);
        setTitle("Additional Visibility - " + visit_date);
        str = CommonString.FILE_PATH;
        GETALLDATA();
        setDataToListView();
        if (inserteslistData.size() > 0 && !inserteslistData.get(0).isExists()) {
            market_checkbox.setChecked(false);
            marketinteligence_list.setVisibility(View.GONE);
            rl_content.setVisibility(View.GONE);
            layout_parentM.setVisibility(View.GONE);
        } else {
            marketinteligence_list.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.VISIBLE);
            layout_parentM.setVisibility(View.VISIBLE);
        }
        btn_add.setOnClickListener(this);
        save_fab.setOnClickListener(this);
        img_photoMar.setOnClickListener(this);
        market_checkbox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.market_checkbox:
                if (market_checkbox.isChecked()) {
                    if (inserteslistData.size() > 0 && !inserteslistData.get(0).isExists()) {
                        inserteslistData.clear();
                        db.removeallmarketIData(store_cd);
                        save_fab.setText("Save");
//                        adapter.notifyDataSetChanged();
                    }
                    marketinteligence_list.setVisibility(View.VISIBLE);
                    rl_content.setVisibility(View.VISIBLE);
                    layout_parentM.setVisibility(View.VISIBLE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Additonalvisibility.this);
                    builder.setTitle("Parinaam").setMessage(R.string.messageM);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            marketinteligence_list.setVisibility(View.GONE);
                            rl_content.setVisibility(View.GONE);
                            layout_parentM.setVisibility(View.GONE);
                            db.open();
                            if (inserteslistData.size() > 0) {
                                inserteslistData.clear();
                                db.removeallmarketIData(store_cd);
                            }
                            MarketIntelligenceGetterSetter marketIntelligenceG = new MarketIntelligenceGetterSetter();
                            marketIntelligenceG.setCategory("");
                            marketIntelligenceG.setCategory_cd("");
                            marketIntelligenceG.setCompany("");
                            marketIntelligenceG.setCompany_cd("");
                            marketIntelligenceG.setCategory_cd("");
                            marketIntelligenceG.setPromotype("");
                            marketIntelligenceG.setPromotype_cd("");
                            marketIntelligenceG.setSubcategory("");
                            marketIntelligenceG.setSubcategory_cd("");
                            marketIntelligenceG.setRemark("");
                            marketIntelligenceG.setPhoto("");
                            marketIntelligenceG.setExists(false);
                            inserteslistData.add(marketIntelligenceG);
                            sampleaddflag = true;
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }

            case R.id.btn_add:
                if (market_checkbox.isChecked()) {
                    if (validation()) {
                        if (validationDuplication()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Additonalvisibility.this);
                            builder.setMessage("Are you sure you want to add")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    db.open();
                                                    MarketIntelligenceGetterSetter marketIntelligenceG = new MarketIntelligenceGetterSetter();
                                                    marketIntelligenceG.setCategory(categorySpinValue);
                                                    marketIntelligenceG.setCategory_cd(category_cdSpinValue);
                                                    marketIntelligenceG.setCompany(brandSpinValue);
                                                    marketIntelligenceG.setCompany_cd(brand_cdSpinValue);
                                                    marketIntelligenceG.setPromotype(categorytype_SpinValue);
                                                    marketIntelligenceG.setPromotype_cd(categorytype_cdSpinValue);
                                                    marketIntelligenceG.setSubcategory(subcat_spinValue);
                                                    marketIntelligenceG.setSubcategory_cd(subcat_cdspinValue);

                                                    marketIntelligenceG.setRemark(desc_edit_txt.getText().toString().replaceAll("[(!@#$%^&*?)\"]", " ").trim());
                                                    marketIntelligenceG.setPhoto(image1);
                                                    marketIntelligenceG.setExists(true);
                                                    inserteslistData.add(marketIntelligenceG);
                                                    adapter = new MyAdapter(Additonalvisibility.this, inserteslistData);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    marketinteligence_list.setLayoutManager(new LinearLayoutManager(Additonalvisibility.this));
                                                    adapter.notifyDataSetChanged();
                                                    desc_edit_txt.setText("");
                                                    desc_edit_txt.setHint("Remark");
                                                    display_spin.setSelection(0);
                                                    bran_spin.setSelection(0);
                                                    categorytype_spin.setSelection(0);
                                                    subcat_spin.setSelection(0);
                                                    image1 = "";
                                                    sampleaddflag = true;
                                                    img_photoMar.setImageResource(R.drawable.camera_orange);
                                                    Snackbar.make(btn_add, "Data has been added", Snackbar.LENGTH_SHORT).show();
                                                }
                                            })
                                    .setNegativeButton("No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Snackbar.make(btn_add, "Already added ", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
                break;
            case R.id.save_fab:
                if (inserteslistData.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Additonalvisibility.this);
                        builder1.setMessage("Are you sure you want to save data")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                db.insertmarketintelligenceData(store_cd, username, visit_date, inserteslistData);
                                                Additonalvisibility.this.finish();
                                                sampleaddflag = false;
                                                Snackbar.make(btn_add, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();
                    } else {
                        Snackbar.make(btn_add, "Please add data", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(btn_add, "Please add data", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_photoMar:
                _pathforcheck = store_cd + "_ADDITIONAL_VIS_" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();
                startCameraActivity();
        }
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    public boolean validationDuplication() {
        boolean value = true;
        if (inserteslistData.size() > 0) {
            for (int i = 0; i < inserteslistData.size(); i++) {
                if (inserteslistData.get(i).getCompany_cd().equals(brand_cdSpinValue) &&
                        inserteslistData.get(i).getCategory_cd().equals(category_cdSpinValue) &&
                        inserteslistData.get(i).getPromotype_cd().equals(categorytype_cdSpinValue)) {
                    value = false;
                    break;
                }
            }
        }

        return value;
    }


    public void GETALLDATA() {
        db.open();

        categorymasterData = db.getassetData();
        categorytypeData = db.getcategorymasterData();
        display_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        display_adapter.add("-Select Display Type-");
        for (int i = 0; i < categorymasterData.size(); i++) {
            display_adapter.add(categorymasterData.get(i).getAsset().get(0));
        }
        display_spin.setAdapter(display_adapter);
        display_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//brand
        brand_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        brand_adapter.add("-Select Brand-");
        for (int i = 0; i < brandmasterData.size(); i++) {
            brand_adapter.add(brandmasterData.get(i).getBrand().get(0));
        }

        bran_spin.setAdapter(brand_adapter);
        brand_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //promotype
        category_type_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        category_type_adapter.add("-Select Category-");

        for (int i = 0; i < categorytypeData.size(); i++) {
            category_type_adapter.add(categorytypeData.get(i).getCategory().get(0));
        }
        categorytype_spin.setAdapter(category_type_adapter);
        category_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //subcategory type
        subcetegory_type_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        subcetegory_type_adapter.add("-Select Sub Category-");

        for (int i = 0; i < subcategoryData.size(); i++) {
            subcetegory_type_adapter.add(subcategoryData.get(i).getSUB_CATEGORY().get(0));
        }
        subcat_spin.setAdapter(subcetegory_type_adapter);
        subcetegory_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorytype_spin.setOnItemSelectedListener(this);
        subcat_spin.setOnItemSelectedListener(this);
        display_spin.setOnItemSelectedListener(this);
        bran_spin.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.display_spin:

                if (position != 0) {
                    category_cdSpinValue = categorymasterData.get(position - 1).getAsset_cd().get(0);
                    categorySpinValue = categorymasterData.get(position - 1).getAsset().get(0);
                }

                break;
            case R.id.bran_spin:

                if (position != 0) {
                    brand_cdSpinValue = brandmasterData.get(position - 1).getBrand_cd().get(0);
                    brandSpinValue = brandmasterData.get(position - 1).getBrand().get(0);
                } else {
                    brand_cdSpinValue = "";
                    brandSpinValue = "0";
                }
                break;
            case R.id.categorytype_spin:
                // category type
                subcategoryData.clear();
                brandmasterData.clear();
                subcetegory_type_adapter.clear();
                subcetegory_type_adapter.add("-Select Sub Category-");
                if (position != 0) {
                    categorytype_cdSpinValue = categorytypeData.get(position - 1).getCategory_cd().get(0);
                    categorytype_SpinValue = categorytypeData.get(position - 1).getCategory().get(0);


                    subcategoryData = db.getsubcategoryTypeData(categorytype_cdSpinValue);
                    for (int i = 0; i < subcategoryData.size(); i++) {
                        subcetegory_type_adapter.add(subcategoryData.get(i).getSUB_CATEGORY().get(0));
                    }
                    subcat_spin.setAdapter(subcetegory_type_adapter);
                    subcetegory_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                } else {
                    categorytype_cdSpinValue = "";
                    categorytype_SpinValue = "0";
                }


                break;
            case R.id.subcat_spin:
                //sub category type
                brandmasterData.clear();
                brand_adapter.clear();
                brand_adapter.add("-Select Brand-");
                if (position != 0) {
                    subcat_cdspinValue = subcategoryData.get(position - 1).getSUB_CATEGORY_CD().get(0);
                    subcat_spinValue = subcategoryData.get(position - 1).getSUB_CATEGORY().get(0);

                    brandmasterData = db.getbrandData(subcat_cdspinValue);
                    for (int i = 0; i < brandmasterData.size(); i++) {
                        brand_adapter.add(brandmasterData.get(i).getBrand().get(0));
                    }
                    bran_spin.setAdapter(brand_adapter);
                    brand_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                } else {
                    subcat_cdspinValue = "";
                    subcat_spinValue = "0";
                }

                break;
        }
        ((Spinner)parent).invalidate();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Crashlytics.logException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
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

                        img_photoMar.setImageResource(R.drawable.camera_green);
                        image1 = _pathforcheck;
                    }
                    _pathforcheck = "";
                }
                break;
        }

    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
    }


    public boolean validation() {
        boolean value = true;
        if (market_checkbox.isChecked()) {
            if (categorytype_spin.getSelectedItem().toString().equalsIgnoreCase("-Select Category-")) {
                value = false;
                showMessage("Please Select Category Dropdown");

            } else if (subcat_spin.getSelectedItem().toString().equalsIgnoreCase("-Select Sub Category-")) {
                value = false;
                showMessage("Please Select Sub Category Dropdown");
            }else if (bran_spin.getSelectedItem().toString().equalsIgnoreCase("-Select Brand-")) {
                value = false;
                showMessage("Please  Brand Dropdown");

            } else if (display_spin.getSelectedItem().toString().equalsIgnoreCase("-Select Category-")) {
                value = false;
                showMessage("Please Select Display Type Dropdown");
            }  else if (desc_edit_txt.getText().toString().replaceAll("[(!@#$%^&*?)\"]", " ").trim().isEmpty()) {
                value = false;
                showMessage("Please Enter Remark");
            } else if (image1.equals("")) {
                value = false;
                showMessage("Please Capture Photo");
            }
        }
        return value;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    finish();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                finish();
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
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<MarketIntelligenceGetterSetter> insertedlist_Data;

        MyAdapter(Context context, ArrayList<MarketIntelligenceGetterSetter> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;

        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.secondary_adapter, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Additonalvisibility.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(Additonalvisibility.this, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Additonalvisibility.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.remove(listid);
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(Additonalvisibility.this, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
            });

            holder.txt_comp.setText(insertedlist_Data.get(position).getCompany());
            holder.txt_cate.setText(insertedlist_Data.get(position).getCategory());
            holder.txt_pro.setText(insertedlist_Data.get(position).getPromotype());

            holder.txt_comp.setId(position);
            holder.txt_cate.setId(position);
            holder.txt_pro.setId(position);
            holder.remove.setId(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_comp, txt_cate, txt_pro;
            ImageView remove;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_comp = (TextView) convertView.findViewById(R.id.txt_comp);
                txt_pro = (TextView) convertView.findViewById(R.id.txt_pro);
                txt_cate = (TextView) convertView.findViewById(R.id.txt_cate);
                remove = (ImageView) convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    public void setDataToListView() {
        try {
            inserteslistData = db.getinsertedMarketIntelligenceData(store_cd, visit_date);
            if (inserteslistData.size() > 0) {
                save_fab.setText("Update");
                Collections.reverse(inserteslistData);
                adapter = new MyAdapter(this, inserteslistData);
                marketinteligence_list.setAdapter(adapter);
                marketinteligence_list.setLayoutManager(new LinearLayoutManager(Additonalvisibility.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching", e.toString());
        }
    }


}
