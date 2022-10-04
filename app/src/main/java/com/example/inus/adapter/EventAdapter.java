package com.example.inus.adapter;

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

import org.w3c.dom.Text;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(@NonNull Context context, List<Event> event) {
        super(context, 0,event);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Event event = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell,parent,false);
        }
        TextView  eventCell = convertView.findViewById(R.id.eventCellTitle);

        String eventTitle = event.getTitle() + " " + event.getDate();
        eventCell.setText(eventTitle);

        return convertView;
    }
}
