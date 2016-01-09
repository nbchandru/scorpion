import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Bharath on 11/15/2015.
 */
public class Home extends HttpServlet {
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
            boolean update = false;
            HttpSession session = request.getSession();
            String a_id = (String) session.getAttribute("a_id");
            String department = (String) session.getAttribute("department");
            connection=getConnection();

                StringBuffer jb = new StringBuffer();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }

            if(jb.toString().equals("")|jb.toString()==null)
                update=false;
            else
            update=true;

            if(update) {
                System.out.println(jb.toString());

                Object vals = JSONValue.parse(jb.toString());
                JSONObject enable = (JSONObject) vals;
boolean form=(boolean)enable.get("form");
                JSONObject res = new JSONObject();
                if(form==false) {
                    long col = (long) enable.get("column");
                    int sem = Integer.parseInt((String) enable.get("semester"));
                    int app = Integer.parseInt((String) enable.get("appraisal"));
                    System.out.println("Column :" + col + " Semester :" + sem + " app :" + app);
                    String update_enable = "";
                    if (col == 5) {
                        update_enable = "UPDATE enable_table SET date_enabled =? ,appraisal_enabled=1 WHERE appraisal_number = ? AND sem = ?";
                    } else if (col == 6) {
                        update_enable = "UPDATE enable_table SET date_disabled =? ,appraisal_enabled=0 WHERE appraisal_number = ? AND sem = ?";

                    }
                    PreparedStatement psena = connection.prepareStatement(update_enable);
                    java.util.Date date = new java.util.Date();
                    Timestamp t = new Timestamp(date.getTime());
                    psena.setInt(2, app);
                    psena.setString(3, String.valueOf(sem));
                    psena.setTimestamp(1, t);
                    psena.executeUpdate();
                    String d = t.toString();
                    res.put("date", d);
                }
                else
                {
                    int sem = Integer.parseInt((String) enable.get("semester"));
                    int app = Integer.parseInt((String) enable.get("appraisal"));
String update_enable = "INSERT INTO enable_table(date_enabled,sem,appraisal_number,department,appraisal_enabled) VALUES (?,?,?,?,?)";

                    PreparedStatement psena = connection.prepareStatement(update_enable);
                    java.util.Date date = new java.util.Date();
                    Timestamp t = new Timestamp(date.getTime());
                    psena.setShort(5, (short) 1);
                    psena.setString(4, department);
                    psena.setInt(3, app);
                    psena.setString(2, String.valueOf(sem));
                    psena.setTimestamp(1, t);
                    psena.executeUpdate();
                    String d = t.toString();
                    res.put("date", d);
                }
                response.setContentType("application/json");
                response.getWriter().write(res.toJSONString());
            }
            else {
                MultiMap<Integer, MultiMap<Integer, String>> appraisals = new MultiValueMap<>();


                String apps = "SELECT DISTINCT appraisal_number FROM enable_table WHERE department = ?";
                PreparedStatement stapp = connection.prepareStatement(apps);
                stapp.setString(1, department);
                ResultSet rsapp = stapp.executeQuery();
                int i = 0;
                while (rsapp.next()) {
                    int appraisal_number = rsapp.getInt(1);
                    MultiMap<Integer, String> activeappraisal = new MultiValueMap<>();
                    String enable_table = "SELECT sem,date_enabled,date_disabled FROM enable_table WHERE sem>=1 AND department = ? AND appraisal_number= ?";
                    PreparedStatement stena = connection.prepareStatement(enable_table);
                    stena.setString(1, department);
                    stena.setInt(2, appraisal_number);
                    ResultSet rsena = stena.executeQuery();
                    int j = 0;
                    while (rsena.next()) {
                        int given = 0;
                        int strength = 0;
                        String semester = rsena.getString(1);
                        String date_enabled = rsena.getString(2);
                        String date_disabled = rsena.getString(3);

                        String givens = "SELECT COUNT( distinct usn) FROM appraisal_complete WHERE appraisal_complete.usn in (select usn from student_details where sem =? AND department = ?)";
                        PreparedStatement psgivens = connection.prepareStatement(givens);
                        psgivens.setString(1, semester);
                        psgivens.setString(2, department);
                        ResultSet rsgivens = psgivens.executeQuery();
                        while (rsgivens.next()) {
                            strength = rsgivens.getInt(1);
                        }

                        String givenq = "SELECT COUNT( distinct usn) FROM appraisal_complete WHERE complete=1 and appraisal_complete.usn in (select usn from student_details where sem =? and department = ?)";
                        PreparedStatement psgiven = connection.prepareStatement(givenq);
//                        psgiven.setInt(1, appraisal_number);
                        psgiven.setString(1, semester);
                        psgiven.setString(2, department);

                        ResultSet rsgiven = psgiven.executeQuery();
                        while (rsgiven.next()) {
                            given = rsgiven.getInt(1);
                        }

                        activeappraisal.put(j, semester);
                        activeappraisal.put(j, strength);
                        activeappraisal.put(j, given);
                        if (date_enabled == null)
                            date_enabled = "-";
                        if (date_disabled == null)
                            date_disabled = "-";
                        activeappraisal.put(j, date_enabled);
                        activeappraisal.put(j, date_disabled);
                        j++;
                    }
                    appraisals.put(i, activeappraisal);
                    i++;
                }

                session.setAttribute("appraisals", appraisals);
                RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                rd.forward(request, response);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(connection!=null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }
}
