package br.com.fiap.regcar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.regcar.api.LoginAPI;
import br.com.fiap.regcar.model.Carro;
import br.com.fiap.regcar.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etSenha;
    public List<Carro> carrosMain;
    private static Login login;

    public static Login getLogin() {
        return LoginActivity.login;
    }

    public static void setLogin(Login login) {
        LoginActivity.login = login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        carrosMain = new ArrayList<Carro>();



    }


    private boolean validaDados(EditText etUsuario, EditText etSenha) {
        if (etUsuario.getText().toString().length() == 0) {
            etUsuario.setError(getString(R.string.digite_usuario));
            return false;
        }
        if (etSenha.getText().toString().length() == 0) {
            etSenha.setError(getString(R.string.digite_senha));
            return false;
        }

        return true;
    }

    public void criarUsuario(View v) {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);

        Intent proximaTela = new Intent(LoginActivity.this,
                CriarLoginActivity.class);

        proximaTela.putExtra("usuario", etUsuario.getText().toString());
        startActivity(proximaTela);
        LoginActivity.this.finish();

    }


    public void verificarSenha(View v) {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);

        if (!validaDados(etUsuario,etSenha)) return;

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                getString(R.string.validando_usuario), true);

        dialog.show();

        LoginAPI api = getRetrofit().create(LoginAPI.class);


        System.out.println("Usuario " + etUsuario.getText().toString() );

        Login login = new Login();
        login.setSenha(etSenha.getText().toString());
        login.setUsuario(etUsuario.getText().toString());

        api.verSenha(etUsuario.getText().toString(), etSenha.getText().toString())
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> verSenha, Response<Login> response) {
                        dialog.dismiss();
                        if(response.isSuccessful()) {
                            Login loginResponse = response.body();

                            LoginActivity.login = loginResponse;

                            SharedPreferences prefs = getSharedPreferences("pref_lista_carros",0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("usuarioLogado", true);
                            editor.putString("idLogin", loginResponse.getId());
                            editor.putString("nomeCompleto", loginResponse.getNome());
                            editor.putString("usuario", loginResponse.getUsuario());
                            editor.commit();

                            Intent proximaTela = new Intent(LoginActivity.this,
                                    MainActivity.class);

                            System.out.println("etusuario LoginActivity: " + etUsuario.getText().toString());
                            proximaTela.putExtra("usuario", etUsuario.getText().toString());
                            startActivity(proximaTela);
                            LoginActivity.this.finish();


                        }

                    }

                    @Override
                    public void onFailure(Call<Login> verSenha, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                R.string.usuario_senha_invalido, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void sair(DialogInterface dialog, int wich) {
        finish();
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://reg-car.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
