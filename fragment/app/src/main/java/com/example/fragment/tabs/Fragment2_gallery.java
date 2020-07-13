package com.example.fragment.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.fragment.R;

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

        //파일탐색기
//        ((FolderListActivity) getActivity()).onCreate(savedInstanceState);

//        Intent intent = new Intent(getActivity(), FolderListActivity.class);
//        startActivity(intent);

        return view;
    }



    //버튼 클릭 시 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view){ //버튼 클릭 이벤트 처리
            switch (view.getId()){
                //"폴더 생성" 버튼 클릭 이벤트 처리
                case R.id.btn_createFolder:
                    setActivityChange();
                    break;
                case R.id.btn_taking_photo:
                    break;
                case R.id.btn_renameFolder:
                    break;
            }
        }
    };

    public void setActivityChange(){
        Button btn = (Button)getActivity().findViewById(R.id.btn_createFolder);
    }

}