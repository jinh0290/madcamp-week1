package com.example.fragment;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.fragment.tabs.TabHostFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TabHostFragment()).commit();
        }
    }
}