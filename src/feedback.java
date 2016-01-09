import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Bharath on 11/13/2015.
 */
public class feedback extends HttpServlet {
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
        try{

            connection=getConnection();

            StringBuffer jb = new StringBuffer();
            String line = null;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            Object _feedback= JSONValue.parse(jb.toString());
            JSONObject feedback=(JSONObject)_feedback;


            String comments= (String) feedback.get("comments");
            JSONObject current= (JSONObject) feedback.get("curTeacher");
            int app= Integer.parseInt((String) current.get("app"));
connection.setAutoCommit(false);
System.out.println("comments "+comments+"app :"+app);
            JSONArray questions= (JSONArray) current.get("questions");
            String login_param=(String)current.get("usn");
  String usn= "";
            String admin_no="";
long given_appraisal=Long.parseLong((String) feedback.get("givenAppraisal"));
given_appraisal++; //As given appraisal is updated in front end only after response from here

  String admin_usn="SELECT `admin_no`,`usn` FROM student_details WHERE `usn`= ? OR `admin_no`= ? ";
            PreparedStatement psadmin=connection.prepareStatement(admin_usn);
            psadmin.setString(1,login_param);
            psadmin.setString(2,login_param);
            ResultSet rsadmin=psadmin.executeQuery();
            while(rsadmin.next())
            {
                admin_no=rsadmin.getString(1);
                usn=rsadmin.getString(2);

            }

            for(int i=0;i<questions.size();i++)
            {
                JSONObject question= (JSONObject) questions.get(i);
                String q_id= (String) question.get("q_id");
                int answer=Integer.parseInt((String)question.get("answer"));
                System.out.println("q_id :"+q_id+" answer :"+answer);
                String update="";
                if(answer== 10)

                {
                   update="UPDATE `appraisal_value` SET `excellent`= `excellent`+1 WHERE `appraisal_id`= ? and `question_id`= ?";
                }
                if (answer== 8 )
                {
                    update="UPDATE `appraisal_value` SET `very_good`=`very_good`+1 WHERE `appraisal_id`= ? and `question_id`= ?";
                }

                if(answer== 6)
                {
                    update="UPDATE `appraisal_value` SET `good`=`good`+1 WHERE `appraisal_id`= ? and `question_id`= ?";
                }
                if (answer== 4 )
                {
                    update="UPDATE `appraisal_value` SET `poor`=`poor`+1 WHERE `appraisal_id`= ? and `question_id`= ?";
                }
                PreparedStatement ps=connection.prepareStatement(update);
                ps.setInt(1,app);
                ps.setString(2,q_id);
                ps.executeUpdate();

            }

            Statement s=connection.createStatement();
            String max="SELECT MAX(`appraisal_comment_id`) FROM appraisal_comment";
            ResultSet rsmax=s.executeQuery(max);
            while(rsmax.next()) {
                int appraisal_comment_id=rsmax.getInt(1);

                String comment_query = "INSERT INTO `appraisal_comment`(`appraisal_comment_id`, `appraisal_id`, `comment`) VALUES (?,?,?)";
                PreparedStatement comment=connection.prepareStatement(comment_query);
                comment.setInt(1,appraisal_comment_id+1);
                comment.setInt(2,app);
                if(comments!=null) {
                    if(!comments.replaceAll("\\s","").equals("")) {
                        comment.setString(3, comments);
                        comment.executeUpdate();
                    }
                }
                String complete = "UPDATE `appraisal_complete` SET `date_complete`=?,`complete`=1 WHERE `appraisal_id`= ? AND `admin_no`= ? AND `usn`= ?";
                PreparedStatement pscomplete=connection.prepareStatement(complete);
                pscomplete.setString(3,admin_no);
                pscomplete.setString(4,usn);
                java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                pscomplete.setTimestamp(1,date);
                pscomplete.setInt(2,app);
                pscomplete.executeUpdate();
            }


//long totalappraisal=4;
//            if(given_appraisal==totalappraisal)
//            {
//                String appraisal_update="UPDATE appraisal SET students_given=students_given+1 WHERE appraisal_id = ?";
//                PreparedStatement ps=connection.prepareStatement(appraisal_update);
//                ps.setInt(1,app);
//                ps.executeUpdate();
//
//            }
connection.commit();
//            JSONObject res=new JSONObject();
//            res.put("data",true);
            response.setContentType("application/json");
            response.getWriter().write("true");

        }catch (Exception e){
            e.printStackTrace();
            try {
                if(connection!=null)
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            if(null!=connection)
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
