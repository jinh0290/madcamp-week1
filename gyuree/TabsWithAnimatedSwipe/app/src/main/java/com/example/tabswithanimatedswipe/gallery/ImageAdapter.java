package com.example.tabswithanimatedswipe.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.tabswithanimatedswipe.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String imgData;
    private String geoData;
    //private ArrayList<STRING> thumbsDataList;
    //private ArrayList<STRING> thumbsIDList;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.pic1, R.drawable.pic2,
            R.drawable.pic3, R.drawable.pic4,
            R.drawable.pic5, R.drawable.pic6,
            R.drawable.pic7, R.drawable.pic8,
            R.drawable.pic9, R.drawable.pic10,
            R.drawable.pic11, R.drawable.pic12,
            R.drawable.pic13, R.drawable.pic14,
            R.drawable.pic15, R.drawable.pic16,
            R.drawable.pic17, R.drawable.pic18,
            R.drawable.pic19, R.drawable.pic20,
    };

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
        //thumbsDataList = new ArrayList<STRING>();
        //thumbsIDList = new ArrayList<STRING>();
        //getThumbInfo(thumbsIDList, thumbsDataList);
    }



    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        if(convertView != null){
            imageView.setImageResource(mThumbIds[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
        } else {
            imageView = (ImageView) convertView;
        }
/*
        // 이미지 크기를 새로 설정한다.
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;
        Bitmap bmp = BitmapFactory.decodeFile(thumbsDataList.get(position), bo);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 95, 95, true);
        imageView.setImageBitmap(resized);
*/
        return imageView;
    }

   // private void getThumbInfo(ArrayList<STRING> thumbsIDList, ArrayList<STRING> thumbsDataList) {
//    }

}
