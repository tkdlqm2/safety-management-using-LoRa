package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DO.action;

public class actionDAO {
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
	
	public boolean add(action ab) {
		connect();
		String sql="insert into action(move) values(?);";
	
		PreparedStatement pstmt=null;
		try {
			
		
			pstmt=conn.prepareStatement(sql);
			if(ab.getMove() == null)
			{
				return false;
			}
			pstmt.setString(1,ab.getMove());
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
