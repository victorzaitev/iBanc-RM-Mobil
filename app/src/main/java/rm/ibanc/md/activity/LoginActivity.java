package rm.ibanc.md.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rm.ibanc.md.constant.UrlConstant;
import rm.ibanc.md.entites.request.LoginForm;
import rm.ibanc.md.entites.rest.CustomersDetails;
import rm.ibanc.md.helper.SessionManager;
import rm.ibanc.md.helper.TokenManager;
import rm.ibanc.md.ibanc_rm.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_PHONE_STATE permission request.
     */

    private static final int REQUEST_READ_PHONE_STATE = 1;
    private TelephonyManager tm;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SessionManager session;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        // Session manager
        session = SessionManager.getInstance();


        tokenManager = TokenManager.getInstance();

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        mEmailView = (EditText) findViewById(R.id.email);

        verifyTelephonyManager();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void verifyTelephonyManager() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        } else {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_READ_PHONE_STATE);
            } else {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            }
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {


            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                } else {
//------------->                  //  boolean should = ActivityCompat.shouldShowRequestPermissionRationale((Activity) cont, Manifest.permission.READ_PHONE_STATE);
//------------->
//------------->
//------------->
                }

            }
            break;


        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        Pattern p = Pattern.compile("^[A-Za-z0-9]+([_A-Za-z0-9-])+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email);
        return m.matches();

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*#?&\\.-_,|~`])[A-Za-z\\d$@$!%*#?&\\.-_,|~`]{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, CustomersDetails> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected CustomersDetails doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            LoginForm loginForm = setLoginForm();

            return getCustomersDetails(loginForm);

        }


        @Override
        protected void onPostExecute(final CustomersDetails customersDetails) {
            mAuthTask = null;
            showProgress(false);

            if (customersDetails != null) {
                if (customersDetails.getReturnCode() == 0) {
                    session.setGuid(customersDetails.getGuid());
                    tokenManager.setToken(customersDetails.getToken());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("CustomersDetails", customersDetails);
                    startActivityForResult(intent, 1);
                    //Activity(intent);
                    finish();
                } else {
                    showToastMessage(customersDetails.getReturnDescription());
                }

            } else {
                showToastMessage("Sunt probleme cu conexiunea, verificati conexiunea cu internetul");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


        private LoginForm setLoginForm() {

            Map<String, String> devicesDetail = new HashMap<>();

            devicesDetail.put("brand", Build.BRAND);
            devicesDetail.put("product", Build.PRODUCT);
            devicesDetail.put("model", Build.MODEL);


            devicesDetail.put("IMIE", tm.getDeviceId());
            devicesDetail.put("IMSI", tm.getSubscriberId());
            devicesDetail.put("softwareVersion", tm.getDeviceSoftwareVersion());

            devicesDetail.put("networkOperator", tm.getNetworkOperator());
            devicesDetail.put("networkOperatorName", tm.getNetworkOperatorName());

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();

            devicesDetail.put("macAddress", wInfo.getMacAddress());
            devicesDetail.put("bssid", wInfo.getBSSID());
            devicesDetail.put("ssid", wInfo.getSSID());

            // Create and populate a simple object to be used in the request
            LoginForm loginForm = new LoginForm();

            loginForm.setmEmail(mEmail);
            loginForm.setmPassword(mPassword);
            loginForm.setDevicesDetail(devicesDetail);

            return loginForm;
        }


        private CustomersDetails getCustomersDetails(LoginForm loginForm) {


            // Set the Content-Type heade
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            final HttpEntity<LoginForm> requestEntity = new HttpEntity<LoginForm>(loginForm, requestHeaders);

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            String url = UrlConstant.loginCustomers;

            // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
            ResponseEntity<CustomersDetails> responseEntity = null;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CustomersDetails.class);
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {

                try {
                    CustomersDetails customersDetailsResponse = new ObjectMapper().readValue(e.getResponseBodyAsString(), CustomersDetails.class);
                    return customersDetailsResponse;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return null;
                }

            } catch (ResourceAccessException ex) {
                return null;
            } catch (Exception ex) {
                return null;
            }

        }

        private void showToastMessage(final String showText) {
            final Context context = getApplicationContext();
            Handler handler = new Handler(context.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, showText, duration);
                    toast.show();
                }
            });
        }

    }
}