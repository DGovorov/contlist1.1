<%@ page import="java.util.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Create contact</title>
</head>
<body>
<h1>Enter data about contact:</h1>
<a href="/"><< Back to list</a>
<form method="POST" action="/create">
    <% request.setCharacterEncoding("UTF-8"); %>
    <%
        List contact = (List)request.getAttribute("name");

        for (Object tmpit : contact) {
            out.print("<br>" + tmpit);
            out.print("<br> <input type=\"TEXT\" required name=\"" + tmpit + "\">\n");
        }
    %>

<br><input type="SUBMIT">
</form>
<br>
</body>
</html>
