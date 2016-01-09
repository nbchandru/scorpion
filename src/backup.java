import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Bharath on 11/15/2015.
 */
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.*;


@WebServlet(name = "backup")
public class backup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysqldump " + "omni2" + " > C:\\Users\\Bharath\\Desktop" + "omni2" + ".sql;"});
//
////Wait for the command to complete, and check if the exit value was 0 (success)
//            if (exec.waitFor() == 0) {
//                //normally terminated, a way to read the output
//                InputStream inputStream = exec.getInputStream();
//                byte[] buffer = new byte[inputStream.available()];
//                inputStream.read(buffer);
//
//                String str = new String(buffer);
//                System.out.println(str);
//            } else {
//                // abnormally terminated, there was some problem
//                //a way to read the error during the execution of the command
//                InputStream errorStream = exec.getErrorStream();
//                byte[] buffer = new byte[errorStream.available()];
//                errorStream.read(buffer);
//
//                String str = new String(buffer);
//                System.out.println(str);
//            }
            String executeCmd = "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysqldump.exe --add-drop-table -uroot -proot omni2 -r C:\\users\\Bharath\\Desktop\\omni5.sql";
            Process runtimeProcess;
            try {
                runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                int processComplete = runtimeProcess.waitFor();
                if (processComplete == 0) {
                    System.out.println("Backup created successfully");
                } else {
                    InputStream errorStream = runtimeProcess.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                errorStream.read(buffer);
                String str = new String(buffer);
                System.out.println(str);
                    System.out.println("Could not create the backup");
                }
        }
        catch (Exception e)
        {e.getStackTrace();
            e.printStackTrace();

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }
}
