<%@ page import="java.util.*" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Change contact</title>
</head>
<body>
<h1>Enter new data about contact:</h1>
<a href="/"><< Back to list</a>
<form method="POST" action="/change">
<%
    List<String[]>  contact = (List)request.getAttribute("name");
    //List thisCont = (List)request.getAttribute("thiscont");
    for (String[]  tmpit : contact) {
        out.print("<br>" + tmpit[0]);
        out.print("<br> <input type=\"TEXT\" placeholder=\""+tmpit[1]+"\" name=\"" + tmpit[0] + "\">\n");
    }
    out.print("<br> <input type=\"HIDDEN\" name=\"id\" value=" + request.getParameter("id") + ">\n");
%>

<br>
<input type="SUBMIT">
</form>
<br>
<% out.print("<form method=\"POST\" action=\"/?action=remove&id="+request.getParameter("id")+"\">");%>
    <input type="submit" value="Remove contact">
</form>
</body>
</html>
