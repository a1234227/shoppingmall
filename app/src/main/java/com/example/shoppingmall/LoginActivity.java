package com.example.shoppingmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public final static String SERVER_ADDRESS="http://10.0.2.2:8080/eshopproject/";
    final static int REGISTER_RETURN=0;
    String[] useable_arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
            , "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    };
    String useable_String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    Character[] useable_char_arr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
            , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    byte[] arr = useable_String.getBytes();

    ArrayList<Byte> useable = new ArrayList<>();
    ArrayList<String> useable2;
    ArrayList<Character> useable3;

EditText account, password;
String path;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account=this.findViewById(R.id.input_login_account);
        password=this.findViewById(R.id.input_login_password);
        sp=getSharedPreferences("info",MODE_PRIVATE);
        if(!((sp.getString("realname","")).equals(""))&&!((sp.getString("account","")).equals(""))){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            LoginActivity.this.finish();
        }
    }

    public void action_login(View v){
        if(v.getId()==R.id.button_login_register) {
            startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class),REGISTER_RETURN);
        }else if(v.getId()==R.id.button_login_login){
            for (int i = 0; i < arr.length; i++) {
                useable.add(arr[i]);
            }
//            useable = Arrays.asList(useable_arr);
//            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            String t_account, t_password;
            t_account = account.getText().toString();
            t_password = password.getText().toString();
            if(t_account.equals("")||t_account==null||t_password.equals("")||t_password==null){
                Toast.makeText(this,"你有欄位沒填喔",Toast.LENGTH_LONG).show();
                return;
            }

            for (int i = 0; i < t_account.length(); i++) {

                if (!(useable.contains(t_account.getBytes()[i]))) {
                    Toast.makeText(LoginActivity.this, "帳號包含非法字元", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            for (int i = 0; i < t_password.length(); i++) {
                if (!(useable.contains(t_password.getBytes()[i]))) {
                    Toast.makeText(LoginActivity.this, "密碼包含非法字元", Toast.LENGTH_LONG).show();
                    return;
                }
            }
//            for (int i = 0; i < useable.size(); i++) {
//                if (!(t_account.contains(useable.get(i)))||!(t_password.contains(useable.get(i)))) {
//                    Toast.makeText(LoginActivity.this, "帳號密碼包含非法字元", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
            GetFromCloud gfc = new GetFromCloud(LoginActivity.this);
            path=SERVER_ADDRESS+"customer_login?value=login&account=" + t_account + "&password=" + t_password;
            String r=gfc.getText(path);
            if(r.equals("OK")){
                path=SERVER_ADDRESS+"customer_login?value=getname&account=" + t_account + "&password=" + t_password;
                String realname=gfc.getText(path);
                sp = getSharedPreferences("info",MODE_PRIVATE);
                spe = sp.edit();
                spe.putString("account",account.getText().toString());
                spe.putString("realname",realname);
                spe.commit();
                Log.i("acc",sp.getString("account",""));
                Log.i("acc",sp.getString("realname",""));
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            }else{
                Toast.makeText(this,"帳號或密碼錯誤，請檢查重試",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REGISTER_RETURN){
            if(resultCode==RESULT_OK){
                String t_account=data.getStringExtra("acc");
                String t_password=data.getStringExtra("pass");
                account.setText(t_account);
                password.setText(t_password);
            }
        }
    }
}

