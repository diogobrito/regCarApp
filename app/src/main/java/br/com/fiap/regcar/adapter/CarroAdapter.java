package br.com.fiap.regcar.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.fiap.regcar.model.Carro;
import br.com.fiap.regcar.R;


public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.ViewHolderCarro> {

    private List<Carro> carros;
    public static int item_selecionado =0;

    public CarroAdapter(List<Carro> carros) {
        this.carros = carros;
    }

    @Override
    public ViewHolderCarro onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_carros, parent, false);
        ViewHolderCarro holderCarro = new ViewHolderCarro((view));

        return holderCarro;
    }

    @Override
    public void onBindViewHolder(ViewHolderCarro holder, int position) {

        int i = 0;
        for (i = 0 ; i < carros.size(); i++) {

            if (i == position ) {
                Carro carro = carros.get(position);
            }
        }

        if ((carros != null) && (carros.size() > 0)) {
            Carro carro = carros.get(position);
            holder.txNome.setText(carro.getNome());
            holder.txPlaca.setText(carro.getPlaca());
        }

        if (position == item_selecionado) {
            holder.txNome.setTextColor(Color.RED);
        }
        else {
            holder.txNome.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("entrou count");
        return carros.size();
    }

    public static class ViewHolderCarro extends RecyclerView.ViewHolder {

        public TextView txNome;
        public TextView txPlaca;

        public ViewHolderCarro(View v) {
            super(v);

            txNome = (TextView) v.findViewById(R.id.txNome);
            txNome = (TextView) v.findViewById(R.id.txPlaca);


        }
    }

}
