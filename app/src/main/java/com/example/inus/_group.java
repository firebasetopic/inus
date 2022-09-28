package com.example.inus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.inus.databinding.ActivityGroupBinding;

public class _group extends AppCompatActivity {

    private ActivityGroupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        binding.button5.setOnClickListener(view -> {
            startActivity(new Intent(this,_pickerfriends.class));
        });

    }
}