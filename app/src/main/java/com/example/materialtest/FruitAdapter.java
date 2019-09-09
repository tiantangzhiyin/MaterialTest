package com.example.materialtest;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private Context mContext;
    private List<Fruit> mFruitList;

    public FruitAdapter(List<Fruit> fruitList){
        mFruitList=fruitList;
    }
    //ViewHolder作为子项缓存
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            fruitImage=(ImageView)view.findViewById(R.id.fruit_image);
            fruitName=(TextView)view.findViewById(R.id.fruit_name);
        }
    }
    //加载列表子项缓存布局
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.fruit_item,parent,false);
        return new ViewHolder(view);
    }
    //绑定子项控件的事件处理
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Fruit fruit=mFruitList.get(position);
        //使用Glide对象加载图片，with()方法传入一个Context、Activity或Fragment参数，
        // load()方法加载图片，传入路径；into()将图片设置到具体的ImageView中
        //使用Glide加载图片进行图片压缩等处理，不用担心内存溢出
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
        holder.fruitName.setText(fruit.getName());
    }
    //返回子项数量
    @Override
    public int getItemCount(){
        return mFruitList.size();
    }
}
