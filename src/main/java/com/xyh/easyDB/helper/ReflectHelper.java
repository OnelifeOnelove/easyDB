package com.xyh.easyDB.helper;

import com.xyh.easyDB.annotation.Column;
import com.xyh.easyDB.annotation.NoDBColumn;
import com.xyh.easyDB.annotation.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类
 * Created by xiayuhui on 2016/8/26.
 */
public class ReflectHelper {

    /**
     * 获取表名
     */
    public String getTableName(Object obj) {
        Class clazz = obj.getClass();
        Table table = (Table) clazz.getAnnotation(Table.class);
        return table.value();
    }

    public String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        return table.value();
    }

    /**
     * 获取所有非空字段
     */
    public Map<String, Object> getColumns(Object obj)
            throws IllegalAccessException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Class clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            String name;
            Object val = field.get(obj);
            NoDBColumn flag = field.getAnnotation(NoDBColumn.class);
            //字段值不为空且不为id，不带NoDBColumn注解时插入
            if(val != null && !"id".equals(field.getName()) && flag == null) {
                //存在Column注解读取注解里的值，不然使用字段值
                Column meta = field.getAnnotation(Column.class);
                if(meta != null) {
                    name = field.getAnnotation(Column.class).value();
                } else {
                    //驼峰转为下划线写法
                    name = formatStr(field.getName());
                }
                dataMap.put(name, val);
            }
        }
        return dataMap;
    }

    /**
     * 设置id值
     */
    public void setId(Object obj, int id)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = obj.getClass();
        //通过反射设置id
        Method m = clazz.getMethod("setId", new Class[]{Integer.class});
        m.invoke(obj, id);
    }

    /**
     * 获取id
     */
    public Object getId(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = obj.getClass().getMethod("getId");
        return  m.invoke(obj);
    }


    /**
     * 字段格式转换
     */
    public String formatStr(String str) {
        StringBuilder result = new StringBuilder();
        for(Character c : str.toCharArray()) {
            if(Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

}
