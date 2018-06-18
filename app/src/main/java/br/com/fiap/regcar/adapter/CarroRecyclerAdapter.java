package br.com.fiap.regcar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.regcar.R;
import br.com.fiap.regcar.model.Carro;

public class CarroRecyclerAdapter extends RecyclerView.Adapter<CarroViewHolder> {
    private List<Carro> mLista;
    public static int selected_item = -1;

    public static SparseBooleanArray selectedItems;
    public CarroRecyclerAdapter(List<Carro> lista) {
        mLista = lista;
    }
    public List<Carro> getListaCarros() {
       return mLista;
    }

    public CarroRecyclerAdapter() {
    }


    @Override
    public CarroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_carros, parent, false);
        return new CarroViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(CarroViewHolder holder, int position) {
        Carro carro = mLista.get(position);
        holder.txViewNome.setText(carro.getNome());
        holder.txViewPlaca.setText(carro.getPlaca());
    }

    @Override
    public int getItemCount() {
        return mLista != null ? mLista.size() : 0;
    }

    public void setfilter(List<Carro> carroItem) {
        List<Carro> mListaSearch = new ArrayList<>();
        mListaSearch.addAll(carroItem);
        notifyDataSetChanged();
    }

    public Carro getItem(int position) {
        return mLista.get(position);
    }

    public void updateList(Carro carro) {
        insertItem(carro);
    }

    private void insertItem(Carro carro) {
        mLista.add(carro);
        notifyItemInserted(getItemCount());
    }


    public void updateItem(int position, Carro carroAlterado) {
        Carro carro = mLista.get(position);
        carro.setNome(carroAlterado.getNome());
        carro.setPlaca(carroAlterado.getPlaca());
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }
}