<%--
  Created by IntelliJ IDEA.
  User: Максим
  Date: 31.07.2016
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Protected data for authorised users</title>
</head>
<body>
<%
  String email = (String) session.getAttribute("email");
  out.print(email);
%>
<tr>
PROTECTED DATA
<br>
<a href="/"><< Back to main</a>
</body>
</html>
