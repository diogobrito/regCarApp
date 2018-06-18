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

public class EditCarActivity extends AppCompatActivity {
    private TextView etNome;
    private EditText etPlaca;
    private ConstraintLayout layoutContentAddProd;
    private Carro carro;
    private EditText etUsuario;
    private String usuario;
    public CarroRecyclerAdapter mAdapter;
    private Carro carroAlterar;
    public Login login;
    private Integer position;
    private String nomeAntigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        String nome = bundle.getString("nome");
        nomeAntigo = nome;
        String placa = bundle.getString("placa");
        position = bundle.getInt("position");

        etNome = (TextView) findViewById(R.id.etNome);
        etNome.setText(nome);


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

        alterarCarro();

    }


    private void alterarCarro() {
        RegCarAPI apiCarro = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();


        alterarBaseDados();


    }

    private void alterarBaseDados() {
        RegCarAPI apiCarro = getRetrofit().create(RegCarAPI.class);
        Carro carro = new Carro();
        login = LoginActivity.getLogin();

        carro.setNome(etNome.getText().toString());
        carro.setPlaca(etPlaca.getText().toString());
        carro.setLogin(login);


        carroAlterar = carro;

        System.out.println("alterarCarro");
        apiCarro.alterarCarro(carro)   //(nomeAntigo, carro, login.getId())
                .enqueue(new Callback<Carro>() {
                    @Override
                    public void onResponse(Call<Carro> alterarCarro, Response<Carro> response) {
                        Carro carroResponse = response.body();

                        if(response.isSuccessful()) {

                            mAdapter.updateItem(position, carroResponse);
                            Toast.makeText(EditCarActivity.this,
                                    R.string.alterado_sucesso, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {

                            Toast.makeText(EditCarActivity.this,
                                    R.string.erro_alterar, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Carro> alterarCarro, Throwable t) {


                        Toast.makeText(EditCarActivity.this,
                                R.string.erro_alterar, Toast.LENGTH_SHORT).show();
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
