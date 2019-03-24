package com.julius.worksubmit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 * 教师头像显示适配器,只有简单的上面固定头像,下面教师名称
 */

public class TeacherShowAdapter extends BaseAdapter {
    private Context context;
    private List<Teacher> teachers;

    public TeacherShowAdapter(final Context context, final List<Teacher> teachers) {
        this.context = context;
        this.teachers = teachers;
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        //如果缓存view为空,则加载布局
        if (view == null) {
            //加载布局
            view = View.inflate(context, R.layout.teacher_show_gridview, null);
            //实例化对象
            viewHolder = new ViewHolder();
            viewHolder.teacher_id = (TextView) view.findViewById(R.id.teacher_show_id);
            viewHolder.teacher_photo = (ImageView) view.findViewById(R.id.teacher_show_photo);
            viewHolder.teacherName = (TextView) view.findViewById(R.id.teacher_show_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //获取id
        Integer id = this.teachers.get(i).getId();
        viewHolder.teacher_id.setText(id.toString());
        //获取姓名
        String name = this.teachers.get(i).getName();
        viewHolder.teacherName.setText(name);
        return view;
    }

    static class ViewHolder {
        //教师id,需要传递到数据库查询信息
        TextView teacher_id;
        //教师头像,暂时默认
        ImageView teacher_photo;
        //教师姓名
        TextView teacherName;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
