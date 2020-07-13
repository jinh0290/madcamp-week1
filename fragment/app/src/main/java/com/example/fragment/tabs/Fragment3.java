package com.example.fragment.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fragment.camera.CameraActivity;
import com.example.fragment.R;

public class Fragment3 extends Fragment {

    public Fragment3(){}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);

        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);

//        ((CameraActivity)getActivity()).onCreate(savedInstanceState);

        return view;
    }
}
