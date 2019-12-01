<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.io.*" %>

<%
FileOutputStream outa;
String a;
String buffer;
try{
outa = new FileOutputStream("/Users/hongjoonkim/eclipse-workspace/mobile2/WebContent/power.txt");
new PrintStream(outa).println("wer");
out.close(); 
}
catch (IOException e){
out.println ("Unable to write to file");
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
