package com.cpm.Constants;

import android.os.Environment;

public class CommonString {

    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Please Check Your Network Connection";
    public static final String MESSAGE_INTERNET_NOT_AVALABLE = "No Internet Connection.Please Check Your Network Connection";
    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_INVALID_XML = "Problem Occured while parsing XML : invalid data";

  //  public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/.LorealPromoter_Images/";
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/.LorealPromoter_Image/";
    public static final String BACKUP_PATH = Environment.getExternalStorageDirectory() + "/Lorealpromoter_backup/";

    public static final String PLS_FILL_DATA = "Please fill the data";
    public static final String CALLS_INVALID_DATA = "Productive Call cannot be greater than Total Calls";
    public static final String ONBACK_ALERT_MESSAGE = "Unsaved data will be lost - Do you want to continue?";
    public static final String DATA_DELETE_ALERT_MESSAGE = "Saved data will be lost - Do you want to continue?";
    // preferenec keys
    public static final String KEY_QUESTION_CD = "question_cd";
    public static final String KEY_ANSWER_CD = "answer_cd";
    public static final String KEY_IS_QUIZ_DONE = "is_quiz_done";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_SELFIE_IMAGE = "SELFIE_IMAGE";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER = "remember";
    public static final String KEY_PATH = "path";
    public static final String KEY_VERSION = "version";
    public static final String METHOD_UPLOAD_XML = "DrUploadXml";
    public static final String MEHTOD_UPLOAD_COVERAGE_STATUS = "UploadCoverage_Status";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_TYPE = "RIGHTNAME";
    public static final String KEY_DATE = "date";
    public static final String KEY_P = "P";
    public static final String KEY_D = "D";
    public static final String KEY_U = "U";
    public static final String KEY_C = "Y";
    public static final String KEY_CHECK_IN = "I";
    public static final String KEY_INVALID = "INVALID";
    public static final String STORE_STATUS_LEAVE = "L";
    public static final String KEY_VALID = "Valid";
    public static final String KEY_STORE_IN_TIME = "Store_in_time";
    public static final String SOAP_ACTION = "http://tempuri.org/";
    public static final String KEY_MERCHANDISER_ID = "MERCHANDISER_ID";
  //  public static final String ERROR = " PROBLEM OCCURED IN ";
    public static final String ERROR = " ";
    public static final String KEY_SUCCESS_chkout = "Success";
    public static final String KEY_notice_board = "notice_board";
    public static final String KEY_quiz_url = "quiz_url";



    // webservice constants
    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_FAILURE = "Failure";
    public static final String KEY_FALSE = "False";
    public static final String KEY_FAIL = "Fail";
    public static final String KEY_CHANGED = "Changed";
    public static final String KEY_NO_DATA = "NoData";
    public static final String KEY_IMAGE = "IMAGE1";
    public static final String KEY_IMAGE2 = "IMAGE2";
    public static final String KEY_IMAGE3 = "IMAGE3";
    public static final String KEY_COVERAGE_REMARK = "REMARK";

    public static final String METHOD_UPLOAD_IMAGE = "GetImageWithFolderName";

    public static final String TABLE_VISITOR_LOGIN = "TABLE_VISITOR_LOGIN";

    public static final String SOAP_ACTION_UPLOAD_IMAGE = "http://tempuri.org/" + METHOD_UPLOAD_IMAGE;
    public static final String TABLE_INSERT_OPENINGHEADER_CLOSING_DATA = "openingHeader_Closing_data";
    public static final String METHOD_Checkout_StatusNew = "Upload_Store_ChecOut_Status_V1";
    public static final String CREATE_TABLE_insert_OPENINGHEADER_CLOSING_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_OPENINGHEADER_CLOSING_DATA
                    + "("
                    + "KEY_ID"
                    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

                    + "STORE_CD"
                    + " VARCHAR,"

                    + "BRAND_CD"
                    + " VARCHAR,"

                    + "BRAND"
                    + " VARCHAR" +
                    ")";


    public static final String TABLE_INSERT_ASSET_HEADER_DATA = "openingHeader_Asset_data";

    public static final String CREATE_TABLE_insert_HEADER_ASSET_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_ASSET_HEADER_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " VARCHAR,"

            + "CATEGORY_CD"
            + " VARCHAR,"

            + "CATEGORY"
            + " VARCHAR" +
            ")";

