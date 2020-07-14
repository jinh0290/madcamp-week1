package com.example.tabswithanimatedswipe.gallery;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tabswithanimatedswipe.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.full_image_view);
    }

    // Integer가 아니라 다른 걸로 바꿔야 된다.
    public void onBind(Integer data) {
        // imageView에 적절한 이미지를 세팅해주기
    }

}
