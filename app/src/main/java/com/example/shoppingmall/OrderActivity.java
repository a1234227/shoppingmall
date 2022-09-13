package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.Adapter_Order;
import com.example.entity.Customer_cart;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Boolean> checkbox_status=new ArrayList<>();
    ArrayList<Integer> checked_position=new ArrayList<>();
    ArrayList<Integer> checked_ids=new ArrayList<>();
    public static ArrayList<Integer> checked_amount=new ArrayList<>();
    ArrayList<Customer_cart> checked_products=new ArrayList<>();
    Adapter_Order mao;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    Button submit;
    Button remove;
    TextView amount;
    ImageView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setTitle("購物車");

        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_order);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_order);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    OrderActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    OrderActivity.this.finish();
                    break;
                case R.id.nav_order:

                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    OrderActivity.this.finish();
                    break;

            }
            return true;
        });
        lv=findViewById(R.id.listview_order);
        submit=findViewById(R.id.button_order_submit);
        remove=findViewById(R.id.button_order_remove);
        mao = Adapter_Order.factoryCreate(OrderActivity.this);
        empty=findViewById(R.id.imageview_order_empty);


        lv.setAdapter(mao);
        if(mao.getCount()==0){
            submit.setVisibility(View.INVISIBLE);
            remove.setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);
        }else {
            submit.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
        }




    }

    public void action_order(View v){

        if(v.getId()==R.id.button_order_submit){
//        amount=mao.getView(1,null,null).findViewById(R.id.text_order_amount);
//        Log.i("msg31",amount.getText().toString());
            checkbox_status=mao.checked;
            checked_position.clear();


            for(int i=0;i<checkbox_status.size();i++){
                if(checkbox_status.get(i)==true){
                    checked_position.add(i);
                }
            }
            Customer_cart cc=new Customer_cart();
                checked_ids.clear();
                checked_amount.clear();
            for(int i=0;i<checked_position.size();i++){
                checked_ids.add(mao.id.get(checked_position.get(i)));
                checked_amount.add(mao.amount_list.get(checked_position.get(i)));
            }
            int[] ids_arr=new int[checked_ids.size()];
            int[] amount_arr=new int[checked_ids.size()];
            for(int i=0;i<checked_ids.size();i++){
                ids_arr[i]=checked_ids.get(i);
//                amount_arr[i]=checked_amount.get(i);
            }
            sp=getSharedPreferences("info",MODE_PRIVATE);
            cc.setAccount(sp.getString("account",null));
            cc.setProduct_id(ids_arr);
            Gson gson=new Gson();
            String json=gson.toJson(cc,Customer_cart.class);
            Log.i("msg_json",json);
            sp=getSharedPreferences("order_list",MODE_PRIVATE);
            sp.edit().putString("order_list",json).commit();
            startActivity(new Intent(this,OrderConfrimActivity.class));


        }
        else if(v.getId()==R.id.button_order_remove){
            checkbox_status=mao.checked;
            checked_position.clear();
            for(int i=0;i<checkbox_status.size();i++){
                if(checkbox_status.get(i)==true){
                    checked_position.add(i);
                }
            }
            Customer_cart cc=new Customer_cart();
            checked_ids.clear();
            for(int i=0;i<checked_position.size();i++){
                checked_ids.add(mao.id.get(checked_position.get(i)));
            }
            int[] ids_arr=new int[checked_ids.size()];
            for(int i=0;i<checked_ids.size();i++){
                ids_arr[i]=checked_ids.get(i);
            }
            sp=getSharedPreferences("info",MODE_PRIVATE);
            cc.setAccount(sp.getString("account",null));
            cc.setProduct_id(ids_arr);
            Gson gson=new Gson();
            String json=gson.toJson(cc,Customer_cart.class);
//            try {
//                json= URLEncoder.encode(json,"UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            MD5Tools base64=new MD5Tools();
//            json=base64.base64Encryption(json);
            try {
                json=URLEncoder.encode(json,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String path=LoginActivity.SERVER_ADDRESS+"customer_query?value=cart_remove&data="+json;
            GetFromCloud gfc=new GetFromCloud(OrderActivity.this);
            String result=gfc.getText(path);
            Log.i("msg",result);
//            if(result.equals("OK")){
//
//            }
                mao=Adapter_Order.factoryCreate(OrderActivity.this);
                lv.setAdapter(mao);
            if(mao.getCount()==0){
                submit.setVisibility(View.INVISIBLE);
                remove.setVisibility(View.INVISIBLE);
                empty.setVisibility(View.VISIBLE);
            }else {
                submit.setVisibility(View.VISIBLE);
                remove.setVisibility(View.VISIBLE);
                empty.setVisibility(View.INVISIBLE);
            }
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