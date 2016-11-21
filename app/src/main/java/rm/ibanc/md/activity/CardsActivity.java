package rm.ibanc.md.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rm.ibanc.md.adapter.AccountAdapter;
import rm.ibanc.md.entites.rest.CardsDetails;
import rm.ibanc.md.entites.rest.CardsList;
import rm.ibanc.md.helper.SessionManager;
import rm.ibanc.md.ibanc_rm.R;
import rm.ibanc.md.recyclerview.ClickListener;
import rm.ibanc.md.recyclerview.DividerItemDecoration;
import rm.ibanc.md.recyclerview.RecyclerTouchListener;

public class CardsActivity extends AppCompatActivity {

    CardsDetails cardsDetails;
    private List<CardsList> cardsFormList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AccountAdapter mAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = SessionManager.getInstance();
        cardsDetails = (CardsDetails) getIntent().getSerializableExtra("CardsDetailsList");
//


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

        prepareMovieData(cardsDetails.getCardsList());
        mAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CardsList cardsForm = cardsFormList.get(position);
                Toast.makeText(getApplicationContext(), cardsForm.getPan() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                CardsList cardsForm = cardsFormList.get(position);


                PopupMenu popupMenu = new PopupMenu(CardsActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_cards_menu, popupMenu.getMenu());
                //registering popup with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(CardsActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();//showing popup menu


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


    @Override
    protected void onResume() {
        super.onResume();
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(CardsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }


    //------------------------------------->
    private void prepareMovieData(List<CardsList> cardsLists) {


        //cardsFormList = cardsLists;
        for (CardsList cardList : cardsLists) {

            CardsList accountForm = new CardsList();

            accountForm.setTypes(cardList.getTypes());
            accountForm.setStatus(cardList.getStatus());
            accountForm.setPan(cardList.getPan());
            accountForm.setDescription(cardList.getDescription());
            accountForm.setMaskPan(cardList.getMaskPan());

            cardsFormList.add(accountForm);
        }

    }
}
