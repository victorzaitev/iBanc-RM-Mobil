package rm.ibanc.md.helper;

/**
 * Created by victor.zaitev on 03.10.2016.
 */
public class TokenManager {

    private static TokenManager instance;
    private String token;

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

        return !(token == null || token.isEmpty() || token.trim().equals(""));
    }


}
