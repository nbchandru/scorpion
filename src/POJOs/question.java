package POJOs;

/**
 * Created by Bharath on 11/12/2015.
 */
public class question {

String q_id;
String q_text;
String q_type;
String q_a1;
String q_a2;
String q_a3;
String q_a4;

    public question() {

    }

    @Override
    public String toString() {
        return "question{" +
                "q_id='" + q_id + '\'' +
                ", q_text='" + q_text + '\'' +
                ", q_type='" + q_type + '\'' +
                ", q_a1='" + q_a1 + '\'' +
                ", q_a2='" + q_a2 + '\'' +
                ", q_a3='" + q_a3 + '\'' +
                ", q_a4='" + q_a4 + '\'' +
                '}';
    }

    public question(String q_a4, String q_a3, String q_a2, String q_a1, String q_id, String q_text, String q_type) {
        this.q_a4 = q_a4;
        this.q_a3 = q_a3;
        this.q_a2 = q_a2;
        this.q_a1 = q_a1;
        this.q_id = q_id;
        this.q_text = q_text;
        this.q_type = q_type;
    }

    public String getQ_a4() {
        return q_a4;
    }

    public void setQ_a4(String q_a4) {
        this.q_a4 = q_a4;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getQ_text() {
        return q_text;
    }

    public void setQ_text(String q_text) {
        this.q_text = q_text;
    }

    public String getQ_type() {
        return q_type;
    }

    public void setQ_type(String q_type) {
        this.q_type = q_type;
    }

    public String getQ_a1() {
        return q_a1;
    }

    public void setQ_a1(String q_a1) {
        this.q_a1 = q_a1;
    }

    public String getQ_a2() {
        return q_a2;
    }

    public void setQ_a2(String q_a2) {
        this.q_a2 = q_a2;
    }

    public String getQ_a3() {
        return q_a3;
    }

    public void setQ_a3(String q_a3) {
        this.q_a3 = q_a3;
    }
}
