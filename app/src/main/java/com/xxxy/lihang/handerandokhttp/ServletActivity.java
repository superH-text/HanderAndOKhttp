package com.xxxy.lihang.handerandokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxxy.lihang.handerandokhttp.adapter.UserAdapter;
import com.xxxy.lihang.handerandokhttp.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServletActivity extends AppCompatActivity {
    ListView userlist_lv;
    List<User> list = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //接收所有的User  数据 以List<User>的方式
            if(msg.what==1){
                String rep = (String) msg.obj;//获取到json串
                Gson gson = new Gson();
                list = gson .fromJson(
                        rep,new TypeToken<List<User>>(){}.getType()
                );
                //向ListView   提供数据，实现Adapter
                UserAdapter adapter = new UserAdapter(
                        ServletActivity.this,
                        list
                );
                userlist_lv.setAdapter(adapter);
            }
            //修改
            if(msg.what==3){
                String rep=(String) msg.obj;
                if(Integer.parseInt(rep)>0){
                    Toast.makeText(ServletActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    request_userlist();
                }else {
                    Toast.makeText(ServletActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
            //删除
            if(msg.what==5){
                String rep=(String) msg.obj;
                if(Integer.parseInt(rep)>0){
                    Toast.makeText(ServletActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    request_userlist();
                }else {
                    Toast.makeText(ServletActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servlet);
        userlist_lv = findViewById(R.id.userlist_lv);
        request_userlist();
        //对listview中的数据进行点击事件的操作
        userlist_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //执行修改操作
                //自定义一个alertdialog 自定义一个xml
                AlertDialog.Builder builder = new AlertDialog.Builder(ServletActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("用户修改过");
                //自定义一个满足需求的view  需要从xml中生成
                View inflate = LayoutInflater.from(ServletActivity.this).inflate(R.layout.show_user, null);
                //修改操作需要我们向alert中放入数据
                User user = list.get(i);
                EditText et_showid = inflate.findViewById(R.id.et_showid);
                EditText et_showusername = inflate.findViewById(R.id.et_showusername);
                EditText et_showpassword = inflate.findViewById(R.id.et_showpassword);
                EditText et_showphone = inflate.findViewById(R.id.et_showphone);

                et_showid.setText(String.valueOf(user.getId()));
                et_showusername.setText(user.getUsername());
                et_showpassword.setText(user.getPassword());
                et_showphone.setText(user.getPhone());
                //id不允许修改
                et_showid.setEnabled(false);//设置不可编辑
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //请求要写到相应的数据
                        User user = new User();
                        user.setId(Integer.parseInt(et_showid.getText().toString()));
                        user.setUsername(et_showusername.getText().toString());
                        user.setPassword(et_showpassword.getText().toString());
                        user.setPhone(et_showphone.getText().toString());
                        //当准备修改的数据，发送请求
                        request_updateUser(user);
                    }
                });
                builder.setNegativeButton("取消", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.setView(inflate);
                alertDialog.show();

            }
        });
        userlist_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //进行删除操作
                AlertDialog.Builder builder = new AlertDialog.Builder(ServletActivity.this);
                builder.setIcon(R.drawable.ic_launcher_background);
                builder.setTitle("删除");
                builder.setMessage("确定删除本人数据吗");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i1) {
                        User user = list.get(i);
                        request_deleteUserById(user.getId());

                    }
                });
                builder.setNegativeButton("取消",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;

            }
        });
    }
    private void request_deleteUserById(int id) {
        //通过okhttp访问servlet
        String strURL = "http://192.168.103.73:8086/infotest_war_exploded/delete";
        //携带数据的请求
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {//接收响应 子线程
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 6;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String rep = response.body().string();
                Message message = new Message();
                message.what = 5;
                message.obj = rep;
                handler.sendMessage(message);

            }
        });

    }


    private void request_updateUser(User user) {
        //通过okhttp访问servlet
        String strURL = "http://192.168.103.73:8086/infotest_war_exploded/update";
        //携带数据的请求
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(user.getId()))
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("phone", user.getPhone())
                .build();
        Request request = new Request.Builder()
                .url(strURL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {//接收响应 子线程
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 4;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String rep = response.body().string();
                Message message = new Message();
                message.what = 3;
                message.obj = rep;
                handler.sendMessage(message);

            }
        });

    }
    private void request_userlist() {
        //通过okHttp访问servlet
        String strURL = "http://192.168.103.73:8086/infotest_war_exploded/findAll";
        Request request = new Request.Builder().url(strURL).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        //接收相应  进入子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 2;
                message.obj = e.getMessage();
                //通过Handler向主线程发送json数据
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rep = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = rep;
                handler.sendMessage(message);
            }
        });
    }
}