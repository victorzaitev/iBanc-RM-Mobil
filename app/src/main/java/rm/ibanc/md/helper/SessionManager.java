package rm.ibanc.md.helper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 * Created by PC01017745 on 28.09.2016.
 */
public class SessionManager {


    private String guid;

    private static SessionManager instance;

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

        if (guid == null || guid.isEmpty() || guid.trim().equals("")){
            return false;
        } else{
            return true;
        }
    }

}
