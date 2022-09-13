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

import com.example.adapter.Adapter_Setting_Follow;
import com.example.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BrowseAllFollowActivity extends AppCompatActivity {
    RecyclerView rv2;
    RecyclerView.Adapter asf;
    ImageView empty2;
    SharedPreferences sp;
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_all_follow);
        getSupportActionBar().setTitle("追蹤的商品");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        empty2=findViewById(R.id.imageview_browse_follow_empty);
        rv2=findViewById(R.id.recycler_browse_follow);
        rv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 設置格線
        rv2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        asf = new Adapter_Setting_Follow(BrowseAllFollowActivity.this);
        rv2.setAdapter(asf);
        if (asf.getItemCount() == 0) {
            empty2.setVisibility(View.VISIBLE);
        } else {
            empty2.setVisibility(View.INVISIBLE);
        }
        rv2.addOnItemTouchListener(new RecyclerItemClickListener(BrowseAllFollowActivity.this, rv2, new RecyclerItemClickListener.OnItemClickListener() {
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
                startActivity(new Intent(BrowseAllFollowActivity.this,ProductActivity.class));
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