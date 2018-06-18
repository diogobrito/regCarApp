package br.com.fiap.regcar;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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

public class AddCarActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etplaca;
    private ConstraintLayout layoutContentAddCar;
    private Carro carro;
    private EditText etUsuario;
    private String usuario;
    public CarroRecyclerAdapter mAdapter;
    private Carro carroIncluir;
    public  Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        etNome = (EditText) findViewById(R.id.etNome);
        etplaca = (EditText) findViewById(R.id.etPlaca);
        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");

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
        if (validaCampos() == true) {
            incluirVeiculo();
        }
    }

    private void pesquisaCarro() {
        RegCarAPI regCarAPI = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();

        carro.setNome(etNome.getText().toString());
        carro.setPlaca(etplaca.getText().toString());
        carro.setLogin(login);

        carroIncluir = carro;

        regCarAPI.buscarCarro(etNome.getText().toString(), login.getId())
            .enqueue(new Callback<Carro>() {
                @Override
                public void onResponse(Call<Carro> buscarCarro, Response<Carro> response) {
                    if(response.isSuccessful())

                    mAdapter.updateList(carroIncluir);
                    Toast.makeText(AddCarActivity.this,
                            R.string.gravado_sucesso, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Carro> salvar, Throwable t) {

                    Toast.makeText(AddCarActivity.this,
                            R.string.erro_salvar_carro, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void incluirVeiculo() {
        RegCarAPI regcarAPI = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();

        carro.setNome(etNome.getText().toString());
        carro.setPlaca(etplaca.getText().toString());
        carro.setLogin(login);

        carroIncluir = carro;

        regcarAPI.salvar(carro)
                .enqueue(new Callback<Carro>() {
                    @Override
                    public void onResponse(Call<Carro> salvar, Response<Carro> response) {
                        Carro carroResponse = response.body();

                        if(response.isSuccessful()) {

                            mAdapter.updateList(carroIncluir);
                            MainActivity.setCarroSearch(mAdapter.getListaCarros()); //para o Search
                            Toast.makeText(AddCarActivity.this,
                                    R.string.gravado_sucesso, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(AddCarActivity.this,
                                    R.string.carro_existe, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Carro> salvar, Throwable t) {

                        Toast.makeText(AddCarActivity.this,
                                R.string.erro_ja_existe, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private boolean validaCampos() {
        String nome = etNome.getText().toString();
        if (isCampoVazio(nome)) {
            etNome.requestFocus();

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage(R.string.digite_carro);
            dlg.setNeutralButton("OK", null);
            dlg.show();
            return false;
        }

        return true;
    }

    private boolean isCampoVazio(String valor) {
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://reg-car.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
