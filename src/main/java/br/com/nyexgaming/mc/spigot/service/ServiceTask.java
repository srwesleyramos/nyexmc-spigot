package br.com.nyexgaming.mc.spigot.service;

import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class ServiceTask extends Thread {

    public final Service service;

    public ServiceTask(Service service) {
        super("NyexGaming Task #1");

        this.service = service;
    }

    @Override
    public void run() {
        while (this.isAlive()) {
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException ignored) {
            }

            if (service.sdk == null) continue;

            try {
                Transaction[] transactions = service.sdk.getTransactions();

                Field[] fields = Transaction.class.getFields();

                Shopping[] shopping = new Shopping[transactions.length];

                for (int i = 0; i < shopping.length; i++) {
                    shopping[i] = new Shopping();

                    for (Field field : fields) {
                        try {
                            field.setAccessible(true);
                            field.set(shopping[i], field.get(transactions[i]));
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                }

                service.getExecutor().execute(shopping);
            } catch (NetworkErrorException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Spigot] [ERROR]: §cNão foi possível conectar a API, verifique a sua conexão.");
            } catch (RequestFailedException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Spigot] [ERROR]: §cOps... Aparentemente o seu plugin está com uma versão antiga, verifique se há atualizações!");
            } catch (TokenFailureException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Spigot] [ERROR]: §cEita! As credenciais de autenticação que você forneceu são inválidas, verifique e recarregue o plugin!");

                service.sdk = null;
            }
        }
    }
}
