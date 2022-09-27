package com.example.inus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private Button rhobby,userregister;
    private EditText rname,rphone,raccount,rpassword;
    private TextView ername,erphone,eraccount,erpassword,erhobby;
    private String[] hobby1 ={"美妝保養","教育學習","居家婦幼","醫療保健","視聽娛樂","流行服飾","旅遊休閒","3C娛樂","食品"};
    private boolean[] hobbyInChecked={false,false,false,false,false,false,false,false};
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>();
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dialog = new Dialog(this);

        rhobby = findViewById(R.id.rhobby);
        userregister =findViewById(R.id.userregister);
        rname = findViewById(R.id.rname);
        rphone = findViewById(R.id.rphone);
        raccount =findViewById(R.id.raccount);
        rpassword =findViewById(R.id.rpassword);
        eraccount = findViewById(R.id.erroraccount);
        ername = findViewById(R.id.errorname);
        erpassword = findViewById(R.id.errorpassword);
        erhobby = findViewById(R.id.errorhobby);
        erphone = findViewById(R.id.errorphone);

        getSupportActionBar().hide();// 隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        ImageView leftIcon = findViewById(R.id.lefticon);//自己做的導覽列
        mAuth = FirebaseAuth.getInstance();
        //上方返回鍵
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //選擇嗜好
        rhobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(register.this)
                        .setMultiChoiceItems(hobby1,hobbyInChecked, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                hobbyInChecked[which] = isChecked;
                            }
                        })
                        .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String myhobby ="";
                                for(int i=0; i<hobbyInChecked.length; i++)
                                {
                                    if(hobbyInChecked[i])
                                        myhobby += " " + hobby1[i];
                                }
                                rhobby.setText(myhobby);
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
        //註冊
        userregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = raccount.getText().toString();
                String password = rpassword.getText().toString();
                String name = rname.getText().toString();
                String phone= rphone.getText().toString();
                String hobby = rhobby.getText().toString();
                //判斷是否空值 全部否才會進行儲存
                if(TextUtils.isEmpty(account)==false) {
                    if (TextUtils.isEmpty(password) == false) {
                        if (TextUtils.isEmpty(name) == false) {
                            if (TextUtils.isEmpty(phone) == false){
                                if(TextUtils.isEmpty(hobby) ==false){
                                    mAuth.createUserWithEmailAndPassword(account, password)
                                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        if(mAuth.getCurrentUser()!=null){
                                                            String ac =mAuth.getCurrentUser().getUid();
                                                            user.put("account",account);
                                                            user.put("password",password);
                                                            user.put("name",name);
                                                            user.put("phone",phone);
                                                            user.put("hobby",hobby);
                                                            try{
                                                                db.collection("user")
                                                                        .document(ac)
                                                                        .set(user)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                opensuccessDialog();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                            }
                                                                        });
                                                            }catch (Exception e) {
                                                            }
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    switch (e.getMessage()) {
                                                        case "The email address is badly formatted.":
                                                            Toast.makeText(register.this, "Email格式錯誤", Toast.LENGTH_LONG).show();
                                                            break;
                                                        case "The given password is invalid. [ Password should be at least 6 characters ]":
                                                            Toast.makeText(register.this, "密碼長度過短", Toast.LENGTH_LONG).show();
                                                            break;
                                                        case "The email address is already in use by another account.":
                                                            Toast.makeText(register.this, "已有此用戶", Toast.LENGTH_LONG).show();
                                                            break;
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                }
                //判斷是否空值
                if(TextUtils.isEmpty(account)){
                    eraccount.setText("請輸入帳號");
                }else{
                    eraccount.setText("");
                }
                if(TextUtils.isEmpty(password)){
                    erpassword.setText("請輸入密碼");
                }else{
                    erpassword.setText("");
                }
                if(TextUtils.isEmpty(name)){
                    ername.setText("請輸入姓名");
                }else{
                    ername.setText("");
                }
                if(TextUtils.isEmpty(phone)){
                    erphone.setText("請輸入電話");
                }else{
                    erphone.setText("");
                }
                if(TextUtils.isEmpty(hobby)){
                    erhobby.setText("請選擇嗜好類別");
                }else{
                    erhobby.setText("");
                }
            }
        });
    }
    //註冊成功圖示
    private void opensuccessDialog(){
        dialog.setContentView(R.layout.success_register);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button get =dialog.findViewById(R.id.get);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }
}