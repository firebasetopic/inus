package com.example.inus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.example.inus.Activity.Setting.setting;
import com.example.inus.Activity.addEvent._addEvent;
import com.example.inus.Activity.addEvent._group;
import com.example.inus.util.CalendarUtils;
import com.example.inus.R;
import com.example.inus.adapter.EventAdapter;
import com.example.inus.databinding.ActivityHomeScreenBinding;
import com.example.inus.model.Event;
import com.example.inus.util.Constants;
import com.example.inus.util.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;

public class Home_screen extends BaseActivity  {

    private ActivityHomeScreenBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;
    private boolean isOpen =false;  // for float btn
    Animation fabOpen , fabClose, rotateForward, rotateBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initView();  // DB from initList
        initList(CalendarUtils.selectDay);
        setListener();
        getToken();
    }

    private void init(){
        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        mAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());

        /* float button animation */
        binding.fabAddEvent.hide();
        binding.fabGroup.hide();
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
    }
    private void initView(){
        binding.calendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View view, ViewGroup viewGroup, CalendarBean calendarBean) {

                if(view == null){
                    view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_xiaomi,null);
                }

                TextView text = (TextView) view.findViewById(R.id.text);
                text.setText("" + calendarBean.day);

                if(calendarBean.mothFlag != 0){
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                // 農曆
//                TextView chinaText = (TextView) view.findViewById(R.id.chinaText);
//                chinaText.setText(calendarBean.chinaDay);
                return view;
            }
        });

        binding.calendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i, CalendarBean calendarBean) {
                CalendarUtils.selectDay = calendarBean.year + "/" + calendarBean.moth +"/"
                        + calendarBean.day;
                binding.title.setText(CalendarUtils.selectDay);
                initList(CalendarUtils.selectDay);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        binding.title.setText(data[0] + "/" +data[1] +"/" + data[2]);
    }
    /*calendar event */
    private void initList(String selectDay){
        db.collection("joinData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Event.eventList.clear();  // 重新讀取時不會有上次紀錄
                            for(QueryDocumentSnapshot doc: task.getResult()) {
                                Event event = new Event(doc.getString("title"), doc.getString("startTime"),
                                        doc.getString("endTime"), doc.getString("description"), doc.getString("location"));
                                Event.eventList.add(event);

                                ArrayList<Event> dailyEvents = Event.eventForDate(selectDay);
                                binding.list.setAdapter(new EventAdapter(getApplicationContext(),dailyEvents));
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage());
                    }
                });

    }

    private void setListener(){
        binding.fab.setOnClickListener(fabListener);
        binding.fabAddEvent.setOnClickListener(fabListener);
        binding.fabGroup.setOnClickListener(fabListener);
        binding.navigation.setSelectedItemId(R.id.home);//選到home按鈕改變顏色
        binding.navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:return true;

                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(),Shop_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),Cart_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.talk:
                        startActivity(new Intent(getApplicationContext(),Talk_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.none:
                        startActivity(new Intent(getApplicationContext(), Notification_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        binding.rightIcon.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), setting.class));
            overridePendingTransition(0,0);
        });

    }
    /*float button event */
    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*btn animation */
            if(isOpen){
                binding.fab.setAnimation(rotateForward);
                binding.fabAddEvent.setAnimation(fabClose);
                binding.fabGroup.setAnimation(fabClose);
                binding.fabAddEvent.hide();
                binding.fabGroup.hide();
                binding.fabAddEvent.setClickable(false);
                binding.fabGroup.setClickable(false);
                isOpen=false;
            }else{
                binding.fab.setAnimation(rotateBackward);
                binding.fabAddEvent.setAnimation(fabOpen);
                binding.fabGroup.setAnimation(fabOpen);
                binding.fabAddEvent.show();
                binding.fabGroup.show();
                binding.fabAddEvent.setClickable(true);
                binding.fabGroup.setClickable(true);
                isOpen=true;
            }

            if(view == binding.fabAddEvent)
                startActivity(new Intent(Home_screen.this, _addEvent.class));
            if(view == binding.fabGroup)
                startActivity(new Intent(Home_screen.this, _group.class));
        }
    };

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token);
//                .addOnSuccessListener(unused -> showToast("Token update successfully"))
//                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}