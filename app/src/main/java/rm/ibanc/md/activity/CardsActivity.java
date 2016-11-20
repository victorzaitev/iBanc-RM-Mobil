package rm.ibanc.md.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rm.ibanc.md.adapter.AccountAdapter;
import rm.ibanc.md.entites.form.AccountForm;
import rm.ibanc.md.entites.rest.CardsList;
import rm.ibanc.md.ibanc_rm.R;
import rm.ibanc.md.recyclerview.ClickListener;
import rm.ibanc.md.recyclerview.DividerItemDecoration;
import rm.ibanc.md.recyclerview.RecyclerTouchListener;

public class CardsActivity extends AppCompatActivity {

    private List<CardsList> cardsFormList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AccountAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //-------------------------------------->
        recyclerView = (RecyclerView) findViewById(R.id.recycler_cards_view);
        mAdapter = new AccountAdapter(cardsFormList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setMinimumHeight(3000);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
        mAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CardsList cardsForm = cardsFormList.get(position);
                Toast.makeText(getApplicationContext(), cardsForm.getDescription() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        //---------------------------------------------->


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    //------------------------------------->
    private void prepareMovieData() {
        CardsList accountForm = new CardsList("MasterCard", "Activ", "1234567890123456", "Card Salarial");
        cardsFormList.add(accountForm);

        accountForm = new CardsList("Visa","Blocat", "8529637419456321", "Depozit");
        cardsFormList.add(accountForm);

        accountForm = new CardsList("Visa", "Expirat", "9517538524569874", "Card de credit");
        cardsFormList.add(accountForm);

        accountForm = new CardsList("Visa", "Activ", "9514789564123654", "Depozit");
        cardsFormList.add(accountForm);

        accountForm = new CardsList("MasterCard", "Activ", "9578965411326547", "Card salarial");
        cardsFormList.add(accountForm);

    }
}
