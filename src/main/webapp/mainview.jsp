<%@ page import="java.util.*" %>
<html>
<head>
    <title>Contacts database</title>
</head>
<body>
<h1>Contact list:</h1>
<%
    List contacts = (List)request.getAttribute("customparam");
    Iterator it = contacts.iterator();

    while(it.hasNext()) {
        Object tmpit = it.next();
        out.print("<br><a href=\"/change?id=" + tmpit + "\">" + tmpit + "</a>");
    }

%>
<br>
<br>
<form action="/create">
    <input type="submit" value="Create new">
</form>
</body>
</html>
