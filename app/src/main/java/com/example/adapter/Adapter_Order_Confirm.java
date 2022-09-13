package com.example.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.Product;
import com.example.shoppingmall.GetFromCloud;
import com.example.shoppingmall.LoginActivity;
import com.example.shoppingmall.OrderActivity;
import com.example.shoppingmall.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Adapter_Order_Confirm extends ArrayAdapter {
    AppCompatActivity c;
    //    String[] name={"A","B","C","D","E","F","G","H","I","J"};
//    String[] price={"1","2","3","4","5","6","7","8","9","10"};
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();
    ArrayList<Integer> amount = new ArrayList<>();
    public String json;
    public BigDecimal total=new BigDecimal(0);


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Adapter_Order_Confirm factoryCreate(AppCompatActivity context) {
        return new Adapter_Order_Confirm(context, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Adapter_Order_Confirm(@NonNull AppCompatActivity context, int resource) {
        super(context, resource);
        c = context;
        sp= c.getSharedPreferences("order_list", Context.MODE_PRIVATE);
        json=sp.getString("order_list",null);
//        MD5Tools base64=new MD5Tools();
//        json= base64.base64Encryption(json);
        try {
            json=URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path= LoginActivity.SERVER_ADDRESS+"customer_query?value=get_order_list&ids="+json;
        GetFromCloud gfc=new GetFromCloud(c);
        json=gfc.getText(path);
        Gson gson=new Gson();
        ArrayList<Product> product=new ArrayList<>();
        Log.i("msg1",json);
//        product.add(gson.fromJson(json,Product.class));
        product=gson.fromJson(json, new TypeToken<ArrayList<Product>>() {
        }.getType());

        for(int i=0;i<product.size();i++){
            name.add(product.get(i).getPro_name());
            price.add(product.get(i).getPro_price().toString());
            if (product.get(i).getPro_image().equals("") ||product.get(i).getPro_image()== null) {
                img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
            } else {
                img.add(gfc.getImg(product.get(i).getPro_image()));
            }
            amount.add(OrderActivity.checked_amount.get(i));
            total=total.add(product.get(i).getPro_price().multiply(BigDecimal.valueOf(OrderActivity.checked_amount.get(i))));
        }


//        String account=sp.getString("account",null);
//        String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=cart_query&account="+account;
//        GetFromCloud gfc=new GetFromCloud(c);
//        String json=gfc.getText(path);
//        try {
//            json= URLEncoder.encode(json,"utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_order_list&ids="+json;



    }

    @Override
    public int getCount() {
        int x = name.size();
        return x;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                TextView t = new TextView(MainActivity.this);
//
//                t.setText("This is a TEST");
//                t.setTextColor(Color.CYAN);
//                t.setTextSize(20.0f);
//
//
//
//                ImageView i = new ImageView(MainActivity.this);
//                i.setImageBitmap(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.apple));
//
//                LinearLayout lo=new LinearLayout(MainActivity.this);
//                lo.setOrientation(LinearLayout.HORIZONTAL);
//                lo.addView(t);
//                lo.addView(i);
//                lo.setBackgroundColor(Color.RED);

        LayoutInflater li = c.getLayoutInflater();
        ConstraintLayout lo;
        lo = (ConstraintLayout) li.inflate(R.layout.row_order_confirm, null);
        TextView n = lo.findViewById(R.id.text_order_confirm_name);
        TextView p = lo.findViewById(R.id.text_order_confirm_price);
        TextView a= lo.findViewById(R.id.text_order_confirm_amount);
        ImageView imageview = lo.findViewById(R.id.imageview_confirm_order);
        n.setText("品名: " + name.get(position));
        p.setText("單價: " + price.get(position).substring(0,price.get(position).length()-4));
        a.setText("數量: "+amount.get(position));
        imageview.setImageBitmap(img.get(position));

        return lo;
    }

}