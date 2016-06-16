<%@ page import="java.util.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Contacts database</title>
</head>
<body>
<h1>Contact list:</h1>
<%
    List<String[]> contacts = (List)request.getAttribute("customparam");

    for(String[] cont : contacts) {
        out.print("<br><a href=\"/change?id=" + cont[0] + "\">" + cont[0] + " - " + cont[1] + "</a>");
    }

%>
<br>
<br>
<form action="/create">
    <input type="submit" value="Create new">
</form>
</body>
</html>
