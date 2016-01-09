package POJOs;

/**
 * Created by Bharath on 11/12/2015.
 */

    public class teacher_details {

    String first_name;
    String middle_name;
    String last_name;
    String staff_initial;

    public teacher_details(String staff_initial, String last_name, String middle_name, String first_name) {
        this.staff_initial = staff_initial;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
    }

    public teacher_details() {

    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getStaff_initial() {
        return staff_initial;
    }

    public void setStaff_initial(String staff_initial) {
        this.staff_initial = staff_initial;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }


    @Override
    public String toString() {
        return "teacher_details{" +
                "first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", staff_initial='" + staff_initial + '\'' +
                '}';
    }
}
