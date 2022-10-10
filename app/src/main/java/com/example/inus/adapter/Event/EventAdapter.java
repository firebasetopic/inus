package com.example.inus.adapter.Event;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inus.R;
import com.example.inus.model.Event;
import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0,events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Event event = getItem(position);
        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");

        Log.d("demoEve", "" + event);
        Log.d("demoP", "" + position);
        Log.d("demo", "" );

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_container_event,parent,false);
        }

        TextView eventTime = convertView.findViewById(R.id.eventCellTime);
        TextView eventTitle = convertView.findViewById(R.id.eventCellTitle);
        TextView eventInfo = convertView.findViewById(R.id.eventCellInfo);

        String Etime = SDF.format(event.getStartTime()) + " ~ " + SDF.format(event.getEndTime());
        String Etitle = event.getTitle();
        String EInfo =event.getLocation() +",  ps. " +  event.getDescription() ;

        eventTime.setText(Etime);
        eventTitle.setText(Etitle);
        eventInfo.setText(EInfo);

        return convertView;
    }


}
