package com.example.helloworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawBoard extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    /* Define Variables */
    ArrayList<Contacts> contactList = new ArrayList<>();
    private ImageView imageView;
    private Button draw_red_btn;
    private Button draw_blue_btn;
    private Button draw_black_btn;
    private Button draw_green_btn;
    private Button clearbtn;
    private Button eraser_btn;
    private Button custombtn;
    private Button save_color_btn;
    private Button option_btn;
    int tColor;

    private Paint mPaint;
    private int paintColor = Color.BLACK;
    private SimpleDrawingView paintview;
    SeekBar seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Tabs */
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("CONTACTS");
        tabHost1.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("GALLERY");
        tabHost1.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("DRAWING");
        tabHost1.addTab(ts3);

        /* Tab 3: Painter */
        paintview = (SimpleDrawingView) findViewById(R.id.simpleDrawingView1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintview.init(metrics);

        /* Buttons */
        draw_red_btn = (Button) findViewById(R.id.draw_red_btn);
        draw_red_btn.setOnClickListener(this);

        draw_blue_btn = (Button) findViewById(R.id.draw_blue_btn);
        draw_blue_btn.setOnClickListener(this);

        draw_black_btn = (Button) findViewById(R.id.draw_black_btn);
        draw_black_btn.setOnClickListener(this);

        draw_green_btn = (Button) findViewById(R.id.draw_green_btn);
        draw_green_btn.setOnClickListener(this);

        clearbtn = (Button) findViewById(R.id.clearbtn);
        clearbtn.setOnClickListener(this);

        eraser_btn = (Button) findViewById(R.id.eraser_btn);
        eraser_btn.setOnClickListener(this);

        custombtn = (Button) findViewById(R.id.custombtn);
        custombtn.setOnClickListener(this);

        save_color_btn = (Button) findViewById(R.id.save_color_btn);
        save_color_btn.setOnClickListener(this);

        option_btn = (Button) findViewById(R.id.option_btn);
        option_btn.setOnClickListener(this);

        /* SeekBar */
        seek = (SeekBar)findViewById(R.id.simpleSeekBar);
        seek.setOnSeekBarChangeListener(mSeekBar);
        seek.setMax(30);
        seek.setProgress(10);
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
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

    /* Supporting Click for Painter */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draw_red_btn:
                paintview.red();
                break;
            case R.id.draw_blue_btn:
                paintview.blue();
                break;
            case R.id.draw_black_btn:
                paintview.black();
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
                PopupMenu popup = new PopupMenu(DrawBoard.this, v);
                popup.setOnMenuItemClickListener(DrawBoard.this);
                popup.inflate(R.menu.menu_main);
                popup.show();
                break;
        }
    }

    /* Setting Options menu item click */
    int saveCnt = 0;
    final int[] mSelect = {0};

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        final String[] imgPath;

        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
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
                paintview.invalidate();
                paintview.buildDrawingCache();
                Bitmap bitmap = paintview.getDrawingCache();
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/CustomPic"+ (saveCnt++) + ".png");
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    if(out != null){
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, out);
                        Toast.makeText(DrawBoard.this, "Save Success", 0).show();
                    }
                    out.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(DrawBoard.this, "File Not Found", 0).show();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}






