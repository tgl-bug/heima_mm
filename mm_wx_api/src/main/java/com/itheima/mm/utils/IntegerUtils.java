package com.itheima.mm.utils;

/**
 * 包名:com.itheima.mm.utils
 *
 * @author Leevi
 * 日期2020-08-08  09:24
 */
public class IntegerUtils {
    public static Integer parseInteger(Object value){
        if (value instanceof Integer) {
            return (Integer) value;
        }else {
            return Integer.valueOf((String) value);
        }
    }
}
