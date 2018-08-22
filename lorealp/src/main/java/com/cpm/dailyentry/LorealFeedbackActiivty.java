package com.cpm.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    ImageView img_photoMar;
    String _pathforcheck,_path,intime,str, image1 = "";

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
        img_photoMar = (ImageView) findViewById(R.id.img_photoMar);
        str = CommonString.FILE_PATH;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        database = new GSKDatabase(this);
        database.open();

        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        current_user.setText("Current User - " + username);
        setTitle("Loreal Feedback- " + visit_date);
        save.setOnClickListener(this);
        img_photoMar.setOnClickListener(this);
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
                    editor.putString(CommonString.KEY_FEEDBACK_IMAGE, image1);
                    editor.commit();
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    LorealFeedbackActiivty.this.finish();

                } else {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }

                break;


            case  R.id.img_photoMar:
                _pathforcheck = "_FEEDBACK_" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();
                startCameraActivity();
        }

    }

    public boolean check_condition() {

        name = ed_name.getText().toString().replaceAll("[(!@#$%^&*?%_)\"]", " ").trim();
        designation = ed_designation.getText().toString().replaceAll("[(!@#$%^&*?%_)\"]", " ").trim();
        if (name.equalsIgnoreCase("") || designation.equalsIgnoreCase("")) {
            error_msg = "Please fill Visitor Name and Designation";
            return false;
        } else if (image1.equals("")) {
            error_msg = "Please Capture Photo";
            return  false;
        }

        return true;
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
    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }
    protected void startCameraActivity() {

        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Crashlytics.logException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {

                        //jee
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
                        Bitmap dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

                        Canvas cs = new Canvas(dest);
                        Paint tPaint = new Paint();
                        tPaint.setTextSize(100);
                        tPaint.setColor(Color.RED);
                        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                        cs.drawBitmap(bmp, 0f, 0f, null);
                        float height = tPaint.measureText("yY");
                        cs.drawText(dateTime, 20f, height + 15f, tPaint);
                        try {
                            dest.compress(Bitmap.CompressFormat.JPEG, 100,
                                    new FileOutputStream(new File(str + _pathforcheck)));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        img_photoMar.setImageResource(R.drawable.camera_green);
                        image1 = _pathforcheck;
                    }
                    _pathforcheck = "";
                }
                break;
        }

    }

}
