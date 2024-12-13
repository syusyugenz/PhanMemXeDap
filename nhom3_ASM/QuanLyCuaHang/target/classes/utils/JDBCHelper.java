/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author bangp
 */
public class JDBCHelper {
    
    private static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl="jdbc:sqlserver://localhost:1433;database=QuanLyCuaHang;encrypt=false;integratedSecurity=false;";
    private static String user ="sa";
    private static String pass ="123";
    
    static {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException {
        Connection con = DriverManager.getConnection(dburl, user, pass);
        PreparedStatement pstmt;
        if (sql.trim().startsWith("{")) {
            pstmt = con.prepareCall(sql);
        } else {
            pstmt = con.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }

    public static ResultSet query(String sql, Object... args) throws SQLException {
        PreparedStatement pstmt = JDBCHelper.getStmt(sql, args);
        return pstmt.executeQuery();
    }

    public static Object value(String sql, Object... args) {
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            if (rs.next()) {
                return rs.getObject(0);
            }
            rs.getStatement().getConnection().close();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int update(String sql, Object... args) {
        try {
            PreparedStatement pstmt = JDBCHelper.getStmt(sql, args);
            try {
                return pstmt.executeUpdate();
            } finally {
                pstmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
