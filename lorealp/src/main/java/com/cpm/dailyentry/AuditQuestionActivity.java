package com.cpm.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.Audit_QuestionDataGetterSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditQuestionActivity extends AppCompatActivity {
    static int currentapiVersion = 1;
    Button btnSave;
    private SharedPreferences preferences;
    String store_cd;
    GSKDatabase db;
    String visit_date, username, intime, str;
    Snackbar snackbar;

    ArrayList<Audit_QuestionDataGetterSetter> headerListData;
    ArrayList<Audit_QuestionDataGetterSetter> childListData;
    ArrayList<Audit_QuestionDataGetterSetter> listDataHeader;
    HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>> listDataChild;
    AnswerAdapter adapter;
    RecyclerView recyclerView;
    Button save_btn;
    List<Integer> checkHeaderArray = new ArrayList<>();
    boolean checkflag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_question);

        currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        save_btn = (Button) findViewById(R.id.save_btn);

        str = CommonString.FILE_PATH;

        db = new GSKDatabase(getApplicationContext());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        setTitle("Category Audit - " + visit_date);

        // preparing list data
        prepareListData();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData(listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuditQuestionActivity.this);
                    builder.setMessage("Are you sure you want to save")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.open();
                                    //temp category cd added
                                    db.saveAuditQuestionAnswerData(listDataHeader, store_cd,"1");

                                    Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean validateData(ArrayList<Audit_QuestionDataGetterSetter> data) {
        //boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSp_answer_id().equalsIgnoreCase("0")) {

                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                checkflag = false;
                //flag = false;
                break;

            } else {
                checkflag = true;
                //flag = true;
            }

            if (checkflag == false) {
                break;
            }
        }
        //expListView.invalidate();
        return checkflag;
    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>>();

        headerListData = db.getAfterSaveAuditQuestionAnswerData(store_cd,"1");
        if (!(headerListData.size() > 0)) {
            headerListData = db.getAuditQuestionData(store_cd);
        }
        else{
            save_btn.setText("Update");
        }

        if (headerListData.size() > 0) {
            // Adding child data
            for (int i = 0; i < headerListData.size(); i++) {
                listDataHeader.add(headerListData.get(i));

                childListData = db.getAuditAnswerData(store_cd, headerListData.get(i).getQuestion_id());
                /*childListData = db.getOpeningStockDataFromDatabase(store_cd, headerListData.get(i).getCategory_cd());
                if (!(childListData.size() > 0)) {
                    childListData = db.getAuditAnswerData(store_cd, headerListData.get(i).getQuestion_id());
                } else {
                    btnSave.setText("Update");
                }*/

                ArrayList<Audit_QuestionDataGetterSetter> answerList = new ArrayList<>();
                for (int j = 0; j < childListData.size(); j++) {
                    answerList.add(childListData.get(j));
                }

                listDataChild.put(listDataHeader.get(i), answerList); // Header, Child data
            }
        }

        adapter = new AnswerAdapter(listDataHeader, listDataChild);
        recyclerView.setAdapter(adapter);
    }

    class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
        ArrayList<Audit_QuestionDataGetterSetter> questionList;
        HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>> answerHashMap;
        ArrayList<Audit_QuestionDataGetterSetter> answerList;
        // ArrayList<Audit_QuestionDataGetterSetter> ans_list;

        public AnswerAdapter(ArrayList<Audit_QuestionDataGetterSetter> questionList,
                             HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>> answerHashMap) {
            this.questionList = questionList;
            this.answerHashMap = answerHashMap;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_question_item_list, parent, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.data = questionList.get(position);

            holder.txt_question.setText(holder.data.getQuestion());

            ArrayList<Audit_QuestionDataGetterSetter> ans_list = new ArrayList<>();
            ans_list = answerHashMap.get(holder.data);
            holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter(AuditQuestionActivity.this, R.layout.spinner_text_view, ans_list));

            final ArrayList<Audit_QuestionDataGetterSetter> finalAns_list = ans_list;
            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Audit_QuestionDataGetterSetter ans = finalAns_list.get(position);
                    holder.data.setSp_answer_id(ans.getAnswer_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < ans_list.size(); i++) {
                if (ans_list.get(i).getAnswer_id().equals(holder.data.getSp_answer_id())) {
                    holder.sp_auditAnswer.setSelection(i);
                    break;
                }
            }

            if (!checkflag) {
                if (checkHeaderArray.contains(position)) {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txt_question;
            public final Spinner sp_auditAnswer;
            CardView card_view;
            Audit_QuestionDataGetterSetter data;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                txt_question = (TextView) view.findViewById(R.id.txt_question);
                sp_auditAnswer = (Spinner) view.findViewById(R.id.sp_auditAnswer);
                card_view = (CardView) view.findViewById(R.id.card_view);
            }
        }
    }

    public class AnswerSpinnerAdapter extends ArrayAdapter<Audit_QuestionDataGetterSetter> {
        List<Audit_QuestionDataGetterSetter> list;
        Context context;
        int resourceId;

        public AnswerSpinnerAdapter(Context context, int resourceId, ArrayList<Audit_QuestionDataGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            Audit_QuestionDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            Audit_QuestionDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuditQuestionActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AuditQuestionActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
