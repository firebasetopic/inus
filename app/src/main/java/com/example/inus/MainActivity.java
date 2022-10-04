package com.example.inus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView register, password_forgot, erroraccount, errorpassword;
    private EditText account, password;
    private Dialog dialog;
    private Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black)); // 狀態列顏色
        register = findViewById(R.id.register);
        password_forgot = findViewById(R.id.passwordforgot);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        erroraccount = findViewById(R.id.erroraccount);
        errorpassword = findViewById(R.id.errorpassword);
        login = findViewById(R.id.login);
        try {
            mAuth = FirebaseAuth.getInstance();
        }catch (Exception e)
        {
            Log.d("error", e.getMessage());
        }
        dialog = new Dialog(this);

        try {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String ac = account.getText().toString();//取得帳號
                    String pw = password.getText().toString();//取得密碼
                    if (TextUtils.isEmpty(ac) == false) {           //是否有輸入帳號
                        if (TextUtils.isEmpty(pw) == false) {       //是否有輸入密碼
                            mAuth.signInWithEmailAndPassword(ac, pw) //抓取資料庫帳密
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {  //核對帳密
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent it = new Intent();
                                                it.setClass(MainActivity.this, Home_screen.class);
                                                startActivity(it);
                                            }
                                        }
                                    })
                                    //錯誤代碼分別對應的意思
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            switch (e.getMessage()) {
                                                case "The email address is badly formatted.":
                                                    Toast.makeText(MainActivity.this, "Email格式錯誤", Toast.LENGTH_LONG).show();
                                                    break;
                                                case "The password is invalid or the user does not have a password.":
                                                    Toast.makeText(MainActivity.this, "密碼錯誤", Toast.LENGTH_LONG).show();
                                                    break;
                                                case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                                    Toast.makeText(MainActivity.this, "無此用戶", Toast.LENGTH_LONG).show();
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }
                    //判斷是否有輸入帳密
                    if (TextUtils.isEmpty(pw) && TextUtils.isEmpty(ac)) {
                        erroraccount.setText("輸入帳號");
                        errorpassword.setText("輸入密碼");
                        return;
                    }
                    if (TextUtils.isEmpty(pw)) {
                        erroraccount.setText(" ");
                        errorpassword.setText("輸入密碼");
                        return;
                    }
                    if (TextUtils.isEmpty(ac)) {
                        erroraccount.setText("輸入帳號");
                        errorpassword.setText(" ");
                        return;
                    }
                }
            });
        }catch (Exception e){
            Log.d("error" ,e.getMessage());
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(MainActivity.this,register.class);
                startActivity(it);
            }
        });
        password_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpwforgotDialog();
            }
        });
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Home_screen.class));
            overridePendingTransition(0,0);
        }
    }

    private void openpwforgotDialog(){
        dialog.setContentView(R.layout.forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imageView = dialog.findViewById(R.id.imageView);
        Button get =dialog.findViewById(R.id.get);
        EditText fpassword =dialog.findViewById(R.id.fpassword);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fpassword.getText().toString())!=true) {
                    String password = fpassword.getText().toString();
                    mAuth.sendPasswordResetEmail(password)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,"請至您的信箱修改密碼",Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Demo",e.getMessage());
                                    switch (e.getMessage()){
                                        case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                            Toast.makeText(MainActivity.this,"無此用戶",Toast.LENGTH_LONG).show();
                                            break;
                                        case "The email address is badly formatted.":
                                            Toast.makeText(MainActivity.this, "Email格式錯誤", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            });
                }else{
                    Toast.makeText(MainActivity.this,"請輸入帳號(信箱)",Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }
}