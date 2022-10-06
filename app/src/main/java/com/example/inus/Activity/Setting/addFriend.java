package com.example.inus.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inus.databinding.ActivityAddEventBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class addFriend extends AppCompatActivity {

    private ActivityAddEventBinding binding;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}