    public static final String TABLE_INSERT_HEADER_MIDDAY_DATA = "openingHeader_Midday_data";

    public static final String CREATE_TABLE_insert_HEADER_MIDDAY_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_HEADER_MIDDAY_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " VARCHAR,"

            + "BRAND_CD"
            + " VARCHAR,"

            + "BRAND"
            + " VARCHAR" +
            ")";

    public static final String TABLE_INSERT_HEADER_FOOD_STORE_DATA = "openingHeader_FOOD_STORE_data";

    public static final String CREATE_TABLE_insert_HEADER_FOOD_STORE_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_HEADER_FOOD_STORE_DATA
                    + "("
                    + "KEY_ID"
                    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

                    + "STORE_CD"
                    + " VARCHAR,"

                    + "BRAND_CD"
                    + " VARCHAR,"

                    + "BRAND"
                    + " VARCHAR" +
                    ")";

    public static final String TABLE_DEEPFREEZER_DATA = "DEEPFREEZER_DATA";
    public static final String CREATE_TABLE_DEEPFREEZER_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DEEPFREEZER_DATA +
                    "(" +
                    "FID" +
                    " INTEGER, " +

                    "STORE_CD" +
                    " VARCHAR," +

                    "DEEP_FREEZER" +
                    " VARCHAR, " +

                    "FREEZER_TYPE" +
                    " VARCHAR, " +

                    "STATUS" +
                    " VARCHAR, " +

                    "REMARK" +
                    " VARCHAR" +
                    ")";

    public static final String TABLE_FACING_COMPETITOR_DATA = "FACING_COMPETITOR_DATA";
    public static final String CREATE_TABLE_FACING_COMPETITOR_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FACING_COMPETITOR_DATA +
                    "(" +
                    "KEY_ID" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "FACING" +
                    " INTEGER " +
                    ")";


    public static final String TABLE_OPENING_STOCK_DATA = "OPENING_STOCK_DATA";
    public static final String CREATE_TABLE_OPENING_STOCK_DATA = "CREATE TABLE IF NOT EXISTS OPENING_STOCK_DATA(Common_Id INTEGER, SKU_CD INTEGER,STORE_CD VARCHAR, CATEGORY_TYPE VARCHAR, AS_PER_MCCAIN VARCHAR, ACTUAL_LISTED VARCHAR, OPENING_STOCK_COLD_ROOM VARCHAR, OPENING_STOCK_MCCAIN_DF VARCHAR, TOTAL_FACING_MCCAIN_DF VARCHAR, OPENING_STOCK_STORE_DF VARCHAR, TOTAL_FACING_STORE_DF VARCHAR, MATERIAL_WELLNESS VARCHAR)";

    public static final String TABLE_CLOSING_STOCK_DATA = "CLOSING_STOCK_DATA";
    public static final String CREATE_TABLE_CLOSING_STOCK_DATA = "CREATE TABLE IF NOT EXISTS CLOSING_STOCK_DATA(Common_Id INTEGER, SKU_CD INTEGER,STORE_CD VARCHAR, COLD_ROOM VARCHAR, MCCAIN_DF VARCHAR, STORE_DF VARCHAR)";

    public static final String TABLE_MIDDAY_STOCK_DATA = "MIDDAY_STOCK_DATA";
    public static final String CREATE_TABLE_MIDDAY_STOCK_DATA = "CREATE TABLE IF NOT EXISTS MIDDAY_STOCK_DATA(Common_Id INTEGER,SKU_CD INTEGER,STORE_CD VARCHAR, MIDDAY_STOCK VARCHAR)";

    public static final String TABLE_FOOD_STORE_DATA = "FOOD_STORE_DATA";
    public static final String CREATE_TABLE_FOOD_STORE_DATA = "CREATE TABLE IF NOT EXISTS FOOD_STORE_DATA(Common_Id INTEGER,STORE_CD VARCHAR, SKU_CD VARCHAR, SKU VARCHAR,AS_PER_MCCAIN VARCHAR,ACTUAL_LISTED VARCHAR, MCCAIN_DF VARCHAR, STORE_DF VARCHAR, MTD_SALES VARCHAR, PACKING_SIZE VARCHAR)";


