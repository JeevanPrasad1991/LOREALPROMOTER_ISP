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
import com.cpm.capitalfoods.R;
import com.cpm.database.GSKDatabase;
import com.cpm.delegates.TableBean;
import com.cpm.fragment.MainFragment;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.AssetChecklistGetterSetter;
import com.cpm.xmlGetterSetter.AssetMasterGetterSetter;
import com.cpm.xmlGetterSetter.BrandGetterSetter;
import com.cpm.xmlGetterSetter.CategoryMasterGetterSetter;
import com.cpm.xmlGetterSetter.CompanyGetterSetter;
import com.cpm.xmlGetterSetter.DesignationGetterSetter;
import com.cpm.xmlGetterSetter.Deviation_Reason;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetChecklistGetterSetter;
import com.cpm.xmlGetterSetter.MappingAssetGetterSetter;
import com.cpm.xmlGetterSetter.MappingAvailabilityGetterSetter;
import com.cpm.xmlGetterSetter.MappingPromotionGetterSetter;
import com.cpm.xmlGetterSetter.NonWorkingReasonGetterSetter;
import com.cpm.xmlGetterSetter.PayslipGetterSetter;
import com.cpm.xmlGetterSetter.SkuMasterGetterSetter;
import com.cpm.xmlGetterSetter.Sup_Merchandiser;
import com.cpm.xmlGetterSetter.Sup_Performance;
import com.cpm.xmlGetterSetter.Sup_Window;
import com.cpm.xmlGetterSetter.SupervisorGetterSetter;
import com.cpm.xmlHandler.XMLHandlers;

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
    MappingAvailabilityGetterSetter mappingavailgettersetter;
    MappingPromotionGetterSetter mappingprormotgettersetter;
    MappingAssetGetterSetter mappingassetgettersetter;
    AssetMasterGetterSetter assetmastergettersetter;
    CompanyGetterSetter companyGetterSetter;
    BrandGetterSetter brandGetterSetter;
    NonWorkingReasonGetterSetter nonworkinggettersetter;
    CategoryMasterGetterSetter categorygettersetter;
    AssetChecklistGetterSetter assetChecklistGetterSetter;
    MappingAssetChecklistGetterSetter mappingAssetChecklistGetterSetter;
    PayslipGetterSetter payslipGetterSetter;
    
    SupervisorGetterSetter ditributorlist;
    JourneyPlanGetterSetter journeyplanMerchan;
    Deviation_Reason deviation_Reason;
    Sup_Window supWin;
    Sup_Performance supperformance;
    Sup_Merchandiser supMerchandiser;
    DesignationGetterSetter designation;
    //NonWrkingMasterGetterSetter nonworkingData;
    //StoreListGetterSetter storeTable;

    GSKDatabase db;
    TableBean tb;
    String _UserId;
    private SharedPreferences preferences;

    boolean flag_cold_stock = true;

    boolean promotion_flag = true;
    boolean asset_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mainpage);

        setContentView(R.layout.activity_main_menu);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        tb = new TableBean();
        db = new GSKDatabase(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new BackgroundTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // db.open();
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment cartfrag = new MainFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, cartfrag)
                .commit();

    }

    /*@Override
    protected void onStop() {
        super.onStop();
        //		db.close();
    }*/

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
            //dialog.setTitle("Download Files");
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

                // JCP
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "JOURNEY_PLAN");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    jcpgettersetter = XMLHandlers.JCPXMLHandler(xpp, eventType);

                    if (jcpgettersetter.getStore_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String jcpTable = jcpgettersetter.getTable_journey_plan();
                        TableBean.setjcptable(jcpTable);
                    } else {
                        return "JOURNEY_PLAN";
                    }
                    data.value = 10;
                    data.name = "JCP Data Downloading";
                }
                publishProgress(data);


                // Store List Master
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "SKU_MASTER");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    skumastergettersetter = XMLHandlers.storeListXML(xpp, eventType);

                    if (skumastergettersetter.getSku_cd().size() > 0) {
                        String skutable = skumastergettersetter.getSku_master_table();
                        if (skutable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSkumastertable(skutable);
                        }
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

                    if (companyGetterSetter.getCompany_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String companytable = companyGetterSetter.getCompany_master_table();
                        TableBean.setCompanytable(companytable);
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

                    if (brandGetterSetter.getBrand_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String brandtable = brandGetterSetter.getBrand_master_table();
                        TableBean.setBrandtable(brandtable);
                    } else {
                        return "BRAND_MASTER";
                    }
                    data.value = 30;
                    data.name = "Brand Master Data Downloading";
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

                    if (categorygettersetter.getCategory_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String categorytable = categorygettersetter.getCategory_master_table();
                        TableBean.setCategorymastertable(categorytable);
                    } else {
                        return "CATEGORY_MASTER";
                    }
                    data.value = 40;
                    data.name = "Category Master Downloading";
                }
                publishProgress(data);


                // Mapping Availability data
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_AVAILABILITY");

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

                    mappingavailgettersetter = XMLHandlers.mappingavailXML(xpp, eventType);

                    if (mappingavailgettersetter.getMapping_avail_table() != null) {
                        String mappingtable = mappingavailgettersetter.getMapping_avail_table();
                        TableBean.setMappingavailtable(mappingtable);
                    }

                    if (mappingavailgettersetter.getSku_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 50;
                        data.name = "Mapping Availability data Downloading";
                    }/*else{
                        return "MAPPING_AVAILABILITY";
					}*/
                }
                publishProgress(data);


                // Mapping Promotion data
                /*request = new SoapObject(CommonString.NAMESPACE,CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
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

                    //if (mappingprormotgettersetter.getMapping_promotion_table() != null) {
                        String mappingtable = mappingprormotgettersetter.getMapping_promotion_table();
                        TableBean.setMappingpromotable(mappingtable);
                    //}

                    if (mappingprormotgettersetter.getPid().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        data.value = 55;
                        data.name = "Mapping Promotion Data Downloading";
                    } else {
                        //return "MAPPING_PROMOTION";
                        promotion_flag = false;
                    }
                }
                publishProgress(data);*/


                // Mapping Asset data
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_ASSET_NEW");

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
                        //return "MAPPING_ASSET";
                        asset_flag = false;
                    }
                }
                publishProgress(data);*/


                // Asset Master data
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
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

                    if (assetmastergettersetter.getAsset_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String assettable = assetmastergettersetter.getAsset_master_table();
                        TableBean.setAssetmastertable(assettable);
                    } else {
                        return "ASSET_MASTER";
                    }
                    data.value = 70;
                    data.name = "Asset Master Data Downloading";
                }
                publishProgress(data);*/

                // ASSET_CHECKLIST data
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "ASSET_CHECKLIST");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultasset_check = (Object) envelope.getResponse();

                if (resultasset_check.toString() != null) {
                    xpp.setInput(new StringReader(resultasset_check.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    assetChecklistGetterSetter = XMLHandlers.assetChecklistXMLHandler(xpp, eventType);

                    if (assetChecklistGetterSetter.getAssetchecklist_insert_table() != null) {
                        String assetchecklist_table = assetChecklistGetterSetter.getAssetchecklist_insert_table();
                        TableBean.setAsset_checklist_table(assetchecklist_table);
                    }

                    if (assetChecklistGetterSetter.getCHECKLIST_ID().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        //return "BRAND_MASTER";
                    }
                    data.value = 80;
                    data.name = "Checklist Data Downloading";
                }
                publishProgress(data);*/


                // MAPPING_ASSET_CHECKLIST
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "MAPPING_ASSET_CHECKLIST");

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object resultmapping_asset_check = (Object) envelope.getResponse();

                if (resultmapping_asset_check.toString() != null) {
                    xpp.setInput(new StringReader(resultmapping_asset_check.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    mappingAssetChecklistGetterSetter = XMLHandlers.mappingAssetChecklistXMLHandler(xpp, eventType);

                    if (mappingAssetChecklistGetterSetter.getMapping_asset_checklist_table() != null) {
                        String mappingAssetchecklist_table = mappingAssetChecklistGetterSetter.getMapping_asset_checklist_table();
                        TableBean.setMapping_asset_checklist_table(mappingAssetchecklist_table);
                    }

                    if (mappingAssetChecklistGetterSetter.getAsset_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                    } else {
                        //return "BRAND_MASTER";
                    }
                    data.value = 85;
                    data.name = "Checklist Mapping Downloading";
                }
                publishProgress(data);*/


                //Non Working Reason data
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "NON_WORKING_REASON_NEW");

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

                    if (nonworkinggettersetter.getReason_cd().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String nonworkingtable = nonworkinggettersetter.getNonworking_table();
                        TableBean.setNonworkingtable(nonworkingtable);
                    } else {
                        return "NON_WORKING_REASON_NEW";
                    }
                    data.value = 90;
                    data.name = "Non Working Reason Downloading";

                }
                publishProgress(data);*/


                // Payment Slip Data
                /*request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
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

                    data.value = 100;
                    data.name = "EMP_SALARY Downloading";
                }*/


                //Database insert method calling
                db.open();
                db.insertJCPData(jcpgettersetter);
                db.insertSkuMasterData(skumastergettersetter);
                db.insertMappingAvailData(mappingavailgettersetter);
                if (promotion_flag) {
                    db.insertMappingPromotionData(mappingprormotgettersetter);
                } else {
                    db.deletePromotionMapping();
                }

                if (asset_flag) {
                    db.insertMappingAssetData(mappingassetgettersetter);
                } else {
                    db.deleteAssetMapping();
                }

                //db.insertDeepFreezerData(deepfreezgettersetter);
                db.insertAssetMasterData(assetmastergettersetter);
                db.insertCompanyMasterData(companyGetterSetter);
                db.insertBrandMasterData(brandGetterSetter);

                if (assetChecklistGetterSetter.getCHECKLIST_ID().size() > 0) {
                    db.insertAssetChecklistData(assetChecklistGetterSetter);
                } else {
                    db.deleteAssetChecklist();
                }

                if (mappingAssetChecklistGetterSetter.getAsset_cd().size() > 0) {
                    db.insertMappingAssetChecklistData(mappingAssetChecklistGetterSetter);
                } else {
                    db.deleteMappingAssetChecklist();
                }

                db.insertCategoryMasterData(categorygettersetter);

                db.insertNonWorkingReasonData(nonworkinggettersetter);
                /*
                db.insertPerformanceData(performanceGetterSetter);
				
				if(flag_cold_stock){
					db.insertClosingColdData(closingGetterSetter);
				}*/

                if (payslipGetterSetter != null) {
                    db.insertPaySlipdata(payslipGetterSetter);
                }

                data.value = 100;
                data.name = "Finishing";
                publishProgress(data);

                return resultHttp;

            } catch (MalformedURLException e) {
                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (IOException e) {
                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (Exception e) {
                final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_EXCEPTION + e, "download", e);
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

            if (result.equals(CommonString.KEY_SUCCESS)) {
                AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_DOWNLOAD, "success", null);
                message.showMessage();
            } else {
                AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this, AlertMessage.MESSAGE_JCP_FALSE + result, "success", null);
                message.showMessage();
            }
        }

    }
}
