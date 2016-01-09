package POJOs;


/**
 * Created by Bharath on 11/12/2015.
 */
public class single_appraisal {

    String type;
    String app;
    String id;
    String usn;
    String subName;
    String subShortName;
    String subCode;
    teacher_details details;

    public teacher_details getDetails() {
        return details;
    }

    public void setDetails(teacher_details details) {
        this.details = details;
    }

    public single_appraisal(String type, String app, String id, String usn, String subName, String subShortName, String subCode) {
        this.type = type;
        this.app = app;
        this.id = id;
        this.usn = usn;
        this.subName = subName;
        this.subShortName = subShortName;
        this.subCode = subCode;
    }

    public single_appraisal() {

    }


    @Override
    public String toString() {
        return "single_appraisal{" +
                "type='" + type + '\'' +
                ", app='" + app + '\'' +
                ", id='" + id + '\'' +
                ", usn='" + usn + '\'' +
                ", subName='" + subName + '\'' +
                ", subShortName='" + subShortName + '\'' +
                ", subCode='" + subCode + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubShortName() {
        return subShortName;
    }

    public void setSubShortName(String subShortName) {
        this.subShortName = subShortName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
