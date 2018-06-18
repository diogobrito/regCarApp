package br.com.fiap.regcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.fiap.regcar.adapter.CarroRecyclerAdapter;
import br.com.fiap.regcar.adapter.RecyclerOnItemClickListener;
import br.com.fiap.regcar.api.RegCarAPI;
import br.com.fiap.regcar.model.Carro;
import br.com.fiap.regcar.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fab;
    private ConstraintLayout layoutContentMain;
    private EditText etUsuario;
    private String usuario;
    List<Carro> carrosMain;
    private static List<Carro> carrosSearches;
    public CarroRecyclerAdapter mAdapter;
    private static CarroRecyclerAdapter mAdapterCarro;  // para passar para o AddProd
    public View selectedView;
    public Integer positionList;
    public Login login;
    SearchView searchView;
    private Date horarioSelectItem;

    public static CarroRecyclerAdapter getAdapter() {
        return MainActivity.mAdapterCarro;
    }

    public static void setCarroSearch(List<Carro> carros) {
        carrosSearches = carros;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("entrou oncreateMain");

        fab = (FloatingActionButton) findViewById(R.id.fab);
//        listar();


        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");

        //       System.out.println("antes recuperalogion");

        login = LoginActivity.getLogin();
        listar(login);

        System.out.println("encerra oncreateMain");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_pesquisar);
        searchView = (SearchView) myActionMenuItem.getActionView();

        System.out.println("entrou createmenuitem");

