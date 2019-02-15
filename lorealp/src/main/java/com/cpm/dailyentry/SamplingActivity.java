package com.cpm.dailyentry;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ToggleButton;

import com.cpm.Constants.CommonString;
import com.cpm.lorealpromoter.R;
import com.cpm.database.GSKDatabase;
import com.cpm.xmlGetterSetter.CategoryMasterGetterSetter;
import com.cpm.xmlGetterSetter.FeedbackGetterSetter;
import com.cpm.xmlGetterSetter.FeedbackMasterGetterSetter;
import com.cpm.xmlGetterSetter.MarketIntelligenceGetterSetter;
import com.cpm.xmlGetterSetter.SampledGetterSetter;
import com.cpm.xmlGetterSetter.SamplingMasterGetterSetter;
import com.cpm.xmlGetterSetter.SkuMasterGetterSetter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class SamplingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    EditText sample_mobile, sample_name;

    GSKDatabase db;
    String store_cd, visit_date, username, _pathforcheck, _path, str, image1 = "";
    String sku_cdSpinValue = "", skuSpinValue = "", category_cdSpinValue = "", categorySpinValue = "", toggle_Value = "NO";
    private SharedPreferences preferences;
    LinearLayout rl_img, rl_feedback, rl_all_data, rl_content;
    NestedScrollView scroll;
    Button btn_add, save_fab;
    Spinner categorysample_spin, skusample_spin;
    EditText sample_feedback_txt;
    ImageView img_sampled;
    RecyclerView sample_list;
    ToggleButton toogle_sampleV;
    MyAdapter adapter;
    private ArrayAdapter<CharSequence> categoryAdapter, skuAdapter;
    ArrayList<SamplingMasterGetterSetter> sampleData = new ArrayList<>();
    ArrayList<FeedbackMasterGetterSetter> skuData = new ArrayList<>();
    ArrayList<SampledGetterSetter> insertedDataList = new ArrayList<>();
    boolean sampleaddflag = false;
    CheckBox sampling_checkbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampling);
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
        rl_img = (LinearLayout) findViewById(R.id.rl_img);
        rl_feedback = (LinearLayout) findViewById(R.id.rl_feedback);
        categorysample_spin = (Spinner) findViewById(R.id.categorysample_spin);
        skusample_spin = (Spinner) findViewById(R.id.skusample_spin);
        sample_feedback_txt = (EditText) findViewById(R.id.sample_feedback_txt);
        img_sampled = (ImageView) findViewById(R.id.img_sampled);
        sample_list = (RecyclerView) findViewById(R.id.sample_list);
        toogle_sampleV = (ToggleButton) findViewById(R.id.toogle_sampleV);
        sample_mobile = (EditText) findViewById(R.id.sample_mobile);
        sample_name = (EditText) findViewById(R.id.sample_name);
        ////new changessss
        sampling_checkbox = (CheckBox) findViewById(R.id.sampling_checkbox);
        rl_all_data = (LinearLayout) findViewById(R.id.rl_all_data);
        rl_content = (LinearLayout) findViewById(R.id.rl_content);

        setTitle("Sampling - " + visit_date);
        str = CommonString.FILE_PATH;
        btn_add.setOnClickListener(this);
        save_fab.setOnClickListener(this);
        img_sampled.setOnClickListener(this);
        toogle_sampleV.setOnClickListener(this);
        sampling_checkbox.setOnClickListener(this);

        GETALLDATA();
        setDataToListView();

        if (insertedDataList.size() > 0 && !insertedDataList.get(0).isExists()) {
            sampling_checkbox.setChecked(false);
            sample_list.setVisibility(View.GONE);
            rl_content.setVisibility(View.GONE);
            rl_all_data.setVisibility(View.GONE);
        } else {
            sample_list.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.VISIBLE);
            rl_all_data.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sampling_checkbox:
                if (sampling_checkbox.isChecked()) {
                    if (insertedDataList.size() > 0 && !insertedDataList.get(0).isExists()) {
                        insertedDataList.clear();
                        db.removeallmarketIData(store_cd);
                        save_fab.setText("Save");
                    }
                    sample_list.setVisibility(View.VISIBLE);
                    rl_content.setVisibility(View.VISIBLE);
                    rl_all_data.setVisibility(View.VISIBLE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Parinaam").setMessage(R.string.messageM);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sample_list.setVisibility(View.GONE);
                            rl_content.setVisibility(View.GONE);
                            rl_all_data.setVisibility(View.GONE);
                            db.open();
                            if (insertedDataList.size() > 0) {
                                insertedDataList.clear();
                                db.removeall_sampled_Data(store_cd);
                            }

                            SampledGetterSetter sampledG = new SampledGetterSetter();
                            sampledG.setCategory("");
                            sampledG.setCategory_cd("0");
                            // sampledG.setSku_cd(sku_cdSpinValue);
                            sampledG.setSku("");
                            //  sampledG.setSampled(toggle_Value);
                            sampledG.setFeedback("");
                            sampledG.setMobile("");
                            sampledG.setName("");
                            sampledG.setSampled_img("");
                            sampledG.setExists(false);
                            insertedDataList.add(sampledG);

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


            case R.id.toogle_sampleV:
                if (toogle_sampleV.isChecked()) {
                    toggle_Value = "YES";
                    rl_img.setVisibility(View.VISIBLE);
                    rl_feedback.setVisibility(View.GONE);
                } else {
                    toggle_Value = "NO";
                    rl_img.setVisibility(View.GONE);
                    rl_feedback.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_sampled:
                _pathforcheck = store_cd + "_SAMPLEDIMG_" + visit_date.replace("/", "") + "_"
                        + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                startCameraActivity();
                break;
            case R.id.btn_add:
                if (sampling_checkbox.isChecked()) {
                    if (validation()) {
                      //  if (validationDuplicateSku()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SamplingActivity.this).setTitle("Parinaam");
                            builder.setMessage("Are you sure you want to add ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    db.open();
                                                    SampledGetterSetter sampledG = new SampledGetterSetter();
                                                    sampledG.setCategory(categorySpinValue);
                                                    sampledG.setCategory_cd(category_cdSpinValue);
                                                    // sampledG.setSku_cd(sku_cdSpinValue);
                                                    sampledG.setSku(skuSpinValue);
                                                    //  sampledG.setSampled(toggle_Value);
                                                    sampledG.setFeedback(sample_feedback_txt.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                                    sampledG.setMobile(sample_mobile.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                                    sampledG.setName(sample_name.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                                    sampledG.setSampled_img(image1);
                                                    sampledG.setExists(true);

                                                    insertedDataList.add(sampledG);
                                                    adapter = new MyAdapter(SamplingActivity.this, insertedDataList);
                                                    sample_list.setAdapter(adapter);
                                                    sample_list.setLayoutManager(new LinearLayoutManager(SamplingActivity.this));
                                                    adapter.notifyDataSetChanged();
                                                    img_sampled.setImageResource(R.drawable.camera_black);
                                                    sample_feedback_txt.setText("");
                                                    sample_mobile.setText("");
                                                    sample_name.setText("");
                                                    sample_feedback_txt.setHint("Feedback");
                                                    categorysample_spin.setSelection(0);
                                                    skusample_spin.setSelection(0);
                                                    toogle_sampleV.setChecked(false);
                                                    rl_img.setVisibility(View.GONE);
                                                    rl_feedback.setVisibility(View.VISIBLE);
                                                    //  toggle_Value = "NO";
                                                    image1 = "";
                                                    sampleaddflag = true;
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
                       // }
                        /*else {
                            Snackbar.make(btn_add, "Already added this sample.", Snackbar.LENGTH_SHORT).show();
                        }*/
                    }

                }
                break;
            case R.id.save_fab:
                if (insertedDataList.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SamplingActivity.this).setTitle("Parinaam");
                        builder1.setMessage("Are you sure you want to save data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                db.insertSampledData(store_cd, username, visit_date, insertedDataList);
                                                finish();
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
                        Snackbar.make(btn_add, "Please add sample data", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(btn_add, "Please add sample data", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    public void GETALLDATA() {
        db.open();
        //for category
        sampleData = db.getSamplingData(store_cd);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.add("-Select-");
        for (int i = 0; i < sampleData.size(); i++) {
            categoryAdapter.add(sampleData.get(i).getSAMPLE().get(0));
        }
        categorysample_spin.setAdapter(categoryAdapter);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorysample_spin.setOnItemSelectedListener(this);


        //for sku
        skuData = db.getFeedbackMasterData();
        skuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        skuAdapter.add("-Select-");
        for (int i = 0; i < skuData.size(); i++) {
            skuAdapter.add(skuData.get(i).getFEEDBACK().get(0));
        }
        skusample_spin.setAdapter(skuAdapter);
        skuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skusample_spin.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.categorysample_spin:
                if (position != 0) {
                    category_cdSpinValue = sampleData.get(position - 1).getSAMPLE_CD().get(0);
                    categorySpinValue = sampleData.get(position - 1).getSAMPLE().get(0);

                }
                break;
            case R.id.skusample_spin:
                if (!category_cdSpinValue.equals("")) {
                    if (position != 0) {
                        //  sku_cdSpinValue = skuData.get(position - 1).getSku_cd().get(0);
                        skuSpinValue = skuData.get(position - 1).getFEEDBACK().get(0);
                    }
                }/* else {
                    Snackbar.make(btn_add, "Please First select category", Snackbar.LENGTH_SHORT).show();
                }*/
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<SampledGetterSetter> insertedlist_Data;

        MyAdapter(Context context, ArrayList<SampledGetterSetter> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;
        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.secondary_adapter_sample, parent, false);
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SamplingActivity.this).setTitle("Parinaam");
                        builder.setMessage("Are you sure you want to Delete ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(SamplingActivity.this, insertedlist_Data);
                                                    sample_list.setAdapter(adapter);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(SamplingActivity.this).setTitle("Parinaam");
                        builder.setMessage("Are you sure you want to Delete ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.removesampledata(listid);
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(SamplingActivity.this, insertedlist_Data);
                                                    sample_list.setAdapter(adapter);
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

            holder.txt_cat.setText(insertedlist_Data.get(position).getCategory());
            holder.txt_sku.setText(insertedlist_Data.get(position).getSku());
            holder.txt_samp.setText(insertedlist_Data.get(position).getSampled());
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_cat, txt_sku, txt_samp;
            ImageView remove;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_cat = (TextView) convertView.findViewById(R.id.txt_cat);
                txt_sku = (TextView) convertView.findViewById(R.id.txt_sku);
                txt_samp = (TextView) convertView.findViewById(R.id.txt_samp);
                remove = (ImageView) convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean validation() {
        boolean value = true;
        if (sampling_checkbox.isChecked()) {
            if (categorysample_spin.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
                value = false;
                showMessage("Please Select Sampling Name Dropdown");
            } else if (skusample_spin.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
                value = false;
                showMessage("Please Select Feedback Dropdown");
            }
      /*  if (categorysample_spin.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
            value = false;
            showMessage("Please Select Sampling Name Dropdown");
        }
        else if (sample_mobile.getText().toString().length() < 10) {
            value = false;
            showMessage(CommonString.stpcontactnolenght);
        }
        else if (sample_name.getText().toString().isEmpty()) {
            value = false;
            showMessage("Please fill Name");
        }else if (skusample_spin.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
            value = false;
            showMessage("Please Select Feedback Dropdown");
        }*/

        }

        return value;
    }

    /* public boolean validationDuplicateSku() {
         boolean value = true;
         if (insertedDataList.size() > 0) {
             for (int i = 0; i < insertedDataList.size(); i++) {
                 if (insertedDataList.get(i).getCategory_cd().equals(category_cdSpinValue)
                         && insertedDataList.get(i).getSku_cd().equals(sku_cdSpinValue)) {
                     value = false;
                 }
             }
         }

         return value;
     }*/
    public boolean validationDuplicateSku() {
        boolean value = true;
        if (insertedDataList.size() > 0) {
            for (int i = 0; i < insertedDataList.size(); i++) {
                if (insertedDataList.get(i).getCategory_cd().equals(category_cdSpinValue)) {
                    value = false;
                    break;
                }
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

    protected void startCameraActivity() {

        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
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
                        img_sampled.setImageResource(R.drawable.camera_green);
                        image1 = _pathforcheck;
                    }
                    _pathforcheck = "";
                }
                break;
        }

    }

    public void setDataToListView() {
        try {
            insertedDataList = db.getinsertedsampledData(store_cd, visit_date);
            if (insertedDataList.size() > 0) {
                Collections.reverse(insertedDataList);
                adapter = new MyAdapter(this, insertedDataList);
                sample_list.setAdapter(adapter);
                sample_list.setLayoutManager(new LinearLayoutManager(SamplingActivity.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
        }
    }


}
