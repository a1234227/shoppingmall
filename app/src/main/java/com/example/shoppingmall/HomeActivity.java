package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adapter.Adapter_Home;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class HomeActivity extends AppCompatActivity {

    ListView listview;
    Spinner spinner;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView left_nav;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String keyword="";
    Adapter_Home adapter_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        left_nav = findViewById(R.id.left_nav);
        left_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:

                        break;
                    case R.id.nav_search:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                        HomeActivity.this.finish();
                        break;
                    case R.id.nav_order:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                        HomeActivity.this.finish();
                        break;
                    case R.id.nav_settings:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        HomeActivity.this.finish();
                        break;

                }
                return false;
            }
        });
        getSupportActionBar().setTitle("商城主頁");
        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.navigation_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:

                        break;
                    case R.id.nav_search:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                        HomeActivity.this.finish();
                        break;
                    case R.id.nav_order:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                        HomeActivity.this.finish();
                        break;
                    case R.id.nav_settings:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        HomeActivity.this.finish();
                        break;

                }
                return true;
            }
        });

        listview = findViewById(R.id.listview_home);
        spinner = findViewById(R.id.spinner_home);
        GetFromCloud gfc = new GetFromCloud(HomeActivity.this);
        String path = LoginActivity.SERVER_ADDRESS + "customer_query?value=category";
        String json_enum = gfc.getText(path);
//        Log.i("msg", json_enum);
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<Integer> sort = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json_enum);
            Iterator<String> it = obj.keys();
                int i=1;
            while (it.hasNext()) {
                JSONObject sub = (JSONObject) obj.get(it.next());
                sort.add(i);
                id.add((String) sub.get("id"));
                name.add((String) sub.get("name"));
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

            String[] names = new String[name.size()];
        String[] ids = new String[id.size()];
        Integer[] sorts = new Integer[sort.size()];
            for (int i = 0; i < name.size(); i++) {
                names[i] = name.get(i);
                ids[i] = id.get(i);
                sorts[i]= sort.get(i);
//                Log.i("msg", String.valueOf(sorts[i]));
//                Log.i("msg", ids[i]);
//                Log.i("msg", names[i]);
            }
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(
                this, R.layout.custom_spinner_layout,names);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keyword=spinner.getSelectedItem().toString();
                Log.i("selected",keyword);
                adapter_home = Adapter_Home.factoryCreate(HomeActivity.this,keyword);
                listview.setAdapter(adapter_home);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String id = adapter_home.id.get(i);
                        Log.i("msg",id);
                        sp = getSharedPreferences("product", MODE_PRIVATE);
                        spe=sp.edit();
                        spe.putString("id",id);
                        spe.commit();
                        sp=getSharedPreferences("product_destination",MODE_PRIVATE);
                        sp.edit().putString("destination", "home").commit();
                        Intent it = new Intent(HomeActivity.this,ProductActivity.class);
                        startActivity(it);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        grid2=findViewById(R.id.grid2);
//        grid3=findViewById(R.id.grid3);

//        grid2.setAdapter(ma);
//        grid3.setAdapter(ma);
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences("info", MODE_PRIVATE);
        spe = sp.edit();
        String realname = sp.getString("realname", "");
        Toast.makeText(this, "歡迎," + realname, Toast.LENGTH_LONG).show();
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem i) {

        if (actionBarDrawerToggle.onOptionsItemSelected(i)) {
            return true;
        }
        return super.onOptionsItemSelected(i);


//        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);


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