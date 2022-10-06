package com.example.inus.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.inus.Activity.Setting.setting;
import com.example.inus.R;
import com.example.inus.databinding.ActivityTalkScreenBinding;
import com.example.inus.util.Constants;
import com.example.inus.util.PreferenceManager;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Talk_screen extends AppCompatActivity {

    private PreferenceManager preferenceManager;
//    private List<ChatMessage> conversations;
//    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore db;
    private ActivityTalkScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTalkScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();  // 載入用戶的 name 跟 頭像
        getToken();
        setListeners();
        listenConversation();
    }

    private void setListeners() {
        binding.rightIcon.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), setting.class));
            overridePendingTransition(0,0);
        });

    }

    private void listenConversation() {
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token);
//                .addOnSuccessListener(unused -> showToast("Token update successfully"))
//                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void  init(){
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
//        conversations = new ArrayList<>();
//        conversationsAdapter = new RecentConversationsAdapter(conversations,this);
//        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        db =FirebaseFirestore.getInstance();

        binding.navigation.setSelectedItemId(R.id.talk);//選到cart按鈕改變顏色
        binding.navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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

                    case R.id.talk:return true;

                    case R.id.none:
                        startActivity(new Intent(getApplicationContext(), Notification_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserDetails(){
            binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
            try {
                byte[] bytes = Base64.getDecoder().decode(preferenceManager.getString(Constants.KEY_IMAGE));
                if(bytes != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.imageProfile.setImageBitmap(bitmap);
                }else {
                    binding.imageProfile.setImageDrawable(getDrawable(R.drawable.person_white));
                }
            }catch (NullPointerException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}