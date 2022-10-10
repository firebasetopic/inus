package com.example.inus.Activity.addEvent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.inus.Activity.Setting.BaseActivity;
import com.example.inus.R;
import com.example.inus.adapter.Event.Timeadapter;
import com.example.inus.databinding.ActivityTimePickerBinding;
import com.example.inus.model.Event;
import com.example.inus.util.Constants;
import com.example.inus.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class _timePicker extends BaseActivity {

    private ActivityTimePickerBinding binding;
    private Date date, beforeDate;
    private Calendar calendar = Calendar.getInstance();
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Event> events = new ArrayList<>();
    public ArrayList<String> SchkEvent = new ArrayList<>();
    public ArrayList<String> EchkEvent = new ArrayList<>();
    public String[] FUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        setEventTime();  // 取出已選好友的事件
        setListeners();
        // adapter
        List<String> items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycleView_timepicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Timeadapter adapter = new Timeadapter(items);
        recyclerView.setAdapter(adapter);
        //set item
        items.add("時段設定");  // first item
        binding.timepickerAddButton1.setOnClickListener(v ->{
            items.add("時段設定");
            adapter.notifyItemChanged(items.size()-1);
        });

        binding.button.setOnClickListener(view -> finish());
    }

    private void setListeners(){
        binding.addTimepickerStart.setOnClickListener(v -> setTime(v));
        binding.addTimepickerEnd.setOnClickListener(v -> setTime(v));
        binding.BtnAddTimepicker.setOnClickListener(view -> {
            try {
                if (preferenceManager.getString(Constants.KEY_EVENT_START_TIME).isEmpty()) {
                    Toast.makeText(getApplicationContext(), "請選擇時間", Toast.LENGTH_SHORT).show();
                } else {
                    clacEventTime();
                    Intent it = new Intent(this, _finishEvent.class);
                    it.putStringArrayListExtra(Constants.KEY_COLLECTION_START_EVENT, SchkEvent);
                    it.putStringArrayListExtra(Constants.KEY_COLLECTION_END_EVENT, EchkEvent);
                    it.putExtra("FUID", FUID);
                    startActivity(it);
                }
            }catch (Exception e){}
                }
            );
    }

    private void setTime(View v){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//取得現在的日期年月日
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");

        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int imonth, int iday) {
                Calendar calendar = new GregorianCalendar(iyear,imonth,iday);
                date = calendar.getTime();

                if(v == binding.addTimepickerStart){
                    binding.addTimepickerStart.setText(SDF.format(date));
                    beforeDate = date;
                    preferenceManager.putString(Constants.KEY_EVENT_START_DAY,SDF.format(beforeDate));
                }else {
                    if(date.before(beforeDate) ){
                        showToast("結束時間不能早於開始時間");
                        binding.addTimepickerEnd.setText("");
                    }else {
                        binding.addTimepickerEnd.setText(SDF.format(date));
                        preferenceManager.putString(Constants.KEY_EVENT_END_DAY,SDF.format(date));
                    }
                }
            }
        },year, month, day ).show();
    };

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void setEventTime(){
        FUID = preferenceManager.getString(Constants.KEY_SELECTED_USERS).split(",");  // 切割出已選好友的ID
        for(int i = 0; i < FUID.length ; i++){
            FUID[i] = FUID[i].trim();
            FUID[i] = FUID[i].replace("[","");
            FUID[i] = FUID[i].replace("]","");
        } // 去除空白, "[", "]"
        for(int i =0; i < FUID.length ; i++) {   // 從已選的好友中
            try {
                db.collection(Constants.KEY_COLLECTION_USERS + "/" + FUID[i] + "/event")  // 取出他們的事件startTime 跟 endTime
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Event event = new Event(doc.getString(Constants.KEY_EVENT_TITLE), doc.getDate(Constants.KEY_EVENT_START_TIME),
                                            doc.getDate(Constants.KEY_EVENT_END_TIME));
                                    events.add(event);
                                }
                            }
                        });
            }catch (Exception e){ }
        } // 從好友名單中取出他們的事件，寫入Event
    }

    private void clacEventTime() {
// 設定本次 開始、結束時間
        String sd = preferenceManager.getString(Constants.KEY_EVENT_START_DAY);
        String ed = preferenceManager.getString(Constants.KEY_EVENT_END_DAY);  // 日期
        String st = preferenceManager.getString(Constants.KEY_EVENT_START_TIME);
        String et = preferenceManager.getString(Constants.KEY_EVENT_END_TIME);  // 時間點
        Calendar SCal = Calendar.getInstance();
        Calendar ECal = Calendar.getInstance();
        Date SDateTime = new Date();
        Date EDateTime = new Date();
        SimpleDateFormat SDFD = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat SDFDT = new SimpleDateFormat("yyyy/MM/dd HH:mm");  // init

        try{
            SCal.setTime(SDFD.parse(sd));
            ECal.setTime(SDFD.parse(ed));
        }catch (Exception e){ Log.d("error" ,e.getMessage()); }  // 設定開始日期跟結束日期 -> calendar

// 用loop，比對 eventStartCal 是否包含
        ECal.add(Calendar.DATE ,1); // 加回最後一天

        for(Date date = SCal.getTime(); SCal.before(ECal) ; SCal.add(Calendar.DATE, 1),  date = SCal.getTime()){
            try{
                SDateTime = SDFDT.parse(getDate(date) + " " +  st);
                EDateTime = SDFDT.parse(getDate(date) + " " + et);
                SchkEvent.add(""+SDFDT.format(SDateTime));  // 預設全部時間都可以
                EchkEvent.add(""+SDFDT.format(EDateTime));  // 預設全部時間都可以
            }catch (Exception e ){ Log.d("error" , e.getMessage()); }  // 取得詳細時間後，轉換型別成為Date

            for(Event e :events){ // all selected event
                if(getDate(date).equals(getDate(e.getStartTime()))){ // 如果比較事件是同一天
                    if(!SDateTime.after(e.getEndTime()) && !EDateTime.before(e.getStartTime())){ // 找出有衝突的日子
                        EchkEvent.remove(""+SDFDT.format(EDateTime));
                        SchkEvent.remove(""+SDFDT.format(SDateTime));  // 排除有衝突的日子
                    }
                }else{ continue;}
            }
        }//從開始日累加到結束日 -> date
    }

    private String getDate(Date date){
        SimpleDateFormat DAY = new SimpleDateFormat("yyyy/MM/dd");
        String s = DAY.format(date);
        return s;
    }
}