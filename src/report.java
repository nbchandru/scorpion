import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.export.*;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class report extends HttpServlet
{
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

    private int ct=0;
    private String s3,s4,vtid,appraisal_name;
    private ResultSet rs3;
    private int n;
    private int appraisal,appraisal_count;
    private double total;
    private String comments;
    private double[] sum=new double[16];
    private double[] per=new double[16];
    private double tot;
    private List<InputStream> pdfs = new ArrayList<InputStream>();
    private Integer reportsem;

    private static String JDBC_CONNECTION_URL ="jdbc:mysql://localhost:3306/omni2?zeroDateTimeBehavior=convertToNull";
    private double totval;
    private double percentage;
    private static String username=null,password=null;
    private static String jrxmlPath=null,reportPath=null;

    void deleteDir(File dir)
    {
        File[] currList;
        Stack<File> stack = new Stack<File>();
        stack.push(dir);
        while (! stack.isEmpty()) {
            if (stack.lastElement().isDirectory()) {
                currList = stack.lastElement().listFiles();
                if (currList.length > 0) {
                    for (File curr: currList) {
                        stack.push(curr);
                    }
                } else {
                    stack.pop().delete();
                }
            } else {
                stack.pop().delete();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Arrays.fill(per,0.0);
//
//            System.out.println(getServletContext().getRealPath("/WEB-INF/omni.properties"));
//            InputStream inputStream=new FileInputStream(getServletContext().getRealPath("/WEB-INF/omni.properties"));
//            Properties properties=new Properties();
//            properties.load(inputStream);
//              jrxmlPath = properties.getProperty("jrxml");
//              reportPath = properties.getProperty("report");
              jrxmlPath="C:\\Users\\chandrasekar\\Desktop\\jrxml";
              reportPath="C:\\Users\\chandrasekar\\Desktop\\report";
        HttpSession session = request.getSession();
        String a_id = (String) session.getAttribute("a_id");
        String Department = (String) session.getAttribute("department");
                int len = reportPath.length();
                if (!(reportPath.charAt(len - 1) == '/'))
                    reportPath += "/";

                len = jrxmlPath.length();
                if (!(jrxmlPath.charAt(len - 1) == '/'))
                    jrxmlPath += "/";
//        response.setContentType("text/html");
//        java.io.PrintWriter out = response.getWriter( );
                Connection conn = null;
                JasperReport jasperReport, jasperReport1, jasperReport2;
                JasperPrint jasperPrint = null;
                JasperDesign jasperDesign, jasperDesign1, jasperDesign2;

            StringBuffer jb = new StringBuffer();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            Object vals = JSONValue.parse(jb.toString());
            JSONObject rep = (JSONObject) vals;
//            System.out.println(Integer.parseInt((String) rep.get("semester")));
//            System.out.println(Integer.parseInt((String) rep.get("appraisal")));
                try {

//                    Class.forName("com.mysql.jdbc.Driver");
//                    conn = DriverManager.getConnection(JDBC_CONNECTION_URL, username, password);
                    conn=getConnection();
//String appraisal_s ="SELECT appraisal_count FROM enable_table";
                    jasperDesign = JRXmlLoader.load(jrxmlPath + "TFINAL.jrxml");
                    jasperDesign1 = JRXmlLoader.load(jrxmlPath + "LFINAL.jrxml");
                    jasperDesign2 = JRXmlLoader.load(jrxmlPath + "SHORT.jrxml");
                    jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    jasperReport1 = JasperCompileManager.compileReport(jasperDesign1);
                    jasperReport2 = JasperCompileManager.compileReport(jasperDesign2);
                    Statement stmt1 = conn.createStatement();
                    Statement stmt2 = conn.createStatement();
                    Statement stmt3 = conn.createStatement();
                    Statement stmt4 = conn.createStatement();
                    Statement stmt5 = conn.createStatement();
                    Statement stmt6 = conn.createStatement();
                    Statement stmt7 = conn.createStatement();
                    Statement stmt8 = conn.createStatement();
                    Statement stmt9 = conn.createStatement();
                    Statement stmt = conn.createStatement();

                    Statement stmtapp = conn.createStatement();

                    Statement stmapp = conn.createStatement();
                    Statement stmdapp= conn.createStatement();

                    Statement stmtd= conn.createStatement();

                    Statement stmsub= conn.createStatement();

                    Statement stmtot= conn.createStatement();

                    Statement stmtotgiv= conn.createStatement();

                    Statement stmval= conn.createStatement();

                    Statement stmcomm= conn.createStatement();


                    ResultSet rstea = null;
                    ResultSet rsdep = null;
                    ResultSet rssub = null;
                    ResultSet rssem = null;
                    ResultSet rssec = null;
                    ResultSet rsapp = null;
                    ResultSet rsbat = null;
                    ResultSet rstdapp = null;
                    double tper[] = new double[50];
//                    String appno = request.getParameter("Appraisal_number");
//                    String Initials = request.getParameter("Initials");
//                    String Department = request.getParameter("Department");
//                    String Subject = request.getParameter("Subject");
//                    String Subject_type = request.getParameter("Subject_type");
//                    String Semester = request.getParameter("Semester");
//                    String Section = request.getParameter("Section");
//                    String Batch = request.getParameter("Batch");
//                    String Group = request.getParameter("Group");
                    String teacher = "";
                    String appno="1";
                    String Semester= String.valueOf(Integer.parseInt((String) rep.get("semester")));
                    if(Semester.equals("9"))
                        Semester="All";
                    String Initials="Haha";


                    System.out.println("input fronm front end : "+appno+" "+Initials+" "+Department+" "+  Semester);
                    int tsub = 0;
                    stmtapp = conn.createStatement();
                    String appup = "UPDATE `appraisal` SET `department`=(SELECT `department` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "`sem`=(SELECT `sem` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            " `section`=(SELECT `section` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "  `subject_code`=(SELECT `subject_code` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "  `subject_type`=(SELECT `subject_type` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "  `batch`=(SELECT `batch` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "  `group`=(SELECT `group` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                            "  `teacher_id`=(SELECT `teacher_id` FROM `session` WHERE `appraisal`.`session_id`=`session`.`session_id`)" +
                            " WHERE `department`IS NULL AND `sem` IS NULL AND `section`IS NULL AND `subject_code` IS NULL AND `subject_type` IS NULL AND `batch` IS NULL AND `group` IS NULL AND `teacher_id`IS NULL;";

                    stmtapp.executeUpdate(appup);

                    System.out.println("update done");
                    // check for condition all and for validation in the records
//                    if (Initials.equals("All"))
                    teacher = "in (SELECT `teacher_id`FROM `teacher_details` WHERE `department`='"+Department+"')";
//                    else {
//                        teacher = "(SELECT `teacher_id`FROM `teacher_details` WHERE `department`='mca' and `staff_initial`=" + Initials + ")";
//                        Statement ts1 = conn.createStatement();
//                        ResultSet t1 = stmt.executeQuery(teacher);
//                        if(!t1.next())
//                        {response.sendRedirect("index.jsp");}
//                    }
//                    if (Department.equals("All"))
//                        Department = "(SELECT `department` FROM `class` WHERE class_id= (select class_id from session where teacher_id = (SELECT teacher_id from teacher_details where department = 'mca')) or `department` is NULL";
//                    else {
//                        Department = "`" + Department + "`";
//                        Statement ts2 = conn.createStatement();
//                        ResultSet t2 = stmt.executeQuery("SELECT `department` FROM `appraisal` WHERE `department`="+Department+";" );
//                        if (!t2.next()) {
//                            response.sendRedirect("index.jsp");
//                        }
//                    }
//                    if (Subject.equals("All"))
//                        Subject = "(SELECT `subject_code` FROM `class` WHERE class_id= (select class_id from session where teacher_id = (SELECT teacher_id from teacher_details where department = 'mca'))";
//                    else {
//                        Subject = "`" + Subject + "`";
//                        Statement ts3 = conn.createStatement();
//                        ResultSet t3 = stmt.executeQuery("SELECT `subject_code` FROM `appraisal` WHERE `subject_code`="+Subject+";" );
//                        if (!t3.next()) {
//                            response.sendRedirect("index.jsp");
//                        }
//                    }
//                    if (Subject_type.equals("All"))
//                     Subject_type = "(SELECT `subject_type`  FROM `class` WHERE class_id= (select class_id from session where teacher_id = (SELECT teacher_id from teacher_details where department = 'mca'))";
//                    else {
//                        Subject_type = "`" + Subject_type + "`";
//                        Statement ts4 = conn.createStatement();
//                        ResultSet t4 = stmt.executeQuery("SELECT `subject_type` FROM `appraisal` WHERE `subject_type`="+Subject_type+";" );
//                        if (!t4.next()) {
//                            response.sendRedirect("index.jsp");
//                        }
//                    }

                    //   File dir = new File(reportPath+"mca");
                    //   deleteDir(dir);
                    //   dir.mkdirs();
                    //   reportPath=reportPath+"mca";

                    if (Semester.equals("All")) {
                        Semester = "in (SELECT distinct(`sem`)  FROM `class` WHERE class_id in (Select class_id from `session` where teacher_id in (SELECT teacher_id from teacher_details where department = '"+Department+"')))";
                        File dir = new File(reportPath+Department);
                        if(dir.exists())
                        deleteDir(dir);
                        dir.mkdirs();
                        reportPath=reportPath+Department+"/";
                        //deleteDir(dir);

                        // deleteDir(dir);

                    }
                    else {
                        String sem=Semester;
                        Semester = "=" + Semester + " ";
//                        Statement ts5 = conn.createStatement();
//                        ResultSet t5 = stmt.executeQuery("SELECT `sem` FROM `appraisal` WHERE `sem`="+Semester+";");
                        File dir = new File(reportPath+Department+"/"+"semester "+sem+"/");
                        if(dir.exists())
                            deleteDir(dir);
                        dir.mkdirs();

                        reportPath=reportPath+Department+"/"+"semester "+sem+"/";
                        //deleteDir(dir);
                        //  if (!t5.next()) {
                        //      response.sendRedirect("index.jsp");
                        //  }
                    }


                    //  String sqlappno = "SELECT `appraisal_id`, `session_id`, `appraisal_number`, `department`, `sem`, `section`, `subject_code`, `subject_type`, `batch`, `group`, `teacher_id` FROM `appraisal` WHERE `appraisal_number`='" + appno + "' and `department`=" + Department + " and  `sem` =" + Semester + " and `section`= " + Section + "and  `subject_code`=" + Subject + " and `subject_type`= " + Subject_type + "and `batch`= " + Batch + "and `group` = " + Group + "and  (`teacher_id` = " + teacher + ") ;";
                    // String sqltd = "SELECT distinct(`teacher_id`)FROM `appraisal` WHERE `appraisal_number`='" + appno + "' and `department`=" + Department + " and  `sem` =" + Semester + " and `section`= " + Section + "and  `subject_code`=" + Subject + " and `subject_type`= " + Subject_type + "and `batch`= " + Batch + "and `group` = " + Group + "and  (`teacher_id` = " + teacher + ") order by `taecher_id`;";
                    String sqlappno = "SELECT `appraisal_id`, `session_id`, `appraisal_number`, `department`, `sem`, `section`, `subject_code`, `subject_type`, `batch`, `group`, `teacher_id` FROM `appraisal` WHERE `appraisal_number`=" + appno + " and `department` = '"+Department+"'  and  `sem` " + Semester + " and `group` = 'teacher' and  `teacher_id` " + teacher + " order by `teacher_id`;";
                    String sqltd = "SELECT distinct(`teacher_id`)FROM `appraisal` WHERE `appraisal_number`=" + appno + " and `department`='"+Department+"' and  `sem` " + Semester + " and `group` = 'teacher' and  (`teacher_id` " + teacher + ") order  by `teacher_id`;";

                    System.out.println(sqltd);
                    System.out.println(sqlappno);

                    rsapp = stmapp.executeQuery(sqlappno);
                    rstdapp = stmdapp.executeQuery(sqltd);
                    if(!rsapp.next()||!rstdapp.next())
                    {     response.sendRedirect("index.jsp");
                    }
                    String[] appraisal_id = new String[100];
                    String[] session_id = new String[100];
                    String[] appraisal_number = new String[100];
                    String[] department = new String[100];
                    String[] sem = new String[100];
                    String[] section = new String[100];
                    String[] subject_code = new String[100];
                    String[] subject_type = new String[100];
                    String[] batch = new String[100];
                    String[] group = new String[100];
                    String[] teacher_id = new String[100];
                    String[] designation = new String[100];
                    String[] name = new String[100];
                    String [] init= new String[100];
                    String [] teadep= new String[100];
                    int[] totgi = new int[100];
                    int[] totalstu = new int[100];

                    int i = 0;
                    int j = 0;
                    rstdapp.beforeFirst();
                    while (rstdapp.next()) {
                        i++;
                        teacher_id[i] = rstdapp.getString("teacher_id");
                        if(rstdapp.getString("teacher_id").equals(null))
                            continue;
                        String s1 = "SELECT first_name,middle_name,last_name,department,designation,staff_initial FROM teacher_details WHERE teacher_id ='" + teacher_id[i] + "' and department ='"+Department+"';";

                        ResultSet td = stmtd.executeQuery(s1);
                        td.next();
                        String first = td.getString("first_name");
                        String middle = td.getString("middle_name");
                        String last = td.getString("last_name");
                        teadep[i] = td.getString("department");
                        designation[i] = td.getString("designation") == null ? "" : td.getString("designation");
                        init[i] = td.getString("staff_initial");
                        if (first == null)
                            first = "";
                        if (middle == null)
                            middle = "";
                        if (last == null)
                            last = "";

                        name[i] = first + " " + middle + " " + last + " (" + init[i] + ")";
                        File dir = new File(reportPath+ teadep[i].toUpperCase()+"_" +init[i].toUpperCase());
                        dir.mkdirs();


                        rsapp.beforeFirst();// goes to first resultset to check again
                        j=0;
                        while (rsapp.next()) {
                            String tea = rsapp.getString("teacher_id");
                            if (teacher_id[i].equals(tea)&& !rsapp.getString("sem").equals(null))  // if this condition is satisfied - teacher teaches this class - generate report for this appraisal
                            {
                                j++;
                                appraisal_id[j] = rsapp.getString("appraisal_id");
                                session_id[j] = rsapp.getString("session_id");
                                appraisal_number[j] = rsapp.getString("appraisal_number");
                                department[j] = rsapp.getString("department");
                                sem[j] = rsapp.getString("sem");
                                section[j] = rsapp.getString("section");
                                subject_code[j] = rsapp.getString("subject_code");
                                subject_type[j] = rsapp.getString("subject_type");
                                batch[j] = rsapp.getString("batch")==null?"":rsapp.getString("batch");

                                group[j] = rsapp.getString("group");

                                rsapp.getString("teacher_id");


                                System.out.println("teacher_count ===>" + i);
                                System.out.println("appraisal_count for current teacher ==>" + j);
                                System.out.println("appraisal_id: " + appraisal_id[j] + "session_id: " + session_id[j] + "appraisal_number: " + appraisal_number[j] + "department: " + department[j] + "sem: " + sem[j] + "section: " + section[j] + "subject_code: " + subject_code[j] + "subject_type: " + subject_type[j] + "batch: " + batch[j] + "group: " + group[j] + "teacher_id: " + teacher_id[j]);


                                int exe = 0;
                                int vgood = 0;
                                int good = 0;
                                int poor = 0;




                                String s2 = "SELECT  `subject_name`, `subject_name_short`, `subject_type` FROM `subject_details` WHERE `subject_code`='" + subject_code[j] + "'";
                                ResultSet sub = stmsub.executeQuery(s2);
                                sub.next();

                                String s3 = "SELECT count(`admin_no`) as total from `appraisal_complete` where `appraisal_id`= " + appraisal_id[j] + ";";
                                String s4 = "SELECT count(`admin_no`) as totalgiven from `appraisal_complete` where `appraisal_id`=" + appraisal_id[j] + " and `complete`=1 ;";


                                ResultSet tot = stmtot.executeQuery(s3);
                                ResultSet totgiv = stmtotgiv.executeQuery(s4);
                                tot.next();
                                totgiv.next();

                                int x;
                                Map parameters = new HashMap();
                                parameters.put("appno", appraisal_number[j]);
                                parameters.put("tid", teacher_id[i].toUpperCase());
                                parameters.put("name", first + " " + middle + " " + last);
                                parameters.put("desig", designation[i]);
                                parameters.put("subname", sub.getString("subject_name") + " (" + sub.getString("subject_name_short").toUpperCase() + ")");
                                parameters.put("section", section[i] == null ? "" : section[j]);
                                parameters.put("sem", sem[j] == null ? "" : sem[j]);
                                parameters.put("batch",batch[j]);
                                parameters.put("subject", subject_code[j].toUpperCase() + "_" + subject_type[j].toUpperCase());
                                parameters.put("totalstu", String.valueOf(tot.getInt("total")));

                                parameters.put("stugiv", String.valueOf(totgiv.getInt("totalgiven")));

                                totalstu[j] = tot.getInt("total");
                                totgi[j] = totgiv.getInt("totalgiven");
                                String s5 = "SELECT `excellent`, `very_good`, `good`, `poor` FROM `appraisal_value` WHERE `appraisal_id`='" + appraisal_id[j] + "';";
                                ResultSet val = stmval.executeQuery(s5);

                                String s6 = "SELECT  DISTINCT(`appraisal_id`),`comment` FROM `appraisal_comment` WHERE `appraisal_id`='" + appraisal_id[j] + "';";
                                //String s7="SELECT `question_id` , `question_text` FROM `appraisal_question` WHERE `appraisal_number`='"+appraisal_number[i]+"' and `department`='"++"' and `type`='"++"' and `group`='"++"''";

                                ResultSet comm = stmcomm.executeQuery(s6);
                                comments = "";
                                int cmct=0;
                                while (comm.next()) {cmct++;
                                    comments = comments + "\r\n" + String.valueOf(cmct)+"." + comm.getString("comment");
                                }
                                double perr=0.0;
                                if (subject_type[j].equals("t"))
                                    x = 10;
                                else
                                    x = 5;
                                for (int m = 1; m <= x && val.next(); m++) {

                                    exe = val.getInt("excellent");
                                    vgood = val.getInt("very_good");
                                    good = val.getInt("good");
                                    poor = val.getInt("poor");
                                    sum[m] = (exe * 10) + (vgood * 8) + (good * 6) + (poor * 4);
                                    total += sum[m];
                                    if (totgi[j]!=0)
                                    per[m] = (sum[m] / ( totgi[j] * 10))*100;
                                        else
                                        per[m]=0;
                                    perr+=per[m];


                                    // per[m]=sum[m]/(totgi[j]*10);
                                    parameters.put("exe" + m, String.valueOf(exe));
                                    parameters.put("vg" + m, String.valueOf(vgood));
                                    parameters.put("g" + m, String.valueOf(good));
                                    parameters.put("po" + m, String.valueOf(poor));
                                    parameters.put("qt" + m, new BigDecimal(sum[m]).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString());
                                    parameters.put("pct" + m,new BigDecimal(per[m]).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString());

                                }
                                perr = perr/(x);
                                parameters.put("total", new BigDecimal(total).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString());

                                if (totgi[j]!=0)
                                percentage = total / (totgi[j] * 10 * x) * 100;
                                else
                                    percentage =0;
                                            // parameters.put("percentage", String.valueOf(percentage));
                                parameters.put("percentage", new BigDecimal(perr).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString());
                                tper[j] = percentage;
                                System.out.println("abcdefg " + comments);
                                parameters.put("comment", comments);
                                //                            parameters.put("comment",p[1]);


                                //jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,conn);
                                if (x == 10) {
                                    appraisal_name= sub.getString("subject_name_short").toUpperCase()+"_" + parameters.get("section");
                                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                                    JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath +  teadep[i]+"_" +init[i]+ "/" + appraisal_name + ".pdf");
                                } else {
                                    appraisal_name= sub.getString("subject_name_short").toUpperCase()+"_" + parameters.get("section")+"_"+batch[j].toUpperCase();
                                    jasperPrint = JasperFillManager.fillReport(jasperReport1, parameters, new JREmptyDataSource());
                                    JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + teadep[i]+"_" +init[i]+ "/" + appraisal_name + ".pdf");
                                }//JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                                parameters.clear();

                                total = 0;
                                percentage = 0;
                                ct = 0;
                                totval = 0;
                                for (int q = 1; q <= n; q++)
                                    sum[q] = 0;
                            }//end of if

                        } //end of result set for each appraisal


                        // List<InputStream> pdfs = new ArrayList<InputStream>();
                        File folder = new File(reportPath + teadep[i]+"_" +init[i]);
                        File[] listOfFiles = folder.listFiles();
                        if (listOfFiles != null) {
                            //  if (listOfFiles.length > 1) {
                            for (File listOfFile : listOfFiles) {
                                if (listOfFile.isFile()) {

                                    System.out.println(listOfFile.getAbsolutePath());
                                    pdfs.add(new FileInputStream(listOfFile.getAbsolutePath()));
                                }
                            }


                            OutputStream output = new FileOutputStream(reportPath +  teadep[i]+"_" +init[i] + "/" + teadep[i]+"_" +init[i] + ".pdf");
                            PDFMerger.mergePDFs(pdfs, output, true);
                            pdfs.removeAll(pdfs);
                            Map parameters = new HashMap();

                            for (int r = 1; r <= j; r++) {
                                if (department[r] == null)
                                    department[r] = "-";
                                if (sem[r] == null)
                                    sem[r] = "-";
                                if (section[r] == null)
                                    section[r] = "-";
                                if (subject_code[r] == null)
                                    subject_code[r] = "-";
                                if (batch[r] == null)
                                    batch[r] = "-";
                                parameters.put("slno" + r,String.valueOf(r));
                                parameters.put("dep" + r, department[r]);
                                parameters.put("sem" + r, sem[r]);
                                parameters.put("sec" + r, section[r]);
                                parameters.put("sub" + r, subject_code[r]);
                                parameters.put("bat" + r, batch[r]);
                                parameters.put("per" + r, new BigDecimal(tper[r]).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString());

                                String app[] = new String[10];


                            }
                            if (j < 11) {
                                for (int r = j+1; r <= 11; r++) {
                                    parameters.put("slno" + r, "-");
                                    parameters.put("dep" + r, "-");
                                    parameters.put("sem" + r, "-");
                                    parameters.put("sec" + r, "-");
                                    parameters.put("sub" + r, "-");
                                    parameters.put("bat" + r, "-");
                                    parameters.put("per" + r, "-");

                                }
                            }
                            parameters.put("desig", td.getString("designation")==null?"-":td.getString("designation"));
                            parameters.put("tid", teacher_id[i]);
                            parameters.put("name", name[i]);
                            parameters.put("appno", appno);
//servletOutputStream.println("<br>"+vtid+"</br>");
                            jasperPrint = JasperFillManager.fillReport(jasperReport2, parameters, new JREmptyDataSource());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath +  teadep[i]+"_" +init[i] + "/Condensed Report.pdf");

//            break;
                            comments = "";
                        }
                        //      request.setAttribute("str", reportsem);
                        //   RequestDispatcher r = request.getRequestDispatcher("/reportConfirmed.jsp");
                        // r.forward(request, response);


                    }// end of 1 teacher itration

                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                } catch (JRException e) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    response.setContentType("text/plain");
                    response.getOutputStream().print(stringWriter.toString());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (Exception ignored) {
                        }
                    }
                }

    }

    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}