package br.com.fiap.regcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.fiap.regcar.model.Login;

public class SplashScreen extends AppCompatActivity {

    private TextView txtBemVindo;

    private final int SPLASH_DISPLAY_LENGHT = 3500;

    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        carregar();
    }

    private void carregar() {

        Animation animacaoLogo = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        animacaoLogo.reset();

        ImageView iv = (ImageView) findViewById(R.id.ivLogo);

        if (iv != null) {

            iv.clearAnimation();
            iv.startAnimation(animacaoLogo);
        }

        SharedPreferences prefs = getSharedPreferences("pref_lista_compras", 0);
        final boolean isLogado = prefs.getBoolean("usuarioLogado", false);
        String nomeCompleto = prefs.getString("nomeCompleto", "");
        String idLogin = prefs.getString("idLogin", "");
        String usuario = prefs.getString("usuario", "");
        txtBemVindo = (TextView) findViewById(R.id.txtBemVindo);


        if (isLogado) {
            txtBemVindo.setText( (getString(R.string.ola) + ", "+ nomeCompleto + "!"));
            Login login = new Login();
            login.setNome(nomeCompleto);
            login.setId(idLogin);
            login.setUsuario(usuario);
            LoginActivity.setLogin(login);

        }
        else {
            txtBemVindo.setText(R.string.bem_vindo);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isLogado) {

                    Intent proximaTela = new Intent(SplashScreen.this,
                            MainActivity.class);

                    proximaTela.putExtra("usuario", LoginActivity.getLogin().getUsuario());
                    startActivity(proximaTela);
                    SplashScreen.this.finish();

                }
                else {
                    Intent proximaTela = new Intent(SplashScreen.this,
                            LoginActivity.class);
                    startActivity(proximaTela);
                    SplashScreen.this.finish();

                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }


}
