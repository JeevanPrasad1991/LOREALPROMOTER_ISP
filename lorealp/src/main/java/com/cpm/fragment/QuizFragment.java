package com.cpm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.Audit_QuestionDataGetterSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QuizFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    ArrayList<Audit_QuestionDataGetterSetter> headerListData;
    ArrayList<Audit_QuestionDataGetterSetter> childListData;
    ArrayList<Audit_QuestionDataGetterSetter> listDataHeader;
    HashMap<Audit_QuestionDataGetterSetter, ArrayList<Audit_QuestionDataGetterSetter>> listDataChild;

    GSKDatabase db;

    String visit_date, username, intime, str, category_id="1";
    int tab_position;
    private SharedPreferences preferences;
    String store_cd;

    RecyclerView recyclerView;
    Button btn;
    AnswerAdapter adapter;
    boolean checkflag = true;

    List<Integer> checkHeaderArray = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuizFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuizFragment newInstance(int columnCount) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        category_id = getArguments().getString(CommonString.KEY_CATEGORY_ID);
        tab_position = getArguments().getInt(CommonString.KEY_POSITION);

        str = CommonString.FILE_PATH;

        db = new GSKDatabase(getActivity());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        btn = (Button) view.findViewById(R.id.btn_save);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    if (validateData(listDataHeader)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you want to save")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mListener.onListFragmentInteraction(listDataHeader, store_cd, category_id, tab_position);

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
                        Snackbar.make(recyclerView,"Please answer all questions",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        prepareListData();

       /* FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    if (validateData(listDataHeader)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you want to save")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mListener.onListFragmentInteraction(listDataHeader, store_cd, category_id, tab_position);

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
                        //adapter.notifyDataSetChanged();
                        Snackbar.make(recyclerView,"Please answer all questions",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        return view;
    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        headerListData = db.getAfterSaveAuditQuestionAnswerData(store_cd, category_id);
        if (!(headerListData.size() > 0)) {
            headerListData = db.getAuditQuestionData(category_id);
        }
        else{
            btn.setText("Update");
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<Audit_QuestionDataGetterSetter> listDataHeader,String store_cd, String category_cd,int tab_position);
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
            holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter(getActivity(), R.layout.spinner_text_view, ans_list));

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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            Audit_QuestionDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getActivity().getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            Audit_QuestionDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }
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
}
