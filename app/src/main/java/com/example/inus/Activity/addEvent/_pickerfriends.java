package com.example.inus.Activity.addEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.inus.R;
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
        binding = ActivityPickerfriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        List<String> items = new LinkedList<>();  // data source

        RecyclerView recyclerView = findViewById(R.id.recycleView_fpicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Demoadapter adapter = new Demoadapter(items);
        recyclerView.setAdapter(adapter);

        for(String i : name){
            items.add(i);
        }
        adapter.notifyDataSetChanged();

        binding.BtnAddfpicker.setOnClickListener(view -> {
            Intent it = new Intent(this,_timePicker.class );
            it.putExtras(getBundle(new Bundle()));
            startActivity(it);
        });
    }

    private Bundle getBundle(Bundle bundle){
        String title = getIntent().getStringExtra("title");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");

        bundle.putString("title",title);
        bundle.putString("location",location);
        bundle.putString("description",description);

        return bundle;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}