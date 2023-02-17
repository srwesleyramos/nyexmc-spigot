package br.com.nyexgaming.mc.spigot.modules.storage;

import br.com.nyexgaming.mc.spigot.modules.service.Service;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StorageCommand implements CommandExecutor, TabCompleter {

    private final Service service;

    public StorageCommand(Service service) {
        this.service = service;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!service.isStorageActivated()) {
            sender.sendMessage("§4Nyex Gaming ⇝ §cO sistema de armazém se encontra desativado no momento.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§4Nyex Gaming ⇝ §cO sistema de armazém só pode ser acessado por jogadores.");
            return true;
        }

        ((Storage) service.getExecutor()).create(((Player) sender));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
