package com.example.helloworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReCyclerViewAdapter extends RecyclerView.Adapter<ReCyclerViewAdapter.MyViewHolder> {
    private ArrayList<Contacts> myDataList = null;
    ReCyclerViewAdapter(ArrayList<Contacts> dataList)
    {
        myDataList = dataList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name, phone_number;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.contact_card_view);
            name = itemView.findViewById(R.id.contact_name);
            phone_number = itemView.findViewById(R.id.contact_phone_number);

        }
    }

    @Override
    public ReCyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_contact_listitem_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position)
    {
        viewHolder.name.setText(myDataList.get(position).name);
        viewHolder.phone_number.setText(myDataList.get(position).phone_number);
    }

    @Override
    public int getItemCount()
    {
        return myDataList.size();
    }
}
