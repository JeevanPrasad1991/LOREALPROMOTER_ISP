package com.cpm.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.GeotaggingBeans;
import com.cpm.GetterSetter.ShareOfShelfGetterSetter;
import com.cpm.GetterSetter.StoreBean;
import com.cpm.GetterSetter.StoreStockinGetterSetter;
import com.cpm.dailyentry.AssetChecklistReasonGettersetter;
import com.cpm.delegates.CoverageBean;
import com.cpm.delegates.TableBean;
import com.cpm.xmlGetterSetter.AssetChecklistGetterSetter;
import com.cpm.xmlGetterSetter.AssetInsertdataGetterSetter;
import com.cpm.xmlGetterSetter.AssetMasterGetterSetter;
import com.cpm.xmlGetterSetter.AssetNonReasonGetterSetter;
import com.cpm.xmlGetterSetter.Audit_QuestionDataGetterSetter;
import com.cpm.xmlGetterSetter.Audit_QuestionGetterSetter;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.CallsGetterSetter;
import com.cpm.xmlGetterSetter.CategoryMasterGetterSetter;
import com.cpm.xmlGetterSetter.ChecklistInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.CompanyGetterSetter;
import com.cpm.xmlGetterSetter.CompetitionPromotionGetterSetter;
import com.cpm.xmlGetterSetter.DeepFreezerGetterSetter;
import com.cpm.xmlGetterSetter.DeepFreezerTypeGetterSetter;
import com.cpm.xmlGetterSetter.FacingCompetitorGetterSetter;
import com.cpm.xmlGetterSetter.FoodStoreInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.HeaderGetterSetter;
import com.cpm.xmlGetterSetter.IncentiveGetterSetter;
import com.cpm.xmlGetterSetter.JCPGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetChecklistGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetChecklistreasonGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetGetterSetter;
import com.cpm.xmlGetterSetter.MappingAvailabilityGetterSetter;
import com.cpm.xmlGetterSetter.MappingPromotionGetterSetter;
import com.cpm.xmlGetterSetter.MappingSosGetterSetter;
import com.cpm.xmlGetterSetter.MarketIntelligenceGetterSetter;
import com.cpm.xmlGetterSetter.NonComplianceChecklistGetterSetter;
import com.cpm.xmlGetterSetter.NonPromotionReasonGetterSetter;
import com.cpm.xmlGetterSetter.NonWorkingReasonGetterSetter;
import com.cpm.xmlGetterSetter.POIGetterSetter;
import com.cpm.xmlGetterSetter.PayslipGetterSetter;
import com.cpm.xmlGetterSetter.PerformanceGetterSetter;
import com.cpm.xmlGetterSetter.PromoTypeGetterSetter;
import com.cpm.xmlGetterSetter.PromotionInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.SampledGetterSetter;
import com.cpm.xmlGetterSetter.SkuGetterSetter;
import com.cpm.xmlGetterSetter.SkuMasterGetterSetter;
import com.cpm.xmlGetterSetter.StockGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;
import com.cpm.xmlGetterSetter.SubCategoryGetterSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("LongLogTag")
public class GSKDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LOREAL_PRO_DATABASE6";
    public static final int DATABASE_VERSION = 6;
    private SQLiteDatabase db;

    public GSKDatabase(Context completeDownloadActivity) {
        super(completeDownloadActivity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
        }
    }

    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TableBean.getjcptable());
        db.execSQL(TableBean.getSkumastertable());
        db.execSQL(TableBean.getMappingavailtable());
        db.execSQL(TableBean.getMappingpromotable());
        db.execSQL(TableBean.getMappingassettable());
        db.execSQL(TableBean.getAssetmastertable());
        db.execSQL(TableBean.getCompanytable());
        db.execSQL(TableBean.getNonworkingtable());
        db.execSQL(TableBean.getBrandtable());
        db.execSQL(TableBean.getEmp_payslip_table());
        db.execSQL(TableBean.getCategorymastertable());
        db.execSQL(TableBean.getAudit_question_table());
        //reason non asset
        db.execSQL(TableBean.getAssetnoreasonTable());
        db.execSQL(TableBean.getNonpromotionnoreasonTable());
        db.execSQL(TableBean.getPromoTypeTable());
        db.execSQL(TableBean.getSubcetegory_table());
        db.execSQL(TableBean.getIncentive_table());
        db.execSQL(TableBean.getMappingsos_table());
        db.execSQL(CommonString.CREATE_TABLE_DEEPFREEZER_DATA);
        db.execSQL(CommonString.CREATE_TABLE_OPENING_STOCK_DATA);
        db.execSQL(CommonString.CREATE_TABLE_CLOSING_STOCK_DATA);
        db.execSQL(CommonString.CREATE_TABLE_MIDDAY_STOCK_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_OPENINGHEADER_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_OPENINGHEADER_CLOSING_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_HEADER_MIDDAY_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_HEADER_PROMOTION_DATA);
        db.execSQL(CommonString.CREATE_TABLE_PROMOTION_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_HEADER_ASSET_DATA);
        db.execSQL(CommonString.CREATE_TABLE_ASSET_DATA);
        db.execSQL(CommonString.CREATE_TABLE_FACING_COMPETITOR_DATA);
        db.execSQL(CommonString.CREATE_TABLE_FOOD_STORE_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_HEADER_FOOD_STORE_DATA);
        db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
        db.execSQL(CommonString.CREATE_TABLE_STOCK_DATA);
        db.execSQL(CommonString.CREATE_TABLE_COMPETITION_POI);
        db.execSQL(CommonString.CREATE_TABLE_POI);
        db.execSQL(CommonString.CREATE_TABLE_COMPETITION_PROMOTION);
        db.execSQL(CommonString.CREATE_TABLE_STOCK_IMAGE);
        db.execSQL(CommonString.CREATE_TABLE_AUDIT_DATA_SAVE);
        db.execSQL(CommonString.CREATE_TABLE_PJP_DEVIATION);
        db.execSQL(CommonString.CREATE_TABLE_INSERT_MARKET_INTELLIGENCE_DATA);
        db.execSQL(CommonString.CREATE_TABLE_INSERT_SAMPLED_DATA);
        db.execSQL(CommonString.CREATE_TABLE_STOCK_BACKOFFICE_IMAGE);
        db.execSQL(CommonString.CREATE_TABLE_insert_OPENINGHEADER_BACKOFFICE_DATA);
        db.execSQL(CommonString.CREATE_TABLE_STOCK_BACKOFFICE_DATA);
        //upendra
        db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
        db.execSQL(CommonString.CREATE_TABLE_SHARE_OF_SHELF_DATA_DATA);
        db.execSQL(CommonString.CREATE_TABLE_insert_mid_day_HEADER_DATA);
        db.execSQL(CommonString.CREATE_TABLE_MID_DAY_DATA);
        db.execSQL(CommonString.CREATE_TABLE_SHARE_OF_SHELF_IMAGE);
        db.execSQL(CommonString.CREATE_TABLE_insert_share_of_shelf_HEADER_DATA);
        db.execSQL(CommonString.CREATE_TABLE_SHARE_OF_SHELF_FACING_DATA);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        onCreate(db);
    }

    public void deleteSpecificStoreData(String storeid) {

        db.delete(CommonString.TABLE_PJP_DEVIATION, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STOCK_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_PROMOTION_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_PROMOTION_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_ASSET_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_SAMPLED_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STOCK_BACKOFFICE_IMAGE, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STOCK_BACKOFFICE_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STOCK_IMAGE, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_SHARE_OF_SHELF_FACING_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_MID_DAY_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_SHARE_OF_SHELF_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);

    }

    public void deleteAllCoverage() {
        db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
    }

    public void deleteSpecificCoverage(String storeid) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
    }


    public void deletePreviousUploadedData(String visit_date) {
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from COVERAGE_DATA where VISIT_DATE <> '" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {

                    db.delete(CommonString.TABLE_PJP_DEVIATION, null, null);
                    db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_STOCK_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_PROMOTION_HEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_PROMOTION_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_ASSET_DATA, null, null);
                    db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, null, null);
                    db.delete(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_SAMPLED_DATA, null, null);
                    db.delete(CommonString.TABLE_STOCK_BACKOFFICE_IMAGE, null, null);
                    db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA, null, null);
                    db.delete(CommonString.TABLE_STOCK_BACKOFFICE_DATA, null, null);
                    db.delete(CommonString.TABLE_STOCK_IMAGE, null, null);
                    db.delete(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, null, null);
                    db.delete(CommonString.TABLE_SHARE_OF_SHELF_FACING_DATA, null, null);
                    db.delete(CommonString.TABLE_MID_DAY_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_GEOTAGGING, null, null);
                    db.delete(CommonString.TABLE_SHARE_OF_SHELF_DATA, null, null);


                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!", e.toString());

        }
    }

    public void deleteAllTables() {

        db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_DATA, null, null);
        db.delete(CommonString.TABLE_STOCK_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_PROMOTION_HEADER_DATA, null, null);
        db.delete(CommonString.TABLE_PROMOTION_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, null, null);
        db.delete(CommonString.TABLE_ASSET_DATA, null, null);
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, null, null);
        db.delete(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_SAMPLED_DATA, null, null);
        db.delete(CommonString.TABLE_STOCK_BACKOFFICE_IMAGE, null, null);
        db.delete(CommonString.TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA, null, null);
        db.delete(CommonString.TABLE_STOCK_BACKOFFICE_DATA, null, null);
        db.delete(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, null, null);
        db.delete(CommonString.TABLE_SHARE_OF_SHELF_FACING_DATA, null, null);
        db.delete(CommonString.TABLE_MID_DAY_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, null, null);
    }

    public void deleteStockData() {
        db.delete("STOCK_DATA", null, null);
    }

    public void deleteStockHeaderData() {
        db.delete("openingHeader_data", null, null);
    }

    //Store data
    public void insertSkuMasterData(SkuMasterGetterSetter data) {
        db.delete("SKU_MASTER", null, null);
        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSku_cd().size(); i++) {
                values.put("SKU_CD", Integer.parseInt(data.getSku_cd().get(i)));
                values.put("SKU", data.getSku().get(i));
                values.put("BRAND_CD", Integer.parseInt(data.getBrand_cd().get(i)));
                values.put("BRAND", data.getBrand().get(i));
                values.put("CATEGORY_CD", Integer.parseInt(data.getCategory_cd().get(i)));
                values.put("CATEGORY", data.getCategory().get(i));
                values.put("SKU_SEQUENCE", data.getSku_sequence().get(i));
                values.put("BRAND_SEQUENCE", data.getBrand_sequence().get(i));
                values.put("CATEGORY_SEQUENCE", data.getCategory_sequence().get(i));
                values.put("HIMALAYA_PHOTO", data.getHIMALAYA_PHOTO().get(i));
                values.put("CATEGORY_PHOTO", data.getCATEGORY_PHOTO().get(i));
                db.insert("SKU_MASTER", null, values);
            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Store Data ",
                    ex.toString());
        }

    }


    public ArrayList<SkuMasterGetterSetter> getSkuMasterData(String category_cd) {

        Log.d("FetchingCategoryType--------------->Start<------------",
                "------------------");
        ArrayList<SkuMasterGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            // ORDER BY SKU_SEQUENCE
            dbcursor = db.rawQuery("select * from SKU_MASTER where CATEGORY_CD='" + category_cd + "' ORDER BY SKU_SEQUENCE", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SkuMasterGetterSetter df = new SkuMasterGetterSetter();
                    df.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    df.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    df.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    df.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    df.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    df.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Category!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingCategory data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //JCP data
    public void insertJCPData(JourneyPlanGetterSetter data) {
        db.delete("JOURNEY_PLAN", null, null);
        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getStore_cd().size(); i++) {
                values.put("STORE_CD", Integer.parseInt(data.getStore_cd().get(i)));
                values.put("EMP_CD", Integer.parseInt(data.getEmp_cd().get(i)));
                values.put("VISIT_DATE", data.getVISIT_DATE().get(i));
                values.put("KEYACCOUNT", data.getKey_account().get(i));
                values.put("STORENAME", data.getStore_name().get(i));
                values.put("CITY", data.getCity().get(i));
                values.put("STORETYPE", data.getStore_type().get(i));
                values.put("UPLOAD_STATUS", data.getUploadStatus().get(i));
                values.put("CHECKOUT_STATUS", data.getCheckOutStatus().get(i));
                values.put("GEO_TAG", data.getGeotagStatus().get(i));
                values.put("LATTITUDE", data.getLatitude().get(i));
                values.put("LONGITUDE", data.getLongitude().get(i));
                values.put("KEYACCOUNT_CD", data.getKeyaccount_cd().get(i));
                values.put("CITY_CD", data.getCity_cd().get(i));
                values.put("STORETYPE_CD", data.getStoretype_cd().get(i));
                values.put("INSTOCK_ALLOW", data.getInstock_allow().get(i));
                db.insert("JOURNEY_PLAN", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert JCP Data ",
                    ex.toString());
        }

    }

    //mapping available data
    public void insertMappingstockData(MappingAvailabilityGetterSetter data) {
        db.delete("MAPPING_STOCK", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getKeyaccount_cd().size(); i++) {

                values.put("KEYACCOUNT_CD", Integer.parseInt(data.getKeyaccount_cd().get(i)));
                values.put("CITY_CD", Integer.parseInt(data.getCity_cd().get(i)));
                values.put("STORETYPE_CD", Integer.parseInt(data.getStoretype_cd().get(i)));
                values.put("SKU_CD", Integer.parseInt(data.getSku_cd().get(i)));

                db.insert("MAPPING_STOCK", null, values);

            }
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Mapping Data ",
                    ex.toString());
        }
    }

    //mapping promotion data
    public void insertMappingPromotionData(MappingPromotionGetterSetter data) {
        db.delete("MAPPING_PROMOTION", null, null);
        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getStoretype_cd().size(); i++) {
                values.put("PID", data.getPid().get(i));
                values.put("KEYACCOUNT_CD", Integer.parseInt(data.getKeyaccount_cd().get(i)));
                values.put("CITY_CD", Integer.parseInt(data.getCity_cd().get(i)));
                values.put("STORETYPE_CD", Integer.parseInt(data.getStoretype_cd().get(i)));
                values.put("BRAND_CD", Integer.parseInt(data.getBrand_cd().get(i)));
                values.put("PROMOTION", data.getPromotion().get(i));
                db.insert("MAPPING_PROMOTION", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Mapping promotion Data ",
                    ex.toString());
        }

    }

    //delete Mappin Promotion
    public void deletePromotionMapping() {
        db.delete("MAPPING_PROMOTION", null, null);
    }

    //mapping asset data
    public void insertMappingAssetData(MappingAssetGetterSetter data) {

        db.delete("MAPPING_ASSET", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getStore_cd().size(); i++) {

                values.put("STORE_CD", Integer.parseInt(data.getStore_cd().get(i)));
                values.put("CATEGORY_CD", Integer.parseInt(data.getCategory_cd().get(i)));
                values.put("ASSET_CD", data.getAsset_cd().get(i));
                values.put("IMAGE_URL ", data.getIMAGE_URL().get(i));

                db.insert("MAPPING_ASSET", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Mapping asset Data",
                    ex.toString());
        }

    }

    //delete Asset Mapping
    public void deleteAssetMapping() {
        db.delete("MAPPING_ASSET_NEW", null, null);
    }

    //Asset master data
    public void insertAssetMasterData(AssetMasterGetterSetter data) {

        db.delete("ASSET_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getAsset_cd().size(); i++) {
                values.put("ASSET_CD", Integer.parseInt(data.getAsset_cd().get(i)));
                values.put("ASSET", data.getAsset().get(i));

                db.insert("ASSET_MASTER", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Asset Master Data ",
                    ex.toString());
        }

    }


    // get Asset master data
    public ArrayList<AssetNonReasonGetterSetter> getAssetReasonData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<AssetNonReasonGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM NON_ASSET_REASON", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetNonReasonGetterSetter sb = new AssetNonReasonGetterSetter();
                    sb.setAreason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AREASON_CD")));
                    sb.setAreason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AREASON")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    //Asset reason data
    public void insertAssetNonReasonData(AssetNonReasonGetterSetter data) {

        db.delete("NON_ASSET_REASON", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getAreason_cd().size(); i++) {

                values.put("AREASON_CD", Integer.parseInt(data.getAreason_cd().get(i)));
                values.put("AREASON", data.getAreason().get(i));

                db.insert("NON_ASSET_REASON", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Asset Master Data ",
                    ex.toString());
        }

    }


    // get Asset master data
    public ArrayList<NonPromotionReasonGetterSetter> getPromotionReasonData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<NonPromotionReasonGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM NON_PROMOTION_REASON", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonPromotionReasonGetterSetter sb = new NonPromotionReasonGetterSetter();
                    sb.setPreason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PREASON_CD")));
                    sb.setPreason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PREASON")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    //Asset master data
    public void insertNonPromotionReasonData(NonPromotionReasonGetterSetter data) {

        db.delete("NON_PROMOTION_REASON", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getPreason_cd().size(); i++) {

                values.put("PREASON_CD", Integer.parseInt(data.getPreason_cd().get(i)));
                values.put("PREASON", data.getPreason().get(i));
                db.insert("NON_PROMOTION_REASON", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Asset Master Data ",
                    ex.toString());
        }

    }

    public void insertpromoTypeData(PromoTypeGetterSetter data) {

        db.delete("PROMO_TYPE_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getPromoType_cd().size(); i++) {

                values.put("PROMOTYPE_CD", Integer.parseInt(data.getPromoType_cd().get(i)));
                values.put("PROMO_TYPE", data.getPromoType().get(i));
                db.insert("PROMO_TYPE_MASTER", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Asset Master Data ",
                    ex.toString());
        }

    }

    public ArrayList<PromoTypeGetterSetter> getpromotypemasterData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<PromoTypeGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * FROM PROMO_TYPE_MASTER ORDER BY PROMOTYPE_CD ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromoTypeGetterSetter sb = new PromoTypeGetterSetter();
                    sb.setPromoType_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTYPE_CD")));
                    sb.setPromoType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_TYPE")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    public ArrayList<CompanyGetterSetter> getcompanymasterData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<CompanyGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * FROM COMPANY_MASTER ORDER BY COMPANY_CD ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CompanyGetterSetter sb = new CompanyGetterSetter();
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));
                    sb.setCompany(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    //Asset master data
    public void insertCompanyMasterData(CompanyGetterSetter data) {

        db.delete("COMPANY_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getCompany_cd().size(); i++) {

                values.put("COMPANY_CD", Integer.parseInt(data.getCompany_cd().get(i)));
                values.put("COMPANY", data.getCompany().get(i));

                db.insert("COMPANY_MASTER", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Comapny Master Data ",
                    ex.toString());
        }

    }

    public void insertBrandMasterData(BrandGetterSetter data) {

        db.delete("BRAND_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getBrand_cd().size(); i++) {

                values.put("BRAND_CD", Integer.parseInt(data.getBrand_cd().get(i)));
                values.put("BRAND", data.getBrand().get(i));
                values.put("BRAND_SEQUENCE", data.getBrand_sequence().get(i));
                values.put("COMPANY_CD", Integer.parseInt(data.getCompany_cd().get(i)));
                values.put("SUB_CATEGORY_CD", Integer.parseInt(data.getCategory_cd().get(i)));

                db.insert("BRAND_MASTER", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Brand Master Data ",
                    ex.toString());
        }

    }


    //Non Working data
    public void insertNonWorkingReasonData(NonWorkingReasonGetterSetter data) {

        db.delete("NON_WORKING_REASON", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getReason_cd().size(); i++) {

                values.put("REASON_CD", Integer.parseInt(data.getReason_cd().get(i)));
                values.put("REASON", data.getReason().get(i));
                values.put("ENTRY_ALLOW", data.getEntry_allow().get(i));

                db.insert("NON_WORKING_REASON", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Non Working Data ",
                    ex.toString());
        }

    }

    //MAPPING_ASSET_NEW_CHECKLIST_REASON

    public ArrayList<CategoryMasterGetterSetter> getcategorymasterData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<CategoryMasterGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * FROM CATEGORY_MASTER ORDER BY CATEGORY_CD ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CategoryMasterGetterSetter sb = new CategoryMasterGetterSetter();
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    //Category Master data
    public void insertCategoryMasterData(CategoryMasterGetterSetter data) {

        db.delete("CATEGORY_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {
            for (int i = 0; i < data.getCategory_cd().size(); i++) {
                values.put("CATEGORY_CD", Integer.parseInt(data.getCategory_cd().get(i)));
                values.put("CATEGORY", data.getCategory().get(i));
                values.put("HIMALAYA_PHOTO", data.getHIMALAYA_PHOTO().get(i));
                values.put("CATEGORY_PHOTO", data.getCATEGORY_PHOTO().get(i));

                db.insert("CATEGORY_MASTER", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Category master Data ",
                    ex.toString());
        }

    }

    //get JCP Data
    public ArrayList<JourneyPlanGetterSetter> getJCPData(String date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<JourneyPlanGetterSetter> list = new ArrayList<JourneyPlanGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from JOURNEY_PLAN " +
                    "where VISIT_DATE = '" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    JourneyPlanGetterSetter sb = new JourneyPlanGetterSetter();

                    sb.setStore_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_CD")));
                    sb.setKey_account(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT")));
                    sb.setStore_name((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORENAME"))));
                    sb.setCity((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CITY"))));
                    sb.setUploadStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("UPLOAD_STATUS"))));
                    sb.setCheckOutStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKOUT_STATUS"))));
                    sb.setVISIT_DATE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIT_DATE"))));
                    sb.setGeotagStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("GEO_TAG"))));
                    sb.setLatitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LATTITUDE"))));
                    sb.setLongitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LONGITUDE"))));
                    sb.setKeyaccount_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT_CD"))));
                    sb.setCity_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CITY_CD"))));
                    sb.setStoretype_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORETYPE_CD"))));
                    sb.setInstock_allow((dbcursor.getString(dbcursor.getColumnIndexOrThrow("INSTOCK_ALLOW"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching JCP!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "JCP data---------------------->Stop<-----------");
        return list;
    }


    //get JCP Data
    public ArrayList<JourneyPlanGetterSetter> getAllJCPData() {

        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");
        ArrayList<JourneyPlanGetterSetter> list = new ArrayList<JourneyPlanGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from JOURNEY_PLAN "
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlanGetterSetter sb = new JourneyPlanGetterSetter();

                    sb.setStore_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_CD")));
                    sb.setKey_account(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT")));
                    sb.setStore_name((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORENAME"))));
                    sb.setCity((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CITY"))));
                    sb.setUploadStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("UPLOAD_STATUS"))));
                    sb.setCheckOutStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKOUT_STATUS"))));
                    sb.setVISIT_DATE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIT_DATE"))));
                    sb.setLatitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LATTITUDE"))));
                    sb.setLongitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LONGITUDE"))));
                    sb.setGeotagStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("GEO_TAG"))));
                    sb.setInstock_allow((dbcursor.getString(dbcursor.getColumnIndexOrThrow("INSTOCK_ALLOW"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching JCP!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingJCP data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get DeepFreezerType Data from Master
    public ArrayList<DeepFreezerTypeGetterSetter> getDFMasterData(String dftype) {

        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");
        ArrayList<DeepFreezerTypeGetterSetter> list = new ArrayList<DeepFreezerTypeGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from DEEPFREEZER_MASTER where FREEZER_TYPE = '" + dftype + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    DeepFreezerTypeGetterSetter df = new DeepFreezerTypeGetterSetter();


                    df.setDeep_freezer(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DEEP_FREEZER")));
                    df.setFid(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("FID")));
                    df.setFreezer_type(dftype);
                    df.setRemark("");
                    df.setStatus("NO");

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Deepfreezer!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching Deep Freezer data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //Insert Deepfreezer Type Data
    public void insertDeepFreezerTypeData(ArrayList<DeepFreezerTypeGetterSetter> data, String dfType, String store_cd) {

        db.delete(CommonString.TABLE_DEEPFREEZER_DATA, "FREEZER_TYPE" + "='" + dfType + "'", null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.size(); i++) {

                values.put("FID", Integer.parseInt(data.get(i).getFid()));
                values.put("STORE_CD", store_cd);
                values.put("DEEP_FREEZER", data.get(i).getDeep_freezer());
                values.put("FREEZER_TYPE", data.get(i).getFreezer_type());
                values.put("STATUS", data.get(i).getStatus());
                values.put("REMARK", data.get(i).getRemark());

                db.insert(CommonString.TABLE_DEEPFREEZER_DATA, null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert DeepFreezer Data ",
                    ex.toString());
        }

    }

    //Insert Facing Competitor Data
    public void insertFscingCompetitorData(String store_cd,
                                           HashMap<FacingCompetitorGetterSetter, List<FacingCompetitorGetterSetter>> data, List<FacingCompetitorGetterSetter> save_listDataHeader) {

        db.delete(CommonString.TABLE_FACING_COMPETITOR_DATA, "STORE_CD" + "='" + store_cd + "'", null);
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        String category, category_cd;

        try {

            for (int i = 0; i < save_listDataHeader.size(); i++) {

                category = save_listDataHeader.get(i).getCategory();
                category_cd = save_listDataHeader.get(i).getCategory_cd();


                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {

                    values.put("CATEGORY_CD", Integer.parseInt(category_cd));
                    values.put("CATEGORY", category);
                    values.put("FACING", data.get(save_listDataHeader.get(i)).get(j).getMccaindf());
                    values.put("BRAND", data.get(save_listDataHeader.get(i)).get(j).getBrand());
                    values.put("BRAND_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getBrand_cd()));
                    //values.put("STORE_DF", data.get(i).getStoredf());
                    values.put("STORE_CD", store_cd);

                    db.insert(CommonString.TABLE_FACING_COMPETITOR_DATA, null, values);

                }
            }


        } catch (Exception ex) {
            Log.d("Database Exception while Insert Facing Competition Data ",
                    ex.toString());
        }

    }

    //get DeepFreezerType Data
    public ArrayList<DeepFreezerTypeGetterSetter> getDFTypeData(String dftype, String storecd) {

        Log.d("FetchingDeepFreezerType--------------->Start<------------",
                "------------------");
        ArrayList<DeepFreezerTypeGetterSetter> list = new ArrayList<DeepFreezerTypeGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from DEEPFREEZER_DATA where FREEZER_TYPE = '" + dftype + "'  AND STORE_CD = '" + storecd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    DeepFreezerTypeGetterSetter df = new DeepFreezerTypeGetterSetter();


                    df.setDeep_freezer(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DEEP_FREEZER")));
                    df.setFid(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("FID")));
                    df.setFreezer_type(dftype);
                    df.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    df.setStatus(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STATUS")));

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Deepfreezer!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingJCP data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }


    //get Facing Competitor Data
    public ArrayList<FacingCompetitorGetterSetter> getFacingCompetitorData(String store_cd) {

        Log.d("Fetching facing competitor--------------->Start<------------",
                "------------------");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from FACING_COMPETITOR_DATA where STORE_CD = '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter fc = new FacingCompetitorGetterSetter();


                    fc.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    fc.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    fc.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));
                    fc.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    fc.setMccaindf(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("FACING")));
                        /*	fc.setStoredf(dbcursor.getString(dbcursor
                                    .getColumnIndexOrThrow("STORE_DF")));*/


                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Facing Competitor!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Competitor---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get Facing Competitor Data Categoty wise
    public ArrayList<FacingCompetitorGetterSetter> getFacingCompetitorCategotywiseData(String store_cd, String category_cd) {

        Log.d("Fetching brand>Start<--",
                "--");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from FACING_COMPETITOR_DATA where STORE_CD = '" + store_cd + "' AND CATEGORY_CD" + " = '" + category_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter fc = new FacingCompetitorGetterSetter();


                    fc.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    fc.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    fc.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));
                    fc.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    fc.setMccaindf(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("FACING")));


                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Facing Competitor!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Competitor---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get Facing Competitor Data
    public ArrayList<PerformanceGetterSetter> getPerformrmance(String store_cd) {

        Log.d("Fetching facing competitor--------------->Start<------------",
                "------------------");
        ArrayList<PerformanceGetterSetter> list = new ArrayList<PerformanceGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from MY_PERFORMANCE where STORE_CD = '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PerformanceGetterSetter fc = new PerformanceGetterSetter();


                    fc.setMonthly_target(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("TARGET")));
                    fc.setMtd_sales(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SALE")));
                    fc.setAchievement(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ACH")));

                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Facing Competitor!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Competitor---------------------->Stop<-----------",
                "-------------------");
        return list;

    }


    public ArrayList<StockNewGetterSetter> getStockAvailabilityData(String account_cd,String city_cd,String storetype_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.CATEGORY_CD, SD.CATEGORY, SD.HIMALAYA_PHOTO, SD.CATEGORY_PHOTO " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "'"+
                    " ORDER BY SD.BRAND_SEQUENCE", null);



            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    // sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getStockAvailabilityData1(String account_cd,String city_cd,String storetype_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT SD.CATEGORY_CD, SD.CATEGORY, SD.HIMALAYA_PHOTO, SD.CATEGORY_PHOTO " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "'"+
                    "ORDER BY SD.BRAND_SEQUENCE", null);



            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    // sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getmappingStockData(String account_cd,String city_cd,String storetype_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT SD.CATEGORY_CD, SD.CATEGORY, SD.HIMALAYA_PHOTO, SD.CATEGORY_PHOTO " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "'"+
                    " ORDER BY SD.BRAND_SEQUENCE", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    // sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getmappingStockInData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT BR.BRAND_CD, SB.SUB_CATEGORY||'-'||BR.BRAND as BRAND FROM BRAND_MASTER BR " +
                    "INNER JOIN SUB_CATEGORY_MASTER SB " +
                    "ON BR.SUB_CATEGORY_CD = SB.SUB_CATEGORY_CD " +
                    "WHERE BR.COMPANY_CD ='1'" , null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                  /*  sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    // sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));*/

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    //Header with cam data
    public ArrayList<StockNewGetterSetter> getHeaderStockImageData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY, CAT_STOCK, IMAGE_STK, IMAGE_CAT_ONE, IMAGE_CAT_TWO,HIMALAYA_PHOTO, CATEGORY_PHOTO FROM STOCK_IMAGE WHERE STORE_CD ='" + store_cd + "'AND VISIT_DATE  ='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setImg_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_STK")));
                    sb.setImg_cat_one(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_ONE")));
                    sb.setImg_cat_two(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_TWO")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));

                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getHeaderStockBackOfficeImageData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY, IMAGE_STK, IMAGE_CAT_ONE, IMAGE_CAT_TWO,HIMALAYA_PHOTO, CATEGORY_PHOTO FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD ='" + store_cd + "'AND VISIT_DATE  ='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setImg_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_STK")));
                    sb.setImg_cat_one(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_ONE")));
                    sb.setImg_cat_two(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_TWO")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }


    //Stock data
    public ArrayList<StockNewGetterSetter> getFoodStoreAvailabilityData(String store_cd) {
        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            String food = "Food";

            dbcursor = db
                    .rawQuery(
                            "SELECT DISTINCT SD.BRAND_CD, SD.BRAND FROM MAPPING_STOCK CD INNER JOIN SKU_MASTER SD ON CD.SKU_CD = SD.SKU_CD WHERE CD.STORE_CD ='" + store_cd + "' AND SD.CATEGORY_TYPE ='" + food + "' ORDER BY CD.BRAND_SEQUENCE"
                            , null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();


                    sb.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));

                    sb.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching opening stock!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching opening stock---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getStockBackOfficeSkuData(String account_cd,String city_cd,String storetype_cd, String categord_cd)  {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND ,BM.COMPANY_CD " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN BRAND_MASTER BM " +
                    "ON BM.BRAND_CD = SD.BRAND_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    "AND BM.COMPANY_CD = '1' ORDER BY SD.SKU_SEQUENCE ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));
                    sb.setEd_openingFacing("");
                    sb.setStock_under45days("");
                    sb.setStock1("");
                    sb.setStock2("");
                    sb.setStock3("");
                    sb.setDate1("");
                    sb.setDate2("");
                    sb.setDate3("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }

        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getStockSkuData(String account_cd,String city_cd,String storetype_cd, String categord_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND ,BM.COMPANY_CD " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN BRAND_MASTER BM " +
                    "ON BM.BRAND_CD = SD.BRAND_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    " ORDER BY SD.SKU_SEQUENCE ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));
                    sb.setEd_openingFacing("");
                    sb.setStock_under45days("");
                    sb.setStock1("");
                    sb.setStock2("");
                    sb.setStock3("");
                    sb.setDate1("");
                    sb.setDate2("");
                    sb.setDate3("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }

        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }

    //Food Stock data
    public ArrayList<FoodStoreInsertDataGetterSetter> getFoodSkuData(String brand_cd, String store_cd) {
        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");
        ArrayList<FoodStoreInsertDataGetterSetter> list = new ArrayList<FoodStoreInsertDataGetterSetter>();
        Cursor dbcursor = null;

        try {

            String food = "Food";
            dbcursor = db
                    .rawQuery(
                            "SELECT SD.SKU_CD, SD.SKU, SD.PACKING FROM MAPPING_STOCK CD INNER JOIN SKU_MASTER SD ON CD.SKU_CD = SD.SKU_CD WHERE SD.BRAND_CD ='" + brand_cd + "' AND CD.STORE_CD ='" + store_cd + "' AND SD.CATEGORY_TYPE ='" + food + "' ORDER BY CD.SKU_SEQUENCE"
                            , null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FoodStoreInsertDataGetterSetter sb = new FoodStoreInsertDataGetterSetter();


                    sb.setSku_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU_CD")));

                    sb.setSku(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU")));

                    sb.setActual_listed("NO");
                    sb.setAs_per_meccain("");
                    sb.setPacking_size(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PACKING")));
                    sb.setMccain_df("");
                    sb.setStore_df("");
                    sb.setMtd_sales("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching opening stock!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching opening stock---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    //Stock Sku Closing data
    public ArrayList<StockNewGetterSetter> getStockSkuClosingData(String account_cd,String city_cd,String storetype_cd, String categord_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND, " +
                    " (S.STOCK_1+S.STOCK_2) AS OPENING_STOCK ,S.OPENING_FACING,S.MIDDAY_STOCK,S.STOCK_UNDER_DAYS " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN STOCK_DATA S on SD.SKU_CD=S.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    " AND S.COMPANY_CD = '1' ORDER BY SD.SKU_SEQUENCE", null);



            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setStock_under45days(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_UNDER_DAYS")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));

                    // sb.setOpening_stock_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_BACKROOM_STOCK")));

                    sb.setEd_closingFacing("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getStockSkuClosingBackOfficeData(String account_cd,String city_cd,String storetype_cd, String categord_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;
        try {


            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND, " +
                    " (S.STOCK_1+S.STOCK_2) AS OPENING_STOCK," +
                    "(STD.STOCK_1+STD.STOCK_2) AS OPENING_STOCK_CHILLER,STD.MIDDAY_STOCK, STD.CLOSING_STOCK " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN STOCK_BACKOFFICE_DATA S " +
                    "on SD.SKU_CD=S.SKU_CD " +
                    "INNER JOIN STOCK_DATA STD " +
                    "on SD.SKU_CD=STD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    " AND S.COMPANY_CD = '1' ORDER BY SD.SKU_SEQUENCE", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setSumofChillERSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK_CHILLER")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    sb.setClosing_stock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")));
                    sb.setEd_closingFacing("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    //Stock Midday Sku data
    public ArrayList<StockNewGetterSetter> getStockSkuMiddayData(String account_cd,String city_cd,String storetype_cd,String categord_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND,S.OPENING_FACING" +
                    ",S.STOCK_UNDER_DAYS,(STOCK_1+STOCK_2) AS OPENING_STOCK " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN STOCK_DATA S " +
                    "on S.SKU_CD=SD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    " AND S.COMPANY_CD = '1' ORDER BY SD.SKU_SEQUENCE", null);



            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));

                    sb.setEd_midFacing("");


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    //Promotion brand data
    public ArrayList<StockNewGetterSetter> getPromotionBrandData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CD.BRAND_CD, CD.PROMOTION " +
                    "FROM MAPPING_PROMOTION CD " +
                    "INNER JOIN JOURNEY_PLAN SD " +
                    "ON CD.KEYACCOUNT_CD = SD.KEYACCOUNT_CD AND CD.CITY_CD = SD.CITY_CD  AND CD.STORETYPE_CD = SD.STORETYPE_CD "+
                    "WHERE SD.STORE_CD ='" + store_cd + "' ", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getPromotionBrandData1 (String account_cd,String city_cd,String storetype_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT SD.BRAND_CD, SD.BRAND " +
                    "FROM MAPPING_PROMOTION CD " +
                    "INNER JOIN BRAND_MASTER SD " +
                    "ON CD.BRAND_CD = SD.BRAND_CD " +
                    "WHERE CD.KEYACCOUNT_CD ='" + account_cd + "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd + "'"+
                    "ORDER BY SD.BRAND_SEQUENCE", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }



        //Promotion brand data
    public ArrayList<AssetInsertdataGetterSetter> getAssetCategoryData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<AssetInsertdataGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY " +
                    "FROM CATEGORY_MASTER BD " +
                    "WHERE CATEGORY_CD IN( SELECT DISTINCT CATEGORY_CD FROM MAPPING_ASSET " +
                    "WHERE STORE_CD ='" + store_cd + "' ) ", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "Asset brand---------------------->Stop<-----------");
        return list;
    }

    // get promotion Sku data
    public ArrayList<PromotionInsertDataGetterSetter> getPromotionSkuData(String brand_cd, String account_cd,String city_cd,String storetype_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PromotionInsertDataGetterSetter> list = new ArrayList<PromotionInsertDataGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CD.PROMOTION, CD.PID " +
                    "FROM MAPPING_PROMOTION CD " +
                    "INNER JOIN BRAND_MASTER SD " +
                    "ON CD.BRAND_CD = SD.BRAND_CD " +
                    "WHERE SD.BRAND_CD ='" + brand_cd + "' AND CD.KEYACCOUNT_CD ='" + account_cd +  "' AND CITY_CD  ='" + city_cd + "' AND STORETYPE_CD  ='" + storetype_cd +"'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionInsertDataGetterSetter sb = new PromotionInsertDataGetterSetter();
                    sb.setPromotion_txt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION")));
                    sb.setPromotion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PID")));
                    sb.setCamera("");
                    sb.setPresentSpi("0");
                    sb.setReason_cd("0");
                    sb.setReason("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<AssetInsertdataGetterSetter> getAssetData(String category_cd, String store_cd) {
        Log.d("Fetching", "Assetdata--------------->Start<------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<AssetInsertdataGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT AM.ASSET_CD, AM.ASSET, M.CATEGORY_CD, M.IMAGE_URL " +
                    "FROM MAPPING_ASSET M " +
                    "INNER JOIN ASSET_MASTER AM " +
                    "ON M.ASSET_CD = AM.ASSET_CD " +
                    "WHERE M.STORE_CD ='" + store_cd + "' AND M.CATEGORY_CD ='" + category_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();

                    sb.setCategory_cd(category_cd);
                    sb.setAsset(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET")));
                    sb.setAsset_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET_CD")));
                    sb.setPlanogram_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_URL")));
                    sb.setPresent("NO");
                    sb.setReason_cd("");
                    sb.setReason("");
                    sb.setImg("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Asset!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "asset data---------------------->Stop<-----------");
        return list;
    }

    // get Asset data
    public ArrayList<AssetInsertdataGetterSetter> getAllAssetData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<AssetInsertdataGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * FROM ASSET_MASTER", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();


                    sb.setAsset(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET")));

                    sb.setAsset_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET_CD")));


                    sb.setPresent("NO");
                    sb.setRemark("");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Asset!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching asset data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    // get Non Working data
    public ArrayList<NonWorkingReasonGetterSetter> getNonWorkingData(boolean flag) {
        Log.d("FetchingAssetdata--------------->Start<------------", "------------------");
        ArrayList<NonWorkingReasonGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM NON_WORKING_REASON", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (flag) {
                        NonWorkingReasonGetterSetter sb = new NonWorkingReasonGetterSetter();
                        String name = dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW"));
                        if (name.equals("1")) {
                            sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_CD")));
                            sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));
                            sb.setEntry_allow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW")));
                            list.add(sb);
                        }
                    } else {
                        NonWorkingReasonGetterSetter sb = new NonWorkingReasonGetterSetter();
                        sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_CD")));
                        sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));
                        sb.setEntry_allow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW")));
                        list.add(sb);
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!", e.toString());
            return list;
        }
        Log.d("Fetching non working data---------------------->Stop<-----------", "-------------------");
        return list;
    }


    // get Non Working data
    public String getNonEntryAllowReasonData(String reason_cd) {
        Log.d("FetchingAssetdata--------------->Start<------------", "------------------");
        String entry_allow = "";
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT ENTRY_ALLOW FROM NON_WORKING_REASON WHERE REASON_CD = '" + reason_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW"));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return entry_allow;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching entry Allow!!!!!!!!!!!",
                    e.toString());
            return entry_allow;
        }
        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return entry_allow;
    }


    //Insert Opening Data with Brand
    public void InsertOpeningStocklistData(String storeid,
                                           HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data, List<StockNewGetterSetter> save_listDataHeader) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                long l = db.insert(CommonString.TABLE_INSERT_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                    values1.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                    values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                    values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                    values1.put("SKU_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()));
                    values1.put("OPENING_FACING", data.get(save_listDataHeader.get(i)).get(j).getEd_openingFacing());
                    values1.put("STOCK_UNDER_DAYS", data.get(save_listDataHeader.get(i)).get(j).getStock_under45days());
                    values1.put("COMPANY_CD", data.get(save_listDataHeader.get(i)).get(j).getCompany_cd());
                    values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());


                  /*  String s1 = data.get(save_listDataHeader.get(i)).get(j).getStock1();
                    if (s1.equals("")) {
                        values1.put("STOCK_1", "0");
                    } else {
                        values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    }
                    String s2 = data.get(save_listDataHeader.get(i)).get(j).getStock2();
                    if (s2.equals("")) {
                        values1.put("STOCK_2", "0");
                    } else {
                        values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());
                    }
                    String s3 = data.get(save_listDataHeader.get(i)).get(j).getStock3();
                    if (s3.equals("")) {
                        values1.put("STOCK_3", "0");
                    } else {
                        values1.put("STOCK_3", data.get(save_listDataHeader.get(i)).get(j).getStock3());
                    }*/
                    db.insert(CommonString.TABLE_STOCK_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public void InsertOpeningStockBackOfficelistData(String storeid,
                                                     HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data, List<StockNewGetterSetter> save_listDataHeader) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                long l = db.insert(CommonString.TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                    values1.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                    values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                    values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                    values1.put("SKU_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()));

                 /*   values1.put("OPENING_FACING", "0");
                    values1.put("STOCK_UNDER_DAYS", "0");*/
                    values1.put("COMPANY_CD", data.get(save_listDataHeader.get(i)).get(j).getCompany_cd());

                 /*   values1.put("DATE_1", data.get(save_listDataHeader.get(i)).get(j).getDate1());
                    values1.put("DATE_2", data.get(save_listDataHeader.get(i)).get(j).getDate2());
                    values1.put("DATE_3", data.get(save_listDataHeader.get(i)).get(j).getDate3());*/

                    values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());
                  /*  values1.put("STOCK_3", data.get(save_listDataHeader.get(i)).get(j).getStock3());*/

                  /*  String s1 = data.get(save_listDataHeader.get(i)).get(j).getStock1();
                    if (s1.equals("")) {
                        values1.put("STOCK_1", "0");
                    } else {
                        values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    }
                    String s2 = data.get(save_listDataHeader.get(i)).get(j).getStock2();
                    if (s2.equals("")) {
                        values1.put("STOCK_2", "0");
                    } else {
                        values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());
                    }
                    String s3 = data.get(save_listDataHeader.get(i)).get(j).getStock3();
                    if (s3.equals("")) {
                        values1.put("STOCK_3", "0");
                    } else {
                        values1.put("STOCK_3", data.get(save_listDataHeader.get(i)).get(j).getStock3());
                    }*/
                    db.insert(CommonString.TABLE_STOCK_BACKOFFICE_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }


    //Insert Header Data with img
    public void InsertHeaderOpeningStocklistData(
            String storeid, String visit_date, List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("VISIT_DATE", visit_date);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                values.put("CAT_STOCK", save_listDataHeader.get(i).getCatstock());
                values.put("IMAGE_STK", save_listDataHeader.get(i).getImg_cam());
                values.put("IMAGE_CAT_ONE", save_listDataHeader.get(i).getImg_cat_one());
                values.put("IMAGE_CAT_TWO", save_listDataHeader.get(i).getImg_cat_two());
                values.put("HIMALAYA_PHOTO", save_listDataHeader.get(i).getHimalaya_camera());
                values.put("CATEGORY_PHOTO", save_listDataHeader.get(i).getCategory_camera());

                long l = db.insert(CommonString.TABLE_STOCK_IMAGE, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
    }


    public void InsertHeaderOpeningStockBackOfficelistData(
            String storeid, String visit_date, List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("VISIT_DATE", visit_date);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                values.put("IMAGE_STK", save_listDataHeader.get(i).getImg_cam());
                values.put("IMAGE_CAT_ONE", save_listDataHeader.get(i).getImg_cat_one());
                values.put("IMAGE_CAT_TWO", save_listDataHeader.get(i).getImg_cat_two());
                values.put("HIMALAYA_PHOTO", save_listDataHeader.get(i).getHimalaya_camera());
                values.put("CATEGORY_PHOTO", save_listDataHeader.get(i).getCategory_camera());
                long l = db.insert(CommonString.TABLE_STOCK_BACKOFFICE_IMAGE, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
    }


    //Update Opening Data with Brand
    public void UpdateOpeningStocklistData(
            String storeid, HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data,
            List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values1 = new ContentValues();
        try {
            ArrayList<HeaderGetterSetter> list = new ArrayList<>();
            list = getHeaderStock(storeid);

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("OPENING_FACING", data.get(save_listDataHeader.get(i)).get(j).getEd_openingFacing());
                    values1.put("STOCK_UNDER_DAYS", data.get(save_listDataHeader.get(i)).get(j).getStock_under45days());
                    //jeevan
                    values1.put("DATE_1", data.get(save_listDataHeader.get(i)).get(j).getDate1());
                    values1.put("DATE_2", data.get(save_listDataHeader.get(i)).get(j).getDate2());
                    values1.put("DATE_3", data.get(save_listDataHeader.get(i)).get(j).getDate3());
                    values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());
                    values1.put("STOCK_3", data.get(save_listDataHeader.get(i)).get(j).getStock3());

                    db.update(CommonString.TABLE_STOCK_DATA, values1,
                            "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId()) + "' AND SKU_CD " +
                                    "='" + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()) + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }


    public void UpdateOpeningStockBackOfficelistData(
            String storeid, HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data,
            List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values1 = new ContentValues();
        try {
            ArrayList<HeaderGetterSetter> list = new ArrayList<>();
            list = getHeaderStock(storeid);

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("OPENING_FACING", "0");
                    values1.put("STOCK_UNDER_DAYS", "0");
                    //jeevan
                    values1.put("DATE_1", data.get(save_listDataHeader.get(i)).get(j).getDate1());
                    values1.put("DATE_2", data.get(save_listDataHeader.get(i)).get(j).getDate2());
                    values1.put("DATE_3", data.get(save_listDataHeader.get(i)).get(j).getDate3());
                    values1.put("STOCK_1", data.get(save_listDataHeader.get(i)).get(j).getStock1());
                    values1.put("STOCK_2", data.get(save_listDataHeader.get(i)).get(j).getStock2());
                    values1.put("STOCK_3", data.get(save_listDataHeader.get(i)).get(j).getStock3());

                    db.update(CommonString.TABLE_STOCK_BACKOFFICE_DATA, values1,
                            "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId()) + "' AND SKU_CD " +
                                    "='" + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()) + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    //Update Opening Data with Brand
    public void UpdateHeaderOpeningStocklistData(
            String storeid, String visit_date,
            List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("IMAGE_STK", save_listDataHeader.get(i).getImg_cam());
                values.put("IMAGE_CAT_ONE", save_listDataHeader.get(i).getImg_cat_one());
                values.put("IMAGE_CAT_TWO", save_listDataHeader.get(i).getImg_cat_two());
                values.put("CAT_STOCK", save_listDataHeader.get(i).getCatstock());

                db.update(CommonString.TABLE_STOCK_IMAGE, values, "STORE_CD" + "='" + storeid +
                        "' AND CATEGORY_CD " + "='" + Integer.parseInt(save_listDataHeader.get(i).getCategory_cd()) +
                        "' AND VISIT_DATE  ='" + visit_date + "'", null);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }


    public void UpdateHeaderOpeningStockBackOfficelistData(
            String storeid, String visit_date,
            List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("IMAGE_STK", save_listDataHeader.get(i).getImg_cam());
                values.put("IMAGE_CAT_ONE", save_listDataHeader.get(i).getImg_cat_one());
                values.put("IMAGE_CAT_TWO", save_listDataHeader.get(i).getImg_cat_two());

                db.update(CommonString.TABLE_STOCK_BACKOFFICE_IMAGE, values, "STORE_CD" + "='" + storeid +
                        "' AND CATEGORY_CD " + "='" + Integer.parseInt(save_listDataHeader.get(i).getCategory_cd()) +
                        "' AND VISIT_DATE  ='" + visit_date + "'", null);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }


    //Update Closing Data with Brand
    public void UpdateClosingStocklistData(String storeid,
                                           HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data,
                                           List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values1 = new ContentValues();

        try {
            ArrayList<HeaderGetterSetter> list = new ArrayList<>();
            list = getHeaderStock(storeid);

            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("CLOSING_STOCK", data.get(save_listDataHeader.get(i)).get(j).getEd_closingFacing());

                    db.update(CommonString.TABLE_STOCK_DATA , values1,
                            "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId())
                                    + "' AND SKU_CD " + "='" + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd())
                                    + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database", " Exception while Insert Posm Master Data " + ex.toString());
        }
    }


    public long UpdateClosingStockBackOfficelistData(String storeid, HashMap<StockNewGetterSetter,
            List<StockNewGetterSetter>> data, List<StockNewGetterSetter> save_listDataHeader) {
        ContentValues values1 = new ContentValues();
        long l = 0;
        try {
            ArrayList<HeaderGetterSetter> list = new ArrayList<>();
            list = getHeaderClosingBackOfficeStock(storeid);
            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("CLOSING_STOCK", data.get(save_listDataHeader.get(i)).get(j).getClosing_stk_backroom());

                    l = db.update(CommonString.TABLE_STOCK_BACKOFFICE_DATA, values1, "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId()) + "' AND SKU_CD " + "='" + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()) + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database", " Exception while Insert Posm Master Data " + ex.toString());
        }
        return l;
    }


    //Update Midday Data with Brand
    public void UpdateMiddayStocklistData(String storeid,
                                          HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data,
                                          List<StockNewGetterSetter> save_listDataHeader,String spinner_irepregisterd) {
        ContentValues values1 = new ContentValues();

        try {
            ArrayList<HeaderGetterSetter> list = new ArrayList<HeaderGetterSetter>();
            list = getHeaderStock(storeid);

            for (int i = 0; i < list.size(); i++) {

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    if (spinner_irepregisterd.equals("1")){
                        values1.put("MIDDAY_STOCK", data.get(save_listDataHeader.get(i)).get(j).getEd_midFacing());
                        db.update(CommonString.TABLE_STOCK_DATA, values1,
                                "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId()) + "' AND SKU_CD " + "='"
                                        + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()) + "'", null);
                    }else {
                        values1.put("MIDDAY_STOCK", "0");
                        db.update(CommonString.TABLE_STOCK_DATA, values1,
                                "Common_Id" + "='" + Integer.parseInt(list.get(i).getKeyId()) + "' AND SKU_CD " + "='"
                                        + Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()) + "'", null);
                    }

                }
            }
        } catch (Exception ex) {
            Log.d("Database ", "Exception while Insert Posm Master Data " + ex.toString());
        }
    }

    //Insert Food Store List data
    public void InsertFoodStorelistData(String storeid,
                                        HashMap<StockNewGetterSetter, List<FoodStoreInsertDataGetterSetter>> data, List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {

            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {

                values.put("STORE_CD", storeid);

                values.put("BRAND_CD", save_listDataHeader.get(i)
                        .getBrand_cd());
                values.put("BRAND", save_listDataHeader
                        .get(i).getBrand());

                long l = db.insert(CommonString.TABLE_INSERT_HEADER_FOOD_STORE_DATA,
                        null, values);

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {

                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("SKU_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j)
                            .getSku_cd()));
                    values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j)
                            .getSku());
                    values1.put("ACTUAL_LISTED", data.get(save_listDataHeader.get(i)).get(j).getActual_listed());
                    values1.put("AS_PER_MCCAIN", data.get(save_listDataHeader.get(i)).get(j).getAs_per_meccain());
                    values1.put("MCCAIN_DF", data.get(save_listDataHeader.get(i)).get(j).getMccain_df());
                    values1.put("STORE_DF", data.get(save_listDataHeader.get(i)).get(j).getStore_df());
                    values1.put("PACKING_SIZE", data.get(save_listDataHeader.get(i)).get(j).getPacking_size());
                    values1.put("MTD_SALES", data.get(save_listDataHeader.get(i)).get(j).getMtd_sales());

                    db.insert(CommonString.TABLE_FOOD_STORE_DATA, null, values1);

                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Posm Master Data ",
                    ex.toString());
        }

    }

    //Insert Promotion Data with Brand
    public void InsertPromotionData(String storeid,
                                    HashMap<StockNewGetterSetter, List<PromotionInsertDataGetterSetter>> data,
                                    List<StockNewGetterSetter> save_listDataHeader) {

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                values.put("BRAND", save_listDataHeader.get(i).getBrand());
                long l = db.insert(CommonString.TABLE_INSERT_PROMOTION_HEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {

                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                    values.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("PID", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getPromotion_id()));
                    values1.put("PROMOTION", data.get(save_listDataHeader.get(i)).get(j).getPromotion_txt());
                    values1.put("REMARK", data.get(save_listDataHeader.get(i)).get(j).getReason());
                    values1.put("REMARK_CD", data.get(save_listDataHeader.get(i)).get(j).getReason_cd());
                    values1.put("CAMERA", data.get(save_listDataHeader.get(i)).get(j).getCamera());
                    values1.put("PRESENT_SPIN", data.get(save_listDataHeader.get(i)).get(j).getPresentSpi());
                    db.insert(CommonString.TABLE_PROMOTION_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database ", "Exception while Insert Posm Master Data " + ex.toString());
        }
    }

    //Insert Asset Data with Brand
    public void InsertAssetData(String storeid, HashMap<AssetInsertdataGetterSetter, List<AssetInsertdataGetterSetter>> data,
                                List<AssetInsertdataGetterSetter> save_listDataHeader, String visit_date) {

        db.delete(CommonString.TABLE_ASSET_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {

                values.put("STORE_CD", storeid);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());

                long l = db.insert(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, null, values);

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("ASSET_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getAsset_cd()));
                    values1.put("ASSET", data.get(save_listDataHeader.get(i)).get(j).getAsset());
                    values1.put("REASON", data.get(save_listDataHeader.get(i)).get(j).getReason());
                    values1.put("REASON_CD", data.get(save_listDataHeader.get(i)).get(j).getReason_cd());
                    values1.put("PRESENT", data.get(save_listDataHeader.get(i)).get(j).getPresent());
                    values1.put("PLANOGRAM_IMG", data.get(save_listDataHeader.get(i)).get(j).getPlanogram_img());
                    values1.put("IMAGE", data.get(save_listDataHeader.get(i)).get(j).getImg());

                    long m = db.insert(CommonString.TABLE_ASSET_DATA, null, values1);

                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception ", "while Insert Posm Master Data " + ex.toString());
        }
    }

    //Get Asset Upload Data
    public ArrayList<AssetInsertdataGetterSetter> getAssetDataFromdatabase(String storeId, String category_cd) {
        Log.d("Fetching", "Assetuploaddata--------------->Start<------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD._id,SD.ASSET_CD, SD.ASSET, SD.PRESENT, SD.REASON, SD.REASON_CD, SD.IMAGE, SD.PLANOGRAM_IMG,CD.CATEGORY_CD, CD.CATEGORY " +
                    "FROM openingHeader_Asset_data CD " +
                    "INNER JOIN ASSET_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "WHERE CD.STORE_CD= '" + storeId + "' AND CD.CATEGORY_CD = '" + category_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("_id")));
                    sb.setAsset_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET_CD")));
                    sb.setAsset(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET")));
                    sb.setPresent(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRESENT")));

                    sb.setImg(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE")));
                    sb.setPlanogram_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PLANOGRAM_IMG")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));

                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));
                    sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_CD")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Storedat---------------------->Stop<-----------");
        return list;
    }

    //check if table is empty
    public boolean isAssetDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM ASSET_DATA WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();

                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }
        return filled;
    }

    //Get Asset Upload Data
    public ArrayList<AssetInsertdataGetterSetter> getAssetUpload(String storeId) {
        Log.d("Fetching", "Assetuploaddata--------------->Start<------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<AssetInsertdataGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT SD.ASSET_CD,SD.ASSET,SD.PRESENT,SD.REMARK, SD.IMAGE,CD.CATEGORY_CD, CD.CATEGORY " +
                    "FROM openingHeader_Asset_data CD INNER JOIN ASSET_DATA SD ON CD.KEY_ID=SD.Common_Id WHERE CD.STORE_CD= '"
                    + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();

                    sb.setAsset_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET_CD")));

                    sb.setAsset(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET")));
                    sb.setImg(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("IMAGE")));

                    sb.setPresent(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PRESENT")));
                    sb.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingStoredat---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    //Get Promotion Upload Data
    public ArrayList<PromotionInsertDataGetterSetter> getPromotionUpload(String storeId) {
        Log.d("FetchingPromotionuploaddata--------------->Start<------------",
                "------------------");
        ArrayList<PromotionInsertDataGetterSetter> list = new ArrayList<PromotionInsertDataGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db
                    .rawQuery(
                            "SELECT SD.PROMOTION, SD.PID, SD.PRESENT, SD.REMARK, SD.IMAGE, CD.BRAND_CD, CD.BRAND " +
                                    "FROM openingHeader_Promotion_data CD INNER JOIN PROMOTION_DATA SD ON CD.KEY_ID=SD.Common_Id WHERE CD.STORE_CD= '"
                                    + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionInsertDataGetterSetter sb = new PromotionInsertDataGetterSetter();


                    sb.setPromotion_txt(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PROMOTION")));
                    sb.setPromotion_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PID")));

                    sb.setPresent(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PRESENT")));
                    sb.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    sb.setImg(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("IMAGE")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingStoredat---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    //Get Promotion Upload Data
    public ArrayList<PromotionInsertDataGetterSetter> getPromotionDataFromDatabase(String storeId, String brand_cd) {
        Log.d("Fetching", "Promotionuploaddata--------------->Start<------------");
        ArrayList<PromotionInsertDataGetterSetter> list = new ArrayList<PromotionInsertDataGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD.PID, SD.CAMERA, SD.PROMOTION,SD.PRESENT_SPIN,SD.REMARK,SD.REMARK_CD," +
                    " CD.BRAND_CD,CD.BRAND FROM openingHeader_Promotion_data CD INNER JOIN PROMOTION_DATA SD ON CD.KEY_ID=SD.Common_Id WHERE CD.STORE_CD= '" + storeId + "' AND CD.BRAND_CD = '" + brand_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionInsertDataGetterSetter sb = new PromotionInsertDataGetterSetter();

                    sb.setPromotion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PID")));
                    sb.setPromotion_txt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION")));
                    sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK_CD")));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));

                    sb.setCamera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAMERA")));
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRESENT_SPIN"));
                    sb.setPresentSpi(value);


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Storedat---------------------->Stop<-----------");
        return list;
    }


    //check if table is empty
    public boolean isPromotionDataFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM PROMOTION_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }


    //check if table is empty
    public boolean issampledDataFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM SAMPLED_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }


//Get Promotion Database Data

    public ArrayList<FoodStoreInsertDataGetterSetter> getFoodStoreDataFromDatabase(String storeId, String brand_cd) {
        Log.d("FetchingPromotionuploaddata--------------->Start<------------",
                "------------------");
        ArrayList<FoodStoreInsertDataGetterSetter> list = new ArrayList<FoodStoreInsertDataGetterSetter>();
        Cursor dbcursor = null;
        try {

            dbcursor = db
                    .rawQuery(
                            "SELECT SD.SKU_CD, SD.SKU,SD.AS_PER_MCCAIN,SD.ACTUAL_LISTED,SD.MCCAIN_DF,SD.STORE_DF, SD.MTD_SALES, SD.PACKING_SIZE, CD.BRAND_CD,CD.BRAND " +
                                    "FROM openingHeader_FOOD_STORE_data CD INNER JOIN FOOD_STORE_DATA SD ON CD.KEY_ID=SD.Common_Id WHERE CD.STORE_CD= '"
                                    + storeId + "' AND CD.BRAND_CD = '" + brand_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FoodStoreInsertDataGetterSetter sb = new FoodStoreInsertDataGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU")));

                    sb.setAs_per_meccain(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("AS_PER_MCCAIN")));

                    sb.setActual_listed(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ACTUAL_LISTED")));
                    sb.setMccain_df(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("MCCAIN_DF")));
                    sb.setStore_df(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_DF")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));
                    sb.setMtd_sales(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("MTD_SALES")));
                    sb.setPacking_size(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PACKING_SIZE")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingFoodStoredat---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    //check if Sku Data is filled
    public boolean isSkuMasterDownloaded() {
        boolean filled = false;

        Cursor dbcursor = null;

        try {

            dbcursor = db
                    .rawQuery(
                            "SELECT * FROM SKU_MASTER ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }


    //opening stock

    public boolean checkStock(String storeId) {
        Log.d("Fetching", "Opening Stock data--------------->Start<------------");
        ArrayList<StockGetterSetter> list = new ArrayList<StockGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD " +
                    "FROM openingHeader_data CD " +
                    "INNER JOIN STOCK_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "WHERE CD.STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockGetterSetter sb = new StockGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return false;
        }

        Log.d("FetchingOPening midday---------------------->Stop<-----------",
                "-------------------");
        return false;
    }


    public boolean checkStockBackOffice(String storeId) {
        Log.d("Fetching", "Opening Stock data--------------->Start<------------");
        ArrayList<StockGetterSetter> list = new ArrayList<StockGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD " +
                    "FROM openingHeaderBackOffice_data CD " +
                    "INNER JOIN STOCK_BACKOFFICE_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "WHERE CD.STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockGetterSetter sb = new StockGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return false;
        }

        Log.d("FetchingOPening midday---------------------->Stop<-----------",
                "-------------------");
        return false;
    }


    //opening stock
    public ArrayList<HeaderGetterSetter> getHeaderClosingBackOfficeStock(String storeId) {
        Log.d("FetchingOpening Stock data--------------->Start<------------",
                "------------------");
        ArrayList<HeaderGetterSetter> list = new ArrayList<HeaderGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db
                    .rawQuery("SELECT KEY_ID FROM openingHeaderBackOffice_data WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    HeaderGetterSetter sb = new HeaderGetterSetter();
                    sb.setKeyId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingOPening midday---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    //Header Key data
    public ArrayList<HeaderGetterSetter> getHeaderStock(String storeId) {
        Log.d("FetchingOpening Stock data--------------->Start<------------",
                "------------------");
        ArrayList<HeaderGetterSetter> list = new ArrayList<HeaderGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db
                    .rawQuery("SELECT KEY_ID FROM openingHeader_data WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    HeaderGetterSetter sb = new HeaderGetterSetter();
                    sb.setKeyId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingOPening midday---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    //get opening stock data from database
    public ArrayList<StockNewGetterSetter> getOpeningStockDataFromDatabase(String store_cd, String categord_cd) {
        Log.d("FetchingOpening", " Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD.DATE_1,SD.DATE_2,SD.DATE_3,SD.STOCK_1,SD.STOCK_2,SD.STOCK_3, " +
                    "SD.SKU_CD,SD.COMPANY_CD, SD.SKU, SM.BRAND_CD, SM.BRAND, " +
                    " SD.OPENING_FACING ,SD.STOCK_UNDER_DAYS " +
                    "FROM openingHeader_data CD " +
                    "INNER JOIN STOCK_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "INNER JOIN SKU_MASTER SM ON SD.SKU_CD=SM.SKU_CD " +
                    "WHERE CD.STORE_CD= '" + store_cd + "' AND CD.CATEGORY_CD = '" + categord_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));
                    sb.setStock_under45days(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_UNDER_DAYS")));
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));
                    sb.setDate1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_1")));
                    sb.setDate2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_2")));
                    sb.setDate3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_3")));
                    sb.setStock1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_1")));
                    sb.setStock2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_2")));
                    sb.setStock3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("FetchingOPening", " midday---------------------->Stop<-----------");

        return list;
    }

    public ArrayList<StockNewGetterSetter> getOpeningStockBackOfficeDataFromDatabase(String store_cd, String categord_cd) {
        Log.d("FetchingOpening", " Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD.DATE_1,SD.DATE_2,SD.DATE_3,SD.STOCK_1,SD.STOCK_2,SD.STOCK_3, " +
                    "SD.SKU_CD,SD.COMPANY_CD, SD.SKU, SM.BRAND_CD, SM.BRAND ,SD.STOCK_UNDER_DAYS " +
                    "FROM openingHeaderBackOffice_data CD " +
                    "INNER JOIN STOCK_BACKOFFICE_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "INNER JOIN SKU_MASTER SM ON SD.SKU_CD=SM.SKU_CD " +
                    "WHERE CD.STORE_CD= '" + store_cd + "' AND CD.CATEGORY_CD = '" + categord_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    // sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));
                    sb.setStock_under45days(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_UNDER_DAYS")));
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));

                    sb.setDate1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_1")));
                    sb.setDate2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_2")));
                    sb.setDate3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_3")));
                    sb.setStock1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_1")));
                    sb.setStock2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_2")));
                    sb.setStock3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("FetchingOPening", " midday---------------------->Stop<-----------");

        return list;
    }


    //check if table is empty
    public boolean isOpeningDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  OPENING_FACING " + "FROM STOCK_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }

        return filled;
    }


    public boolean isOpeningBackOfficeDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  STOCK_1 " + "FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }

        return filled;
    }

    public boolean isClosingBackOfficeDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT CLOSING_STOCK FROM STOCK_BACKOFFICE_DATA " + "WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }


    //check if table is empty
    public boolean isOpeningDataAllFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT CLOSING_STOCK FROM STOCK_DATA WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")) == null ||
                            dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

/*
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }
*/


        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }

        return filled;
    }

    public boolean isStockBackRoomDataAllFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT CLOSING_STOCK FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")) == null || dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }



/*
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
*/
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }
        return filled;
    }


//get Closing stock data from database

    public ArrayList<StockNewGetterSetter> getClosingStockDataFromDatabase(String account_cd, String category_cd, String store_cd) {
        Log.d("Fetching", "Opening Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD AS SKU_CD, SD.SKU AS SKU,SD.BRAND_CD AS BRAND_CD,SD.BRAND AS BRAND, S.STOCK_1" +
                    " AS OPENING_STOCK, M.MIDDAY_STOCK AS MIDDAY_STOCK, F.STOCK_1 AS OPENING_STOCK_BACKROOM, ifnull(F.CLOSING_STOCK,0) AS CLOSING_STOCK_BACKROOM, S.CLOSING_STOCK AS CLOSING_STOCK_FLOOR " +
                    " FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN (SELECT * FROM STOCK_DATA WHERE STORE_CD = " + store_cd + " )  S ON SD.SKU_CD=S.SKU_CD " +
                    "INNER JOIN (SELECT * FROM MID_DAY_DATA WHERE STORE_CD = " + store_cd + " ) M ON SD.SKU_CD = M.SKU_CD " +
                    "INNER JOIN (SELECT * FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD = " + store_cd + " ) F ON SD.SKU_CD = F.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD= '" + account_cd + "' AND SD.CATEGORY_CD ='" + category_cd + "' AND S.COMPANY_CD='" + "1" + "' " + " ORDER BY SD.SKU_SEQUENCE", null);




            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    sb.setEd_closingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_FLOOR")));
                    sb.setOpening_stock_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK_BACKROOM")));
                   /* if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_BACKROOM"))==null){
                        sb.setClosing_stk_backroom("0");
                    }else {
                        sb.setClosing_stk_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_BACKROOM")));

                    }*/
                    sb.setClosing_stk_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_BACKROOM")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "OPening midday---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getClosingStockBackOfficeDataFromDatabase(String account_cd, String category_cd,String store_cd) {
        Log.d("Fetching", "Opening Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {



            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD AS SKU_CD, SD.SKU AS SKU,SD.BRAND_CD AS BRAND_CD,SD.BRAND AS BRAND, S.STOCK_1" +
                    " AS OPENING_STOCK, M.MIDDAY_STOCK AS MIDDAY_STOCK, F.STOCK_1 AS OPENING_STOCK_BACKROOM, " +
                    "S.CLOSING_STOCK AS CLOSING_STOCK_FLOOR,F.CLOSING_STOCK AS CLOSING_STOCK_BACKROOM " +
                    " FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN (SELECT * FROM STOCK_DATA WHERE STORE_CD ='" + store_cd +"'"+ " ) S ON SD.SKU_CD=S.SKU_CD " +
                    "INNER JOIN (SELECT * FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD ='" + store_cd +"'"+ " )F ON SD.SKU_CD=F.SKU_CD " +
                    "INNER JOIN (SELECT * FROM MID_DAY_DATA WHERE STORE_CD = '" + store_cd +"'"+ " ) M ON SD.SKU_CD = M.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD= '" + account_cd + "' AND SD.CATEGORY_CD ='" + category_cd + "' AND S.COMPANY_CD='1' " + " ORDER BY SD.SKU_SEQUENCE", null);



           /* SELECT DISTINCT SD.SKU_CD AS SKU_CD, SD.SKU AS SKU,SD.BRAND_CD AS BRAND_CD,SD.BRAND AS BRAND, S.STOCK_1
            AS OPENING_STOCK, M.MIDDAY_STOCK AS MIDDAY_STOCK, F.STOCK_1 AS OPENING_STOCK_BACKROOM, S.CLOSING_STOCK AS CLOSING_STOCK
            FROM MAPPING_STOCK CD INNER JOIN SKU_MASTER SD ON CD.SKU_CD = SD.SKU_CD
            INNER JOIN (SELECT * FROM STOCK_DATA WHERE STORE_CD = 3 )  S ON SD.SKU_CD=S.SKU_CD
            INNER JOIN (SELECT * FROM STOCK_BACKOFFICE_DATA WHERE STORE_CD = 3 )  F ON SD.SKU_CD= F.SKU_CD
            INNER JOIN (SELECT * FROM MID_DAY_DATA WHERE STORE_CD = 3 ) M ON SD.SKU_CD = M.SKU_CD
            WHERE CD.KEYACCOUNT_CD= '9' AND SD.CATEGORY_CD ='1' AND S.COMPANY_CD='1'  ORDER BY SD.SKU_SEQUENCE*/


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    sb.setEd_closingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_FLOOR")));
                    sb.setClosing_stk_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_BACKROOM")));
                    sb.setOpening_stock_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK_BACKROOM")));

                    //getClosing_stk_backroom
                   // setEd_closingFacing

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "OPening midday---------------------->Stop<-----------");
        return list;
    }


//get Closing stock data from database


    //check if table is empty
    public boolean isClosingDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT CLOSING_STOCK FROM STOCK_DATA " + "WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }


//get Midday stock data from database

    public ArrayList<StockNewGetterSetter> getMiddayStockDataFromDatabase(String account_cd, String categord_cd) {
        Log.d("Fetching", "Mid Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND, " +
                    " (STOCK_1+STOCK_2) AS OPENING_STOCK,S.OPENING_FACING,S.MIDDAY_STOCK,S.STOCK_UNDER_DAYS " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "INNER JOIN STOCK_DATA S " +
                    "on S.SKU_CD=SD.SKU_CD " +
                    "WHERE CD.KEYACCOUNT_CD= '" + account_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "'AND S.COMPANY_CD='" + "1" + "' " +
                    "ORDER BY SD.SKU_SEQUENCE", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setStock_under45days(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_UNDER_DAYS")));
                    sb.setSumofSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_STOCK")));
                    sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records Mid Stock!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", "Mid Stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getMiddayDataFromDatabase(String store_cd, String brand_cd) {
        Log.d("Fetching", "Mid Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND,SD.MIDDAY_STOCK " +
                    "FROM MID_DAY_DATA SD " +
                    "WHERE SD.BRAND_CD= '" + brand_cd + "' AND SD.STORE_CD ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records Mid Stock!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", "Mid Stock---------------------->Stop<-----------");
        return list;
    }


    //check if table is empty
    public boolean isMiddayDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT MIDDAY_STOCK FROM MID_DAY_DATA " +
                    "WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }
        return filled;
    }


//deletecross Food Store data

    public void deleteFoodStoreData(String storeid) {

        try {

            db.delete(CommonString.TABLE_FOOD_STORE_DATA,
                    CommonString.KEY_STORE_CD + "='" + storeid + "'", null);

            db.delete(CommonString.TABLE_INSERT_HEADER_FOOD_STORE_DATA,
                    CommonString.KEY_STORE_CD + "='" + storeid + "'", null);

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }
    }


//deletecross Promotion Data

    public void deletePromotionData(String storeid) {
        try {
            db.delete(CommonString.TABLE_PROMOTION_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_PROMOTION_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
        }
    }

    //deletecross Asset Data
    public void deleteAssetData(String storeid, ArrayList<HeaderGetterSetter> header_list) {
        try {

            for (int i = 0; i < header_list.size(); i++) {
                db.delete(CommonString.TABLE_ASSET_CHECKLIST_DATA, CommonString.COMMONID + "='" + header_list.get(i).getKeyId() + "'", null);
                db.delete(CommonString.TABLE_ASSET_SKU_CHECKLIST_INSERT, CommonString.COMMONID + "='" + header_list.get(i).getKeyId() + "'", null);
            }

            db.delete(CommonString.TABLE_ASSET_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_ASSET_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);


        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
        }
    }


    //get DeepFreezerType Data
    public ArrayList<FacingCompetitorGetterSetter> getCategoryData() {

        Log.d("FetchingCategoryType--------------->Start<------------",
                "------------------");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY from CATEGORY_MASTER"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter df = new FacingCompetitorGetterSetter();


                    df.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    df.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    df.setBrand("");
                    df.setBrand_cd("");
                    df.setMccaindf("");

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Category!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingCategory data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //category for competition
    public ArrayList<FacingCompetitorGetterSetter> getCategoryCompetionData() {

        Log.d("FetchingCategoryType--------------->Start<------------",
                "------------------");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY from CATEGORY_MASTER WHERE CATEGORY_CD IN(SELECT DISTINCT CATEGORY_CD FROM BRAND_MASTER WHERE COMPANY_CD <>" + "'1'" + ")"
                    , null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter df = new FacingCompetitorGetterSetter();


                    df.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    df.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    df.setBrand("");
                    df.setBrand_cd("");
                    df.setMccaindf("");
                    //df.setStoredf("");

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Category!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingCategory data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //category for competition faceup
    public ArrayList<FacingCompetitorGetterSetter> getCategoryCompetionFaceupData(String store_cd) {

        Log.d("FetchingCategoryType--------------->Start<------------",
                "------------------");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT CATEGORY_CD, CATEGORY FROM CATEGORY_MASTER WHERE CATEGORY_CD IN(SELECT DISTINCT CATEGORY_CD FROM SKU_MASTER WHERE SKU_CD IN( SELECT  SKU_CD FROM MAPPING_STOCK  WHERE STORE_CD = '" + store_cd + "'))"
                    , null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter df = new FacingCompetitorGetterSetter();


                    df.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    df.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    df.setBrand("");
                    df.setBrand_cd("");
                    df.setMccaindf("");
                    //df.setStoredf("");

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Category!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingCategory data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get Brand Data
    public ArrayList<StockNewGetterSetter> getBrandData(String category_id) {

        Log.d("FetchingBrand--------------->Start<------------",
                "------------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT BRAND_CD, BRAND from BRAND_MASTER  WHERE CATEGORY_CD" + " = '" + category_id +
                            "' "
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter df = new StockNewGetterSetter();


                    df.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    df.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Brand!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Brand data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get Brand Data for competion faceup
    public ArrayList<FacingCompetitorGetterSetter> getBrandCompetitionData(String category_id) {

        Log.d("FetchingBrand-->Start<-",
                "------------------");
        ArrayList<FacingCompetitorGetterSetter> list = new ArrayList<FacingCompetitorGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT BRAND_CD, BRAND from BRAND_MASTER  WHERE CATEGORY_CD" + " = '" + category_id +
                            "' AND COMPANY_CD <> " + "'1'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    FacingCompetitorGetterSetter df = new FacingCompetitorGetterSetter();


                    df.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    df.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));
                    df.setMccaindf("");

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception fetch Brand",
                    e.toString());
            return list;
        }

        Log.d("Brand data-->Stop<-",
                "-------------------");
        return list;

    }


    public int CheckMid(String currdate, String storeid) {

        Cursor dbcursor = null;
        int mid = 0;
        try {
            dbcursor = db.rawQuery("SELECT  * from "
                    + CommonString.TABLE_COVERAGE_DATA + "  WHERE "
                    + CommonString.KEY_VISIT_DATE + " = '" + currdate
                    + "' AND " + CommonString.KEY_STORE_ID + " ='" + storeid
                    + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                mid = dbcursor.getInt(dbcursor
                        .getColumnIndexOrThrow(CommonString.KEY_ID));

                dbcursor.close();

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
        }

        return mid;
    }


    public long InsertCoverageData(CoverageBean data) {
        long l=0;
        ContentValues values = new ContentValues();
        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_IN_TIME, data.getInTime());
            values.put(CommonString.KEY_OUT_TIME, data.getOutTime());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_COVERAGE_STATUS, data.getStatus());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_IMAGE2, data.getImage1());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_PJP_DEVIATION, data.isPJPDeviation());
            l= db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Closes Data ",
                    ex.toString());
        }
        return l;
    }

    // getCoverageData
    public ArrayList<CoverageBean> getCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setImage1((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE2))))));
                    sb.setReason((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));

                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!" + e.toString());
        }

        return list;
    }

    public ArrayList<CoverageBean> getCoveragePreviousData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + " <> '" + visitdate + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setImage1((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE2))))));
                    sb.setReason((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));

                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!" + e.toString());
        }

        return list;
    }

    // getCoverageData
    public ArrayList<CoverageBean> getCoverageSpecificData(String store_id) {

        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_STORE_ID + "='" + store_id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setInTime(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IN_TIME)));
                    sb.setOutTime(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));

                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE3)) == null) {
                        sb.setCheckout_img("");
                    } else {
                        sb.setCheckout_img((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE3))))));
                    }

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    //check if table is empty
    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        String status = "INVALID";

        try {

            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA " + "where " +
                    CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "' AND Coverage <>'" + status + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }
        return filled;
    }

    public long updateCoverageStatus(int mid, String status) {
        long l=0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_COVERAGE_STATUS, status);
           l= db.update(CommonString.TABLE_COVERAGE_DATA, values, CommonString.KEY_ID + "=" + mid, null);
        } catch (Exception e) {

        }
        return l;
    }

    public long updateCoverageStatusNew(String store_id, String status) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_COVERAGE_STATUS, status);
            l = db.update(CommonString.TABLE_COVERAGE_DATA, values, CommonString.KEY_STORE_ID + "=" + store_id, null);
        } catch (Exception e) {

        }
        return l;
    }


    public long updateCoverageStoreOutTime(String StoreId, String VisitDate,String checkout_img, String outtime, String status) {
        long l=0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_OUT_TIME, outtime);
            values.put(CommonString.KEY_IMAGE3, checkout_img);
            values.put(CommonString.KEY_COVERAGE_STATUS, status);
          l=  db.update(CommonString.TABLE_COVERAGE_DATA, values, CommonString.KEY_STORE_ID + "='" + StoreId + "' AND " + CommonString.KEY_VISIT_DATE + "='" + VisitDate + "'", null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return l;
    }

    public long updateStoreStatusOnLeave(String storeid, String visitdate, String status) {
        long l=0;
        try {
            ContentValues values = new ContentValues();
            values.put("UPLOAD_STATUS", status);
          l=  db.update("JOURNEY_PLAN", values, CommonString.KEY_STORE_CD + "='" + storeid + "' AND " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);
        } catch (Exception e) {
        }
        return  l;
    }


    //UpdateJourney Plan
    public long updateStoreStatusOnCheckout(String storeid, String visitdate, String status) {
        long l=0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_CHECKOUT_STATUS, status);
          l=  db.update("JOURNEY_PLAN", values, CommonString.KEY_STORE_CD + "='" + storeid + "' AND " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);
        } catch (Exception e) {

        }
        return l;
    }

    //Insert Calls Data
    public void insertCallsData(String store_cd, String total_calls, String productive_calls) {
        db.delete("CALLS_DATA", "STORE_CD" + "='" + store_cd + "'", null);
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_CD", store_cd);
            values.put("TOTAL_CALLS", total_calls);
            values.put("PRODUCTIVE_CALLS", productive_calls);
            db.insert(CommonString.TABLE_CALLS_DATA, null, values);
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Calls Data ",
                    ex.toString());
        }

    }


    //get Calls Data
    public ArrayList<CallsGetterSetter> getCallsData(String store_cd) {

        Log.d("Fetching calls--------------->Start<------------",
                "------------------");
        ArrayList<CallsGetterSetter> list = new ArrayList<CallsGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from CALLS_DATA where STORE_CD = '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CallsGetterSetter fc = new CallsGetterSetter();


                    fc.setStore_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_CD")));
                    fc.setTotal_calls(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("TOTAL_CALLS")));
                    fc.setProductive_calls(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PRODUCTIVE_CALLS")));
                    fc.setKey_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("Key_Id")));

                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Calls data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Calls---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    //get PJP Deviation Stores List Data


    public void deleteStockRow(int id, String store_cd) {

        try {
            db.delete(CommonString.TABLE_CALLS_DATA, CommonString.KEY_STORE_CD + "='" + store_cd + "' AND "
                            + "Key_Id" + "='" + id + "'"
                    , null);
        } catch (Exception e) {

        }

    }

    //Insert Calls Data
    public void insertCompetitionPOIData(String store_cd, POIGetterSetter poiGetterSetter) {
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_CD", store_cd);
            values.put("CATEGORY_CD", poiGetterSetter.getCategory_cd());
            values.put("CATEGORY", poiGetterSetter.getCategory());
            values.put("ASSET_CD", poiGetterSetter.getAsset_cd());
            values.put("ASSET", poiGetterSetter.getAsset());
            values.put("BRAND", poiGetterSetter.getBrand());
            values.put("BRAND_CD", poiGetterSetter.getBrand_cd());
            values.put("REMARK", poiGetterSetter.getRemark());

            db.insert(CommonString.TABLE_COMPETITION_POI, null, values);


        } catch (Exception ex) {
            Log.d("Database Exception while Insert Calls Data ",
                    ex.toString());
        }

    }

    //get Calls Data
    public ArrayList<POIGetterSetter> getCompetitionPOIData(String store_cd) {

        Log.d("Fetching calls--------------->Start<------------",
                "------------------");
        ArrayList<POIGetterSetter> list = new ArrayList<POIGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from COMPETITION_POI WHERE STORE_CD= '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    POIGetterSetter fc = new POIGetterSetter();


                    fc.setId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("Key_Id")));
                    fc.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    fc.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    fc.setAsset_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET_CD")));
                    fc.setAsset(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET")));
                    fc.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    fc.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    fc.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching comp poi data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Calls---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    public void deleteCompetitionPOIRow(int id) {

        try {
            db.delete(CommonString.TABLE_COMPETITION_POI, "Key_Id " + "='" + id + "'"
                    , null);
        } catch (Exception e) {

        }

    }

    //Insert Calls Data
    public void insertCompetitionPromotionData(CompetitionPromotionGetterSetter poiGetterSetter, String store_cd) {

        ContentValues values = new ContentValues();

        try {

            values.put("STORE_CD", store_cd);
            values.put("CATEGORY_CD", poiGetterSetter.getCategory_cd());
            values.put("CATEGORY", poiGetterSetter.getCategory());
            values.put("BRAND_CD", poiGetterSetter.getBrand_cd());
            values.put("BRAND", poiGetterSetter.getBrand());
            values.put("REMARK", poiGetterSetter.getRemark());
            values.put("PROMOTION", poiGetterSetter.getPromotion());

            db.insert(CommonString.TABLE_COMPETITION_PROMOTION, null, values);


        } catch (Exception ex) {
            Log.d("Database Exception while Insert Calls Data ",
                    ex.toString());
        }

    }

    //get Competition Promotion Data
    public ArrayList<CompetitionPromotionGetterSetter> getCompetitionPromotionData(String store_cd) {

        Log.d("Fetching calls--------------->Start<------------",
                "------------------");
        ArrayList<CompetitionPromotionGetterSetter> list = new ArrayList<CompetitionPromotionGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from COMPETITION_PROMOTION WHERE STORE_CD= '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CompetitionPromotionGetterSetter fc = new CompetitionPromotionGetterSetter();

                    fc.setId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("Key_Id")));
                    fc.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    fc.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    fc.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    fc.setPromotion(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PROMOTION")));
                    fc.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    fc.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching comp poi data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Calls---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    public void deleteCompetitionPromotionRow(int id) {

        try {
            db.delete(CommonString.TABLE_COMPETITION_PROMOTION, "Key_Id " + "='" + id + "'"
                    , null);
        } catch (Exception e) {

        }

    }

    //Insert Calls Data
    public void insertPOIData(POIGetterSetter poiGetterSetter, String store_cd) {

        ContentValues values = new ContentValues();

        try {

            values.put("STORE_CD", store_cd);
            values.put("CATEGORY_CD", poiGetterSetter.getCategory_cd());
            values.put("CATEGORY", poiGetterSetter.getCategory());
            values.put("ASSET_CD", poiGetterSetter.getAsset_cd());
            values.put("ASSET", poiGetterSetter.getAsset());
            values.put("BRAND_CD", poiGetterSetter.getBrand_cd());
            values.put("BRAND", poiGetterSetter.getBrand());
            values.put("REMARK", poiGetterSetter.getRemark());
            values.put("IMAGE_POI", poiGetterSetter.getImage());
            db.insert(CommonString.TABLE_POI, null, values);


        } catch (Exception ex) {
            Log.d("Database Exception while Insert Calls Data ",
                    ex.toString());
        }

    }

    //get Calls Data
    public ArrayList<POIGetterSetter> getPOIData(String store_cd) {

        Log.d("Fetching calls--------------->Start<------------",
                "------------------");
        ArrayList<POIGetterSetter> list = new ArrayList<POIGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from POI WHERE STORE_CD= '" + store_cd + "'"
                    , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    POIGetterSetter fc = new POIGetterSetter();

                    fc.setId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("Key_Id")));
                    fc.setCategory_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY_CD")));
                    fc.setCategory(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CATEGORY")));
                    fc.setAsset_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET_CD")));
                    fc.setAsset(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ASSET")));
                    fc.setBrand_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_CD")));
                    fc.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));
                    fc.setRemark(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("REMARK")));
                    fc.setImage(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("IMAGE_POI")));
                    list.add(fc);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching comp poi data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching facing Calls---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    public void deletePOIRow(int id, String store_cd) {

        try {
            db.delete(CommonString.TABLE_POI, CommonString.KEY_STORE_CD + "='" + store_cd + "' AND "
                            + "Key_Id " + "='" + id + "'"
                    , null);
        } catch (Exception e) {

        }

    }

    /// get store Status
    public JourneyPlanGetterSetter getStoreStatus(String id) {

        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");

        JourneyPlanGetterSetter sb = new JourneyPlanGetterSetter();

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from  JOURNEY_PLAN"
                    + "  WHERE STORE_CD = '"
                    + id + "'", null);

            if (dbcursor != null) {
                int numrows = dbcursor.getCount();

                dbcursor.moveToFirst();
                for (int i = 0; i < numrows; i++) {

                    sb.setStore_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_CD)));

                    sb.setCheckOutStatus((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKOUT_STATUS"))));

                    sb.setUploadStatus(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("UPLOAD_STATUS")));

                    dbcursor.moveToNext();

                }

                dbcursor.close();

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
        }

        Log.d("FetchingStoredat---------------------->Stop<-----------",
                "-------------------");
        return sb;

    }

    //Get Deviation store status
    public JourneyPlanGetterSetter getDeviationStoreStatus(String id) {

        Log.d("FetchingStoredata--------------->Start<------------",
                "------------------");

        JourneyPlanGetterSetter sb = new JourneyPlanGetterSetter();

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from  JOURNEY_DEVIATION"
                    + "  WHERE STORE_CD = '"
                    + id + "'", null);

            if (dbcursor != null) {
                int numrows = dbcursor.getCount();

                dbcursor.moveToFirst();
                for (int i = 0; i < numrows; i++) {

                    sb.setStore_cd(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_CD)));

                    sb.setCheckOutStatus((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKOUT_STATUS"))));

                    sb.setUploadStatus(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("UPLOAD_STATUS")));

                    dbcursor.moveToNext();

                }

                dbcursor.close();

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
        }

        Log.d("FetchingStoredat---------------------->Stop<-----------",
                "-------------------");
        return sb;

    }


    public void deletePaySlipData() {

        db.delete("EMP_SALARY", null, null);
    }

    //Salary Pay Slip data
    public void insertPaySlipdata(PayslipGetterSetter data) {
        db.delete("EMP_SALARY", null, null);
        ContentValues values = new ContentValues();

        try {
            values.put("SALARY_MONTH", data.getMONTH().get(0));
            values.put("SALARY_YEAR", data.getSALARY_YEAR().get(0));
            values.put("ECODE", data.getECODE().get(0));
            values.put("EMP_NAME", data.getEMP_NAME().get(0));
            values.put("PAYMENT_MODE", data.getPAYMENT_MODE().get(0));
            values.put("PRESENT_DAYS", data.getPRESENT_DAYS().get(0));
            values.put("INCENTIVE", data.getINCENTIVE().get(0));
            values.put("NATIONAL_H", data.getNATIONAL_H().get(0));
            values.put("TOTAL_EARNING", data.getTOTAL_EARNING().get(0));
            values.put("PF", data.getPF().get(0));
            values.put("ESI", data.getESI().get(0));
            values.put("PT", data.getPT().get(0));
            values.put("LWF", data.getLWF().get(0));
            values.put("MIS_DEDUCTION", data.getMIS_DEDUCTION().get(0));
            values.put("TDS", data.getTDS().get(0));
            values.put("TAKE_HOME", data.getTAKE_HOME().get(0));

            db.insert("EMP_SALARY", null, values);

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Non Working Data ", ex.toString());
        }
    }


    public void insertIncentiveTypeData(IncentiveGetterSetter data) {
        db.delete("EMP_INCENTIVE", null, null);
        ContentValues values = new ContentValues();

        try {
            values.put("MONTH", data.getMonth().get(0));
            values.put("YEAR", data.getYear().get(0));
            values.put("INCENTIVE", data.getIncentive().get(0));
            db.insert("EMP_INCENTIVE", null, values);

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Non Working Data ", ex.toString());
        }
    }

    public void insertMappingSosData(MappingSosGetterSetter data) {
        db.delete("MAPPING_SOS", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getBrand_cd().size(); i++) {
                values.put("BRAND_CD", Integer.parseInt(data.getBrand_cd().get(i)));
                db.insert("MAPPING_SOS", null, values);

            }
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Mapping sos Data ",
                    ex.toString());
        }

    }


    //get Payslip Data
    public ArrayList<PayslipGetterSetter> getPaySlipData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<PayslipGetterSetter> list = new ArrayList<PayslipGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from EMP_SALARY", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PayslipGetterSetter sb = new PayslipGetterSetter();

                    sb.setMONTH(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SALARY_MONTH")));
                    sb.setSALARY_YEAR(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SALARY_YEAR")));
                    sb.setECODE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("ECODE"))));
                    sb.setEMP_NAME((dbcursor.getString(dbcursor.getColumnIndexOrThrow("EMP_NAME"))));
                    sb.setPAYMENT_MODE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("PAYMENT_MODE"))));
                    sb.setPRESENT_DAYS((dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRESENT_DAYS"))));
                    sb.setINCENTIVE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("INCENTIVE"))));
                    sb.setNATIONAL_H((dbcursor.getString(dbcursor.getColumnIndexOrThrow("NATIONAL_H"))));
                    sb.setTOTAL_EARNING((dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOTAL_EARNING"))));
                    sb.setPF((dbcursor.getString(dbcursor.getColumnIndexOrThrow("PF"))));
                    sb.setESI((dbcursor.getString(dbcursor.getColumnIndexOrThrow("ESI"))));


                    sb.setPT((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("PT"))));

                    sb.setLWF((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("LWF"))));


                    sb.setMIS_DEDUCTION((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("MIS_DEDUCTION"))));

                    sb.setTDS((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("TDS"))));

                    sb.setTAKE_HOME((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("TAKE_HOME"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching JCP!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingJCP data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    public ArrayList<StockNewGetterSetter> getPaidVisibilitySkuData(String store_cd, String categord_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND " +
                    "FROM MAPPING_STOCK CD " +
                    "INNER JOIN SKU_MASTER SD " +
                    "ON CD.SKU_CD = SD.SKU_CD " +
                    "WHERE CD.STORE_CD= '" + store_cd + "' AND SD.CATEGORY_CD ='" + categord_cd + "' " +
                    "ORDER BY SD.SKU_SEQUENCE", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setChk_skuBox("0");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }

        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<SkuGetterSetter> getPaidVisibilitySkuListData(String key_id) {
        Log.d("Fetching ", "checklist data--------------->Start<------------");
        ArrayList<SkuGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_ASSET_SKU_CHECKLIST_INSERT +
                    " WHERE COMMONID='" + key_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SkuGetterSetter sb = new SkuGetterSetter();

                    sb.setSKU_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSKU(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBRAND_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBRAND(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setQUANTITY(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_QUANTITY")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching checklist data!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", " checklist data---------------------->Stop<-----------");
        return list;
    }


    public boolean isStoreAssetDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY " +
                    "FROM CATEGORY_MASTER BD " +
                    "WHERE CATEGORY_CD IN( SELECT DISTINCT CATEGORY_CD FROM MAPPING_ASSET_NEW " +
                    "WHERE STORE_CD ='" + storeId + "' ) ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();

                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }
        return filled;
    }

    //Data Upload
    public ArrayList<StockNewGetterSetter> getOpeningStockUpload(String storeId) {
        Log.d("Fetching", "Stock upload data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_STOCK_DATA + " WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setStore_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_CD")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setDate1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_1")));
                    sb.setStock1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_1")));
                    sb.setDate2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_2")));
                    sb.setStock2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_2")));
                    sb.setDate3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_3")));
                    sb.setStock3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_3")));
                    sb.setEd_openingFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OPENING_FACING")));
                    //midday stock xml
                    String midday_stock = dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK"));
                    if (midday_stock == null || midday_stock.equals("")) {
                        sb.setEd_midFacing("0");
                    } else {
                        sb.setEd_midFacing(midday_stock);
                    }
                    //Closing stock xml data
                    String closing_stock = dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK"));
                    if (closing_stock == null || closing_stock.equals("")) {
                        sb.setEd_closingFacing("0");
                    } else {
                        sb.setEd_closingFacing(closing_stock);
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Stock Data Fetching------->Stop<-------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getOpeningBackRoomStockUpload(String storeId) {
        Log.d("Fetching", "Stock upload data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_STOCK_BACKOFFICE_DATA + " WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();
                    sb.setStore_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_CD")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                 //   sb.setDate1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_1")));
                    sb.setStock1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_1")));
                   // sb.setDate2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_2")));
                    sb.setStock2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_2")));
                  //  sb.setDate3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DATE_3")));
                    sb.setStock3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_3")));

                  //  sb.setClosing_stk_backroom(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK_BACKROOM")));
                    String closing_stock = dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLOSING_STOCK"));
                    if (closing_stock == null || closing_stock.equals("")) {
                        sb.setClosing_stk_backroom("0");
                    } else {
                        sb.setClosing_stk_backroom(closing_stock);
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Stock Data Fetching------->Stop<-------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getStockImageUploadData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY, IMAGE_STK, IMAGE_CAT_ONE, IMAGE_CAT_TWO, CAT_STOCK," +
                    "HIMALAYA_PHOTO, CATEGORY_PHOTO  " +
                    "FROM STOCK_IMAGE " +
                    "WHERE STORE_CD ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                 /*   sb.setImg_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_STK")));*/
                    sb.setImg_cat_one(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_ONE")));
                  /*  sb.setImg_cat_two(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_TWO")));*/
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));
                  //  sb.setCatstock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_STOCK")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getStockBackRoomImageUploadData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT DISTINCT CATEGORY_CD, CATEGORY, IMAGE_STK, IMAGE_CAT_ONE, IMAGE_CAT_TWO," +
                    "HIMALAYA_PHOTO, CATEGORY_PHOTO  " +
                    "FROM STOCK_BACKOFFICE_IMAGE " +
                    "WHERE STORE_CD ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setImg_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_STK")));
                    sb.setImg_cat_one(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_ONE")));
                    sb.setImg_cat_two(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_TWO")));
                    sb.setHimalaya_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("HIMALAYA_PHOTO")));
                    sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<PromotionInsertDataGetterSetter> getPromotionUploadData(String storeId) {
        Log.d("Fetching", "Promotion Upload Data--------------->Start<------------");
        ArrayList<PromotionInsertDataGetterSetter> list = new ArrayList<PromotionInsertDataGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT SD.PID, SD.PRESENT_SPIN,SD.REMARK_CD,SD.CAMERA, SD.PROMOTION,SD.REMARK, CD.BRAND_CD,CD.BRAND FROM openingHeader_Promotion_data CD INNER JOIN PROMOTION_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id WHERE CD.STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionInsertDataGetterSetter sb = new PromotionInsertDataGetterSetter();
                    sb.setPromotion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PID")));
                    sb.setPromotion_txt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION")));
                    sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK_CD")));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setCamera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAMERA")));
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRESENT_SPIN"));
                    sb.setPresentSpi(value);
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Storedat---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<HeaderGetterSetter> getAssetHeaderData(String storeId) {
        Log.d("Fetching", "Assetuploaddata--------------->Start<------------");
        ArrayList<HeaderGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT KEY_ID FROM openingHeader_Asset_data " + "WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    HeaderGetterSetter sb = new HeaderGetterSetter();
                    sb.setKeyId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Storedat---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<AssetInsertdataGetterSetter> getAssetUploadData(String storeId) {
        Log.d("Fetching", "Assetuploaddata--------------->Start<------------");
        ArrayList<AssetInsertdataGetterSetter> list = new ArrayList<AssetInsertdataGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD.ASSET_CD, SD.ASSET, SD.PRESENT, SD.REASON_CD, SD.IMAGE, CD.CATEGORY_CD, CD.CATEGORY " +
                    "FROM openingHeader_Asset_data CD " +
                    "INNER JOIN ASSET_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "WHERE CD.STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetInsertdataGetterSetter sb = new AssetInsertdataGetterSetter();
                    sb.setAsset_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET_CD")));
                    sb.setAsset(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET")));
                    sb.setReason_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_CD")));
                    sb.setImg(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    String toggle = dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRESENT"));
                    if (toggle.equalsIgnoreCase("Yes")) {
                        sb.setPresent("1");
                    } else {
                        sb.setPresent("0");
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "Storedat---------------------->Stop<-----------");
        return list;
    }


    //Audit Question Data
    public void insertAuditQuestionData(Audit_QuestionGetterSetter data) {
        db.delete("AUDIT_QUESTION_CATEGORYWISE", null, null);
        ContentValues values = new ContentValues();

        try {
            for (int i = 0; i < data.getQUESTION_ID().size(); i++) {
                values.put("QUESTION_ID", data.getQUESTION_ID().get(i));
                values.put("QUESTION", data.getQUESTION().get(i));
                values.put("ANSWER_ID", data.getANSWER_ID().get(i));
                values.put("ANSWER", data.getANSWER().get(i));
                values.put("QUESTION_TYPE", data.getQUESTION_TYPE().get(i));
                values.put("CATEGORY_ID", data.getCATEGORY_ID().get(i));
                values.put("CATEGORY", data.getCATEGORY().get(i));

                db.insert("AUDIT_QUESTION_CATEGORYWISE", null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Audit Question Data " + ex.toString());
        }
    }

    public ArrayList<Audit_QuestionDataGetterSetter> getAuditQuestionData(String category_id) {
        Log.d("Fetching", "AuditQuestion Data--------------->Start<------------");
        ArrayList<Audit_QuestionDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT QUESTION_ID,QUESTION " +
                    "From AUDIT_QUESTION_CATEGORYWISE WHERE CATEGORY_ID='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Audit_QuestionDataGetterSetter sb = new Audit_QuestionDataGetterSetter();

                    sb.setQuestion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")));
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setSp_answer_id("0");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<Audit_QuestionGetterSetter> getCategoryQuestionData() {
        Log.d("Fetching", "Category Data--------------->Start<------------");
        ArrayList<Audit_QuestionGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT CATEGORY_ID, CATEGORY " +
                    "From AUDIT_QUESTION_CATEGORYWISE ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Audit_QuestionGetterSetter sb = new Audit_QuestionGetterSetter();

                    sb.setCATEGORY(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setCATEGORY_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Category Data!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "Category Data---------------------->Stop<-----------");
        return list;
    }


    public void saveAuditQuestionAnswerData(ArrayList<Audit_QuestionDataGetterSetter> questionAnswerList, String store_cd, String category_cd) {
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, "STORE_CD" + "='" + store_cd + "' AND CATEGORY_ID ='" + category_cd + "'", null);

        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < questionAnswerList.size(); i++) {
                Audit_QuestionDataGetterSetter data = questionAnswerList.get(i);

                values.put("STORE_CD", store_cd);
                values.put("QUESTION_ID", data.getQuestion_id());
                values.put("QUESTION", data.getQuestion());
                values.put("ANSWER_ID", data.getSp_answer_id());
                values.put("CATEGORY_ID", category_cd);

                db.insert(CommonString.TABLE_AUDIT_DATA_SAVE, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database ", "Exception while Insert Audit Data " + ex.toString());
        }
    }

    public ArrayList<Audit_QuestionDataGetterSetter> getAfterSaveAuditQuestionAnswerData(String store_cd, String category_id) {
        Log.d("Fetching", "AuditQuestion Data--------------->Start<------------");
        ArrayList<Audit_QuestionDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * " + "From " + CommonString.TABLE_AUDIT_DATA_SAVE
                    + " where STORE_CD='" + store_cd + "' AND CATEGORY_ID ='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Audit_QuestionDataGetterSetter sb = new Audit_QuestionDataGetterSetter();

                    sb.setQuestion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")));
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setSp_answer_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_ID")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Audit after save!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "Audit after save---------------------->Stop<-----------");
        return list;
    }


    public void remove(String user_id) {
        db.execSQL("DELETE FROM MARKET_INTELLIGENCE WHERE KEY_ID = '" + user_id + "'");
    }

    public void removesampledata(String user_id) {
        db.execSQL("DELETE FROM SAMPLED_DATA WHERE KEY_ID = '" + user_id + "'");
    }

    public void removeallmarketIData(String store_cd) {
        db.delete(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, CommonString.KEY_STORE_CD + "='" + store_cd + "'", null);
    }

    public long insertmarketintelligenceData(String store_cd, String user_name, String visit_date, ArrayList<MarketIntelligenceGetterSetter> list) {
        db.delete(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, "STORE_CD" + "='" + store_cd + "'AND VISIT_DATE='" + visit_date + "'", null);
        long l = 0;
        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < list.size(); i++) {
                values.put("STORE_CD", store_cd);
                values.put("USER_ID", user_name);
                values.put("VISIT_DATE", visit_date);
                values.put("CHECKBOX", list.get(i).isExists());
                values.put("COMPANY_CD", list.get(i).getCompany_cd());
                values.put("COMPANY", list.get(i).getCompany());
                values.put("CATEGORY_CD", list.get(i).getCategory_cd());
                values.put("CATEGORY", list.get(i).getCategory());
                values.put("PROMOTYPE_CD", list.get(i).getPromotype_cd());
                values.put("PROMOTYPE", list.get(i).getPromotype());
                values.put("PHOTO", list.get(i).getPhoto());
                values.put("REMARK", list.get(i).getRemark());

                l = db.insert(CommonString.TABLE_INSERT_MARKET_INTELLI_DATA, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Facing Competition Data ", ex.toString());
        }
        return l;
    }

    public ArrayList<MarketIntelligenceGetterSetter> getinsertedMarketIntelligenceData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<MarketIntelligenceGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from MARKET_INTELLIGENCE where STORE_CD='" + store_cd + "'AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MarketIntelligenceGetterSetter sb = new MarketIntelligenceGetterSetter();
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKBOX"));
                    if (value.equals("0")) {
                        sb.setExists(false);
                    } else {
                        sb.setExists(true);
                    }
                    sb.setCompany_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_CD")));
                    sb.setCompany(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    /*sb.setPromotype_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTYPE_CD")));
                    sb.setPromotype(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTYPE")));*/
                    sb.setPhoto(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PHOTO")));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<Audit_QuestionDataGetterSetter> getAuditAnswerData(String store_cd, String question_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<Audit_QuestionDataGetterSetter> list = new ArrayList<>();
        Audit_QuestionDataGetterSetter sb1 = new Audit_QuestionDataGetterSetter();
        sb1.setAnswer_id("0");
        sb1.setAnswer("Select");
        list.add(0, sb1);

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from AUDIT_QUESTION_CATEGORYWISE " + "where QUESTION_ID='" + question_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Audit_QuestionDataGetterSetter sb = new Audit_QuestionDataGetterSetter();

                    sb.setAnswer_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_ID")));
                    sb.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public long insertSampledData(String store_cd, String user_name, String visit_date, ArrayList<SampledGetterSetter> list) {
        db.delete(CommonString.TABLE_INSERT_SAMPLED_DATA, "STORE_CD" + "='" + store_cd + "'AND VISIT_DATE='" + visit_date + "'", null);
        long l = 0;
        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < list.size(); i++) {
                values.put("STORE_CD", store_cd);
                values.put("USER_ID", user_name);
                values.put("VISIT_DATE", visit_date);
                values.put("CATEGORY_CD", list.get(i).getCategory_cd());
                values.put("CATEGORY", list.get(i).getCategory());

                values.put("SKU_CD", list.get(i).getSku_cd());
                values.put("SKU", list.get(i).getSku());
                values.put("SAMPLED", list.get(i).getSampled());
                values.put("PHOTO", list.get(i).getSampled_img());
                values.put("FEEDBACK", list.get(i).getFeedback());

                l = db.insert(CommonString.TABLE_INSERT_SAMPLED_DATA, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Facing Competition Data ", ex.toString());
        }
        return l;
    }


    public ArrayList<SampledGetterSetter> getinsertedsampledData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<SampledGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from SAMPLED_DATA where STORE_CD='" + store_cd + "'AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SampledGetterSetter sb = new SampledGetterSetter();
                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setSampled(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SAMPLED")));
                    sb.setSampled_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PHOTO")));
                    sb.setFeedback(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FEEDBACK")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<AssetMasterGetterSetter> getassetData() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<AssetMasterGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * FROM ASSET_MASTER ORDER BY ASSET_CD ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AssetMasterGetterSetter sb = new AssetMasterGetterSetter();
                    sb.setAsset_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET_CD")));
                    sb.setAsset(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ASSET")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    public void insertSubCetegoryMasterData(SubCategoryGetterSetter data) {

        db.delete("SUB_CATEGORY_MASTER", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getCATEGORY_CD().size(); i++) {

                values.put("SUB_CATEGORY_CD", Integer.parseInt(data.getSUB_CATEGORY_CD().get(i)));
                values.put("SUB_CATEGORY", data.getSUB_CATEGORY().get(i));
                values.put("CATEGORY_CD", Integer.parseInt(data.getCATEGORY_CD().get(i)));
                db.insert("SUB_CATEGORY_MASTER", null, values);
            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Brand Master Data ",
                    ex.toString());
        }

    }

    public ArrayList<SubCategoryGetterSetter> getsubcategoryTypeData(String category_cd) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<SubCategoryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("Select * from SUB_CATEGORY_MASTER " + "where CATEGORY_CD='" + category_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SubCategoryGetterSetter sb = new SubCategoryGetterSetter();
                    sb.setSUB_CATEGORY_CD(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_CD")));
                    sb.setSUB_CATEGORY(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    public ArrayList<BrandGetterSetter> getbrandData(String categorysub_cd) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<BrandGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("Select * from BRAND_MASTER " + "where SUB_CATEGORY_CD='" + categorysub_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandGetterSetter sb = new BrandGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    public void InsertSTOREgeotag(String storeid, double lat, double longitude, String path, String status) {
      //  db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();

        try {
            values.put("STORE_ID", storeid);
            values.put("LATITUDE", Double.toString(lat));
            values.put("LONGITUDE", Double.toString(longitude));
            values.put("FRONT_IMAGE", path);
            values.put("GEO_TAG", status);
            values.put("STATUS", status);
            db.insert(CommonString.TABLE_STORE_GEOTAGGING, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
        }

    }

    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String store_id) {


        ArrayList<GeotaggingBeans> geodata = new ArrayList<GeotaggingBeans>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_STORE_GEOTAGGING +
                    "  WHERE STORE_ID = '" + store_id + "'", null);
            if (dbcursor != null) {
                int numrows = dbcursor.getCount();

                dbcursor.moveToFirst();
                for (int i = 1; i <= numrows; ++i) {

                    GeotaggingBeans data = new GeotaggingBeans();

                    data.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    data.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow("LATITUDE"))));
                    data.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow("LONGITUDE"))));
                    data.setUrl1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));
                    data.setStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GEO_TAG")));



                    geodata.add(data);
                    dbcursor.moveToNext();
                }

                dbcursor.close();

            }

        } catch (Exception e) {

        } finally {
            if (dbcursor != null && !dbcursor.isClosed()) {
                dbcursor.close();
            }
        }


        return geodata;

    }

    public void updateGeoTagData(String storeid, String status) {

        try {
            ContentValues values = new ContentValues();
            values.put("GEO_TAG", status);
            int l = db.update(CommonString.TABLE_STORE_GEOTAGGING, values, CommonString.KEY_STORE_ID + "=?", new String[]{storeid});
            System.out.println("update : " + l);
        } catch (Exception e) {
            Log.d("Database Data ", e.toString());

        }
    }

    public void deleteGeoTagData(String storeid) {
        try {
            db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        } catch (Exception e) {

        }
    }
    public void updateDataStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            db.update("JOURNEY_PLAN", values, CommonString.KEY_STORE_CD + "='" + id + "'", null);

        } catch (Exception ex) {
            Log.d("Database Data ", ex.toString());

        }

    }
    public long updatecheckoutStore(String id, String status) {
        long l=0;
        ContentValues values = new ContentValues();
        try {
            values.put("CHECKOUT_STATUS", status);
           l= db.update("JOURNEY_PLAN", values, CommonString.KEY_STORE_CD + "='" + id + "'", null);

        } catch (Exception ex) {
            Log.d("Database Data ", ex.toString());

        }
        return l;

    }

    public ArrayList<IncentiveGetterSetter> getIncentiveData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<IncentiveGetterSetter> list = new ArrayList<IncentiveGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from EMP_INCENTIVE", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    IncentiveGetterSetter sb = new IncentiveGetterSetter();

                    sb.setMonth(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MONTH")));
                    sb.setYear(dbcursor.getString(dbcursor.getColumnIndexOrThrow("YEAR")));
                    sb.setIncentive((dbcursor.getString(dbcursor.getColumnIndexOrThrow("INCENTIVE"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching JCP!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("FetchingJCP data---------------------->Stop<-----------",
                "-------------------");
        return list;

    }

    public long InsertSpinnereStockinData(StoreStockinGetterSetter data, String storeid, String visitdate) {
         db.delete(CommonString.TABLE_SHARE_OF_SHELF_DATA, CommonString.KEY_STORE_ID + "='" +storeid + "'", null);
        ContentValues values = new ContentValues();
        long id = 0;
        try {

            values.put("STORE_ID", storeid);
            values.put("VISIT_DATE", visitdate);
            values.put("STOCK_BRAND", data.getSelect_brand());

            id = db.insert(CommonString.TABLE_SHARE_OF_SHELF_DATA, null, values);
            if (id > 0) {
                return id;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }

    }


    public StoreStockinGetterSetter getStockInSpinnerData(String store_id) {
        Log.d("FetchinggetCityMasterData--------------->Start<------------",
                "------------------");
        StoreStockinGetterSetter sb = new StoreStockinGetterSetter();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_SHARE_OF_SHELF_DATA+" WHERE "+CommonString.KEY_STORE_ID+" = "+store_id, null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setSelect_brand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STOCK_BRAND")));
                    sb.setKey_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("KEY_ID")));


                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching CITY MASTER!!!!!!!!!!!",
                    e.toString());
            return sb;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return sb;
    }
//upendra_12jan
public ArrayList<ShareOfShelfGetterSetter> getHeaderShareOfSelfImageData(String store_cd) {
    Log.d("Fetching", "Storedata--------------->Start<------------");
    ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
    Cursor dbcursor = null;

    try {

        dbcursor = db.rawQuery("SELECT SB.SUB_CATEGORY_CD AS SUB_CATEGORY_CD, CA.CATEGORY||'-'||SB.SUB_CATEGORY AS SUB_CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'') AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')" +
                "  AS CAT_FACING FROM CATEGORY_MASTER CA " +
                " INNER JOIN (SELECT * FROM SUB_CATEGORY_MASTER WHERE SUB_CATEGORY_CD IN( SELECT DISTINCT SUB_CATEGORY_CD FROM BRAND_MASTER WHERE BRAND_CD IN(" +
                " SELECT BRAND_CD FROM MAPPING_SOS))) AS  SB ON CA.CATEGORY_CD = SB.CATEGORY_CD " +
                " LEFT JOIN " +
                "(SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='"+store_cd+ "')"+ " AS D ON SB.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD" , null);



      /*  SELECT  SB.SUB_CATEGORY_CD, CA.CATEGORY||'-'||SB.SUB_CATEGORY
        AS SUB_CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'')
        AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')
        AS CAT_FACING FROM CATEGORY_MASTER CA
        INNER JOIN
        (SELECT * FROM SUB_CATEGORY_MASTER WHERE SUB_CATEGORY_CD IN( SELECT DISTINCT SUB_CATEGORY_CD FROM BRAND_MASTER WHERE BRAND_CD IN(
                SELECT BRAND_CD FROM MAPPING_SOS))) AS  SB ON CA.CATEGORY_CD = SB.CATEGORY_CD
        LEFT JOIN (SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='3')
        AS D ON SB.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD*/




/*
        dbcursor = db.rawQuery("SELECT SB.SUB_CATEGORY_CD, CA.CATEGORY||'-'||SB.SUB_CATEGORY AS SUB_CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'') AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')" +
                "  AS CAT_FACING FROM CATEGORY_MASTER CA " +
                " INNER JOIN (SELECT * FROM SUB_CATEGORY_MASTER WHERE SUB_CATEGORY_CD IN( SELECT DISTINCT SUB_CATEGORY_CD FROM BRAND_MASTER WHERE BRAND_CD IN " +
                " SELECT BRAND_CD FROM MAPPING_SOS))) AS  SB ON CA.CATEGORY_CD = SB.CATEGORY_CD " +
                " LEFT JOIN " +
                "(SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='"+store_cd+ "')"+ " AS D ON SB.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD" , null);
*/


      /*  SELECT  SB.SUB_CATEGORY_CD, CA.CATEGORY||'-'||SB.SUB_CATEGORY
        AS SUB_CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'')
        AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')
        AS CAT_FACING FROM CATEGORY_MASTER CA
        INNER JOIN
        (SELECT * FROM SUB_CATEGORY_MASTER WHERE SUB_CATEGORY_CD IN( SELECT DISTINCT SUB_CATEGORY_CD FROM BRAND_MASTER WHERE BRAND_CD IN(
                SELECT BRAND_CD FROM MAPPING_SOS))) AS  SB ON CA.CATEGORY_CD = SB.CATEGORY_CD
        LEFT JOIN (SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='3')
        AS D ON SB.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD*/



        if (dbcursor != null) {
            dbcursor.moveToFirst();
            while (!dbcursor.isAfterLast()) {
                ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
              //  sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_CD")));
               sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                sb.setCat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_FACING")));
                sb.setImg_cat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_FACING")));

                list.add(sb);
                dbcursor.moveToNext();
            }
            dbcursor.close();
            return list;
        }
    } catch (Exception e) {
        Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
        return list;
    }
    Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
    return list;
}

    public ArrayList<ShareOfShelfGetterSetter> getmappingShareOfShelfData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT CATEGORY_CD,CATEGORY_PHOTO, CATEGORY FROM CATEGORY_MASTER", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();

                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                  //  sb.setCategory_camera(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_PHOTO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }
    public ArrayList<ShareOfShelfGetterSetter> getShareOfShelfDataFromDatabase(String store_cd, String categord_cd) {
        Log.d("FetchingOpening", " Stock data--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SD.FACING, " +
                    " SM.BRAND_CD, SM.BRAND, " +
                    "FROM openingHeader_data CD " +
                    "INNER JOIN STOCK_DATA SD " +
                    "ON CD.KEY_ID=SD.Common_Id " +
                    "INNER JOIN SKU_MASTER SM ON SD.SKU_CD=SM.SKU_CD " +
                    "WHERE CD.STORE_CD= '" + store_cd + "' AND CD.CATEGORY_CD = '" + categord_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("FetchingOPening", " midday---------------------->Stop<-----------");

        return list;
    }

    public ArrayList<ShareOfShelfGetterSetter> getShareofShelfBrandData(String categord_cd,String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;

        try {


            dbcursor = db.rawQuery("SELECT BR.BRAND_CD, BR.BRAND AS BRAND, IFNULL(D.FACING,'') " +
                    "AS FACING FROM BRAND_MASTER BR " +
                    "INNER JOIN MAPPING_SOS M ON BR.BRAND_CD = M.BRAND_CD " +
                    "INNER JOIN SUB_CATEGORY_MASTER SB " +
                    " ON BR.SUB_CATEGORY_CD = SB.SUB_CATEGORY_CD AND BR.SUB_CATEGORY_CD='" +categord_cd+ "' LEFT JOIN (SELECT C.STORE_CD, C.SUB_CATEGORY_CD, B.BRAND_CD, B.FACING" +
                    " FROM DR_SHARE_OF_SHELF_FACING_DATA B INNER JOIN DR_CATEGORY_SHARE_OF_SHELF_IMAGE C ON " +
                    " C.KEY_ID = B.COMMON_ID  WHERE C.STORE_CD ='"+store_cd+ "'AND C.SUB_CATEGORY_CD ='" +categord_cd+ "') " +
                    "AS D ON BR.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD AND BR.COMPANY_CD='1'" , null);

        /*    SELECT BR.BRAND_CD, BR.BRAND AS BRAND, IFNULL(D.FACING,'')
            AS FACING FROM BRAND_MASTER BR
            INNER JOIN MAPPING_SOS M ON BR.BRAND_CD = M.BRAND_CD
            INNER JOIN SUB_CATEGORY_MASTER SB ON BR.SUB_CATEGORY_CD = SB.SUB_CATEGORY_CD AND BR.SUB_CATEGORY_CD='1'
            LEFT JOIN (SELECT C.STORE_CD, C.SUB_CATEGORY_CD, B.BRAND_CD, B.FACING FROM DR_SHARE_OF_SHELF_FACING_DATA B
            INNER JOIN DR_CATEGORY_SHARE_OF_SHELF_IMAGE C ON  C.KEY_ID = B.COMMON_ID  WHERE C.STORE_CD ='3'AND C.SUB_CATEGORY_CD ='1') AS D ON BR.SUB_CATEGORY_CD = D.SUB_CATEGORY_CD AND BR.COMPANY_CD='1'

*/


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")));
                   // sb.setEd_openingFacing("");
                  //  sb.setFacing("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }

        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<BrandGetterSetter> getBrandMaster() {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<BrandGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("Select * from BRAND_MASTER ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandGetterSetter sb = new BrandGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    public ArrayList<StockNewGetterSetter> getSkuMiddayData(String brand_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT  SKU_CD, SKU FROM SKU_MASTER WHERE BRAND_CD ='"+brand_cd+"'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setEd_midFacing("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }




    public void InsertMidDayStockInlistData(String storeid,
                                           HashMap<StockNewGetterSetter, List<StockNewGetterSetter>> data, List<StockNewGetterSetter> save_listDataHeader,String spiner_value,String visit_date) {
        db.delete(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, CommonString.KEY_STORE_CD + "='" +storeid + "'", null);
        db.delete(CommonString.TABLE_MID_DAY_DATA, CommonString.KEY_STORE_CD + "='" +storeid + "'", null);
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();


        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                if (spiner_value.equals("1")){
                    values.put("STORE_CD", storeid);
                    values.put("VISIT_DATE", visit_date);
                    values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                    values.put("BRAND", save_listDataHeader.get(i).getBrand());
                    long l = db.insert(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, null, values);
                    for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                        values1.put("Common_Id", (int) l);
                        values1.put("STORE_CD", storeid);
                        values.put("VISIT_DATE", visit_date);
                        values1.put("COMPANY_CD", "1");
                        values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                        values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                        values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                        values1.put("SKU_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()));
                        if (!data.get(save_listDataHeader.get(i)).get(j).getEd_midFacing().equals("")){
                            values1.put("MIDDAY_STOCK", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getEd_midFacing()));
                        }
                        else {
                            values1.put("MIDDAY_STOCK", "0");
                        }
                        db.insert(CommonString.TABLE_MID_DAY_DATA, null, values1);
                    }
                }else {
                    values.put("STORE_CD", storeid);
                    values.put("VISIT_DATE", visit_date);
                    values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                    values.put("BRAND", save_listDataHeader.get(i).getBrand());
                    long l = db.insert(CommonString.TABLE_INSERT_MID_DAY_HEADER_DATA, null, values);
                    for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                        values1.put("Common_Id", (int) l);
                        values1.put("STORE_CD", storeid);
                        values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_cd());
                        values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                        values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                        values1.put("SKU_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getSku_cd()));
                        values1.put("MIDDAY_STOCK", "0");
                        values1.put("COMPANY_CD", "1");
                        db.insert(CommonString.TABLE_MID_DAY_DATA, null, values1);
                    }
                }

            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public void InsertHeaderCategorylistData(
            String storeid, String visit_date, List<ShareOfShelfGetterSetter> save_listDataHeader) {
        db.delete(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, CommonString.KEY_STORE_CD + "='" +storeid + "'", null);

        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("VISIT_DATE", visit_date);
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                values.put("CAT_FACING", save_listDataHeader.get(i).getCat_facing());
                values.put("IMAGE_CAT_FACING", save_listDataHeader.get(i).getImg_cat_facing());
                long l = db.insert(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
    }

    public void InsertCategoryShareofSelflistData(String storeid,String visit_date,
                                           HashMap<ShareOfShelfGetterSetter, List<ShareOfShelfGetterSetter>> data, List<ShareOfShelfGetterSetter> save_listDataHeader) {

        db.delete(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, CommonString.KEY_STORE_CD + "='" +storeid + "'", null);
        db.delete(CommonString.TABLE_SHARE_OF_SHELF_FACING_DATA, CommonString.KEY_STORE_CD + "='" +storeid + "'", null);
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();


        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("VISIT_DATE", visit_date);
              //  values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("SUB_CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                values.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                values.put("CAT_FACING", save_listDataHeader.get(i).getCat_facing());
                values.put("IMAGE_CAT_FACING", save_listDataHeader.get(i).getImg_cat_facing());
                long l = db.insert(CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE, null, values);

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("CATEGORY_CD", save_listDataHeader.get(i).getCategory_cd());
                    values1.put("CATEGORY", save_listDataHeader.get(i).getCategory());
                    values1.put("BRAND_CD",data.get(save_listDataHeader.get(i)).get(j).getBrand_cd());
                    values1.put("BRAND", data.get(save_listDataHeader.get(i)).get(j).getBrand());
                    values1.put("FACING", data.get(save_listDataHeader.get(i)).get(j).getFacing());

                    db.insert(CommonString.TABLE_SHARE_OF_SHELF_FACING_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }


    public boolean isShareOfShelfDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  FACING " + "FROM DR_SHARE_OF_SHELF_FACING_DATA WHERE STORE_CD= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")) == null || dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return filled;
        }

        return filled;
    }

    public ArrayList<StockNewGetterSetter> getStockInUploadFromDatabase(String store_cd) {
        Log.d("Fetching", "Mid Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SD.SKU_CD, SD.SKU,SD.BRAND_CD,SD.BRAND,SD.MIDDAY_STOCK " +
                    "FROM MID_DAY_DATA SD " +
                    "WHERE STORE_CD ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records Mid Stock!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", "Mid Stock---------------------->Stop<-----------");
        return list;
    }


    public StoreStockinGetterSetter getStockInSpinneruPLOADData(String store_id) {
        Log.d("FetchinggetCityMasterData--------------->Start<------------",
                "------------------");
        StoreStockinGetterSetter sb = new StoreStockinGetterSetter();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_SHARE_OF_SHELF_DATA+" WHERE "+CommonString.KEY_STORE_ID+" = "+store_id, null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                 //   sb.setSelect_brand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_BRAND")));
                  String select=dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_BRAND"));
                    if (select.equalsIgnoreCase("1")) {
                        sb.setSelect_brand("1");
                    } else {
                        sb.setSelect_brand("0");
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching CITY MASTER!!!!!!!!!!!", e.toString());
            return sb;
        }
        Log.d("Fetching non working data---------------------->Stop<-----------", "-------------------");
        return sb;
    }


    public ArrayList<ShareOfShelfGetterSetter> getHeaderShareOfSelfImgUploadData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT CA.CATEGORY_CD, CA.CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'') AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')" +
                    "  AS CAT_FACING FROM CATEGORY_MASTER CA  LEFT JOIN " +
                    "(SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='"+store_cd+ "')"+ " AS D ON CA.CATEGORY_CD = D.CATEGORY_CD" , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setCat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_FACING")));
                    sb.setImg_cat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_FACING")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Key_Id")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<ShareOfShelfGetterSetter> getHeaderShareOfSelfImgUploadData1(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE+" WHERE "+CommonString.KEY_STORE_CD+" = "+store_cd, null);

           /* dbcursor = db.rawQuery("SELECT CA.CATEGORY_CD, CA.CATEGORY, IFNULL(D.IMAGE_CAT_FACING,'') AS IMAGE_CAT_FACING, IFNULL(D.CAT_FACING,'')" +
                    "  AS CAT_FACING FROM CATEGORY_MASTER CA  LEFT JOIN " +
                    "(SELECT * FROM DR_CATEGORY_SHARE_OF_SHELF_IMAGE WHERE STORE_CD='"+store_cd+ "')"+ " AS D ON CA.CATEGORY_CD = D.CATEGORY_CD" , null);*/

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                 //   sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_CD")));
                    sb.setCategory_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_CD")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    sb.setCat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_FACING")));
                    sb.setImg_cat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_FACING")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Key_Id")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<ShareOfShelfGetterSetter> getShareofShelfBrandDataUpload(String common_id, String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM DR_SHARE_OF_SHELF_FACING_DATA WHERE STORE_CD = '"+ store_cd +"' AND COMMON_ID = '"+ common_id +"'" , null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }

        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<ShareOfShelfGetterSetter> getShareofSelfCheckoutData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ShareOfShelfGetterSetter> list = new ArrayList<ShareOfShelfGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE+" WHERE "+CommonString.KEY_STORE_CD+" = "+store_cd, null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ShareOfShelfGetterSetter sb = new ShareOfShelfGetterSetter();
                    sb.setCat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAT_FACING")));
                    sb.setImg_cat_facing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_CAT_FACING")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<StockNewGetterSetter> getMiddayDataFromCheckoutDatabase(String store_cd) {
        Log.d("Fetching", "Mid Stock data--------------->Start<------------");
        ArrayList<StockNewGetterSetter> list = new ArrayList<StockNewGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_MID_DAY_DATA+" WHERE "+CommonString.KEY_STORE_CD+" = "+store_cd, null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockNewGetterSetter sb = new StockNewGetterSetter();

                    sb.setSku_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setEd_midFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MIDDAY_STOCK")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records Mid Stock!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching", "Mid Stock---------------------->Stop<-----------");
        return list;
    }
    public ArrayList<JourneyPlanGetterSetter> getJCPDataStore_id(String store_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<JourneyPlanGetterSetter> list = new ArrayList<JourneyPlanGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from JOURNEY_PLAN " +
                    "where STORE_CD = '" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    JourneyPlanGetterSetter sb = new JourneyPlanGetterSetter();

                    sb.setStore_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_CD")));
                    sb.setKey_account(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT")));
                    sb.setStore_name((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORENAME"))));
                    sb.setCity((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CITY"))));
                    sb.setUploadStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("UPLOAD_STATUS"))));
                    sb.setCheckOutStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKOUT_STATUS"))));
                    sb.setVISIT_DATE((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIT_DATE"))));
                    sb.setGeotagStatus((dbcursor.getString(dbcursor.getColumnIndexOrThrow("GEO_TAG"))));
                    sb.setLatitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LATTITUDE"))));
                    sb.setLongitude((dbcursor.getString(dbcursor.getColumnIndexOrThrow("LONGITUDE"))));
                    sb.setKeyaccount_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT_CD"))));
                    sb.setCity_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("CITY_CD"))));
                    sb.setStoretype_cd((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORETYPE_CD"))));
                    sb.setInstock_allow((dbcursor.getString(dbcursor.getColumnIndexOrThrow("INSTOCK_ALLOW"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching JCP!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching", "JCP data---------------------->Stop<-----------");
        return list;
    }


}
