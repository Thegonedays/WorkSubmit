package com.julius.worksubmit;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectFileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView path;
    private ListView listView;
    //记录当前的父文件夹
    private File parentFiles;
    //记录当前文件夹下面的图片
    private File[] currentFiles;
    //返回上级按钮
    private Button upButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        listView = (ListView) findViewById(R.id.selectFile_ListView);
        path = (TextView) findViewById(R.id.select_activity_myList_textView);
        upButton = (Button) findViewById(R.id.selectFile_button);
        upButton.setOnClickListener(this);
        //获取根目录
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (root.exists()) {
            parentFiles = root;
            currentFiles = root.listFiles();
            //填充ListView
            inflateListView(currentFiles);
        }
        //设置监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentFiles[i].isFile()) {
                    try {
                        //文件完整路径
                        String FilePath = parentFiles.getCanonicalPath() + "/" + currentFiles[i].getName();
                        Intent intent = new Intent();
                        intent.putExtra("file_name", FilePath);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                File[] tmp = currentFiles[i].listFiles();
                if (tmp == null || tmp.length == 0) {
                    Toast.makeText(SelectFileActivity.this, "当前路径没有文件或者不可访问", Toast.LENGTH_SHORT).show();
                } else {
                    //获取用户单击列表项对应的文件夹,设为当前的父文件夹
                    parentFiles = currentFiles[i];
                    //保存当前文件夹下面的全部文件和文件夹
                    currentFiles = tmp;
                    inflateListView(currentFiles);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectFile_button) {
            upLevel();
        }
    }

    private void inflateListView(File[] currentFiles) {
        //创建一个List集合
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < currentFiles.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            if (currentFiles[i].isDirectory()) {
                listItem.put("icon", R.drawable.folder);
            } else {
                listItem.put("icon", R.drawable.file);
            }
            listItem.put("fileName", currentFiles[i].getName());
            listItems.add(listItem);
        }
        //创建一个简单适配器
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listItems, R.layout.select_activity_mylist,
                new String[]{"icon", "fileName"},
                new int[]{R.id.select_activity_myList_icon, R.id.select_activity_myList_textView});
        listView.setAdapter(adapter);
        try {
            path.setText("当前文件路径为:" + parentFiles.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回上一级
    private void upLevel() {
        try {
            if (!parentFiles.getCanonicalPath().equals
                    (Environment.getExternalStorageDirectory().getAbsolutePath())) {
                //获取上一层目录
                parentFiles = parentFiles.getParentFile();
                //列出当前下面所有文件
                currentFiles = parentFiles.listFiles();
                //再次刷新ListView
                inflateListView(currentFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
