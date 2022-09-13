package com.example.adapter;

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

import com.example.shoppingmall.GetFromCloud;
import com.example.shoppingmall.LoginActivity;
import com.example.shoppingmall.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class Adapter_Home extends ArrayAdapter {
    AppCompatActivity c;

    ArrayList<Integer> sort = new ArrayList<>();
    public ArrayList<String> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<Bitmap> img = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Adapter_Home factoryCreate(AppCompatActivity context, String keyword) {
        return new Adapter_Home(context, keyword, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Adapter_Home(@NonNull AppCompatActivity context, String keyword, int resource) {
        super(context, resource);
        c = context;

        GetFromCloud gfc = new GetFromCloud(c);
        try {
            String json = gfc.getText(LoginActivity.SERVER_ADDRESS + "customer_query?value=get_list&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
            JSONObject obj = new JSONObject(json);
            Iterator<String> it = obj.keys();
            while (it.hasNext()) {
                JSONObject sub = (JSONObject) obj.get(it.next());
                int i = 1;
                sort.add(i);
                id.add((String) sub.get("id"));
                name.add((String) sub.get("name"));
                price.add((String) sub.get("price"));
                if (sub.get("img").equals("") || sub.get("img") == null) {
                    img.add(gfc.getImg("https://i.imgur.com/6k6XPJN.jpg"));
                } else {
                    img.add(gfc.getImg((String) sub.get("img")));
                }
                i++;
            }
        } catch (JSONException | UnsupportedEncodingException e) {
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
        p.setText("單價: " + price.get(position)+" 元");
        imageView.setImageBitmap(img.get(position));
        return lo;
    }

}