package com.example.inus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.inus.databinding.ActivityAddEventBinding;

public class _addEvent extends AppCompatActivity {

    private ActivityAddEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        binding.button2.setOnClickListener(view -> {
            startActivity(new Intent(this,Home_screen.class));
        });

    }
}