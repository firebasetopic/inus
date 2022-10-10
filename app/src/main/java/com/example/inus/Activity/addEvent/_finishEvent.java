package com.example.inus.Activity.addEvent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inus.Activity.Home_screen;
import com.example.inus.Activity.Setting.BaseActivity;
import com.example.inus.R;
import com.example.inus.adapter.Event.ResultAdapter;
import com.example.inus.databinding.ActivityFinishEventBinding;
import com.example.inus.util.Constants;
import com.example.inus.util.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class _finishEvent extends BaseActivity {

    private ActivityFinishEventBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth =FirebaseAuth.getInstance();
    private String UID = auth.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinishEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        final Bundle stringArraylist = getIntent().getExtras();
        ArrayList<String> SResults = stringArraylist.getStringArrayList(Constants.KEY_COLLECTION_START_EVENT);
        ArrayList<String> EResults = stringArraylist.getStringArrayList(Constants.KEY_COLLECTION_END_EVENT);
        String[] FUID = stringArraylist.getStringArray("FUID");

        RecyclerView recyclerView = findViewById(R.id.recyclerView_Result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ResultAdapter resultAdapter = new ResultAdapter(SResults,EResults);
        recyclerView.setAdapter(resultAdapter);
        resultAdapter.notifyDataSetChanged();

        String title = preferenceManager.getString(Constants.KEY_EVENT_TITLE);
        String location = preferenceManager.getString(Constants.KEY_EVENT_LOCATION);
        String description = preferenceManager.getString(Constants.KEY_EVENT_DESCRIPTION);

        binding.textViewTitle.setText(title);
        binding.textViewLocation.setText(location);

        binding.button.setOnClickListener(view ->  finish() );
        binding.btnEventSubmit.setOnClickListener(view ->  {
            HashMap<String,Object> data = new HashMap<>();
            data.put(Constants.KEY_EVENT_TITLE,title);
            data.put(Constants.KEY_EVENT_START_TIME,getStringToDate(preferenceManager.getString(Constants.KEY_COLLECTION_START_TINE)));
            data.put(Constants.KEY_EVENT_END_TIME,getStringToDate(preferenceManager.getString(Constants.KEY_COLLECTION_END_TIME)));
            data.put(Constants.KEY_EVENT_LOCATION,location);
            data.put(Constants.KEY_EVENT_DESCRIPTION, description);

            // 寫入自己的DB
            db.collection(Constants.KEY_COLLECTION_USERS + "/" + UID + "/event" )
                    .document().set(data);
            //寫入別人的DB
            for(String id : FUID){
                db.collection(Constants.KEY_COLLECTION_USERS + "/" + id + "/event")
                        .document().set(data);
            }

            startActivity(new Intent(this, Home_screen.class) );
            });
    }

    private Date getStringToDate(String s)  {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        try {
            date = SDF.parse(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    };

}
