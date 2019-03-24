package com.julius.worksubmit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

//负责登录界面的账号和密码的合法验证
public class LoginActivity extends Activity implements View.OnClickListener {
    //登录按钮 注册按钮 忘记密码按钮 用户名和密码输入框
    private Button loginButton, registerButton, forgetPassWordButton;
    private EditText username, password;
    //记住密码和明密文切换
    private CheckBox login_remberPassWord;
    private CheckBox login_showPassWord;
    //保存文件名,存储模式(此处采用私有模式)
    private SharedPreferences preferences;
    private SharedPreferences.Editor sp = null;

    //本地
//    private final String LOGIN_URL = "http://localhost:8080/jobmanagementsystem/appLogin";
    private final String LOGIN_URL = "http://47.107.119.38:8080/jobmanagementsystem/appLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //控件初始化
        initControl();
        //帮助用户还原登录的账号和密码
        sp = Common.getSharePreferencesEditor(this, Common.USER_INFORMATION);
        preferences = Common.getSharePreferences(this, Common.USER_INFORMATION);
        boolean flag = preferences.getBoolean("flag", false);
        if (flag) {
            String user = preferences.getString("username", "");
            String pass = preferences.getString("password", "");
            this.username.setText(user);
            this.password.setText(pass);
            this.login_remberPassWord.setChecked(true);
        }
    }

    //控件初始化和设置监听
    private void initControl() {
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_loginbutton);
        registerButton = (Button) findViewById(R.id.login_registerbutton);
        forgetPassWordButton = (Button) findViewById(R.id.login_forgetpasswordbutton);
        login_remberPassWord = (CheckBox) findViewById(R.id.login_remberPassword);
        login_showPassWord = (CheckBox) findViewById(R.id.login_showPassword);
        login_remberPassWord.setOnClickListener(this);
        login_showPassWord.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPassWordButton.setOnClickListener(this);
        //权限申请(此处主要是读写外部文件的权限)
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //事件监听
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginbutton:
                //需要获取用户的输入合法验证和到数据库进行验证
                String username = this.username.getText().toString().trim();
                String password = this.password.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "输入不能为空,请检查", Toast.LENGTH_SHORT).show();
                    return;
                }
                //不管用户是否登录成功,都会保存账号,后面会使用到
                sp.putString("username", username);
                sp.apply();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                login(user);
                break;
            case R.id.login_registerbutton:
                register();
                break;
            case R.id.login_forgetpasswordbutton:
                Common.showToast("请联系管理员找回您的密码!", this);
                break;
            case R.id.login_showPassword:
                //监控密码明密文显示
                if (this.login_showPassWord.isChecked()) {
                    this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    //用户注册
    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 进行登录操作
     *
     * @param user 用户信息对象
     */
    private void login(final User user) {
        RequestParams params = new RequestParams(LOGIN_URL);
        //checkpwd?type=student
        params.addBodyParameter("studentId", user.getUsername());
        params.addBodyParameter("password", user.getPassword());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Common.showToast(result, Common.getContext());
                if (Common.SUCCESS.equals(result)) {
                    //判断用户是否选择记住密码
                    if (login_remberPassWord.isChecked()) {
                        sp.putString("username", user.getUsername());
                        sp.putString("password", user.getPassword());
                        sp.putBoolean("flag", true);
                        sp.apply();
                    } else {
                        sp.putString("username", user.getUsername());
                        //清空下本地密码
                        sp.putString("password", "");
                        sp.putBoolean("flag", false);
                        sp.apply();
                    }
                    Intent intent = new Intent(LoginActivity.this, CourseDetailActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Common.showToast("服务器出错,请稍后再试!", LoginActivity.this);
                Log.e("tag", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}