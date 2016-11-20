package rm.ibanc.md.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rm.ibanc.md.entites.form.AccountForm;
import rm.ibanc.md.entites.rest.CardsList;
import rm.ibanc.md.ibanc_rm.R;

/**
 * Created by PC01017745 on 21.10.2016.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private List<CardsList> cardsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView types;
        private TextView status;
        private TextView pan;
        private TextView description;

        public MyViewHolder(View view) {
            super(view);
            types = (ImageView) view.findViewById(R.id.imageView2);
            status = (TextView) view.findViewById(R.id.status);
            pan = (TextView) view.findViewById(R.id.pan);
            description = (TextView) view.findViewById(R.id.description);
        }
    }


    public AccountAdapter(List<CardsList> cardsList) {
        this.cardsList = cardsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CardsList accountForm = cardsList.get(position);


        if (accountForm.getTypes().equals("MasterCard")) {
            holder.types.setImageResource(R.drawable.list_mc);
        } else if (accountForm.getTypes().equals("Visa")) {
            holder.types.setImageResource(R.drawable.list_visa);
        }

        holder.status.setText(accountForm.getStatus());
        holder.pan.setText(accountForm.getPan());
        holder.description.setText(accountForm.getDescription());
    }


    @Override
    public int getItemCount() {
        return cardsList.size();
    }


}
