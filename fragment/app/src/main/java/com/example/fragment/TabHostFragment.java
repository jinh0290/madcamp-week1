package com.example.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Vector;

public class TabHostFragment extends Fragment {
    private ViewPager viewPager;
    private TabsPageAdapter mAdapter;
    private TabHost host;
    private String[] Tabnames = {"Contacts", "Gallery", "Calendar"};

    //Constructors
    public TabHostFragment() {

    }
    public TabHostFragment newInstance(){
        TabHostFragment fragment = new TabHostFragment();
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabs, container, false);

        //TabHost
        host = (TabHost) rootView.findViewById(android.R.id.tabhost);
        host.setup();

        for(int i = 0; i < Tabnames.length; i++){
            TabHost.TabSpec tabSpec = host.newTabSpec(Tabnames[i]);
            tabSpec.setIndicator(Tabnames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            host.addTab(tabSpec);
        }
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int selectedItem = host.getCurrentTab();
                viewPager.setCurrentItem(selectedItem);
            }
        });

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        List<Fragment> fragmentList = new Vector<Fragment>();
        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2_gallery());
        fragmentList.add(new Fragment3());

        mAdapter = new TabsPageAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    //Adapter
    public class TabsPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        public TabsPageAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position){
            Fragment getItem = fragmentList.get(position);
            return getItem;
        }
        @Override
        public int getCount(){
            return fragmentList.size();
        }
    }

}