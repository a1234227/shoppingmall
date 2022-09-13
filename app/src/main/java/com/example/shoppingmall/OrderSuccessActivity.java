package com.example.shoppingmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        getSupportActionBar().hide();
    }

    public void action_order_success(View v){

        if(v.getId()==R.id.button_order_success_home){
            startActivity(new Intent(this,HomeActivity.class));
            this.finish();
        }else if(v.getId()==R.id.button_order_success_myorder){
            startActivity(new Intent(this,BrowseAllOrderActivity.class));
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
        this.finish();
    }
}