    public static final String TABLE_ASSET_DATA = "ASSET_DATA";
    public static final String CREATE_TABLE_ASSET_DATA = "CREATE TABLE IF NOT EXISTS ASSET_DATA" +
            "(" +
            KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Common_Id INTEGER," +
            "ASSET_CD INTEGER," +
            "STORE_CD VARCHAR, " +
            "ASSET VARCHAR, " +
            "PRESENT VARCHAR, " +
            "REASON VARCHAR," +
            "REASON_CD INTEGER," +
            "PLANOGRAM_IMG VARCHAR," +
            "IMAGE VARCHAR" +
            ")";

    public static final String TABLE_CALLS_DATA = "CALLS_DATA";
    public static final String CREATE_TABLE_CALLS_DATA = "CREATE TABLE IF NOT EXISTS CALLS_DATA(Key_Id INTEGER PRIMARY KEY AUTOINCREMENT, STORE_CD VARCHAR, TOTAL_CALLS VARCHAR, PRODUCTIVE_CALLS VARCHAR)";

    public static final String TABLE_COMPETITION_POI = "COMPETITION_POI";
    public static final String CREATE_TABLE_COMPETITION_POI = "CREATE TABLE IF NOT EXISTS COMPETITION_POI(Key_Id INTEGER PRIMARY KEY AUTOINCREMENT, STORE_CD VARCHAR, CATEGORY_CD VARCHAR, ASSET_CD VARCHAR, CATEGORY VARCHAR, ASSET VARCHAR, BRAND_CD VARCHAR, BRAND VARCHAR, REMARK VARCHAR)";

    public static final String TABLE_POI = "POI";
    public static final String CREATE_TABLE_POI = "CREATE TABLE IF NOT EXISTS POI(Key_Id INTEGER PRIMARY KEY AUTOINCREMENT, STORE_CD VARCHAR, CATEGORY_CD VARCHAR, ASSET_CD VARCHAR, CATEGORY VARCHAR, ASSET VARCHAR, BRAND_CD VARCHAR, BRAND VARCHAR, REMARK VARCHAR, IMAGE_POI VARCHAR)";

    public static final String TABLE_COMPETITION_PROMOTION = "COMPETITION_PROMOTION";
    public static final String CREATE_TABLE_COMPETITION_PROMOTION = "CREATE TABLE IF NOT EXISTS COMPETITION_PROMOTION(Key_Id INTEGER PRIMARY KEY AUTOINCREMENT, STORE_CD VARCHAR, CATEGORY_CD VARCHAR, CATEGORY VARCHAR, BRAND_CD VARCHAR, BRAND VARCHAR, REMARK VARCHAR, PROMOTION VARCHAR)";


    // location


    public static final String NAMESPACE = "http://tempuri.org/";

   // public static final String URL = "http://di.parinaam.in/Danonewebservice.asmx";
    public static final String URL = "http://lipromo.parinaam.in/LoralMerchandising.asmx";

    public static String URL2 = "http://lipromo.parinaam.in/LoralMerchandising.asmx";
    //http://lipromo.parinaam.in/LoralMerchandising.asmx/Uploadimages
   /* http://lipromo.parinaam.in/LoralMerchandising.asmx*/
    public static final String URLFORRETROFIT = "http://lipromo.parinaam.in/LoralMerchandising.asmx";
    public static final String URLBACKUPUPLOADRETROFIT = "http://lipromo.parinaam.in/LoralMerchandising.asmx";

    public static final String URL_Notice_Board = "http://lipromo.parinaam.in/notice/notice.html";


  //  public static final String METHOD_LOGIN = "UserLoginDetail";
    public static final String METHOD_LOGIN = "UserLoginDetail_New";
    public static final String SOAP_ACTION_LOGIN = "http://tempuri.org/" + METHOD_LOGIN;
    // Upload Coverage
    public static final String METHOD_UPLOAD_DR_STORE_COVERAGE = "UPLOAD_COVERAGE";
    public static final String TABLE_COVERAGE_DATA = "COVERAGE_DATA";

    public static final String TABLE_PJP_DEVIATION = "PJP_DEVIATION";


    // FOR JCP DOWNLOAD


    public static final String KEY_STORE_ID = "STORE_ID";
    public static final String KEY_STORE_NAME = "STORE_NAME";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_IN_TIME = "IN_TIME";
    public static final String KEY_OUT_TIME = "OUT_TIME";
    public static final String KEY_VISIT_DATE = "VISIT_DATE";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_COVERAGE_STATUS = "Coverage";
    public static final String KEY_REASON_ID = "REASON_ID";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_CHECKOUT_STATUS = "CHECKOUT_STATUS";

