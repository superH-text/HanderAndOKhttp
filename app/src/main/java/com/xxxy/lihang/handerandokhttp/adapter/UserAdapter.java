package com.xxxy.lihang.handerandokhttp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.xxxy.lihang.handerandokhttp.R;
import com.xxxy.lihang.handerandokhttp.holder.ShopHolder;
import com.xxxy.lihang.handerandokhttp.holder.UserHolder;
import com.xxxy.lihang.handerandokhttp.model.JsonRootBean;
import com.xxxy.lihang.handerandokhttp.model.User;

import java.util.List;


public class UserAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<User> list;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        UserHolder userHolder = new UserHolder();
        if (view == null){
            view = layoutInflater.inflate(R.layout.user_item,null);
            userHolder = new UserHolder();
            userHolder.id_tv=view.findViewById(R.id.tv_id);
            userHolder.username_tv=view.findViewById(R.id.tv_username);
            userHolder.password_tv=view.findViewById(R.id.tv_password);
            userHolder.phone_tv=view.findViewById(R.id.tv_phone);
            view.setTag(userHolder);
        }else {
            userHolder = (UserHolder) view.getTag();
        }
        //绑定数据
        userHolder.id_tv.setText(String.valueOf(list.get(position).getId()));
        userHolder.username_tv.setText(list.get(position).getUsername());
        userHolder.password_tv.setText(list.get(position).getPassword());
        userHolder.phone_tv.setText(list.get(position).getPhone());
        return view;
    }
}
