package rm.ibanc.md.entites.request;

import java.util.Map;

/**
 * Created by victor.zaitev on 09.08.2016.
 */
public class LoginForm {

    private String mEmail;
    private String mPassword;

    private Map<String, String> devicesDetail;

    public LoginForm() {
    }

    public LoginForm(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public Map<String, String> getDevicesDetail() {
        return devicesDetail;
    }

    public void setDevicesDetail(Map<String, String> devicesDetail) {
        this.devicesDetail = devicesDetail;
    }
}
