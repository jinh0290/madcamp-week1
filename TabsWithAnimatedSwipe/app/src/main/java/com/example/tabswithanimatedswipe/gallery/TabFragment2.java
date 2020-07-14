package com.example.tabswithanimatedswipe.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tabswithanimatedswipe.R;
import com.example.tabswithanimatedswipe.gallery.FullImageActivity;
import com.example.tabswithanimatedswipe.gallery.ImageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TabFragment2 extends Fragment {
    public static final String ARG_OBJECT = "object";

    //그리드뷰 변수들
    View fragmentView;
    GridView gridView;
    ImageAdapter imageAdapter;

    //fab 변수들
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_menu, fab_camera, fab_folder;

    //camera 변수
    private MediaScanner mMediaScanner;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //folderExplorer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_tab2, container, false);

        /*
        GridView
         */
        gridView = fragmentView.findViewById(R.id.grid_view); // .xml의 GridView와 연결
        imageAdapter = new ImageAdapter(getActivity()); //Adapter와 연결
        gridView.setAdapter(imageAdapter); // GridView가 ImageAdapter에서 받은 값 뿌릴 수 있게 연결

        //사진 선택시 확대
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });


        /*
        fab
         */
        fab_menu = fragmentView.findViewById(R.id.fab_menu);
        fab_camera = fragmentView.findViewById(R.id.fab_camera);
        fab_folder = fragmentView.findViewById(R.id.fab_folder);

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        /*
        camera
         */
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        /*
        folder
         */
        fab_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "folder name : "
                                + getSaveFolder().getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    //menu icon 클릭시 메뉴 보여주기
    public void showMenu(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_menu:
                anim();
                Toast.makeText(getActivity(), "메뉴", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_camera:
                anim();
                Toast.makeText(getActivity(), "카메라", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_folder:
                anim();
                Toast.makeText(getActivity(), "폴더", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //fab menu icon 클릭시 나타나는 효과
    public void anim() {

        if (isFabOpen) {
            fab_camera.startAnimation(fab_close);
            fab_folder.startAnimation(fab_close);
            fab_camera.setClickable(false);
            fab_folder.setClickable(false);
            isFabOpen = false;
        } else {
            fab_camera.startAnimation(fab_open);
            fab_folder.startAnimation(fab_open);
            fab_camera.setClickable(true);
            fab_folder.setClickable(true);
            isFabOpen = true;
        }
    }

    //카메라 접근 권한 확인
    public void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {

            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Toast.makeText(getActivity(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(getActivity(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
                }
            };

            TedPermission.with(getActivity())
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("카메라 권한이 필요합니다.")
                    .setDeniedMessage("거부하셨습니다.")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .check();
        } else{
            dispatchTakePictureIntent();
        }
    }

    //카메라 실행
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //폴더 생성
    private File getSaveFolder() {
        String folderName = "myAlbum";
        File dir = new File(Environment.DIRECTORY_PICTURES
                + File.pathSeparator + folderName);
        try {
            if(!dir.exists()){
                dir.mkdirs();
                Log.d("TAG", "===========폴더 없음");
            }else{
                Log.d("TAG", "===========폴더 이미 존재");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return dir;
    }
}

