package com.xyh.easyDB.helper;

import com.xyh.easyDB.annotation.Table;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * sql操作工具类
 * Created by xiayuhui on 2016/8/24.
 */
public class SQLHelper {

    private QueryRunner qr;

    private ReflectHelper helper = new ReflectHelper();

    public SQLHelper(DataSource dataSource) {
        this.qr = new QueryRunner(dataSource);
    }

    /**
     * 插入实体操作
     */
    public int insert(Object obj) throws SQLException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        int result;
        Map<String, Object> dataMap = helper.getColumns(obj);
        String tableName = helper.getTableName(obj);

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(tableName);
        //拼接SQL
        StringBuilder keyBuffer = new StringBuilder().append(" (");
        StringBuilder valueBuffer = new StringBuilder().append(" (");
        int length = dataMap.size();
        for(String key : dataMap.keySet()) {
            keyBuffer.append(key);
            valueBuffer.append("?");
            if(length > 1) {
                keyBuffer.append(",");
                valueBuffer.append(",");
            } else {
                keyBuffer.append(") ");
                valueBuffer.append(") ");
            }
            length--;
        }
        sql.append(keyBuffer);
        sql.append("values");
        sql.append(valueBuffer);
        Object[] params = dataMap.values().toArray();
        //执行SQL
        result = qr.update(sql.toString(), params);
        BigInteger newId = qr.query("select last_insert_id()", new ScalarHandler<BigInteger>(1));
        helper.setId(obj, newId.intValue());
        return result;
    }

    /**
     * 删除实体操作
     */
    public int delete(Object obj) throws SQLException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String tableName = helper.getTableName(obj);
        Object id = helper.getId(obj);
        StringBuilder sql = new StringBuilder().append("delete from ");
        sql.append(tableName);
        sql.append(" where id = ?");
        return qr.update(sql.toString(), id);
    }

    /**
     *  修改实体操作
     */
    public int update(Object obj) throws SQLException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> dataMap = helper.getColumns(obj);
        String tableName = helper.getTableName(obj);
        Object id = helper.getId(obj);

        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(tableName);
        sql.append(" set ");
        int length = dataMap.size();
        for(String key : dataMap.keySet()) {
            sql.append(key);
            sql.append("=?");
            if(length > 1) {
                sql.append(",");
            }
            length--;
        }
        sql.append(" where id = ");
        sql.append(id);
        Object[] params = dataMap.values().toArray();
        //执行SQL
        return qr.update(sql.toString(), params);
    }

    /**
     *  查询实体操作
     */
    public <T> T getEntity(Integer id, Class<T> clazz) throws SQLException {
        StringBuilder sql = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).value();
        sql.append("select * from ");
        sql.append(tableName);
        sql.append(" where id = ?");
        return qr.query(sql.toString(), new BeanHandler<T>(clazz), id);
    }

    /**
     *  查询实体操作(返回list)
     */
    public <T> List<T> getEntity(String condition, Class<T> clazz) throws SQLException {
        StringBuilder sql = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).value();
        sql.append("select * from ");
        sql.append(tableName);
        sql.append(" ");
        sql.append(condition);
        return qr.query(sql.toString(), new BeanListHandler<T>(clazz));
    }

    /**
     * 查询返回Map
     */
    public Map<String, Object> get(Integer id, Class clazz) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Table table = (Table) clazz.getAnnotation(Table.class);
        sql.append("select * from ");
        sql.append(table.value());
        sql.append(" where id = ?");
        return qr.query(sql.toString(), new MapHandler(), id);
    }

    /**
     * 查询返回Map(返回list)
     */
    public List<Map<String, Object>> get(String condition, Class clazz) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Table table = (Table) clazz.getAnnotation(Table.class);
        sql.append("select * from ");
        sql.append(table.value());
        sql.append(" ");
        sql.append(condition);
        return qr.query(sql.toString(), new MapListHandler());
    }

    /**
     * 查询计数
     */
    public int getCount(String sql) throws SQLException{
        return ((Long)qr.query(sql, new ScalarHandler(1))).intValue();
    }

    /**
     * 自定义SQL执行写操作
     */
    public int updateBySQL(String sql, Object... params) throws SQLException {
        return qr.update(sql.toString(), params);
    }

    /**
     * 自定义SQL执行读操作
     */
    public List<Map<String, Object>> getBySQL(String sql, Object... params) throws SQLException {
        return qr.query(sql.toString(), new MapListHandler());
    }

    /**
     * 返回某一列的所有值
     */
    public <T> List<T> getColumnData(String columnName, String tableName) throws SQLException {
        String aliasName = helper.formatStr(columnName);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(columnName);
        if(!columnName.equals(aliasName)) {
            sql.append(" ");
            sql.append(aliasName);
        }
        sql.append(" from ");
        sql.append(tableName);
        return qr.query(sql.toString(), new ColumnListHandler<T>());
    }

    public <T> List<T> getColumnData(String columnName, Class clazz) throws SQLException {
        String tableName = helper.getTableName(clazz);
        String aliasName = helper.formatStr(columnName);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(columnName);
        if(!columnName.equals(aliasName)) {
            sql.append(" ");
            sql.append(aliasName);
        }
        sql.append(" from ");
        sql.append(tableName);
        return qr.query(sql.toString(), new ColumnListHandler<T>());
    }

    /**
     * 自定义SQL返回查询某一列的值
     */
    public <T> List<T> getColumnData(String sql) throws SQLException {
        return qr.query(sql.toString(), new ColumnListHandler<T>());
    }

}
