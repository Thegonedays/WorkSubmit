package com.julius.worksubmit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 */

public class Common {
    private Common() {}

    //全局的ip和端口设置
//    public static final String HTTPURL = "http://47.107.119.38:9999/course/";
    //本机
    public static final String HTTPURL = "http://192.168.0.105:8080/course/";
    //通用结果集
    public static final String SUCCESS = "成功";
    public static final String FAILURE = "失败";
    //更新软件请求地址
    public static final String UPDATE_SOFTWARE_URL = HTTPURL + "updateSoftWare";
    //收藏商品信息地址
    public static final String COLLECT_COMMODITY = HTTPURL + "collectCommodity";
    //本地化存储用户信息
    public static final String USER_INFORMATION = "userInformation";
    //商品信息相关本地化存储
    public static final String COMMODITY_INFO = "commodity_info";
    //查询商品地址
    public static final String SELECT_COMMODITIES = HTTPURL + "selectCommodities";
    //查询收藏商品地址
    public static final String SELECT_COLLECT_COMMODITIES = HTTPURL + "selectCollectCommodities";
    //删除商品地址
    public static final String DELETE_COMMODITY_BY_ID = HTTPURL + "deleteCommodityById";
    //获取系统信息地址
    public static final String GET_MESSAGE = HTTPURL + "getMessage";
    public static final String SELECT_COMMODITY_DETAIL = HTTPURL + "selectCommodityDetail";

    /**
     * 获取SharedPreferences存储对象,读取数据
     *
     * @param context  上下文
     * @param fileName 持久化文件名
     * @return SharedPreferences对象
     */
    public static SharedPreferences getSharePreferences(Context context, String fileName) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences.Editor存储对象,存储数据
     *
     * @param context  上下文
     * @param fileName 持久化文件名
     * @return SharedPreferences.Editor对象
     */
    public static SharedPreferences.Editor getSharePreferencesEditor(Context context, String fileName) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
    }

    /**
     * 全局获取当前登录用户名
     *
     * @return 当前登录用户名
     */
    public static String getCurrentUserName() {
        return getSharePreferences(getContext(), USER_INFORMATION).getString("username", "");
    }

    /**
     * 返回全局的上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 弹出一个Toast
     *
     * @param content 弹出内容
     * @param context 当前上下文
     */
    public static void showToast(String content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
    /**
     * 显示一个ProgressDialog
     *
     * @param title          标题
     * @param message        信息
     * @param progressDialog 对象
     * @param isCancel       是否可取消
     */
    public static void showProgressDialog(String title, String message,
                                          ProgressDialog progressDialog,
                                          Boolean isCancel) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancel);
        progressDialog.show();
    }
    /**
     * 取消显示ProgressDialog
     *
     * @param progressDialog 对象
     */
    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }
}