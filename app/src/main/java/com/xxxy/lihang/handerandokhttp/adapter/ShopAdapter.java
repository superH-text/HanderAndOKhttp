package com.xxxy.lihang.handerandokhttp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.bumptech.glide.Glide;
import com.xxxy.lihang.handerandokhttp.R;
import com.xxxy.lihang.handerandokhttp.holder.ShopHolder;
import com.xxxy.lihang.handerandokhttp.model.JsonRootBean;

import java.util.List;


public class ShopAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<JsonRootBean> list;
    public ShopAdapter(Context context, List<JsonRootBean> list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ShopHolder shopHolder;
        if (view == null){
            view = layoutInflater.inflate(R.layout.shop_item,null);
            shopHolder = new ShopHolder();
            shopHolder.tv_shopName=view.findViewById(R.id.tv_shopName);
            shopHolder.tv_saleNum=view.findViewById(R.id.tv_saleNum);
            shopHolder.tv_offerPrice=view.findViewById(R.id.tv_offerPrice);
            shopHolder.tv_distributionCost=view.findViewById(R.id.tv_distributionCost);
            shopHolder.tv_welfare=view.findViewById(R.id.tv_welfare);
            shopHolder.tv_time=view.findViewById(R.id.tv_time);
            shopHolder.iv_shopPic = view.findViewById(R.id.iv_shopPic);
            view.setTag(shopHolder);
        }else {
            shopHolder = (ShopHolder) view.getTag();
        }
        //绑定数据
        shopHolder.tv_shopName.setText(list.get(i).getShopName());
        shopHolder.tv_saleNum.setText(String.valueOf(list.get(i).getSaleNum()));
        shopHolder.tv_offerPrice.setText(String.valueOf(list.get(i).getOfferPrice()));
        shopHolder.tv_distributionCost.setText(String.valueOf(list.get(i).getDistributionCost()));
        shopHolder.tv_welfare.setText(list.get(i).getShopName());
        shopHolder.tv_time.setText(list.get(i).getShopName());

        Glide.with(context).load(list.get(i).getShopPic()).into(shopHolder.iv_shopPic);
        return view;
    }
}
