<%@ page import="java.util.*" %>

<html>
<head>
    <title>Change contact</title>
</head>
<body>
<h1>Enter new data about contact:</h1>
<form action="/welcome">
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
<form>
    <input type="submit" value="Remove contact">
</form>
</body>
</html>
