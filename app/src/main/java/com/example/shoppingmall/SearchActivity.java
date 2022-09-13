package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    EditText name, pricemin, pricemax;
    Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        name = findViewById(R.id.input_search_name);
        pricemin = findViewById(R.id.input_search_pricemin);
        pricemax = findViewById(R.id.input_search_pricemax);
        getSupportActionBar().setTitle("搜尋商品");
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_search);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    SearchActivity.this.finish();
                    break;
                case R.id.nav_search:

                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this, OrderActivity.class));
                    SearchActivity.this.finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    SearchActivity.this.finish();
                    break;

            }
            return true;
        });
    }

    public void action_search(View v) {
        if (v.getId() == R.id.button_search_reset) {
            name.setText("");
            pricemin.setText("");
            pricemax.setText("");
        } else if (v.getId() == R.id.button_search_search) {
            //20220907 edit by鄭紹謙
            //取輸入的產品名稱
            String input_name = name.getText().toString();
            String input_max = pricemax.getText().toString();
            String input_min = pricemin.getText().toString();
            if ((input_name.equals("") || input_name == null) && (input_max.equals("") || input_max == null) && (input_min.equals("") || input_min == null)) {
                Toast.makeText(SearchActivity.this, "請輸入搜尋條件", Toast.LENGTH_LONG).show();
                return;
            }
            if (!(input_max.equals(""))) {

                if ((Integer.parseInt(input_max) > 99999)||(Integer.parseInt(input_max) < 0)) {
                    Toast.makeText(SearchActivity.this, "條件設定過大或為負數，請重新指定", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            if (!(input_min.equals(""))) {

                if ((Integer.parseInt(input_min) < 0)||(Integer.parseInt(input_min) > 99999)) {
                    Toast.makeText(SearchActivity.this, "條件設定過大或為負數，請重新指定", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            if (!(input_max.equals("")) && !(input_min.equals(""))) {
                if (Integer.parseInt(input_max) < Integer.parseInt(input_min)) {
                    Toast.makeText(SearchActivity.this, "最小值不可大於最大值", Toast.LENGTH_LONG).show();
                    return;
                }
            }


            //呼叫SharedPreferences儲存輸入的名稱
            SharedPreferences sp;
            SharedPreferences.Editor spe;
            sp = getSharedPreferences("search", MODE_PRIVATE);
            spe = sp.edit();
            spe.clear();
            spe.putString("input_name", input_name).commit();
            spe.putString("input_min", input_min).commit();
            spe.putString("input_max", input_max).commit();
            spe.commit();
            //重定向到ResultActivity
            startActivity(new Intent(this, ResultActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
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