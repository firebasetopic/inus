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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class None_screen extends AppCompatActivity {
    private BottomNavigationView navigation;
    private noneAdapter noneAdapter;
    private Button group,groupbuy;
    private ImageView rigthicon;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int Tpye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_none_screen);
        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        navigation= findViewById(R.id.navigation);
        group = findViewById(R.id.group);
        groupbuy =findViewById(R.id.groupbuy);
        recyclerView = findViewById(R.id.recyclerView);
        rigthicon = findViewById(R.id.righticon);
        mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid();
        //預設按鈕顏色
        group.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
        groupbuy.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
        //裝資料庫內的知通
        ArrayList<String> glist = new ArrayList<>();
        db.collection("user/"+Uid+"/group")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                docobject n = doc.toObject(docobject.class);
                                glist.add(n.name);
                                noneAdapter = new noneAdapter(None_screen.this,glist,Tpye);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(None_screen.this));
                            recyclerView.setAdapter(noneAdapter);
                        }
                    }
                });
        rigthicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),setting.class));
                overridePendingTransition(0,0);
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
                groupbuy.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
                Tpye=0;
                ArrayList<String> glist = new ArrayList<>();
                db.collection("user/"+Uid+"/group")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        docobject n = doc.toObject(docobject.class);
                                        glist.add(n.name);
                                        noneAdapter = new noneAdapter(None_screen.this,glist,Tpye);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(None_screen.this));
                                    recyclerView.setAdapter(noneAdapter);
                                }
                            }
                        });
            }
        });
        groupbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupbuy.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
                group.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
                Tpye=1;
                ArrayList<String> glist = new ArrayList<>();
                db.collection("user/"+Uid+"/groupbuy")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        docobject n = doc.toObject(docobject.class);
                                        glist.add(n.name);
                                        noneAdapter = new noneAdapter(None_screen.this,glist,Tpye);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(None_screen.this));
                                    recyclerView.setAdapter(noneAdapter);
                                }
                            }
                        });
            }
        });
        navigation.setSelectedItemId(R.id.none);//選到none按鈕改變顏色
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
                        startActivity(new Intent(getApplicationContext(),Cart_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.talk:
                        return true;
                    case R.id.none:
                        return true;
                }
                return false;
            }
        });
//        Calendar c = Calendar.getInstance();
//        String time = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(c.getTime());
//        Log.d("Demo",time);
    }
}