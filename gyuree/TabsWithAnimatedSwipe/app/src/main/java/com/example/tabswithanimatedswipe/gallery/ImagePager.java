package com.example.tabswithanimatedswipe.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabswithanimatedswipe.R;

import java.util.ArrayList;


// 2번탭 갤러리에서 사진 하나를 클릭했을 때 호출하는 pager의 adapter
public class ImagePager extends RecyclerView.Adapter<ImageViewHolder> {
    private ArrayList<Integer> listData; // data 타입 바꿔야됨

    public ImagePager(ArrayList<Integer> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // 항목 하나하나의 layout 설정
        View view = LayoutInflater.from(context).inflate(R.layout.full_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 정해진 position에 대해서 어떤 데이터를 묶어줄지 정하기
        if(holder instanceof ImageViewHolder) {
            ImageViewHolder viewHolder = holder;
            viewHolder.onBind(listData.get(position)); // onBind()를 호출해서 묶어줌
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
