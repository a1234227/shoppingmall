package com.example.shoppingmall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    EditText account, password, realname, address;
    String path;
    boolean visble = false;
    String[] useable_arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
            , "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    };
    String useable_String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    String useable_String2 = "1234567890";

    Character[] useable_char_arr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
            , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    byte[] arr = useable_String.getBytes();

    ArrayList<Byte> useable = new ArrayList<>();
    ArrayList<String> useable2;
    ArrayList<Character> useable3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = this.findViewById(R.id.input_register_account);
        password = this.findViewById(R.id.input_login_password);
        realname = this.findViewById(R.id.input_register_realname);
        address = this.findViewById(R.id.input_register_address);
    }

    public void action_register(View v) {
        if (v.getId() == R.id.button_register_register) {
//            startActivity(new Intent(this,LoginActivity.class));
//            for(int i=0;i<useable_char_arr.length;i++) {
//                useable3.add(useable_char_arr[i]);
//            }
            for (int i = 0; i < arr.length; i++) {
                useable.add(arr[i]);
            }

//            useable_arr
//            for(int i=0;i<useable_arr.length;i++) {
//                arr[i]=Byte.parseByte(useable_arr[i]);
//                useable.add(arr[i]);
//            }

//            Log.i("msg", useable.toString());
            String t_account, t_password, t_realname, t_address;
            t_account = account.getText().toString();
            t_password = password.getText().toString();
            t_realname = realname.getText().toString();
            t_address = address.getText().toString();
            try {
                if ((t_account.equals("") || t_account.equals(null)) || (t_password.equals("") || t_password.equals(null)) || (t_realname.equals("") || t_realname.equals(null)) || (t_address.equals("") || t_address.equals(null))) {
                    Toast.makeText(RegisterActivity.this, "你有東西沒填喔", Toast.LENGTH_LONG).show();
                    return;
                }

                for (int i = 0; i < t_account.length(); i++) {

                    if (!(useable.contains(t_account.getBytes()[i]))) {
                        Toast.makeText(RegisterActivity.this, "帳號包含非法字元", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                for (int i = 0; i < t_password.length(); i++) {
                    if (!(useable.contains(t_password.getBytes()[i]))) {
                        Toast.makeText(RegisterActivity.this, "密碼包含非法字元", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(t_account.length()<4){
                    Toast.makeText(RegisterActivity.this, "帳號過短，請增加至4字以上", Toast.LENGTH_LONG).show();
                    return;
                }

                if(t_password.length()<4){
                    Toast.makeText(RegisterActivity.this, "密碼過短，請增加至4字以上", Toast.LENGTH_LONG).show();
                    return;
                }

                if (t_address.length()>45||t_realname.length()>45||t_account.length()>45||t_password.length()>45) {
                    Toast.makeText(RegisterActivity.this, "資訊過長，每一欄位不得超過45字", Toast.LENGTH_LONG).show();
                    return;
                }


                path = LoginActivity.SERVER_ADDRESS + "customer_login?value=register&account=" + t_account + "&password=" + t_password + "&realname=" + URLEncoder.encode(t_realname, "UTF-8") + "&address=" + URLEncoder.encode(t_address, "UTF-8");
                Log.i("msg", path);
                GetFromCloud gfc = new GetFromCloud(RegisterActivity.this);
                String result = gfc.getText(path);
                closeSoftInput(RegisterActivity.this);
                if (result.equals("OK")) {
                    Toast.makeText(RegisterActivity.this, "OK", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent back = new Intent(RegisterActivity.this, LoginActivity.class);
                            back.putExtra("acc", t_account);
                            back.putExtra("pass", t_password);
                            RegisterActivity.this.setResult(RESULT_OK, back);
                            RegisterActivity.this.finish();
                        }
                    }, 1500);
                } else if (result.equals("Duplicate Account")) {
                    Toast.makeText(RegisterActivity.this, "帳號重複", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Unknown Error", Toast.LENGTH_LONG).show();
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (v.getId() == R.id.button_register_reset) {
            account.setText("");
            password.setText("");
            realname.setText("");
            address.setText("");
        } else if (v.getId() == R.id.button_register_passwordVisible) {
            if (visble == false) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                visble = true;
            } else {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                visble = false;
            }
        }
    }


    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}