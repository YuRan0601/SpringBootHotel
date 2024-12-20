package com.cloudSerenityHotel.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.utils.JDBCUtils;


public abstract class BaseDao {
	
	public int update(Connection conn, String sql, Object ... args) {
		int row = 0;
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			
			row = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return row;
	}
	
	public <T> List<T> getForList(Connection conn, String sql, Class<T> clazz, Object ... args) {
		List<T> res = new ArrayList<T>();
		
		try (PreparedStatement ps = conn.prepareStatement(sql)){
			
			for(int i = 0; i < args.length; i++) {
				
				ps.setObject(i + 1, args[i]);
				
			}
			
			try (ResultSet rs = ps.executeQuery()) {
				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				int columnCount = rsmd.getColumnCount();
				
				while(rs.next()) {
					
					Constructor<T> constructor = clazz.getDeclaredConstructor();
					
					T t = constructor.newInstance();
					
					for(int i = 0; i < columnCount; i++) {
						Object value = rs.getObject(i + 1);
						
						String columnLabel = rsmd.getColumnLabel(i + 1);
						
						Field field = clazz.getDeclaredField(columnLabel);
						
						field.setAccessible(true);
						
						field.set(t, value);
					}
					
					res.add(t);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public <T> T getOne(Connection conn, String sql, Class<T> clazz, Object ... args) {
		T t = null;
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			
			try (ResultSet rs = ps.executeQuery()) {
				
				ResultSetMetaData rsmd = rs.getMetaData();
				
				if(rs.next()) {
					
					Constructor<T> constructor = clazz.getConstructor();
					
					t = constructor.newInstance();
					
					for(int i = 0; i < rsmd.getColumnCount(); i++) {
						String columnLabel = rsmd.getColumnLabel(i + 1);
						
						Object value = rs.getObject(i + 1);
						
						Field field = clazz.getDeclaredField(columnLabel);
						
						field.setAccessible(true);
						
						field.set(t, value);
					}
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
    /**
     * 獲取特殊值 ex：單一欄位的值、彙總函數的值
     * @param <E> 回傳值的類型
     * @param conn Connection物件
     * @param sql 要執行的sql
     * @param args 填充sql的"?"的參數
     * @return 回傳的特殊值
     */
    public <E> E getValue(Connection conn, String sql, Object ... args) {
        try (PreparedStatement ps = conn.prepareStatement(sql)){
        	
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            
            
            try (ResultSet rs = ps.executeQuery()){
            	if(rs.next()) {
                    return (E) rs.getObject(1);
                }
			} catch (SQLException e) {
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
