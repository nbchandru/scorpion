<%--
  Created by IntelliJ IDEA.
  User: Bharath
  Date: 11/15/2015
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>







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
                    <a href="Home">
                        <i class="fa fa-dashboard"></i> <span>Home</span>
                    </a>
                </li>
                <li>
                    <a href="#">
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
                    <form method="post" action="xlsReader" enctype="multipart/form-data">

                        <%--<div class="input-field col s6">--%>
                            <%--<select class="browser-default">--%>
                                <%--<option value="" disabled selected>Choose your option</option>--%>
                                <%--<option value="1">Option 1</option>--%>
                                <%--<option value="2">Option 2</option>--%>
                                <%--<option value="3">Option 3</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>

                        <div class="col-md-12">
                            <div class="file-field input-field">
                                <div class="btn">
                                    <span>Upload Files</span>
                                    <input type="file" multiple accept=".xlsx,.xlsm" name="file" size="5000000">
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="file-path validate" type="text">
                                </div>
                            </div>
                        </div>
                        <div>
                        <button id="sub" type="submit" name="action">Submit
                            <i class="material-icons right">send</i>
                        </button>
                        </div>

                    </form>
                    <form method="post" action="classxls" enctype="multipart/form-data" >
                        <div class="col-md-12">
                            <div class="file-field input-field">
                                <div class="btn">
                                    <span>Add class</span>
                                    <input type="file" multiple accept=".xls,.xlsx,.xlsm" name="file" size="5000000">
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="file-path validate" type="text">
                                </div>
                            </div>
                        </div>
                        <div>
                            <button id="sub_c"  type="submit" name="action">Submit
                                <i class="material-icons right">send</i>
                            </button>
                        </div>

                    </form>

                </div>
            </div>
            <div class="formrow" id="formrow">
                <form class="col s12">
                    <div class="row">
                        <div class="input-field col s12">
                            <input id="sem" type="number" class="validate">
                            <label for="sem" data-error="Invalid" data-success="right">Semester</label>
                        </div>
                    </div>
                </form>
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
        $('td').click(function () {
            var col = $(this).parent().children().index($(this));
            var row = $(this).parent().parent().children().index($(this).parent());
            var sem= $('table tr').eq(row).find('td').eq(0).html();
            console.log('Row: ' + row + ', Column: ' + col);
            console.log('Semester : ' + $('table tr').eq(row).find('td').eq(0).html());
            var lastChar = app_id.substr(app_id.length - 1);
            $.ajax({
                type: "POST",
                url: "Home",
                contentType: 'application/json; charset=UTF-8',
                processData: false,
                data :JSON.stringify({ column: col, semester: sem,appraisal:lastChar }),
                success: function(msg){
                    $('table tr').eq(row).find('td').eq(3).html(msg.date);
                    if(msg.redirect)
                        window.location.href=msg.redirect;
                },
                error: function(reason){
                    alert("failure :"+reason);
                }
            });
        });
    });
</script>
<script>
    function addsem()
    {
        alert(app_id);
        $('.formrow').show();

    }

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
        $("li#li1").cloneWithProperties({ id: "li"+lastidnum  }).insertAfter($('li#'+lastid));
        $("li#li1").removeClass("active");
    }
</script>
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
            $(function() {

                $("input:file").change(function () {
                    var fileName = $(this).val();
                    if (fileName === null)
                        alert("Please attach a file");
                    else
                        $('#sub').removeClass("disabled");
                    $('#sub_c').removeClass("disabled");
                    console.log(fileName);


                });
            });
</script>

</body>


</html>