    public static final String KEY_KEYACCOUNT_CD = "KEYACCOUNT_CD";
    public static final String KEY_CITY_CD = "CITY_CD";
    public static final String KEY_STORETYPE_CD = "STORETYPE_CD";

    public static final String KEY_STORE_CD = "STORE_CD";
    public static final String KEY_CHANNEL_CD = "CHANNEL_CD";
    public static final String KEY_FLOOR_STATUS = "FLOOR_STATUS";
    public static final String KEY_BACKROOK_STATUS = "BACKROOK_STATUS";
    public static final String KEY_STOREVISITED = "STORE_VISITED";
    public static final String KEY_STOREVISITED_STATUS = "STORE_VISITED_STATUS";
    public static final String KEY_FOOD_STORE = "FOOD_STORE";

    public static final String KEY_CATEGORY_ID = "CATEGORY_ID";
    public static final String REASON_ID = "REASON_ID";
    //For Asset Checklist Insert table
    public static final String CHECK_LIST_ID = "CHECK_LIST_ID";
    public static final String CHECK_LIST = "CHECK_LIST";
    public static final String CHECK_LIST_TEXT = "CHECK_LIST_TEXT";
    public static final String CHECK_LIST_TYPE = "CHECK_LIST_TYPE";
    public static final String ASSET_CD = "ASSET_CD";

    public static final String KEY_PJP_DEVIATION = "PJP_DEVIATION";


    public static final String COMMONID = "COMMONID";


    public static final String METHOD_NAME_UNIVERSAL_DOWNLOAD = "Download_Universal";
    public static final String SOAP_ACTION_UNIVERSAL = "http://tempuri.org/"
            + METHOD_NAME_UNIVERSAL_DOWNLOAD;


    public static final String CREATE_TABLE_COVERAGE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_COVERAGE_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER,USER_ID VARCHAR, " + KEY_IN_TIME + " VARCHAR,"
            + KEY_OUT_TIME + " VARCHAR," + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_LATITUDE + " VARCHAR," + KEY_LONGITUDE + " VARCHAR,"
            + KEY_MERCHANDISER_ID + " INTEGER,"
            + KEY_COVERAGE_STATUS + " VARCHAR," + KEY_IMAGE + " VARCHAR,"
            + KEY_IMAGE2 + " VARCHAR,"
            + KEY_IMAGE3 + " VARCHAR,"
            + KEY_REASON_ID + " INTEGER,"
            + KEY_PJP_DEVIATION + " VARCHAR,"
            + KEY_COVERAGE_REMARK
            + " VARCHAR,"
            + KEY_REASON + " VARCHAR" +
            ")";


///attendence data


    public static final String CREATE_TABLE_PJP_DEVIATION = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_PJP_DEVIATION + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_CD + " VARCHAR, "
            + KEY_VISIT_DATE + " VARCHAR)";


    // planogram
    public static final String TABLE_ASSET_CHECKLIST_INSERT = "ASSET_CHECKLIST_INSERT";

    public static final String CREATE_TABLE_ASSET_CHECKLIST_INSERT = "CREATE TABLE "
            + TABLE_ASSET_CHECKLIST_INSERT
            + " ("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + CHECK_LIST_ID
            + " VARCHAR,"
            + CHECK_LIST
            + " VARCHAR,"
            + CHECK_LIST_TEXT
            + " VARCHAR,"
            + CHECK_LIST_TYPE
            + " VARCHAR,"
            + KEY_STORE_CD
            + " VARCHAR,"
            + ASSET_CD
            + " VARCHAR,"

            + "CATEGORY_CD"
            + " VARCHAR,"

            + KEY_VISIT_DATE
            + " VARCHAR)";


    //New Changes 12-04-2017

    public static final String TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA = "openingHeaderBackOffice_data";

    public static final String TABLE_INSERT_OPENINGHEADER_DATA = "openingHeader_data";
    public static final String CREATE_TABLE_insert_OPENINGHEADER_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_OPENINGHEADER_DATA
                    + " ("
                    + "KEY_ID" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT,"

                    + "STORE_CD" +
                    " VARCHAR," +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "CATEGORY_CD" +
                    " VARCHAR,"

                    + "CATEGORY" +
                    " VARCHAR"
                    + ")";

