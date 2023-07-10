/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.net.URL;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Jarrod
 */
public class DBManager {

    private static final BasicDataSource dataSource;
    
    public DBManager() {}

    static {
        GetProperties properties = new GetProperties("config.properties");
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.get("dbDriverClassName"));
        dataSource.setUrl(properties.get("url"));
        dataSource.setPassword(properties.get("dbPassword"));
        dataSource.setUsername(properties.get("dbUser"));
        dataSource.setMinIdle(10);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
        dataSource.setMaxTotal(100);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
