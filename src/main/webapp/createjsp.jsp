<%@ page import="java.util.*" %>
<html>
<head>
    <title>Create contact</title>
</head>
<body>
<h1>Enter data about contact:</h1>
<form method="POST" action="/create">
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