    public static final String CREATE_TABLE_insert_OPENINGHEADER_BACKOFFICE_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_OPENINGHEADER_BACKOFFICE_DATA
                    + " ("
                    + "KEY_ID" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT,"

                    + "STORE_CD" +
                    " VARCHAR,"

                    + "CATEGORY_CD" +
                    " VARCHAR,"

                    + "CATEGORY" +
                    " VARCHAR"
                    + ")";



    public static final String TABLE_STOCK_BACKOFFICE_DATA = "STOCK_BACKOFFICE_DATA";

    public static final String TABLE_STOCK_DATA = "STOCK_DATA";
    public static final String CREATE_TABLE_STOCK_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STOCK_DATA +
                    "(" +
                    "Common_Id" +
                    " INTEGER, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "SKU_CD" +
                    " INTEGER," +

                    "SKU" +
                    " VARCHAR, " +

                    "STOCK_UNDER_DAYS" +
                    " VARCHAR, " +


                  /*  //remove
                    "OPENING_STOCK" +
                    " INTEGER, " +*/

                    "OPENING_FACING" +
                    " INTEGER, " +

                    "MIDDAY_STOCK" +
                    " INTEGER, " +

                    "DATE_1" +
                    " VARCHAR, " +
                    "DATE_2" +
                    " VARCHAR, " +
                    "DATE_3" +
                    " VARCHAR, " +
                    "STOCK_1" +
                    " INTEGER, " +
                    "STOCK_2" +
                    " INTEGER, " +
                    "STOCK_3" +
                    " INTEGER, " +

                    "COMPANY_CD" +
                    " INTEGER, " +

                    "CLOSING_STOCK" +
                    " INTEGER" +
                    ")";


    public static final String CREATE_TABLE_STOCK_BACKOFFICE_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STOCK_BACKOFFICE_DATA +
                    "(" +
                    "Common_Id" +
                    " INTEGER, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "SKU_CD" +
                    " INTEGER," +

                    "SKU" +
                    " VARCHAR, " +

                    "STOCK_UNDER_DAYS" +
                    " VARCHAR, " +


                    "OPENING_FACING" +
                    " INTEGER, " +

                    "MIDDAY_STOCK" +
                    " INTEGER, " +

                    "DATE_1" +
                    " VARCHAR, " +
                    "DATE_2" +
                    " VARCHAR, " +
                    "DATE_3" +
                    " VARCHAR, " +
                    "STOCK_1" +
                    " INTEGER, " +
                    "STOCK_2" +
                    " INTEGER, " +
                    "STOCK_3" +
                    " INTEGER, " +

                    "COMPANY_CD" +
                    " INTEGER, " +

                    "CLOSING_STOCK" +
                    " INTEGER" +
                    ")";


    public static final String TABLE_STOCK_IMAGE = "STOCK_IMAGE";

    public static final String TABLE_STOCK_BACKOFFICE_IMAGE = "STOCK_BACKOFFICE_IMAGE";

    public static final String CREATE_TABLE_STOCK_IMAGE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STOCK_IMAGE +
                    "(" +
                    "Key_Id" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "HIMALAYA_PHOTO" +
                    " INTEGER, " +

                    "CATEGORY_PHOTO" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "IMAGE_STK" +
                    " VARCHAR, " +

                    "IMAGE_CAT_ONE" +
                    " VARCHAR, " +

                    "IMAGE_CAT_TWO" +
                    " VARCHAR, " +

                    "CAT_STOCK" +
                    " INTEGER, " +

                    "VISIT_DATE" +
                    " VARCHAR" +
                    ")";


    public static final String CREATE_TABLE_STOCK_BACKOFFICE_IMAGE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STOCK_BACKOFFICE_IMAGE +
                    "(" +
                    "Key_Id" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "HIMALAYA_PHOTO" +
                    " INTEGER, " +

                    "CATEGORY_PHOTO" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "IMAGE_STK" +
                    " VARCHAR, " +

                    "IMAGE_CAT_ONE" +
                    " VARCHAR, " +

                    "IMAGE_CAT_TWO" +
                    " VARCHAR, " +

                    "VISIT_DATE" +
                    " VARCHAR" +
                    ")";


    public static final String TABLE_INSERT_PROMOTION_HEADER_DATA = "openingHeader_Promotion_data";
    public static final String CREATE_TABLE_insert_HEADER_PROMOTION_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_PROMOTION_HEADER_DATA
            + "("
            + " KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " VARCHAR,"

