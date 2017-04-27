package com.cpm.dailyentry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.himalaya.R;
import com.cpm.xmlGetterSetter.Audit_QuestionDataGetterSetter;
import com.cpm.xmlGetterSetter.Audit_QuestionGetterSetter;
import com.cpm.xmlGetterSetter.DeepFreezerTypeGetterSetter;
import com.cpm.xmlGetterSetter.StockNewGetterSetter;

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
    ArrayAdapter<String> spinnerAdapter;
    RecyclerView recyclerView;

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

        str = CommonString.FILE_PATH;

        db = new GSKDatabase(getApplicationContext());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        // preparing list data
        prepareListData();
    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>>();

        /*headerListData = db.getHeaderStockImageData(store_cd, visit_date);
        if (!(headerListData.size() > 0)) {
        }*/
        headerListData = db.getAuditQuestionData(store_cd);

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
            final Audit_QuestionDataGetterSetter data = questionList.get(position);

            holder.txt_question.setText(data.getQuestion());

            answerList = new ArrayList<>();
            answerList = answerHashMap.get(data);

            spinnerAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item);
            spinnerAdapter.add("Select");
            for (int i = 0; i < answerList.size(); i++) {
                spinnerAdapter.add(answerList.get(i).getAnswer());
            }
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.sp_auditAnswer.setAdapter(spinnerAdapter);

            for (int i = 0; i < answerList.size(); i++) {
                if (data.getSp_answer().equals(answerList.get(i).getAnswer_id())) {
                    holder.sp_auditAnswer.setSelection(i);
                    break;
                }
            }

            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                    String str = (String) parent.getItemAtPosition(position);

                    if (str.equalsIgnoreCase("Select")) {
                        data.setSp_answer("0");
                    } else {
                        for (int i = 0; i < answerList.size(); i++) {
                            if (str.equalsIgnoreCase(answerList.get(i).getAnswer())) {
                                data.setSp_answer(answerList.get(i).getAnswer_id());
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txt_question;
            public final Spinner sp_auditAnswer;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                txt_question = (TextView) view.findViewById(R.id.txt_question);
                sp_auditAnswer = (Spinner) view.findViewById(R.id.sp_auditAnswer);
            }
        }
    }
}
