package com.example.inus.Activity.addEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.inus.R;
import com.example.inus.adapter.Timeadapter;
import com.example.inus.databinding.ActivityTimePickerBinding;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class _timePicker extends AppCompatActivity {

    private String datetime;
    private ActivityTimePickerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        List<String> items = new LinkedList<>();  // data source

        binding.button6.setOnClickListener(clickListener);
        binding.button7.setOnClickListener(clickListener);

        RecyclerView recyclerView = findViewById(R.id.recycleView_timepicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Timeadapter adapter = new Timeadapter(items);
        recyclerView.setAdapter(adapter);

        items.add("時段設定");  // first item
        binding.timepickerAddButton1.setOnClickListener(v ->{
            items.add("時段設定");
            adapter.notifyItemChanged(items.size()-1);
        });

        // next page
        binding.BtnAddTimepicker.setOnClickListener(view ->{
            Intent it = new Intent(this,_finishEvent.class );
            it.putExtras(getBundle(new Bundle()));
            startActivity(it);
        });

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
            int month = calendar.get(Calendar.MONTH) ;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    int cMonth = month+1; // month value is [0:11] st. ++
                    datetime = "" + year + "/" + cMonth  + "/" + day;
                    binding.button7.setText(datetime);
                }
            }, year, month, day).show();

        }
    };

    private Bundle getBundle(Bundle bundle){
        String title = getIntent().getStringExtra("title");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");

        bundle.putString("title",title);
        bundle.putString("location",location);
        bundle.putString("description",description);

        return bundle;
    }
}