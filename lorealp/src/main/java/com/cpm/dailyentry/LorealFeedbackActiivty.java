package com.cpm.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.GetterSetter.VisitorDetailGetterSetter;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.FeedbackGetterSetter;

import java.util.ArrayList;

public class LorealFeedbackActiivty extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lay_heading;
    EditText ed_name, ed_designation;
    TextView current_user;
    Button save;
    Activity activity;
    Context context;
    SharedPreferences preferences;
    private GSKDatabase database;
    String visit_date, username;
    String name, designation;
    String error_msg = "";
    private RecyclerView rv_visitor;
    private SharedPreferences.Editor editor = null;
    ArrayList<FeedbackGetterSetter> feedback_list = new ArrayList<>();
    FeedbackGetterSetter feedbackGetterSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loreal_feedback_actiivty);
        declaration();

    }

    void declaration() {
        activity = this;
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        current_user = (TextView) findViewById(R.id.current_user);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_designation = (EditText) findViewById(R.id.ed_designation);
        save = (Button) findViewById(R.id.save);
        rv_visitor = (RecyclerView) findViewById(R.id.rv_visitor);
        lay_heading = (LinearLayout) findViewById(R.id.lay_heading);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        database = new GSKDatabase(this);
        database.open();

        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        current_user.setText("Current User - " + username);
        setTitle("Loreal Feedback- " + visit_date);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.save:
                if (check_condition()) {
                    Intent in = new Intent(LorealFeedbackActiivty.this, LorealFeedback.class);
                    editor.putString(CommonString.KEY_NAME, name);
                    editor.putString(CommonString.KEY_DESIGNATION, designation);
                    editor.commit();
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    LorealFeedbackActiivty.this.finish();

                } else {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    public boolean check_condition() {

        name = ed_name.getText().toString().replaceAll("[(!@#$%^&*?%_)\"]", " ").trim();
        designation = ed_designation.getText().toString().replaceAll("[(!@#$%^&*?%_)\"]", " ").trim();
        if (name.equalsIgnoreCase("") || designation.equalsIgnoreCase("")) {
            error_msg = "Please fill Visitor Name and Designation";
            return false;
        } else {
            return true;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        setlistviewData();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        Context context;
        LayoutInflater inflater;

        RecyclerAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.child_feedback, parent, false);
            return new RecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
            holder.tv_name.setText(feedback_list.get(position).getVisitor_name());
            holder.tv_outtime.setText(feedback_list.get(position).getVisitor_designation());
            if (feedback_list.get(position).getStatus() != null && feedback_list.get(position).getStatus().equals("U")) {
                holder.img_upload_tick.setVisibility(View.VISIBLE);
            } else {
                holder.img_upload_tick.setVisibility(View.INVISIBLE);
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedbackGetterSetter = feedback_list.get(position);

                    if (feedbackGetterSetter.getStatus() != null && feedbackGetterSetter.getStatus().equalsIgnoreCase("U")) {
                        Toast.makeText(getApplicationContext(), "Data already uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent in = new Intent(LorealFeedbackActiivty.this, LorealFeedback.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return feedback_list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_name;

            TextView tv_outtime;
            LinearLayout layout;
            ImageView img_upload_tick;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_outtime = (TextView) itemView.findViewById(R.id.tv_outtime);
                layout = (LinearLayout) itemView.findViewById(R.id.ll_item);
                img_upload_tick = (ImageView) itemView.findViewById(R.id.img_upload_tick);
            }
        }
    }

    public void setlistviewData() {

        database.open();
        feedback_list = database.getVisitorLoginFeedbackData(visit_date, username);
        if (feedback_list.size() > 0) {
            lay_heading.setVisibility(View.VISIBLE);
            rv_visitor.setAdapter(new RecyclerAdapter(getApplicationContext()));
            rv_visitor.setLayoutManager(new LinearLayoutManager(LorealFeedbackActiivty.this));
            rv_visitor.setVisibility(View.VISIBLE);
        } else {
            lay_heading.setVisibility(View.INVISIBLE);
            rv_visitor.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
