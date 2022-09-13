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
import com.example.shoppingmall.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Adapter_Result extends ArrayAdapter {
    AppCompatActivity c;
    SharedPreferences sp;
    String keyword,max,min;
    ArrayList<Integer> sort = new ArrayList<>();
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<BigDecimal> price = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Adapter_Result factoryCreate(AppCompatActivity context, String keyword) {
        return new Adapter_Result(context, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Adapter_Result(@NonNull AppCompatActivity context, int resource) {
        super(context, resource);
        c = context;
        sp=c.getSharedPreferences("search", Context.MODE_PRIVATE);
        keyword = sp.getString("input_name", "");
        max = sp.getString("input_max", "");
        min = sp.getString("input_min", "");
        GetFromCloud gfc = new GetFromCloud(c);
        try {
            String path= LoginActivity.SERVER_ADDRESS + "customer_query?value=search_product&keyword=" + URLEncoder.encode(keyword, "UTF-8")+"&max="+max+"&min="+min;
            Log.i("path8888",path);
            String json = gfc.getText(path);
            Gson gson=new Gson();
            ArrayList<Product> data =gson.fromJson(json,new TypeToken<ArrayList<Product>>() {
            }.getType());
            for (int i = 0; i < data.size() ; i++) {
                sort.add(i);
                id.add(data.get(i).getPro_id());
                name.add(data.get(i).getPro_name());
                price.add(data.get(i).getPro_price());

                if (data.get(i).getPro_image().equals("") ||data.get(i).getPro_image()== null) {
                    img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
                } else {
                    img.add(gfc.getImg(data.get(i).getPro_image()));
                }
            }




        } catch ( UnsupportedEncodingException e) {
            e.printStackTrace();
        }





        Log.i("msg", sort.toString());
        Log.i("msg", name.toString());
        Log.i("msg", price.toString());
        Log.i("msg", img.toString());


    }

    @Override
    public int getCount() {
        int x = name.size();
        return x;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = c.getLayoutInflater();
        ConstraintLayout lo;

        lo = (ConstraintLayout) li.inflate(R.layout.row_home, null);

        TextView n = lo.findViewById(R.id.text_home_name);
        TextView p = lo.findViewById(R.id.text_home_price);
        ImageView imageView = lo.findViewById(R.id.imageview_home);
        n.setText("品名: " + name.get(position));
        p.setText("單價: " + price.get(position));

        GetFromCloud gfc = new GetFromCloud();
        imageView.setImageBitmap(img.get(position));
        return lo;
    }

}