package com.cloudSerenityHotel.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JDBCUtils {
	private static DataSource ds;
	
	static {
	    try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:/comp/env/jdbc/csh");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 從DataSource獲取Connection的方法
	 * @return Connection物件
	 */
	public static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 關閉資源的方法，若沒有該資源可以傳入null
	 * @param conn 要關閉的Connection物件
	 * @param statement 要關閉的Statement(PreparedStatement)物件
	 * @param resultSet 要關閉的ResultSet物件
	 */
	public static void closeResource(Connection conn, Statement statement, ResultSet resultSet) {
		try {
			if (conn != null) {
				conn.close();
			}
			
			if (statement != null) {
				statement.close();
			}
			
			if (resultSet != null) {
				resultSet.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
