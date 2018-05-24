package com.cpm.download;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.dailyentry.IncentiveActivity;
import com.cpm.lorealpromoter.R;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.TableBean;
import com.cpm.fragment.MainFragment;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.AssetMasterGetterSetter;
import com.cpm.xmlGetterSetter.AssetNonReasonGetterSetter;
import com.cpm.xmlGetterSetter.Audit_QuestionGetterSetter;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.CategoryMasterGetterSetter;
import com.cpm.xmlGetterSetter.CompanyGetterSetter;
import com.cpm.xmlGetterSetter.FocusPerformanceGetterSetter;
import com.cpm.xmlGetterSetter.IncentiveGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetGetterSetter;
import com.cpm.xmlGetterSetter.MappingAvailabilityGetterSetter;
import com.cpm.xmlGetterSetter.MappingPromotionGetterSetter;
import com.cpm.xmlGetterSetter.MappingSosGetterSetter;
import com.cpm.xmlGetterSetter.NonPromotionReasonGetterSetter;
import com.cpm.xmlGetterSetter.NonWorkingReasonGetterSetter;
import com.cpm.xmlGetterSetter.PayslipGetterSetter;
import com.cpm.xmlGetterSetter.PerformanceGetterSetter;
import com.cpm.xmlGetterSetter.PromoTypeGetterSetter;
import com.cpm.xmlGetterSetter.SkuMasterGetterSetter;
import com.cpm.xmlGetterSetter.SubCategoryGetterSetter;
import com.cpm.xmlGetterSetter.TodayQuestionGetterSetter;
import com.cpm.xmlHandler.XMLHandlers;
import com.crashlytics.android.Crashlytics;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

public class CompleteDownloadActivity extends AppCompatActivity {
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private Data data;
    int eventType;

    JourneyPlanGetterSetter jcpgettersetter;
    SkuMasterGetterSetter skumastergettersetter;
    MappingAvailabilityGetterSetter mappingstockgettersetter;
    MappingPromotionGetterSetter mappingprormotgettersetter;
    MappingAssetGetterSetter mappingassetgettersetter;
    AssetMasterGetterSetter assetmastergettersetter;
    CompanyGetterSetter companyGetterSetter;
    BrandGetterSetter brandGetterSetter;
    SubCategoryGetterSetter subCategoryGetterSetter;
    PerformanceGetterSetter  performanceGetterSetter;
    FocusPerformanceGetterSetter focusperformanceGetterSetter;
    NonWorkingReasonGetterSetter nonworkinggettersetter;
    CategoryMasterGetterSetter categorygettersetter;
    PayslipGetterSetter payslipGetterSetter;
    AssetNonReasonGetterSetter assetNonReasonGetterSetter;
    NonPromotionReasonGetterSetter nonPromotionReasonGetterSetter;
    Audit_QuestionGetterSetter audit_questionGetterSetter;
    PromoTypeGetterSetter promoTypeGetterSetter;
    IncentiveGetterSetter incentiveGetterSetter;
    MappingSosGetterSetter mappingSosGetterSetter;
    TodayQuestionGetterSetter todayQuestionGetterSetter;
    GSKDatabase db;
    TableBean tb;
    String _UserId, visit_date;
    private SharedPreferences preferences;
    boolean promotion_flag = true;
    boolean asset_flag = true;
    boolean success_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        tb = new TableBean();

