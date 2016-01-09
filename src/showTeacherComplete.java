import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Bharath on 11/16/2015.
 */
public class showTeacherComplete extends HttpServlet {
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


        try {
            Connection con = getConnection();

            response.setContentType("text/html");
            String teacher_details = "SELECT teacher_id,first_name,middle_name,last_name,email_id,contact_number FROM teacher_details WHERE department = ?";
            PreparedStatement stmt1 = con.prepareStatement(teacher_details);

            HttpSession session = request.getSession();
            String a_id = (String) session.getAttribute("a_id");
            String department = (String) session.getAttribute("department");
            stmt1.setString(1,department);
            MultiMap<Integer,String> rows=new MultiValueMap<>();
            ResultSet rs1 = stmt1.executeQuery();
            int i=0;
            while(rs1.next()) {
                String complete = null;
                String name = null;
                String id = rs1.getString("teacher_id");
                String name1 = rs1.getString("first_name");
                String name2 = rs1.getString("middle_name");
//                int error_flag = rs1.getInt("error_flag");
//                String error_comments = rs1.getString("error_comments");
                String name3 = rs1.getString("last_name");

//                if (error_comments == null)
//                    error_comments = "-";

                if (name1 == null)
                    name1 = "";
                if (name2 == null)
                    name2 = "";
                if (name3 == null)
                    name3 = "";
                name1 = Character.toUpperCase(name1.charAt(0)) + name1.substring(1);
                name = name1 + " " + name2 + " " + name3;
                String email = rs1.getString("email_id");
                if (email == null)
                    email = "-";
                String contact = rs1.getString("contact_number");
                if (contact == null)
                    contact = "-";
                rows.put(i,id);
                rows.put(i,name);
                rows.put(i,email);
                rows.put(i,contact);
                i++;
            }
           session.setAttribute("rows",rows);
            RequestDispatcher rd=request.getRequestDispatcher("teacher_status.jsp");
            rd.forward(request,response);

        }catch (Exception e)
        {

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }
}
