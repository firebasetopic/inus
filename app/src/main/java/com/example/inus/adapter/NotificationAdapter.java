package com.example.inus.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inus.R;
import com.example.inus.listeners.Callback;
import com.example.inus.util.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> implements Callback {
    private Context context;
    private ArrayList<String> events;
    private boolean isGroupBuy;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String collectionPath;

    public NotificationAdapter(Context context, ArrayList<String> events, boolean isGroupBuy) {
        this.context = context;
        this.events=events;
        this.isGroupBuy = isGroupBuy;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.noneadapter_view,parent,false);
        return new NotificationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        collectionPath ="group";
        if (isGroupBuy == true)  collectionPath = "groupBuy";

        //
        DocumentReference doc = db.collection(Constants.KEY_COLLECTION_USERS).document(Constants.UID)
                .collection(collectionPath).document(events.get(position));
        // setTitle
        doc.get().addOnSuccessListener(documentSnapshot ->
                holder.name.setText(setTitleText(""+ documentSnapshot.get(Constants.KEY_EVENT_TITLE),
                        "" + documentSnapshot.get(Constants.KEY_EVENT_STATE))));

        holder.itemView.setOnClickListener(v -> {
            doc.get().addOnSuccessListener(documentSnapshot -> {
                Toast.makeText(v.getContext(), "" + documentSnapshot.get(Constants.KEY_EVENT_STATE), Toast.LENGTH_SHORT).show();
            });
        });

        // 長按刪除
        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle("刪除").setMessage("確定要刪除嗎")
                    .setPositiveButton("OK",(dialogInterface, i) -> {
                        db.collection(Constants.KEY_COLLECTION_USERS).document(Constants.UID).collection(collectionPath)
                                .document(events.get(position)).delete();
                    }).setNegativeButton("取消",null).show();
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void responseCallback(String data) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    private String setTitleText(String title, String state){
        String message ="test" + title + state;

        if(!isGroupBuy){
            if(state.equals("main")){
                message = title;
            }else if(state.equals("0")){
                message = "您已被邀請參加 " + "" + title + " 活動，請確認是否參加??";
            }else if(state.equals("1")){
                message = "距離開始時間還有";
            }
        }else {
            if(state.equals("main")){
                message = title;
            }else if(state.equals("0")){
                message = "已結束"+ title +"團購活動";
            }else if(state.equals("1")){
                message = "距離結束時間還有";
            }
        }
        return message;
    }

    private String getCountDay() {


        return new String();
    }
}
