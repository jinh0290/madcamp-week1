package com.example.tabswithanimatedswipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.tabswithanimatedswipe.contact.TabFragment1;
import com.example.tabswithanimatedswipe.drawing.TabFragment3;
import com.example.tabswithanimatedswipe.gallery.TabFragment2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.droidsonroids.gif.GifDrawable;

// 앱의 기본 틀(TabLayout과 ViewPager2)을 만드는 액티비티 - 탭 내부의 세부사항은 다른 Fragment를 불러와서 채움.
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
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Here, thisActivity is the current activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new TapPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if ((position + 1) == 3) {
                    // 그림판(3번탭)에 들어갔을 때에는 스와이프 비활성화
                    viewPager.setUserInputEnabled(false);
                } else {
                    viewPager.setUserInputEnabled(true);
                }
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                this::setTextOfTabs).attach();
        tabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
    }


    private void setTextOfTabs(TabLayout.Tab tab, int position) {
        switch (position + 1) {
            case 1 :
                tab.setText("연락처");
                GifDrawable gifDrawable1 = null;
                try { gifDrawable1 = new GifDrawable(getResources(), R.drawable.docparrot); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (gifDrawable1 != null) {
                    tab.setIcon(gifDrawable1);
                }
                break;
            case 2 :
                tab.setText("이미지");
                GifDrawable gifDrawable2 = null;
                try { gifDrawable2 = new GifDrawable(getResources(), R.drawable.quadparrot); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (gifDrawable2 != null) {
                    gifDrawable2.stop();
                    tab.setIcon(gifDrawable2);
                }
                break;
            case 3 :
                tab.setText("그림판");
                GifDrawable gifDrawable3 = null;
                try { gifDrawable3 = new GifDrawable(getResources(), R.drawable.picassoparrot); }
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
            Fragment fragment = new TabFragment1(); // 코드를 조금 더 이쁘게 만들 수 있을 것 같은데
            switch (position + 1) {
                case 1 :
                    // fragment = new TabFragment1();
                    break;
                case 2 :
                    fragment = new TabFragment2();
                    break;
                case 3 :
                    fragment = new TabFragment3();
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