package com.julius.worksubmit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    //获取课程信息地址
    private final String GET_TASKS_URL = "http://192.168.0.105:8080/jobmanagementsystem/getTasks";
    //检查是否已经提交过
    private final String TASK_IS_SUBMIT = "http://192.168.0.105:8080/jobmanagementsystem/taskIsSubmit";
    //上传作业
    private final String TASK_UPLOAD_FILE = "http://192.168.0.105:8080/jobmanagementsystem/appUpload";

    private Button selectFile;
    private Button submit;
    private TextView textView;
    private Spinner spinner;
    private String filePath = "";
    //存放返回回来的Task对象集合
    private final List<Task> tasks = new ArrayList<>();
    //存放列表适配器的内容
    private final List<String> items = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("学生作业提交");
        init();
    }

    private void init() {
        submit = (Button) findViewById(R.id.select_home_work_submit);
        selectFile = (Button) findViewById(R.id.select_home_work);
        textView = (TextView) findViewById(R.id.select_home_work_path);
        spinner = (Spinner) findViewById(R.id.courses);
        submit.setOnClickListener(this);
        selectFile.setOnClickListener(this);
        getTasks();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_home_work_submit:
                if (filePath == null) {
                    Common.showToast("请选择需要上传的作业文件", this);
                    return;
                }
                checkIsSubmit();
                break;
            case R.id.select_home_work:
                selectFile();
                break;
        }
    }

    //检查作业是否已经提交并且提交作业
    private void checkIsSubmit() {
        int position = spinner.getSelectedItemPosition();
        String text = spinner.getSelectedItem().toString();
        if ("全部".equals(text)) {
            Common.showToast("请选择课程", Common.getContext());
            return;
        }
        //获取学生本地化存储信息
        SharedPreferences sp = Common.getSharePreferences(this,
                Common.USER_INFORMATION);
        final Integer taskId = tasks.get(position).getTaskId();
        final String studentId = sp.getString("username", "");
        RequestParams params = new RequestParams(TASK_IS_SUBMIT);
        params.addBodyParameter("taskId", taskId.toString());
        params.addBodyParameter("studentId", studentId);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.length() > 10) {
                    //弹出对话框,确认提交
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailActivity.this);
                    builder.setTitle("提示信息")
                            .setMessage(result)
                            .setCancelable(true)
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //提交
                                    submitTask(studentId, taskId.toString());
                                }
                            }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                } else {
                    //直接提交
                    submitTask(studentId, taskId.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    //获取需要提交的作业信息
    private void getTasks() {
        //获取学生本地化存储信息
        SharedPreferences sp = Common.getSharePreferences(this,
                Common.USER_INFORMATION);
        String studentId = sp.getString("username", "");
        RequestParams params = new RequestParams(GET_TASKS_URL);
        params.addBodyParameter("studentId", studentId);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.length() < 10) {
                    return;
                }
                    /*
                     不能调用toString()方法对字符串再次进行加工
                     * 不能使用Handler机制,去包装result再次进行发送
                     * 直接调用fastJson进行解析,不允许包装.
                     * 包装之后会出现无法解析,包含非法字符
                     */
                jsonToList(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    /**
     * 提交作业
     *
     * @param studentId 学生id
     * @param taskId    作业id
     */
    private void submitTask(String studentId, String taskId) {
        RequestParams params = new RequestParams(TASK_UPLOAD_FILE);
        //设置支持文件上传
        params.setMultipart(true);
        params.addBodyParameter("studentId", studentId);
        params.addBodyParameter("taskId", taskId);
        params.addBodyParameter("uploadFile", new File(filePath));
        final ProgressDialog progressDialog = new ProgressDialog(this);
        Common.showProgressDialog("提示信息", "努力提交中,请耐心等待", progressDialog, true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Common.showToast(result, Common.getContext());
                Common.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Common.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Common.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onFinished() {
                Common.dismissProgressDialog(progressDialog);
            }
        });
    }

    //json解析成POJO对象
    private void jsonToList(String response) {
        JSONArray array = null;
        try {
            //将数组Json转换成一个个对象,像下面这种格式
            //[{"name":"julius","age":22},{"name":"kitty","age":20}]
            array = JSON.parseArray(response);
            for (int index = 0; index < array.size(); index++) {
                Task task = new Task();
                //解析一个个对象{"name":"julius","age":22}
                JSONObject object = array.getJSONObject(index);
                //获取每个属性封装到javaBean对象中
                Integer taskId = object.getInteger("taskId");
                if (taskId != null) {
                    task.setTaskId(object.getInteger("taskId"));
                }
                String taskName = object.getString("taskName");
                if (taskName != null) {
                    task.setTaskName(taskName);
                }
                tasks.add(task);
            }
        } catch (Exception e) {
            Log.e("error:{}", e.getMessage());
        }
        //取出数据填充到item中
        for (int i = 0; i < tasks.size(); i++) {
            String taskName = tasks.get(i).getTaskName();
            if (taskName != null) {
                items.add(tasks.get(i).getTaskName());
            }
        }
        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(arrayAdapter);
        //刷新适配器
        arrayAdapter.notifyDataSetChanged();
    }

    //选择需要上传的文件
    private void selectFile() {
        Intent intent = new Intent(this, SelectFileActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //设置路径显示
                    filePath = data.getStringExtra("file_name");
                    textView.setText(filePath);
                    textView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
