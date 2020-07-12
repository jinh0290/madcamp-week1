package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;

public class Fragment2_gallery extends Fragment {
    public Fragment2_gallery(){}
    public Fragment2_gallery newInstance(){
        Fragment2_gallery fragment2 = new Fragment2_gallery();
        return fragment2;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment2, container, false);

        //현재 폴더 목록 보여주기
        //Intent intent = new Intent(getActivity(), FolderListActivity.class);
        //startActivity(intent);

        return view;
    }


    /*
    //버튼 클릭 시 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view){ //버튼 클릭 이벤트 처리
            switch (view.getId()){
                //"폴더 생성" 버튼 클릭 이벤트 처리
                case R.id.btn_createFolder:
                    setActivityChange();
                    break;
            }
        }
    };

    public void setActivityChange(){
        Button btn = (Button)getActivity().findViewById(R.id.btn_createFolder);
    }
    */
}