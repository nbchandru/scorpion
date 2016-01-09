import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Bharath on 11/29/2015.
 */
public class semFix {

    public void fixsemesters() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omni2", "root", "root");
            connection.setAutoCommit(false);
            Statement s=connection.createStatement();
            Statement s1=connection.createStatement();
            String query="SELECT subject_code FROM subject_details";
            ResultSet rs=s.executeQuery(query);
            while (rs.next()) {
                String sub_code=rs.getString(1);
                String[] p = sub_code.split("[0-9]{2,3}[a-z]{2,6}", 2);
                String sem=String.valueOf(p[1].charAt(0));
                String q="UPDATE subject_details SET sem='"+sem+"' WHERE subject_code='"+sub_code+"'";
                s1.addBatch(q);
            }
            s1.executeBatch();
            connection.commit();
        }catch (Exception e)
        {

            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            if(connection!=null)
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
        }
    }

}
