package br.com.fiap.regcar;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fiap.regcar.adapter.CarroRecyclerAdapter;
import br.com.fiap.regcar.api.RegCarAPI;
import br.com.fiap.regcar.model.Carro;
import br.com.fiap.regcar.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AltCarActivity extends AppCompatActivity {
    private TextView etxNome;
    private EditText etPlaca;
    private ConstraintLayout layoutContentAddCar;
    private Carro carro;
    private EditText etUsuario;
    private String usuario;
    public CarroRecyclerAdapter mAdapter;
    private Carro carroAlterar;
    public  Login login;
    private Integer position;
    private String nomeAntigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            Bundle bundle = getIntent().getExtras();
            String nome = bundle.getString("nome");
            nomeAntigo = nome;
            String placa = bundle.getString("placa");
            position = bundle.getInt("position");


            etxNome = (TextView) findViewById(R.id.etxNome);
            etxNome.setText(nome);


            etPlaca = (EditText) findViewById(R.id.etPlaca);
            etPlaca.setText(placa);

            mAdapter = MainActivity.getAdapter();

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_menu_incluir_car, menu);

            return super.onCreateOptionsMenu(menu);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch(id) {
                case R.id.action_ok:
                    confirmar();
                    break;
                case R.id.action_cancelar:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

    private void confirmar() {
        incluirVeiculo();
    }


    private void incluirVeiculo() {
        RegCarAPI regCarAPI = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();


        atualizaCarroUser();

    }

    private void atualizaCarroUser() {
        RegCarAPI regCarAPI = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();

        carro.setNome(etxNome.getText().toString());
        carro.setPlaca(etPlaca.getText().toString());
        carro.setLogin(login);

        carroAlterar = carro;

        regCarAPI.alterarCarro(carro)
            .enqueue(new Callback<Carro>() {
                @Override
                public void onResponse(Call<Carro> incluirVeiculo, Response<Carro> response) {
                    Carro carroResponse = response.body();

                    if(response.isSuccessful()) {
                        mAdapter.updateItem(position, carroResponse);
                        MainActivity.setCarroSearch(mAdapter.getListaCarros());
                        finish();
                    }
                    else {
                        System.out.println("voltou alterar com null " + mAdapter);
                        Toast.makeText(AltCarActivity.this,
                                R.string.erro_alterar_carro, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Carro> incluirVeiculo, Throwable t) {
                    Toast.makeText(AltCarActivity.this,
                            R.string.erro_alterar_carro, Toast.LENGTH_SHORT).show();
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