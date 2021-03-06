package com.xyh.easyDB.helper;

import com.xyh.easyDB.model.DbcpDataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据库连接工具类
 * Created by xiayuhui on 2016/8/24.
 */
public class DBHelper {

    /**
     * 初始化数据源
     */
    public static DataSource initDataSource() {
        //配置dbcp数据源
        DbcpDataSource dbcpDataSource = new DbcpDataSource();
        dbcpDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&connectTimeout=1000&serverTimezone=GMT");
        //dbcpDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbcpDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dbcpDataSource.setUsername("root");
        dbcpDataSource.setPassword("xyh19910214");
        dbcpDataSource.setDefaultAutoCommit(true);
        dbcpDataSource.setMaxActive(100);
        dbcpDataSource.setMaxIdle(30);
        dbcpDataSource.setMaxWait(500);
        return dbcpDataSource;
    }

    public static DataSource initDataSource(Map<String, String> config) {
        //配置dbcp数据源
        DbcpDataSource dbcpDataSource = new DbcpDataSource();
        if(config.get("url") != null) {
            dbcpDataSource.setUrl(config.get("url"));
        } else {
            String host = config.get("host");
            String port = config.get("port");
            String database = config.get("database");
            dbcpDataSource.setUrl(combineUrl(host, port, database));
        }
        if(config.get("driverClassName") != null) {
            dbcpDataSource.setDriverClassName(config.get("driverClassName"));
        } else {
            dbcpDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        }
        dbcpDataSource.setUsername(config.get("username"));
        dbcpDataSource.setPassword(config.get("password"));
        //可选配置项
        dbcpDataSource.setDefaultAutoCommit(true);
        dbcpDataSource.setMaxActive(100);
        dbcpDataSource.setMaxIdle(30);
        dbcpDataSource.setMaxWait(500);
        return dbcpDataSource;
    }

    /**
     * 拼接url
     */
    private static String combineUrl(String host, String port, String database) {
        StringBuilder url = new StringBuilder().append("jdbc:mysql://");
        url.append(host);
        url.append(":");
        url.append(port);
        url.append("/");
        url.append(database);
        url.append("?useUnicode=true&characterEncoding=UTF-8&connectTimeout=1000&serverTimezone=GMT");
        return url.toString();
    }

    /**
     * 读取配置文件
     */
    public static Map<String, String> initConfig(String configName) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            Configuration config = new PropertiesConfiguration(configName);
            Iterator<String> keyIterator = config.getKeys();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                result.put(key, config.getString(key));
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Connection getConnection(DataSource dataSource) {
        Connection connection = null;
        try {
            if(dataSource != null) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static QueryRunner getQueryRunner(DataSource dataSource) {
        if(dataSource != null){
            return new QueryRunner(dataSource);
        } else {
            return null;
        }
    }

}
