package com.example.inus.Activity.Setting;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.inus.databinding.ActivityAddToFriendsBinding;
import com.example.inus.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddToFriends extends BaseActivity {

    private ActivityAddToFriendsBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddToFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.leftIcon.setOnClickListener(view -> finish());
        binding.button3.setOnClickListener(v-> {
            try{
                DocumentReference doc = db.collection(Constants.KEY_COLLECTION_USERS).document(Constants.UID);
                doc.update("friends", FieldValue.arrayUnion(binding.inputfrineds.getText().toString()));
                Toast.makeText(getApplicationContext(), "已新增好友", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.d("demoErr" , e.getMessage());
            }
        });



    }
}