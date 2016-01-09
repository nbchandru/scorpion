<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%--
  Created by IntelliJ IDEA.
  User: Bharath
  Date: 10/24/2015
  Time: 10:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%
  ArrayList<ArrayList<ArrayList<String>>> all_tables= (ArrayList<ArrayList<ArrayList<String>>>) request.getAttribute("all_tables");
  //ArrayList<ArrayList<Integer>> valid_rows=(ArrayList<ArrayList<Integer>>) request.getAttribute("valid_rows");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<table id="x" >
<%
  Iterator<ArrayList<ArrayList<String>>> sheet_index= all_tables.iterator();

  while(sheet_index.hasNext())
  {    int gg=0;
  ArrayList<ArrayList<String>> table =sheet_index.next();
    Iterator<ArrayList<String>> table_iterator = table.iterator();
      while(table_iterator.hasNext())
      {
      out.println("<tr>");
      gg++;
      ArrayList<String> column_values = table_iterator.next();
        Iterator<String> i=column_values.iterator();
        int z=0;
          while(i.hasNext())
          {
           z++;
          out.print("<td>"+i.next()+"</td>");
          }
//        out.print("<br>"+z+"<br>");
          out.println("</tr>");
      }
//          out.println("Number of rows in arraylist = " + gg);
//
//          out.println("End of sheet");
//          out.println("----------------------------------------------------");
  }
//          out.println("Value of column = "+all_tables.get(1).get(6).get(3));

%>
</table>
</body>
</html>
