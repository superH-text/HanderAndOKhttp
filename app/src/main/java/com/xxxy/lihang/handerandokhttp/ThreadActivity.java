package com.xxxy.lihang.handerandokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ThreadActivity extends AppCompatActivity {
    private TextView mShowTv;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        mShowTv=findViewById(R.id.tv_show);
        mPb = findViewById(R.id.pb_progress);
        mShowTv.setText(""+1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<=100; i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mPb.setProgress(i);
                    mPb.setSecondaryProgress(i*2);
                    //建立消息
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = i;
                    mHandler.sendMessage(msg);
                    //mShowTv.setText(""+i);
                }
            }
        }).start();
    }
    //建立Handle语柄
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //2.建立处理机制
            switch (msg.what){
                case 0x123:
                    mShowTv.setText(msg.obj.toString());
                    break;
            }
        }
    };
}