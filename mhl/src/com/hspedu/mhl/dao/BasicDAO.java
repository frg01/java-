package com.hspedu.mhl.dao;

import com.hspedu.mhl.utils.JDBCUtilByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 开发BasicDao,是其他Dao的父类
 */
public class BasicDAO<T> {//泛型指定具体类型

    private QueryRunner qr = new QueryRunner();

    //开发通用dml方法，针对任意的表
    public int update(String sql,Object... parameters){

        Connection connection = null;

        try {
            connection = JDBCUtilByDruid.getConnection();
            int update = qr.update(connection, sql, parameters);
            return update;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);//编译异常转运行异常
        }finally{
            JDBCUtilByDruid.close(null,null,connection);
        }

    }

    //返回多个对象（多行），针对任意表

    /**
     *
     * @param sql sql语句  有？
     * @param clazz 传入一个类的Class对象  比如Actor.class
     * @param parameters 传入？ 的具体值，可以多个
     * @return  根据Actor.class 返回对应的Arraylist类型
     */
    public List<T> queryMulti(String sql,Class<T> clazz,Object... parameters){

        Connection connection = null;

        try {
            connection = JDBCUtilByDruid.getConnection();
            List<T> query = qr.query(connection, sql, new BeanListHandler<>(clazz), parameters);
            return query;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);//编译异常转运行异常
        }finally{
            JDBCUtilByDruid.close(null,null,connection);
        }
    }

    //查询单行结果的通用方法
    public T querySingle(String sql, Class<T> clazz,Object... parameters){

        Connection connection = null;

        try {
            connection = JDBCUtilByDruid.getConnection();
            T query = qr.query(connection, sql, new BeanHandler<T>(clazz), parameters);
            return query;

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);//编译异常转运行异常
        }finally{
            JDBCUtilByDruid.close(null,null,connection);
        }

    }

    //查询单行单列，即单值 的方法
    public Object queryScalar(String sql,Object... parameters){

        Connection connection = null;

        try {
            connection = JDBCUtilByDruid.getConnection();
            Object query = qr.query(connection, sql, new ScalarHandler(), parameters);
            return query;

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);//编译异常转运行异常
        }finally{
            JDBCUtilByDruid.close(null,null,connection);
        }
    }
}
