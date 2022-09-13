package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.Adapter_Setting_Follow;
import com.example.adapter.Adapter_Setting_Order;
import com.example.entity.Customer_order;
import com.example.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    TextView myname;
    ImageView empty,empty2,ea;
    RecyclerView rv,rv2;
    RecyclerView.Adapter ao,asf;
    Gson gson = new Gson();
    ArrayList<Customer_order> co = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myname = findViewById(R.id.text_settings_greeting);
        rv = findViewById(R.id.recycler_setting_order);
        rv2 = findViewById(R.id.recycler_setting_follow);
        empty = findViewById(R.id.imageView_settings_empty);
        empty2 = findViewById(R.id.imageView_settings_empty2);
        ea=findViewById(R.id.head);
        getSupportActionBar().setTitle("個人設定");
        sp = getSharedPreferences("info", MODE_PRIVATE);
        String realname = sp.getString("realname", "");
        myname.setText("歡迎，" + realname);
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_settings);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_settings);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    SettingsActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    SettingsActivity.this.finish();
                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this, OrderActivity.class));
                    SettingsActivity.this.finish();
                    break;
                case R.id.nav_settings:

                    break;

            }
            return true;
        });

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // 設置格線
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        ao = new Adapter_Setting_Order(SettingsActivity.this);
        rv.setAdapter(ao);
        if (ao.getItemCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.INVISIBLE);
        }
        rv.addOnItemTouchListener(new RecyclerItemClickListener(SettingsActivity.this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sp = getSharedPreferences("ordered_products", MODE_PRIVATE);
                String json = sp.getString("json", null);
                co = gson.fromJson(json, new TypeToken<ArrayList<Customer_order>>() {
                }.getType());
                Customer_order send = co.get(position);
                sp.edit().putString("json_send", gson.toJson(send, Customer_order.class)).commit();
                sp.edit().putInt("position", position).commit();
                startActivity(new Intent(SettingsActivity.this, OrderDetailActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        rv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // 設置格線
        rv2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        asf = new Adapter_Setting_Follow(SettingsActivity.this);
        rv2.setAdapter(asf);
        ea.setVisibility(View.VISIBLE);

        if (asf.getItemCount() == 0) {
            empty2.setVisibility(View.VISIBLE);
        } else {
            empty2.setVisibility(View.INVISIBLE);
        }
        rv2.addOnItemTouchListener(new RecyclerItemClickListener(SettingsActivity.this, rv2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sp=getSharedPreferences("follow_id",MODE_PRIVATE);
                String json=sp.getString("follow_id","[]");
                ArrayList<Integer> ids=gson.fromJson(json,new TypeToken<ArrayList<Integer>>(){}.getType());
                int id=ids.get(position);
                sp=getSharedPreferences("product",MODE_PRIVATE);
                sp.edit().putString("id", String.valueOf(id)).commit();
                sp=getSharedPreferences("product_destination",MODE_PRIVATE);
                sp.edit().putString("destination", "settings").commit();
                startActivity(new Intent(SettingsActivity.this,ProductActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void action_settings(View v) {
        if (v.getId() == R.id.button_settings_logout) {
            sp = getSharedPreferences("info", MODE_PRIVATE);
            spe = sp.edit();
            spe.clear();
            spe.commit();
            startActivity(new Intent(this, WelcomeActivity.class));
        }else if(v.getId() == R.id.button_settings_order) {
            startActivity(new Intent(this,BrowseAllOrderActivity.class));

        }else if(v.getId() == R.id.button_settings_follow) {
            startActivity(new Intent(this,BrowseAllFollowActivity.class));

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再點擊一次返回退出", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}