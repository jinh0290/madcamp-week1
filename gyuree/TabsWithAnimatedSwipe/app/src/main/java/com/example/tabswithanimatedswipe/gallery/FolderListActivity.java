package com.example.tabswithanimatedswipe.gallery;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabswithanimatedswipe.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderListActivity extends ListActivity {
    LinearLayout layout;
    Button button1;
    Button button2;

    private List<String> item = null; //갤러리에 나타나는 내용
    private List<String> path = null; //item 클릭시 이동할 경로
//   onActivityResult
    private String root = "/sdcard/";
    //private String root = Environment.DIRECTORY_DCIM; //루트 디렉토리를 사진저장 폴더로 설정
    private TextView myPath ; //현재 경로를 저장

    public FolderListActivity(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.explorer);

        //현재경로 뷰어 설정: 상단 텍스트
 //       this.myPath = (TextView)this.findViewById(R.id.path);
        this.getDir(this.root);

    }

    //주어진 파일 경로(dirPath)에 대한 뷰 생성
    private void getDir(String dirPath) {
        //현재경로
        this.myPath.setText("Location: " + dirPath);

        this.item = new ArrayList();
        this.path = new ArrayList();

        //주어진 경로에 대한 File 객체 생성, 하위 디렉토리/파일 행렬 생성
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if(files == null) return;

        //item, path 추가
        if (!dirPath.equals(this.root)) {
            this.item.add(this.root); //root 폴더 아이템
            this.path.add(this.root); //root 폴더 경로
            this.item.add("../"); //상위 디렉토리
            this.path.add(f.getParent()); //상위 경로
        }

        //현재경로의 하위 디렉토리/파일의 item, path 추가
        for(int i = 0; i < files.length; ++i) {
            File file = files[i];
            this.path.add(file.getPath());
            if (file.isDirectory()) {
                this.item.add(file.getName() + "/");
            } else {
                this.item.add(file.getName());
            }
        }

        //ArrayAdapter를 통해서 현재경로에서의 뷰(목록) 생성
        ArrayAdapter<String> fileList = new ArrayAdapter(this, R.layout.row, this.item);
        this.setListAdapter(fileList);
    }

    //아이템 클릭시 이벤트 처리
    protected void onListItemClick(ListView l, View v, int position, long id) {
        /*
            현재 뷰가 root 디렉토리가 아닌 경우,
            path.get(0) = root 경로
            path.get(1) = 상위 디렉토리 경로
            path.get(2) = getDir에서 저장된 하위 디렉토리/파일 경로
         */
        File file = new File((String)this.path.get(position));

        //디렉토리를 클릭한 경우
        if (file.isDirectory()) {
            //파일 접근 권한 확인
            if (file.canRead()) { //접근 허용
                this.getDir((String)this.path.get(position)); //하위 디렉토리/파일을 뷰로 구성
            } else { //접근 거부
                (new AlertDialog.Builder(this)).setIcon(R.drawable.button)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }

        //파일을 클릭한 경우
        } else {
            (new AlertDialog.Builder(this))
                    .setIcon(R.drawable.button)
                    .setTitle("[" + file.getName() + "]")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }

    }
}