            + "BRAND_CD"
            + " VARCHAR,"

            + "BRAND"
            + " VARCHAR" +
            ")";

    public static final String TABLE_PROMOTION_DATA = "PROMOTION_DATA";
    public static final String CREATE_TABLE_PROMOTION_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PROMOTION_DATA
            + "("
            + "Common_Id"
            + " INTEGER,"
            + "PID"
            + " INTEGER,"
            + "STORE_CD"
            + " INTEGER,"
            + "PROMOTION"
            + " VARCHAR,"
            + "PRESENT_SPIN"
            + " VARCHAR,"
            + "REMARK"
            + " VARCHAR,"
            + "REMARK_CD"
            + " INTEGER,"

            + "PROMO_STOCK"
            + " INTEGER,"
            + "RUNNING_POS"
            + " INTEGER,"
            + "BRAND_CD"
            + " VARCHAR,"
            + "BRAND"
            + " VARCHAR,"
            + "CAMERA"
            + " VARCHAR)";

    public static final String TABLE_ASSET_SKU_CHECKLIST_INSERT = "Paid_Visibility_SkuDailog";

    //Checklist Paid Visibility
    public static final String TABLE_ASSET_CHECKLIST_DATA = "ASSET_CHECKLIST_DATA";

    public static final String CREATE_ASSET_CHECKLIST_DATA = "CREATE TABLE "
            + TABLE_ASSET_CHECKLIST_DATA
            + " ("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + CHECK_LIST_ID
            + " VARCHAR,"
            + CHECK_LIST
            + " VARCHAR,"
            + CHECK_LIST_TEXT
            + " VARCHAR,"
            + CHECK_LIST_TYPE
            + " VARCHAR,"

            + COMMONID
            + " VARCHAR,"

            + REASON_ID
            + " INTEGER)";


    public static final String TABLE_AUDIT_DATA_SAVE = "Audit_Data_Save";
    public static final String CREATE_TABLE_AUDIT_DATA_SAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AUDIT_DATA_SAVE
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " INTEGER,"

            + "QUESTION_ID"
            + " INTEGER,"

            + "QUESTION"
            + " VARCHAR,"

            + "ANSWER_ID"
            + " INTEGER,"

            + "CATEGORY_ID"
            + " INTEGER" + ")";

    public static final String KEY_POSITION = "POSITION";
    public static final String TABLE_INSERT_MARKET_INTELLI_DATA = "MARKET_INTELLIGENCE";
    public static final String CREATE_TABLE_INSERT_MARKET_INTELLIGENCE_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_MARKET_INTELLI_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "CHECKBOX"
            + " boolean,"
            + "COMPANY_CD"
            + " INTEGER,"
            + "COMPANY"
            + " VARCHAR,"

            + "CATEGORY_CD"
            + " INTEGER,"
            + "CATEGORY"
            + " VARCHAR,"
            + "PROMOTYPE_CD"
            + " INTEGER,"
            + "PROMOTYPE"
            + " VARCHAR,"

            + "PHOTO"
            + " VARCHAR,"
            + "REMARK"
            + " VARCHAR,"

            + "VISIT_DATE"
            + " VARCHAR,"
            + "USER_ID"
            + " VARCHAR,"

            + "STORE_CD"
            + " INTEGER)";

    public static final String TABLE_INSERT_SAMPLED_DATA = "SAMPLED_DATA";

    public static final String CREATE_TABLE_INSERT_SAMPLED_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_SAMPLED_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "CATEGORY_CD"
            + " INTEGER,"
            + "CATEGORY"
            + " VARCHAR,"
            + "SKU_CD"
            + " INTEGER,"
            + "SKU"
            + " VARCHAR,"

            + "SAMPLED"
            + " VARCHAR,"

            + "PHOTO"
            + " VARCHAR,"
            + "FEEDBACK"
            + " VARCHAR,"

            + "VISIT_DATE"
            + " VARCHAR,"
            + "USER_ID"
            + " VARCHAR,"

            + "MOBILE"
            + " VARCHAR,"
            + "NAME"
            + " VARCHAR,"

            + "STORE_CD"
            + " INTEGER)";


    public static final String TABLE_STORE_GEOTAGGING = "STORE_GEOTAGGING";
    public static final String CREATE_TABLE_STORE_GEOTAGGING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STORE_GEOTAGGING
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_ID"
            + " VARCHAR,"

