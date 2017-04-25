package com.cpm.dailyentry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.himalaya.R;
import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.CheckoutBean;
import com.cpm.xmlGetterSetter.FailureGetterSetter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class CheckOutStoreActivity extends Activity implements LocationListener {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private String username, visit_date, store_id, store_intime;
    private Data data;
    int eventType;
    private GSKDatabase db;
    private SharedPreferences preferences = null;
    private CheckoutBean checkout = new CheckoutBean();
    private FailureGetterSetter failureGetterSetter = null;
    static int counter = 1;
    private double latitude = 0.0, longitude = 0.0;
    public static String currLatitude = "0.0";
    public static String currLongitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storename);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        //visit_date = preferences.getString(CommonString.KEY_DATE, null);
        store_id = preferences.getString(CommonString.KEY_STORE_CD, "");
        store_intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        currLatitude = preferences.getString(CommonString.KEY_LATITUDE, "0.0");
        currLongitude = preferences.getString(CommonString.KEY_LONGITUDE, "0.0");

        db = new GSKDatabase(this);
        db.open();

        visit_date = db.getVisiteDateFromCoverage(store_id);
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

                String onXML = "[STORE_CHECK_OUT_STATUS]"
                        + "[USER_ID]" + username + "[/USER_ID]"
                        + "[STORE_ID]" + store_id + "[/STORE_ID]"
                        + "[LATITUDE]" + latitude + "[/LATITUDE]"
                        + "[LOGITUDE]" + longitude + "[/LOGITUDE]"
                        + "[CHECKOUT_DATE]" + visit_date + "[/CHECKOUT_DATE]"
                        + "[CHECK_OUTTIME]" + getCurrentTime() + "[/CHECK_OUTTIME]"
                        + "[CHECK_INTIME]" + store_intime + "[/CHECK_INTIME]"
                        + "[CREATED_BY]" + username + "[/CREATED_BY]"
                        + "[/STORE_CHECK_OUT_STATUS]";

                final String sos_xml = "[DATA]" + onXML + "[/DATA]";

                SoapObject request = new SoapObject(CommonString.NAMESPACE, "Upload_Store_ChecOut_Status");
                request.addProperty("onXML", sos_xml);
                /*request.addProperty("KEYS", "CHECKOUT_STATUS");
                request.addProperty("USERNAME", username);*/
                //request.addProperty("MID", mid);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION + "Upload_Store_ChecOut_Status", envelope);

                Object result = (Object) envelope.getResponse();

                if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                    return "Upload_Store_ChecOut_Status";
                }

                if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                    return "Upload_Store_ChecOut_Status";
                }


                // for failure
                data.value = 100;
                data.name = "Checkout Done";
                publishProgress(data);

                if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS_chkout)) {

                    db.updateCoverageStoreOutTime(store_id, visit_date, getCurrentTime(), CommonString.KEY_C);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(CommonString.KEY_STOREVISITED, "");
                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                    editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                    editor.putString(CommonString.KEY_LATITUDE, "");
                    editor.putString(CommonString.KEY_LONGITUDE, "");
                    editor.commit();

                    db.updateStoreStatusOnCheckout(store_id, visit_date, CommonString.KEY_C);

                } else {
                    if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                        return CommonString.METHOD_Checkout_StatusNew;
                    }

                    // for failure
                }
                return CommonString.KEY_SUCCESS;

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
                        /*
                         * if (counter < 10) { new
						 * BackgroundTask(CheckOutUploadActivity
						 * .this).execute(); } else { message.showMessage();
						 * counter =1; }
						 */
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
                /*AlertMessage message = new AlertMessage(CheckOutStoreActivity.this, CommonString.ERROR + result, "success", null);
                message.showMessage();*/
                Toast.makeText(getApplicationContext(), "Network Error Try Again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    class Data {
        int value;
        String name;
    }

    public String getCurrentTimeX() {

        Calendar m_cal = Calendar.getInstance();
        int hour = m_cal.get(Calendar.HOUR_OF_DAY);
        int min = m_cal.get(Calendar.MINUTE);

        String intime = "";

        if (hour == 0) {
            intime = "" + 12 + ":" + min + " AM";
        } else if (hour == 12) {
            intime = "" + 12 + ":" + min + " PM";
        } else {

            if (hour > 12) {
                hour = hour - 12;
                intime = "" + hour + ":" + min + " PM";
            } else {
                intime = "" + hour + ":" + min + " AM";
            }
        }
        return intime;

    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

        return cdate;

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    String makeJson(String json) {
        json = json.replace("\\", "");
        json = json.replace("\"[", "[");
        json = json.replace("]\"", "]");

        return json;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
