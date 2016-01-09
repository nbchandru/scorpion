import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.*;
import java.lang.*;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.SystemUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bharath on 11/16/2015.
 */
/*
*fix stuff on line 235 ... blank check
*
* */

public class classxls extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection=null;
        String curfile="";
        try {                HashSet<String> filedep= new HashSet<>();
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omni2", "root", "root");

            String q="SELECT field_name from input_map where input_id='class_details'";
            Statement s=connection.createStatement();
            ResultSet resultSet=s.executeQuery(q);
            ArrayList<MultiMap> a=new ArrayList<>();

            MultiMap<Integer,ArrayList<String>> valid_fields = new MultiValueMap();
            ArrayList<String> initials =new ArrayList<>();

/*
MultiMap test=new MultiValueMap();
test.put(1,"Yo");
test.put(1,"Yoyo");
test.put(2,"Youuu");

System.out.println(test.containsValue("Youuu"));
*/
            int j=0;
            while(resultSet.next()) {
                valid_fields.put(0,resultSet.getString(1));
            }
            if(valid_fields.containsValue("prog"))
                System.out.println("hooray");
            long startTime = System.nanoTime();
            int start=0 ,end=0;
            String teacher_origin="";
            boolean course_end_execute=true;
            HttpSession session=request.getSession();
            String department=(String)session.getAttribute("department");
//            if(session.getAttribute("user") == null){
//                response.sendRedirect("login.jsp");
//            }
//        String selectsem=request.getParameter("selectsem");
//        System.out.println("Even or odd ? ===========>"+selectsem);

////        correct.add("cse_4_sem.csv");
////        correct.add("cse_6_sem.csv");
//        correct.add("map_table.csv");
//        correct.add("student_details.csv");
//        correct.add("subject_name.csv");
//        correct.add("teacher_details.csv");
//        correct.add("question_map.csv");
//        correct.add("cse_2_sem.csv");
//        correct.add("cse_4_sem.csv");
//        correct.add("cse_6_sem.csv");
//
//        correct.add("enable_table.csv");
            response.setContentType("text/html");
            InputStream inputStream=new FileInputStream(getServletContext().getRealPath("/WEB-INF/omni.properties"));
            Properties properties=new Properties();
            properties.load(inputStream);
//        System.out.println("Success ! :"+properties.getProperty("name"));
            String filePath = properties.getProperty("csvupload");
            File f=new File(filePath);
            if (!f.exists())
                f.mkdir();

            System.out.println(filePath);
            int len= filePath.length();
            if(!(filePath.charAt(len-1)=='/'))
                filePath +="/";
            HashSet<String> received =new HashSet<String>();
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("Ismultipart ? "+ isMultipart);

            DiskFileItemFactory factory = new DiskFileItemFactory();
            int maxMemSize = 4 * 1024;
            factory.setSizeThreshold(maxMemSize);
            if(SystemUtils.IS_OS_WINDOWS) {
                File file=new File("C:\\temp");
                if(!file.exists())
                    file.mkdir();
                factory.setRepository(new File("c:\\temp"));
            }
            else
                factory.setRepository(new File("/tmp"));
            ServletFileUpload upload = new ServletFileUpload(factory);

            int maxFileSize = 50 * 1024 * 1024;
            upload.setSizeMax(maxFileSize);
            String fileName="";
            Set<String> files=new HashSet<>();
            if(isMultipart) {
                List fileItems = upload.parseRequest(request);
                System.out.println("Parsed request");

                Iterator i = fileItems.iterator();

                while (i.hasNext()) {
                    FileItem fi = (FileItem) i.next();
                    if (!fi.isFormField()) {
                        String fieldName = fi.getFieldName();
                        fileName = fi.getName();
                        String contentType = fi.getContentType();
                        boolean isInMemory = fi.isInMemory();
                        long sizeInBytes = fi.getSize();
                        File file;
                        if (fileName.lastIndexOf("\\") >= 0) {
                            file = new File(filePath +
                                    fileName.substring(fileName.lastIndexOf("\\")));
                        } else {
                            file = new File(filePath +
                                    fileName.substring(fileName.lastIndexOf("\\") + 1));
                        }
                        fi.write(file);

                        System.out.println("Uploaded Filename: "+fileName );
                        files.add(fileName);
                    }
                }
            }

            Iterator itf=files.iterator();
            int usn_read_ct=0;
            while (itf.hasNext()) {

                curfile= (String) itf.next();
//        FileInputStream file = new FileInputStream(new File("C:\\Dev\\omni 2.0 test xlsms\\12.xlsx"));
                FileInputStream file = new FileInputStream(new File(filePath +curfile));
//        FileInputStream file = new FileInputStream(new File("C:\\Dev\\omni 2.0 test xlsms\\official_files\\5th sem\\5th sem\\Sec A\\cse_vsem_12CS52.xlsx"));

                ArrayList<ArrayList<ArrayList<String>>> all_tables = new ArrayList<>();
                ArrayList<ArrayList<Integer>> valid_rows = new ArrayList<>();

                ArrayList<String> sheet_names = new ArrayList<>();

                //Get the workbook instance for XLS file
                XSSFWorkbook workbook = new XSSFWorkbook(file);

                //Get first sheet from the workbook
                int x = workbook.getNumberOfSheets();
                MultiMap rows = new MultiValueMap();
                for (int sheet_no = 0; sheet_no < x; sheet_no++) {
                    int autosize = 1;
                    XSSFSheet sheet = workbook.getSheetAt(sheet_no);
                    sheet_names.add(sheet.getSheetName());
                    System.out.println(sheet.getLastRowNum());
                    ArrayList<ArrayList<String>> table_data = new ArrayList<>();
                    //Iterate through each rows from first sheet

                    int rowStart = Math.min(15, sheet.getFirstRowNum());
                    int rowEnd = Math.max(1, sheet.getLastRowNum());
                    ArrayList<Integer> validrows = new ArrayList<>();
//                        int row_col=0;
                    int count = 0;
                    start = rowStart;
                    end = rowEnd;
                    for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
//                            row_col++;
                        int flag;
                        count = 0;
                        ArrayList<String> column_data = new ArrayList<>();
                        Row r = sheet.getRow(rowNum);
                        if (r == null) {
                            if (rowNum == rowEnd - 1)
                                break;
                            else
                                continue;
                        }
                        int lastColumn = Math.max(r.getLastCellNum(), 1);
                        int first = r.getFirstCellNum();
                        for (int cn = 0; cn < lastColumn; cn++) {
                            flag = 0;
                            Cell cell = r.getCell(cn, Row.RETURN_NULL_AND_BLANK);
                            count++;
                            if (cell == null) {
                                // if (cn <= first)
                                //    continue;
//                                if (cn > first)
                                column_data.add("");
                            } else {
                                if (autosize == 1)
                                    sheet.autoSizeColumn(cell.getColumnIndex());

                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        column_data.add(String.valueOf(cell.getBooleanCellValue()).toLowerCase());
                                        rows.put(rowNum, String.valueOf(cell.getBooleanCellValue()).toLowerCase());
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            column_data.add(new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue()).toLowerCase());
                                            rows.put(rowNum, new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue()).toLowerCase());
                                        } else {
                                            column_data.add(new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                            rows.put(rowNum, new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                        }
                                        break;

                                    case Cell.CELL_TYPE_STRING:
                                        column_data.add(cell.getStringCellValue().toLowerCase());
                                        rows.put(rowNum, cell.getStringCellValue().toLowerCase());
                                        break;
                                    case Cell.CELL_TYPE_BLANK:
                                        column_data.add("");
//                                        rows.put(rowNum,"");
                                        break;
                                    case Cell.CELL_TYPE_FORMULA:
                                        flag = 1;
                                        switch (cell.getCachedFormulaResultType()) {
                                            case Cell.CELL_TYPE_NUMERIC:
                                                column_data.add(new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                                rows.put(rowNum, new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                                break;
                                            case Cell.CELL_TYPE_STRING:
                                                column_data.add(cell.getStringCellValue().toLowerCase());
                                                rows.put(rowNum, cell.getStringCellValue().toLowerCase());
                                                break;
                                        }
                                        break;
                                    default:
                                        column_data.add("");
                                }
                                if (flag == 1) {
                                    validrows.add(r.getRowNum());
                                }
                            }
                        }
                        autosize = 0;

                        table_data.add(column_data);
                        a.add(rows);
                    }

//                        int z = i + 1;
                    System.out.println("Sheet " + sheet_no+1 + " complete");
                    System.out.println("Number of concerned rows " + validrows.size());
                    System.out.println("Rows are :");
                    Iterator<Integer> it = validrows.iterator();
                    while (it.hasNext()) {
                        System.out.print((it.next() + 1) + " ");
                    }
                    System.out.println("");
                    System.out.println("----------------------------------------------------------");
                    System.out.println("No of columns :" + count);
                    valid_rows.add(validrows);
                    all_tables.add(table_data);


                    for (ArrayList<ArrayList<String>> e : all_tables) {
                        for (ArrayList<String> fe : e) {
                            for (String g : fe) {
                                System.out.print(g + " ");

                            }

                            System.out.println(" ");

                        }

                    }

//    class details
// read origin department
// cocatinate ug/pg and check ug pg - with prog check against dept
// section check against
// copy the below from excel
                    String Originating_department = "cse_ug";
                    String subject_code = "";
                    String sem = "";
                    String section = "";

                    MultiMap theory_initials = new MultiValueMap();
                    MultiMap lab_initials = new MultiValueMap();
                    MultiMap usn = new MultiValueMap();
                    int flag = 0;
                    for (int i = start; i <= end; i++) {
                        ArrayList<String> e = (ArrayList<String>) rows.get(i);


                        if (e != null) {
                            if ( flag == 1) {

                                if(!(lab_initials.isEmpty())) {
                                    if (e.size() >= 4) {
                                        if (e.get(3).length()>50||e.get(3).contains("note")||e.get(3).contains("Note")||e.get(3).contains("NOTE")||e.get(3).contains("NOte"))// special note case
                                        {
                                            usn.put(e.get(2), e.get(0).replaceAll("[\\s_]", ""));}
                                        else {
                                            if(e.get(3).length() > 1&&e.get(3).length() < 50)
                                                usn.put(e.get(3), e.get(1).replaceAll("[\\s_]", ""));
                                            else
                                                usn.put(section + e.get(3), e.get(1).replaceAll("[\\s_]", "")); // needs to be changed .. found input with wrong batch .. only batch neber to be extracted
                                        }}
                                    else if (e.size() >= 3) { // without slno logic
                                        if (e.get(3).length() > 1)
                                        {
                                            usn.put(e.get(2), e.get(0).replaceAll("[\\s_]", ""));
                                        }
                                        else{
                                            usn.put(section + e.get(2), e.get(0).replaceAll("[\\s_]", "")); // needs to be changed .. found input with wrong batch .. only batch neber to be extracted
                                        }
                                    }
                                }// for lab logic

                                else if (!(theory_initials.isEmpty())) {
                                    if (e.size() >= 3) //theory
                                    { if (e.get(2).length()>100||e.get(2).contains("note")||e.get(2).contains("Note")||e.get(2).contains("NOTE")||e.get(2).contains("NOte"))
                                        usn.put(section, e.get(0).replaceAll("[\\s_]", ""));
                                    else
                                    {
                                        usn.put(section, e.get(1).replaceAll("[\\s_]", ""));
                                    }
                                    }
                                    else if (e.size() >= 2) //without slno logic
                                    {

                                        usn.put(section, e.get(0).replaceAll("[\\s_]", ""));
                                    }
                                }
                            }
                            //make logic more intelligent
                            if (e.contains("section:")) { // why check against section ? check with prog
                                department = e.get(3).replaceAll("\\s","") ;//+ "_" + e.get(1);

                                subject_code = e.get(e.size() - 1).replaceAll("\\s","");

                                section = e.get(7);

                            } else if (e.contains("Faculty"))
                                System.out.println(e);

                            //change all harcoded logic

                            int size = e.size();
                            for (String string : e) {
                                string = string.replaceAll("\\s", "");
//                string=string.replaceAll("\\s", "");
//                System.out.println(string);

                                if (string.contains("(theory)")) {
                                    String[] estring = new String[size];
                                    estring = e.toArray(estring);
                                    for (int z = 0; z < estring.length; z++)
                                        estring[z] = estring[z].replaceAll("\\s", "");
//                                    Pattern initials_pattern = Pattern.compile("(?<=\\()[^\\(.]+?(?=\\))|(?<=\\[)[^\\[.]+?(?=\\])");
                                    Pattern initials_pattern = Pattern.compile("(?<=\\()[^\\(.]+?(?=\\))|(?<=\\[)[^\\[.]+?(?=\\])|(?<=\\().*?(?=\\))");
                                    for (int k = 1; k < size; k++) {
//                            estring[k]= estring[k].replaceAll("\\s", "");
                                        if (estring[k].contains("+")) {
                                            String[] parts = estring[k].split("\\+");
                                            for (int p = 0; p < parts.length; p++) {
                                                Matcher initials_matcher = initials_pattern.matcher(parts[p]);
                                                if (initials_matcher.find())
                                                    theory_initials.put("theory", initials_matcher.group(0));
                                                else
                                                    theory_initials.put("theory", parts[p]);
                                            }
                                        } else if (estring[k].contains(",")) {
                                            String[] parts = estring[k].split("\\,");
                                            for (int p = 0; p < parts.length; p++) {
                                                Matcher initials_matcher = initials_pattern.matcher(parts[p]);
                                                if (initials_matcher.find())
                                                    theory_initials.put("theory", initials_matcher.group(0));
                                                else
                                                    theory_initials.put("theory", parts[p]);
                                            }
                                        } else {
                                            Matcher initials_matcher = initials_pattern.matcher(estring[k]);
                                            if (initials_matcher.find())
                                                theory_initials.put("theory", initials_matcher.group(0));
                                            else
                                                theory_initials.put("theory", estring[k]);

                                        }
                                    }
                                } else if (string.contains("(lab)")) {

                                    Pattern initials_pattern = Pattern.compile("(?<=\\()[^\\(.]+?(?=\\))|(?<=\\[)[^\\[.]+?(?=\\])");
                                    final Pattern batch_pattern = Pattern.compile("^[a-z]{1,3}[0-9]{1,2}|[0-9]{1,2}");
//                    int cnt=0;
//                    String next=null;

                                    String[] estring = new String[size];
                                    estring = e.toArray(estring);
                                    for (int z = 0; z < estring.length; z++)
                                        estring[z] = estring[z].replaceAll("\\s", "");
                                    for (int k = 1; k < size; k++) {
                                        String labs = estring[k];
//                        Matcher initials_matcher = initials_pattern.matcher(labs);
                                        Matcher batch_matcher = batch_pattern.matcher(labs);
                                        if (batch_matcher.find()) {
                                            if(section.length()>4||section.length()<1)
                                            section= String.valueOf(batch_matcher.group().charAt(0));

                                            if (estring.length > 2) {
                                                if (k + 1 <= estring.length-1) {
                                                    while (!batch_pattern.matcher(estring[k + 1]).find()) {
                                                        if (estring[k + 1].contains("+")) {
                                                            String[] parts = estring[k + 1].split("\\+");
                                                            for (int p = 0; p < parts.length; p++) {
                                                                Matcher initials_matcher = initials_pattern.matcher(parts[p]);
                                                                if (initials_matcher.find()) {
//                                   System.out.print(initials_pattern.matcher(estring[k + 1]).find() + " ");
                                                                    lab_initials.put(batch_matcher.group(), initials_matcher.group(0));
                                                                } else
                                                                    lab_initials.put(batch_matcher.group(), parts[p]);

                                                            }
                                                        } else if (estring[k + 1].contains(",")) {
                                                            String[] parts = estring[k + 1].split("\\,");
                                                            for (int p = 0; p < parts.length; p++) {
                                                                Matcher initials_matcher = initials_pattern.matcher(parts[p]);
                                                                if (initials_matcher.find()) {
//                                   System.out.print(initials_pattern.matcher(estring[k + 1]).find() + " ");
                                                                    lab_initials.put(batch_matcher.group(), initials_matcher.group(0));
                                                                    lab_initials.put(batch_matcher.group(), initials_matcher.group(0));
                                                                } else
                                                                    lab_initials.put(batch_matcher.group(), parts[p]);

                                                            }
                                                        } else {
                                                            Matcher initials_matcher = initials_pattern.matcher(estring[k + 1]);
                                                            if (initials_matcher.find()) {
//                                   System.out.print(initials_pattern.matcher(estring[k + 1]).find() + " ");
                                                                lab_initials.put(batch_matcher.group(), initials_matcher.group(0));
                                                            } else
                                                                lab_initials.put(batch_matcher.group(), estring[k + 1]);

                                                        }
                                                        k++;
                                                        if (k == size - 1)
                                                            break;
                                                    }
                                                }
                                            }

                                        }
                                    }

                                }
                                else if (string.contains("Prog"))
                                {
                                    System.out.println("HHHHHHHHHHHHHHHHHHHHHHHH");
                                }


//      string = string.trim().replaceAll("\n ", "");
                                else if (string.equals("usn")) {
                                    flag = 1;
                                    break;
                                }

                                if(i==0)
                                    teacher_origin=e.get(0);

                            }

                        }
                    }
                    usn_read_ct+=usn.size();
                    Originating_department = (String) session.getAttribute("department");
                    Originating_department=department;
                    System.out.println(Originating_department);
                    System.out.println(subject_code);
                    System.out.println(sem);
                    System.out.println(theory_initials);
                    System.out.println(lab_initials);
                    System.out.println(section);
                    teacher_origin=teacher_origin.substring(teacher_origin.indexOf("f")+2).toLowerCase();
                    if(teacher_origin.equalsIgnoreCase("computer science & engineering")||teacher_origin.equalsIgnoreCase("cse"))
                        teacher_origin="cse";
                    else  if(teacher_origin.replaceAll("\\s","").equalsIgnoreCase("Biotechnology")||teacher_origin.equalsIgnoreCase("bt")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("biotech"))
                        teacher_origin="bt";
                    else  if(teacher_origin.replaceAll("\\s","").equalsIgnoreCase("ase")||teacher_origin.equalsIgnoreCase("aero")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("aerospace"))
                        teacher_origin="ase";
                    else  if(teacher_origin.equalsIgnoreCase("Mathematics")||teacher_origin.equalsIgnoreCase("math")||teacher_origin.equalsIgnoreCase("ma"))
                        teacher_origin="math";
                    else  if(teacher_origin.equalsIgnoreCase("Chemical")||teacher_origin.equalsIgnoreCase("ch")||teacher_origin.equalsIgnoreCase("chem")||teacher_origin.equalsIgnoreCase("Chemical Engineering"))
                        teacher_origin="ch";
                    else  if(teacher_origin.equalsIgnoreCase("Chemistry")||teacher_origin.equalsIgnoreCase("che")||teacher_origin.equalsIgnoreCase("chem"))
                        teacher_origin="chem";
                    else  if(teacher_origin.equalsIgnoreCase("Electronics and Communications Engineering")||teacher_origin.equalsIgnoreCase("ece")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("Electronics&CommunicationEngineering")||teacher_origin.equalsIgnoreCase("ec")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("lectronics&CommunicationEngineering"))
                        teacher_origin="ece";
                    else  if(teacher_origin.replaceAll("\\s","").equalsIgnoreCase("CivilEngineering")||teacher_origin.equalsIgnoreCase("cv")||teacher_origin.equalsIgnoreCase("ce"))
                        teacher_origin="cv";
                    else  if(teacher_origin.equalsIgnoreCase("Industrial Engineering and Management")||teacher_origin.equalsIgnoreCase("iem"))
                        teacher_origin="iem";
                    else  if(teacher_origin.equalsIgnoreCase("Electronics and Instrumentation Engineering")||teacher_origin.equalsIgnoreCase("eie")||teacher_origin.equalsIgnoreCase("ei")||teacher_origin.equalsIgnoreCase("it")||teacher_origin.equalsIgnoreCase("Instrumentation Engineering")||teacher_origin.equalsIgnoreCase("Instrumentation Technology")||teacher_origin.equalsIgnoreCase("electronics & instrumentation engineering"))
                        teacher_origin="eie";
                    else  if(teacher_origin.equalsIgnoreCase("Electrical and Electronics Engineering")||teacher_origin.equalsIgnoreCase("eee")||teacher_origin.equalsIgnoreCase("Electrical & Electronics Engineering")||teacher_origin.equalsIgnoreCase("lectrical &Electronics Engineering"))
                        teacher_origin="eee";
                    else  if(teacher_origin.equalsIgnoreCase("Physics")||teacher_origin.equalsIgnoreCase("phy"))
                        teacher_origin="phy";
                    else  if(teacher_origin.equalsIgnoreCase("Telecommunication Engineering")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("Telecommunication&Engineering")||teacher_origin.equalsIgnoreCase("tc")||teacher_origin.equalsIgnoreCase("te")||teacher_origin.equalsIgnoreCase("tce"))
                        teacher_origin="te";
                    else  if(teacher_origin.equalsIgnoreCase("Information Science and Engineering")||teacher_origin.replaceAll("\\s","").equalsIgnoreCase("InformationScience&Engineering")||teacher_origin.equalsIgnoreCase("ise")||teacher_origin.equalsIgnoreCase("is"))
                        teacher_origin="ise";
                    else  if(teacher_origin.equalsIgnoreCase("mca")||teacher_origin.equalsIgnoreCase("Masters in computerscience & Architechture")||teacher_origin.equalsIgnoreCase("masters in computersci"))
                        teacher_origin="mca";
                    else  if(teacher_origin.equalsIgnoreCase("mech")||teacher_origin.equalsIgnoreCase("Mechanical engineering")||teacher_origin.equalsIgnoreCase("mechanical"))
                        teacher_origin="me";
                    System.out.println("teacher_origin:"+teacher_origin);
                    System.out.println(usn.values());
                    System.out.println(usn.keySet());

//// end of copy from excel
                    if (Objects.equals(section, "") || section == null)
                        section = "a";

                    if(section.length()>4)
                        section="a";
//
                    String subject_type = "";
                    int highestval = 0;
                    int highestses = 0;
                    Statement department_details = connection.createStatement();
                    Statement set_dep = connection.createStatement();
                    Statement stcla = connection.createStatement();
                    Statement inserts = connection.createStatement();
                    Statement inserts_theory = connection.createStatement();
                    Statement max = connection.createStatement();
                    Statement maxses = connection.createStatement();
                    Statement sttid = connection.createStatement();
                    Statement ad = connection.createStatement();
                    Statement attends = connection.createStatement();

                    connection.setAutoCommit(false);

//start of commentables
// department verification
                    if(Originating_department.equalsIgnoreCase("computer science & engineering")||Originating_department.equalsIgnoreCase("cse"))
                        Originating_department="cse";
                    else  if(Originating_department.replaceAll("\\s","").equalsIgnoreCase("ase")||Originating_department.equalsIgnoreCase("aero")||Originating_department.replaceAll("\\s","").equalsIgnoreCase("aerospace"))
                        Originating_department="ase";
                    else  if(Originating_department.replaceAll("\\s","").equalsIgnoreCase("Biotechnology")||Originating_department.equalsIgnoreCase("bt")||Originating_department.replaceAll("\\s","").equalsIgnoreCase("biotech"))
                        Originating_department="bt";
                    else  if(Originating_department.equalsIgnoreCase("Mathematics")||Originating_department.equalsIgnoreCase("math")||Originating_department.equalsIgnoreCase("ma"))
                        Originating_department="math";
                    else  if(Originating_department.equalsIgnoreCase("Chemical")||Originating_department.equalsIgnoreCase("ch")||Originating_department.equalsIgnoreCase("chem")||Originating_department.equalsIgnoreCase("Chemical Engineering"))
                        Originating_department="ch";
                    else  if(Originating_department.equalsIgnoreCase("Chemistry")||Originating_department.equalsIgnoreCase("che")||Originating_department.equalsIgnoreCase("chem"))
                        Originating_department="chem";
                    else  if(Originating_department.equalsIgnoreCase("Electronics and Communications Engineering")||Originating_department.equalsIgnoreCase("ece")||Originating_department.replaceAll("\\s","").equalsIgnoreCase("Electronics&CommunicationEngineering"))
                        Originating_department="ece";
                    else  if(Originating_department.equalsIgnoreCase("Civil Engineering")||Originating_department.equalsIgnoreCase("cv")||Originating_department.equalsIgnoreCase("ce"))
                        Originating_department="cv";
                    else  if(Originating_department.equalsIgnoreCase("Industrial Engineering and Management")||Originating_department.equalsIgnoreCase("iem"))
                        Originating_department="iem";
                    else  if(Originating_department.equalsIgnoreCase("Electronics and Instrumentation Engineering")||Originating_department.equalsIgnoreCase("eie")||Originating_department.equalsIgnoreCase("ei")||Originating_department.equalsIgnoreCase("it")||Originating_department.equalsIgnoreCase("Instrumentation Engineering"))
                        Originating_department="eie";
                    else  if(Originating_department.equalsIgnoreCase("Electrical and Electronics Engineering")||Originating_department.equalsIgnoreCase("eee"))
                        Originating_department="eee";
                    else  if(Originating_department.equalsIgnoreCase("Physics")||Originating_department.equalsIgnoreCase("phy"))
                        Originating_department="phy";
                    else  if(Originating_department.equalsIgnoreCase("Telecommunication Engineering")||Originating_department.equalsIgnoreCase("tc")||Originating_department.equalsIgnoreCase("te")||Originating_department.equalsIgnoreCase("tce"))
                        Originating_department="te";
                    else  if(Originating_department.equalsIgnoreCase("Information Science and Engineering")||Originating_department.equalsIgnoreCase("ise")||Originating_department.equalsIgnoreCase("is"))
                        Originating_department="ise";
                    else  if(Originating_department.equalsIgnoreCase("mca")||Originating_department.equalsIgnoreCase("Masters in computerscience & Architechture")||Originating_department.equalsIgnoreCase("masters in computersci"))
                        Originating_department="mca";
                    else  if(Originating_department.equalsIgnoreCase("mech")||Originating_department.equalsIgnoreCase("Mechanical engineering")||Originating_department.equalsIgnoreCase("mechanical"))
                        Originating_department="me";
                    else  if(Originating_department.equalsIgnoreCase("m.techcne")||Originating_department.equalsIgnoreCase("mteach cne")||Originating_department.equalsIgnoreCase("m. teach cne"))
                        Originating_department="mtechcne";
                    else  if(Originating_department.equalsIgnoreCase("m.techcse")||Originating_department.equalsIgnoreCase("mteach cse")||Originating_department.equalsIgnoreCase("m. teach cse"))
                        Originating_department="mtechcse";

                    String department_details_query = "SELECT `department_id`, `department_name`, `hod_id`, `date_created` FROM `department_details` WHERE `department_name`='" + Originating_department + "' or `department_id`= '" + Originating_department + "' ";
                    ResultSet rsdepart = department_details.executeQuery(department_details_query);

                    if (!rsdepart.isBeforeFirst())
                    {System.out.println("invalid department");
                        SQLException de = new SQLException("invalid department "+Originating_department);
                        throw de;

                    }

                    while (rsdepart.next()) {
                        department = rsdepart.getString(1);
                        filedep.add(department);
                    }
                    //       String department_set="";

                    //   set_dep.executeUpdate(department_set);

                    Statement stsub = connection.createStatement();
                    String subject_details_query = "SELECT `subject_type`, `sem` FROM `subject_details` WHERE `subject_code`='" + subject_code + "'";
                    ResultSet sub_details = stsub.executeQuery(subject_details_query);
                    if ( !sub_details.isBeforeFirst())
                    {System.out.print("no subject "+subject_code +"query"+subject_details_query);
                        Exception nst = new SQLException("no subject "+subject_code +"query"+subject_details_query);
                        throw nst;

                    }
                    while (sub_details.next()) {
                        subject_type = sub_details.getString(1);
                        sem = sub_details.getString(2);
                        course_end_execute=true;
                        //theory logic
                        if (!(theory_initials.isEmpty())) {
                            String cla = "SELECT `class_id` FROM `class` WHERE `department`='" + department + "' and `sem` ='" + sem + "' and (ISNULL(section) OR `section`='" + section + "') and `subject_code`='" + subject_code + "' and ISNULL(`batch`)  ";
                            ResultSet rscla = stcla.executeQuery(cla);
                            while(rscla.next())
                            {
                                course_end_execute=false;
                                String class_id=rscla.getString(1);
                                String del_class="DELETE FROM `class` WHERE `class_id`= ?";
                                PreparedStatement p =connection.prepareStatement(del_class);
                                p.setString(1,class_id);
                                p.executeUpdate();
                            }

                            String max_query = "SELECT max(`class_id`) FROM class";
                            ResultSet rsmax = max.executeQuery(max_query);
                            while (rsmax.next()) {
                                highestval = rsmax.getInt("max(`class_id`)");
                            }
                            String ses_max_query = "SELECT max(`session_id`) FROM session";
                            ResultSet rsmaxses = maxses.executeQuery(ses_max_query);
                            while (rsmaxses.next()) {
                                highestses = rsmaxses.getInt("max(`session_id`)");
                            }
                            highestval++;
                            String class_query = "INSERT INTO `class`(`class_id`, `department`, `sem`, `section`, `subject_code`, `subject_type`, `batch`, `group`) VALUES (" + highestval + ",'" + department + "','" + sem + "','" + section + "','" + subject_code + "','t',null,'teacher') on duplicate key update `department`=`department` ";

                            inserts_theory.addBatch(class_query);
                            String sub_fix ="UPDATE `subject_details` SET `subject_type`='t' WHERE `subject_code`= ?;";
                            PreparedStatement pfix = connection.prepareStatement(sub_fix);
                            pfix.setString(1,subject_code);
                            pfix.executeUpdate();
                            ArrayList<String> initials1 = (ArrayList<String>) theory_initials.get("theory");
                            for (String Initials : initials1) {
                                String tid_query = "SELECT `teacher_id` FROM `teacher_details` WHERE `staff_initial`= '" + Initials + "' and `department`= '"+teacher_origin+"'";
                                ResultSet rstid = sttid.executeQuery(tid_query);
                                if ( !rstid.isBeforeFirst())
                                {System.out.print("no teacher "+Initials +"\nquery"+tid_query);
                                    Exception nst = new SQLException("no teacher "+Initials +"\nquery"+tid_query);
                                    throw nst;

                                }
                                while (rstid.next()) {
                                    highestses++;
                                    String tid = rstid.getString(1);
                                    String insertsession = "INSERT INTO `session`(`session_id`, `teacher_id`, `class_id`, `active`) VALUES (" + highestses + ",'" + tid + "'," + highestval + ",1) on duplicate key update `teacher_id`=`teacher_id` ";
                                    inserts_theory.addBatch(insertsession);
                                }
                            }
                            Collection<String> usns = usn.values();
                            if (usns.isEmpty())
                            {System.out.print("no students -in usns colelction");
                                Exception nst = new SQLException("no students -in usns DS");
                                throw nst;
                            }

                            for (String Usn : usns) {
                                String ad_query = "SELECT `admin_no` FROM `student_details` WHERE `usn`='" + Usn + "'";
                                ResultSet rsad = ad.executeQuery(ad_query);

                                if ( !rsad.isBeforeFirst())
                                {System.out.print("no student- "+Usn+"\n query- " +ad_query);
                                    Exception nst = new SQLException("no student- "+Usn+"\n query- " +ad_query);
                                    throw nst;

                                }

                                while (rsad.next()) {
                                    String admin_no = rsad.getString(1);
                                    String attends_query = "INSERT INTO `attend`(`admin_no`, `usn`, `class_id`) VALUES ('" + admin_no + "','" + Usn + "'," + highestval + ") on duplicate key update `usn`=`usn`";
                                    inserts_theory.addBatch(attends_query);
                                }
                            }


                        }
                        inserts_theory.executeBatch();
                        connection.commit();
                        ////lab logic
                        connection.setAutoCommit(false);
                        if (!(lab_initials.isEmpty())) {
                            String max_query = "SELECT max(`class_id`) FROM class";
                            ResultSet rsmax = max.executeQuery(max_query);
                            while (rsmax.next()) {
                                highestval = rsmax.getInt("max(`class_id`)");
                            }
                            String ses_max_query = "SELECT max(`session_id`) FROM session";
                            ResultSet rsmaxses = maxses.executeQuery(ses_max_query);
                            while (rsmaxses.next()) {
                                highestses = rsmaxses.getInt("max(`session_id`)");
                            }
                            Set<String> batches = usn.keySet();
                            for (String batch : batches) {
                                String cla = "SELECT `class_id` FROM `class` WHERE `department`='" + department + "' and `sem` ='" + sem + "' and `section`='" + section + "' and `subject_code`='" + subject_code + "' and `batch`='" + batch + "' and `group`='teacher' ";
                                ResultSet rscla = stcla.executeQuery(cla);

                                while(rscla.next())
                                {
                                    course_end_execute=false;
                                    String class_id=rscla.getString(1);
                                    String del_class="DELETE FROM `class` WHERE `class_id`= ?;";
                                    PreparedStatement p =connection.prepareStatement(del_class);
                                    p.setString(1,class_id);
                                    p.executeUpdate();
                                }
                                ArrayList<String> initials1 = (ArrayList<String>) lab_initials.get(batch);
                                highestval++;
                                String class_query = "INSERT INTO `class`(`class_id`, `department`, `sem`, `section`, `subject_code`, `subject_type`, `batch`, `group`) VALUES (" + highestval + ",'" + department + "','" + sem + "','" + section + "','" + subject_code + "','l','" + batch + "','teacher') on duplicate key update `department`=`department` ";
                                String sub_fix ="UPDATE `subject_details` SET `subject_type`='l' WHERE `subject_code`= ?;";
                                PreparedStatement pfix = connection.prepareStatement(sub_fix);
                                pfix.setString(1,subject_code);
                                pfix.executeUpdate();

                                inserts.addBatch(class_query);
//                       String Initials =initials.get(initials.indexOf(batch));
                                for (String Initials1 : initials1) {
                                    highestses++;
                                    String tid_query = "SELECT `teacher_id` FROM `teacher_details` WHERE `staff_initial`= '" + Initials1 + "' and `department`='"+teacher_origin+"'";
                                    ResultSet rstid = sttid.executeQuery(tid_query);
                                    if ( !rstid.isBeforeFirst())
                                    {System.out.print("no teacher "+Initials1 +"\nquery"+tid_query);
                                        Exception nst = new SQLException("no teacher "+Initials1 +" \nquery"+tid_query);
                                        throw nst;

                                    }
                                    while (rstid.next()) {
                                        String tid = rstid.getString(1);
                                        String insertsession = "INSERT INTO `session`(`session_id`, `teacher_id`, `class_id`, `active`) VALUES (" + highestses + ",'" + tid + "'," + highestval + ",1) on duplicate key update `teacher_id`=`teacher_id`";
                                        inserts.addBatch(insertsession);
                                    }
                                }
                                Collection<String> usns = (Collection<String>) usn.get(batch);
                                if (usns.isEmpty())
                                {System.out.print("no students -in" + batch);
                                    Exception nst = new SQLException("no students -in " +batch);
                                    throw nst;
                                }
                                for (String Usn : usns) {
                                    String ad_query = "SELECT `admin_no` FROM `student_details` WHERE `usn`='" + Usn + "'";
                                    ResultSet rsad = ad.executeQuery(ad_query);
                                    if ( !rsad.isBeforeFirst())
                                    {System.out.print("no students -"+Usn+"\n query- " +ad_query);
                                        Exception nst = new SQLException("no students -"+Usn+"\n query- " +ad_query);

                                        throw nst;

                                    }
                                    while (rsad.next()) {

                                        String admin_no = rsad.getString(1);
                                        System.out.println("student "+admin_no+" attending");
                                        String attends_query = "INSERT INTO `attend`(`admin_no`, `usn`, `class_id`) VALUES ('" + admin_no + "','" + Usn + "'," + highestval + ")";
                                        inserts.addBatch(attends_query);
                                    }
                                }

                            }
                        }
                        inserts.executeBatch();
                        connection.commit();
                        ////end of lab logic
                    }
// if class stops existing and student,teacher mappped ... delete from foront end ui --- change to data table

//course end feed back
                    if(course_end_execute==true) {
                        connection.setAutoCommit(false);
                        HashMap<String, String> subs = new HashMap<>();
                        Statement course_end = connection.createStatement();
                        String cla_query = "SELECT distinct `subject_code`,`sem`,`subject_type`,`group` FROM `class` WHERE `department`='" + department + "' AND subject_code='" + subject_code + "'";
                        Statement clast = connection.createStatement();
                        ResultSet rsclass = clast.executeQuery(cla_query);

                        while (rsclass.next()) {
                            subs.put(rsclass.getString(1), rsclass.getString(3));

                        }
                        String course_into_teacher = "INSERT INTO `teacher_details`(`first_name`,`middle_name`,`last_name`,`teacher_id`,`designation`, `password`,`department`) VALUES ('course','end','feedback','course_end_feedback_" + department + "','subject','course_end_feedback_" + department + "','" + department + "') on duplicate key update `teacher_id`=`teacher_id`;";
                        course_end.addBatch(course_into_teacher);
                        for (Map.Entry<String, String> entry : subs.entrySet()) {
                            highestval++;
                            highestses++;
                            String course_end_query = "insert into `class`(`class_id`, `department`, `sem`, `subject_code`, `subject_type`,`group`,`section`) VALUES (" + highestval + ",'" + department + "',null,'" + entry.getKey() + "','" + entry.getValue() + "','course_end_feedback',null) on duplicate key UPDATE `sem`=`sem` ";
                            course_end.addBatch(course_end_query);
                            String course_end_session = "INSERT INTO session(session_id, teacher_id, class_id, active) VALUES (" + highestses + ",'course_end_feedback_" + department + "'," + highestval + ",1) on duplicate key update `active`=`active`";
                            course_end.addBatch(course_end_session);
                        }

                        course_end.executeBatch();
                        connection.commit();

                    }
// end of course end feedback


                    rows.clear();
                }
                file.close();
                valid_rows.clear();

            }


            connection.setAutoCommit(false);
            int app_max = 0;
            Statement inserts_app = connection.createStatement();
            Statement stapp = connection.createStatement();
            Statement stsess = connection.createStatement();
            Statement stques = connection.createStatement();
            Statement stust = connection.createStatement();
            String app_max_query = "SELECT max(`appraisal_id`) AS highest FROM `appraisal`";
            ResultSet rs = stapp.executeQuery(app_max_query);
            while (rs.next()) {
                app_max = rs.getInt(1);
            }
            Statement stena = connection.createStatement();
            Statement stena2 = connection.createStatement();
            Iterator iter = filedep.iterator();
