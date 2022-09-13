package com.example.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.Customer_order;
import com.example.entity.Product;
import com.example.shoppingmall.GetFromCloud;
import com.example.shoppingmall.LoginActivity;
import com.example.shoppingmall.R;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Order_Detail extends RecyclerView.Adapter<Adapter_Order_Detail.myViewHolder> {
    List<String> mData;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ArrayList<Integer> id = new ArrayList<>();
//    ArrayList<Integer> amount = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();
    String json;
    Gson gson=new Gson();
    int[] amount;
    BigDecimal total=new BigDecimal(0);
    BigDecimal item=new BigDecimal(0);
    BigDecimal times=new BigDecimal(0);

    public Adapter_Order_Detail(AppCompatActivity c) {

        sp= c.getSharedPreferences("ordered_products", Context.MODE_PRIVATE);
        json=sp.getString("json_send",null);
//        MD5Tools base64=new MD5Tools();
//        json= base64.base64Encryption(json);
        Customer_order co=new Customer_order();
        co=gson.fromJson(json,Customer_order.class);
        amount=new int[co.getAmount().length];
        String order_id= String.valueOf(co.getOrder_id());
        int[] products=co.getProduct_id();
        for(int i=0;i<products.length;i++) {
            String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_product_lite&id=" + products[i];
            GetFromCloud gfc = new GetFromCloud(c);
            json=gfc.getText(path);
            Product pro =new Product();
            pro=gson.fromJson(json,Product.class);
            name.add(pro.getPro_name());
            price.add(String.valueOf(pro.getPro_price()));
            Log.i("total",pro.getPro_price().toString());
            times= BigDecimal.valueOf(co.getAmount()[i]);
            item=pro.getPro_price();
            item=item.multiply(times);
            total=total.add(item);
            item=new BigDecimal(0);
            times=new BigDecimal(0);
            if(pro.getPro_image()==null||pro.getPro_image().equals("")){
                img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
            }else{
                img.add(gfc.getImg(pro.getPro_image()));
            }
            amount[i]=co.getAmount()[i];
        }
        Log.i("msg",total.toString());
        sp= c.getSharedPreferences("total", Context.MODE_PRIVATE);
        sp.edit().putString("total",total.toString()).commit();




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

    class myViewHolder extends RecyclerView.ViewHolder {

            TextView n,p,a;
            ImageView imageview;

        public myViewHolder(@NonNull View v) {
            super(v);
            n =v.findViewById(R.id.text_order_list_name);
            p=v.findViewById(R.id.text_order_list_price);
            a=v.findViewById(R.id.text_order_detail_amount);
            imageview=v.findViewById(R.id.imageview_list_order);
        }
    }

    @NonNull
    @Override
    public Adapter_Order_Detail.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_list,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Order_Detail.myViewHolder vh, int position) {
        vh.n.setText("品名: " + name.get(position));
        vh.p.setText("單價: " + price.get(position).substring(0,price.get(position).length()-4)+" 元");
        vh.a.setText("數量:"+amount[position]);
        vh.imageview.setImageBitmap(img.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}
