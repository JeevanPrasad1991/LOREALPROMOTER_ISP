package com.cpm.upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.himalaya.MainMenuActivity;
import com.cpm.himalaya.R;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.AssetInsertdataGetterSetter;
import com.cpm.xmlGetterSetter.ChecklistInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.CompetitionPromotionGetterSetter;
import com.cpm.xmlGetterSetter.FacingCompetitorGetterSetter;
import com.cpm.xmlGetterSetter.FailureGetterSetter;
import com.cpm.xmlGetterSetter.POIGetterSetter;
import com.cpm.xmlGetterSetter.PromotionInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;
import com.cpm.xmlHandler.FailureXMLHandler;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

@SuppressWarnings("deprecation")
public class UploadDataActivity extends Activity {
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    String app_ver;
    private String visit_date, username;
    private SharedPreferences preferences;
    private GSKDatabase database;
    private int factor;
    String datacheck = "";
    String[] words;
    String validity;
    int mid;
    Data data;

    boolean upload_status;
    String Path;
    String errormsg = "";
    boolean up_success_flag = true;

    private ArrayList<CoverageBean> coverageBeanlist = new ArrayList<CoverageBean>();
    private FailureGetterSetter failureGetterSetter = null;

    private ArrayList<AssetInsertdataGetterSetter> assetInsertdata = new ArrayList<AssetInsertdataGetterSetter>();
    private ArrayList<StockNewGetterSetter> stockImgData = new ArrayList<StockNewGetterSetter>();
    ArrayList<POIGetterSetter> poiData = new ArrayList<POIGetterSetter>();
    ArrayList<POIGetterSetter> competitionpoiData = new ArrayList<POIGetterSetter>();
    ArrayList<FacingCompetitorGetterSetter> facingcompetition = new ArrayList<FacingCompetitorGetterSetter>();
    ArrayList<CompetitionPromotionGetterSetter> promotioncompetitionData = new ArrayList<CompetitionPromotionGetterSetter>();
    private ArrayList<ChecklistInsertDataGetterSetter> assetChecklistInsertdata = new ArrayList<ChecklistInsertDataGetterSetter>();
    private ArrayList<StockNewGetterSetter> stockData = new ArrayList<>();
    ArrayList<PromotionInsertDataGetterSetter> promotionData = new ArrayList<>();
    ArrayList<AssetInsertdataGetterSetter> paidVisibility = new ArrayList<>();
    ArrayList<ChecklistInsertDataGetterSetter> paidVisibilityCheckList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
        database = new GSKDatabase(this);
        database.open();

        Intent i = getIntent();
        upload_status = i.getBooleanExtra("UploadAll", false);

        Path = Environment.getExternalStorageDirectory() + "/Mccain_Images/";

