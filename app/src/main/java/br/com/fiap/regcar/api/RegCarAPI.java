package br.com.fiap.regcar.api;

import java.util.List;

import br.com.fiap.regcar.model.Carro;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegCarAPI {

    @GET(value = "/carro/carros/{idLogin}")
    Call<List<Carro>> listaCarros(@Path(value = "idLogin") String idLogin);

    @POST("/carro")
    Call<Carro> salvar(@Body Carro carro);

    @DELETE(value = "/carro/nome/{nome}/{idLogin}")
    Call<Void> excluirCarro(@Path(value = "nome") String nome, @Path(value = "idLogin") String idLogin);

    @POST(value = "/carro/altera")
    Call<Carro> alterarCarro(@Body Carro carro) ;

    @GET(value = "/carro/nome/{nome}/{idLogin}")
    Call<Carro> buscarCarro(@Path(value = "nome") String nome, @Path(value = "idLogin") String idLogin);

}
