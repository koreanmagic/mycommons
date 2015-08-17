package kr.co.koreanmagic.commons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
	
	
	/*
	 * SELECT 문 만들기
	 */
	public static String createSelectSQL(String[] columns, String tableName, String where) {
		StringBuilder sql = new StringBuilder("SELECT ");
		StringUtils.join(sql, ",", columns).append(" FROM ").append(tableName);
		
		if(where != null && where.length() > 1)
			sql.append(" ").append(where);
		
		return sql.toString();
	}
	
	
	public static List<Object[]> array(Connection connection, String[] columns, String tableName, String where) throws SQLException {
		final List<Object[]> result = new ArrayList<>();
		
		PreparedStatement ps = connection.prepareStatement(createSelectSQL(columns, tableName, where));
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			result.add(array(columns, rs));
		}
		
		return result;
	}
	private static Object[] array(String[] columns, ResultSet rs) throws SQLException {
		Object[] result = new Object[columns.length];
		
		for(int i = 0, l = columns.length; i < l; i++) {
			result[i] = rs.getObject(i+1);
		}
		
		return result;
	}
	
	public static List<Map<String, Object>> map(Connection connection, String[] columns, String tableName, String where) throws SQLException {
		final List<Map<String, Object>> result = new ArrayList<>();
		
		PreparedStatement ps = connection.prepareStatement(createSelectSQL(columns, tableName, where));
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			result.add(map(columns, rs));
		}
		
		return result;
	}
	private static Map<String, Object> map(String[] columns, ResultSet rs) throws SQLException {
		
		Map<String, Object> map = new HashMap<>();

		for(int i = 0, l = columns.length; i < l; i++) {
			map.put(columns[i], rs.getObject(i+1));
		}
		
		return map;
	}

}
