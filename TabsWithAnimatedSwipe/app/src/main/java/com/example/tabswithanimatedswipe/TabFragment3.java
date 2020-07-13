package com.example.tabswithanimatedswipe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.os.Environment.DIRECTORY_PICTURES;

public class TabFragment3 extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    /* Define Variables */
    private static final int MY_PERMISSION_EXTERNAL_WRITE = 4444;

    private Button draw_red_btn;
    private Button draw_blue_btn;
    private Button draw_green_btn;
    private Button clearbtn;
    private Button eraser_btn;
    private Button custombtn;
    private Button save_color_btn;
    private Button option_btn;
    int saveCnt = 0;
    int tColor;

    private SimpleDrawingView paintview;
    SeekBar seek;

    SeekBar.OnSeekBarChangeListener mSeekBar = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            paintview.change_size(i);
            paintview.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_object3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        paintview = (SimpleDrawingView) view.findViewById(R.id.simpleDrawingView1);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintview.init(metrics);

        /* Buttons */
        draw_red_btn = (Button) view.findViewById(R.id.draw_red_btn);
        draw_red_btn.setOnClickListener(this);

        draw_blue_btn = (Button) view.findViewById(R.id.draw_blue_btn);
        draw_blue_btn.setOnClickListener(this);

        draw_green_btn = (Button) view.findViewById(R.id.draw_green_btn);
        draw_green_btn.setOnClickListener(this);

        clearbtn = (Button) view.findViewById(R.id.clearbtn);
        clearbtn.setOnClickListener(this);

        eraser_btn = (Button) view.findViewById(R.id.eraser_btn);
        eraser_btn.setOnClickListener(this);

        custombtn = (Button) view.findViewById(R.id.custombtn);
        custombtn.setOnClickListener(this);

        save_color_btn = (Button) view.findViewById(R.id.save_color_btn);
        save_color_btn.setOnClickListener(this);

        option_btn = (Button) view.findViewById(R.id.option_btn);
        option_btn.setOnClickListener(this);

        /* SeekBar */
        seek = (SeekBar) view.findViewById(R.id.simpleSeekBar);
        seek.setOnSeekBarChangeListener(mSeekBar);
        seek.setMax(30);
        seek.setProgress(10);
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintview.setColor(color);
                save_color_btn.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draw_red_btn:
                paintview.red();
                break;
            case R.id.draw_blue_btn:
                paintview.blue();
                break;
            case R.id.draw_green_btn:
                paintview.green();
                break;
            case R.id.clearbtn:
                paintview.clear();
                break;
            case R.id.eraser_btn:
                paintview.erase();
                break;
            case R.id.custombtn:
                openColorPicker();
                break;
            case R.id.save_color_btn:
                ColorDrawable saved_color = (ColorDrawable)save_color_btn.getBackground();
                int save = saved_color.getColor();
                paintview.saveColor(save);
                break;
            case R.id.option_btn:
                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.menu_main);
                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                paintview.normal();
                return true;
            case R.id.emboss:
                paintview.emboss();
                return true;
            case R.id.blur:
                paintview.blur();
                return true;
            case R.id.save:
                checkExternalWritePermission(item);
                return true;
            default:
                return false;
        }
    }

    private void checkExternalWritePermission(MenuItem item) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_EXTERNAL_WRITE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // permssion 있을 때 할 일
            paintview.invalidate();
            paintview.buildDrawingCache();
            Bitmap bitmap = paintview.getDrawingCache();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CustomPic"+ (saveCnt++) + ".png");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if(out != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, out);
                    Toast.makeText(getActivity(), "Save Success", Toast.LENGTH_SHORT).show();
                }
                out.close();
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}