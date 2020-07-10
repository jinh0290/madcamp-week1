package com.example.week1_tab;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Phonebook> myDataList = null;

    RecyclerViewAdapter(ArrayList<Phonebook> dataList)
    {
        myDataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name, number;

        ViewHolder(View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            number = itemView.findViewById(R.id.grade);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        viewHolder.name.setText(myDataList.get(position).getName());
        viewHolder.number.setText(myDataList.get(position).getNumber());

    }

    @Override
    public int getItemCount()
    {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return myDataList.size();
    }


}
