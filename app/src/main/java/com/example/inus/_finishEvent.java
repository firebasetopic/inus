package com.example.inus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.inus.databinding.ActivityFinishEventBinding;

public class _finishEvent extends AppCompatActivity {

    private ActivityFinishEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinishEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        binding.button5.setOnClickListener(view -> {
            startActivity(new Intent(this,Home_screen.class));
        });

    }
}