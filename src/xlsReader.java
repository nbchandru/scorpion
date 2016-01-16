import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bharath on 10/24/2015.
 */
public class xlsReader extends HttpServlet {
    String SQL_INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024 *1024;
    private int maxMemSize = 4 * 1024;
    private File file ;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omni2", "root", "root");

            response.setContentType("teaxt/html");
            HttpSession session=request.getSession();
//            if(session.getAttribute("user") == null){
//                response.sendRedirect("login.jsp");
//            }
//        String selectsem=request.getParameter("selectsem");
//        System.out.println("Even or odd ? ===========>"+selectsem);
            HashSet<String> correct=new HashSet<String>();
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
            filePath=properties.getProperty("csvupload");
            File f=new File(filePath);
            if (!f.exists())
                f.mkdir();

            System.out.println(filePath);
            int len=filePath.length();
            if(!(filePath.charAt(len-1)=='/'))
                filePath+="/";
            HashSet<String> received =new HashSet<String>();
            isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("Ismultipart ? "+isMultipart);

            DiskFileItemFactory factory = new DiskFileItemFactory();
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

            upload.setSizeMax( maxFileSize );
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
            try {
                Iterator itf=files.iterator();
                connection.setAutoCommit(false);

                while (itf.hasNext()) {
                    long startTime = System.nanoTime();
                    String file_name = request.getParameter("file_name"); //Change this to upload later
                    FileInputStream file = new FileInputStream(new File(filePath+itf.next()));
                    ArrayList<ArrayList<ArrayList<String>>> all_tables = new ArrayList<>();
                    ArrayList<ArrayList<Integer>> valid_rows = new ArrayList<>();
                    ArrayList<String> sheet_names = new ArrayList<>();
                    //Get the workbook instance for XLS file
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
                    org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(file);

                    //Get first sheet from the workbook
                    int x = workbook.getNumberOfSheets();

                    for (int i = 0; i < x; i++) {
                        int autosize = 1;
                        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
//                XSSFSheet sheet = workbook.getSheetAt(i);
                        sheet_names.add(sheet.getSheetName());
                        System.out.println(sheet.getLastRowNum());
                        ArrayList<ArrayList<String>> table_data = new ArrayList<>();
                        //Iterate through each rows from first sheet

                        int rowStart = Math.min(15, sheet.getFirstRowNum());
                        int rowEnd = Math.max(1, sheet.getLastRowNum());
                        ArrayList<Integer> validrows = new ArrayList<>();
//                        int row_col=0;
                        int count = 0;
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
                                Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                                count++;
                                if (cell == null) {
                                    if (cn <= first)
                                        continue;
                                    else
                                        column_data.add("");
                                } else {
                                    if (autosize == 1)
                                        sheet.autoSizeColumn(cell.getColumnIndex());
                                    switch (cell.getCellType()) {
                                        case Cell.CELL_TYPE_BOOLEAN:
                                            column_data.add(String.valueOf(cell.getBooleanCellValue()).toLowerCase());
                                            break;
                                        case Cell.CELL_TYPE_NUMERIC:
                                            if (DateUtil.isCellDateFormatted(cell)) {
                                                column_data.add(new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue()).toLowerCase());
                                            } else {
                                                column_data.add(new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                            }
                                            break;
                                        case Cell.CELL_TYPE_STRING:
                                            column_data.add(cell.getStringCellValue().toLowerCase());
                                            break;
                                        case Cell.CELL_TYPE_BLANK:
                                            column_data.add("");
                                            break;
                                        case Cell.CELL_TYPE_FORMULA:
                                            flag = 1;
                                            switch (cell.getCachedFormulaResultType()) {
                                                case Cell.CELL_TYPE_NUMERIC:
                                                    column_data.add(new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString().toLowerCase());
                                                    break;
                                                case Cell.CELL_TYPE_STRING:
                                                    column_data.add(cell.getStringCellValue().toLowerCase());
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
                        }

                        int z = i + 1;



                        System.out.println("Sheet " + z + " complete");
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

                    }

                    file.close();
                    int y = -1;
                    Iterator<String> z=sheet_names.iterator();
                    while(z.hasNext()) {
                        y++;
                        request.setAttribute("all_tables", all_tables);
                        request.setAttribute("valid_rows", valid_rows);
                        String a_id = (String)session.getAttribute("a_id");
                        ArrayList<String> header = all_tables.get(y).get(0);
                        String[] harray=new String[header.size()];
                        harray=header.toArray(harray);
                        String qm = StringUtils.repeat("?,",header.size());
                        qm = (String) qm.subSequence(0, qm.length() - 1);
                        String query_input_id="select input_id from input_map group by input_id having count(distinct field_name) = ? and count(distinct case when field_name in ("+qm+") then field_name end) = ?";
                        PreparedStatement ps_input=connection.prepareStatement(query_input_id);
                        ps_input.setInt(1,header.size());
                        for(int i=2;i<=header.size()+1;i++) {
                            ps_input.setString(i,harray[i-2]);
                        }
                        ps_input.setInt(header.size()+2,header.size());
                        ResultSet resultSet=ps_input.executeQuery();
                        String input_id="";
                        while (resultSet.next())
                        {
                            input_id=resultSet.getString(1);
                        }
//                String input_id = request.getParameter("input_id");
                        System.out.println("a_id :" + a_id + " input_id :" + input_id);
                        // execute select SQL steatement
                        //automated field input  input_id from GUI a_id from loginServlet
//                    String s1="SELECT column_count,comment FROM input_file where input_id = ? and (a_id is NULL or a_id = ?) ; ";
                        String s1 = "SELECT column_count,`comment` FROM input_file WHERE input_id = ? AND a_id = ?; ";
                        PreparedStatement p1 = connection.prepareStatement(s1);
                        p1.setString(1, input_id);
                        p1.setString(2, a_id);
                        ResultSet rs1 = p1.executeQuery();
                        while (rs1.next()) {
                            System.out.println("column_count : " + rs1.getInt(1) + " comment : " + rs1.getString(2));
                        }


                        connection.setAutoCommit(false);
                        String input_map_query="SELECT `field_name`, `table_name`, `attribute_name`, `action` FROM input_map where input_id= ? ;";
                        PreparedStatement p2=connection.prepareStatement(input_map_query);
                        p2.setString(1,input_id);
                        ResultSet rs2=p2.executeQuery();
                        rs2.last();
                        int repeatsize=rs2.getRow();
                        rs2.beforeFirst();
                        //write logic for multiple table attribute later
                        //SELECT DISTINCT `table_name` FROM input_map where input_id= 'teacher_timetable'
                        String[] attributes=new String[repeatsize];
                        String[] fieldnames=new String[repeatsize];
                        int var=0;
                        String tableName="";
                        int var2=0;
                        while(rs2.next())
                        {
                            System.out.println("field_name : "+rs2.getString(1)+" table_name : "+rs2.getString(2)+" attribute_name : "+rs2.getString(3)+" action : "+rs2.getString(4));
                            String field_name=rs2.getString(1);
                            tableName=rs2.getString(2);
                            String attribute_name=rs2.getString(3);
                            String action=rs2.getString(4);
                            attributes[var++]="`"+attribute_name+"`";
                            fieldnames[var2++]=field_name;
                        }

                        ArrayList<String> row = all_tables.get(y).get(0);
                        String questionmarks = StringUtils.repeat("?,", repeatsize);
                        questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);

                        String query = "INSERT INTO " + tableName + "("+StringUtils.join(attributes,",")+") VALUES ("+questionmarks+") ON DUPLICATE KEY UPDATE  ";


                        for(int i = 0;i<repeatsize;i++)
                        {
                            query= query +attributes[i]+" ="+"?,";

                        }
                        query=query.substring(0,query.length()-1);
                        query=query+";";
                        PreparedStatement ps=connection.prepareStatement(query);

                        int i=1;
                        while(i<all_tables.get(y).size()) {
                            if(!(all_tables.get(y).get(i).size()<repeatsize)) {

                                query=query.substring(0,query.length()-1);
                                query=query+";";
                                for (int j = 0; j < repeatsize; j++) {

                                    int col_index = row.indexOf(fieldnames[j]);
                                    if(fieldnames[j].equals("usn")||fieldnames[j].equals("admin_no"))
                                        ps.setString(j + 1, all_tables.get(y).get(i).get(col_index).replaceAll("[\\s_]",""));
                                    else
                                    ps.setString(j + 1, all_tables.get(y).get(i).get(col_index));
                                }
                                for (int j = repeatsize; j < 2*repeatsize; j++) {
                                    int col_index = row.indexOf(fieldnames[(j-repeatsize)]);
                                    if(fieldnames[j-repeatsize].equals("usn")||fieldnames[j-repeatsize].equals("admin_no"))
                                        ps.setString(j + 1, all_tables.get(y).get(i).get(col_index).replaceAll("[\\s_]",""));
                                    else
                                    ps.setString(j + 1, all_tables.get(y).get(i).get(col_index));
                                }

                                ps.addBatch();
                                System.out.println("Query :" + query);
                            }
                            i++;
                        }
                        ps.executeBatch();
                        connection.commit();
                        z.next();
                    }

                }
    //            semFix fix=new semFix();
  //              fix.fixsemesters();

                RequestDispatcher r=request.getRequestDispatcher("/create.jsp");
                r.forward(request,response);
            }
            catch (StringIndexOutOfBoundsException ie)
            {
                PrintWriter pw= response.getWriter();
                pw.print("<html><head><h1>invalid fields inserted</h1></head><body><h2>check input map file</h2></body></html>");
            }
            catch (Exception e) {
                connection.rollback();
                request.setAttribute("e", e);
                RequestDispatcher r=request.getRequestDispatcher("/error.jsp");
                e.printStackTrace();
                System.out.println("Error : " + e.getMessage());
            } finally {
                connection.close();
            }

//                long stopTime = System.nanoTime();
//                long elapsedTime = stopTime - startTime;
//
//                System.out.println("Time taken : " + elapsedTime / 1000000000.0 + "s");

//                RequestDispatcher r = request.getRequestDispatcher("/check.jsp");
//                r.forward(request, response);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);

    }



}