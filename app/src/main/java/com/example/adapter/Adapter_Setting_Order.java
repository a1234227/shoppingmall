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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Setting_Order extends RecyclerView.Adapter<Adapter_Setting_Order.myViewHolder> {
    List<String> mData;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<int[]> products = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> first = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();
    ArrayList<Product> pro = new ArrayList<>();
    ArrayList<Customer_order> order = new ArrayList<>();
    Bitmap bm;
    String json;

    int max,left;

    public Adapter_Setting_Order(AppCompatActivity c) {

        sp = c.getSharedPreferences("info", Context.MODE_PRIVATE);
        String account = sp.getString("account", null);
//        MD5Tools base64=new MD5Tools();
//        json= base64.base64Encryption(json);

        String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_customer_order_list&account=" + account;
        Log.i("path7655", path);
        GetFromCloud gfc = new GetFromCloud(c);
        json = gfc.getText(path);
        Gson gson = new Gson();

        Log.i("msg11235", json);
//        product.add(gson.fromJson(json,Product.class));
        sp = c.getSharedPreferences("ordered_products", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        sp.edit().putString("json",json).commit();
        order = gson.fromJson(json, new TypeToken<ArrayList<Customer_order>>() {
        }.getType());
        for (int i = 0; i < order.size(); i++) {
            id.add(order.get(i).getOrder_id());

            products.add(order.get(i).getProduct_id());
            first.add(String.valueOf(order.get(i).getProduct_id()[0]));
            path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_product_lite&id=" + first.get(i);
            Product first_obj = gson.fromJson(gfc.getText(path), Product.class);
            name.add(first_obj.getPro_name());
            price.add(String.valueOf(first_obj.getPro_price()));
            if (first_obj.getPro_image().equals("") || first_obj.getPro_image() == null) {
                bm = gfc.getImg("https://i.imgur.com/6k6XPJN.jpg");
            } else {
                bm = gfc.getImg(first_obj.getPro_image());
            }
            img.add(bm);
        }

//        for (int i = 0; i < id.size(); i++) {
//
//
//            for (int j = 0; j < products.get(i).length; j++) {
//                path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_product_lite&id=" + products.get(i)[j];
//                String receive=gfc.getText(path);
//                Product product=gson.fromJson(receive,Product.class);
//                product.setEnum_id(i+1);
//                pro.add(product);
//            }
//        }
//        ArrayList<Integer> counter=new ArrayList<>();
//        for(int i=0;i<pro.size();i++){
//            counter.add(pro.get(i).getEnum_id());
//        }
//        int pos=1;
//        max = counter.get(counter.size()-1);
//        for(int i=0;i<max;i++){
//            pos=1;
//            for(int j=0;j<pro.size();j++){
//                if(pro.get(j).getEnum_id()==i+1){
//                    sp = c.getSharedPreferences("ordered_products", Context.MODE_PRIVATE);
//                    sp.edit().putString("name"+(i+1)+"-"+pos,pro.get(j).getPro_name()).commit();
//                    sp.edit().putString("price"+(i+1)+"-"+pos,pro.get(j).getPro_price().toString()).commit();
//                    sp.edit().putString("img"+(i+1)+"-"+pos,pro.get(j).getPro_image()).commit();
//                    pos++;
//                }
//            }
//        }

//        for (int i = 0; i < id.size(); i++) {
//            name.add(order.get(i).getPro_name());
//            price.add(product.get(i).getPro_price().toString());
//            if (product.get(i).getPro_image().equals("") || product.get(i).getPro_image() == null) {
//                img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
//            } else {
//                img.add(gfc.getImg(product.get(i).getPro_image()));
//            }
//        }


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

        TextView n, p,amount;
        ImageView imageview;

        public myViewHolder(@NonNull View v) {
            super(v);
            amount=v.findViewById(R.id.text_order_detail_amount);
            n = v.findViewById(R.id.text_order_list_name);
            p = v.findViewById(R.id.text_order_list_price);
            imageview = v.findViewById(R.id.imageview_list_order);
        }
    }

    @NonNull
    @Override
    public Adapter_Setting_Order.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_list, parent, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Setting_Order.myViewHolder vh, int position) {
        vh.amount.setVisibility(View.INVISIBLE);
        vh.n.setText("訂單: " + id.get(position));
        vh.p.setText("此訂單共有: "+(order.get(position).getProduct_id().length)+" 項商品");
        vh.imageview.setImageBitmap(img.get(position));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }
}