while (iter.hasNext()) {
    String depa = (String)iter.next();
    String ena = "SELECT `sem`, `date_enabled`, `date_disabled`, `appraisal_number`, `type` FROM `enable_table` WHERE  `department`='" + depa + "' AND `appraisal_enabled`=1 ";
    ResultSet rsena = stena.executeQuery(ena);
    while (rsena.next()) {
        String sses = "SELECT `session_id`, `class_id`, `active` FROM `session` WHERE `class_id` in (select `class_id` from `class` where sem='" + rsena.getString("sem") + "' AND `department`='" + depa + "' and `group`='" + rsena.getString("type") + "')";
        ResultSet rsses = stsess.executeQuery(sses);
        while (rsses.next()) {

            app_max++;
            String app_q = "INSERT INTO `appraisal`(`appraisal_id`, `session_id`, `appraisal_number`, `students_given`) VALUES (" + app_max + ",'" + rsses.getString("session_id") + "'," + rsena.getInt("appraisal_number") + ",0) ON DUPLICATE KEY UPDATE `appraisal_number`=`appraisal_number`";
            stena2.executeUpdate(app_q);// what the??!!! y didnt this happen before ??!!
            //inserts_app.addBatch(app_q);
            String ques = "";
            // Mapping of appraisal value to appraisal (appraisal_value table)
            if (rsena.getString("type").equals("both")) {
                ques = "SELECT `question_id` FROM `appraisal_question` WHERE `type` in (select `subject_type` from `class` where `class_id`='" + rsses.getString("class_id") + "')";
            } else if (rsena.getString("type").equals("teacher")) {
                ques = "SELECT `question_id` FROM `appraisal_question` WHERE `type` in (select `subject_type` from `class` where `class_id`='" + rsses.getString("class_id") + "' and`group`='teacher')";
            } else if (rsena.getString("type").equals("course_end_feedback")) {
                ques = "SELECT `question_id` FROM `appraisal_question` WHERE `type`in (select `subject_type` from `class` where `class_id`='" + rsses.getString("class_id") + "' and`group`='course_end_feedback')";
            }
            ResultSet rsques = stques.executeQuery(ques);
            while (rsques.next()) {
                Statement appidst = connection.createStatement();
                String appidcheck = "SELECT appraisal_id FROM `appraisal` WHERE  session_id=" + rsses.getString("session_id") + " and appraisal_number= " + rsena.getInt("appraisal_number") + "";
                ResultSet rsappid = appidst.executeQuery(appidcheck);
                while (rsappid.next()) {
                    String q1 = "INSERT INTO`appraisal_value`(`appraisal_id`,`question_id`,`question_sum`) VALUES( " + rsappid.getInt(1) + ", '" + rsques.getString("question_id") + "', 0) ON DUPLICATE KEY UPDATE appraisal_id=appraisal_id";
                    inserts_app.addBatch(q1);
                }
            }
            //System.out.println("appraisal value filled");
            // Mapping of appraisal to appraisal complete (appraisal_value table)
            int stu_att_ct = 0;
            String stu = "SELECT `admin_no`, `usn` FROM `attend` WHERE `class_id`='" + rsses.getString("class_id") + "'";
            ResultSet rsstu = stust.executeQuery(stu);
            while (rsstu.next()) {
                Statement appidst = connection.createStatement();
                System.out.println("session id :" + rsses.getString("session_id"));
                String appidcheck = "SELECT appraisal_id FROM `appraisal` WHERE  session_id=" + rsses.getString("session_id") + " and appraisal_number= " + rsena.getInt("appraisal_number") + "";
                ResultSet rsappid = appidst.executeQuery(appidcheck);
                if (!(rsappid.isBeforeFirst())) {
                    Exception noapp = new SQLException("no appraisal created for session " + rsses.getString("session_id"));
                    throw noapp;
                }

                while (rsappid.next()) {
                    stu_att_ct++;
                    // System.out.println("apprsaisal id :"+rsappid.getInt(1));
                    String stu_q = "INSERT INTO`appraisal_complete`(`admin_no`,`usn`,`appraisal_id`) VALUES('" + rsstu.getString("admin_no") + "','" + rsstu.getString("usn") + "', " + rsappid.getInt(1) + ") ON DUPLICATE KEY UPDATE admin_no=admin_no";
                    inserts_app.addBatch(stu_q);
                }
                if (stu_att_ct == 1)
                    System.out.println("new appraisal" + stu_att_ct);
            }


        }

    }
}
            inserts_app.executeBatch();
            connection.commit();

            connection.setAutoCommit(false);
            Statement stmtapp = connection.createStatement();
            String appup = "UPDATE `appraisal` SET `department`=(SELECT `department` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "`students_given`=(Select count(appraisal_complete.usn) from appraisal_complete where appraisal_complete.appraisal_id=appraisal.appraisal_id ), "+
                    "`sem`=(SELECT `sem` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    " `section`=(SELECT `section` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "  `subject_code`=(SELECT `subject_code` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "  `subject_type`=(SELECT `subject_type` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "  `batch`=(SELECT `batch` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "  `group`=(SELECT `group` FROM `class`, `session` WHERE `class`.`class_id`=`session`.`class_id` AND `appraisal`.`session_id`=`session`.`session_id`)," +
                    "  `teacher_id`=(SELECT `teacher_id` FROM `session` WHERE `appraisal`.`session_id`=`session`.`session_id`)" +
                    " WHERE `department`IS NULL or `sem` IS NULL or `section`IS NULL or `subject_code` IS NULL or `subject_type` IS NULL or `batch` IS NULL or `group` IS NULL or `teacher_id`IS NULL;";
            stmtapp.executeUpdate(appup);
            connection.commit();
            RequestDispatcher r=request.getRequestDispatcher("/create.jsp");
            r.forward(request,response);

        }

        catch (SQLException ie)
        {ie.printStackTrace();
            PrintWriter pw= response.getWriter();
            pw.print("<html><head><h1>"+curfile+"</h1></head><body><h2>"+ie.getMessage()+"</h2></body></html>");
        }
        catch (NullPointerException ne)
        {ne.printStackTrace();
            PrintWriter pw= response.getWriter();
            pw.print("<html><head><h1>"+curfile+"</h1></head><body><h2>"+ne.getMessage()+"</br>check placement of teachers in file or check prog higher position *check section to batch*</h2></body></html>");
        }
        catch (IndexOutOfBoundsException ie)
        {ie.printStackTrace();
            PrintWriter pw= response.getWriter();
            pw.print("<html><head><h1>"+curfile+"</h1></head><body><h2>"+ie.getMessage()+"</br><h2>check number of fields </h2></body></html>");
        }
        catch (Exception e)
        {

            try {e.printStackTrace();
                connection.rollback();
                request.setAttribute("e", e);
                RequestDispatcher r=request.getRequestDispatcher("/error.jsp");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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