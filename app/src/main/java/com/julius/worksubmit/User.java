package com.julius.worksubmit;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 */

@Setter
@Getter
public class User {
    //用户唯一表示,没有业务含义
    private Integer id;
    //用户账号
    private String username;
    //用户密码
    private String password;
    //性别
    private String sex;
    //用户所在地
    private String address;
    //学生证号
    private Integer studentNumber;
    //手机号
    private String mobile;
    //用户审核状态,0未上传,1审核中,2已审核
    private Integer state;
    //注册时间
    private Date createDate;
    //发布商品数量
    private Integer issueCount;
}