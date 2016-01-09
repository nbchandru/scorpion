<%@ page import="org.apache.commons.collections4.MultiMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %><%--
  Created by IntelliJ IDEA.
  User: Bharath
  Date: 11/15/2015
  Time: 11:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MultiMap<Integer,MultiMap<Integer,String>> appraisals= (MultiMap<Integer, MultiMap<Integer, String>>) session.getAttribute("appraisals");

%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <script src="js/jquery-1.11.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/app.js" type="text/javascript"></script>
    <script src="js/materialize.min.js" ></script>

    <title>Omniscient terminal</title>

    <!-- Bootstrap CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/AdminLTE.css" rel="stylesheet" type="text/css" />
    <link href="css/AdminLTE_custom.css" rel="stylesheet" type="text/css" />
    <link href="css/materialize.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/loading.css">
    <!--<link href="css/prism.css" rel="stylesheet">-->
    <!--<link href="css/ghpages-materialize.css" type="text/css" rel="stylesheet" media="screen,projection">-->
    <link href="css/inconsolata.css" rel="stylesheet" type="text/css">
    <link href="css/material_icons.css" rel="stylesheet">
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
                <li class="active">
                    <a href="#">
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

    <aside class="right-side">
        <section class="content">
                 <span class="container">
	<div class="div1"></div>
	<div class="div2"></div>
	<div class="div3"></div>
	<div class="div4"></div>
         </span>
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs" id="tabs">
<%          Set keySet = appraisals.keySet();
    Iterator keyIterator = keySet.iterator();

    while( keyIterator.hasNext()) {
        Integer key = (Integer) keyIterator.next( );
        System.out.print( "Key: " + key + ", " );
        String active="";
        if(key+1==1) {
            active = "class=\"active\"";
        }
%>
       <li <%=active%> id="li<%=key+1%>">
           <a href="#appraisal_<%=key+1%>" data-toggle="tab" aria-expanded="true">Appraisal <%=key+1%></a>
       </li>

<% }%>
        <a class="btn waves-effect waves-light light-blue" onclick="addapp();"><i class="material-icons">add</i></a>
                        </ul>
                        <%--<form name="form2" method="GET" action="Report_test" id="form2">--%>
                            <%--<input type="hidden" name="semester"/>--%>
                            <%--<input type="hidden" name="count" />--%>
                        <%--</form>--%>
                        <%--<form name="form1" method="POST" action="Home" id="form1"></form>--%>
                        <div class="tab-content">
<%
    Set keySet1 = appraisals.keySet();
    Iterator keyIterator1 = keySet1.iterator();

    while( keyIterator1.hasNext()) {
        Integer key = (Integer) keyIterator1.next();
        System.out.print( "Key: " + key + ", " );
        String active="";
        if(key+1==1)
        {
         active="class=\"tab-pane active\"";
        }
%>
                            <div <%=active%> id="appraisal_<%=key+1%>">
                                <div class="box-body table-responsive no-padding">
                                    <table class="table table-hover">
                                        <tbody>
                                        <tr>
                                            <th>Semester</th>
                                            <th>Strength</th>
                                            <th>Given</th>
                                            <th>Enabled on</th>
                                            <th>Disabled on</th>
                                            <th></th>
                                            <th></th>
                                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn-floating btn-sm waves-effect waves-light red" onclick="addsem()"><i class="material-icons">add</i></a></th>
                                        </tr>

<%
    Collection activeappraisals= (Collection) appraisals.get(key);
    Iterator it=activeappraisals.iterator();
    while(it.hasNext())
    {
    MultiMap<Integer,String> activeappraisal= (MultiMap<Integer, String>) it.next();
    Set activeset=activeappraisal.keySet();
    Iterator activeiterator=activeset.iterator();
    while(activeiterator.hasNext())
        {         Integer activekey = (Integer) activeiterator.next();
            Collection values = (Collection) activeappraisal.get(activekey);
            Iterator valuesIterator = values.iterator();
%>
                                        <tr>
                                            <%
                                            while(valuesIterator.hasNext()) { %>
                                            <td><%=valuesIterator.next()%></td>
                                            <% }%>
                                            <td>
                                                <button  id="enable<%=key+1+"_"+activekey+1%>" name="enable<%=key+1+"_"+activekey+1%>" class="btn btn-block btn-success">Enable</button>
                                            </td>
                                            <td>
                                                <button  id="disable<%=key+1+"_"+activekey+1%>" name="disable<%=key+1+"_"+activekey+1%>" class="btn btn-block btn-danger">Disable</button>
                                            </td>
                                            <td>
                                                <button id="sem<%=key+1+"_"+activekey+1%>" name="sem<%=key+1+"_"+activekey+1%>" class="btn btn-block btn-info">Generate Report</button>
                                            </td>
                                        </tr>
<%}
}%>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
<%
}%>
                        </div>
                        <!--</form>-->
                    </div>
                </div>
            </div>
            <div class="t">
            <button id="backup" class="btn waves-effect waves-light" type="submit" name="action">Backup Full db
                <i class="material-icons right">send</i>
            </button>
                &nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;
                &nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;
                &nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;
                &nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;	&nbsp;
                <button id="allsem" class="btn waves-effect waves-light" type="submit" name="allsem">Generate reports for all semesters
                    <i class="material-icons right">send</i>
                </button>
            </div>
            <div class="formrow" id="formrow">
                <div class="col s12">
                    <div class="row">
                        <div class="input-field col s12">
                            <input id="sem" type="number" class="validate">
                            <label id="lbl" for="sem">Add Semester</label>
                            <button id="sub" class="btn waves-effect waves-light disabled" type="submit" name="action">Submit
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </aside>
</div>
<script>
    var app_id="appraisal1";
