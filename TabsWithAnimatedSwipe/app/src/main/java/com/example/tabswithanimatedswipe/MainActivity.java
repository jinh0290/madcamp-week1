package com.example.tabswithanimatedswipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new TapPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                this::setTextOfTabs).attach();
        tabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
    }

    private void setTextOfTabs(TabLayout.Tab tab, int position) {
        switch (position + 1) {
            case 1 :
                tab.setText("연락처");
                GifDrawable gifDrawable1 = null;
                try { gifDrawable1 = new GifDrawable(getResources(), R.drawable.partyparrot); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (gifDrawable1 != null) {
                    gifDrawable1.stop();
                    tab.setIcon(gifDrawable1);
                }
                break;
            case 2 :
                tab.setText("이미지");
                GifDrawable gifDrawable2 = null;
                try { gifDrawable2 = new GifDrawable(getResources(), R.drawable.shufflefurtherparrot); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (gifDrawable2 != null) {
                    gifDrawable2.stop();
                    tab.setIcon(gifDrawable2);
                }
                break;
            case 3 :
                tab.setText("자율");
                GifDrawable gifDrawable3 = null;
                try { gifDrawable3 = new GifDrawable(getResources(), R.drawable.slomoparrot); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (gifDrawable3 != null) {
                    gifDrawable3.stop();
                    tab.setIcon(gifDrawable3);
                }
                break;
        }
    }

    private class TapPagerAdapter extends FragmentStateAdapter {
        public TapPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new ObjectFragment2(); // 코드를 조금 더 이쁘게 만들 수 있을 것 같은데
            switch (position + 1) {
                case 1 :
                    fragment = new ObjectFragment1();
                    break;
                case 2 :
                    // fragment = new ObjectFragment2();
                    break;
                case 3 :
                    fragment = new ObjectFragment3();
                    break;
            }

            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f); // perfectly transparent

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view. setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {
                // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }

        }

    }

    private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            GifDrawable gifDrawable = (GifDrawable) tab.getIcon();
            gifDrawable.reset();
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            GifDrawable gifDrawable = (GifDrawable) tab.getIcon();
            gifDrawable.reset();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab)  {
            GifDrawable gifDrawable = (GifDrawable) tab.getIcon();
            gifDrawable.stop();
        }
    }
}