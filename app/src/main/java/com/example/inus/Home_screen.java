package com.example.inus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.inus.agenda.DrawableCalendarEvent;
import com.example.inus.agenda.DrawableEventRenderer;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Home_screen extends AppCompatActivity implements CalendarPickerController {
    private BottomNavigationView navigation;
    private ImageView rigthicon;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static FloatingActionButton fab,fabAddEvent,fabGroup;
    private AgendaCalendarView magendaview;


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    boolean isOpen =false;  // for float btn
    Animation fabOpen , fabClose, rotateForward, rotateBackward;

    // for event
    ArrayList<CalendarEvent> eventList = new ArrayList<>();
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportActionBar().hide();//隱藏上方導覽列
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
        rigthicon = findViewById(R.id.righticon);
        navigation= findViewById(R.id.navigation);
        mAuth = FirebaseAuth.getInstance();
        navigation.setSelectedItemId(R.id.home);//選到home按鈕改變顏色
        magendaview = findViewById(R.id.magenda_calendar_view);
        fab = findViewById(R.id.fab);
        fabAddEvent =  findViewById(R.id.fabAddEvent);
        fabGroup = findViewById(R.id.fabGroup);
        /* float button hide , and showed when fab be click */
//        (findViewById(R.id.fab1))
        fabAddEvent.hide();
        fabGroup.hide();
        /*button animation */
        {
            fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
            fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
            rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
            rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        }

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mockList(eventList);

        magendaview.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        magendaview.addEventRenderer(new DrawableEventRenderer());

        /* fab btn */
        fab.setOnClickListener((view -> animanFab()));
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animanFab();
                startActivity(new Intent(Home_screen.this,_addEvent.class));
            }
        });

        fabGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animanFab();
                startActivity(new Intent(Home_screen.this,_group.class));
                magendaview.refreshDrawableState();
            }
        });

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        return true;
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(),Shop_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),Cart_screen.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.talk:
                        return true;
                    case R.id.none:
                        startActivity(new Intent(getApplicationContext(), Notification_screen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        rigthicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),setting.class));
                overridePendingTransition(0,0);
            }
        });


    }

    /*Generate*/
    public void onDaySelected(DayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
        Toast.makeText(Home_screen.this, "Selected event:" + event , Toast.LENGTH_SHORT).show();
    }

    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    private void mockList(ArrayList<CalendarEvent> eventList) {
        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 30);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("去朋友家", "賽班", "花蓮",
                ContextCompat.getColor(this, R.color.theme_primary), startTime3, endTime3, false, android.R.drawable.btn_star);
        eventList.add(event3);
    }

    private void animanFab(){

        if(isOpen){
            fab.setAnimation(rotateForward);
            fabAddEvent.setAnimation(fabClose);
            fabGroup.setAnimation(fabClose);
            fabAddEvent.hide();
            fabGroup.hide();
            fabAddEvent.setClickable(false);
            fabGroup.setClickable(false);
            isOpen=false;
        }else{
            fab.setAnimation(rotateBackward);
            fabAddEvent.setAnimation(fabOpen);
            fabGroup.setAnimation(fabOpen);
            fabAddEvent.show();
            fabGroup.show();
            fabAddEvent.setClickable(true);
            fabGroup.setClickable(true);
            isOpen=true;
        }
    }
}