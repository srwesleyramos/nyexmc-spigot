package br.com.nyexgaming.mc.spigot.modules.service;

import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;

public class ServiceTask extends Thread {

    private final Service service;

    public ServiceTask(Service service) {
        super("NyexTask #1");

        this.service = service;
    }

    @Override
    public void run() {
        while (this.service.getSdk() != null && this.isAlive()) {
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException ignored) {
            }

            try {
                this.service.getExecutor().execute(this.service.getSdk().getTransactions());
            } catch (NetworkErrorException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming] [ERROR]: §cNão foi possível conectar a API, verifique a sua conexão.");
            } catch (RequestFailedException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming] [ERROR]: §cOps... Aparentemente o seu plugin está com uma versão antiga, verifique se há atualizações!");
            } catch (TokenFailureException e) {
                Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming] [ERROR]: §cEita! As credenciais de autenticação que você forneceu são inválidas, verifique e recarregue o plugin!");
                break;
            }
        }
    }
}
