import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Bharath on 11/14/2015.
 */
public class showComplete extends HttpServlet {
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
            System.out.println("Entered showComplete");
            TimeUnit.SECONDS.sleep(2);
            HttpSession session = request.getSession();
            String a_id = (String) session.getAttribute("a_id");
            String department = (String) session.getAttribute("department");
            System.out.println(department);
            JSONObject res = new JSONObject();
            res.put("redirect", "student_status.jsp");
            response.setContentType("application/json");
            MultiMap<Integer,String> map=new MultiValueMap<>();

            String students="SELECT first_name,middle_name,last_name,usn FROM student_details WHERE department = ?";
            PreparedStatement psstud=connection.prepareStatement(students);
            psstud.setString(1,department);
            ResultSet rsstud=psstud.executeQuery();
            int i=0;
            while(rsstud.next())
            {
               String first_name=rsstud.getString(1);
                String middle_name=rsstud.getString(2);
                String last_name=rsstud.getString(3);
                String usn=rsstud.getString(4);

                if(middle_name==null)
                    middle_name="";
                if(last_name==null)
                    last_name="";
              map.put(i,first_name+" "+middle_name+" "+last_name);
              map.put(i,usn);

              String appraisal_complete="SELECT COUNT(complete),date_complete FROM appraisal_complete WHERE usn =? and complete=1";
                PreparedStatement psapp=connection.prepareStatement(appraisal_complete);
                psapp.setString(1,usn);
                ResultSet rsapp=psapp.executeQuery();
                int total_given = 0;
                int to_be_given=0;
                String date_complete="";
                while(rsapp.next())
                {
                total_given=rsapp.getInt(1);
                date_complete=rsapp.getString(2);
                }
                String appraisal_complete_2="SELECT COUNT(complete) FROM appraisal_complete WHERE usn =? ";
                PreparedStatement psapp_2=connection.prepareStatement(appraisal_complete_2);
                psapp_2.setString(1,usn);
                ResultSet rsapp_2=psapp_2.executeQuery();
                while(rsapp_2.next())
                {
                    to_be_given=rsapp_2.getInt(1);
                }

                if((to_be_given==total_given)&&to_be_given!=0)
                    map.put(i,1);
                else
                map.put(i,0);

                map.put(i,total_given);
                map.put(i,to_be_given);
                if(date_complete==null)
                    date_complete="-";
                map.put(i,date_complete);
                i++;
            }
            session.setAttribute("rows",map);

            response.getWriter().write(res.toJSONString());

        }catch (Exception e){e.printStackTrace();}finally {
            if(connection!=null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
