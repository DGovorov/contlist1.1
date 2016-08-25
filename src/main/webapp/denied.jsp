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

<tr>
    ACCESS DENIED
    <br>
    <a href="/">Back to main page</a>
    <br>
    <form method="get" name="google" action="/login">
        <button type="submit">Login with Google</button>
    </form>
    <form method="get" action="/logout">
        <button type="submit">Log out</button>
    </form>
</body>
</html>
