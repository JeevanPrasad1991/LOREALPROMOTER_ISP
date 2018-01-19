package com.cpm.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.NavMenuItemGetterSetter;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreEntry extends AppCompatActivity {
    GSKDatabase db;
    private SharedPreferences preferences;
    String store_cd, visit_date,account_cd,city_cd,storetype_cd;
    boolean food_flag;
    String user_type = "";
    ValueAdapter adapter;
    RecyclerView recyclerView;

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

        account_cd= preferences.getString(CommonString.KEY_KEYACCOUNT_CD, null);
        city_cd= preferences.getString(CommonString.KEY_CITY_CD, null);
        storetype_cd= preferences.getString(CommonString.KEY_STORETYPE_CD, null);

        food_flag = preferences.getBoolean(CommonString.KEY_FOOD_STORE, false);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        setTitle("Store Entry - " + visit_date);
        user_type="Promoter";
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        adapter = new ValueAdapter(getApplicationContext(), getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        if (setCheckOutData()) {
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
        List<NavMenuItemGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
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
            final NavMenuItemGetterSetter current = data.get(position);
            viewHolder.icon.setImageResource(current.getIconImg());
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Openning activty.
                    if (current.getIconImg() == R.drawable.opening_stock || current.getIconImg() == R.drawable.opening_stock_done) {
                       if (!db.isClosingDataFilled(store_cd)) {
                            Intent in1 = new Intent(getApplicationContext(), OpeningStock.class);
                            startActivity(in1);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    //midday activty
/*
                    if (current.getIconImg() == R.drawable.midday_stock || current.getIconImg() == R.drawable.midday_stock_done) {
                        if (db.isClosingDataFilled(store_cd)) {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                        } else if (db.isOpeningDataFilled(store_cd) && db.isOpeningBackOfficeDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), StockInActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Data", Snackbar.LENGTH_SHORT).show();
                        }
                    }
*/
/*
                    if (current.getIconImg() == R.drawable.midday_stock || current.getIconImg() == R.drawable.midday_stock_done) {
                        if (db.isClosingDataFilled(store_cd)) {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                        } else if (db.isOpeningDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), StockInActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                        }
                    }
*/
                    if (current.getIconImg() == R.drawable.midday_stock || current.getIconImg() == R.drawable.midday_stock_done) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), StockInActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                        }

                       /* if (db.isClosingDataFilled(store_cd)) {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                        } else if (db.isOpeningDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), StockInActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                        }*/
                    }


                    //upe today 12
/*
                    if (current.getIconImg() == R.drawable.share_of_shelf || current.getIconImg() == R.drawable.share_of_shelf_done) {
                        if (db.isClosingDataFilled(store_cd)) {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                        } else if (db.isOpeningDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), ShareOfShelfActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                        }

                    }
*/
                    if (current.getIconImg() == R.drawable.share_of_shelf || current.getIconImg() == R.drawable.share_of_shelf_done) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            Intent in3 = new Intent(getApplicationContext(), ShareOfShelfActivity.class);
                            startActivity(in3);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    //clossing
/*
                    if (current.getIconImg() == R.drawable.closing_stock || current.getIconImg() == R.drawable.closing_stock_done) {
                        if (db.isOpeningDataFilled(store_cd) && db.isOpeningBackOfficeDataFilled(store_cd)) {
                            if (db.isMiddayDataFilled(store_cd)) {
                                if (!db.isClosingBackOfficeDataFilled(store_cd)) {
                                    Intent in2 = new Intent(getApplicationContext(), ClosingStock.class);
                                    startActivity(in2);
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                } else {
                                    Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(recyclerView, "First fill Midday Stock Data", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock data", Snackbar.LENGTH_SHORT).show();
                        }

                    }
*/


                    if (current.getIconImg() == R.drawable.audit || current.getIconImg() == R.drawable.audit_done) {
                      /*  if (db.isOpeningDataFilled(store_cd)) {*/
                            Intent in7 = new Intent(getApplicationContext(), Additonalvisibility.class);
                            startActivity(in7);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                       /* } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Data", Snackbar.LENGTH_SHORT).show();
                        }*/


                    }


                   /* if (current.getIconImg() == R.drawable.closing_stock || current.getIconImg() == R.drawable.closing_stock_done) {
                        if (db.isOpeningDataFilled(store_cd)) {
                            if (db.isMiddayDataFilled(store_cd)) {
                                if (!db.isClosingBackOfficeDataFilled(store_cd)) {
                                    Intent in2 = new Intent(getApplicationContext(), ClosingStock.class);
                                    startActivity(in2);
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                } else {
                                    Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(recyclerView, "First fill Midday Stock Data", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock data", Snackbar.LENGTH_SHORT).show();
                        }

                    }*/

                    ///promotion
                    if (current.getIconImg() == R.drawable.promotion || current.getIconImg() == R.drawable.promotion_done) {

                            Intent in4 = new Intent(getApplicationContext(), PromotionActivity.class);
                            startActivity(in4);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }
                    //asset
                    if (current.getIconImg() == R.drawable.asset || current.getIconImg() == R.drawable.asset_done) {

        /*                if (db.isOpeningDataFilled(store_cd)) {*/
                            Intent in5 = new Intent(getApplicationContext(), PaidVisibilityActivity.class);
                            startActivity(in5);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                       /* } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Data", Snackbar.LENGTH_SHORT).show();
                        }*/

                    }

                   /* if (current.getIconImg() == R.drawable.c_add_display || current.getIconImg() == R.drawable.c_add_display_done) {
                        Intent in5 = new Intent(getApplicationContext(), AdditionalPOIActivity.class);
                        startActivity(in5);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
*/
                    //SampleActivity
/*
                    if (current.getIconImg() == R.drawable.competition || current.getIconImg() == R.drawable.competition_done) {
                        Intent in7 = new Intent(getApplicationContext(), SampleActivity.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
*/


                    if (current.getIconImg() == R.drawable.closing_stock || current.getIconImg() == R.drawable.closing_stock_done) {
                        if (db.isOpeningDataFilled(store_cd)) {
                           // if (db.isMiddayDataFilled(store_cd)) {
                                if (!db.isClosingBackOfficeDataFilled(store_cd)) {
                                    Intent in2 = new Intent(getApplicationContext(), ClosingStock.class);
                                    startActivity(in2);
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                } else {
                                    Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                                }
                           // }
                            /*else {
                                Snackbar.make(recyclerView, "First fill Midday Stock Data", Snackbar.LENGTH_SHORT).show();
                            }*/
                        } else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor data", Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    //opennig stock back room activity
                    if (current.getIconImg() == R.drawable.opening_stock_backroom_done || current.getIconImg() == R.drawable.opening_stock_backroom) {
                        if (db.isOpeningDataFilled(store_cd)) {
                           if (!db.isClosingBackOfficeDataFilled(store_cd)) {
                                Intent in8 = new Intent(getApplicationContext(), OpenningStockBackofficeActivity.class);
                                startActivity(in8);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            } else {
                                Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_SHORT).show();
                            }
                        }else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Floor data", Snackbar.LENGTH_SHORT).show();
                        }
                   }

                    //closing stock back room activity
                    if (current.getIconImg() == R.drawable.closing_stock_backroom || current.getIconImg() == R.drawable.closing_stock_backroom_done) {
                        if (db.isOpeningBackOfficeDataFilled(store_cd) ) {
                          //  if (db.isMiddayDataFilled(store_cd)) {
                                if (db.isClosingDataFilled(store_cd)) {
                                    Intent in8 = new Intent(getApplicationContext(), ClosingStockBackofficeActvity.class);
                                    startActivity(in8);
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                } else {
                                    Snackbar.make(recyclerView, "First fill Closing Stock Floor Data", Snackbar.LENGTH_SHORT).show();
                                }
                            } /*else {
                                Snackbar.make(recyclerView, "First fill Midday Stock Data", Snackbar.LENGTH_SHORT).show();
                            }
                        } */else {
                            Snackbar.make(recyclerView, "First fill Opening Stock Backroom Data", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            //TextView txt;
            ImageView icon;
            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
            }
        }
    }

    public List<NavMenuItemGetterSetter> getdata() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();

        int openingImg, openningstockbackoffice, middayImg,marketIntelligence , closingBackoffice, promotionImg, assetImg,shareofshelf, closingImg;

        if (db.isOpeningDataFilled(store_cd)) {
            openingImg = R.drawable.opening_stock_done;
        } else {
            openingImg = R.drawable.opening_stock;
        }
        if (db.isOpeningBackOfficeDataFilled(store_cd)) {
            openningstockbackoffice = R.drawable.opening_stock_backroom_done;
        } else {
            openningstockbackoffice = R.drawable.opening_stock_backroom;
        }
        if (db.isMiddayDataFilled(store_cd)) {
            middayImg = R.drawable.midday_stock_done;
        } else {
            middayImg = R.drawable.midday_stock;
        }
        if (db.getinsertedMarketIntelligenceData(store_cd, visit_date).size() > 0) {
            marketIntelligence = R.drawable.audit_done;
        } else {
            marketIntelligence = R.drawable.audit;
        }

        if (db.isClosingBackOfficeDataFilled(store_cd)) {
            closingBackoffice = R.drawable.closing_stock_backroom_done;
        } else {
            closingBackoffice = R.drawable.closing_stock_backroom;
        }

        if (db.isAssetDataFilled(store_cd)) {
            assetImg = R.drawable.asset_done;
        } else {
            assetImg = R.drawable.asset;
        }

        if (db.isPromotionDataFilled(store_cd)) {
            promotionImg = R.drawable.promotion_done;
        } else {
            promotionImg = R.drawable.promotion;
        }

        if (db.isShareOfShelfDataFilled(store_cd)) {
            shareofshelf = R.drawable.share_of_shelf_done;
        } else {
            shareofshelf = R.drawable.share_of_shelf;
        }

        if (db.isClosingDataFilled(store_cd)) {
            closingImg = R.drawable.closing_stock_done;
        } else {
            closingImg = R.drawable.closing_stock;
        }




       /* if (db.issampledDataFilled(store_cd)) {
            sampled = R.drawable.competition_done;
        } else {
            sampled = R.drawable.competition;
        }*/

     /*   if (user_type.equals("Promoter")) {
            int img[] = {openingImg, openningstockbackoffice, middayImg, closingImg, closingBackoffice, promotionImg, assetImg, marketIntelligence};//, additionalImg, competitionImg};
            for (int i = 0; i < img.length; i++) {
                NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
                recData.setIconImg(img[i]);
                data.add(recData);
            }
        }*/
        if (user_type.equals("Promoter")) {
            int img[] = {openingImg,openningstockbackoffice, middayImg, promotionImg, assetImg, marketIntelligence,closingBackoffice,shareofshelf,closingImg};//, additionalImg, competitionImg};
            for (int i = 0; i < img.length; i++) {
                NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
                recData.setIconImg(img[i]);
                data.add(recData);
            }
        }

        return data;
    }

    public boolean setCheckOutData() {
        boolean flag = true;
        //opning stock category
        if (db.getStockAvailabilityData1(account_cd,city_cd,storetype_cd).size() > 0) {
            if (db.isOpeningDataAllFilled(store_cd)) {
                flag = true;
            } else {
                flag = false;
            }
        }
        //stock backroom
        if (flag) {
            /*if (db.getStockAvailabilityData(store_cd).size() > 0) {*/
            if (db.getStockAvailabilityData1(account_cd,city_cd,storetype_cd).size() > 0) {
                if (db.isStockBackRoomDataAllFilled(store_cd)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }

        //promotion
        if (flag) {
            if (db.getPromotionBrandData(store_cd).size() > 0) {
                if (db.isPromotionDataFilled(store_cd)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        //pais visibility
      /*  if (flag) {
            if (db.isStoreAssetDataFilled(store_cd)) {
                if (db.getAssetCategoryData(store_cd).size() > 0) {
                    if (db.isAssetDataFilled(store_cd)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            }
        }*/
        //marketintelligence
        if (flag) {
            if (db.getinsertedMarketIntelligenceData(store_cd, visit_date).size() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        }


        if (flag) {
            if (db.getMiddayDataFromCheckoutDatabase(store_cd).size() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        }

        if (flag) {
            if (db.getShareofSelfCheckoutData(store_cd).size() > 0) {
                    flag = true;
                } else {
                    flag = false;
            }
        }


        return flag;
    }

}