        new UploadTask(this).execute();
    }

    private class UploadTask extends AsyncTask<Void, Data, String> {
        private Context context;

        UploadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_upload);
            dialog.setTitle("Uploading Data");
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                data = new Data();

                if (upload_status == false) {
                    coverageBeanlist = database.getCoverageData(visit_date);
                } else {
                    coverageBeanlist = database.getCoverageData(null);
                }

                if (coverageBeanlist.size() > 0) {
                    if (coverageBeanlist.size() == 1) {
                        factor = 50;
                    } else {
                        factor = 100 / (coverageBeanlist.size());
                    }
                }

                for (int i = 0; i < coverageBeanlist.size(); i++) {
                    if (!coverageBeanlist.get(i).getStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        String onXML =
                                "[DATA]"
                                        + "[USER_DATA]"
                                        + "[STORE_CD]" + coverageBeanlist.get(i).getStoreId() + "[/STORE_CD]"
                                        + "[VISIT_DATE]" + coverageBeanlist.get(i).getVisitDate() + "[/VISIT_DATE]"
                                        + "[LATITUDE]" + coverageBeanlist.get(i).getLatitude() + "[/LATITUDE]"
                                        + "[APP_VERSION]" + app_ver + "[/APP_VERSION]"
                                        + "[LONGITUDE]" + coverageBeanlist.get(i).getLongitude() + "[/LONGITUDE]"
                                        + "[IN_TIME]" + coverageBeanlist.get(i).getInTime() + "[/IN_TIME]"
                                        + "[OUT_TIME]" + coverageBeanlist.get(i).getOutTime() + "[/OUT_TIME]"
                                        + "[UPLOAD_STATUS]" + "N" + "[/UPLOAD_STATUS]"
                                        + "[USER_ID]" + username + "[/USER_ID]"
                                        + "[IMAGE_URL]" + coverageBeanlist.get(i).getImage() + "[/IMAGE_URL]"
                                        + "[REASON_ID]" + coverageBeanlist.get(i).getReasonid() + "[/REASON_ID]"
                                        + "[REASON_REMARK]" + coverageBeanlist.get(i).getRemark() + "[/REASON_REMARK]"
                                        + "[/USER_DATA]"
                                        + "[/DATA]";


                        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE);
                        request.addProperty("onXML", onXML);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE, envelope);

                        Object result = (Object) envelope.getResponse();

                        datacheck = result.toString();
                        datacheck = datacheck.replace("\"", "");
                        words = datacheck.split("\\;");
                        validity = (words[0]);

                        if (validity.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                            database.updateCoverageStatus(coverageBeanlist.get(i).getMID(), CommonString.KEY_P);

                            database.updateStoreStatusOnLeave(coverageBeanlist.get(i).getStoreId(),
                                    coverageBeanlist.get(i).getVisitDate(), CommonString.KEY_P);
                        } else {
                            continue;
                            /*if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                return CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_DR_STORE_COVERAGE;
                            }*/
                        }
                        mid = Integer.parseInt((words[1]));

                        data.value = 10;
                        data.name = "Uploading";
                        publishProgress(data);


                        String final_xml = "";

                        //Stock Data
                        final_xml = "";
                        onXML = "";
                        stockData = database.getOpeningStockUpload(coverageBeanlist.get(i).getStoreId());

                        if (stockData.size() > 0) {
                            for (int j = 0; j < stockData.size(); j++) {
                                onXML = "[STOCK_DATA]"
                                        + "[MID]" + mid + "[/MID]"
                                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                        + "[SKU_CD]" + stockData.get(j).getSku_cd() + "[/SKU_CD]"
                                        + "[OPENING_STOCK]" + stockData.get(j).getEd_openingStock() + "[/OPENING_STOCK]"
                                        + "[OPENING_FACING]" + stockData.get(j).getEd_openingFacing() + "[/OPENING_FACING]"
                                        + "[MIDDAY_STOCK]" + stockData.get(j).getEd_midFacing() + "[/MIDDAY_STOCK]"
                                        + "[CLOSING_STOCK]" + stockData.get(j).getEd_closingFacing() + "[/CLOSING_STOCK]"
                                        + "[STOCK_UNDER_DAYS]" + stockData.get(j).getStock_under45days() + "[/STOCK_UNDER_DAYS]"
                                        + "[/STOCK_DATA]";

                                final_xml = final_xml + onXML;
                            }

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "STOCK_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                            result = (Object) envelope.getResponse();

                            /*if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }*/
                        }


                        //Promotion Data
                        final_xml = "";
                        onXML = "";
                        promotionData = database.getPromotionUploadData(coverageBeanlist.get(i).getStoreId());

                        if (promotionData.size() > 0) {
                            for (int j = 0; j < promotionData.size(); j++) {
                                onXML = "[PROMOTION_DATA]"
                                        + "[MID]" + mid + "[/MID]"
                                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                        + "[BRAND_CD]" + promotionData.get(j).getBrand_cd() + "[/BRAND_CD]"
                                        + "[CAMERA]" + promotionData.get(j).getCamera() + "[/CAMERA]"
                                        + "[PROMO_STOCK]" + promotionData.get(j).getPromoStock() + "[/PROMO_STOCK]"
                                        + "[PROMO_TALKER]" + promotionData.get(j).getPromoTalker() + "[/PROMO_TALKER]"
                                        + "[RUNNING_POS]" + promotionData.get(j).getRunningPOS() + "[/RUNNING_POS]"
                                        + "[/PROMOTION_DATA]";

                                final_xml = final_xml + onXML;
                            }

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "PROMOTION_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                            result = (Object) envelope.getResponse();

                            /*if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }*/
                        }


                        //Paid Visibility Data
                        final_xml = "";
                        String checkList_xml = "", paid_visibility = "";
                        onXML = "";
                        paidVisibility = database.getAssetUploadData(coverageBeanlist.get(i).getStoreId());

                        if (paidVisibility.size() > 0) {

                            for (int j = 0; j < paidVisibility.size(); j++) {
                                onXML = "[PAID_VISIBILITY_DATA]"
                                        + "[MID]" + mid + "[/MID]"
                                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                        + "[CATEGORY_CD]" + paidVisibility.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                        + "[ASSET_CD]" + paidVisibility.get(j).getAsset_cd() + "[/ASSET_CD]"
                                        + "[PRESENT]" + paidVisibility.get(j).getPresent() + "[/PRESENT]"
                                        + "[REMARK]" + paidVisibility.get(j).getRemark() + "[/REMARK]"
                                        + "[IMAGE]" + paidVisibility.get(j).getImg() + "[/IMAGE]"
                                        + "[/PAID_VISIBILITY_DATA]";

                                paid_visibility = paid_visibility + onXML;
                            }

                            //CheckList
                            paidVisibilityCheckList = database.getAssetCheckUploadData(coverageBeanlist.get(i).getStoreId());

                            if (paidVisibilityCheckList.size() > 0) {
                                for (int j = 0; j < paidVisibilityCheckList.size(); j++) {
                                    onXML = "[CHECKLIST_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[ASSET_CD]" + paidVisibilityCheckList.get(j).getAsset_cd() + "[/ASSET_CD]"
                                            + "[CHECK_LIST_ID]" + paidVisibilityCheckList.get(j).getChecklist_id() + "[/CHECK_LIST_ID]"
                                            + "[CHECK_LIST_TOGGLE]" + paidVisibilityCheckList.get(j).getChecklist_text() + "[/CHECK_LIST_TOGGLE]"
                                            + "[/CHECKLIST_DATA]";

                                    checkList_xml = checkList_xml + onXML;
                                }
                            }

                            final_xml = paid_visibility + checkList_xml;

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "PAID_VISIBILITY_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                            result = (Object) envelope.getResponse();

                            /*if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }*/
                        }


                        //<editor-fold desc="Previous Code Upload">