            + "LATITUDE"
            + " VARCHAR,"

            + "LONGITUDE"
            + " VARCHAR,"

            + "GEO_TAG"
            + " VARCHAR,"

            + "STATUS"
            + " VARCHAR,"

            + "FRONT_IMAGE" + " VARCHAR)";



    public static final String TABLE_SHARE_OF_SHELF_DATA = "STOCK_IN_SPINNER_DATA";
    public static final String TABLE_SHARE_OF_SHELF_POPUP_DATA = "STOCK_IN_POPUP_DATA";
    public static final String CREATE_TABLE_SHARE_OF_SHELF_DATA_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SHARE_OF_SHELF_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "STOCK_BRAND"
            + " INTEGER,"
            + "VISIT_DATE"
            + " VARCHAR,"
            + "STORE_ID"
            + " INTEGER)";

    public static final String CREATE_TABLE_SHARE_OF_SHELF_POPUP_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SHARE_OF_SHELF_POPUP_DATA
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "STOCK_BRAND"
            + " INTEGER,"
            + "VISIT_DATE"
            + " VARCHAR,"
            + "CURRENT_DATE"
            + " VARCHAR,"
            + "STORE_ID"
            + " INTEGER)";


    public static final String TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE = "DR_CATEGORY_SHARE_OF_SHELF_IMAGE";

    public static final String CREATE_TABLE_SHARE_OF_SHELF_IMAGE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_SHARE_OF_SHELF_IMAGE +
                    "(" +
                    "Key_Id" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "STORE_CD" +
                    " INTEGER, " +
                   /* "CATEGORY_CD" +
                    " INTEGER, " +*/
                    "CATEGORY_CD" +
                    " INTEGER, " +
                    "SUB_CATEGORY_CD" +
                    " INTEGER, " +
                    "CATEGORY" +
                    " VARCHAR, " +
                    "IMAGE_CAT_FACING" +
                    " VARCHAR, " +
                    "CAT_FACING" +
                    " INTEGER, " +
                    "VISIT_DATE" +
                    " VARCHAR" +
                    ")";

    public static final String TABLE_INSERT_MID_DAY_HEADER_DATA = "MID_DAY_HEADER_DATA";
    public static final String CREATE_TABLE_insert_mid_day_HEADER_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_MID_DAY_HEADER_DATA
                    + " ("
                    + "KEY_ID" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT,"

                    + "STORE_CD" +
                    " VARCHAR," +

                     "VISIT_DATE" +
                    " VARCHAR," +

                    "BRAND_CD" +
                    " INTEGER, " +

                     "BRAND" +
                    " VARCHAR"
                    + ")";


    public static final String TABLE_MID_DAY_DATA = "MID_DAY_DATA";
    public static final String CREATE_TABLE_MID_DAY_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MID_DAY_DATA +
                    "(" +
                    "Common_Id" +
                    " INTEGER, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "VISIT_DATE" +
                    " VARCHAR, " +

                    "SKU_CD" +
                    " INTEGER," +

                    "SKU" +
                    " VARCHAR, " +

                    "MIDDAY_STOCK" +
                    " INTEGER, " +

                    "COMPANY_CD" +
                    " INTEGER" +
                    ")";



    public static final String TABLE_INSERT_SHAREOF_SHELF_HEADER_DATA = "DR_shareofShelfHeader_data";
    public static final String CREATE_TABLE_insert_share_of_shelf_HEADER_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INSERT_SHAREOF_SHELF_HEADER_DATA
                    + " ("
                    + "KEY_ID" +
                    " INTEGER PRIMARY KEY AUTOINCREMENT,"

                    + "STORE_CD" +
                    " VARCHAR," +

                    "CATEGORY_CD" +
                    " VARCHAR,"

                    + "CATEGORY" +
                    " VARCHAR"
                    + ")";


    public static final String TABLE_SHARE_OF_SHELF_FACING_DATA = "DR_SHARE_OF_SHELF_FACING_DATA";
    public static final String CREATE_TABLE_SHARE_OF_SHELF_FACING_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SHARE_OF_SHELF_FACING_DATA +
                    "(" +
                    "Common_Id" +
                    " INTEGER, " +

                    "STORE_CD" +
                    " INTEGER, " +

                    "CATEGORY_CD" +
                    " INTEGER, " +

