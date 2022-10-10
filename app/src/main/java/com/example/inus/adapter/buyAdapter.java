package com.example.inus.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class buyAdapter  extends RecyclerView.Adapter<buyAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> namelist,numlist,priceslist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public buyAdapter(Context context,ArrayList<String>namelist,ArrayList<String>numlist,ArrayList<String>priceslist) {
        this.context = context;
        this.namelist=namelist;
        this.numlist=numlist;
        this.priceslist=priceslist;

    }
    @NonNull
    @Override
    public buyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.buy_item,parent,false);
        return new buyAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull buyAdapter.MyViewHolder holder, int position) {
        holder.name.setText(namelist.get(position));
        holder.num.setText(numlist.get(position));
        holder.price.setText(priceslist.get(position));
    }
    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
       TextView name,num,price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.buy_name);
            num = itemView.findViewById(R.id.buy_num);
            price =itemView.findViewById(R.id.buy_price);
        }
    }
}
