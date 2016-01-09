
<%--
  Created by IntelliJ IDEA.
  User: Bharath
  Date: 25-02-2015
  Time: 20:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = null;
    if(session.getAttribute("user") == null){
        response.sendRedirect("login.jsp");
    }else user = (String) session.getAttribute("user");
    String userName = null;
    String sessionID = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("user")) userName = cookie.getValue();
            if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
        }
    }else{
        sessionID = session.getId();
    }
Exception e=(Exception)request.getAttribute("e");
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    <title>Omniscient terminal</title>

    <!--<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'>-->

    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>
<header class="header">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h1>Omniscient terminal</h1>
            </div>
            <div class="col-md-6 pull-right">
                <form action="LogoutServlet" method="post">
                    <input type="submit" class="h5 dumb pull-right" value="Logout">
                </form>
            </div>
        </div>

    </div>
</header>
<br>
<%=e.printStackTrace(new java.io.PrintWriter(out)) %>
</br>
</body>
</html>