        db = new GSKDatabase(this);
        db.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new BackgroundTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // db.open();
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MainFragment cartfrag = new MainFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, cartfrag)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class Data {
        int value;
        String name;
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
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom);
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {
            String resultHttp = "";

            try {
                data = new Data();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                // JCP
                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "JOURNEY_PLAN");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result_jcp = (Object) envelope.getResponse();

                if (result_jcp.toString() != null) {
                    xpp.setInput(new StringReader(result_jcp.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    jcpgettersetter = XMLHandlers.JCPXMLHandler(xpp, eventType);
                    String jcpTable = jcpgettersetter.getTable_journey_plan();
                    TableBean.setjcptable(jcpTable);

                    if (jcpgettersetter.getStore_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.name = "JCP Data Downloading";
                        publishProgress(data);
                    } else {
                        return " JOURNEY_PLAN";
                    }

                }

                // Store List Master
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "SKU_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    skumastergettersetter = XMLHandlers.storeListXML(xpp, eventType);
                    if (skumastergettersetter.getSku_cd().size() > 0) {
                        String skutable = skumastergettersetter.getSku_master_table();
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setSkumastertable(skutable);

                    } else {
                        return "SKU_MASTER";
                    }
                    data.value = 20;
                    data.name = "Store Data Download";
                }
                publishProgress(data);

                // Company Master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "COMPANY_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultcompany = (Object) envelope.getResponse();

                if (resultcompany.toString() != null) {
                    xpp.setInput(new StringReader(resultcompany.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    companyGetterSetter = XMLHandlers.comapnyMasterXML(xpp, eventType);
                    String companytable = companyGetterSetter.getCompany_master_table();
                    TableBean.setCompanytable(companytable);
                    if (companyGetterSetter.getCompany_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "COMPANY_MASTER";
                    }
                    data.value = 25;
                    data.name = "Company Master Data Downloading";
                }
                publishProgress(data);


                // Brand Master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "BRAND_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultbrand = (Object) envelope.getResponse();

                if (resultbrand.toString() != null) {
                    xpp.setInput(new StringReader(resultbrand.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    brandGetterSetter = XMLHandlers.brandMasterXML(xpp, eventType);
                    String brandtable = brandGetterSetter.getBrand_master_table();
                    TableBean.setBrandtable(brandtable);
                    if (brandGetterSetter.getBrand_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "BRAND_MASTER";
                    }
                    data.value = 30;
                    data.name = "Brand Master Data Downloading";
                }
                publishProgress(data);


                // UPENDRA  SUB_CATEGORY_MASTER data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "SUB_CATEGORY_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultsubcategorymaster = (Object) envelope.getResponse();

                if (resultsubcategorymaster.toString() != null) {
                    xpp.setInput(new StringReader(resultsubcategorymaster.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    subCategoryGetterSetter = XMLHandlers.subcetegorymasterXML(xpp, eventType);
                    String subcategorytable = subCategoryGetterSetter.getSub_category_master_table();
                    TableBean.setSubcetegory_table(subcategorytable);
                    if (subCategoryGetterSetter.getCATEGORY_CD().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "SUB_CATEGORY_MASTER";
                    }
                    data.value = 30;
                    data.name = "SUB_CATEGORY_MASTER Data Downloading";
                }
                publishProgress(data);



                //Category master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "CATEGORY_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultcategorymaster = (Object) envelope.getResponse();

                if (resultcategorymaster.toString() != null) {
                    xpp.setInput(new StringReader(resultcategorymaster.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    categorygettersetter = XMLHandlers.categoryMasterXML(xpp, eventType);
                    String categorytable = categorygettersetter.getCategory_master_table();
                    TableBean.setCategorymastertable(categorytable);
                    if (categorygettersetter.getCategory_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "CATEGORY_MASTER";
                    }
                    data.value = 40;
                    data.name = "Category Master Downloading";
                }
                publishProgress(data);




                //Category master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "STORE_SALES_PERFORMANCE");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object performanceMaster = (Object) envelope.getResponse();

                if (performanceMaster.toString() != null) {
                    xpp.setInput(new StringReader(performanceMaster.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    performanceGetterSetter = XMLHandlers.performanceXML(xpp, eventType);
                    String performanceTable = performanceGetterSetter.getPerformance_table();
                    TableBean.setPerformancetable(performanceTable);
                    if (performanceGetterSetter.getSTORE_CD().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {

                    }
                    data.value = 45;
                    data.name = "Performance Data Downloading";
                }
                publishProgress(data);



                        //STORE_FOCUS_SKU_PERFORMANCE master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "STORE_FOCUS_SKU_PERFORMANCE");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                Object focusperformanceMaster = (Object) envelope.getResponse();

                if (focusperformanceMaster.toString() != null) {
                    xpp.setInput(new StringReader(focusperformanceMaster.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    focusperformanceGetterSetter = XMLHandlers.focusperformanceXML(xpp, eventType);
                    String focusperformanceTable = focusperformanceGetterSetter.getFocusperformance_table();
                    TableBean.setFocusperformancetable(focusperformanceTable);
                    if (focusperformanceGetterSetter.getSTORE_CD().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {

                    }
                    data.value = 46;
                    data.name = "Focus Performance Data Downloading";
                }
                publishProgress(data);



                // Mapping Availability data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_STOCK");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                Object resultnonWorking = (Object) envelope.getResponse();
                if (resultnonWorking.toString() != null) {
                    xpp.setInput(new StringReader(resultnonWorking.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    mappingstockgettersetter = XMLHandlers.mappingavailXML(xpp, eventType);
                    if (mappingstockgettersetter.getMapping_avail_table() != null) {
                        String mappingtable = mappingstockgettersetter.getMapping_avail_table();
                        TableBean.setMappingavailtable(mappingtable);
                    }
                    if (mappingstockgettersetter.getSku_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 50;
                        data.name = "MAPPING_STOCK data Downloading";
                    }
                }
                publishProgress(data);


                //MAPPING_SOS
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_SOS");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                Object resultmappingsos = (Object) envelope.getResponse();
                if (resultmappingsos.toString() != null) {
                    xpp.setInput(new StringReader(resultmappingsos.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    mappingSosGetterSetter = XMLHandlers.mappingSOSXML(xpp, eventType);
                    if (mappingSosGetterSetter.getMapping_sos_table() != null) {
                        String mappingtable = mappingSosGetterSetter.getMapping_sos_table();
                        TableBean.setMappingsos_table(mappingtable);
                    }
                    if (mappingSosGetterSetter.getBrand_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 52;
                        data.name = "MAPPING_SOS data Downloading";
                    }
                }
                publishProgress(data);




                // Mapping Promotion data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_PROMOTION");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultmapping = (Object) envelope.getResponse();

                if (resultmapping.toString() != null) {
                    xpp.setInput(new StringReader(resultmapping.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingprormotgettersetter = XMLHandlers.mappingpromotXML(xpp, eventType);
                    String mappingtable = mappingprormotgettersetter.getMapping_promotion_table();
                    TableBean.setMappingpromotable(mappingtable);
                    if (mappingprormotgettersetter.getPid().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 55;
                        data.name = "Mapping Promotion Data Downloading";
                    } else {
                        promotion_flag = false;
                    }
                }
                publishProgress(data);


                // Mapping Asset data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_ASSET");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultmappingasst = (Object) envelope.getResponse();

                if (resultmappingasst.toString() != null) {
                    xpp.setInput(new StringReader(resultmappingasst.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    mappingassetgettersetter = XMLHandlers.mappingassetXML(xpp, eventType);

                    if (mappingassetgettersetter.getMapping_asset_table() != null) {
                        String mappingtable = mappingassetgettersetter.getMapping_asset_table();
                        TableBean.setMappingassettable(mappingtable);
                    }
                    if (mappingassetgettersetter.getStore_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 60;
                        data.name = "Mapping Asset Data Downloading";
                    } else {
                        //return "MAPPING_ASSET_NEW";
                       // asset_flag = false;
                    }
                }
                publishProgress(data);


                // Asset Master data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "ASSET_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultasset = (Object) envelope.getResponse();

                if (resultasset.toString() != null) {
                    xpp.setInput(new StringReader(resultasset.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    assetmastergettersetter = XMLHandlers.assetMasterXML(xpp, eventType);
                    String assettable = assetmastergettersetter.getAsset_master_table();
                    TableBean.setAssetmastertable(assettable);
                    if (assetmastergettersetter.getAsset_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "ASSET_MASTER";
                    }
                    data.value = 70;
                    data.name = "Asset Master Data Downloading";
                }
                publishProgress(data);
                //Non Working Reason data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "NON_WORKING_REASON");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultnonworking = (Object) envelope.getResponse();

                if (resultnonworking.toString() != null) {
                    xpp.setInput(new StringReader(resultnonworking.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    nonworkinggettersetter = XMLHandlers.nonWorkinReasonXML(xpp, eventType);
                    String nonworkingtable = nonworkinggettersetter.getNonworking_table();
                    TableBean.setNonworkingtable(nonworkingtable);
                    if (nonworkinggettersetter.getReason_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "NON_WORKING_REASON";
                    }
                    data.value = 90;
                    data.name = "Non Working Reason Downloading";

                }
                publishProgress(data);


                //Payment Slip Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "EMP_SALARY");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result1 = (Object) envelope.getResponse();

                if (result1.toString() != null) {
                    xpp.setInput(new StringReader(result1.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    payslipGetterSetter = XMLHandlers.payslipXML(xpp, eventType);
                    if (payslipGetterSetter.getEmp_salary_table() != null) {
                        String payslipTable = payslipGetterSetter.getEmp_salary_table();
                        TableBean.setEmp_payslip_table(payslipTable);
                    } else {
                        return "EMP_SALARY";
                    }
                    data.value = 95;
                    data.name = "EMP_SALARY Downloading";
                }



                //PERFORMANCE_OQAD Slip Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "PERFORMANCE_OQAD");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result1 = (Object) envelope.getResponse();

                if (result1.toString() != null) {
                    xpp.setInput(new StringReader(result1.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    todayQuestionGetterSetter = XMLHandlers.todayquesXML(xpp, eventType);
                    if (todayQuestionGetterSetter.getToday_question_table() != null) {
                        String payslipTable = todayQuestionGetterSetter.getToday_question_table();
                        TableBean.setTodayquestion_ans_table(payslipTable);
                    } else {
                        return "PERFORMANCE_OQAD";
                    }
                    data.value = 95;
                    data.name = "PERFORMANCE_OQAD Downloading";
                }

                //Incentive Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "EMP_INCENTIVE");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result2 = (Object) envelope.getResponse();

                if (result2.toString() != null) {
                    xpp.setInput(new StringReader(result2.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    incentiveGetterSetter = XMLHandlers.incentiveXML(xpp, eventType);
                    if (incentiveGetterSetter.getIncentive_table() != null) {
                        String incentiveTable = incentiveGetterSetter.getIncentive_table();
                        TableBean.setIncentive_table(incentiveTable);
                    } else {
                        return "EMP_INCENTIVE";
                    }
                    data.value = 96;
                    data.name = "EMP_INCENTIVE Downloading";
                }

                //AUDIT_QUESTION_CATEGORYWISE
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "AUDIT_QUESTION_CATEGORYWISE");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                Object auditQuestionResult_category = (Object) envelope.getResponse();
                if (auditQuestionResult_category.toString() != null) {
                    xpp.setInput(new StringReader(auditQuestionResult_category.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    audit_questionGetterSetter = XMLHandlers.audit_QuestionXML(xpp, eventType);
                    String auditQuestionTable = audit_questionGetterSetter.getAudit_question_table();
                    TableBean.setAudit_question_table(auditQuestionTable);
                    data.value = 100;
                    data.name = "AUDIT_QUESTION_CATEGORYWISE Downloading";
                }

                //NON_ASSET_REASON FOR ASSET Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "NON_ASSET_REASON");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result1 = (Object) envelope.getResponse();
                if (result1.toString() != null) {
                    xpp.setInput(new StringReader(result1.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    assetNonReasonGetterSetter = XMLHandlers.assetnonreasonXML(xpp, eventType);
                    String payslipTable = assetNonReasonGetterSetter.getNonAssetReasonTable();
                    TableBean.setAssetnoreasonTable(payslipTable);
                    if (assetNonReasonGetterSetter.getAreason_cd() != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "NON_ASSET_REASON";
                    }

                    data.value = 96;
                    data.name = "NON_ASSET_REASON Downloading";
                }

                //NON_PROMOTION_REASON FOR ASSET Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "NON_PROMOTION_REASON");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result1 = (Object) envelope.getResponse();
                if (result1.toString() != null) {
                    xpp.setInput(new StringReader(result1.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    nonPromotionReasonGetterSetter = XMLHandlers.promotionnonreasonXML(xpp, eventType);
                    String payslipTable = nonPromotionReasonGetterSetter.getNonpromotionreasonTable();
                    TableBean.setNonpromotionnoreasonTable(payslipTable);
                    if (nonPromotionReasonGetterSetter.getPreason_cd() != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "NON_PROMOTION_REASON";
                    }
                    data.value = 98;
                    data.name = "NON_PROMOTION_REASON Downloading";
                }


                //PROMO_TYPE_MASTER FOR ASSET Data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "PROMO_TYPE_MASTER");
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result1 = (Object) envelope.getResponse();
                if (result1.toString() != null) {
                    xpp.setInput(new StringReader(result1.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    promoTypeGetterSetter = XMLHandlers.promotypeXML(xpp, eventType);
                    String payslipTable = promoTypeGetterSetter.getProtypeTable();
                    TableBean.setPromoTypeTable(payslipTable);
                    if (promoTypeGetterSetter.getPromoType_cd() != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        return "PROMO_TYPE_MASTER";
                    }
                    data.value = 99;
                    data.name = "PROMO_TYPE_MASTER Downloading";
                }

                //Database insert method calling

                db.open();
                db.insertJCPData(jcpgettersetter);
                db.insertSkuMasterData(skumastergettersetter);
                db.insertMappingstockData(mappingstockgettersetter);
                if (promotion_flag) {
                    db.insertMappingPromotionData(mappingprormotgettersetter);
                } else {
                    db.deletePromotionMapping();
                }

                db.insertPerformanceData(performanceGetterSetter);
                db.insertfocusPerformanceData(focusperformanceGetterSetter);
                db.insertMappingAssetData(mappingassetgettersetter);
                db.insertAssetMasterData(assetmastergettersetter);
                db.insertCompanyMasterData(companyGetterSetter);
                db.insertBrandMasterData(brandGetterSetter);
                db.insertSubCetegoryMasterData(subCategoryGetterSetter);
                db.insertCategoryMasterData(categorygettersetter);
                db.insertNonWorkingReasonData(nonworkinggettersetter);
                if (payslipGetterSetter != null) {
                    db.insertPaySlipdata(payslipGetterSetter);
                } else {
                    db.deletePaySlipData();
                }
                //Audit
                db.insertAuditQuestionData(audit_questionGetterSetter);

                //asset noreason
                db.insertAssetNonReasonData(assetNonReasonGetterSetter);
                db.insertNonPromotionReasonData(nonPromotionReasonGetterSetter);
                db.insertpromoTypeData(promoTypeGetterSetter);
                db.insertIncentiveTypeData(incentiveGetterSetter);
                db.insertMappingSosData(mappingSosGetterSetter);
                db.insertQuestionAnsData(todayQuestionGetterSetter);


                data.value = 100;
                data.name = "Finishing";
                publishProgress(data);

                return resultHttp;

            } catch (MalformedURLException e) {
                success_flag = false;

                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (IOException e) {
                success_flag = false;
                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (Exception e) {
                Crashlytics.logException(e);
                success_flag = false;
                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_EXCEPTION , "download", e);
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

            if (success_flag) {
                if (result.equals(CommonString.KEY_SUCCESS)) {
                    AlertMessage message = new AlertMessage(CompleteDownloadActivity.this, AlertMessage.MESSAGE_DOWNLOAD, "success", null);
                    message.showMessage();
                }else if (result.equalsIgnoreCase("JOURNEY_PLAN")){
                    AlertMessage message = new AlertMessage(CompleteDownloadActivity.this, AlertMessage.MESSAGE_JCP_FALSE_NOJCP, "success", null);
                    message.showMessage();
                } else {
                    AlertMessage message = new AlertMessage(CompleteDownloadActivity.this, AlertMessage.MESSAGE_JCP_FALSE_NOJCP + result, "success", null);
                    message.showMessage();
                }
            }

        }

    }
}
