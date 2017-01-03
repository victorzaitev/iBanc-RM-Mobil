package rm.ibanc.md.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rm.ibanc.md.adapter.ExchangeAdapter;
import rm.ibanc.md.constant.UrlConstant;
import rm.ibanc.md.entites.request.ExchangeRateForm;
import rm.ibanc.md.entites.rest.ExchangeRateDetails;
import rm.ibanc.md.entites.rest.ExchangeRateList;
import rm.ibanc.md.entites.view.ExchangeDetails;
import rm.ibanc.md.ibanc_rm.R;
import rm.ibanc.md.recyclerview.DividerItemDecoration;

public class CursActivity extends AppCompatActivity {
    private int selectPosition = 0;
    //--------------------------------------------------------------------------------------------
    public DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {


            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            String dateInString = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;

            Date date = null;
            try {
                date = sdf.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(selectPosition, date))
                    .commit();


        }
    };
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Section 2",
                        "Section 3",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, new Date()))
                        .commit();
                selectPosition = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_curs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            calendar = Calendar.getInstance();

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
            datePickerDialog.show();

            return true;
        } else if (id == R.id.convertor_valutar) {

            Intent covertActivity = new Intent(CursActivity.this, ConvertorActivity.class);
            startActivity(covertActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
    //--------------------------------------------------------------------------------------------

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Date date;
        private RecyclerView recyclerView;
        private ExchangeAdapter mAdapter;
        private List<ExchangeDetails> exchangeFormList = new ArrayList<>();

        //final Adapter mAdapter = null;
        public PlaceholderFragment() {

        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Date date) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.date = date;
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_curs, container, false);

            //mListView = (ListView) rootView.findViewById(R.id.listView);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

            mAdapter = new ExchangeAdapter(exchangeFormList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setMinimumHeight(3000);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));


            recyclerView.setAdapter(mAdapter);
            final int position = getArguments().getInt(ARG_SECTION_NUMBER);

            new RetrieveFeedTask(date, position + 1).execute();

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);


            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            return rootView;
        }

        public final class RetrieveFeedTask extends AsyncTask<Void, Void, ExchangeRateDetails> {

            private Date date;
            private ProgressDialog progressDialog;
            private int selectPosition;

            public RetrieveFeedTask(Date date, int selectPosition) {
                this.date = date;
                this.selectPosition = selectPosition;
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
            protected ExchangeRateDetails doInBackground(Void... position) {

                if (date == null) {
                    date = new Date();
                }


                final String url = UrlConstant.findExchangeRateByDate;

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application", "json"));
                ExchangeRateForm exchangeRateForm = new ExchangeRateForm();
                exchangeRateForm.setDateCurs(date);

                final HttpEntity<ExchangeRateForm> requestEntity = new HttpEntity<>(exchangeRateForm, requestHeaders);

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


                if (exchangeRateDetails != null) {

                    if (exchangeRateDetails.getReturnCode() == 0) {

                        for (ExchangeRateList exchangeRateList : exchangeRateDetails.getExchangeRateList()) {
                            ExchangeDetails exchangeForm = new ExchangeDetails();

                            exchangeForm.setValuta(exchangeRateList.getValutaShortName());
                            exchangeForm.setCumparare(exchangeRateList.getBuyCurs());
                            exchangeForm.setVinzare(exchangeRateList.getSellCurs());
                            exchangeForm.setImagePath(exchangeRateList.getImagePath());
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
                progressDialog.dismiss();
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
}


