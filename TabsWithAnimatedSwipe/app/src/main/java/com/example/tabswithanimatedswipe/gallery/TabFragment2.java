package com.example.tabswithanimatedswipe.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tabswithanimatedswipe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class TabFragment2 extends Fragment {
    private static final int MY_PERMISSION_CAMERA = 5555;


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

    /*
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
     */

    //카메라 실행
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("카메라 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                                Toast.makeText(getActivity(), "카메라 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "카메라 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            dispatchTakePictureIntent();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults.length > 0) {
                    boolean check_result = true;
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            check_result = false;
                            break;
                        }
                    }
                    if (check_result) {
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        dispatchTakePictureIntent();
                    }
                } else {
                    // permission denied, boo!
                    Toast.makeText(getActivity(), "카메라 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}

