package com.example.inus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inus.adapter.cartAdapter;
import com.example.inus.model.docobject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Cart_screen extends AppCompatActivity {
    private BottomNavigationView navigation;
    private Button buyer,seller;
    private ImageView rigthicon;
    private RecyclerView recyclerView;
    private TextView name;
    private com.example.inus.adapter.cartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int Tpye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        rigthicon = findViewById(R.id.righticon);
        navigation= findViewById(R.id.navigation);
        name =findViewById(R.id.name);
        buyer = findViewById(R.id.buyer);
        seller =findViewById(R.id.seller);
        recyclerView =findViewById(R.id.recyclerView);

        mAuth = FirebaseAuth.getInstance();
        String Uid =mAuth.getCurrentUser().getUid();
        buyer.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
        seller.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
        db.collection("user")//抓取資料將名子存起來
                .document(Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String n = documentSnapshot.getString("name");
                            name.setText(n);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Demo",e.getMessage());
                    }
                });
        ArrayList<String> buy = new ArrayList<>();
        ArrayList<String>buyid = new ArrayList<>();
        Tpye=0;
        db.collection("user/"+Uid+"/buy")//抓取buy資料裝到buy、buyid陣列並傳到cartAdapter
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                docobject b = doc.toObject(docobject.class);
                                buy.add(b.title);
                                buyid.add(doc.getId());
                                cartAdapter= new cartAdapter(Cart_screen.this,buy,buyid,Tpye);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(Cart_screen.this));
                            recyclerView.setAdapter(cartAdapter);
                        }
                    }
                });
        rigthicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),setting.class));//點選右上角設定轉跳至設定畫面
                overridePendingTransition(0,0);
            }
        });
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyer.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));//當我按下上方按鈕後將顏色改變
                seller.setBackground(getResources().getDrawable(R.drawable.select_btn_color));//當我按下上方按鈕後將顏色改變
                Tpye=0;
                ArrayList<String> buy = new ArrayList<>();
                ArrayList<String>buyid = new ArrayList<>();
                db.collection("user/"+Uid+"/buy")//抓取buy資料裝到buy、buyid陣列並傳到cartAdapter
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        docobject b = doc.toObject(docobject.class);
                                        buyid.add(doc.getId());
                                        buy.add(b.title);
                                        cartAdapter= new cartAdapter(Cart_screen.this,buy,buyid,Tpye);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Cart_screen.this));
                                    recyclerView.setAdapter(cartAdapter);
                                }
                            }
                        });
            }
        });
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seller.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));//當我按下上方按鈕後將顏色改變
                buyer.setBackground(getResources().getDrawable(R.drawable.select_btn_color));//當我按下上方按鈕後將顏色改變
                Tpye=1;
                ArrayList<String> buy = new ArrayList<>();
                ArrayList<String>buyid = new ArrayList<>();
                db.collection("user/"+Uid+"/sell")//抓取sell資料裝到buy、buyid陣列並傳到cartAdapter
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        docobject b = doc.toObject(docobject.class);
                                        buy.add(b.title);
                                        buyid.add(doc.getId());
                                        cartAdapter= new cartAdapter(Cart_screen.this,buy,buyid,Tpye);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Cart_screen.this));
                                    recyclerView.setAdapter(cartAdapter);
                                }
                            }
                        });
            }
        });
        navigation.setSelectedItemId(R.id.cart);//選到cart按鈕改變顏色
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {//下方導覽列
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(),Shop_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        return true;
                    case R.id.talk:
                        return true;
                    case R.id.none:
                        startActivity(new Intent(getApplicationContext(), Notification_screen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }
}