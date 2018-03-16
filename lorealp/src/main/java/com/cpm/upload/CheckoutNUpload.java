package com.cpm.upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.GeotaggingBeans;
import com.cpm.GetterSetter.ShareOfShelfGetterSetter;
import com.cpm.GetterSetter.StoreStockinGetterSetter;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.CoverageBean;
import com.cpm.lorealpromoter.MainMenuActivity;
import com.cpm.lorealpromoter.R;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.AssetInsertdataGetterSetter;
import com.cpm.xmlGetterSetter.FailureGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.MarketIntelligenceGetterSetter;
import com.cpm.xmlGetterSetter.PromotionInsertDataGetterSetter;
import com.cpm.xmlGetterSetter.SampledGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;
import com.cpm.xmlHandler.FailureXMLHandler;
import com.crashlytics.android.Crashlytics;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CheckoutNUpload extends Activity {
    ArrayList<JourneyPlanGetterSetter> jcplist;
    GSKDatabase database;
    private SharedPreferences preferences;
    private String username, visit_date, store_out_time, prev_date, result;
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private Data data;
    ArrayList<CoverageBean> coverageBean;
    private ArrayList<CoverageBean> coverageBeanlist = new ArrayList<CoverageBean>();
    String app_ver;
    String datacheck = "";
    String img_str="";
    String[] words;
    String validity;
    int mid;
    String errormsg = "";
    String Path;
    private FailureGetterSetter failureGetterSetter = null;
    private ArrayList<StockNewGetterSetter> stockData = new ArrayList<>();
    private ArrayList<PromotionInsertDataGetterSetter> promotionData = new ArrayList<>();
    private ArrayList<AssetInsertdataGetterSetter> paidVisibility = new ArrayList<>();
    ArrayList<MarketIntelligenceGetterSetter> additionalVisibilityData = new ArrayList<>();
    private ArrayList<StockNewGetterSetter> stockbackroomData = new ArrayList<>();
    private ArrayList<StockNewGetterSetter> stockImages = new ArrayList<>();
    private ArrayList<StockNewGetterSetter> stockbackroomImages = new ArrayList<>();
    ArrayList<SampledGetterSetter> sampledData = new ArrayList<>();
    boolean isError = false;
    boolean up_success_flag = true;
    String exceptionMessage = "";
    String resultFinal;


    ArrayList<ShareOfShelfGetterSetter> shareOfShelImgData= new ArrayList<>();
    ArrayList<ShareOfShelfGetterSetter> shareOfShelfData= new ArrayList<>();
    StoreStockinGetterSetter storeSpinner;
    ArrayList<StockNewGetterSetter> stockInData = new ArrayList<>();
    JourneyPlanGetterSetter journeyPlanGetterSetter;
    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_n_upload);
        database = new GSKDatabase(this);
        database.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        storeSpinner=new StoreStockinGetterSetter();
        Path = CommonString.FILE_PATH;
        if (!isCheckoutDataExist()) {
            new UploadTask(this).execute();
        }
    }

    public boolean isCheckoutDataExist() {
        boolean flag = false;
        database.open();
        jcplist = database.getAllJCPData();
        for (int i = 0; i < jcplist.size(); i++) {
            if (!jcplist.get(i).getVISIT_DATE().get(0).equals(visit_date)) {
                prev_date = jcplist.get(i).getVISIT_DATE().get(0);
                coverageBean = database.getCoverageSpecificData(jcplist.get(i).getStore_cd().get(0));
                if (coverageBean.size() > 0) {
                    for (int i1 = 0; i1 < coverageBean.size(); i1++) {
                        if (coverageBean.get(i1).getStatus().equalsIgnoreCase(CommonString.KEY_VALID)||coverageBean.get(i1).getStatus().equalsIgnoreCase(CommonString.KEY_INVALID)) {
                            store_out_time = coverageBean.get(0).getOutTime();
                            if (store_out_time == null || store_out_time.equals("")) {
                                store_out_time = getCurrentTime();
                            }
                            flag = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    new BackgroundTask(this).execute();
                    break;
                }
            }

        }
        return flag;
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
                data.value = 10;
                data.name = "Uploading....";
                publishProgress(data);
                database.open();
                coverageBeanlist = database.getCoverageData(prev_date);
                String final_xml = "";
                String onXML = "";
                if (coverageBeanlist.size() > 0) {
                    for (int i = 0; i < coverageBeanlist.size(); i++) {
                        // if (!coverageBeanlist.get(i).getStatus().equalsIgnoreCase(CommonString.KEY_U)) {
                        journeyPlanGetterSetter = database.getStoreStatus(coverageBeanlist.get(i).getStoreId());
                        if (!journeyPlanGetterSetter.getUploadStatus().get(0).equalsIgnoreCase(CommonString.KEY_U)
                                && !journeyPlanGetterSetter.getUploadStatus().get(0).equalsIgnoreCase(CommonString.KEY_D)) {
                            onXML = "[DATA]" + "[USER_DATA]"
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
                                    + "[IMAGE_URL1]" + coverageBeanlist.get(i).getImage1() + "[/IMAGE_URL1]"
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
                                database.updateStoreStatusOnLeave(coverageBeanlist.get(i).getStoreId(), coverageBeanlist.get(i).getVisitDate(), CommonString.KEY_P);
                            } else {
                                isError = true;
                                continue;
                            }
                            mid = Integer.parseInt((words[1]));
                            data.value = 20;
                            data.name = "Uploading";
                            publishProgress(data);

                            //LOREALPRO_STOCK_DATA
                            final_xml = "";
                            onXML = "";
                            stockData = database.getOpeningStockUpload(coverageBeanlist.get(i).getStoreId());
                            if (stockData.size() > 0) {
                                for (int j = 0; j < stockData.size(); j++) {
                                    onXML = "[LOREAL_STOCK_FLOOR_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[SKU_CD]" + stockData.get(j).getSku_cd() + "[/SKU_CD]"
                                            + "[STOCK]" + stockData.get(j).getStock1() + "[/STOCK]"
                                            + "[CLOSING_STOCK]" + stockData.get(j).getEd_closingFacing() + "[/CLOSING_STOCK]"
                                            + "[/LOREAL_STOCK_FLOOR_DATA]";

                                    final_xml = final_xml + onXML;
                                }

                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_STOCK_FLOOR_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 20;
                                data.name = "LOREAL_STOCK_FLOOR_DATA Data";
                                publishProgress(data);
                            }

                            //LOREALPRO_STOCK_IMAGE_DATA
                            final_xml = "";
                            onXML = "";
                            stockImages = database.getStockImageUploadData(coverageBeanlist.get(i).getStoreId());
                            if (stockImages.size() > 0 && !stockImages.get(0).getImg_cam().equals("") ||  (stockImages.size() > 0 && !stockImages.get(0).getCatstock().equals("") )) {
                                for (int j = 0; j < stockImages.size(); j++) {

                                    onXML = "[LOREAL_STOCK_CATEGORY_IMAGE_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[CATEGORY_CD]" + stockImages.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                            + "[CATEGORY_IMAGE]" + stockImages.get(j).getImg_cat_one() + "[/CATEGORY_IMAGE]"
                                            + "[/LOREAL_STOCK_CATEGORY_IMAGE_DATA]";
                                    final_xml = final_xml + onXML;

                                }

                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_STOCK_CATEGORY_IMAGE_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 25;
                                data.name = "Stock Image Data";
                                publishProgress(data);
                            }

                            //LOREAL_STOCK_BACKROOM_DATA
                            final_xml = "";
                            onXML = "";
                            stockbackroomData = database.getOpeningBackRoomStockUpload(coverageBeanlist.get(i).getStoreId());
                            if (stockbackroomData.size() > 0) {
                                for (int j = 0; j < stockbackroomData.size(); j++) {
                                    onXML = "[LOREAL_STOCK_BACKROOM_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[SKU_CD]" + stockbackroomData.get(j).getSku_cd() + "[/SKU_CD]"
                                            + "[STOCK]" + stockbackroomData.get(j).getStock1() + "[/STOCK]"
                                            + "[CLOSING_STOCK]" + stockbackroomData.get(j).getEd_closingFacing() + "[/CLOSING_STOCK]"
                                            + "[/LOREAL_STOCK_BACKROOM_DATA]";
                                    final_xml = final_xml + onXML;
                                }

                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_STOCK_BACKROOM_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 20;
                                data.name = "LOREAL_STOCK_BACKROOM_DATA Data";
                                publishProgress(data);
                            }


                            /*//LOREAL_STOCK_BACKROOM_IMAGE_DATA
                            final_xml = "";
                            onXML = "";
                            stockbackroomImages = database.getStockBackRoomImageUploadData(coverageBeanlist.get(i).getStoreId());
                            if (stockbackroomImages.size() > 0 && !stockbackroomImages.get(0).getImg_cam().equals("")) {
                                for (int j = 0; j < stockbackroomImages.size(); j++) {
                                    if (!stockbackroomImages.get(j).getImg_cam().equals("") || !stockbackroomImages.get(j).getImg_cat_one().equals("")
                                            || !stockbackroomImages.get(j).getImg_cat_two().equals("")) {

                                        onXML = "[LOREAL_STOCK_BACKROOM_IMAGE_DATA]"
                                                + "[MID]" + mid + "[/MID]"
                                                + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                                + "[CATEGORY_CD]" + stockbackroomImages.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                                + "[LOREAL_BACKROOM_IMAGE]" + stockbackroomImages.get(j).getImg_cam() + "[/LOREAL_BACKROOM_IMAGE]"
                                                + "[CATEGORY__ONE_BACKROOM_IMAGE]" + stockbackroomImages.get(j).getImg_cat_one() + "[/CATEGORY__ONE_BACKROOM_IMAGE]"
                                                + "[CATEGORY__TWO_BACKROOM_IMAGE]" + stockbackroomImages.get(j).getImg_cat_two() + "[/CATEGORY__TWO_BACKROOM_IMAGE]"
                                                + "[/LOREAL_STOCK_BACKROOM_IMAGE_DATA]";

                                        final_xml = final_xml + onXML;
                                    }

                                }

                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_STOCK_BACKROOM_IMAGE_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 25;
                                data.name = "LOREAL_STOCK_BACKROOM Image Data";
                                publishProgress(data);
                            }*/

                            //upendra_12jan
                            //LOREALPRO_STOCK_IN_DATA
                            final_xml = "";
                            onXML = "";
                            storeSpinner = database.getStockInSpinneruPLOADData(coverageBeanlist.get(i).getStoreId());
                            stockInData = database.getStockInUploadFromDatabase(coverageBeanlist.get(i).getStoreId());
                            if (stockInData.size() > 0) {
                                String stock_in_exit = "", stock_in_brandListXml = "";

                                onXML = "[LOREAL_STOCK_IN_EXIT_DATA]"
                                        + "[MID]" + mid + "[/MID]"
                                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                        + "[SELECT_BRAND]" + storeSpinner.getSelect_brand() + "[/SELECT_BRAND]"
                                        + "[/LOREAL_STOCK_IN_EXIT_DATA]";
                                stock_in_exit = stock_in_exit + onXML;

                                for (int j = 0; j < stockInData.size(); j++) {
                                    stock_in_brandListXml = "[LOREAL_STOCK_IN_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[SKU_CD]" + stockInData.get(j).getSku_cd() + "[/SKU_CD]"
                                            + "[STOCK_IN_DATA]" + stockInData.get(j).getEd_midFacing() + "[/STOCK_IN_DATA]"
                                            + "[/LOREAL_STOCK_IN_DATA]";
                                    final_xml = final_xml + stock_in_brandListXml;

                                }


                                final String sos_xml = "[DATA]" +final_xml+stock_in_exit + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_STOCK_IN_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 21;
                                data.name = "LOREAL_STOCK_IN_EXIT_DATA Data";
                                publishProgress(data);
                            }

                            //shareOfShelImgData
                            final_xml = "";
                            onXML = "";
                            shareOfShelImgData = database.getHeaderShareOfSelfImgUploadData1(coverageBeanlist.get(i).getStoreId());
                            if (shareOfShelImgData.size() > 0) {
                                for (int j = 0; j < shareOfShelImgData.size(); j++) {
                                    shareOfShelfData=   database.getShareofShelfBrandDataUpload(shareOfShelImgData.get(j).getKey_id(), coverageBeanlist.get(i).getStoreId());
                                    String share_of_shelf_List="",share_ofShef_ImageListXml="";
                                    for (int c = 0; c < shareOfShelfData.size(); c++) {
                                        share_ofShef_ImageListXml = "[LOREAL_SHARE_OF_SHELF_DATA]"
                                                + "[MID]" + mid + "[/MID]"
                                                + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                                + "[COMMON_ID]" + shareOfShelImgData.get(j).getKey_id() + "[/COMMON_ID]"
                                                + "[CATEGORY_CD]" + shareOfShelImgData.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                                + "[BRAND_CD]" + shareOfShelfData.get(c).getBrand_cd() + "[/BRAND_CD]"
                                              //  + "[BRAND]" + shareOfShelfData.get(c).getBrand() + "[/BRAND]"
                                                + "[BRAND_FACING]" + shareOfShelfData.get(c).getFacing() + "[/BRAND_FACING]"
                                                + "[/LOREAL_SHARE_OF_SHELF_DATA]";

                                        share_of_shelf_List = share_of_shelf_List + share_ofShef_ImageListXml;
                                    }
                                    // final String share_of_xml =  share_ofShef_ImageListXml;

                                    onXML = "[LOREAL_SHARE_OF_SHELF_IMAGE_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[COMMON_ID]" + shareOfShelImgData.get(j).getKey_id() + "[/COMMON_ID]"
                                            + "[CATEGORY_CD]" + shareOfShelImgData.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                           // + "[CATEGORY]" + shareOfShelImgData.get(j).getCategory() + "[/CATEGORY]"
                                            + "[CATEGORY_FACING]" + shareOfShelImgData.get(j).getCat_facing() + "[/CATEGORY_FACING]"
                                            + "[IMAGE_CATEGORY_FACING]" + shareOfShelImgData.get(j).getImg_cat_facing() + "[/IMAGE_CATEGORY_FACING]"
                                            + "[SHARE_OF_SHELF_BRAND_FACING]"
                                            + share_of_shelf_List
                                            + "[/SHARE_OF_SHELF_BRAND_FACING]"
                                            + "[/LOREAL_SHARE_OF_SHELF_IMAGE_DATA]";

                                    final_xml = final_xml + onXML;
                                }
                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_SHARE_OF_SHELF_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 23;
                                data.name = "LOREAL_SHARE_OF_SHELF_IMAGE_DATA Data";
                                publishProgress(data);
                            }
                            // //upendra_12jan END
                            //

                            //Promotion Data
                            final_xml = "";
                            onXML = "";
                            promotionData = database.getPromotionUploadData(coverageBeanlist.get(i).getStoreId());
                            if (promotionData.size() > 0) {
                                for (int j = 0; j < promotionData.size(); j++) {
                                    onXML = "[LOREAL_PROMOTION_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[PID]" + promotionData.get(j).getPromotion_id() + "[/PID]"
                                            + "[BRAND_CD]" + promotionData.get(j).getBrand_cd() + "[/BRAND_CD]"
                                            + "[PRESENT_SPIN]" + promotionData.get(j).getPresentSpi() + "[/PRESENT_SPIN]"
                                            + "[CAMERA]" + promotionData.get(j).getCamera() + "[/CAMERA]"
                                            + "[PROMOTION_REMARK_CD]" + promotionData.get(j).getReason_cd() + "[/PROMOTION_REMARK_CD]"
                                            + "[/LOREAL_PROMOTION_DATA]";

                                    final_xml = final_xml + onXML;
                                }

                                final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_PROMOTION_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);

                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 30;
                                data.name = "Promotion Data";
                                publishProgress(data);
                            }

                            geotaglist = database.getinsertGeotaggingData(coverageBeanlist.get(i).getStoreId());
                            String geo_xml = "";
                            boolean geotag_status = false;
                            if (geotaglist.size() > 0) {
                                for (int j = 0; j < geotaglist.size(); j++) {

                                    if (!geotaglist.get(j).getStatus().equals("Y")) {
                                        geotag_status = true;
                                        String onXML1 = "[GeoTag_DATA][STORE_ID]"
                                                + geotaglist.get(j).getStoreid()
                                                + "[/STORE_ID]"
                                                + "[LATTITUDE]"
                                                + geotaglist.get(j).getLatitude()
                                                + "[/LATTITUDE]"
                                                + "[LONGITUDE]"
                                                + geotaglist.get(j).getLongitude()
                                                + "[/LONGITUDE]"
                                                + "[FRONT_IMAGE]"
                                                + geotaglist.get(j).getUrl1()
                                                + "[/FRONT_IMAGE]"
                                                + "[CREATED_BY]" + username
                                                + "[/CREATED_BY][/GeoTag_DATA]";

                                        geo_xml = geo_xml + onXML1;

                                    }

                                }
                                if (geotag_status) {
                                    geo_xml = "[DATA]" + geo_xml + "[/DATA]";
                                    request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                    request.addProperty("MID", "0");
                                    request.addProperty("KEYS", "GEOTAG_DATA");
                                    request.addProperty("USERNAME", username);
                                    request.addProperty("XMLDATA", geo_xml);
                                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                    envelope.dotNet = true;
                                    envelope.setOutputSoapObject(request);
                                    androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                    androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                    result = (Object) envelope.getResponse();
                                    if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                                    } else {
                                        return CommonString.METHOD_UPLOAD_XML;

                                    }
                                }

                            }


                            //Paid Visibility Data
                            final_xml = "";
                            String paid_visibility = "";
                            onXML = "";
                            paidVisibility = database.getAssetUploadData(coverageBeanlist.get(i).getStoreId());
                            if (paidVisibility.size() > 0) {
                                for (int j = 0; j < paidVisibility.size(); j++) {

                                    onXML = "[LOREAL_PAID_VISIBILITY]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[CATEGORY_CD]" + paidVisibility.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                            + "[ASSET_CD]" + paidVisibility.get(j).getAsset_cd() + "[/ASSET_CD]"
                                            + "[PRESENT]" + paidVisibility.get(j).getPresent() + "[/PRESENT]"
                                            + "[ASSET_REASON_CD]" + paidVisibility.get(j).getReason_cd() + "[/ASSET_REASON_CD]"
                                            + "[IMAGE]" + paidVisibility.get(j).getImg() + "[/IMAGE]"
                                            + "[/LOREAL_PAID_VISIBILITY]";

                                    paid_visibility = paid_visibility + onXML;
                                }
                                final String sos_xml = "[DATA]" + paid_visibility + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_PAID_VISIBILITY");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();

                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 40;
                                data.name = "Paid Visibility Data";
                                publishProgress(data);

                            }

                            final_xml = "";
                            onXML = "";
                            paid_visibility = "";
                            String valueex="";
                            additionalVisibilityData = database.getinsertedMarketIntelligenceData(coverageBeanlist.get(i).getStoreId(), coverageBeanlist.get(i).getVisitDate());
                            if (additionalVisibilityData.size() > 0) {
                                for (int j = 0; j < additionalVisibilityData.size(); j++) {
                                    valueex="";
                                    boolean exist=additionalVisibilityData.get(j).isExists();
                                    if (exist){
                                        valueex="1";
                                    }else {
                                        valueex="0";
                                    }
                                    onXML = "[LOREAL_ADDITIONAL_VISIBILITY]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[BRAND_CD]" + additionalVisibilityData.get(j).getCompany_cd() + "[/BRAND_CD]"
                                            + "[DISPLAY_CD]" + additionalVisibilityData.get(j).getCategory_cd() + "[/DISPLAY_CD]"
                                            + "[PHOTO]" + additionalVisibilityData.get(j).getPhoto() + "[/PHOTO]"
                                            + "[REMARK]" + additionalVisibilityData.get(j).getRemark() + "[/REMARK]"
                                            + "[EXIST]" +valueex + "[/EXIST]"
                                            + "[/LOREAL_ADDITIONAL_VISIBILITY]";

                                    paid_visibility = paid_visibility + onXML;
                                }
                                final String sos_xml = "[DATA]" + paid_visibility + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_ADDITIONAL_VISIBILITY");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 50;
                                data.name = "ADDITIONAL VISIBILITY Data";
                                publishProgress(data);

                            }

                          /*  final_xml = "";
                            onXML = "";
                            paid_visibility = "";
                            sampledData = database.getinsertedsampledData(coverageBeanlist.get(i).getStoreId(), coverageBeanlist.get(i).getVisitDate());
                            if (sampledData.size() > 0) {
                                for (int j = 0; j < sampledData.size(); j++) {

                                    onXML = "[LOREAL_SAMPLED_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                            + "[SKU_CD]" + sampledData.get(j).getSku_cd() + "[/SKU_CD]"
                                            + "[CATEGORY_CD]" + sampledData.get(j).getCategory_cd() + "[/CATEGORY_CD]"
                                            + "[SAMPLED]" + sampledData.get(j).getSampled() + "[/SAMPLED]"
                                            + "[PHOTO]" + sampledData.get(j).getSampled_img() + "[/PHOTO]"
                                            + "[FEEDBACK]" + sampledData.get(j).getFeedback() + "[/FEEDBACK]"
                                            + "[/LOREAL_SAMPLED_DATA]";

                                    paid_visibility = paid_visibility + onXML;
                                }
                                final String sos_xml = "[DATA]" + paid_visibility + "[/DATA]";
                                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                                request.addProperty("XMLDATA", sos_xml);
                                request.addProperty("KEYS", "LOREAL_SAMPLED_DATA");
                                request.addProperty("USERNAME", username);
                                request.addProperty("MID", mid);
                                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                                result = (Object) envelope.getResponse();
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    isError = true;
                                }
                                data.value = 49;
                                data.name = "Market Intelligence Data";
                                publishProgress(data);

                            }*/

                            // SET COVERAGE STATUS
                            String final_xml1 = "";
                            String onXML1 = "";
                            onXML1 = "[COVERAGE_STATUS]"
                                    + "[STORE_ID]" + coverageBeanlist.get(i).getStoreId() + "[/STORE_ID]"
                                    + "[VISIT_DATE]" + coverageBeanlist.get(i).getVisitDate() + "[/VISIT_DATE]"
                                    + "[USER_ID]" + coverageBeanlist.get(i).getUserId() + "[/USER_ID]"
                                    + "[STATUS]" + CommonString.KEY_D + "[/STATUS]"
                                    + "[/COVERAGE_STATUS]";

                            final_xml1 = final_xml1 + onXML1;

                            final String sos_xml = "[DATA]" + final_xml1 + "[/DATA]";

                            SoapObject request1 = new SoapObject(CommonString.NAMESPACE, CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS);
                            request1.addProperty("onXML", sos_xml);
                            SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope1.dotNet = true;
                            envelope1.setOutputSoapObject(request1);

                            HttpTransportSE androidHttpTransport1 = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport1.call(CommonString.SOAP_ACTION + CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS, envelope1);
                            Object result1 = (Object) envelope1.getResponse();
                            if (result1.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                database.open();
                                database.updateStoreStatusOnLeave(coverageBeanlist.get(i).getStoreId(), coverageBeanlist.get(i).getVisitDate(), CommonString.KEY_D);
                                database.updateCoverageStatus(coverageBeanlist.get(i).getMID(), CommonString.KEY_D);

                            }
                            if (!result1.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                // return "COVERAGE_STATUS";
                                isError = true;
                            }
                            data.value = 100;
                            data.name = "Covrage Status";
                            publishProgress(data);
                            // resultFinal = result.toString();
                        }
                    }

                }
                File dir = new File(CommonString.FILE_PATH);
                ArrayList<String> list = new ArrayList();
                list = getFileNames(dir.listFiles());
                if (list.size() > 0) {
                    for (int i1 = 0; i1 < list.size(); i1++) {
                        if (list.get(i1).contains("_STORE_IMG_SELFIE_") || list.get(i1).contains("_STORE_IMG_GROOMING_") || list.get(i1).contains("_NONWORKING_IMG_") || list.get(i1).contains("_CHECKOUT_IMG_")) {
                            File originalFile = new File(Path + list.get(i1));
                            result = UploadImage(originalFile.getName(), "StoreImages");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 70;
                            data.name = "Store Images";
                            publishProgress(data);
                        }else   if (list.get(i1).contains("_PROMO_")) {
                            File originalFile = new File(Path + list.get(i1));
                            result = UploadImage( originalFile.getName(), "PromotionImages");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 75;
                            data.name = "Promotion Images";
                            publishProgress(data);
                        }else if (list.get(i1).contains("_PAID_VISIBILITY_IMG_")) {
                            File originalFile = new File(Path + list.get(i1));
                            result = UploadImage( originalFile.getName(), "PaidVisibility");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 80;
                            data.name = "PaidVisibility Images";
                            publishProgress(data);
                        }else if (list.get(i1).contains("_ADDITIONAL_VIS_")) {
                            File originalFile = new File(Path + list.get(i1));
                            result = UploadImage( originalFile.getName(), "AdditionalVisibility");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 85;
                            data.name = "AdditionalVisibility Images";
                            publishProgress(data);
                        }else if (list.get(i1).contains("_OPENING_STOCK_IMAGE_")) {
                            File originalFile = new File(Path + list.get(i1));

                            result = UploadImage( originalFile.getName(), "StockImages");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 90;
                            data.name = "Stock Images";
                            publishProgress(data);
                        }else if (list.get(i1).contains("_SHARE_OF_SHELF_IMAGE_")) {
                            File originalFile = new File(Path + list.get(i1));

                            result = UploadImage( originalFile.getName(), "ShareOfShelf");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 95;
                            data.name = "ShareOfShelf Images";
                            publishProgress(data);
                        }
                        else if (list.get(i1).contains("_GeoTag_")) {
                            File originalFile = new File(Path + list.get(i1));

                            result = UploadImage( originalFile.getName(), "GeotagImages");
                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return result.toString();
                            }
                            data.value = 97;
                            data.name = "Geotag Images";
                            publishProgress(data);
                        }

                    }
                    String response = updateStatus();
                    if (!response.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                        return CommonString.KEY_FAILURE;
                    } else {
                        return CommonString.KEY_SUCCESS;
                    }

                }else {
                    String response = updateStatus();
                    if (response.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                        return CommonString.KEY_SUCCESS;
                    } else {
                        return AlertMessage.MESSAGE_SOCKETEXCEPTION;
                    }
                }

            } catch (MalformedURLException e) {
                up_success_flag = false;
                exceptionMessage = e.toString();

            } catch (IOException e) {
                up_success_flag = false;
                exceptionMessage = e.toString();

            } catch (Exception e) {
                Crashlytics.logException(e);

                up_success_flag = false;
                exceptionMessage = e.toString();

            }

           /* if (up_success_flag) {*/
            if (up_success_flag == true) {
               /* return resultFinal;*/
                return CommonString.KEY_SUCCESS;
            } else {
                return exceptionMessage;
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
            // database.deleteAllTables();
            if (isError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutNUpload.this);
                builder.setTitle("Parinaam");
                builder.setMessage("Uploaded Successfully with some problem ").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if (result.equals(CommonString.KEY_SUCCESS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutNUpload.this);
                    builder.setTitle("Parinaam");
                    builder.setMessage(AlertMessage.MESSAGE_UPLOAD_DATA).setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else if (result.equals(CommonString.KEY_FAILURE) || !result.equals("")) {
                    AlertMessage message = new AlertMessage(CheckoutNUpload.this, CommonString.ERROR + result, "success", null);
                    message.showMessage();
                }
            }

        }
    }


    public String UploadImage(String path, String folder_path) throws Exception {
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
        Bitmap bitmap = BitmapFactory.decodeFile(Path + path, o2);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);
        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);
        String[] split = path.split("/");
        String path1 = split[split.length - 1];
        request.addProperty("img", ba1);
        request.addProperty("name", path1);
        request.addProperty("FolderName", folder_path);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);

        Object result = envelope.getResponse();
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
            failureGetterSetter = failureXMLHandler.getFailureGetterSetter();
            if (failureGetterSetter.getStatus().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }
        } else {
            new File(Path + path).delete();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
            editor.commit();
        }

        return result.toString();
    }

    private class BackgroundTask extends AsyncTask<Void, Data, String> {
        private Context context;

        BackgroundTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("Uploading Checkout Data");
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... params) {
            try {
                data = new Data();
                data.value = 20;
                data.name = "Checked out Data Uploading";
                publishProgress(data);
                if (coverageBean.size() > 0) {
                    String onXML = "[STORE_CHECK_OUT_STATUS]"
                            + "[USER_ID]" + username + "[/USER_ID]"
                            + "[STORE_ID]" + coverageBean.get(0).getStoreId() + "[/STORE_ID]"
                            + "[LATITUDE]" + "0.0" + "[/LATITUDE]"
                            + "[LOGITUDE]" + "0.0" + "[/LOGITUDE]"
                            + "[CHECKOUT_DATE]" + coverageBean.get(0).getVisitDate() + "[/CHECKOUT_DATE]"
                            + "[CHECK_OUTTIME]" + store_out_time + "[/CHECK_OUTTIME]"
                            + "[CHECK_INTIME]" + coverageBean.get(0).getInTime() + "[/CHECK_INTIME]"
                            + "[CREATED_BY]" + username + "[/CREATED_BY]"
                            + "[CHECKOUT_IMAGE]" + coverageBean.get(0).getCheckout_img() + "[/CHECKOUT_IMAGE]"
                            + "[/STORE_CHECK_OUT_STATUS]";

                    final String sos_xml = "[DATA]" + onXML + "[/DATA]";

                    SoapObject request = new SoapObject(CommonString.NAMESPACE, "Upload_Store_ChecOut_Status");
                    request.addProperty("onXML", sos_xml);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                    androidHttpTransport.call(CommonString.SOAP_ACTION + "Upload_Store_ChecOut_Status", envelope);
                    Object result = (Object) envelope.getResponse();
                    if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS_chkout)) {
                        database.open();
                      //  db.updateCoverageStoreOutTime(store_id, specificCdata.get(0).getVisitDate(),img_str, getCurrentTime(), CommonString.KEY_C);
                        database.updateCoverageStoreOutTime(coverageBean.get(0).getStoreId(), coverageBean.get(0).getVisitDate(),coverageBean.get(0).getCheckout_img(), store_out_time, CommonString.KEY_C);
                        database.updateStoreStatusOnCheckout(coverageBean.get(0).getStoreId(), coverageBean.get(0).getVisitDate(), CommonString.KEY_C);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(CommonString.KEY_STOREVISITED, "");
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                        editor.commit();
                        data.value = 100;
                        data.name = "Checkout Done";
                        publishProgress(data);
                        return CommonString.KEY_SUCCESS;
                    } else {
                        if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                            return CommonString.METHOD_Checkout_StatusNew;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                final AlertMessage message = new AlertMessage(CheckoutNUpload.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (IOException e) {
                final AlertMessage message = new AlertMessage(CheckoutNUpload.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);
                // counter++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (Exception e) {
                Crashlytics.logException(e);

                final AlertMessage message = new AlertMessage(CheckoutNUpload.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            }

            return "";
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
                new UploadTask(CheckoutNUpload.this).execute();
            } else if (result.equals("")) {
                Toast.makeText(getApplicationContext(), "Network Error Try Again", Toast.LENGTH_SHORT).show();
                finish();
            } else if (!result.equals("")) {
                Toast.makeText(getApplicationContext(), "Network Error Try Again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    class Data {
        int value;
        String name;
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":" + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);
        return intime;

    }


    private String updateStatus() throws IOException, XmlPullParserException {
        String final_xml = "";
        String onXML = "";
        Object result1 = "";
        try {
            database.open();
            coverageBeanlist = database.getCoveragePreviousData(visit_date);
            for (int i = 0; i < coverageBeanlist.size(); i++) {
                journeyPlanGetterSetter = database.getStoreStatus(coverageBeanlist.get(i).getStoreId());
                if (!journeyPlanGetterSetter.getUploadStatus().get(0).equalsIgnoreCase(CommonString.KEY_U)){
                    String final_xml1 = "";
                    String onXML1 = "";
                    onXML1 = "[COVERAGE_STATUS]"
                            + "[STORE_ID]" + coverageBeanlist.get(i).getStoreId() + "[/STORE_ID]"
                            + "[VISIT_DATE]" + coverageBeanlist.get(i).getVisitDate() + "[/VISIT_DATE]"
                            + "[USER_ID]" + coverageBeanlist.get(i).getUserId() + "[/USER_ID]"
                            + "[STATUS]" + CommonString.KEY_U + "[/STATUS]"
                            + "[/COVERAGE_STATUS]";

                    final_xml1 = final_xml1 + onXML1;

                    final String sos_xml = "[DATA]" + final_xml1 + "[/DATA]";

                    SoapObject request1 = new SoapObject(CommonString.NAMESPACE, CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS);
                    request1.addProperty("onXML", sos_xml);
                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope1.dotNet = true;
                    envelope1.setOutputSoapObject(request1);

                    HttpTransportSE androidHttpTransport1 = new HttpTransportSE(CommonString.URL);
                    androidHttpTransport1.call(CommonString.SOAP_ACTION + CommonString.MEHTOD_UPLOAD_COVERAGE_STATUS, envelope1);
                    result1 = (Object) envelope1.getResponse();
                    if (result1.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                        database.open();
                        database.updateStoreStatusOnLeave(coverageBeanlist.get(i).getStoreId(), coverageBeanlist.get(i).getVisitDate(), CommonString.KEY_U);
                        database.updateCoverageStatus(coverageBeanlist.get(i).getMID(), CommonString.KEY_U);
                        database.deleteSpecificStoreData(coverageBeanlist.get(i).getStoreId());
                    }
                    if (!result1.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                        isError = true;
                    }


                    data.value = 100;
                    data.name = "Covrage Status";
                    //publishProgress(data);
                    resultFinal = result1.toString();
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);

            e.printStackTrace();
        }

        return result1.toString();

    }
    public ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }


}
