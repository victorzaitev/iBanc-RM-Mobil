package rm.ibanc.md.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rm.ibanc.md.entites.view.ExchangeDetails;
import rm.ibanc.md.ibanc_rm.R;

/**
 * Created by victor.zaitev on 07.12.2016.
 */
public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.MyViewHolder> {

    private List<ExchangeDetails> exchangeDetailsList;

    public ExchangeAdapter(List<ExchangeDetails> exchangeDetailsList) {
        this.exchangeDetailsList = exchangeDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exchange_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ExchangeDetails exchangeForm = exchangeDetailsList.get(position);

        if (exchangeForm.getImagePath()) {
            holder.imageExchange.setImageResource(R.drawable.arrow_true);
        } else {
            holder.imageExchange.setImageResource(R.drawable.arrow_false);
        }
        holder.valuta.setText(exchangeForm.getValuta());
        holder.cumparare.setText(exchangeForm.getCumparare().toString());
        holder.vinzare.setText(exchangeForm.getVinzare().toString());
        holder.cursBnm.setText(exchangeForm.getCursBnm().toString());
    }

    @Override
    public int getItemCount() {
        return exchangeDetailsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageExchange;
        private TextView valuta;
        private TextView cumparare;
        private TextView vinzare;
        private TextView cursBnm;

        public MyViewHolder(View view) {
            super(view);
            imageExchange = (ImageView) view.findViewById(R.id.image_exchange);
            valuta = (TextView) view.findViewById(R.id.valuta);
            cumparare = (TextView) view.findViewById(R.id.cumparare);
            vinzare = (TextView) view.findViewById(R.id.vinzare);
            cursBnm = (TextView) view.findViewById(R.id.curs_bnm);
        }
    }
}