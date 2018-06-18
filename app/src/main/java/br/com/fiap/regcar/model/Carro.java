package br.com.fiap.regcar.model;



public class Carro {

    private String id;
    private String nome;
    private String placa;
    private Login login;

    public Carro() {

    }

    public Carro(String id, String nome, String placa, Login login) {
        this.id = id;
        this.nome = nome;
        this.placa = placa;
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
