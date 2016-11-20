package rm.ibanc.md.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rm.ibanc.md.adapter.AccountAdapter;
import rm.ibanc.md.entites.form.AccountForm;
import rm.ibanc.md.entites.request.LogoutForm;
import rm.ibanc.md.entites.request.RequestForm;
import rm.ibanc.md.entites.rest.CardsDetails;
import rm.ibanc.md.entites.rest.CustomersDetails;
import rm.ibanc.md.helper.SessionManager;
import rm.ibanc.md.helper.TokenManager;

import rm.ibanc.md.ibanc_rm.R;
import rm.ibanc.md.recyclerview.DividerItemDecoration;
import rm.ibanc.md.recyclerview.ClickListener;
import rm.ibanc.md.recyclerview.RecyclerTouchListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private SessionManager session;
    private TokenManager tokenManager;
    private TextView textNameView;
    private TextView textEmailView;
    private View mProgressView;
    private CustomersDetails customersDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_home);
        mProgressView = findViewById(R.id.activity_progress);
        session = SessionManager.getInstance();
        tokenManager = TokenManager.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);

        textNameView = (TextView) headerView.findViewById(R.id.textNameView);
        textEmailView = (TextView) headerView.findViewById(R.id.textEmailView);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (session.isLoggedIn())
            new UserLogoutTask().execute();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (session.isLoggedIn())
            new UserLogoutTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        customersDetails = (CustomersDetails) getIntent().getSerializableExtra("CustomersDetails");

        if (customersDetails == null) {
            new UserLogoutTask().execute();
        } else {

            textEmailView.setText(customersDetails.getEmail());
            textNameView.setText(customersDetails.getFirstName() + " " + customersDetails.getLastName());
        }
    }


    //---------------------------------------------->



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_userPage) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cards) {

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            RequestForm requestForm = new RequestForm();
            requestForm.setGUID(tokenManager.getToken());
            requestForm.setPersonalId(customersDetails.getPersonalId());

            final HttpEntity<RequestForm> requestEntity = new HttpEntity<RequestForm>(requestForm, requestHeaders);
            String url = "http://172.18.111.101:8089/iBanc-RM-1.0/find/cards";

            UserGetAccountTask userGetAccountTask = new UserGetAccountTask(requestEntity, url);
            userGetAccountTask.execute((Void) null);


        } else if (id == R.id.nav_accounts) {
            Toast.makeText(getApplicationContext(), "Acconts is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_transfer) {
            Toast.makeText(getApplicationContext(), "Transfer is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_invoices) {
            Toast.makeText(getApplicationContext(), "Invoices is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_services) {
            Toast.makeText(getApplicationContext(), "Service is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_tools) {
            Toast.makeText(getApplicationContext(), "Tools is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_exchangeRate) {
            Toast.makeText(getApplicationContext(), "Exchange Rate is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_maps) {
            Toast.makeText(getApplicationContext(), "Maps is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_news) {
            Toast.makeText(getApplicationContext(), "News is selected!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), "About is selected!", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        }
    }


    public class UserGetAccountTask extends AsyncTask<Object, Object, CardsDetails> {

        private final HttpEntity<RequestForm> requestEntity;

        private final String url;

        public UserGetAccountTask(HttpEntity<RequestForm> requestEntity, String url) {
            this.requestEntity = requestEntity;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected CardsDetails doInBackground(Object... params) {
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());



            ResponseEntity<CardsDetails> responseEntity = null;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CardsDetails.class);
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {

                try {
                    CardsDetails cardsDetailsResponse = new ObjectMapper().readValue(e.getResponseBodyAsString(), CardsDetails.class);
                    return cardsDetailsResponse;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return null;
                }

            } catch (ResourceAccessException ex){
                return null;
            } catch (Exception ex){
                return null;
            }

        }

        @Override
        protected void onPostExecute(CardsDetails cardsDetails) {
            showProgress(false);
//--------->
            Intent intent = new Intent(HomeActivity.this, CardsActivity.class);
            startActivity(intent);
//--------->

            if (cardsDetails!=null) {
                if (cardsDetails.getReturnCode() == 0) {

                    tokenManager.setToken(cardsDetails.getToken());

                  //  Intent intent = new Intent(HomeActivity.this, CardsActivity.class);

                  //  intent.putExtra("CardsDetailsList",  (Serializable) cardsDetails.getCardsList());


                    startActivity(intent);
                    finish();
                } else {
                    showToastMessage(customersDetails.getReturnDescription());
                }

            } else {
                showToastMessage("Sunt probleme cu conexiunea, verificati conexiunea cu internetul");
            }

        }
    }


    private void showToastMessage(final String showText){
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

    public class UserLogoutTask extends AsyncTask {


        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Object[] params) {

            LogoutForm logoutForm = new LogoutForm();
            logoutForm.setGuidToken(session.getGuid());


            // Set the Content-Type heade
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<LogoutForm> requestEntity = new HttpEntity<LogoutForm>(logoutForm, requestHeaders);


            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            String url = "http://172.18.111.101:8089/iBanc-RM-1.0/logout/customers";
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

            HttpStatus httpStatus = responseEntity.getStatusCode();
            System.out.println(" httpStatus " + httpStatus.toString());

            System.out.println(" responseEntity.getBody() " + responseEntity.getBody());
            System.out.println(" responseEntity.getBody().getName() " + responseEntity.getBody());

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            showProgress(false);
            session.setGuid(null);
            tokenManager.setToken(null);
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
