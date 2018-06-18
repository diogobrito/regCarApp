package br.com.fiap.regcar.api;


import br.com.fiap.regcar.model.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginAPI {

    @POST("/login")
    Call<Login> salvar(@Body Login login );

    @GET(value = "/login/validarsenha/{usuario}/{senha}")
    Call<Login> verSenha(@Path(value = "usuario") String usuario, @Path(value = "senha") String senha);

    @GET(value = "/login/usuario/{usuario}")
    Call<Login> verUsuario(@Path(value = "usuario") String usuario);
}
