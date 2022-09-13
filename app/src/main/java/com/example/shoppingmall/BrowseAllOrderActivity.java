package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.adapter.Adapter_Setting_Order;
import com.example.entity.Customer_order;
import com.example.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BrowseAllOrderActivity extends AppCompatActivity {
        RecyclerView rv;
        RecyclerView.Adapter ao;
        ImageView empty;
        SharedPreferences sp;
        ArrayList<Customer_order> co;
        Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_all_order);
        getSupportActionBar().setTitle("所有訂單");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv=findViewById(R.id.recycler_browse_order);
        empty=findViewById(R.id.imageview_browse_order_empty);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 設置格線
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ao = new Adapter_Setting_Order(BrowseAllOrderActivity.this);
        rv.setAdapter(ao);
        if (ao.getItemCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.INVISIBLE);
        }
        rv.addOnItemTouchListener(new RecyclerItemClickListener(BrowseAllOrderActivity.this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sp = getSharedPreferences("ordered_products", MODE_PRIVATE);
                String json = sp.getString("json", null);
                co = gson.fromJson(json, new TypeToken<ArrayList<Customer_order>>() {
                }.getType());
                Customer_order send = co.get(position);
                sp.edit().putString("json_send", gson.toJson(send, Customer_order.class)).commit();
                sp.edit().putInt("position", position).commit();
                startActivity(new Intent(BrowseAllOrderActivity.this, OrderDetailActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,SettingsActivity.class));
        this.finish();
    }
}