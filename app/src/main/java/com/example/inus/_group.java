package com.example.inus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
            Bundle bundle = new Bundle();
            bundle.putString("title",binding.edtext1.getText().toString() );
            bundle.putString("location",binding.edtext2.getText().toString() );
            bundle.putString("description",binding.edtext3.getText().toString());

            if(isVaildInput()){
                Intent it = new Intent(this, _pickerfriends.class );
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

    private boolean isVaildInput() {
        if(binding.edtext1.getText().toString().isEmpty()){
            showToast("請輸入標題");
            return false;
        }else if(binding.edtext2.getText().toString().isEmpty()){
            showToast("請輸入地點");
            return false;
        }else { return true; }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}