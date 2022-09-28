package com.example.inus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inus.adapter.shopAdapter;
import com.example.inus.adapter.shopcartAdapter;
import com.example.inus.model.addformat;
import com.example.inus.model.docobject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Shop_screen extends AppCompatActivity {
    private BottomNavigationView navigation;
    private Button shop_cart,shop_post,post_add;
    private ImageView rigthicon;
    private RecyclerView recyclerView;
    private com.example.inus.adapter.shopAdapter shopAdapter;
    private com.example.inus.adapter.shopcartAdapter shopcartAdapter;
    private Dialog adddialog,suredialog,dialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String[] hobby ={"美妝保養","教育學習","居家婦幼","醫療保健","視聽娛樂","流行服飾","旅遊休閒","3C娛樂","食品"};
    int no= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_screen);
        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        navigation= findViewById(R.id.navigation);
        rigthicon = findViewById(R.id.righticon);
        shop_cart = findViewById(R.id.shop_cart);
        shop_post = findViewById(R.id.shop_post);
        post_add =findViewById(R.id.post_add);
        recyclerView = findViewById(R.id.recyclerView);
        adddialog = new Dialog(this);
        suredialog = new Dialog(this);
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        shop_post.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
        shop_cart.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> articleList = new ArrayList<>();
        ArrayList<String> titlelist = new ArrayList<>();
        ArrayList<String> endtimelist= new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                id.add(doc.getId());
                                docobject b = doc.toObject(docobject.class);
                                nameList.add(b.name);
                                articleList.add(b.article);
                                titlelist.add(b.title);
                                endtimelist.add(b.endtime);
                                shopAdapter = new shopAdapter(Shop_screen.this, nameList,articleList,titlelist,endtimelist,id);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(Shop_screen.this));
                            recyclerView.setAdapter(shopAdapter);
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
        shop_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_post.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
                shop_cart.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
                ArrayList<String> nameList = new ArrayList<>();
                ArrayList<String> articleList = new ArrayList<>();
                ArrayList<String> titlelist = new ArrayList<>();
                ArrayList<String> endtimelist= new ArrayList<>();
                ArrayList<String> id = new ArrayList<>();
                post_add.setVisibility(View.VISIBLE);
                db.collection("post")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        id.add(doc.getId());
                                        docobject b = doc.toObject(docobject.class);
                                        nameList.add(b.name);
                                        articleList.add(b.article);
                                        titlelist.add(b.title);
                                        endtimelist.add(b.endtime);
                                        shopAdapter = new shopAdapter(Shop_screen.this, nameList,articleList,titlelist,endtimelist,id);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Shop_screen.this));
                                    recyclerView.setAdapter(shopAdapter);
                                }
                            }
                        });

            }
        });
        shop_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_cart.setBackground(getResources().getDrawable(R.drawable.theme2_fill__button_color));
                shop_post.setBackground(getResources().getDrawable(R.drawable.select_btn_color));
                post_add.setVisibility(View.GONE);
                ArrayList<String> cartbuy = new ArrayList<>();
                db.collection("user/"+mAuth.getUid()+"/cart")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        docobject b = doc.toObject(docobject.class);
                                        cartbuy.add(b.title);
                                        shopcartAdapter = new shopcartAdapter(Shop_screen.this, cartbuy);
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Shop_screen.this));
                                    recyclerView.setAdapter(shopcartAdapter);
                                }
                            }
                        });
            }
        });
        post_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openaddpostDialog();
            }
        });
        navigation.setSelectedItemId(R.id.shop);//選到shop按鈕改變顏色
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shop:
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),Cart_screen.class));
                        overridePendingTransition(0,0);
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
    private void openaddpostDialog(){
        adddialog.setContentView(R.layout.post_add);
        adddialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button add_hobby =adddialog.findViewById(R.id.add_hobby);
        Button add_endtime =adddialog.findViewById(R.id.add_endtime);
        Button next = adddialog.findViewById(R.id.next);
        ImageView exit= adddialog.findViewById(R.id.exit);
        EditText add_title =adddialog.findViewById(R.id.add_title);
        EditText add_art = adddialog.findViewById(R.id.add_art);
        Button add_format = adddialog.findViewById(R.id.add_format);
        ArrayList<String> storename= new ArrayList<>();
        ArrayList<String> storeprice= new ArrayList<>();
        HashMap<String,String>firebase_Store = new HashMap<>();
        HashMap<String,String>firebase_sell = new HashMap<>();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
            }
        });
        add_hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Shop_screen.this)
                        .setSingleChoiceItems(hobby, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                no = which;
                            }
                        })
                        .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add_hobby.setText(hobby[no]);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        add_format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.add_format);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ArrayList<addformat> addformatsArrayList = new ArrayList<>();
                LinearLayout linearLayout = dialog.findViewById(R.id.linear_list);
                Button add_list = dialog.findViewById(R.id.add_list);
                Button format_sure = dialog.findViewById(R.id.format_sure);
                ImageView  add_close = dialog.findViewById(R.id.add_close);
                storename.clear();
                storeprice.clear();
                add_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                format_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i= 0;i<linearLayout.getChildCount();i++){
                            View addf_View2= linearLayout.getChildAt(i);
                            EditText addf_name= (EditText) addf_View2.findViewById(R.id.addf_name);
                            EditText addf_price= (EditText) addf_View2.findViewById(R.id.addf_price);
                            addformat addformat = new addformat();
                            addformat.setName(addf_name.getText().toString());
                            addformat.setPrice(addf_price.getText().toString());
                            addformatsArrayList.add(addformat);
                        }
                       for (int i= 0;i<addformatsArrayList.size();i++){
                           addformat addformat= addformatsArrayList.get(i);
                           storename.add(addformat.getName());
                           storeprice.add(addformat.getPrice());
                       }
                       StringBuilder stringBuilder = new StringBuilder();
                       StringBuilder stringBuilder2 = new StringBuilder();
                        for (int i= 0;i<storename.size();i++){
                            stringBuilder.append(storename.get(i)+",");
                            stringBuilder2.append(storeprice.get(i)+",");
                        }
                        add_format.setText(stringBuilder.toString());
                        for (int i= 0;i<storename.size();i++){
                            firebase_Store.put("item_name"+i,storename.get(i));
                            firebase_Store.put("item_price"+i,storeprice.get(i));
                            firebase_sell.put("item_name"+i,storename.get(i));
                            firebase_sell.put("item_price"+i,storeprice.get(i));
                        }


                        dialog.cancel();
                    }
                });
                add_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View addf_View = getLayoutInflater().inflate(R.layout.add_format_click,null,false);
                        ImageView close=(ImageView) addf_View.findViewById(R.id.close);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linearLayout.removeView(addf_View);
                            }
                        });
                        linearLayout.addView(addf_View);
                    }
                });
                dialog.show();
            }
        });

        add_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String datetime = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day);
                        add_endtime.setText(datetime);   //取得選定的日期指定給日期編輯框
                    }
                }, year, month, day).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suredialog.setContentView(R.layout.post_add_sure);
                suredialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button sure = suredialog.findViewById(R.id.sure);
                Button nsure = suredialog.findViewById(R.id.not_sure);
                Button sure_hobby = suredialog.findViewById(R.id.sure_hobby);
                Button sure_endtime = suredialog.findViewById(R.id.sure_endtime);
                TextView sure_title = suredialog.findViewById(R.id.sure_title);
                TextView sure_art = suredialog.findViewById(R.id.sure_art);
                Button sure_format = suredialog.findViewById(R.id.sure_format);
                db.collection("user")
                        .document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot =task.getResult();
                                    String name = documentSnapshot.getString("name");
                                    String addtitle=add_title.getText().toString();
                                    String addhobby=add_hobby.getText().toString();
                                    String addformat=add_format.getText().toString();
                                    String addendtime = add_endtime.getText().toString();
                                    String addart= add_art.getText().toString();
                                    sure_title.setText(addtitle);
                                    sure_hobby.setText(addhobby);
                                    sure_format.setText(addformat);
                                    sure_endtime.setText(addendtime);
                                    sure_art.setText(addart);
                                    sure.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String size = String.valueOf(storename.size());
                                            firebase_Store.put("name",name);
                                            firebase_Store.put("title",addtitle);
                                            firebase_Store.put("endtime",addendtime);
                                            firebase_Store.put("hobby",addhobby);
                                            firebase_Store.put("article",addart);
                                            firebase_Store.put("size",size);
                                            try {
                                                db.collection("post")
                                                        .add(firebase_Store)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                String id = documentReference.getId();
                                                                firebase_sell.put("name",name);
                                                                firebase_sell.put("title",addtitle);
                                                                firebase_sell.put("endtime",addendtime);
                                                                firebase_sell.put("hobby",addhobby);
                                                                firebase_sell.put("article",addart);
                                                                firebase_sell.put("size",size);
                                                                db.collection("user/"+mAuth.getUid()+"/sell")
                                                                        .document(id)
                                                                        .set(firebase_sell)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Log.d("Demo","輸入成功");
                                                                            }
                                                                        });
                                                                startActivity(new Intent(getApplicationContext(),Shop_screen.class));
                                                                overridePendingTransition(0,0);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("Demo",e.getMessage());
                                                            }
                                                        });
                                            }catch (Exception e){}
                                        }
                                    });
                                    nsure.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            suredialog.cancel();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Demo", e.getMessage());
                            }
                        });
                suredialog.show();
            }
        });
        adddialog.show();
    }
}