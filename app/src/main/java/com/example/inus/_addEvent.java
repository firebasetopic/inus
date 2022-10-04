package com.example.inus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.inus.databinding.ActivityAddEventBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class _addEvent extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityAddEventBinding binding;
    private boolean isSelect=false;
    String datetime ="";
    String datetimeBefore ="";
    private int Rmwhich = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色

        binding.switch1.setOnClickListener(view -> {
            if(!isSelect){
                binding.textViewEt.setVisibility(View.GONE);
                isSelect= true;
            }else {
                binding.textViewEt.setVisibility(View.VISIBLE);
                isSelect =false;
            }
        });
        binding.textViewSt.setOnClickListener(view ->timepicker(view));
        binding.textViewEt.setOnClickListener(view ->timepicker(view));
        binding.button.setOnClickListener(view -> startActivity(new Intent(this,Home_screen.class)));
        binding.button2.setOnClickListener(view -> {
            if(isValidInput()){
                addEvent();
                startActivity(new Intent(this,Home_screen.class));
            }
        });
        binding.textViewRm.setOnClickListener(v->{
            String[] strings={"一小時前","三小時前","一天前"};

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setSingleChoiceItems(strings, Rmwhich, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//結束對話框
                    Rmwhich = which;
                    binding.textViewRm.setText(strings[which]);
                }
            });
            builder.show();
        });
    }

    public void timepicker(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                int cMonth = month+1; // month value is [0:11] , so add 1
                datetime = "" + year  + "/" + cMonth  + "/" + day;

                new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        datetime += " " + hourOfDay + ":" + minute;

                        if(v == binding.textViewSt){
                            datetimeBefore = datetime;
                            binding.textViewSt.setText(datetimeBefore);
                        }else {
                            if(datetime.compareTo(datetimeBefore) <0 ){
                                showToast("結束時間不能早於開始時間");
                                binding.textViewEt.setText("");
                            }else{
                                binding.textViewEt.setText(datetime);
                            }
                        }
                    }
                }, hourOfDay, minute, true).show();
            }
        }, year, month, day).show();

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidInput() {
        if(binding.edTextTitle.getText().toString().isEmpty()){
            showToast("請輸入標題");
            return false;
        }else if(binding.textViewSt.getText().toString().isEmpty()){
            showToast("請輸入開始時間");
            return false;
        }else if(binding.textViewEt.getText().toString().isEmpty()){
            showToast("請輸入結束時間");
            return false;
        }else if(binding.editText.getText().toString().isEmpty()){
            binding.editText.setText("");
            return true;
        }else if(binding.editText2.getText().toString().isEmpty()){
            binding.editText2.setText("");
            return true;
        }else {
            return true;
        }
    }

    private void addEvent() {
        HashMap<Object,String> data = new HashMap<>();
        data.put("title",binding.edTextTitle.getText().toString());
        data.put("startTime",binding.textViewSt.getText().toString());
        data.put("endTime",binding.textViewEt.getText().toString());
        data.put("location",binding.editText.getText().toString());
        data.put("description",binding.editText2.getText().toString());

        db.collection("joinData").document()
                .set(data)
                .addOnSuccessListener(v -> {
                    Log.d("result","success");
                })
                .addOnFailureListener(v ->{
                    Log.d("result","fail");
                });
    }
}