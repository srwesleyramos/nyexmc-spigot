package br.com.nyexgaming.mc.spigot.database.models;

import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;

public class Shopping extends Transaction {

    public boolean donated;
    public String recipient;

    public Shopping() {
    }

    public Shopping delivered(int entregue) {
        this.entregue = entregue;
        return this;
    }

    public Shopping donated(boolean donated, String recipient) {
        this.donated = donated;
        this.recipient = recipient;
        return this;
    }

    public TransactionStatus status() {
        return TransactionStatus.valueOf(this.status);
    }

    public String target() {
        return donated ? recipient : identificador;
    }
}
