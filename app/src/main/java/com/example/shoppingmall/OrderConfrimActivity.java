package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.Adapter_Order_Confirm;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OrderConfrimActivity extends AppCompatActivity {
    ListView listView;
    Adapter_Order_Confirm aoc;
    SharedPreferences sp;
    Gson gson=new Gson();
    Button submit;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confrim);
        getSupportActionBar().setTitle("訂單確認");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_order_confirm);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_order);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    OrderConfrimActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    OrderConfrimActivity.this.finish();
                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this, OrderActivity.class));
                    OrderConfrimActivity.this.finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    OrderConfrimActivity.this.finish();
                    break;

            }
            return true;
        });
        submit=findViewById(R.id.button_order_confirm_submit);
        listView = findViewById(R.id.listview_order_confirm);
        aoc = Adapter_Order_Confirm.factoryCreate(OrderConfrimActivity.this);
        listView.setAdapter(aoc);
        if(aoc.getCount()==0){
            submit.setVisibility(View.INVISIBLE);
        }else{
            submit.setVisibility(View.VISIBLE);
        }

        total=findViewById(R.id.text_order_confirm_total);
        total.setText("共計: "+aoc.total.toString().substring(0,aoc.total.toString().length()-4));
    }

    public void action_order_confirm(View v) {
        if (v.getId() == R.id.button_order_confirm_submit) {
            Log.i("msg", aoc.json);
            sp = getSharedPreferences("info", MODE_PRIVATE);
            String account = sp.getString("account", null);
            String data = null;
            try {
                data = URLEncoder.encode(aoc.json, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String amount=gson.toJson(OrderActivity.checked_amount);
//            MD5Tools base64=new MD5Tools();
//            data=base64.base64Encryption(aoc.json);
            try {
                amount = URLEncoder.encode(amount, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=order_add&account=" + account  + "&data=" + data+ "&amount=" + amount;
            Log.i("msg345",path);
            GetFromCloud gfc = new GetFromCloud(OrderConfrimActivity.this);
            String r = gfc.getText(path);
            Log.i("msg",r);
            if (r.equals("OK")) {
                startActivity(new Intent(this, OrderSuccessActivity.class));
                this.finish();
            }else{
                Toast.makeText(OrderConfrimActivity.this,"送出訂單發生錯誤",Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.button_order_confirm_back) {
            startActivity(new Intent(this, OrderActivity.class));
            this.finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                startActivity(new Intent(this,OrderActivity.class));
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}