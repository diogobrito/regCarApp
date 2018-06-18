package br.com.fiap.regcar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.fiap.regcar.R;

public class CarroViewHolder extends RecyclerView.ViewHolder {
    TextView txViewNome;
    TextView txViewPlaca;

    public CarroViewHolder(View itemView) {
        super(itemView);

        txViewNome = (TextView) itemView.findViewById(R.id.txNome);
        txViewPlaca = (TextView) itemView.findViewById(R.id.txPlaca);
    }
}
