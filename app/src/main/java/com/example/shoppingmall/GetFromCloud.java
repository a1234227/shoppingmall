package com.example.shoppingmall;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class GetFromCloud {
    Activity activity;

    public GetFromCloud(Activity activity) {
        this.activity = activity;
    }

    public GetFromCloud() {

    }

    public String getText(String path) {
        final String[] json_send = new String[1];
        Thread t = new Thread() {
            @Override
            public void run() {
                ArrayList<Byte> tank = new ArrayList<>();
                InputStream is = null;
                super.run();
                try {
                    is = new URL(path).openStream();
                    byte[] raw_json = new byte[2048];
                    int size=is.read(raw_json);
                    while (size != -1) {
                        for (int i = 0; i < size; i++) {
                            tank.add(raw_json[i]);
                        }
                        size=is.read(raw_json);
                    }

                    byte[] json_arr = new byte[tank.size()];
                    for (int i = 0; i < tank.size(); i++) {
                        json_arr[i] = tank.get(i);
                    }
                    String json = new String(json_arr).trim();
                    json_send[0] = json;
                } catch (IOException e) {
                    e.printStackTrace();
//                } finally {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return json_send[0];
    }

    public Bitmap getImg(String path) {
        final Bitmap[] bitmap_send = {null};
        Thread t = new Thread() {

            @Override
            public void run() {
                InputStream is = null;
                super.run();


                {
                    try {
                        byte[] img_raw = new byte[1024];
                        is = new URL(path).openStream();
                        ArrayList<Byte> img_data = new ArrayList<>();
                        int size = is.read(img_raw);

                        while (size != -1) {
                            for (int i = 0; i < size; i++) {
                                img_data.add(img_raw[i]);
                            }
                            size = is.read(img_raw);
                        }
                        byte[] img_byte = new byte[img_data.size()];
                        for (int i = 0; i < img_data.size(); i++) {

                            img_byte[i] = img_data.get(i);
                        }
                        Bitmap bm = BitmapFactory.decodeByteArray(img_byte, 0, img_data.size());

                        bitmap_send[0] = bm;

                    } catch (MalformedURLException e) {
                        Log.e("ERROR", "請求的圖片路徑錯誤");
                        Toast.makeText(activity, "請求的圖片路徑錯誤\n請聯絡管理員", Toast.LENGTH_LONG);
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap_send[0];
    }

    final String[] r = new String[1];

    public String uploadText(String path, String remote_name, String remote_value) {
        Thread t = new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(path);
                    // URLConnection類別用來製造我們要的HttpURLConnection類別物件
                    URLConnection urlConnection = url.openConnection();  // URL是URLConnection的工廠
                    // HttpURLConnection類別可以修改發出的請求的header(變成POST),由URLConnection產生(其後代)
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;  //因為 String物件是寫 "http://...."
                    //////
                    //HttpURLConnection 修改發出的請求的 header

                    // 設置是否能從這次連線讀入資料(InputStream)，默認情況下是true;
                    httpURLConnection.setDoInput(true);
                    // 設置是否能從這次連線寫出(上傳)資料(OutputStream)，默認情況下是false;
                    httpURLConnection.setDoOutput(true);  //我後面會上傳圖片(甚麼都行)必須明確設為true(預設fasle)
                    // 請先知道甚麼是Caches,你自己決定要true或false
                    httpURLConnection.setUseCaches(false);

                    // 關鍵:修改成POST，默認是GET
                    httpURLConnection.setRequestMethod("POST");

                    ////////////
                    //以下的設定部分是http通訊協定必要的設定
                    //部分是模擬web form的行為

                    // 設置字符編碼連接參數 : http通訊協定必要的設定 (如果連線需要長時間完成)
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    // 設置字符編碼 : http通訊協定的設定(視需求)
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    // 設置請求內容類型 : 是模擬web form的行為,將來如果要上傳像圖片之類的資料,就一定要設定
                    // "Content-Type" 是指定 web form 中各個(multipart很多個的意思) <input type="....."> , form-data 就是模擬web form
                    // boundary 是指定各個 <input type="....."> 之間的界線
                    String boundary = "*****";   //這裡設為 ***** 你可以自己設定!但注意不要與form內容有重複的地方
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    //準備輸出串流往server輸出
                    OutputStream os = httpURLConnection.getOutputStream(); // 利用這個os往server出啦


                    // "Content-Type"設定好,就要開始模擬web form裡的各個 <input type=".....">
                    // http通訊協定要求,要先向server寫入一個 "--" 再寫入一個 上面的"boundary" 再寫入一個  "\r\n"

                    // 開始前的邊界
                    os.write("--".getBytes());
                    os.write(boundary.getBytes());
                    os.write("\r\n".getBytes());

                    // 開始輸出
                    // 以本題為例:我只有一個數字或文字參數要傳給server,那我就模擬
                    // <input type="text"  name="test"> 這個input元件
                    // 必須向server輸出 "Content-Disposition: form-data; name=\"test\""這樣的字串
                    os.write(("Content-Disposition: form-data;name=\"" + remote_name + "\"").getBytes());
                    // 每傳完一筆資料都要輸出兩組 "\r\n"
                    os.write("\r\n".getBytes());
                    os.write("\r\n".getBytes());

                    // 接著向server輸出這個<input type="text" name="test">的值
                    // 這個值來自畫面上的使用者對parameter_to_server的輸入
                    String data = remote_value;
                    os.write(data.getBytes()); // 送出server
                    // 再輸出一個 "\r\r"
                    os.write("\r\n".getBytes());
                    os.write("--".getBytes());
                    os.write(boundary.getBytes());
                    os.write("--".getBytes());
                    //本題就一個<input type="text"  name="test">,顧可以收尾了
                    //結束是 "--" + boundary + "--" + "\r\n"

                    os.flush();
                    os.close();
                    byte[] return_data = new byte[100];
                    //如果也要接收server回傳
                    InputStream is = httpURLConnection.getInputStream();
                    is.read(return_data);
                    String return_text = new String(return_data).trim();
                    r[0] = return_text;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }

            }

        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return r[0];
    }

    @Deprecated
    public String uploadByte(String path, String remote_name, byte[] remote_file) {
        final String[] result = {null};
        Thread t = new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    // String物件描述伺服器位置
                    //String path = "http://10.0.2.2/haha/test4.php";  //同一支server程式(這個php能反映post) php派
//                    String path = path;  // java派
                    // URL物件定位
                    URL url = new URL(path);
                    // URLConnection類別用來製造我們要的HttpURLConnection類別物件
                    URLConnection urlConnection = url.openConnection();  // URL是URLConnection的工廠
                    // HttpURLConnection類別可以修改發出的請求的header(變成POST),由URLConnection產生(其後代)
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;  //因為 String物件是寫 "http://...."
                    //////
                    //HttpURLConnection 修改發出的請求的 header

                    // 設置是否能從這次連線讀入資料(InputStream)，默認情況下是true;
                    httpURLConnection.setDoInput(true);
                    // 設置是否能從這次連線寫出(上傳)資料(OutputStream)，默認情況下是false;
                    httpURLConnection.setDoOutput(true);  //我後面會上傳圖片(甚麼都行)必須明確設為true(預設fasle)
                    // 請先知道甚麼是Caches,你自己決定要true或false
                    httpURLConnection.setUseCaches(false);

                    // 關鍵:修改成POST，默認是GET
                    httpURLConnection.setRequestMethod("POST");

                    ////////////
                    //以下的設定部分是http通訊協定必要的設定
                    //部分是模擬web form的行為

                    // 設置字符編碼連接參數 : http通訊協定必要的設定 (如果連線需要長時間完成)
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    // 設置字符編碼 : http通訊協定的設定(視需求)
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    // 設置請求內容類型 : 是模擬web form的行為,將來如果要上傳像圖片之類的資料,就一定要設定
                    // "Content-Type" 是指定 web form 中各個(multipart很多個的意思) <input type="....."> , form-data 就是模擬web form

                    // 指定"ENCTYPE"標頭值:"multipart/form-data" 這是告訴server送過來的byte是web form data
                    httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    // boundary 是指定各個 <input type="....."> 之間的界線
                    String boundary = "*****";   //這裡設為 ***** 你可以自己設定!但注意不要與form內容有重複的地方
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    //準備輸出串流往server輸出
                    OutputStream os = httpURLConnection.getOutputStream(); // 利用這個os往server出啦


                    // "Content-Type"設定好,就要開始模擬web form裡的各個 <input type=".....">
                    // http通訊協定要求,要先向server寫入一個 "--" 再寫入一個 上面的"boundary" 再寫入一個  "\r\n"

                    // 開始前的邊界
                    os.write("--".getBytes());
                    os.write(boundary.getBytes());
                    os.write("\r\n".getBytes());

                    //修改以配合能當指令呼叫
                    // 開始輸出
                    // 以本題為例:我只有一個數字或文字參數要傳給server,那我就模擬
                    // <input type="text"  name="test"> 這個input元件
                    // 必須向server輸出 "Content-Disposition: form-data; name=\"test\"" 這樣的字串
                    os.write(("Content-Disposition: form-data; name=\"" + remote_name + "\"; filename=\"file.jpg\"" + "\r\n").getBytes());
                    // 每傳完一筆資料都要輸出兩組 "\r\n"
//                    os.write("\r\n".getBytes());
                    os.write("\r\n".getBytes());

                    // 接著向server輸出這個<input type="file" name="test">的值
                    // 但是這個值來自使用者對parameter_to_server的輸入(已經是一個byte[]陣列了)

                    os.write(remote_file); // 送出server
                    // 再輸出一個 "\r\r"
                    os.write("\r\n".getBytes());
                    //本題就一個<input type="text"  name="test">,顧可以收尾了
                    //結束是 "--" + boundary + "--" + "\r\n"
                    os.write(("--" + boundary + "--" + "\r\n").getBytes());
                    os.flush();
                    //因為http是半雙工!!Input/Output必須一次只執行一種
                    //如果也要接收server回傳
                    InputStream is = httpURLConnection.getInputStream();

                    byte[] return_data = new byte[100];
                    is.read(return_data);
                    String return_text = new String(return_data).trim();
                    result[0] = return_text;
                    Log.i("XXXX", return_text);
                    /*
                    //這段是你上傳完成後你這邊的Activity要另外做甚麼畫面
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              //加在此
                        }
                    });
                    */
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    public String uploadByte2(String actionURL, String remote_name, byte[] data) {
        //把一些模擬HTTP通訊協定要用的符號用字串表示
        final String[] result = {null};
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();


                String towHyphens = "--";
                String boundary = "******";
                String end = "\r\n";
                try {
                    // URL定位server在哪裡(本函數只用URLConnect就做完了)
                    URL url = new URL(actionURL);
                    // URLConnection物件製造
                    URLConnection urlConnection = url.openConnection();
                    // 因為URLConnection就可以設定header了!就不往HttpURLConnection去了

                    //指明可以向server輸出
                    urlConnection.setDoOutput(true);
                    //指明可以從server輸入
                    urlConnection.setDoInput(true);
                    //關閉快取
                    urlConnection.setUseCaches(false);
                    //設定HTTP通訊協定的資料分隔符號為 ******
                    urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    //開始向server輸出
                    //取得輸出串流
                    OutputStream outputStream = urlConnection.getOutputStream();
                    //因為想直接對server輸出字串,所以把低階的OutputStream套接一個高階的DataOutputStream
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                    //我想改成只上傳一個檔案的版本(給android用)


                    // 向server輸出邊界表示要開始
                    dataOutputStream.writeBytes(towHyphens + boundary + end);
                    // 輸出模擬web form的訊息 ====>注意!!我的servlet那頭式的變數是"test"
                    dataOutputStream.writeBytes("Content-Disposition:form-data;name=\"" + remote_name + "\";filename=" + "OKLA.jpg" + end);
                    // 輸出模擬web form的訊息
                    dataOutputStream.writeBytes(end);

                    // 本題是ImageView裡的圖片
                    // 故做法是:使用者用參數傳給這個函數來上傳

                    dataOutputStream.write(data);

                    // 輸出模擬web form的訊息
                    dataOutputStream.writeBytes(end);

                    // 輸出模擬web form的訊息
                    dataOutputStream.writeBytes(towHyphens + boundary + towHyphens + end);
                    // 強迫所有資料離開 --- flush 沖水!!!
                    dataOutputStream.flush();

                    // ========以上輸出完畢=======
                    // 以下接收server回應的訊息
                    // 取得(低階)輸入流
                    InputStream inputStream = urlConnection.getInputStream();
                    // 套接一個中階輸入流
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // 再套接一個高階輸入流
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // server回應的是"文字",以下對這些文字好好地處理
                    // 單行變數備用
                    String line;
                    // StringBuilder是一個"文字建造工具"有一些處理文字的功能
                    StringBuilder stringBuilder = new StringBuilder();
                    // 使用高階輸入串流從server一行一行讀取文字
                    // 因為不知道server會送來幾行,用while(讀到非null就處理)
                    while ((line = bufferedReader.readLine()) != null) {
                        // 以StringBuilder簡單處理
                        stringBuilder.append(line); //一行一行裝到StringBuilder中(StringBuilder可以看成String的List)
                    }

                    // 好習慣!!主動關閉不用的物件!!!
                    // 注意關閉的次序!!
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();

                    // 好習慣!!主動關閉不用的物件!!!
                    // 注意關閉的次序!!
                    dataOutputStream.close();
                    outputStream.close();

                    // StringBuilder的toString函數可以整合所有內容
                    // 回傳出去
                    result[0] = stringBuilder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 失敗回傳null
        return result[0];


    }
}




