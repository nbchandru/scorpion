import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Bharath on 11/14/2015.
 */
public class pathconfig extends HttpServlet {

    public void doPost(HttpServletRequest request,HttpServletResponse response)
    {
        try {
//            OutputStream output=new FileOutputStream("C:/users/sumaram/Desktop/omni.properties");
            String csvupload=request.getParameter("csvupload");
            String report=request.getParameter("report");
            String logo=request.getParameter("logo");
            String db_pass=request.getParameter("db_pass");
            String db_user=request.getParameter("db_user");
            String jdbc_url=request.getParameter("jdbc_url");
            String jrxml=request.getParameter("jrxml");
            String picture_path=request.getParameter("picture_path");
            System.out.println(csvupload);
            System.out.println(report);
            System.out.println(logo);
            System.out.println(db_pass);
            System.out.println(db_user);
            System.out.println(jdbc_url);
            System.out.println(jrxml);
            System.out.println(picture_path);


            Properties properties=new Properties();
            properties.setProperty("csvupload",csvupload);
            properties.setProperty("report",report);
            properties.setProperty("logo",logo);
//            properties.setProperty("db_pass",db_pass);
//            properties.setProperty("db_user",db_user);
//            properties.setProperty("jdbc_url",jdbc_url);
            properties.setProperty("jrxml",jrxml);
//            properties.setProperty("picture_path",picture_path);
            properties.store(new FileOutputStream(getServletContext().getRealPath("/WEB-INF/omni.properties")),"Properties file to specify paths");
//            properties.store(output,"success");
            get();

            RequestDispatcher rd=request.getRequestDispatcher("/path.jsp");
            rd.forward(request,response);
//            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }


    }
    public void get()
    {

        Properties properties=new Properties();
        try {
            FileInputStream inputStream=new FileInputStream(getServletContext().getRealPath("/WEB-INF/omni.properties"));
            properties.load(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Property :"+properties.getProperty("report"));
        System.out.println("Property :"+properties.getProperty("logo"));
//        System.out.println("Property :"+properties.getProperty("db_pass"));
//        System.out.println("Property :"+properties.getProperty("db_user"));
//        System.out.println("Property :"+properties.getProperty("jdbc_url"));
        System.out.println("Property :"+properties.getProperty("jrxml"));
        System.out.println("Property :"+properties.getProperty("csvupload"));

    }
    public void doGet(HttpServletRequest request,HttpServletResponse response)
    {


    }

}