                    "CATEGORY" +
                    " VARCHAR, " +

                    "BRAND_CD" +
                    " INTEGER, " +

                    "BRAND" +
                    " VARCHAR, " +

                    "FACING" +
                    " INTEGER" +
                    ")";
    public static final int TIMEOUT = 20000;
    public static final String METHOD_NAME_IMAGE = "GetImageWithFolderName";
    public static final String KEY_EMP_CODE = "EMP_CODE";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_DESIGNATION = "DESIGNATION";
    public static final String KEY_FEEDBACK_IMAGE = "FEEDBACK_IMAGE";
    public static final String KEY_UPLOADSTATUS = "UPLOADSTATUS";
    public static final String KEY_IN_TIME_IMAGE = "IN_TIME_IMAGE";
    public static final String KEY_OUT_TIME_IMAGE = "OUT_TIME_IMAGE";
    public static final String KEY_EXIT = "EXIT";
    public static final String KEY_FEEDBACK = "FEEDBACK";
    public static final String KEY_FEEDBACK_CD = "FEEDBACK_CD";
    public static final String KEY_REMARK = "REMARK";
    public static final String KEY_COMMON_ID = "COMMON_ID";
    public static final String KEY_FQUESTION_ID = "FQUESTION_ID";
    public static final String KEY_FQUESTION = "FQUESTION";
    public static final String KEY_FEEDBACK_RATING = "FEEDBACK_RATING";
    public static final String KEY_VISITOR_NAME = "VISITOR_NAME";
    public static final String KEY_VISITOR_DESIGNATION = "VISITOR_DESIGNATION";

    public static final String CREATE_TABLE_VISITOR_LOGIN = "CREATE TABLE "
            + TABLE_VISITOR_LOGIN + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_USERNAME + " VARCHAR,"
            + KEY_EMP_CODE + " VARCHAR,"
            + KEY_NAME + " VARCHAR,"
            + KEY_DESIGNATION + " VARCHAR,"
            + KEY_UPLOADSTATUS + " VARCHAR,"
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_IN_TIME + " VARCHAR,"
            + KEY_OUT_TIME + " VARCHAR,"
            + KEY_IN_TIME_IMAGE + " VARCHAR,"
            + KEY_EXIT + " VARCHAR,"
            + KEY_OUT_TIME_IMAGE + " VARCHAR)";

    public static final String TABLE_INSERT_FEEDBACK = "FEEDBACK";
    public static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_FEEDBACK
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_COMMON_ID + " INTEGER,"
            + KEY_FEEDBACK_CD + " INTEGER,"
            + KEY_FEEDBACK + " VARCHAR,"
            + KEY_EXIT + " VARCHAR,"
            + KEY_REMARK + " VARCHAR,"
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_NAME + " VARCHAR,"
            + KEY_DESIGNATION + " VARCHAR,"
            + KEY_FEEDBACK_RATING + " INTEGER"
            + ")";

    ///feedback
    public static final String TABLE_FEEDBACK_DATA_SAVE = "FEEDBACK_Data_Save";
    public static final String TABLE_feedback_save = "FEEDBACK_save";
    public static final String CREATE_feedback_save = "CREATE TABLE IF NOT EXISTS "
            + TABLE_feedback_save
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_VISITOR_NAME+ " VARCHAR,"
            + KEY_VISITOR_DESIGNATION + " VARCHAR,"
            + KEY_STATUS+ " VARCHAR,"
            + KEY_USER_ID + " VARCHAR"
            + ")";

    public static final String CREATE_TABLE_FEEDBACK_DATA_SAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FEEDBACK_DATA_SAVE
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " INTEGER,"

            + "COMMONA_ID"
            + " INTEGER,"

            + "QUESTION_ID"
            + " INTEGER,"

            + "QUESTION"
            + " VARCHAR,"

            + "ANSWER_ID"
            + " INTEGER,"

            + "SUB_CATEGORY_ID"
            + " INTEGER,"

            + "VISITOR_NAME"
            + " VARCHAR,"

            + "VISITOR_DESIGNATION"
            + " VARCHAR,"

            + "USER_ID"
            + " VARCHAR,"

            + "FEEDBACK_IMAGE"
            + " VARCHAR,"

            + "CATEGORY_ID"
            + " INTEGER"

            + ")";

  public static final String stpcontactnolenght = "Please fill atleast 10 digit contact number";
}
