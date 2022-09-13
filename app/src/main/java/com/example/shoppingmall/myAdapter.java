package com.example.shoppingmall;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class myAdapter extends ArrayAdapter {
    AppCompatActivity c;
    String[] name={"A","B","C","D","E","F","G","H","I","J"};
    String[] price={"1","2","3","4","5","6","7","8","9","10"};

//    ArrayList<String> name = new ArrayList<>();
//    ArrayList<Double> price = new ArrayList<>();
//    ArrayList<Bitmap> img2 = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static myAdapter factoryCreate(AppCompatActivity context) {
        return new myAdapter(context, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public myAdapter(@NonNull AppCompatActivity context, int resource) {
        super(context, resource);
        c = context;


//        Path p = Paths.get("/data/data/com.my.eshop/data.json");
//
//        Thread t = new Thread() {
//
//            URL u;
//
//            {
//                try {
//                    u = new URL("https://thisserversucks-2f053.web.app/data/data/data.json");
//                    InputStream is = u.openStream();
//                    byte[] raw = new byte[1024]; // 1K 1024 = 2 ^10
//                    is.read(raw);
//                    String result = new String(raw);
//                    Log.i("DATA", result.trim());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        };
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getCount() {
        int x = name.length;
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

        lo = (ConstraintLayout) li.inflate(R.layout.row1, null);

        TextView n = lo.findViewById(R.id.text_home_name);
        TextView p = lo.findViewById(R.id.text_home_price);
//        ImageView img = lo.findViewById(R.id.img);
        n.setText("品名: " + name[position]);
        p.setText("價格: " + price[position]);
//        img.setImageBitmap(img2.get(position));
        return lo;
    }

}