//        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("entrou na queryTextChange : " + s);

                List<Carro> prod1 = mAdapter.getListaCarros();
                for (Carro prod: prod1) {
                    System.out.println("Carro da mLista: " + prod.getNome());
                }

                final List<Carro> filtermodelist = filter(MainActivity.carrosSearches, s);
       //         mAdapter.setfilter(filtermodelist);
                for (Carro prod: filtermodelist) {
                     System.out.println("Carro do Filter: " + prod.getNome());
                }

                atualizaAdapter(filtermodelist);
                return false;
            }
        });

        return true;

    }

    private List<Carro> filter(List<Carro> p1, String query) {
        query = query.toUpperCase();
        final List<Carro> filteredModeList = new ArrayList<>();

        for (Carro model:p1) {
            final String text=model.getNome().toUpperCase();
            if (text.startsWith(query)) {
                filteredModeList.add(model);
            }
        }

        for (Carro model:filteredModeList) {
            System.out.println("Carro na FilteredModeList: " + model.getNome());
        }
        carrosMain = filteredModeList;

        return filteredModeList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            case R.id.action_sair:

                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.encerrar_sessao);
                builder.setMessage(R.string.pergunta_encerra_sessao);
                builder.setPositiveButton(R.string.title_menu_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                builder.setNegativeButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences prefs = getSharedPreferences("pref_lista_compras",0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("usuarioLogado", false);
                        editor.putString("idLogin", "");
                        editor.putString("nomeCompleto", "");
                        editor.putString("usuario", "" );
                        editor.commit();

                        Login login = new Login();

                        LoginActivity.setLogin(login);

                        Intent proximaTela = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(proximaTela);
                        MainActivity.this.finish();

                    }
                });
                alerta = builder.create();
                alerta.show();

                break;
            case R.id.action_sobre:
                Intent proximaTela = new Intent(MainActivity.this,
                        SobreActivity.class);
                startActivity(proximaTela);

                System.out.println("saiu sobre");

                break;

        }

        return super.onOptionsItemSelected(item);

    }


    private void listar(Login login) {
        ArrayList<Carro> lista = new ArrayList<>();

        RegCarAPI apiCar = getRetrofit().create(RegCarAPI.class);
        List<Carro> carros;

        Toast.makeText(MainActivity.this,
                R.string.gerando_lista, Toast.LENGTH_LONG).show();

        apiCar.listaCarros( login.getId())
                .enqueue(new Callback<List<Carro>>() {
                    @Override
                    public void onResponse(Call<List<Carro>> salvar, Response<List<Carro>> response) {
                        if(response.isSuccessful()) {

                            List<Carro> carros = response.body();
                            int i = 0;
                            for (i = 0; i < carros.size(); i++) {

                            }
                            carrosMain = carros;
                            carrosSearches = carros;
                            atualizaAdapter(carrosMain);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Carro>> salvar, Throwable t) {

                        Toast.makeText(MainActivity.this,
                                R.string.erro_lista_prod, Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void atualizaAdapter(List<Carro> carros) {

        final RecyclerView recyclerViewCarros = (RecyclerView) findViewById(R.id.lstCarros);
        recyclerViewCarros.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CarroRecyclerAdapter(buscarCarros());
        recyclerViewCarros.setAdapter(mAdapter);

        mAdapterCarro = mAdapter;

        recyclerViewCarros.addOnItemTouchListener(new RecyclerOnItemClickListener(this, new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemClicado(position, view);

            }
        }));


    }


    private ArrayList<Carro> buscarCarros() {
        ArrayList<Carro> lista = new ArrayList<>();

        int i = 0;
        for (i=0; i < carrosMain.size(); i++) {

            Carro carro = new Carro();

            carro.setNome(carrosMain.get(i).getNome());
            carro.setPlaca(carrosMain.get(i).getPlaca());

            lista.add(carro);

        }

        return lista;
    }

    private void onItemClicado(int position, View view) {
        Carro carro = mAdapter.getItem(position);


        if (horarioSelectItem != null) {
            Calendar horaAtual = Calendar.getInstance();
            horaAtual.setTime(new Date());

            Calendar horaSelectWait = Calendar.getInstance();
            horaSelectWait.setTime(horarioSelectItem);
            horaSelectWait.add(Calendar.SECOND,2);

            if (horaSelectWait.getTime().before(horaAtual.getTime())) {
            }
            else {
                return;
            }

        }

        horarioSelectItem = new Date();

        List<Carro> prods= mAdapter.getListaCarros();
        for (Carro prod:prods ) {
            System.out.println("Lista da mAdapter: " + prod.getNome());
            System.out.println("Lista da mAdapter: " + prod.getNome());
        }

//voltar        selected_item = position;

        System.out.println("Valor Inicial do selected_item: " + CarroRecyclerAdapter.selected_item);

        if (selectedView != null) {
            System.out.println("SelectedView is not null - coloca false: " + position );
            selectedView.setSelected(false);
//            view.setSelected(false);
        //    selectedView.setBackgroundResource(R.color.background_splash);
        }
        if (selectedView == view & CarroRecyclerAdapter.selected_item >= 0) {
            System.out.println("SelectedView = view - coloca false: " + position );
            view.setSelected(false);
//            view.setBackgroundResource(R.color.background_splash);
            CarroRecyclerAdapter.selected_item = -1; //significa que nesse momento ele desmarcou o botáo e náo tem item selecionado para alterar ou excluir
        }
        else {
            System.out.println("SelectedView <> view coloca true: " + position );
            System.out.println("marcando item: " + CarroRecyclerAdapter.selected_item);
  //          view.setBackgroundResource(R.color.background_selected);
            CarroRecyclerAdapter.selected_item = position;
            view.setSelected(true);

        }
        selectedView = view;

        System.out.println("Valor Final do selected_item: " + CarroRecyclerAdapter.selected_item);

        Toast.makeText(getApplicationContext(), carro.getNome(), Toast.LENGTH_LONG).show();


    }


    public void incluirCarro(View view) {
        Intent proximaTela = new Intent(MainActivity.this,
                AddCarActivity.class);

        proximaTela.putExtra("usuario", usuario);
        proximaTela.putExtra( "mAdapter", mAdapter.getClass());
        startActivity(proximaTela);

    }

    public void excluirCarro(View view) {

        if (CarroRecyclerAdapter.selected_item <0) {

            Toast.makeText(getApplicationContext(), (getString(R.string.selecionar_carro_excluir)) , Toast.LENGTH_LONG).show();
        }
        else {

            Carro carro = mAdapter.getItem(CarroRecyclerAdapter.selected_item);

            final String nomeCarro = carro.getNome();


            AlertDialog alerta;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.excluir_carro);
            builder.setMessage(getString(R.string.pergunta_excluir_carro) + carro.getNome() + "?");
            builder.setPositiveButton(R.string.cancelar_excluir, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            builder.setNegativeButton(R.string.sim, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    excluirBaseDados(nomeCarro, login.getId());

                }
            });
            alerta = builder.create();
            alerta.show();


        }

    }

    public void alterarCarro(View view) {

        if (CarroRecyclerAdapter.selected_item < 0) {
            Toast.makeText(getApplicationContext(), (getString(R.string.selecionar_para_alterar)) , Toast.LENGTH_LONG).show();
        }
        else {
            Carro carro = mAdapter.getItem(CarroRecyclerAdapter.selected_item);


            Intent proximaTela = new Intent(MainActivity.this,
                    AltCarActivity.class);


            proximaTela.putExtra("nome", carro.getNome());
            proximaTela.putExtra("placa", carro.getPlaca());
            proximaTela.putExtra("position", CarroRecyclerAdapter.selected_item);
            proximaTela.putExtra( "mAdapter", mAdapter.getClass());
            startActivity(proximaTela);

        }

    }



    private void excluirBaseDados(String nome, String id) {
        RegCarAPI apiProduto = getRetrofit().create(RegCarAPI.class);

        apiProduto.excluirCarro(nome, id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> excluirCarros, Response<Void> response) {
                        if(response.isSuccessful()) {

                            System.out.println("Excluiu produto " + login.getUsuario());
                            mAdapter.deleteItem(CarroRecyclerAdapter.selected_item);
                            carrosSearches = mAdapterCarro.getListaCarros();

                        }
                        else {
                        }


                    }

                    @Override
                    public void onFailure(Call<Void> excluirCarros, Throwable t) {

                    }
                });


    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://reg-car.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}