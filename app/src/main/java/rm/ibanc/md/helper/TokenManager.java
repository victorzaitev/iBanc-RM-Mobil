package rm.ibanc.md.helper;

/**
 * Created by PC01017745 on 03.10.2016.
 */
public class TokenManager {

    private String token;

    private static  TokenManager instance;

    private TokenManager(){}

    public static TokenManager getInstance(){

        if (instance == null){
            instance = new TokenManager();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public boolean isTokenExist(){

        if (token == null || token.isEmpty() || token.trim().equals("")){
            return false;
        } else{
            return true;
        }
    }


}
