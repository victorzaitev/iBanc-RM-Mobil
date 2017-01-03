package rm.ibanc.md.helper;

/**
 * Created by victor.zaitev on 28.09.2016.
 */
public class SessionManager {


    private static SessionManager instance;
    private String guid;

    private SessionManager(){}

    public static SessionManager getInstance(){

       if (instance == null) {
           instance = new SessionManager();
       }

       return instance;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isLoggedIn(){

        return !(guid == null || guid.isEmpty() || guid.trim().equals(""));
    }

}
