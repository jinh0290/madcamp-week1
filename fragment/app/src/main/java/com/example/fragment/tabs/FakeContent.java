package com.example.fragment.tabs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class FakeContent implements TabHost.TabContentFactory {
    Context context;
    public FakeContent(Context mcontext){
        context = mcontext;
    }

    @Override
    public View createTabContent(String tag){
        View view = new View(context);
        view.setMinimumWidth(0);
        view.setMinimumHeight(0);
        return view;
    }
}
