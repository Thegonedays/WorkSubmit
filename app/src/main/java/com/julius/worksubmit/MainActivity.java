package com.julius.worksubmit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import lombok.core.Main;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Teacher> teachers = new ArrayList<>();
    //查询教师信息请求地址
    private final String QUERY_URL = Common.HTTPURL + "getTeachers";
    //适配器
    private TeacherShowAdapter teacherShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("作业提交");
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.teacher_show_gridview);
        //获取学生本地化存储信息
        SharedPreferences sp = Common.getSharePreferences(this,
                Common.USER_INFORMATION);
        //设置学生账号进行查询其教师信息
        Student student = new Student();
        student.setUsername(sp.getString("username", ""));
        //获取下服务器的数据
        getCommoditiesForTable(student);
    }

    private void getCommoditiesForTable(Student student) {
        RequestParams params = new RequestParams(QUERY_URL);
        if (student != null) {
            params.addBodyParameter("username", student.getUsername());
        }
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
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

    private void jsonToList(String response) {
        JSONArray array = null;
        try {
            //将数组Json转换成一个个对象,像下面这种格式
            //[{"name":"julius","age":22},{"name":"kitty","age":20}]
            array = JSON.parseArray(response);
            for (int index = 0; index < array.size(); index++) {
                Teacher teacher = new Teacher();
                //解析一个个对象{"name":"julius","age":22}
                JSONObject object = array.getJSONObject(index);
                //获取每个属性封装到javaBean对象中
                teacher.setId(object.getInteger("id"));
                teacher.setName(object.getString("name"));
                teachers.add(teacher);
            }
        } catch (Exception e) {
            Log.e("error:{}", e.getMessage());
        }
        if (teacherShowAdapter == null) {
            //设置更新Adapter
            teacherShowAdapter = new TeacherShowAdapter(this, teachers);
            gridView.setAdapter(teacherShowAdapter);
        }
        //刷新适配器显示
        teacherShowAdapter.notifyDataSetChanged();
        //监听点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取当前用户点击的列表项中教师id
                TextView textView = (TextView) view.findViewById(R.id.teacher_show_id);
                Integer id = Integer.valueOf(textView.getText().toString());
                Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //没有数据,不再进行监听滑动事件
//        if (array.size() < 10) {
//            return;
//        }
//        // 监听GridView滑动事件
//        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            int count = 1;
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    //保证只执行一次
//                    if (count == 1) {
//                        count++;
//                        //设置当前偏移量
//                        condition.setCurrentCount(totalItemCount);
//                        getCommoditiesForTable(condition);
//                    }
//                }
//            }
//        });
    }
}
