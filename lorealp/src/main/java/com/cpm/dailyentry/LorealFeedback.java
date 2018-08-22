package com.cpm.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.retrofit.UploadImageWithRetrofit;
import com.cpm.xmlGetterSetter.FeedbackGetterSetter;
import com.cpm.xmlGetterSetter.JourneyPlanGetterSetter;
import com.cpm.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;
import com.cpm.xmlGetterSetter.POGGetterSetter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LorealFeedback extends AppCompatActivity {
    ExpandableListView expandableListView;
    String visit_date, username;
    String name, designation,feedback_image;
    private SharedPreferences preferences;
    private GSKDatabase db;
    List<MSL_AvailabilityStockFacingGetterSetter> headerDataList;
    HashMap<MSL_AvailabilityStockFacingGetterSetter, List<POGGetterSetter>> hashMapListChildData = new HashMap<>();
    ExpandableListAdapter adapter;
    List<Integer> checkHeaderArray = new ArrayList<>();
    String error_msg = "";
    boolean checkflag = true;
    ArrayList<JourneyPlanGetterSetter> jcplist;
    private String store_cd;
    FeedbackGetterSetter feedbackGetterSetter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loreal_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        db = new GSKDatabase(this);
        db.open();
        context = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);

        name = preferences.getString(CommonString.KEY_NAME, null);
        designation = preferences.getString(CommonString.KEY_DESIGNATION, null);
        feedback_image = preferences.getString(CommonString.KEY_FEEDBACK_IMAGE, null);

        setTitle("Loreal Feedback- " + visit_date);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        feedbackGetterSetter = new FeedbackGetterSetter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {

                    setData();

                    AlertDialog.Builder builder = new AlertDialog.Builder(LorealFeedback.this);
                    builder.setMessage("Do you want to save the data ")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (CheckNetAvailability()) {
                                        db.open();
                                        long return_id = db.InsertfeedbackData(feedbackGetterSetter);
                                        if (return_id > 0) {
                                            db.savePOGQuestionAnswerData(hashMapListChildData, headerDataList, store_cd, return_id, name, designation, username,feedback_image);

                                        }
                                        new UploadVisitorData().execute();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No internet connection! try again later", Toast.LENGTH_LONG).show();

                                    }


                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    adapter.notifyDataSetChanged();
                    expandableListView.invalidateViews();
                    Snackbar.make(expandableListView, error_msg, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        prepareListData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LorealFeedback.this);
            builder.setTitle("Parinam");
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LorealFeedback.this);
        builder.setTitle("Parinam");
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    void prepareListData() {

        db.open();
        jcplist = db.getJCPData(visit_date);
        store_cd = String.valueOf(jcplist.get(0).getStore_cd().get(0));

        headerDataList = new ArrayList<>();

        headerDataList = db.getSubCategoryMasterForPOG();

        if (headerDataList.size() > 0) {
            for (int i = 0; i < headerDataList.size(); i++) {

                ArrayList<POGGetterSetter> quizDataList = db.getAfterSavePOGQuestionAnswerData(store_cd, headerDataList.get(i).getF_category_cd());

                if (quizDataList.size() == 0) {
                    quizDataList = db.getPOGSubCategoryWise(headerDataList.get(i).getF_category_cd());
                }

                ArrayList<POGGetterSetter> childListData = new ArrayList<>();

                if (quizDataList.size() > 0) {
                    String select = getString(R.string.title_activity_select_dropdown);
                    // Adding child data
                    for (int j = 0; j < quizDataList.size(); j++) {
                        childListData = db.getPOGAnswerData(quizDataList.get(j).getQUESTION_ID(), headerDataList.get(i).getF_category_cd(), select);
                        quizDataList.get(j).setAnswerList(childListData);
                    }
                }

                hashMapListChildData.put(headerDataList.get(i), quizDataList);
            }
        }

        adapter = new ExpandableListAdapter(this, headerDataList, hashMapListChildData);
        expandableListView.setAdapter(adapter);

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<MSL_AvailabilityStockFacingGetterSetter> _listDataHeader;
        private HashMap<MSL_AvailabilityStockFacingGetterSetter, List<POGGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<MSL_AvailabilityStockFacingGetterSetter> listDataHeader,
                                     HashMap<MSL_AvailabilityStockFacingGetterSetter, List<POGGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            MSL_AvailabilityStockFacingGetterSetter headerTitle = (MSL_AvailabilityStockFacingGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_stock_facing_header, null, false);
            }

            TextView txt_categoryHeader = (TextView) convertView.findViewById(R.id.txt_categoryHeader);
            RelativeLayout rel_header = (RelativeLayout) convertView.findViewById(R.id.rel_categoryHeader);
            ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);
            CardView card_view = (CardView) convertView.findViewById(R.id.card_view);

            txt_categoryHeader.setTypeface(null, Typeface.BOLD);

            txt_categoryHeader.setText(headerTitle.getF_category());


            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    card_view.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    card_view.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }


            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final POGGetterSetter childData =
                    (POGGetterSetter) getChild(groupPosition, childPosition);
            ArrayList<POGGetterSetter> ans_list = childData.getAnswerList();
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.pog_question, null, false);

                holder = new ViewHolder();
                holder.txt_question = (TextView) convertView.findViewById(R.id.txt_question);
                holder.sp_auditAnswer = (Spinner) convertView.findViewById(R.id.sp_auditAnswer);
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter(LorealFeedback.this, R.layout.custom_spinner_item, ans_list));

            final ArrayList<POGGetterSetter> finalAns_list = ans_list;
            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    POGGetterSetter ans = finalAns_list.get(position);
                    childData.setANSWER_ID(ans.getANSWER_ID());
                    childData.setANSWER(ans.getANSWER());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.txt_question.setText(childData.getQUESTION());

            for (int i = 0; i < ans_list.size(); i++) {
                if (ans_list.get(i).getANSWER_ID().equals(childData.getANSWER_ID())) {
                    holder.sp_auditAnswer.setSelection(i);
                    break;
                }
            }

            if (!checkflag) {
                if (childData.getANSWER_ID().equals("0")) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
        CardView cardView;
        View mView;
        TextView txt_question;
        Spinner sp_auditAnswer;
    }

    public class AnswerSpinnerAdapter extends ArrayAdapter<POGGetterSetter> {
        List<POGGetterSetter> list;
        Context context;
        int resourceId;

        public AnswerSpinnerAdapter(Context context, int resourceId, ArrayList<POGGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            POGGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_text);
            txt_spinner.setText(list.get(position).getANSWER());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            POGGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_text);
            txt_spinner.setText(cm.getANSWER());

            return view;
        }
    }

    boolean validateData() {
        checkHeaderArray.clear();

        checkflag = true;
        for (int l = 0; l < headerDataList.size(); l++) {

            List<POGGetterSetter> child_data = hashMapListChildData.get(headerDataList.get(l));
            for (int i = 0; i < child_data.size(); i++) {
                if (child_data.get(i).getANSWER_ID().equalsIgnoreCase("0")) {
                    error_msg = getString(R.string.pls_answer_all_qns);
                    checkflag = false;
                }

                if (checkflag == false) {
                    break;
                }
            }

            if (checkflag == false) {
                if (!checkHeaderArray.contains(l)) {
                    checkHeaderArray.add(l);
                }
                break;
            }
        }

        return checkflag;
    }


    class UploadVisitorData extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = new ProgressDialog(LorealFeedback.this);
            dialog.setTitle("Visitor data");
            dialog.setMessage("Uploading....");
            dialog.setCancelable(false);
            dialog.show();


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                UploadImageWithRetrofit uploadRetro = new UploadImageWithRetrofit(context);

                db.open();
                ArrayList<POGGetterSetter> feedback = db.getAfterSavePOGForUploadData(store_cd);
                if (feedback.size() > 0) {
                    String onXml_pog_data = "", POG_data = "";
                    for (int n = 0; n < feedback.size(); n++) {

                        onXml_pog_data = "[FEEDBACK_DATA]"
                                + "[USER_ID]" + username + "[/USER_ID]"
                                + "[STORE_CD]" + store_cd + "[/STORE_CD]"
                                + "[VISIT_DATE]" + visit_date + "[/VISIT_DATE]"
                                + "[FEEDBACK_IMAGE]" + feedback_image + "[/FEEDBACK_IMAGE]"
                                + "[CATEGORY_ID]" + feedback.get(n).getCATEGORY_CD() + "[/CATEGORY_ID]"
                                + "[QUESTION_ID]" + feedback.get(n).getQUESTION_ID() + "[/QUESTION_ID]"
                                + "[ANSWER_ID]" + feedback.get(n).getANSWER_ID() + "[/ANSWER_ID]"
                                + "[VISITER_NAME]" + feedback.get(n).getVISITOR_NAME() + "[/VISITER_NAME]"
                                + "[DESIGNATION]" + feedback.get(n).getVISITOR_DESIGNATION() + "[/DESIGNATION]"
                                + "[/FEEDBACK_DATA]";

                        POG_data = POG_data + onXml_pog_data;
                    }

                    final String sos_xml = "[DATA]" + POG_data + "[/DATA]";

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                    request.addProperty("XMLDATA", sos_xml);
                    request.addProperty("KEYS", "LOREAL_FEEDBACK");
                    request.addProperty("USERNAME", username);
                    request.addProperty("MID", 0);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                    androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                    Object result = (Object) envelope.getResponse();

                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                        return "Failure";

                    }


                }
                return CommonString.KEY_SUCCESS;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result.equals(CommonString.KEY_SUCCESS)) {

                Toast.makeText(getApplicationContext(), "Feedback Data Uploaded", Toast.LENGTH_SHORT).show();
                //update upload_status to U
                db.open();
                feedbackGetterSetter = db.getfeedbackData(username);
                db.updateFeedbackData(feedbackGetterSetter.getVisitor_name(), feedbackGetterSetter.getVisitor_designation());
                db.open();
                db.deletetfeedbackTables();

                Intent in = new Intent(LorealFeedback.this, LorealFeedbackActiivty.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                LorealFeedback.this.finish();

            } else {
                Toast.makeText(getApplicationContext(), "Data not uploaded!", Toast.LENGTH_LONG).show();
            }


        }

    }


    private void setData() {
        feedbackGetterSetter.setUser_id(username);
        feedbackGetterSetter.setVisit_date(visit_date);
        feedbackGetterSetter.setVisitor_name(name);
        feedbackGetterSetter.setVisitor_designation(designation);
        feedbackGetterSetter.setStatus("I");

    }

    public boolean CheckNetAvailability() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }


}
