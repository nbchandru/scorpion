import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bharath on 11/14/2015.
 */
public class loginServlet extends HttpServlet {

    private String encodedURL=null;
    private int x=0;
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
            JSONObject res = new JSONObject();
            String username = request.getParameter("Username");
            String password = request.getParameter("password");
            String user_query = "SELECT a_id,password,department FROM admin_details WHERE a_id = ?";
            PreparedStatement psuser =connection.prepareStatement(user_query) ;
            psuser.setString(1,username);
            ResultSet rsuser=psuser.executeQuery();

            if(!rsuser.first())
            {

                res.put("err", true);
                res.put("msg", "Admin username not found !!");
            }
            rsuser.beforeFirst();
            while (rsuser.next())
            {
                String pass=rsuser.getString(2);
                String department=rsuser.getString(3);
                if(!password.equals(pass))
                {
                    res.put("err", true);
                    res.put("msg", "Incorrect password entered!!");
                }
                else
                {
                    //create session and store admin id and department

                    x++;
                    HttpSession session = request.getSession();
                    session.setAttribute("a_id", username);
                    session.setAttribute("department",department);
                    session.setMaxInactiveInterval(30*60);
                    Cookie userName = new Cookie("user", username);
                    response.addCookie(userName);
                    if(x==1)
                        encodedURL = "path.jsp";
                    else
                        encodedURL = "Home";
res.put("redirect",encodedURL);
                }

            }

            System.out.println(res.toJSONString());
            response.setContentType("application/json");
            response.getWriter().write(res.toJSONString());

        }catch (Exception e){

            e.printStackTrace();
        }finally {
            try {
                if(connection!=null)
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
