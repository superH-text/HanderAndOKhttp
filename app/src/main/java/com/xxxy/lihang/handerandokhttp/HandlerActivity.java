package com.xxxy.lihang.handerandokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class HandlerActivity extends AppCompatActivity {
    TextView tv_new;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //接收message
            switch (msg.what){
                case 1:
                    String value = (String) msg.obj;
                    tv_new.setText(value);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        tv_new = findViewById(R.id.tv_new);
        //主线程更新自己的ui   随便更新

        //创建一个子线程出来
        new Thread(new Runnable() {
            @Override
            public void run() {

                //子线程处理方式  耗时的操作
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //创建出Message对象

                Message message = new Message();

                //给message对象设置what   名字

                message.what = 1;

                //给message对象赛数据

                message.obj = "你好!!!";

                //通过handler发送message

                handler.sendMessage(message);
            }
        }).start();
    }
}