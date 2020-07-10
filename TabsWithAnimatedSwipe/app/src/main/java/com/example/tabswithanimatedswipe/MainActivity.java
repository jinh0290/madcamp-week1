package com.example.tabswithanimatedswipe;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends FragmentActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
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
        // Here, thisActivity is the current activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Instantiate a ViewPager2 and a PagerAdapter.
                    viewPager = findViewById(R.id.pager);
                    pagerAdapter = new TapPagerAdapter(this);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setPageTransformer(new ZoomOutPageTransformer());

                    TabLayout tabLayout = findViewById(R.id.tab_layout);
                    new TabLayoutMediator(tabLayout, viewPager,
                            this::setTextOfTabs).attach();
                    tabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
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