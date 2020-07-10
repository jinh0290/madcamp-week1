package com.example.week1_tab;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
//Activitiygroup AppCompatActivity
public class MainActivity extends AppCompatActivity {
    private ArrayList<Phonebook> phonebookArrayList = new ArrayList<>();
    ImageView selectedImage;

    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("Phonebook.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }

    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray phonebookArray = jsonObject.getJSONArray("Contact");

            for(int i=0; i<phonebookArray.length(); i++)
            {
                JSONObject phonebookObject = phonebookArray.getJSONObject(i);

                Phonebook phonebook = new Phonebook();

                phonebook.setName(phonebookObject.getString("name"));
                phonebook.setNumber(phonebookObject.getString("number"));

                phonebookArrayList.add(phonebook);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void InitializeData()
    {
        phonebookArrayList = new ArrayList<>();
        jsonParsing(getJsonString());
        //      phonebookArrayList.add(new Phonebook()R.drawable.2);
        //     phonebookArrayList.add(new Phonebook()R.drawable.3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////Tab//////////////////
        final TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();
        //tabHost1.setup(this.getLocalActivityManager());
        LayoutInflater.from(this).inflate(R.layout.activity_main,
                tabHost1.getTabContentView(), true);

        //탭에 넣을 이미지들 설정
        ImageView tabwidget01 = new ImageView(this);
        tabwidget01.setImageResource(R.drawable.tab_01);

        ImageView tabwidget02 = new ImageView(this);
        tabwidget02.setImageResource(R.drawable.tab_02);

        ImageView tabwidget03 = new ImageView(this);
        tabwidget03.setImageResource(R.drawable.tab_03);

        //Intent로 이동
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("Contact");
        //ts1.setContent(new Intent(this, Tab1Activity.class));
        tabHost1.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("Gallery");
        //ts2.setContent(new Intent(this, Tab2Activity.class));
        tabHost1.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("Quiz");
        //ts3.setContent(new Intent(this, Tab3Activity.class));
        tabHost1.addTab(ts3);

        tabHost1.setCurrentTab(0);
        this.InitializeData();

        //탭 배경색 설정
        tabHost1.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabld){
                for(int i = 0; i < tabHost1.getTabWidget().getChildCount(); i++){
                    tabHost1.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#255187"));
                }
                tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#4f81bd"));
            }
        });

        //RecyclerView
        RecyclerView recyclerView;
        RecyclerView.LayoutManager manager;

        recyclerView = findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new RecyclerViewAdapter(phonebookArrayList));  // Adapter 등록


        ////////////////////Gallery////////////////////
        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        selectedImage=(ImageView)findViewById(R.id.imageView);
        gallery.setSpacing(1);
        final ImageAdapter imageAdapter= new ImageAdapter(this);
        gallery.setAdapter(imageAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                selectedImage.setImageResource(imageAdapter.mImageIds[position]);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}