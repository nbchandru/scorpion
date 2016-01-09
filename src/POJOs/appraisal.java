package POJOs;

import java.util.HashMap;

/**
 * Created by Bharath on 11/12/2015.
 */
public class appraisal {

    String[] appraisal_ids;
    int appraisal_count;
    HashMap<String,single_appraisal> s_app;


    public HashMap<String, single_appraisal> getS_app() {
        return s_app;
    }

    public void setS_app(HashMap<String, single_appraisal> s_app) {
        this.s_app = s_app;
    }

    public int getAppraisal_count() {
        return appraisal_count;
    }

    public void setAppraisal_count(int appraisal_count) {
        this.appraisal_count = appraisal_count;
    }

    public String[] getAppraisal_ids() {
        return appraisal_ids;
    }

    public void setAppraisal_ids(String[] appraisal_ids) {
        this.appraisal_ids = appraisal_ids;
    }
}
