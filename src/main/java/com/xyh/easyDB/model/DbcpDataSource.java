package com.xyh.easyDB.model;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created by xiayuhui on 2016/8/29.
 */
public class DbcpDataSource extends BasicDataSource {

    //下划线转驼峰写法
    private boolean mapUnderscoreToCamelCase = true;

    /**
     *  无意义，解决方法未实现
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }
}
