<%@ page import="java.util.*" %>

<%@ page import="com.auth.AuthChecker" %>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Contacts database</title>
</head>
<body>
<h1>Contact list:</h1>

<%
    List<String[]> contacts = (List) request.getAttribute("customparam");

    for (String[] cont : contacts) {
        out.print("<br><a href=\"/change?id=" + cont[0] + "\">" + cont[0] + " - " + cont[1] + "</a>");
    }

%>

<br>
<br>
<form action="/create">
    <input type="submit" value="Create new">
</form>

<form action="/authorized">
    <input type="submit" value="Protected">
</form>


<div class="oauthDemo">
    <%
        final AuthChecker checker = new AuthChecker();
        Boolean authout = checker.CkeckStatus(session, request, response);

        String message = checker.AuthMessageMainPage(authout, session, request);

        if (!authout) {
            out.println("<form method=\"get\" name=\"google\" action=\"/login/google\">" +
                    "<button type=\"submit\">Login with Google</button>" +
                    "</form>");
            out.println("<form method=\"get\" name=\"vk\" action=\"/login/vk\">" +
                    "<button type=\"submit\">Login with Vkontakte</button>" +
                    "</form>");
        }
        else
            out.println("<form method=\"get\" action=\"/logout\">"+
                    "<button type=\"submit\">Log out</button>"+
                    "</form>");


        out.println(message);


    %>

</div>
</body>
</html>
