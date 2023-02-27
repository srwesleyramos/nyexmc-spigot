package br.com.nyexgaming.mc.spigot.language.impl;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface MessageImpl {

    List<String> getMessage(String path, String... placeholders);

    void sendMessage(CommandSender player, String path, String... placeholders);
}
