package com.example.inus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> buy;
    private int Tpye;
    public cartAdapter(Context context,ArrayList<String>buy,int tpye) {
        this.context = context;
        this.buy=buy;
        this.Tpye = tpye;
    }
    @NonNull
    @Override
    public cartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.noneadapter_view,parent,false);
        return new cartAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull cartAdapter.MyViewHolder holder, int position) {
        if (Tpye ==0){
            holder.name.setText("已參加"+buy.get(position)+"團購活動");
        }else{
            holder.name.setText(buy.get(position)+"團購活動");
        }
//        db.collection("user/"+mAuth.getUid()+"/cart/wuDF6htYJO6MBfLhSVz0/prices")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot doc: task.getResult()){
//                                Log.d("Demo",doc.getId());
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Demo",e.getMessage());
//                    }
//                });
    }
    @Override
    public int getItemCount() {
        return buy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }
}
