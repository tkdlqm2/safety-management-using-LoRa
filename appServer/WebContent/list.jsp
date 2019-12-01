<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*" %>
    <%@ page import = "java.sql.*" %>
    <%@ page import = "java.util.*" %>
    <%@ page import = "java.net.*" %>
    <%@ page import = "org.json.simple.JSONObject" %>
    <%@ page import = "org.json.simple.JSONArray" %>
<%@ page import="DAO.whereDAO" %>
<%@ page import="DO.where" %>
    
<%
	where result = null;
	where memVo = new where();
	whereDAO memDAO = new whereDAO();	
	String latitute = request.getParameter("latitute");
	String longtitute = request.getParameter("longtitute");
	String boundary = request.getParameter("boundary");
	
	System.out.println(latitute+"\t"+longtitute+"\t"+boundary+"\t");
	memVo.setLatitute(latitute);
	memVo.setLongtitute(longtitute);
	memVo.setBoundary(boundary);


	String temp = "success";
	if(memDAO.add(memVo))
	{
		temp = "ok";
	}

/* 
	else if(key.equals("update"))
	{
		//수정한 내용을 변수에 저장한 후 값 업데이트해주기
		memVO.setId(Integer.parseInt(request.getParameter("id")));
		memVO.setUsername(request.getParameter("username"));
        memVO.setTel(request.getParameter("tel"));
        memVO.setGender(request.getParameter("gender"));
        memVO.setHeight(Integer.parseInt(request.getParameter("height")));
        memVO.setGift(request.getParameter("gift"));
        memVO.setFruit(request.getParameter("fruit"));
        memVO.setWant(request.getParameter("want"));	     
        
        if(memDAO.updateDB(memVO))
        {
        	ArrayList<MemberVO> datas=memDAO.getList();
			request.setAttribute("datas", datas);
			RequestDispatcher view = request.getRequestDispatcher("result_list.jsp");
			view.forward(request, response);
        }
        else
        {
        	RequestDispatcher view = request.getRequestDispatcher("error.jsp");
			view.forward(request, response);
        }
		
	} */
	
	
%>  
<% 
	String url = "jdbc:mysql://power.c7eehnt35wec.ap-northeast-2.rds.amazonaws.com:3306/power?useSSL=false&serverTimezone=UTC";
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet rs = null;
	try
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url,"Id","password");
		String sql = "select * from setlocation order by id desc limit 1";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
		JSONObject jsonMain = new JSONObject(); // 객체
		JSONArray jArray = new JSONArray(); // 배열
		int count = 0;
		while(rs.next())
		{
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("boundary", rs.getString("boundary"));
			jsonobj.put("latitute", rs.getString("latitute"));
			jsonobj.put("longtitute", rs.getString("longtitute"));
			
			jArray.add(count, jsonobj);
			count++;
		}
		jsonMain.put("sendData", jArray); // JSON의 제목 지정
		out.clear(); // 출력 화면을 클리어 시킴
		out.println(jsonMain); // 출력
		out.flush(); // 버퍼를 비움
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		// db close 처리
		try
		{
			if(rs != null)
			{
				rs.close();
			}
			if(pstmt != null)
			{
				pstmt.close();
			}
			if(conn != null)
			{
				conn.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
