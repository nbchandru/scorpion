import POJOs.appraisal;
import POJOs.single_appraisal;
import POJOs.teacher_details;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by Bharath on 11/11/2015.
 */
public class JSONtest extends HttpServlet {

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

        Connection connection = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omni2", "root", "root");
            connection=getConnection();
            System.out.println("hello");
            StringBuffer jb = new StringBuffer();
            String line = null;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            Object usn_obj= JSONValue.parse(jb.toString());
            JSONObject usn_json=(JSONObject)usn_obj;
             String usn=(String)usn_json.get("usn");
            System.out.println(usn);
//
//            Gson gson = new Gson();
//            single_appraisal s = gson.fromJson(jb.toString(), single_appraisal.class);
//            String usn=s.getUsn();

            //start query logic
//
//

//            JSONObject res=new JSONObject();
//            JSONArray  id=new JSONArray();
//            id.add("1");
//            System.out.println(id.get(0));
//            res.put("appraisal_ids",id);
//            res.put("appraisal_count",1);
//            JSONObject tab=new JSONObject();
//            tab.put("type","t");
//            tab.put("app","appraisal");
//            tab.put("id","cs129");
//            tab.put("usn","1rv13cs037");
//            tab.put("subName","intellectual property rights & entrepreneurship");
//            tab.put("subShortName","ipr");
//            tab.put("subCode","12hsi51");
//            JSONObject teacher_detail=new JSONObject();
//            teacher_detail.put("first_name","shishira");
//            teacher_detail.put("middle_name","s");
//            teacher_detail.put("last_name",null);
//            teacher_detail.put("staff_initial","ssl");
//            tab.put("details",teacher_detail);
//            res.put("1",tab);



JSONObject res=new JSONObject();
ArrayList<Integer> appraisal_ids=new ArrayList<>();
String rsud="SELECT `admin_no`, `usn`, `first_name`, `middle_name`, `last_name`, `department`, `sem`, `section`,`gender` FROM `student_details` WHERE `usn`= ? or `admin_no`= ? "; //and  `password`= request.getparameter("password"); for session
PreparedStatement ststud=connection.prepareStatement(rsud);
ststud.setString(1,usn);
ststud.setString(2,usn);
ResultSet rsstud=ststud.executeQuery();
if(!rsstud.first())
{
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    res.put("err","Invalid usn entered");
    System.out.println("Invalid usn entered");
}
rsstud.beforeFirst();
while(rsstud.next())
{
    String admin_no=rsstud.getString(1);
    String first_name=rsstud.getString(3);
    String second_name=rsstud.getString(4);
    String last_name=rsstud.getString(5);
    String department=rsstud.getString(6);
    String sem=rsstud.getString(7);
    String lab_batch=rsstud.getString(8);
    String gender=rsstud.getString(9);
    String ena="SELECT `date_enabled`, `date_disabled`, `appraisal_number`, `type` FROM `enable_table` WHERE  `department`= ? and  `appraisal_enabled`=1 and `sem`= ?";
    PreparedStatement stena=connection.prepareStatement(ena);
    stena.setString(1,department);
    stena.setString(2,sem);
    ResultSet rsena=stena.executeQuery();

    if(!rsena.first())
    {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        res.put("err","Appraisal not enabled for your semester");
        System.out.println("Appraisal not enabled");
    }
    rsena.beforeFirst();
    while(rsena.next())
    {
        int appraisal_number=rsena.getInt(3);
        res.put("appraisal_count",appraisal_number);
        String date_enabled=rsena.getString(1);
        String type=rsena.getString(4);
        String rescomp="SELECT `appraisal_id` FROM `appraisal_complete` WHERE (`admin_no`= ? or  `usn`= ?) and `complete`=0";
    PreparedStatement stcomp=connection.prepareStatement(rescomp);
    stcomp.setString(1,admin_no);
    stcomp.setString(2,usn);
    ResultSet rscomp=stcomp.executeQuery();

    if(!rscomp.first())
    {  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        res.put("err","Step Aside.............You have completed your appraisal");
        System.out.println("Step Aside..you have completed appraisal");
    }
    rscomp.beforeFirst();
    while(rscomp.next())    //For every appraisal to be given for student
    {
        JSONObject tab=new JSONObject();

        int appraisal_id=rscomp.getInt(1);

        tab.put("app",String.valueOf(appraisal_id));

        appraisal_ids.add(appraisal_id);

        // Get session id from appraisal

        String app="SELECT `session_id` FROM `appraisal` WHERE `appraisal_id`= ?";
            PreparedStatement psapp=connection.prepareStatement(app);
            psapp.setInt(1,appraisal_id);
            ResultSet rsapp=psapp.executeQuery();
            while(rsapp.next())
            {   int session_id=rsapp.getInt(1);

                String ses="SELECT `class_id`,`teacher_id`  FROM `session` WHERE `active`=1 and `session_id` = ?";
                PreparedStatement psses=connection.prepareStatement(ses);
                psses.setInt(1,session_id);
                ResultSet rsses=psses.executeQuery();
                while(rsses.next())
                {
                    int class_id=rsses.getInt(1);
                    String teacher_id=rsses.getString(2);

                    tab.put("id",teacher_id);


                    JSONObject teacher_detail=new JSONObject();
                    String tea="SELECT `first_name`, `middle_name`, `last_name`, `staff_initial`, `designation`,`department`, `picture_path` FROM `teacher_details` WHERE `teacher_id`= ?";
                    PreparedStatement pstea=connection.prepareStatement(tea);
                    pstea.setString(1,teacher_id);
                    ResultSet rstea=pstea.executeQuery();
                    while(rstea.next())
                    {
                        String first_name_teacher=rstea.getString(1);
                        String middle_name_teacher=rstea.getString(2);
                        String last_name_teacher=rstea.getString(3);
                        String staff_initials=rstea.getString(4);
                        teacher_detail.put("first_name",first_name_teacher);
                        teacher_detail.put("middle_name",middle_name_teacher);
                        teacher_detail.put("last_name",last_name_teacher);
                        teacher_detail.put("staff_initial",staff_initials);
                    }
                   String rscla = "SELECT `subject_code`, `subject_type`,`group` FROM `class` WHERE `class_id`= ? ";
                    PreparedStatement pscla=connection.prepareStatement(rscla);
                    pscla.setInt(1,class_id);
                    ResultSet rs_cla=pscla.executeQuery();
                    while (rs_cla.next())
                    {
                        String subject_code=rs_cla.getString(1);
                        String subject_type_class=rs_cla.getString(2);
                        tab.put("subCode",subject_code);

                        String group=rs_cla.getString(3);
                        String sub="SELECT  `subject_name`, `subject_name_short`, `subject_type`, `sem` FROM `subject_details` WHERE `subject_code` = ? ";
                        PreparedStatement pssub=connection.prepareStatement(sub);
                        pssub.setString(1,subject_code);
                        ResultSet rssub=pssub.executeQuery();
                        while(rssub.next())
                        {
                            String subject_name=rssub.getString(1);
                            String subject_name_short=rssub.getString(2);
//                            String subject_type=rssub.getString(3);
                            if(subject_type_class.equals("e")|subject_type_class.equals("t")) {
                                tab.put("type", "t");
                                tab.put("subName", subject_name);
                            }
                            else
                            {
                                tab.put("subName", subject_name+" lab");
                                tab.put("type", "l");

                            }
                                tab.put("subShortName",subject_name_short);

                        } //end of subject details

                    } //end of class
                    tab.put("details",teacher_detail);
                    tab.put("usn",usn);
                } //end of session

            } //end of appraisal
      res.put(appraisal_id,tab);
    } //end of appraisal_complete

        JSONArray id = new JSONArray();
        for(int i=0;i<appraisal_ids.size();i++)
        {
         id.add(appraisal_ids.get(i));
        }
        res.put("appraisal_ids",id);

    } //end of enable table

} //end of student_details



        System.out.println(res.toJSONString());
            response.setContentType("application/json");
            response.getWriter().write(res.toJSONString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
