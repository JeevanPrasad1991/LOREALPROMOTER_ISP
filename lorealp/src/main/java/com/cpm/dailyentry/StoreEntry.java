package com.cpm.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.NavMenuItemGetterSetter;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.MappingMenuOptionGetterSetter;
import com.cpm.xmlGetterSetter.SamplingMasterGetterSetter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreEntry extends AppCompatActivity {
    GSKDatabase db;
    private SharedPreferences preferences;
    String store_cd, visit_date, account_cd, city_cd, storetype_cd, floor_status, backroom_status;
    boolean food_flag;
    String user_type = "", region_Id, channel_cd;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<MappingMenuOptionGetterSetter> mappingmenu_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_recycle_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new GSKDatabase(getApplicationContext());
        db.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        account_cd = preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd = preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd = preferences.getString(CommonString.KEY_STORETYPE_CD, null);
        floor_status = preferences.getString(CommonString.KEY_FLOOR_STATUS, null);
        backroom_status = preferences.getString(CommonString.KEY_BACKROOK_STATUS, null);
        food_flag = preferences.getBoolean(CommonString.KEY_FOOD_STORE, false);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        region_Id = preferences.getString(CommonString.KEY_REGION_Id, "");
        channel_cd = preferences.getString(CommonString.KEY_CHANNEL_CD, null);
        setTitle("Store Entry - " + visit_date);
        mappingmenu_List = db.getmappingMenuEntryByRegion(region_Id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        adapter = new ValueAdapter(getApplicationContext(), getdata(mappingmenu_List));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        if (setCheckOutDatabyregion(mappingmenu_List)) {
            db.updateCoverageStatusNew(store_cd, CommonString.KEY_VALID);
            db.updateStoreStatusOnCheckout(store_cd, visit_date, CommonString.KEY_VALID);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }
        return super.onOptionsItemSelected(item);
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<MappingMenuOptionGetterSetter> data;

        public ValueAdapter(Context context, List<MappingMenuOptionGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final MappingMenuOptionGetterSetter current = data.get(position);
            viewHolder.icon_txtname.setText(current.getMenu_name().get(0));
            File imgFile = new File(CommonString.FILE_PATH_MENU_ICONS + current.getJudge_imagename());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.icon.setImageBitmap(myBitmap);
            }

            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Openning activty.
                    if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Opening Stock Category")
                            && !current.getJudge_imagename().trim().equalsIgnoreCase("opening_stock_gray.png")) {
                        if (!db.isClosingDataFilled(store_cd)) {
                            Intent in1 = new Intent(getApplicationContext(), OpeningStock.class).putExtra(CommonString.KEY_NAME, current.getMenu_name().get(0));
                            startActivity(in1);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "Data cannot be changed.", Snackbar.LENGTH_SHORT).show();
                        }
                    } else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Floor Opening Stock")
                            && !current.getJudge_imagename().trim().equalsIgnoreCase("opening_stock_gray.png")) {
                        if (!db.isMiddayDataFilled(store_cd)) {
                            Intent in1 = new Intent(getApplicationContext(), OpeningStock.class).putExtra(CommonString.KEY_NAME, current.getMenu_name().get(0));
                            startActivity(in1);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "Data cannot be changed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    //  isClosingDataFilled
                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Stock In") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("midday_stock_gray.png")) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            if (db.isStockBackRoomDataAllFilled(store_cd)) {
                                Intent in3 = new Intent(getApplicationContext(), MidDayStock.class).putExtra(CommonString.KEY_NAME, current.getMenu_name().get(0));
                                startActivity(in3);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            } else {
                                Snackbar.make(recyclerView, "First fill Backroom Opening Stock Data.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Category Data.", Snackbar.LENGTH_SHORT).show();
                        }

                    } else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Mid Day Stock") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("midday_stock_gray.png")) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            if (!db.isClosingDataFilled(store_cd)) {
                                Intent in3 = new Intent(getApplicationContext(), MidDayStock.class).putExtra(CommonString.KEY_NAME, current.getMenu_name().get(0));
                                startActivity(in3);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            } else {
                                Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Category Data.", Snackbar.LENGTH_SHORT).show();
                        }

                    } else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Share of Shelf") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("share_of_shelf_gray.png")) {
                        Intent in3 = new Intent(getApplicationContext(), ShareOfShelfActivity.class);
                        startActivity(in3);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Additional Visibility") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("additionalvisibility_gray.png")) {
                        Intent in7 = new Intent(getApplicationContext(), Additonalvisibility.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
                    ///promotion
                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Promotion") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("promotion_gray.png")) {
                        Intent in4 = new Intent(getApplicationContext(), PromotionActivity.class);
                        startActivity(in4);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }

                    //asset
                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Paid Visibility") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("paidvisibility_gray.png")) {
                        Intent in5 = new Intent(getApplicationContext(), PaidVisibilityActivity.class);
                        startActivity(in5);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }

                    //sample
                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Sampling")
                            && !current.getJudge_imagename().trim().equalsIgnoreCase("sampling_gray.png")) {
                        Intent in5 = new Intent(getApplicationContext(), SamplingActivity.class);
                        startActivity(in5);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Closing Stock Category") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("closing_stock_gray.png")) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            if (db.isMiddayDataFilled(store_cd)) {
                                Intent in2 = new Intent(getApplicationContext(), ClosingStock.class);
                                startActivity(in2);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            } else {
                                Snackbar.make(recyclerView, "First fill Mid Day Stock Data.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Category Data.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    //opennig stock back room activity
                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Backroom Opening Stock") &&
                            !current.getJudge_imagename().trim().equalsIgnoreCase("openingstock_backroom_gray.png")) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            if (!db.isMiddayDataFilled(store_cd)){
                                Intent in8 = new Intent(getApplicationContext(), OpenningStockBackofficeActivity.class);
                                startActivity(in8);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }else {
                                Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                            }

                        } else {
                            Snackbar.make(recyclerView, "First fill Floor Opening Stock Data.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    //closing stock back room activity
//                    else if (current.getMenu_name().get(0).trim().equalsIgnoreCase("Closing Stock Backroom")) {
//                        if (db.isOpeningBackOfficeDataFilled(store_cd)) {
//                            if (db.isMiddayDataFilled(store_cd)) {
//                                if (db.isClosingDataFilled(store_cd)) {
//                                    Intent in8 = new Intent(getApplicationContext(), ClosingStockBackofficeActvity.class);
//                                    startActivity(in8);
//                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
//                                } else {
//                                    Snackbar.make(recyclerView, "First fill Closing Stock Floor Data.", Snackbar.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Snackbar.make(recyclerView, "First fill Stock In Data.", Snackbar.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Snackbar.make(recyclerView, "First  fill Opening Stock floor,Opening Stock Backroom, Stock in Data.", Snackbar.LENGTH_SHORT).show();
//                        }
                    //                }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView icon_txtname;
            ImageView icon;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
                icon_txtname = (TextView) itemView.findViewById(R.id.icon_txtname);
            }
        }

    }


    public List<MappingMenuOptionGetterSetter> getdata(ArrayList<MappingMenuOptionGetterSetter> mapping_menuList) {
        if (mapping_menuList.size() > 0) {
            for (int k = 0; k < mapping_menuList.size(); k++) {
                if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Opening Stock Category")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                }
               else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Floor Opening Stock")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                }

                else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Stock In")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isMiddayDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Mid Day Stock")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isMiddayDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Share of Shelf")) {
                    if (db.getHeaderShareOfSelfImageData(store_cd).size() > 0) {
                        if (db.isShareOfShelfDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }

                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Paid Visibility")) {
                    if (db.getAssetCategoryData(store_cd).size() > 0) {
                        if (db.isAssetDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Additional Visibility")) {
                    if (db.getcategorymasterData().size() > 0) {
                        if (db.getinsertedMarketIntelligenceData(store_cd, visit_date).size() > 0) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }

                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Promotion")) {
                    if (db.getPromotionBrandData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isPromotionDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));

                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Closing Stock Category")) {
                    if (db.getmappingStockDataNew(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isClosingDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Sampling")) {
                    if (db.getSamplingData(store_cd).size() > 0) {
                        if (db.issampledDataFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));
                    }

                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Backroom Opening Stock")) {
                    if (db.getStockAvailabilityDataNew(channel_cd).size() > 0) {
                        if (db.isStockBackRoomDataAllFilled(store_cd)) {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_done().get(0));
                        } else {
                            mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon().get(0));
                        }
                    } else {
                        mapping_menuList.get(k).setJudge_imagename(mapping_menuList.get(k).getMenu_icon_gray().get(0));

                    }
                }
            }
        }

        return mapping_menuList;
    }


    public boolean setCheckOutDatabyregion(ArrayList<MappingMenuOptionGetterSetter> mapping_menuList) {
        boolean flag = true;
        if (mapping_menuList.size() > 0) {
            for (int k = 0; k < mapping_menuList.size(); k++) {
                if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Opening Stock Category")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                }
               else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Floor Opening Stock")) {
                    if (db.getStockAvailabilityData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                }

                else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Stock In")) {
                    if (flag && db.getmappingStockDataNew(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (flag && db.isMiddayDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Mid Day Stock")) {
                    if (flag && db.getmappingStockDataNew(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (flag && db.isMiddayDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Share of Shelf")) {
                    if (flag && db.isShareOfShelfDataFilled(store_cd)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Paid Visibility")) {
                    if (flag && db.getAssetCategoryData(store_cd).size() > 0) {
                        if (flag && db.isAssetDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Additional Visibility")) {
                    if (flag && db.getcategorymasterData().size() > 0) {
                        if (flag && db.getinsertedMarketIntelligenceData(store_cd, visit_date).size() > 0) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Promotion")) {
                    if (flag && db.getPromotionBrandData1(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (flag && db.isPromotionDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Closing Stock Category")) {
                    if (flag && db.getmappingStockDataNew(account_cd, city_cd, storetype_cd).size() > 0) {
                        if (flag && db.isClosingDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Sampling")) {
                    if (flag && db.getSamplingData(store_cd).size() > 0) {
                        if (flag && db.issampledDataFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                } else if (mapping_menuList.get(k).getMenu_name().get(0).trim().equalsIgnoreCase("Backroom Opening Stock")) {
                    if (flag && db.getStockAvailabilityDataNew(channel_cd).size() > 0) {
                        if (flag && db.isStockBackRoomDataAllFilled(store_cd)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                }
            }
        }

        return flag;
    }

}
