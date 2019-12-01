package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DO.where;

public class whereDAO {
	Connection conn=null;
	PreparedStatement pstmt=null;
	
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://power.c7eehnt35wec.ap-northeast-2.rds.amazonaws.com:3306/power?useSSL=false&serverTimezone=UTC";
	
	void connect() {
		try 
		{
			Class.forName(jdbc_driver);
			conn = DriverManager.getConnection(jdbc_url, "power","D349h65307!");
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean add(where ab) {
		connect();
		String sql="insert into setlocation(latitute,longtitute,boundary) values(?,?,?);";
	
		PreparedStatement pstmt=null;
		try {
			
		
			pstmt=conn.prepareStatement(sql);
			if(ab.getLatitute() == null)
			{
				return false;
			}
			pstmt.setString(1,ab.getLatitute());
			pstmt.setString(2,ab.getLongtitute());
			pstmt.setString(3,ab.getBoundary());
			pstmt.executeUpdate();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		finally {
			disconnect();
		}
		return true;
	}
	
	public boolean updateDB(where vo)
	{
		String sql="update which set latitute=?, longtitute=?, boundary=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,vo.getLatitute());
			pstmt.setString(2, vo.getLongtitute());
			pstmt.setString(3,vo.getBoundary());
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	void disconnect() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
