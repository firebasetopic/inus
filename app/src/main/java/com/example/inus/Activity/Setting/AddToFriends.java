package com.example.inus.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inus.databinding.ActivityAddToFriendsBinding;

public class AddToFriends extends BaseActivity {

    private ActivityAddToFriendsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddToFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}