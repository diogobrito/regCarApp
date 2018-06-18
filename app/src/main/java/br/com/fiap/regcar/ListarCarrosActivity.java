package br.com.fiap.regcar;

import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.fiap.regcar.api.LoginAPI;
import br.com.fiap.regcar.api.RegCarAPI;
import br.com.fiap.regcar.model.Carro;
import br.com.fiap.regcar.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListarCarrosActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etIdLogin;

    RecyclerView mRecyclerView;
    //private LIneAdapter mAdapter;

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_compras);

        Bundle bundle = getIntent().getExtras();
        String usuario = bundle.getString("usuario");


        final ProgressDialog dialog = ProgressDialog.show(ListarCarrosActivity.this, "",
                getString(R.string.carregando_lista), true);

        dialog.show();

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etIdLogin = (EditText) findViewById(R.id.etIdLogin);

        LoginAPI api = getRetrofit().create(LoginAPI.class);

        api.verUsuario(usuario)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> verUsuario, Response<Login> response) {
                        if(response.isSuccessful()) {
                            Login login = response.body();
                            etUsuario.setText(login.getUsuario());
                            etIdLogin.setText(login.getId());

                            listarCarros(login.getId());
                        }
                        dialog.dismiss();


                    }

                    @Override
                    public void onFailure(Call<Login> verUsuario, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(ListarCarrosActivity.this,
                                R.string.erro_carga_lista, Toast.LENGTH_LONG).show();


                    }
                });

    }

    private void listarCarros(String idLogin) {

        RegCarAPI api = getRetrofit().create(RegCarAPI.class);

        api.listaCarros(idLogin)
                .enqueue(new Callback<List<Carro>>() {
                    @Override
                    public void onResponse(Call<List<Carro>> listaCarros, Response<List<Carro>> response) {
                        if(response.isSuccessful()) {

                           List<Carro> listaCarro = response.body();

                            for (int i = 0; i< listaCarro.size(); i++) {
                                System.out.println("Carro: " + listaCarro.get(i).getId() + "|" + listaCarro.get(i).getNome() + "|" + listaCarro.get(i).getPlaca() + "|" );
                            }
                            exibirListaCarros(listaCarro);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Carro>> listaCarros, Throwable t) {
                        Toast.makeText(ListarCarrosActivity.this,
                                R.string.erro_carga_lista_compra, Toast.LENGTH_LONG).show();


                    }
                });


    }

    private void exibirListaCarros(List<Carro> listaCarros) {

    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://reg-car.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