/*
                        //uploading Stock data
                        String final_xml = "";
                        onXML = "";
                        stockData = database.getStockUpload(coverageBeanlist.get(i).getStoreId());

                        if (stockData.size() > 0) {
                            for (int j = 0; j < stockData.size(); j++) {
                                onXML = "[STOCK_DATA][SKU_CD]"
                                        + stockData.get(j).getSku_cd()
                                        + "[/SKU_CD]"
                                        + "[BRAND_CD]"
                                        + stockData.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[OPENING_TOTAL_STOCK]"
                                        + stockData.get(j).getOpenning_total_stock()
                                        + "[/OPENING_TOTAL_STOCK]"
                                        + "[FACING]"
                                        + stockData.get(j).getOpening_facing()
                                        + "[/FACING]"
                                        + "[STOCK_UNDER_DAYS]"
                                        + stockData.get(j).getStock_under45days()
                                        + "[/STOCK_UNDER_DAYS]"
                                        + "[CLOSING_STOCK]"
                                        + stockData.get(j).getClosing_stock()
                                        + "[/CLOSING_STOCK]"
                                        + "[MIDDAY_TOTAL_STOCK]"
                                        + stockData.get(j).getTotal_mid_stock_received()
                                        + "[/MIDDAY_TOTAL_STOCK]"
                                        + "[/STOCK_DATA]";

                                final_xml = final_xml + onXML;
                            }

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "STOCK_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }
                        }

                        //uploading Stock Image data
                        final_xml = "";
                        onXML = "";
                        stockImgData = database.getHeaderStockImageData(coverageBeanlist.get(i).getStoreId(), visit_date);

                        if (stockImgData.size() > 0) {

                            for (int j = 0; j < stockImgData.size(); j++) {
                                if (stockImgData.get(j).getImg_cam() != null) {
                                    onXML = "[STOCK_IMAGE_DATA]"
                                            + "[BRAND_CD]"
                                            + stockImgData.get(j).getBrand_cd()
                                            + "[/BRAND_CD]"
                                            + "[MID]"
                                            + mid
                                            + "[/MID]"
                                            + "[CREATED_BY]"
                                            + username
                                            + "[/CREATED_BY]"
                                            + "[STOCK_IMAGE]"
                                            + stockImgData.get(j).getImg_cam()
                                            + "[/STOCK_IMAGE]"
                                            + "[/STOCK_IMAGE_DATA]";

                                    final_xml = final_xml + onXML;
                                }

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "STOCK_IMAGE_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }


                        }


                        //	uploading Asset data

                        final_xml = "";
                        onXML = "";
                        assetInsertdata = database.getAssetUpload(coverageBeanlist.get(i).getStoreId());

                        if (assetInsertdata.size() > 0) {

                            for (int j = 0; j < assetInsertdata.size(); j++) {

                                onXML = "[ASSET_DATA][ASSET_CD]"
                                        + assetInsertdata.get(j).getAsset_cd()
                                        + "[/ASSET_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[PRESENT]"
                                        + assetInsertdata.get(j).getPresent()
                                        + "[/PRESENT]"
                                        + "[CATEGORY_CD]"
                                        + assetInsertdata.get(j).getCategory_cd()
                                        + "[/CATEGORY_CD]"
                                        + "[REMARK]"
                                        + assetInsertdata.get(j).getRemark()
                                        + "[/REMARK]"
                                        + "[IMAGE_URL]"
                                        + assetInsertdata.get(j).getImg()
                                        + "[/IMAGE_URL]"
                                        + "[/ASSET_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "ASSET_DATA_NEW");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }


                        }

                        //	uploading Asset Checklist data
                        final_xml = "";
                        onXML = "";

                        assetChecklistInsertdata = database.getCheckListAllInsertData(coverageBeanlist.get(i).getStoreId(), visit_date);

                        if (assetChecklistInsertdata.size() > 0) {

                            for (int j = 0; j < assetChecklistInsertdata.size(); j++) {

                                onXML = "[CHECK_LIST_DATA][ASSET_CD]"
                                        + assetChecklistInsertdata.get(j).getAsset_cd()
                                        + "[/ASSET_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[CHECK_LIST_ID]"
                                        + assetChecklistInsertdata.get(j).getChecklist_id()
                                        + "[/CHECK_LIST_ID]"
                                        + "[CHECK_LIST_TEXT]"
                                        + assetChecklistInsertdata.get(j).getChecklist_text()
                                        + "[/CHECK_LIST_TEXT]"
                                        + "[/CHECK_LIST_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "CHECK_LIST_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }


                        }


                        //uploading Competition Faceup data
                        final_xml = "";
                        onXML = "";
                        facingcompetition = database.getFacingCompetitorData(coverageBeanlist.get(i).getStoreId());

                        if (facingcompetition.size() > 0) {

                            for (int j = 0; j < facingcompetition.size(); j++) {
                                onXML = "[COMPETITION_FACEUP_DATA][BRAND_CD]"
                                        + facingcompetition.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[FACING]"
                                        + facingcompetition.get(j).getMccaindf()
                                        + "[/FACING]"
                                        + "[/COMPETITION_FACEUP_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "COMPETITION_FACEUP_DATA_NEW");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }
                        }



                        //uploading Additional poi data
                        final_xml = "";
                        onXML = "";
                        poiData = database.getPOIData(coverageBeanlist.get(i).getStoreId());

                        if (poiData.size() > 0) {
                            for (int j = 0; j < poiData.size(); j++) {
                                onXML = "[POI_DATA][CATEGORY_CD]"
                                        + poiData.get(j).getCategory_cd()
                                        + "[/CATEGORY_CD]"
                                        + "[ASSET_CD]"
                                        + poiData.get(j).getAsset_cd()
                                        + "[/ASSET_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[REMARK]"
                                        + poiData.get(j).getRemark()
                                        + "[/REMARK]"
                                        + "[BRAND_CD]"
                                        + poiData.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[IMAGE_POI]"
                                        + poiData.get(j).getImage()
                                        + "[/IMAGE_POI]"

                                        + "[/POI_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "POI_NEW_DATA");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }


                        }


                        //uploading Competition Additional poi data
                        final_xml = "";
                        onXML = "";
                        competitionpoiData = database.getCompetitionPOIData(coverageBeanlist.get(i).getStoreId());

                        if (competitionpoiData.size() > 0) {

                            for (int j = 0; j < competitionpoiData.size(); j++) {
                                onXML = "[COMPETITION_POI_DATA][CATEGORY_CD]"
                                        + competitionpoiData.get(j).getCategory_cd()
                                        + "[/CATEGORY_CD]"
                                        + "[ASSET_CD]"
                                        + competitionpoiData.get(j).getAsset_cd()
                                        + "[/ASSET_CD]"
                                        + "[BRAND_CD]"
                                        + competitionpoiData.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[REMARK]"
                                        + competitionpoiData.get(j).getRemark()
                                        + "[/REMARK]"

                                        + "[/COMPETITION_POI_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml
                                    + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "COMPETITION_POI_DATA_NEW");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }
                        }


                        //uploading Promotion data
                        final_xml = "";
                        onXML = "";
                        promotionData = database.getPromotionUpload(coverageBeanlist.get(i).getStoreId());

                        if (promotionData.size() > 0) {
                            for (int j = 0; j < promotionData.size(); j++) {

                                onXML = "[PROMOTION_DATA]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[PID]"
                                        + promotionData.get(j).getPid()
                                        + "[/PID]"
                                        *//*+ "[PROMOTION]"
                                        + promotionData.get(j).getPromotion_txt()
										+ "[/PROMOTION]"*//*
                                        + "[PRESENT]"
                                        + promotionData.get(j).getPresent()
                                        + "[/PRESENT]"
                                        + "[REMARK]"
                                        + promotionData.get(j).getRemark()
                                        + "[/REMARK]"
                                        + "[IMAGE_URL]"
                                        + promotionData.get(j).getImg()
                                        + "[/IMAGE_URL]"
                                        + "[BRAND_CD]"
                                        + promotionData.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[/PROMOTION_DATA]";

                                final_xml = final_xml + onXML;
                            }

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "PROMOTION_DATA_NEW");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);

                            androidHttpTransport.call(
                                    CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML,
                                    envelope);
                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(
                                    CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(
                                    CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                        }

                        //uploading Competition Promotion data
                        final_xml = "";
                        onXML = "";
                        promotioncompetitionData = database.getCompetitionPromotionData(coverageBeanlist.get(i).getStoreId());

                        if (promotioncompetitionData.size() > 0) {

                            for (int j = 0; j < promotioncompetitionData.size(); j++) {
                                onXML = "[COMPETITION_PROMOTION_DATA][CATEGORY_CD]"
                                        + promotioncompetitionData.get(j).getCategory_cd()
                                        + "[/CATEGORY_CD]"
                                        + "[MID]"
                                        + mid
                                        + "[/MID]"
                                        + "[CREATED_BY]"
                                        + username
                                        + "[/CREATED_BY]"
                                        + "[PROMOTION]"
                                        + promotioncompetitionData.get(j).getPromotion()
                                        + "[/PROMOTION]"
                                        + "[BRAND_CD]"
                                        + promotioncompetitionData.get(j).getBrand_cd()
                                        + "[/BRAND_CD]"
                                        + "[REMARK]"
                                        + promotioncompetitionData.get(j).getRemark()
                                        + "[/REMARK]"
                                        + "[/COMPETITION_PROMOTION_DATA]";

                                final_xml = final_xml + onXML;

                            }

                            final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "COMPETITION_PROMOTION_DATA_NEW");
                            request.addProperty("USERNAME", username);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_XML;
                            }
                        }*/
                        //</editor-fold>

                        //<editor-fold desc="Previous Code">
                            /*onXML = "";
                        deepfreezerData = database.getDFTypeUploadData(coverageBeanlist.get(i).getStoreId());

						if (deepfreezerData.size()>0) {

							for (int j = 0; j < deepfreezerData.size(); j++) {

								String data = "[DEEPFREEZER_DATA][FID]"
										+ deepfreezerData.get(j).getFid()
										+ "[/FID]"
										+ "[MID]"
										+ mid
										+ "[/MID]"
										+ "[CREATED_BY]"
										+ username
										+ "[/CREATED_BY]"
										+ "[STATUS]"
										+ deepfreezerData.get(j).getStatus()
										+ "[/STATUS]"
										+ "[REMARK]"
										+ deepfreezerData.get(j).getRemark()
										+ "[/REMARK]"
										+ "[/DEEPFREEZER_DATA]";
									onXML=onXML+data;

							}

							String finaldata = "[DATA]"+onXML+"[/DATA]";
							*//*final String sos_xml = "[MID]"
                                        + mid
										+ "[/MID][KEY]"
										+ "DEEPFREEZER_DATA"
										+ "[/KEY][XMLDATA]"
										+ onXML
										+ "[/XMLDATA][USERNAME]"
										+ username
										+ "[/USERNAME]";*//*


							 request = new SoapObject(
									CommonString.NAMESPACE,
									CommonString.METHOD_UPLOAD_XML);
							request.addProperty("XMLDATA", finaldata);
							request.addProperty("KEYS", "DEEPFREEZER_DATA");
							request.addProperty("USERNAME", username);
							request.addProperty("MID", mid);

							 envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							envelope.dotNet = true;
							envelope.setOutputSoapObject(request);

						androidHttpTransport = new HttpTransportSE(
									CommonString.URL);

							androidHttpTransport.call(
									CommonString.SOAP_ACTION+CommonString.METHOD_UPLOAD_XML,
									envelope);
							result = (Object) envelope.getResponse();


							if (result.toString().equalsIgnoreCase(
									CommonString.KEY_NO_DATA)) {
								return CommonString.METHOD_UPLOAD_XML;
							}

							if (result.toString().equalsIgnoreCase(
									CommonString.KEY_FAILURE)) {
								return CommonString.METHOD_UPLOAD_XML;
							}

						}*/


                        //	uploading Facing Competitor data

						/*final_xml = "";
                        onXML = "";
						facingCompetitorData = database.getFacingCompetitorData(coverageBeanlist.get(i).getStoreId());

						if (facingCompetitorData.size()>0) {

							for (int j = 0; j < facingCompetitorData.size(); j++) {

								onXML = "[FACING_COMPETITOR_DATA][CATEGORY_CD]"
										+ facingCompetitorData.get(j).getCategory_cd()
										+ "[/CATEGORY_CD]"
										+ "[MID]"
										+ mid
										+ "[/MID]"
										+ "[CREATED_BY]"
										+ username
										+ "[/CREATED_BY]"
										+ "[MCCAIN_DF]"
										+ facingCompetitorData.get(j).getMccaindf()
										+ "[/MCCAIN_DF]"
										+ "[STORE_DF]"
										+ facingCompetitorData.get(j).getStoredf()
										+ "[/STORE_DF]"
										+ "[/FACING_COMPETITOR_DATA]";

								final_xml = final_xml + onXML;

							}

							final String sos_xml = "[DATA]" + final_xml
									+ "[/DATA]";

							 request = new SoapObject(
										CommonString.NAMESPACE,
										CommonString.METHOD_UPLOAD_XML);
								request.addProperty("XMLDATA", sos_xml);
								request.addProperty("KEYS", "FACING_COMPETITOR_DATA");
								request.addProperty("USERNAME", username);
								request.addProperty("MID", mid);

								 envelope = new SoapSerializationEnvelope(
										SoapEnvelope.VER11);
								envelope.dotNet = true;
								envelope.setOutputSoapObject(request);

							androidHttpTransport = new HttpTransportSE(
										CommonString.URL);

								androidHttpTransport.call(
										CommonString.SOAP_ACTION+CommonString.METHOD_UPLOAD_XML,
										envelope);
								result = (Object) envelope.getResponse();


								if (result.toString().equalsIgnoreCase(
										CommonString.KEY_NO_DATA)) {
									return CommonString.METHOD_UPLOAD_XML;
								}

								if (result.toString().equalsIgnoreCase(
										CommonString.KEY_FAILURE)) {
									return CommonString.METHOD_UPLOAD_XML;
								}

							}*/
                        //</editor-fold>

                        //<editor-fold desc="Previous Code">
                        //		uploading Food Store data

						/*final_xml = "";
                        onXML = "";
						foodStoredata = database.getFoodStoreUpload(coverageBeanlist.get(i).getStoreId());

						if (foodStoredata.size()>0) {

							for (int j = 0; j < foodStoredata.size(); j++) {

								onXML = "[FOOD_STORE_DATA][SKU_CD]"
										+ foodStoredata.get(j).getSku_cd()
										+ "[/SKU_CD]"
										+ "[MID]"
										+ mid
										+ "[/MID]"
										+ "[CREATED_BY]"
										+ username
										+ "[/CREATED_BY]"
										+ "[ACTUAL_LISTED]"
										+ foodStoredata.get(j).getActual_listed()
										+ "[/ACTUAL_LISTED]"
										+ "[MCCAIN_DF]"
										+ foodStoredata.get(j).getMccain_df()
										+ "[/MCCAIN_DF]"
										+ "[STORE_DF]"
										+ foodStoredata.get(j).getStore_df()
										+ "[/STORE_DF]"
										+ "[MTD_SALES]"
										+ foodStoredata.get(j).getMtd_sales()
										+ "[/MTD_SALES]"
										+ "[PACKING_SIZE]"
										+ foodStoredata.get(j).getPacking_size()
										+ "[/PACKING_SIZE]"
										+ "[BRAND_CD]"
										+ foodStoredata.get(j).getBrand_cd()
										+ "[/BRAND_CD]"
										+ "[/FOOD_STORE_DATA]";

								final_xml = final_xml + onXML;

							}

							final String sos_xml = "[DATA]" + final_xml
									+ "[/DATA]";


							request = new SoapObject(
									CommonString.NAMESPACE,
									CommonString.METHOD_UPLOAD_XML);
							request.addProperty("XMLDATA", sos_xml);
							request.addProperty("KEYS", "FOOD_STORE_DATA");
							request.addProperty("USERNAME", username);
							request.addProperty("MID", mid);

							 envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							envelope.dotNet = true;
							envelope.setOutputSoapObject(request);

						androidHttpTransport = new HttpTransportSE(
									CommonString.URL);

							androidHttpTransport.call(
									CommonString.SOAP_ACTION+CommonString.METHOD_UPLOAD_XML,
									envelope);
							result = (Object) envelope.getResponse();


							if (result.toString().equalsIgnoreCase(
									CommonString.KEY_NO_DATA)) {
								return CommonString.METHOD_UPLOAD_XML;
							}

							if (result.toString().equalsIgnoreCase(
									CommonString.KEY_FAILURE)) {
								return CommonString.METHOD_UPLOAD_XML;
							}

						}*/
                        //</editor-fold>

                        //<editor-fold desc="Previous Code">
                        //	uploading Calls data

					/*	final_xml = "";
                        onXML = "";
						callsData = database.getCallsData(coverageBeanlist.get(i).getStoreId());

						if (callsData.size()>0) {
							
								onXML = "[CALLS_DATA]"	
										+ "[MID]"
										+ mid
										+ "[/MID]"
										+ "[CREATED_BY]"
										+ username
										+ "[/CREATED_BY]"
										+ "[TOTAL_CALLS]"
										+ callsData.get(0).getTotal_calls()
										+ "[/TOTAL_CALLS]"
										+ "[PRODUCTIVE_CALLS]"
										+ callsData.get(0).getProductive_calls()
										+ "[/PRODUCTIVE_CALLS]"
										+ "[/CALLS_DATA]";

								final_xml = onXML;


							final String sos_xml = "[DATA]" + final_xml
									+ "[/DATA]";
							
							 request = new SoapObject(
										CommonString.NAMESPACE,
										CommonString.METHOD_UPLOAD_XML);
								request.addProperty("XMLDATA", sos_xml);
								request.addProperty("KEYS", "CALL_DATA");
								request.addProperty("USERNAME", username);
								request.addProperty("MID", mid);

								 envelope = new SoapSerializationEnvelope(
										SoapEnvelope.VER11);
								envelope.dotNet = true;
								envelope.setOutputSoapObject(request);

							androidHttpTransport = new HttpTransportSE(
										CommonString.URL);

							 	androidHttpTransport.call(
										CommonString.SOAP_ACTION+CommonString.METHOD_UPLOAD_XML,
										envelope);
								result = (Object) envelope.getResponse();


								if (result.toString().equalsIgnoreCase(
										CommonString.KEY_NO_DATA)) {
									return CommonString.METHOD_UPLOAD_XML;
								}

								if (result.toString().equalsIgnoreCase(
										CommonString.KEY_FAILURE)) {
									return CommonString.METHOD_UPLOAD_XML;
								}

							}*/

						
						
						/*status=coverageBeanlist.get(i).getStatus();
                        if(status.equals(CommonString.STORE_STATUS_LEAVE)){
							
							String path=coverageBeanlist.get(i).getImage();
							String store_cd=coverageBeanlist.get(i).getStoreId();
							if(path!=null && !path.equals("")){
								UploadImage(path,store_cd);
							}
							
							
							
						}
*/
                        //</editor-fold>
                    }
                    data.value = factor * (i + 1);
                    data.name = "Uploading";
                    publishProgress(data);


                    // SET COVERAGE STATUS

                    String final_xml = "";
                    String onXML = "";
                    onXML =
                            "[COVERAGE_STATUS]"
                                    + "[STORE_ID]" + coverageBeanlist.get(i).getStoreId() + "[/STORE_ID]"
                                    + "[VISIT_DATE]" + coverageBeanlist.get(i).getVisitDate() + "[/VISIT_DATE]"
                                    + "[USER_ID]" + coverageBeanlist.get(i).getUserId() + "[/USER_ID]"
                                    + "[STATUS]" + CommonString.KEY_D + "[/STATUS]"
                                    + "[/COVERAGE_STATUS]";

                    final_xml = final_xml + onXML;

                    final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                    SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS);
                    request.addProperty("onXML", sos_xml);
                    /*request.addProperty("KEYS", "COVERAGE_STATUS");
                    request.addProperty("USERNAME", username);
					request.addProperty("MID", mid);*/

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                    androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS, envelope);

                    Object result = (Object) envelope.getResponse();

                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                        return CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS;
                    }

                    /*if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                        return CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS;
                    }

                    if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        return CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS;
                    }*/

                    database.open();

                    /*database.updateCoverageStatus(coverageBeanlist.get(i).getMID(), CommonString.KEY_D);
                    database.updateStoreStatusOnLeave(coverageBeanlist.get(i).getStoreId(),
                            coverageBeanlist.get(i).getVisitDate(), CommonString.KEY_D);*/
                }

            } catch (MalformedURLException e) {
                up_success_flag = false;
                final AlertMessage message = new AlertMessage(UploadDataActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });

            } catch (IOException e) {
                up_success_flag = false;
                final AlertMessage message = new AlertMessage(UploadDataActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });

            } catch (Exception e) {
                up_success_flag = false;
                final AlertMessage message = new AlertMessage(UploadDataActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
                e.getMessage();
                e.printStackTrace();
                e.getCause();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            }

            if (up_success_flag == true) {
                return CommonString.KEY_SUCCESS;
            } else {
                return CommonString.KEY_FAILURE;
            }
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                /*Intent intent = new Intent(getBaseContext(), UploadAllImageActivity.class);
                intent.putExtra("UploadAll", true);
                startActivity(intent);*/


                //temparory code
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
                finish();

            } else if (result.equals(CommonString.KEY_FAILURE) || !result.equals("")) {
                AlertMessage message = new AlertMessage(UploadDataActivity.this, CommonString.ERROR + result, "success", null);
                message.showMessage();
            }
        }
    }

    class Data {
        int value;
        String name;
    }

    @Override
    protected void onStop() {
        super.onStop();
        database.close();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
        finish();
    }


    String makeJson(String json) {
        json = json.replace("\\", "");
        json = json.replace("\"[", "[");
        json = json.replace("]\"", "]");

        return json;
    }

    public JSONArray makeJsonArray(JSONArray json) {
        JSONArray jason = new JSONArray();

        for (int i = 0; i < json.length(); i++) {


        }
        return json;
    }

    public String UploadImage(String path, String store_cd) throws Exception {

        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Path + path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(
                Path + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE,
                CommonString.METHOD_UPLOAD_IMAGE);

        String[] split = path.split("/");
        String path1 = split[split.length - 1];

        request.addProperty("img", ba1);
        request.addProperty("name", path1);
        request.addProperty("FolderName", "StoreImages");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                CommonString.URL);

        androidHttpTransport
                .call(CommonString.SOAP_ACTION_UPLOAD_IMAGE,
                        envelope);
        Object result = (Object) envelope.getResponse();

        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                return CommonString.KEY_FALSE;
            }

            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // for failure
            FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
            xmlR.setContentHandler(failureXMLHandler);

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.toString()));
            xmlR.parse(is);

            failureGetterSetter = failureXMLHandler
                    .getFailureGetterSetter();

            if (failureGetterSetter.getStatus().equalsIgnoreCase(
                    CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }
        } else {
            new File(Path + path).delete();
            SharedPreferences.Editor editor = preferences
                    .edit();
            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
            editor.commit();
        }

        return "";
    }
}