</script>
<script>
    $(document).ready(function() {
//      alert(  $('#tabs').find(".active_a").attr('id'));
        $('.formrow').hide();
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            var target = $(e.target).attr("href") // activated tab
            app_id = target;
        });

    });
</script>
<script>

    $(document).ready(function() {
//      alert(  $('#tabs').find(".active_a").attr('id'));
        $('.formrow').hide();
        $('#backup').click(function(){
            console.log("clicked");
            $.ajax({
                type: "POST",
                url: "backup",
                success: function(msg){
                    alert("Database successfully backed up");
                        window.location.href="Home";
                },
                error: function(){
                    alert("failure");
                }
            });
        });
    });
</script>
<script>
    $(document).ready(function() {
        $('.container').hide();
        $('.stud').on("click",function(){
            $(".row").hide();
            $(".t").hide();
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
    $(document).ready(function() {
        $('td').click(function () {
            var col = $(this).parent().children().index($(this));
            var row = $(this).parent().parent().children().index($(this).parent());
            var sem= $('table tr').eq(row).find('td').eq(0).html();
            console.log('Row: ' + row + ', Column: ' + col);
            console.log('Semester : ' + $('table tr').eq(row).find('td').eq(0).html());
            var lastChar = app_id.substr(app_id.length - 1);
            if(col===5||col===6) {
                $.ajax({
                    type: "POST",
                    url: "Home",
                    dataType: "json",
                    contentType: 'application/json; charset=UTF-8',
                    processData: false,
                    data: JSON.stringify({column: col, semester: sem, appraisal: lastChar, form : false}),
                    success: function (msg) {
                        console.log("success");
                        if (col === 5)
                            $('table tr').eq(row).find('td').eq(3).html(msg.date);
                        else
                            $('table tr').eq(row).find('td').eq(4).html(msg.date);
                        if (msg.redirect)
                            window.location.href = msg.redirect;
                    },
                    error: function (reason) {
                        alert("failure :" + reason);
                    }
                });
            }
            else if(col===7)
            {

                $(".row").hide();
                $(".t").hide();
                $('.container').show();
                $.ajax({
                    type: "POST",
                    url: "report",
                    dataType: "json",
                    contentType: 'application/json; charset=UTF-8',
                    processData: false,
                    data: JSON.stringify({semester: sem, appraisal: lastChar, form : false}),
                    success: function (msg) {
                            window.location.href = "ZipDownloadServlet";
                        alert("reports generated for semester "+sem+" successfully");
                    },
                    error: function (reason) {
                        $(".row").show();
                        $(".t").show();
                        $('.container').hide();
                        alert("reports generated for semester "+sem+" successfully");
                        window.location.href = "ZipDownloadServlet";

                    }
                });
            }
        });

    });
</script>
<script>
$('#allsem').click(function() {
    var lastChar = app_id.substr(app_id.length - 1);
    $(".row").hide();
    $(".t").hide();
    $('.container').show();
    $.ajax({
        type: "POST",
        url: "report",
        dataType: "json",
        contentType: 'application/json; charset=UTF-8',
        processData: false,
        data: JSON.stringify({semester: "9", appraisal: lastChar, form: false}),
        success: function (msg) {
            window.location.href = "ZipDownloadServlet";
            alert("reports generated for all semesters successfully");
        },
        error: function (reason) {
            $(".row").show();
            $(".t").show();
            $('.container').hide();
            alert("reports generated for all semesters successfully");
            window.location.href = "ZipDownloadServlet";

        }
    });
});


</script>
<script>
    function addsem()
    {
//        alert(app_id);
        $('.formrow').show();

    }

</script>
<script>
    $("#sem").bind("change paste keyup", function() {
       var fieldvalue = $.trim($(this).val());
       if (!fieldvalue || fieldvalue == "placeholdertext") {
           if(!$('#sub').hasClass('disabled'))
           $('#sub').addClass('disabled');

       } else if(fieldvalue > 8 || fieldvalue ==='e' ||fieldvalue < 1)
       {
           if(!$('#sub').hasClass('disabled'))
               $('#sub').addClass('disabled');
       }
        else {
           $('#sub').removeClass('disabled');

       }
   });
</script>
<script>
    $('#sub').click(function()
    {
        console.log($('#sem').val());
        console.log(app_id);
        var sem=$('#sem').val();
        var char=app_id.substr(app_id.length - 1);
        $.ajax({
            type: "POST",
            url: "Home",
            dataType: "json",
            contentType: 'application/json; charset=UTF-8',
            processData: false,
            data: JSON.stringify({semester: sem, appraisal: char ,form :true}),
            success: function (msg) {
                console.log("success");
//                if (msg.redirect)
                    window.location.href = "Home";
            },
            error: function (reason) {
                alert("failure :" + reason);
            }
        });



    });
</script>
<script>
    (function($) {
        $.fn.cloneWithProperties = function (properties) {
            return this.clone().prop(properties);
        };
    })(jQuery)
</script>
<script>
    function addapp()
    {
        var lastid=$('ul#tabs').children('li').last().attr('id');
        console.log(lastid);
        var lastidnum=lastid.substr(lastid.length-1);
        lastidnum++;
        console.log(lastidnum);
        var ref_this = $("ul.sidebar-menu li .active");
        console.log(ref_this.data('id'));
        $(ref_this).cloneWithProperties({ id: "li"+lastidnum  }).insertAfter($('li#'+lastid));
        $(ref_this).removeClass("active");
    }
</script>
</body>


</html>
