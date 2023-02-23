package br.com.nyexgaming.mc.spigot.storage;

import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StorageCommand implements CommandExecutor, TabCompleter {

    public final Service service;

    public StorageCommand(Service service) {
        this.service = service;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!service.isStorageActivated()) {
            sender.sendMessage("§cO sistema de armazém se encontra desativado no momento.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§4Nyex Gaming ⇝ §cO sistema de armazém só pode ser acessado por jogadores.");
            return true;
        }

        if (args.length == 1 && sender.hasPermission("nyexgaming.command.storage.recognize")) {
            if (Arrays.asList("recognize", "reconhecer", "select", "selecionar").contains(args[0].toLowerCase())) {
                return recognize(sender);
            }
        }

        if (args.length == 1 && sender.hasPermission("nyexgaming.command.storage.others")) {
            if (!service.storage.views.containsKey(args[0].toLowerCase())) {
                sender.sendMessage("§4Nyex Gaming ⇝ §cEsse jogador não consta em nosso banco de dados.");
                return true;
            }

            service.storage.createViewer((Player) sender, args[0]);
            return true;
        }

        service.storage.createViewer((Player) sender, sender.getName());
        return true;
    }

    public boolean recognize(CommandSender sender) {
        if (!service.isNpcsActivated()) {
            sender.sendMessage("§4Nyex Gaming ⇝ §cO módulo armazém/npcs encontra-se desativado.");
            return true;
        }

        net.citizensnpcs.api.npc.NPC npc = net.citizensnpcs.api.CitizensAPI.getDefaultNPCSelector().getSelected(sender);

        if (npc == null) {
            sender.sendMessage("§4Nyex Gaming ⇝ §cVocê deve ter um NPC selecionado para usar este comando.");
            return true;
        }

        //service.database.insertOrDelete(npc.getId(), npc.getStoredLocation());

        if (service.storage.npcs.containsKey(npc.getId())) {
            service.storage.npcs.remove(npc.getId());

            sender.sendMessage("§9Nyex Gaming ⇝ §fGG! Você removeu esse NPC da listagem do armazém.");
            return true;
        }

        service.storage.npcs.put(npc.getId(), npc.getStoredLocation());

        sender.sendMessage("§9Nyex Gaming ⇝ §fPronto! Você adicionou esse NPC a listagem do armazém.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(name -> args.length != 1 || name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }
}
