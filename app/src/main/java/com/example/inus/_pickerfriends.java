package com.example.inus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.inus.adapter.Demoadapter;
import com.example.inus.databinding.ActivityPickerfriendsBinding;

import java.util.LinkedList;
import java.util.List;

public class _pickerfriends extends AppCompatActivity {
    private ActivityPickerfriendsBinding binding;
    String[] name = {"A","B","C","D","E"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        binding = ActivityPickerfriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        List<String> items = new LinkedList<>();

        RecyclerView recyclerView = findViewById(R.id.recycleView_fpicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Demoadapter adapter = new Demoadapter(items);
        recyclerView.setAdapter(adapter);

        for(String i : name){
            items.add(i);
        }
        adapter.notifyDataSetChanged();

        binding.BtnAddfpicker.setOnClickListener(view -> startActivity(new Intent(this,_timePicker.class)));

    }
}