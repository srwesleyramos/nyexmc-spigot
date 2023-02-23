package br.com.nyexgaming.mc.spigot.database.models;

import br.com.nyexgaming.sdk.endpoints.products.Product;

public class DeliveryModel {

    private long id_transacao;
    private long id_cupom;
    private long id_loja;

    private int status;
    private int entregue;

    private String identificador;
    private String email;

    private String gateway;
    private double valor;
    private String hex_transacao;
    private String external_reference;

    private Product[] produtos;
    private long criado_em;
    private long atualizado_em;

    private boolean presente;
    private String destinatario;

    public DeliveryModel(long id_transacao, long id_cupom, long id_loja, int status, int entregue, String identificador, String email, String gateway, double valor, String hex_transacao, String external_reference, Product[] produtos, long criado_em, long atualizado_em, boolean presente, String destinatario) {
        this.id_transacao = id_transacao;
        this.id_cupom = id_cupom;
        this.id_loja = id_loja;
        this.status = status;
        this.entregue = entregue;
        this.identificador = identificador;
        this.email = email;
        this.gateway = gateway;
        this.valor = valor;
        this.hex_transacao = hex_transacao;
        this.external_reference = external_reference;
        this.produtos = produtos;
        this.criado_em = criado_em;
        this.atualizado_em = atualizado_em;
        this.presente = presente;
        this.destinatario = destinatario;
    }

    public long getId_transacao() {
        return id_transacao;
    }

    public void setId_transacao(long id_transacao) {
        this.id_transacao = id_transacao;
    }

    public long getId_cupom() {
        return id_cupom;
    }

    public void setId_cupom(long id_cupom) {
        this.id_cupom = id_cupom;
    }

    public long getId_loja() {
        return id_loja;
    }

    public void setId_loja(long id_loja) {
        this.id_loja = id_loja;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEntregue() {
        return entregue;
    }

    public void setEntregue(int entregue) {
        this.entregue = entregue;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getHex_transacao() {
        return hex_transacao;
    }

    public void setHex_transacao(String hex_transacao) {
        this.hex_transacao = hex_transacao;
    }

    public String getExternal_reference() {
        return external_reference;
    }

    public void setExternal_reference(String external_reference) {
        this.external_reference = external_reference;
    }

    public Product[] getProdutos() {
        return produtos;
    }

    public void setProdutos(Product[] produtos) {
        this.produtos = produtos;
    }

    public long getCriado_em() {
        return criado_em;
    }

    public void setCriado_em(long criado_em) {
        this.criado_em = criado_em;
    }

    public long getAtualizado_em() {
        return atualizado_em;
    }

    public void setAtualizado_em(long atualizado_em) {
        this.atualizado_em = atualizado_em;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
}
