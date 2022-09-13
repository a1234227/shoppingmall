package com.example.shoppingmall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.adapter.Adapter_Result;

public class ResultActivity extends AppCompatActivity {
    String keyword="",max,min;
    Adapter_Result adapter_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        keyword = getSharedPreferences("search", MODE_PRIVATE).getString("input_name", "");
        max = getSharedPreferences("search", MODE_PRIVATE).getString("max", "");
        min = getSharedPreferences("search", MODE_PRIVATE).getString("min", "");
        getSupportActionBar().setTitle(keyword+"  的搜尋結果");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_result);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this,HomeActivity.class));
                    ResultActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this,SearchActivity.class));
                    ResultActivity.this.finish();
                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this,OrderActivity.class));
                    ResultActivity.this.finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this,SettingsActivity.class));
                    ResultActivity.this.finish();
                    break;

            }
            return true;
        });



        //20220907 edit by 鄭紹謙
        //listview 的adapter set上去
        ListView lv=findViewById(R.id.listview_result);
        adapter_result = Adapter_Result.factoryCreate(ResultActivity.this,keyword);
        lv.setAdapter(adapter_result);
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