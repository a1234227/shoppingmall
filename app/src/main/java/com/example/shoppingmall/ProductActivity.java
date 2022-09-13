package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Customer_cart;
import com.example.entity.Product;
import com.example.tools.MD5Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;


public class ProductActivity extends AppCompatActivity {
    TextView name, category, desc, stock, price;
    ImageView img;
    String id = "";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ArrayList<Product> data;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("商品資訊");
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_product);
//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    ProductActivity.this.finish();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    ProductActivity.this.finish();
                    break;
                case R.id.nav_order:
                    startActivity(new Intent(this, OrderActivity.class));
                    ProductActivity.this.finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    ProductActivity.this.finish();
                    break;

            }
            return true;
        });

        name = this.findViewById(R.id.text_product_name);
        category = this.findViewById(R.id.text_product_category);
        desc = this.findViewById(R.id.text_product_desc);
        stock = this.findViewById(R.id.text_product_stock);
        price = this.findViewById(R.id.text_product_price);
        img = this.findViewById(R.id.imageview_product);
        sp = getSharedPreferences("product", MODE_PRIVATE);
        id = sp.getString("id", "1");
        Gson gson = new Gson();
        GetFromCloud gfc = new GetFromCloud(ProductActivity.this);
        String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_product&id=" + id;
        String raw_data = gfc.getText(path);
        data = gson.fromJson(raw_data, new TypeToken<ArrayList<Product>>() {
        }.getType());
        Log.i("data", data.toString());
        name.setText(data.get(0).getPro_name());
        category.setText(data.get(0).getEnum_name());
        desc.setText(data.get(0).getPro_desc());
        stock.setText(String.valueOf(data.get(0).getPro_stock()));
        int price_length = data.get(0).getPro_price().toString().length();
        price.setText((data.get(0).getPro_price().toString().substring(0, price_length - 4)) + "元");
        if (data.get(0).getPro_image().equals("") ||data.get(0).getPro_image()== null) {
            img.setImageBitmap(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
        } else {
            img.setImageBitmap(gfc.getImg(data.get(0).getPro_image()));
        }
        submit=findViewById(R.id.button_product_submit);
        if(data.get(0).getPro_stock()<1){
            submit.setVisibility(View.INVISIBLE);
            stock.setTextColor(getResources().getColor(R.color.Error));
        }


    }

    public void action_product(View v) {

        if (v.getId() == R.id.button_product_submit) {

                /*sp = getSharedPreferences("item_cart", MODE_PRIVATE);
                spe=sp.edit();
                int item_id=data.get(0).getPro_id();
                int current_position=0;
                int position=-1;
                for(int i=1;current_position!=-1;i++) {
                    current_position = sp.getInt(String.valueOf(i), -1);
                    position=i;
                    if(current_position==-1){
                        break;
                    }
                }
                if(position==-1){
                    spe.putInt("1",1);
                    spe.putInt("item_cart_1",item_id);
                    spe.putInt("item_cart_2",-2);
                    spe.commit();
                }else if(position!=-1){
                    int result=0;
                    for(int i=1;result!=-1;i++){
                        result=sp.getInt("item_cart_"+i,-1);
                        if(result==item_id){
                            Toast.makeText(ProductActivity.this,"購物車內已有此商品",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    spe.putInt(String.valueOf(position),position);
                    spe.putInt("item_cart_"+position,item_id);
                    spe.commit();
                }*/
            sp = getSharedPreferences("info", MODE_PRIVATE);
            String account = sp.getString("account", null);
            Customer_cart send = new Customer_cart();
            String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=cart_query&account=" + account;
            GetFromCloud gfc = new GetFromCloud(ProductActivity.this);
            String raw = gfc.getText(path);
            ArrayList<Customer_cart> data = new ArrayList<>();
            Gson gson = new Gson();
            data = gson.fromJson(raw, new TypeToken<ArrayList<Customer_cart>>() {
            }.getType());
            account = data.get(0).getAccount();
            ArrayList<Integer> ids_origin_arr = new ArrayList<>();
            ArrayList<Integer> ids_new_arr = new ArrayList<>();
            int[] ids_origin = data.get(0).getProduct_id();
            int[] ids_new;
            if (ids_origin.length != 0) {
                for (int i = 0; i < ids_origin.length; i++) {
                    ids_origin_arr.add(ids_origin[i]);
                }
                ids_new_arr.addAll(ids_origin_arr);
            }
            if (ids_new_arr.contains(Integer.parseInt(id))) {
                Toast.makeText(this, "購物車內已經有此商品", Toast.LENGTH_LONG).show();
                return;
            }
            ids_new_arr.add(Integer.parseInt(id));
            ids_new=new int[ids_new_arr.size()];
            for (int i=0;i<ids_new_arr.size();i++){
            ids_new[i]=ids_new_arr.get(i);
            }
            Customer_cart send2=new Customer_cart();
            send2.setAccount(account);
            send2.setProduct_id(ids_new);
            Gson gson2=new Gson();
            String send_json=gson2.toJson(send2,Customer_cart.class);

//            MD5Tools base64=new MD5Tools();
//            send_json=base64.base64Encryption(send_json);
            try {
                send_json=URLEncoder.encode(send_json,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            path=LoginActivity.SERVER_ADDRESS+"customer_query?value=cart_renew&data="+send_json;
            Log.d("msg",path);
            String r=gfc.getText(path);
            Toast.makeText(this, "成功加入購物車", Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button_product_follow) {

//                sp = getSharedPreferences("item_follow", MODE_PRIVATE);
//                spe=sp.edit();
//                int item_id=data.get(0).getPro_id();
//                int current_position=0;
//                int position=-1;
//                for(int i=1;current_position!=-1;i++) {
//                    current_position = sp.getInt(String.valueOf(i), -1);
//                    position=i;
//                    if(current_position==-1){
//                        break;
//                    }
//                }
//                if(position==-1){
//                    spe.putInt("1",1);
//                    spe.putInt("item_follow_1",item_id);
//                    spe.putInt("item_follow_2",-2);
//                    spe.commit();
//                }else if(position!=-1){
//                    int result=0;
//                    for(int i=1;result!=-1;i++){
//                        result=sp.getInt("item_follow_"+i,-1);
//                        if(result==item_id){
//                            Toast.makeText(ProductActivity.this,"追隨內已有此商品",Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                    spe.putInt(String.valueOf(position),position);
//                    spe.putInt("item_follow_"+position,item_id);
//                    spe.commit();
//                }
            sp = getSharedPreferences("info", MODE_PRIVATE);
            String account = sp.getString("account", null);
            Customer_cart send = new Customer_cart();
            String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=follow_query&account=" + account;
            GetFromCloud gfc = new GetFromCloud(ProductActivity.this);
            String raw = gfc.getText(path);
            ArrayList<Customer_cart> data = new ArrayList<>();
            Gson gson = new Gson();
            data = gson.fromJson(raw, new TypeToken<ArrayList<Customer_cart>>() {
            }.getType());
            account = data.get(0).getAccount();
            ArrayList<Integer> ids_origin_arr = new ArrayList<>();
            ArrayList<Integer> ids_new_arr = new ArrayList<>();
            int[] ids_origin = data.get(0).getProduct_id();
            int[] ids_new;
            if (ids_origin.length != 0) {
                for (int i = 0; i < ids_origin.length; i++) {
                    ids_origin_arr.add(ids_origin[i]);
                }
                ids_new_arr.addAll(ids_origin_arr);
            }
            if (ids_new_arr.contains(Integer.parseInt(id))) {
                Toast.makeText(this, "追隨列表內已經有此商品", Toast.LENGTH_LONG).show();
                return;
            }
            ids_new_arr.add(Integer.parseInt(id));
            ids_new=new int[ids_new_arr.size()];
            for (int i=0;i<ids_new_arr.size();i++){
                ids_new[i]=ids_new_arr.get(i);
            }
            Customer_cart send2=new Customer_cart();
            send2.setAccount(account);
            send2.setProduct_id(ids_new);
            Gson gson2=new Gson();
            String send_json=gson2.toJson(send2,Customer_cart.class);

//            MD5Tools base64=new MD5Tools();
//            send_json=base64.base64Encryption(send_json);
            try {
                send_json=URLEncoder.encode(send_json,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            path=LoginActivity.SERVER_ADDRESS+"customer_query?value=follow_renew&data="+send_json;
            Log.d("msg",path);
            String r=gfc.getText(path);
            Toast.makeText(this, "追隨商品成功", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                sp=getSharedPreferences("product_destination",MODE_PRIVATE);
                String destination=sp.getString("destination","home");
                if(destination.equals("home")){
                    startActivity(new Intent(ProductActivity.this,HomeActivity.class));
                    ProductActivity.this.finish();
                }else if(destination.equals("settings")){
                    startActivity(new Intent(ProductActivity.this,SettingsActivity.class));
                    ProductActivity.this.finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}