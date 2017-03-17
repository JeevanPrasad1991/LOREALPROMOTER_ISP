package com.cpm.dailyentry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.cpm.capitalfoods.R;
import com.cpm.database.GSKDatabase;

import com.cpm.xmlGetterSetter.PayslipGetterSetter;

import java.util.ArrayList;

public class PaySlip extends AppCompatActivity {

    TextView ecode, ename, paymentmode, presentdays, incentive, national_holiday,totalearning, pf, esi, pt, lwf,
            misdeduction, tds,
            takehome, date;
    GSKDatabase db;
    ArrayList<PayslipGetterSetter> list = new ArrayList<PayslipGetterSetter>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_slip);
        ecode = (TextView)findViewById(R.id.ecode);
        ename = (TextView)findViewById(R.id.ename);
        paymentmode = (TextView)findViewById(R.id.paymentmode);
        presentdays = (TextView)findViewById(R.id.presentdays);
        incentive = (TextView)findViewById(R.id.incentive);
        totalearning = (TextView)findViewById(R.id.totalearnings);
        pf = (TextView)findViewById(R.id.pf);
        esi = (TextView)findViewById(R.id.esi);
        pt = (TextView)findViewById(R.id.pt);
        lwf = (TextView)findViewById(R.id.lwf);
        misdeduction = (TextView)findViewById(R.id.misdeduction);
        tds = (TextView)findViewById(R.id.tds);
        takehome = (TextView)findViewById(R.id.takehome);
        date = (TextView)findViewById(R.id.date);
        national_holiday = (TextView)findViewById(R.id.national_holiday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new GSKDatabase(getApplicationContext());
        db.open();

        list = db.getPaySlipData();

        if(list.size()>0){

            ecode.setText(list.get(0).getECODE().get(0).replaceAll("[&^<>{}'$]", " "));
            ename.setText(list.get(0).getEMP_NAME().get(0).replaceAll("[&^<>{}'$]", " "));
            paymentmode.setText(list.get(0).getPAYMENT_MODE().get(0).replaceAll("[&^<>{}'$]", " "));
            presentdays.setText(list.get(0).getPRESENT_DAYS().get(0).replaceAll("[&^<>{}'$]", " "));
            incentive.setText(list.get(0).getINCENTIVE().get(0).replaceAll("[&^<>{}'$]", " "));
            national_holiday.setText(list.get(0).getNATIONAL_H().get(0).replaceAll("[&^<>{}'$]", " "));
            totalearning.setText(list.get(0).getTOTAL_EARNING().get(0).replaceAll("[&^<>{}'$]", " "));
            pf.setText(list.get(0).getPF().get(0).replaceAll("[&^<>{}'$]", " "));
            esi.setText(list.get(0).getESI().get(0).replaceAll("[&^<>{}'$]", " "));
            pt.setText(list.get(0).getPT().get(0).replaceAll("[&^<>{}'$]", " "));
            lwf.setText(list.get(0).getLWF().get(0).replaceAll("[&^<>{}'$]", " "));
            misdeduction.setText(list.get(0).getMIS_DEDUCTION().get(0).replaceAll("[&^<>{}'$]", " "));
            tds.setText(list.get(0).getTDS().get(0).replaceAll("[&^<>{}'$]", " "));
            takehome.setText(list.get(0).getTAKE_HOME().get(0).replaceAll("[&^<>{}'$]", " "));
            date.setText(list.get(0).getMONTH().get(0).replaceAll("[&^<>{}'$]", " ")+ "/" +
                    list.get(0).getSALARY_YEAR().get(0).replaceAll("[&^<>{}'$]", " ") );

        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home){

            // NavUtils.navigateUpFromSameTask(this);
            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
