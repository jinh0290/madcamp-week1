package com.example.fragment.gallery;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class FileCreatorActivity {
    String MIDEA_PATH = Environment.getExternalStorageDirectory() + "/";
    String BACKUP_PATH = MIDEA_PATH+"fdr/Backup/";

    File dir = new File(BACKUP_PATH);

    void createFolder(){

        try{
            //갤러리 경로에 폴더 생성
            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "testDir");

            //폴더 생성 성공여부 확인
            if (!dir.mkdirs()) {
                Log.e("FILE", "폴더 생성 실패");
            }else {
                //Toast.makeText(getContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
