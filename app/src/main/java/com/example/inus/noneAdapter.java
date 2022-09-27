package com.example.inus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class noneAdapter extends RecyclerView.Adapter<noneAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> glist;
    private int Tpye;
    public noneAdapter(Context context,ArrayList<String>glist,int tpye) {
        this.context = context;
        this.glist=glist;
        this.Tpye = tpye;
    }
    @NonNull
    @Override
    public noneAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.noneadapter_view,parent,false);
        return new noneAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull noneAdapter.MyViewHolder holder, int position) {
        if (Tpye ==0){
            holder.name.setText("您已被邀請參加"+glist.get(position)+"，請確認是否參加??");
        }else{
            holder.name.setText("已結束"+glist.get(position)+"團購活動");

        }
    }
    @Override
    public int getItemCount() {
        return glist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }
}
