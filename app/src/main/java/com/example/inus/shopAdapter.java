package com.example.inus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.MyViewHolder>{
    Context context;
    private Dialog joindialog ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private postjoinAdapter postjoinAdapter;
    ArrayList<String> nameLiset,articleList,titlelist,endtimelist,id;
    ArrayList<Viewpageitem> viewpageitemArrayList;
    public shopAdapter(Context context,ArrayList<String>nameLiset,ArrayList<String>articleList,ArrayList<String>titlelist,ArrayList<String>endtimelist,ArrayList<String>id) {
        this.context = context;
        this.nameLiset=nameLiset;
        this.articleList=articleList;
        this.titlelist=titlelist;
        this.endtimelist=endtimelist;
        this.id=id;

    }
    @NonNull
    @Override
    public shopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_post,parent,false);
        return new shopAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull shopAdapter.MyViewHolder holder, int position) {
        String posid = id.get(position);
        holder.name.setText(nameLiset.get(position));
        holder.article.setText(articleList.get(position));
        holder.title.setText(titlelist.get(position));
        holder.time.setText(endtimelist.get(position));
        int[] images = {R.drawable.none1,R.drawable.none};
        viewpageitemArrayList = new ArrayList<>();
        for(int i = 0;i<images.length;i++){
            Viewpageitem viewpageitem = new Viewpageitem(images[i]);
            viewpageitemArrayList.add(viewpageitem);
        }
        VpAdapter vpAdapter = new VpAdapter(viewpageitemArrayList);
        holder.viewPager2.setAdapter(vpAdapter);
        holder.viewPager2.setClipToPadding(false);
        holder.viewPager2.setClipChildren(false);
        holder.viewPager2.setOffscreenPageLimit(1);
        holder.viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

//        db.collection("user/"+mAuth.getUid()+"/cart")
//                .document(posid)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot  = task.getResult();
//                            String sure = documentSnapshot.getString("sure");
//                            String sure1= new String(sure);
//                            String num =new String("1");
//                                if(sure1.contentEquals(num)==true){
//                                    holder.button.setBackgroundResource(R.drawable.theme2_fill__button_color);
//                                    holder.button.setText("已參加");
//                                    holder.button.setTextColor(Color.RED);
//                                }else {
//                                    Log.d("Demo","1");
//                                }
//                        }
//                    }
//                });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HashMap<String,String> sure = new HashMap<>();
//                sure.put("sure","0");
//                db.collection("user/"+mAuth.getUid()+"/cart")
//                        .document(posid)
//                        .set(sure)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                            }
//                        });
                ArrayList<String> itemname = new ArrayList<>();
                ArrayList<String> itemprice = new ArrayList<>();
                joindialog = new Dialog(context);
                joindialog.setContentView(R.layout.post_join);
                joindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RecyclerView recyclerView = joindialog.findViewById(R.id.recyclerView);
                TextView join_title = joindialog.findViewById(R.id.join_title);
                ImageView join_exit = joindialog.findViewById(R.id.join_exit);
                Button join_sumbit = joindialog.findViewById(R.id.join_sumbit);
                db.collection("post")
                        .document(posid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String size = documentSnapshot.getString("size");
                                    join_title.setText(documentSnapshot.getString("title"));
                                    int sizeint = Integer.parseInt(size);
                                    for (int i= 0;i<sizeint;i++){
                                        String name = documentSnapshot.getString("item_name"+i);
                                        String price = documentSnapshot.getString("item_price"+i);
                                        itemprice.add(price);
                                        itemname.add(name);
                                    }
                                    postjoinAdapter = new postjoinAdapter(context,itemname,itemprice,posid);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(postjoinAdapter);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Demo",e.getMessage());
                            }
                        });
                join_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joindialog.cancel();
                        for(int i=0 ; i< itemname.size();i++){
                            db.collection("user/"+mAuth.getUid()+"/cart/"+posid+"/"+itemname.get(i))
                                    .document("prices")
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                            db.collection("user/"+mAuth.getUid()+"/cart")
                                    .document(posid)
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                        }
                    }
                });
                join_sumbit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        joindialog.cancel();
                        HashMap<String,String> sure = new HashMap<>();
                        sure.put("title",titlelist.get(position));
                        db.collection("user/"+mAuth.getUid()+"/cart")
                                .document(posid)
                                .set(sure)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });

                    }
                });
                joindialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,title,article,time;
        Button button;
        ViewPager2 viewPager2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            article=itemView.findViewById(R.id.article);
            button= itemView.findViewById(R.id.button);
            viewPager2 = itemView.findViewById(R.id.viewpager);
            time = itemView.findViewById(R.id.time);
        }
    }
}
