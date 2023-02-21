package br.com.nyexgaming.mc.spigot.service;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class ServiceCommand implements CommandExecutor, TabCompleter {

    public final Service service;

    public ServiceCommand(Service service) {
        this.service = service;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("daily")) {
            sender.sendMessage("§9Nyex Gaming ⇝ §fA operação selecionada ainda não está disponível.");
            return true;
        }

        if (args.length == 1 && Arrays.asList("reload", "recarregar").contains(args[0].toLowerCase())) {
            service.reload();

            sender.sendMessage("§9Nyex Gaming ⇝ §fA operação solicitada foi executada. Verifique o console para possíveis erros.");
            return true;
        }

        sender.sendMessage("§9Nyex Gaming ⇝ §fUse: §7/nyexgaming <daily / recarregar>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList("daily", "reload", "recarregar");
    }
}
