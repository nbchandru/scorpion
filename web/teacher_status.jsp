<%@ page import="org.apache.commons.collections4.MultiMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MultiMap<Integer,String> rows= (MultiMap<Integer, String>) session.getAttribute("rows");
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
    <link href="css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
    <link href="css/dataTables.responsive.css" rel="stylesheet" type="text/css" />
    <link href="css/metisMenu.css" rel="stylesheet" type="text/css" />

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
                <li>
                    <a href="path.jsp">
                        <i class="fa fa-dashboard"></i> <span>Set Paths</span>
                    </a>
                </li>
                <li class="treeview active">
                    <a href="#">
                        <i class="fa fa-book"></i>
                        <span>Status</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">

                        <form id="showcomplete" action="showComplete" method="post" >
                            <li><a onclick="showcomplete.submit()"><i class="fa fa-angle-double-right"></i> Student</a></li>
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

    <aside class="right-side">
        <section class="content-header">
            <h1>Student Status</h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">

                    <div class="dataTable_wrapper">
                        <div class="box-body table-responsive no-padding">
                            <table id="example" class="table table-striped table-bordered table-hover">

                                <thead>
                                <tr id="filterrow">
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Contact number</th>
                                </tr>

                                <tr>
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Contact number</th>
                                </tr>

                                </thead>

                                <tbody>
                                <%
                                    Set keySet = rows.keySet( );
                                    Iterator keyIterator = keySet.iterator();

                                    while( keyIterator.hasNext( ) ) {
                                        Object key = keyIterator.next( );
                                        System.out.print( "Key: " + key + ", " );

                                        Collection values = (Collection) rows.get(key); %>
                                <tr class="odd gradeX">
                                    <% Iterator valuesIterator = values.iterator();
                                        while( valuesIterator.hasNext()) { %>
                                    <td><%=valuesIterator.next()%></td>
                                    <% }%>
                                </tr>
                                <%
                                    }
                                %>
                                </tbody>

                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </aside>
</div>
</div>
</body>

<script src="js/jquery-1.11.0.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/app.js" type="text/javascript"></script>
<script src="js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/metisMenu.js" type="text/javascript"></script>
<script src="js/dataTables.bootstrap.min.js" type="text/javascript"></script>
<%--<script>--%>
<%--$(document).ready(function() {--%>
<%--$('#table1').DataTable({--%>
<%--responsive:true--%>
<%--});--%>
<%--});--%>
<%--</script>--%>
<%--<script>--%>
<%--$(document).ready(function(){--%>
<%--$('#table1').DataTable()--%>
<%--.columnFilter({--%>
<%--aoColumns: [ { type: "text" },--%>
<%--{ type: "text" },--%>
<%--{ type: "text" },--%>
<%--{ type: "text" },--%>
<%--{ type: "text" },--%>
<%--{ type: "text" }--%>
<%--]--%>

<%--});--%>
<%--});--%>
<%--</script>--%>
<script>
    $('#example thead tr#filterrow th').each( function () {
        var title = $('#example thead th').eq( $(this).index() ).text();
        $(this).html( '<input type="text" onclick="stopPropagation(event);" placeholder="Search '+title+'" />' );
    } );

    // DataTable
    var table = $('#example').DataTable( {
        orderCellsTop: true
    } );

    // Apply the filter
    $("#example thead input").on( 'keyup change', function () {
        table
                .column( $(this).parent().index()+':visible' )
                .search( this.value )
                .draw();
    } );

    function stopPropagation(evt) {
        if (evt.stopPropagation !== undefined) {
            evt.stopPropagation();
        } else {
            evt.cancelBubble = true;
        }
    }
</script>
</html>
