package com.cpm.dailyentry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.GSKDatabase;
import com.cpm.lorealpromoter.R;
import com.cpm.xmlGetterSetter.PerformanceGetterSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPerformance extends AppCompatActivity {
    private RecyclerView lv_route;
    GSKDatabase db;
    String store_cd;
    ArrayList<PerformanceGetterSetter> list=new ArrayList<>();
    private SharedPreferences preferences;
    RouteAdapter routeAdapter;
    Toolbar toolbar;
    LinearLayout linearLayout,no_data_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_performance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_route=(RecyclerView)findViewById(R.id.lv_routewise);
        linearLayout = findViewById(R.id.ll_layout);
        no_data_lay = findViewById(R.id.no_data_lay);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        db=new GSKDatabase(getApplicationContext());
        db.open();
        list = db.getPerformrmance();
        if(list.size()>0){
            routeAdapter = new RouteAdapter(getApplicationContext(), list);
            lv_route.setAdapter(routeAdapter);
            lv_route.setLayoutManager(new LinearLayoutManager(this));
            linearLayout.setVisibility(View.VISIBLE);
            no_data_lay.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            no_data_lay.setVisibility(View.VISIBLE);
        }
    }

    public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder>{
        private LayoutInflater inflator;
        List<PerformanceGetterSetter> data = Collections.emptyList();

        public RouteAdapter(Context context, List<PerformanceGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.route_item, parent, false);
            RouteAdapter.MyViewHolder holder = new RouteAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RouteAdapter.MyViewHolder viewHolder, final int position) {

            final PerformanceGetterSetter current = data.get(position);

            viewHolder.tvroute.setText(String.valueOf(current.getSTORE_TARGET().get(0)));
            viewHolder.tvpss.setText(String.valueOf(current.getSALES().get(0)));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvroute, tvpss,tvmerchandise;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvroute = (TextView) itemView.findViewById(R.id.tvroute);
                tvpss = (TextView) itemView.findViewById(R.id.tvpss);
                tvmerchandise = (TextView) itemView.findViewById(R.id.tvmerchandise);
            }
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

}
