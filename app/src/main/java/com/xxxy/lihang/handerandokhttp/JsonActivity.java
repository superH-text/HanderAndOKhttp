package com.xxxy.lihang.handerandokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxxy.lihang.handerandokhttp.adapter.ShopAdapter;
import com.xxxy.lihang.handerandokhttp.model.JsonRootBean;
import com.xxxy.lihang.handerandokhttp.utils.Constant;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonActivity extends AppCompatActivity {
    ListView lv_shop;
    List<JsonRootBean> list;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    //拿到json数据
                    String json_shop= (String) msg.obj;
                    //把对象转换成List<JsonRootBean>
                    list = new Gson().fromJson(json_shop,new TypeToken<List<JsonRootBean>>(){}.getType());

                    //new出adapter
                    ShopAdapter shopAdapter =new ShopAdapter(getApplicationContext(),list);
                    lv_shop.setAdapter(shopAdapter);
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        lv_shop = findViewById(R.id.lv_shop);
        request_shop();
    }

    private void request_shop() {
        //请求地址
        String startURL = Constant.WEB_SITE+Constant.SHOP_URL;
        //模拟request请求
        Request request = new Request.Builder().url(startURL).build();
        //创建出OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //发送请求
        Call call = okHttpClient.newCall(request);
        //请求回调
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的时候的方法
                Log.d("bbbbbb","bug");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功的时候的方法
                Message message = new Message();
                message.what = 1;
                //拿到响应json数据
                String shop_json = response.body().string();
                message.obj  =shop_json;
                handler.sendMessage(message);
            }
        });
    }
}