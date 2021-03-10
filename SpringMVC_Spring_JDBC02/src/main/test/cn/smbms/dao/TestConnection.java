package cn.smbms.dao;

import cn.smbms.utils.BaseDao;
import org.junit.Test;

import java.sql.Connection;

/**
 * @ClassName TestConnection
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/7 15:08
 * @Version 1.0
 **/
public class TestConnection {

    @Test
    public void testConn(){
        Connection connection= BaseDao.getConnection();
        System.out.println("connection:"+connection);
    }
}
