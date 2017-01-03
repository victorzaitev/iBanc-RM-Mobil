package rm.ibanc.md.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rm.ibanc.md.activity.CursActivity;
import rm.ibanc.md.adapter.ExchangeAdapter;
import rm.ibanc.md.constant.UrlConstant;
import rm.ibanc.md.entites.request.ExchangeRateForm;
import rm.ibanc.md.entites.rest.ExchangeRateDetails;
import rm.ibanc.md.entites.rest.ExchangeRateList;
import rm.ibanc.md.entites.view.ExchangeDetails;
import rm.ibanc.md.ibanc_rm.R;
import rm.ibanc.md.recyclerview.DividerItemDecoration;

/**
 * Created by victor.zaitev on 27.07.2016.
 */
public class ExchangeFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;


    private List<ExchangeDetails> exchangeFormList = new ArrayList<>();
    private ExchangeAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        initCollapsingToolbar(view);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment_exchange_view);

        mAdapter = new ExchangeAdapter(exchangeFormList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setMinimumHeight(3000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);
        prepareMovieData();


        try {
            Glide.with(this).load(R.drawable.covert_excenge).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CursActivity.class);
                getActivity().startActivity(intent);

            }
        });


        return view;
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Valuta  Cumparare  Vinzare  BNM");

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;

                }
            }
        });
    }


    //------------------------------------->
    private void prepareMovieData() {


        String url = UrlConstant.findExchangeRateByDate;


        GetExchangeRateByDateTask getExchangeRateByDateTask = new GetExchangeRateByDateTask(url, new Date());
        getExchangeRateByDateTask.execute((Void) null);

    }


    public class GetExchangeRateByDateTask extends AsyncTask<Void, Void, ExchangeRateDetails> {
        private final String url;
        private final Date date;
        private ProgressDialog progressDialog;

        public GetExchangeRateByDateTask(String url, Date date) {
            this.url = url;
            this.date = date;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Getting Data ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ExchangeRateDetails doInBackground(Void... params) {

            // Set the Content-Type heade
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            ExchangeRateForm exchangeRateForm = new ExchangeRateForm();
            exchangeRateForm.setDateCurs(date);


            final HttpEntity<ExchangeRateForm> requestEntity = new HttpEntity<>(exchangeRateForm, requestHeaders);


            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            ResponseEntity<ExchangeRateDetails> responseEntity = null;

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ExchangeRateDetails.class);
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {
                try {
                    ExchangeRateDetails exchangeRateDetails = new ObjectMapper().readValue(e.getResponseBodyAsString(), ExchangeRateDetails.class);
                    return exchangeRateDetails;
                } catch (IOException exxx) {
                    return null;
                }

            } catch (ResourceAccessException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ExchangeRateDetails exchangeRateDetails) {
            progressDialog.dismiss();

            if (exchangeRateDetails != null) {

                if (exchangeRateDetails.getReturnCode() == 0) {

                    for (ExchangeRateList exchangeRateList : exchangeRateDetails.getExchangeRateList()) {
                        ExchangeDetails exchangeForm = new ExchangeDetails();

                        exchangeForm.setValuta(exchangeRateList.getValutaShortName());
                        exchangeForm.setCumparare(exchangeRateList.getBuyCurs());
                        exchangeForm.setVinzare(exchangeRateList.getSellCurs());

                        exchangeForm.setCursBnm(exchangeRateList.getOfficialCurs());


                        exchangeFormList.add(exchangeForm);
                    }
                } else {
                    showToastMessage(exchangeRateDetails.getReturnDescription());
                }
            } else {
                showToastMessage("Sunt probleme cu conexiunea, verificati conexiunea cu internetul");
            }
            mAdapter.notifyDataSetChanged();
        }


        private void showToastMessage(final String showText) {
            final Context context = getContext();
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

