package com.example.inus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inus.R;

import java.util.ArrayList;

public class VpAdapter extends RecyclerView.Adapter<VpAdapter.ViewHolder> {

    ArrayList<Viewpageitem> viewpageitemArrayList;
    public VpAdapter(ArrayList<Viewpageitem> viewpageitemArrayList){
        this.viewpageitemArrayList = viewpageitemArrayList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpage,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Viewpageitem viewpageitem = viewpageitemArrayList.get(position);
        holder.imageView.setImageResource(viewpageitem.image);
    }

    @Override
    public int getItemCount() {
        return viewpageitemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageView);
        }
    }
}
