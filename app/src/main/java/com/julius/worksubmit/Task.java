package com.julius.worksubmit;

import lombok.Getter;
import lombok.Setter;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 * 课程对应实体类
 */

@Setter
@Getter
public class Task extends User {
    //学生id
    private Integer studentId;
    //课程编号
    private Integer taskId;
    //课程名称
    private String taskName;
}
