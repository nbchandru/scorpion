import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by Bharath on 11/24/2015.
 */
public class querybox extends HttpServlet {
    DataSource datasource;
    public void init(ServletConfig config) throws ServletException {
        try {
            Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
            datasource = (DataSource) envCtx.lookup("jdbc/omni2");
        }
        catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection connection=null;
                try {
                    connection=getConnection();
                    response.setContentType("text/html");
                    PrintWriter out=response.getWriter();
                    String query = request.getParameter("query");
                    Statement s=connection.createStatement();
                    ResultSet rs=s.executeQuery(query);
                    ResultSetMetaData rd=rs.getMetaData();
                    int x=rd.getColumnCount();
                    out.println("<table>");
                    out.println("<tr>");
                    HashMap<Integer,Integer> map=new HashMap<>();
                    for(int i=1;i<=x;i++) {
                        out.println("<th>" + rd.getColumnName(i) + "</th>");
                    map.put(i,rd.getColumnType(i));
                    }
                        out.println("</tr>");
                    while (rs.next())
                    {
                        out.println("<tr>");
                       for(int j=1;j<=x;j++) {
                        if(map.get(j)==4)
                            out.println("<td>" + rs.getInt(j) + "</td>");
                        else if(map.get(j)==12)
                            out.println("<td>" + rs.getString(j) + "</td>");
                        else if(map.get(j)==91)
                            out.println("<td>" + rs.getDate(j) + "</td>");
                           else
                            out.println("<td>" + rs.getInt(j) + "</td>");

                       }
                            out.println("</tr>");

                    }
                    out.println("</table>");

                }catch (Exception e)
                {e.printStackTrace();}


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }
}
