package com.cpm.dailyentry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.delegates.CoverageBean;
import com.cpm.message.AlertMessage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class CheckOutStoreActivity extends Activity {
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private String username, store_id;
    private Data data;
    private GSKDatabase db;
    private SharedPreferences preferences = null;
    ArrayList<CoverageBean> specificCdata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storename);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        db = new GSKDatabase(this);
        db.open();
        store_id = getIntent().getStringExtra(CommonString.KEY_STORE_CD);
        specificCdata = db.getCoverageSpecificData(store_id);
        new BackgroundTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        db.close();
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
            dialog.setTitle("Sending Checkout Data");
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
                //String result = "";
                data = new Data();
                data.value = 20;
                data.name = "Checked out Data Uploading";
                publishProgress(data);
                // if (specificCdata.size()>0)
                String onXML = "[STORE_CHECK_OUT_STATUS]"
                        + "[USER_ID]" + username + "[/USER_ID]"
                        + "[STORE_ID]" + store_id + "[/STORE_ID]"
                        + "[LATITUDE]" + specificCdata.get(0).getLatitude() + "[/LATITUDE]"
                        + "[LOGITUDE]" + specificCdata.get(0).getLongitude() + "[/LOGITUDE]"
                        + "[CHECKOUT_DATE]" + specificCdata.get(0).getVisitDate() + "[/CHECKOUT_DATE]"
                        + "[CHECK_OUTTIME]" + getCurrentTime() + "[/CHECK_OUTTIME]"
                        + "[CHECK_INTIME]" + specificCdata.get(0).getInTime() + "[/CHECK_INTIME]"
                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
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
                    db.updateCoverageStoreOutTime(store_id, specificCdata.get(0).getVisitDate(), getCurrentTime(), CommonString.KEY_C);
                    db.updateStoreStatusOnCheckout(store_id, specificCdata.get(0).getVisitDate(), CommonString.KEY_C);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(CommonString.KEY_STOREVISITED, "");
                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                    editor.commit();
                    data.value = 100;
                    data.name = "Checkout Done";
                    publishProgress(data);
                    return CommonString.KEY_SUCCESS;
                } else {
                    return CommonString.METHOD_Checkout_StatusNew;

                }

            } catch (MalformedURLException e) {
                final AlertMessage message = new AlertMessage(CheckOutStoreActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });

            } catch (IOException e) {
                final AlertMessage message = new AlertMessage(CheckOutStoreActivity.this, AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);
                // counter++;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        message.showMessage();
                    }
                });
            } catch (Exception e) {
                final AlertMessage message = new AlertMessage(CheckOutStoreActivity.this, AlertMessage.MESSAGE_EXCEPTION, "download", e);
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
                AlertMessage message = new AlertMessage(CheckOutStoreActivity.this, "Successfully Checked out", "checkout", null);
                message.showMessage();
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
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

    }


}
