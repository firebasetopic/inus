package com.example.inus.adapter.Event;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inus.databinding.ItemContainerResultBinding;
import com.example.inus.util.Constants;
import com.example.inus.util.PreferenceManager;

import java.util.Collections;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>{

    private PreferenceManager preferenceManager;
    private final List<String> result,result2;
    private int checkedPosition = 0; // -1 : no default selection ; 0 :1st item selected
    int selectPosition=0;

    public ResultAdapter( List<String> result, List<String> result2){
        this.result = result;
        this.result2 = result2;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerResultBinding itemContainerResultBinding =
                ItemContainerResultBinding.inflate(LayoutInflater.from(parent.getContext()
                ),parent,false);
        preferenceManager = new PreferenceManager(parent.getContext());

        return new ResultViewHolder(itemContainerResultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.setDate(Collections.singletonList(result.get(position)), Collections.singletonList(result2.get(position)));

        if(checkedPosition == position){
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f5dc"));
            holder.getAdapterPosition();
        }else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder{

        ItemContainerResultBinding binding;

        ResultViewHolder(ItemContainerResultBinding itemContainerResultBinding){
            super(itemContainerResultBinding.getRoot());
            binding = itemContainerResultBinding;
        }

        void setDate(List<String> result , List<String> result2){

            binding.textViewResultDay.setText("" + getDate(result) );
            binding.textViewResultTime.setText(getTime(result) + "~" + getTime(result2));
            binding.viewReResult.setOnClickListener(view -> {
                setSingleSelected(getAdapterPosition());
                preferenceManager.putString(Constants.KEY_COLLECTION_START_TINE, "" + result.get(selectPosition -1 ));
                preferenceManager.putString(Constants.KEY_COLLECTION_END_TIME, "" + result2.get(selectPosition - 1 ));
            });
        }

        private void setSingleSelected(int adapterPosition){
            if(adapterPosition == RecyclerView.NO_POSITION) return;

            notifyItemChanged(checkedPosition);
            checkedPosition = adapterPosition;
            notifyItemChanged(checkedPosition);
            selectPosition = checkedPosition;
        }
    }

    private String getDate(List<String> date){
        String d = "" + date;
        String[] d2 = d.split(" ") ;
        String d3 =  d2[0].substring(6);
        return d3;
    }

    private String getTime(List<String> date) {
        String t = "" + date;
        t.replace("]","");
        String[] t2 = t.split(" ");
        return t2[1].substring(0,5);
    }



}


