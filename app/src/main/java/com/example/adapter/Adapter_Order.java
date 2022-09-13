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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.Product;
import com.example.shoppingmall.GetFromCloud;
import com.example.shoppingmall.LoginActivity;
import com.example.shoppingmall.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Adapter_Order extends ArrayAdapter {
    AppCompatActivity c;
    //    String[] name={"A","B","C","D","E","F","G","H","I","J"};
//    String[] price={"1","2","3","4","5","6","7","8","9","10"};
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    public ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    public ArrayList<Integer> amount_list = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();
    ArrayList<Integer> position_checked = new ArrayList();
    public ArrayList<Boolean> checked = new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Adapter_Order factoryCreate(AppCompatActivity context) {
        return new Adapter_Order(context, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Adapter_Order(@NonNull AppCompatActivity context, int resource) {
        super(context, resource);
        c = context;
        sp= c.getSharedPreferences("info", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String account=sp.getString("account",null);
        String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=cart_query&account="+account;
        GetFromCloud gfc=new GetFromCloud(c);
        String json=gfc.getText(path);
        json=json.substring(1,json.length()-1);
        try {
            json= URLEncoder.encode(json,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        MD5Tools base64=new MD5Tools();
//        json=base64.base64Encryption(json);
        path = LoginActivity.SERVER_ADDRESS + "customer_query?value=get_order_list&ids="+json;
        String json1=gfc.getText(path);
        Log.i("pathqqq",json1);
        ArrayList<Product> list = gson.fromJson(json1, new TypeToken<ArrayList<Product>>() {
        }.getType());
        for(int i=0;i<list.size();i++){
            id.add(list.get(i).getPro_id());
            name.add(list.get(i).getPro_name());
            price.add(list.get(i).getPro_price().toString());
//            img.add(gfc.getImg(list.get(i).getPro_image()));
            checked.add(true);

            if (list.get(i).getPro_image().equals("") ||list.get(i).getPro_image()== null) {
                img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
            } else {
                img.add(gfc.getImg(list.get(i).getPro_image()));
            }
            amount_list.add(1);

        }



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
//        amount_list.clear();
        LayoutInflater li = c.getLayoutInflater();
        ConstraintLayout lo;
        lo = (ConstraintLayout) li.inflate(R.layout.row_order, null);
        TextView n = lo.findViewById(R.id.text_order_name);
        TextView p = lo.findViewById(R.id.text_order_price);
        CheckBox cb = lo.findViewById(R.id.checkBox);
        TextView amount=lo.findViewById(R.id.text_order_amount);
        Button plus,minus;
        plus=lo.findViewById(R.id.button_order_increace);
        minus=lo.findViewById(R.id.button_order_decreace);
        ImageView imageview = lo.findViewById(R.id.imageview_order);
        final boolean[] status = {cb.isSelected()};
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=Integer.parseInt(amount.getText().toString());
                if(count>=50){
                    return;
                }
                amount.setText(String.valueOf(count+1));
                amount_list.set(position,count+1);
                Log.i("amount",amount_list.toString());
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=Integer.parseInt(amount.getText().toString());
                if(count<=1){
                    return;
                }
                amount.setText(String.valueOf(count-1));
                amount_list.set(position,count-1);
                Log.i("amount",amount_list.toString());
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(status[0] ==true){
//                    status[0] =false;
//                }else{
//                    status[0]=true;
//                }
//                position_checked.add(position);
//                checked.add(status[0]);
                if (cb.isChecked()) {
                    checked.set(position, true);

                } else {
                    checked.set(position, false);
                }
            }
        });
        n.setText("品名: " + name.get(position));
        p.setText("單價: " + price.get(position).substring(0,price.get(position).length()-4));
        amount.setText(amount_list.get(position).toString());
        imageview.setImageBitmap(img.get(position));
        cb.setChecked(true);

//        amount.getText();
//        amount_list.add(Integer.parseInt(amount.getText().toString()));
//        Log.i("msg3",amount_list.toString());

        return lo;
    }

}