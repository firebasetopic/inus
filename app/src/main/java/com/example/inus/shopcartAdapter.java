package com.example.inus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class shopcartAdapter extends RecyclerView.Adapter<shopcartAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> cartbuy;
    public shopcartAdapter(Context context,ArrayList<String>cartbuy) {
        this.context = context;
        this.cartbuy=cartbuy;

    }
    @NonNull
    @Override
    public shopcartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_post_cart,parent,false);
        return new shopcartAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull shopcartAdapter.MyViewHolder holder, int position) {
        holder.name.setText("已參加"+cartbuy.get(position)+"團購活動");
    }
    @Override
    public int getItemCount() { return cartbuy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }
}
