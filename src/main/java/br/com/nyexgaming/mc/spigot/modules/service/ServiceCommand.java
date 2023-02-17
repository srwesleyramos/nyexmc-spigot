package br.com.nyexgaming.mc.spigot.modules.service;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ServiceCommand implements CommandExecutor, TabCompleter {

    private final Service service;

    public ServiceCommand(Service service) {
        this.service = service;
    }

    /*
     * /nyexgaming daily - informações das transações do dia;
     * /nyexgaming storage <jogador> - ver armazem do jogador;
     * /nyexgaming reload - recarregar o sistema;
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.service.reload();

        sender.sendMessage("§9Nyex Gaming ⇝ §fA operação foi executada, verifique o console para possíveis erros.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
