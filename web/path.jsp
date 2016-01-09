<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.File" %>
<%--
  Created by IntelliJ IDEA.
  User: Bharath
  Date: 22-03-2015
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = null;
    if(session.getAttribute("a_id") == null){
        response.sendRedirect("login.html");
    }else user = (String) session.getAttribute("a_id");
    System.out.println(user);
    System.out.println((String)session.getAttribute("department"));
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


    String report="";
    String logo="";
    String db_pass="";
    String db_user="";
    String jdbc_url="";
    String jrxml = "";
    String csvupload="";
    String picture_path="";
    Properties properties=new Properties();
    boolean exists=false;
    try {
        File file=new File(getServletConfig().getServletContext().getRealPath("/WEB-INF/omni.properties"));
        if(file.exists())
            exists=true;


if(exists==true) {
    FileInputStream inputStream = new FileInputStream(getServletConfig().getServletContext().getRealPath("/WEB-INF/omni.properties"));
    properties.load(inputStream);
    report=properties.getProperty("report");
    logo=properties.getProperty("logo");
    db_pass=properties.getProperty("db_pass");
    db_user=properties.getProperty("db_user");
    jdbc_url=properties.getProperty("jdbc_url");
    jrxml=properties.getProperty("jrxml");
    csvupload=properties.getProperty("csvupload");
    picture_path=properties.getProperty("picture_path");
}
        else {
    report = logo = db_pass = db_user =picture_path= jdbc_url = jrxml = csvupload = "";
}
    }
     catch (FileNotFoundException e) {
    e.printStackTrace();
    } catch (IOException e) {
    e.printStackTrace();
    }
%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    <title>Omniscient terminal</title>

    <!-- Bootstrap CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/AdminLTE.css" rel="stylesheet" type="text/css" />
    <link href="css/AdminLTE_custom.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/loading.css">
    <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
</head>

<body class="skin-blue">
<header class="header">
    <a href="Home" class="logo"> Omniscient Terminal </a>
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <div class="navbar-right">
            <ul class="nav navbar-nav">

                <li class="dropdown user user-menu">
                    <a href="LogoutServlet">
                        <i class="glyphicon glyphicon-user"></i>
                        <span>Logout</span>
                    </a>
                </li>
            </ul>
        </div>
    </nav>
</header>

<div class="wrapper row-offcanvas row-offcanvas-left">
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="left-side sidebar-offcanvas">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- Sidebar user panel -->
            <div class="user-panel">
                <div class="pull-left image">
                    <img src="img/admin.png" class="img-circle" alt="User Image" />
                </div>
                <div class="pull-left info">
                    <p>Hello</p>

                    <a href="#"> Admin</a>
                </div>
            </div>

            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu">
                <li>
                    <a href="Home">
                        <i class="fa fa-dashboard"></i> <span>Home</span>
                    </a>
                </li>
                <li>
                    <a href="create.jsp">
                        <i class="fa fa-dashboard"></i> <span>Create DB</span>
                    </a>
                </li>
                <li class="active">
                    <a href="#">
                        <i class="fa fa-dashboard"></i> <span>Set Paths</span>
                    </a>
                </li>
                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-book"></i>
                        <span>Status</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <form id="showcomplete" action="showComplete" method="post" >
                            <li id="stud_complete" class="stud"><a><i class="fa fa-angle-double-right"></i> Student</a></li>
                        </form>
                        <form id="showteachercomplete" action="showTeacherComplete" method="post" >
                            <li><a onclick="showteachercomplete.submit()"><i class="fa fa-angle-double-right"></i> Teacher</a></li>
                        </form>
                    </ul>
                </li>

            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>
<form id="pathform" action="pathconfig" method="post">
    <aside class="right-side">
        <section class="content">
                <span class="container">
	<div class="div1"></div>
	<div class="div2"></div>
	<div class="div3"></div>
	<div class="div4"></div>
</span>
            <div id="path" class="row">
                <div class="col-md-12">
                    <div class="input-group input-group-lg">
                  <span class="input-group-addon">
                  JRXMLs
                  </span>
                        <input type="text" id="jrxml" name="jrxml" value="<%=jrxml%>" required class="form-control" placeholder="Enter path">
                    </div>
                    <br>


                    <div class="input-group input-group-lg">
                  <span class="input-group-addon">
                  File upload
                  </span>
                        <input type="text" id="csvupload" name="csvupload" required value="<%=csvupload%>" class="form-control" placeholder="Enter path">
                    </div>
                    <br>

                    <div class="input-group input-group-lg">
                  <span class="input-group-addon">
                  RV Logo
                  </span>
                        <input type="text" id="logo" name="logo" value="<%=logo%>" class="form-control" placeholder="Path 1">
                    </div>
                    <br>

                    <div class="input-group input-group-lg">
                  <span class="input-group-addon">
                  Report Destination
                  </span>
                        <input type="text" id="report" name="report" value="<%=report%>" required class="form-control" placeholder="Path 10">
                    </div>
                    <br>

                    <br><br>

                    <button type="submit" onclick="validate();" class="btn btn-primary btn-lg">Submit</button>
                </div>
            </div>
        </section>
    </aside>
</form>
</div>

</body>

<script src="js/jquery-1.11.0.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/app.js" type="text/javascript"></script>
<script>

    $(document).ready(function() {
        $('.container').hide();
        $('.stud').on("click",function(){
            $(".row").hide();
            $('.container').show();
            setTimeout(delay,2000);
            $.ajax({
                type: "POST",
                url: "showComplete",
                success: function(msg){
                    if(msg.redirect)
                    window.location.href=msg.redirect;
                },
                error: function(){
                    alert("failure");
                }
            });
        });

    });
function delay()
{

}
    function showResponse(e, o, s, n) {
        if(e.err===true)
        {
            alert(e.msg);
        }
        else if(e.redirect)
        {
            window.location.href = e.redirect;
        }
    }
</script>
<script>
 function validate()
 {   var form=document.getElementById("pathform");
        for ( var i = 0; i < form.elements.length; i++ ) {
            var contPathWin = form.elements[i].val;

            if (contPathWin == "" || !windowsPathValidation(contPathWin)) {
                alert("please enter valid path");
                return false;
            }
        }


 }
</script>
<script>
    function windowsPathValidation(contwinpath) {
        if ((contwinpath.charAt(0) != "\\" || contwinpath.charAt(1) != "\\") || (contwinpath.charAt(0) != "/" || contwinpath.charAt(1) != "/")) {
            if (!contwinpath.charAt(0).match(/^[a-zA-Z]/)) {
                return false;
            }
            if (!contwinpath.charAt(1).match(/^[:]/) || !contwinpath.charAt(2).match(/^[\/\\]/)) {
                return false;
            }

        }
    }
</script>
</html>