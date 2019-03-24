package com.julius.worksubmit;

import lombok.Getter;
import lombok.Setter;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 * 教师实体类
 */
@Setter
@Getter
public class Teacher {
    //教师唯一id
    private Integer id;
    //教师姓名
    private String name;
    //教师头像,此时暂时是默认的
    private String photoUrl;

}
