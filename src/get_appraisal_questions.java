import POJOs.question;
import com.google.gson.Gson;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bharath on 11/12/2015.
 */
public class get_appraisal_questions extends HttpServlet {
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

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Questions");
        //store as arraylist of question objects
        Connection connection=null;
        try {

            connection=getConnection();
            Set<question> questions = new HashSet<>();
            String questions_query="SELECT * FROM appraisal_question";
            Statement stq=connection.createStatement();
            ResultSet rs=stq.executeQuery(questions_query);
            while(rs.next())
            {
                String id=rs.getString("question_id");
                String text=rs.getString("question_text");
                String type=rs.getString("type");
                question q=new question();
                q.setQ_id(id);
                q.setQ_text(text);
                q.setQ_type(type);
                q.setQ_a1("Excellent");
                q.setQ_a2("Very Good");
                q.setQ_a3("Good");
                q.setQ_a4("Satisfactory");
                questions.add(q);
            }

            Gson gson = new Gson();
            String json = gson.toJson(questions);
            System.out.println(json);
//        String json="[{\"q_id\":\"ql1\",\"q_text\":\"what is the clarity with the teacher explains the conduction of experiments in the  lab?\",\"q_type\":\"l\",\"q_a1\":\"exceedingly well\",\"q_a2\":\"adequately well\",\"q_a3\":\"inadequate\",\"q_a4\":\"totally inadequate\"},{\"q_id\":\"ql10\",\"q_text\":\"what is the overall effectiveness of the teaching learning process in the laboratory?\",\"q_type\":\"l\",\"q_a1\":\"very effective\",\"q_a2\":\"effective\",\"q_a3\":\"somewhat effective\",\"q_a4\":\"not effective\"},{\"q_id\":\"ql2\",\"q_text\":\"does the teacher conduct viva voce and invite discussions in the laboratory?\",\"q_type\":\"l\",\"q_a1\":\"always encourages\",\"q_a2\":\"often encourages\",\"q_a3\":\"rarely encourages\",\"q_a4\":\"discourages\"},{\"q_id\":\"ql3\",\"q_text\":\"what is the extent to which teacher stimulates the students to think innovatively in lab? \",\"q_type\":\"l\",\"q_a1\":\"highly stimulating\",\"q_a2\":\"stimulating\",\"q_a3\":\"rarely stimulating\",\"q_a4\":\"never\"},{\"q_id\":\"ql4\",\"q_text\":\"what is the level of clarity of communication the teacher exhibits in demonstrating the practical utility of the experiment?\",\"q_type\":\"l\",\"q_a1\":\"excellent\",\"q_a2\":\"good\",\"q_a3\":\"satisfactory\",\"q_a4\":\"poor\"},{\"q_id\":\"ql5\",\"q_text\":\"what is the attitude of the teacher towards the students in the laboratory ?\",\"q_type\":\"l\",\"q_a1\":\"usually sympathetic\",\"q_a2\":\"avoids personal contact\",\"q_a3\":\"indifferent towards student\",\"q_a4\":\"shows personal bias\"},{\"q_id\":\"ql6\",\"q_text\":\"how do you consider the teacher's approach to evaluation?\",\"q_type\":\"l\",\"q_a1\":\"fair and objective\",\"q_a2\":\"strict\",\"q_a3\":\"liberal and generous\",\"q_a4\":\"exhibits bias\"},{\"q_id\":\"ql7\",\"q_text\":\"what point about the teacher impressed you most? \",\"q_type\":\"l\",\"q_a1\":\"ability to motivate learning and create interest\",\"q_a2\":\"presents the concepts clearly\",\"q_a3\":\"ensures good academic ambience\",\"q_a4\":\"nothing in particular\"},{\"q_id\":\"ql8\",\"q_text\":\"how often does the teacher discussed and offered guidance and tips for better performance in practical exams? \",\"q_type\":\"l\",\"q_a1\":\"regularly\",\"q_a2\":\"as and when necessary\",\"q_a3\":\"occasionally\",\"q_a4\":\"does not offer any tips\"},{\"q_id\":\"ql9\",\"q_text\":\"does the teacher often insist on design and procedure before allowing students to conduct experiments?\",\"q_type\":\"l\",\"q_a1\":\"regularly\",\"q_a2\":\"depending on the experiment\",\"q_a3\":\"quite often\",\"q_a4\":\"not at all\"},{\"q_id\":\"qt1\",\"q_text\":\"what is the clarity with the teacher explains the concepts in subject? \",\"q_type\":\"t\",\"q_a1\":\"exceedingly well\",\"q_a2\":\"adequately well\",\"q_a3\":\"inadequate\",\"q_a4\":\"totally inadequate\"},{\"q_id\":\"qt10\",\"q_text\":\"how do you consider the teacher's evaluation?\",\"q_type\":\"t\",\"q_a1\":\"fair and objective\",\"q_a2\":\"strict\",\"q_a3\":\"generous\",\"q_a4\":\"exhibits bias\"},{\"q_id\":\"qt11\",\"q_text\":\"what qualities of the teacher you experienced?\",\"q_type\":\"t\",\"q_a1\":\"presents concepts clearly, motivates and guides\",\"q_a2\":\"presents concepts clearly\",\"q_a3\":\"guides in preparing for exam\",\"q_a4\":\"nothing in particular\"},{\"q_id\":\"qt12\",\"q_text\":\"how often the teacher gives information about the developments in the subject?\",\"q_type\":\"t\",\"q_a1\":\"regularly\",\"q_a2\":\"quite often\",\"q_a3\":\"rarely\",\"q_a4\":\"never\"},{\"q_id\":\"qt13\",\"q_text\":\"how often does the teacher discuss, offer guidance and tips for better performance in exams? \",\"q_type\":\"t\",\"q_a1\":\"regularly\",\"q_a2\":\"quiet often\",\"q_a3\":\"occasionally\",\"q_a4\":\"does not offer any tips\"},{\"q_id\":\"qt14\",\"q_text\":\"does the teacher encourage questions and clarifications outside class hour?\",\"q_type\":\"t\",\"q_a1\":\"always encourages\",\"q_a2\":\"sometimes encourages\",\"q_a3\":\"rarely encourages\",\"q_a4\":\"does not entertain\"},{\"q_id\":\"qt15\",\"q_text\":\"what is the overall effectiveness of the teaching learning process in class?\",\"q_type\":\"t\",\"q_a1\":\"most effective\",\"q_a2\":\"effective\",\"q_a3\":\"somewhat effective\",\"q_a4\":\"not effective\"},{\"q_id\":\"qt2\",\"q_text\":\"does the teacher encourage questions and invite discussions in class?\",\"q_type\":\"t\",\"q_a1\":\"always encourages\",\"q_a2\":\"often encourages\",\"q_a3\":\"rarely encourages\",\"q_a4\":\"discourages\"},{\"q_id\":\"qt3\",\"q_text\":\"what is the extent to which teacher stimulates the students to think? \",\"q_type\":\"t\",\"q_a1\":\"highly\",\"q_a2\":\"average\",\"q_a3\":\"rarely\",\"q_a4\":\"never\"},{\"q_id\":\"qt4\",\"q_text\":\"how does the teacher executes the plan to cover the syllabus with in time?\",\"q_type\":\"t\",\"q_a1\":\"as per schedule\",\"q_a2\":\"earlier than schedule\",\"q_a3\":\"later than schedule\",\"q_a4\":\"does not have a fixed plan\"},{\"q_id\":\"qt5\",\"q_text\":\"how much of the class time does the teacher use for teaching the subject?\",\"q_type\":\"t\",\"q_a1\":\"above 90%\",\"q_a2\":\"80-90%\",\"q_a3\":\"60-80%\",\"q_a4\":\"below 60 %\"},{\"q_id\":\"qt6\",\"q_text\":\"what is the level of clarity of communication the teacher exhibits in class?\",\"q_type\":\"t\",\"q_a1\":\"excellent\",\"q_a2\":\"good\",\"q_a3\":\"satisfactory\",\"q_a4\":\"poor\"},{\"q_id\":\"qt7\",\"q_text\":\"what is the attitude of the teacher towards the students?\",\"q_type\":\"t\",\"q_a1\":\"participative in teaching learning process\",\"q_a2\":\"supportive role\",\"q_a3\":\"cooperative attitude\",\"q_a4\":\"not supportive and cooperative\"},{\"q_id\":\"qt8\",\"q_text\":\"how punctual is the teacher to classes?\",\"q_type\":\"t\",\"q_a1\":\"always on time\",\"q_a2\":\"occasionally comes late\",\"q_a3\":\"frequently comes late\",\"q_a4\":\"never comes on time\"},{\"q_id\":\"qt9\",\"q_text\":\"how often does the teacher use practical example to drive home concepts? \",\"q_type\":\"t\",\"q_a1\":\"regularly\",\"q_a2\":\"depending on the topic of discussion \",\"q_a3\":\"occasionally\",\"q_a4\":\"never\"}]";
            response.setContentType("application/json");
            response.getWriter().write(json);
        }catch (Exception e){e.printStackTrace();}
        finally {
            try {
                if(connection!=null)
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }
