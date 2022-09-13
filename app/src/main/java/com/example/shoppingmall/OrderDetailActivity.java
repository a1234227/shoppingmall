package com.example.shoppingmall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.adapter.Adapter_Order_Detail;

public class OrderDetailActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.Adapter ao;
    SharedPreferences sp;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        sp=getSharedPreferences("ordered_products",MODE_PRIVATE);
        int pos=sp.getInt("position",-1);
        getSupportActionBar().setTitle("訂單 "+(pos+1)+" 詳情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.recycler_order_detail);
        total=findViewById(R.id.text_order_detail_total);
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_order_detail_confirm);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_settings);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    OrderDetailActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    OrderDetailActivity.this.finish();
                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this, OrderActivity.class));
                    OrderDetailActivity.this.finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    OrderDetailActivity.this.finish();
                    break;

            }
            return true;
        });

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 設置格線
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ao = new Adapter_Order_Detail(OrderDetailActivity.this);

        rv.setAdapter(ao);
        sp= getSharedPreferences("total", Context.MODE_PRIVATE);
        String total_receive=sp.getString("total","0");

        total.setText("共計: "+total_receive.substring(0,total_receive.length()-4)+" 元");
//        if(ao.getItemCount()==0){
//        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}