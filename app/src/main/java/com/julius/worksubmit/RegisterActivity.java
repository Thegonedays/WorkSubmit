package com.julius.worksubmit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 处理用户注册
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText againPassWord;
    private EditText studentName;
    private Button submit;
    private Button reset;
    private final String REGISTER_URL = "http://47.107.119.38:8080/jobmanagementsystem/appRegister";
//    private final String URL = "http://192.168.0.105:8080/";
//    private final String REGISTER_URL = URL + "appRegister";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    //控件初始化以及设置监听
    private void init() {
        this.username = (EditText) findViewById(R.id.register_username);
        this.password = (EditText) findViewById(R.id.register_password);
        this.againPassWord = (EditText) findViewById(R.id.register_again_password);
        this.studentName = (EditText) findViewById(R.id.register_name);
        this.submit = (Button) findViewById(R.id.register_submitbutton);
        this.reset = (Button) findViewById(R.id.register_resetbutton);
        this.submit.setOnClickListener(this);
        this.reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_submitbutton:
                submit();
                break;
            case R.id.register_resetbutton:
                reset();
                break;
            default:

                break;
        }
    }


    //处理用户注册
    private void submit() {
        //获取 用户输入
        String username = this.username.getText().toString().trim();
        String studentName = this.studentName.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String againPassWord = this.againPassWord.getText().toString().trim();
        //简单校验
        if (!isEmpty(username)) {
            Common.showToast("账号输入不能为空,请检查!", Common.getContext());
            return;
        }
        if (!isEmpty(studentName)) {
            Common.showToast("姓名输入不能为空,请检查!", Common.getContext());
            return;
        }
        if (!isEmpty(password) || !isEmpty(againPassWord)) {
            Common.showToast("密码输入不能为空,请检查!", Common.getContext());
            return;
        }

        if (!password.equals(againPassWord)) {
            Common.showToast("两次密码输入不一致,请检查!", Common.getContext());
            return;
        }
        RequestParams params = new RequestParams(REGISTER_URL);
        params.addBodyParameter("studentId", username);
        params.addBodyParameter("studentName", studentName);
        params.addBodyParameter("password", password);
        //执行网络请求
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (Common.SUCCESS.equals(result)) {
                    //注册成功销毁当前活动
                    finish();
                }
                Common.showToast(result, Common.getContext());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //打印异常信息
                Log.e("error:{}", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    //清空输入
    private void reset() {
        this.username.setText("");
        this.password.setText("");
        this.againPassWord.setText("");
    }

    /**
     * 字符串或者对象非空判断b
     *
     * @param object 对像
     * @return Boolean类型
     */
    private Boolean isEmpty(Object object) {
        return !"".equals(object) && object != null;
    }
}
