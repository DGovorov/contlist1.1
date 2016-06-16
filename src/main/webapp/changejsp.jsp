<%@ page import="java.util.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Change contact</title>
</head>
<body>
<h1>Enter new data about contact:</h1>
<form action="/">
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
<% out.print("<form method=\"POST\" action=\"/?action=remove&id="+request.getParameter("id")+"\">");%>
    <input type="submit" value="Remove contact">
</form>
</body>
</html>
