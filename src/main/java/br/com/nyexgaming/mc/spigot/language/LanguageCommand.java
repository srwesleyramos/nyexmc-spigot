package br.com.nyexgaming.mc.spigot.language;

import br.com.nyexgaming.mc.spigot.database.models.UserModel;
import br.com.nyexgaming.mc.spigot.language.lang.Translation;
import br.com.nyexgaming.mc.spigot.service.Service;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageCommand extends Command {

    private final Pattern pattern = Pattern.compile("<translations>");
    private final Service service;

    public LanguageCommand(Service service) {
        super(
                "nyexlanguage",
                "Configure the language you want to see the plugin.",
                "/nyexlanguage <language>",
                Collections.singletonList("nyexidioma")
        );

        this.service = service;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Translation language = sender instanceof Player ? service.language.getTranslationByUser(sender) : service.language.getDefaultTranslation();

        if (args.length == 3) {
            if (Arrays.asList("set", "setar").contains(args[0].toLowerCase())) {
                return setLanguageCommand(sender, args, language);
            }
        }

        if (args.length == 2) {
            if (Arrays.asList("see", "ver").contains(args[0].toLowerCase())) {
                return getLanguageCommand(sender, args, language);
            }
        }

        if (args.length == 1) {
            if (Arrays.asList("list", "listar").contains(args[0].toLowerCase())) {
                return listLanguagesCommand(sender, args, language);
            }

            return setSelfLanguageCommand(sender, args, language);
        }

        return getSelfLanguageCommand(sender, args, language);
    }

    public boolean listLanguagesCommand(CommandSender sender, String[] args, Translation language) {
        if (!sender.hasPermission("nyexgaming.command.language.list") || !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return false;
        }

        TextComponent formatted = new TextComponent("");

        service.language.getTranslations().forEach(translation -> {
            if (formatted.getExtra() != null) {
                formatted.addExtra(", ");
            }

            TextComponent component = new TextComponent();

            component.setText((language.getName().equals(translation.getName()) ? "§e" : "§7") + translation.getName());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(component.getText()).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + getName() + " " + translation.getName()));

            formatted.addExtra(component);
        });

        language.getMessage("language.list.get").forEach(text -> {
            TextComponent component = new TextComponent("");

            Matcher matcher = pattern.matcher(text);
            int startsString = 0;

            while (matcher.find()) {
                component.addExtra(new TextComponent(text.substring(startsString, matcher.start()).replace("&", "§")));
                component.addExtra(formatted);

                startsString = matcher.end();
            }

            if (startsString != text.length()) {
                component.addExtra(new TextComponent(text.substring(startsString).replace("&", "§")));
            }

            ((Player) sender).spigot().sendMessage(component);
        });

        return true;
    }

    public boolean setLanguageCommand(CommandSender sender, String[] args, Translation language) {
        if (!sender.hasPermission("nyexgaming.command.language.others")) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return false;
        }

        UserModel target = service.database.getUserByName(args[1]);

        if (target == null) {
            language.sendMessage(sender, "language.player-not-found", "<player>", args[1]);
            return false;
        }

        Translation translation = service.language.getTranslation(args[2]);

        if (translation == null) {
            language.sendMessage(sender, "language.translation-not-found", "<translation>", args[2]);
            return false;
        }

        target.setLanguage(translation.getName());

        language.sendMessage(sender, "language.others.set", "<player>", target.getName(), "<translation>", target.getLanguage());

        return true;
    }

    public boolean getLanguageCommand(CommandSender sender, String[] args, Translation language) {
        if (!sender.hasPermission("nyexgaming.command.language.others")) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return false;
        }

        UserModel target = service.database.getUserByName(args[1]);

        if (target == null) {
            language.sendMessage(sender, "language.player-not-found", "<player>", args[1]);
            return false;
        }

        language.sendMessage(sender, "language.others.get", "<player>", target.getName(), "<translation>", target.getLanguage());

        return true;
    }

    public boolean setSelfLanguageCommand(CommandSender sender, String[] args, Translation language) {
        if (!sender.hasPermission("nyexgaming.command.language") || !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return false;
        }

        UserModel target = service.database.getUserByName(sender.getName());

        if (target == null) {
            language.sendMessage(sender, "language.player-not-found", "<player>", sender.getName());
            return false;
        }

        Translation translation = service.language.getTranslation(args[0]);

        if (translation == null) {
            language.sendMessage(sender, "language.translation-not-found", "<translation>", args[0]);
            return false;
        }

        target.setLanguage(translation.getName());

        language.sendMessage(sender, "language.command.set", "<translation>", target.getLanguage());

        return true;
    }

    public boolean getSelfLanguageCommand(CommandSender sender, String[] args, Translation language) {
        if (!sender.hasPermission("nyexgaming.command.language") || !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return false;
        }

        UserModel target = service.database.getUserByName(sender.getName());

        if (target == null) {
            language.sendMessage(sender, "language.player-not-found", "<player>", sender.getName());
            return false;
        }

        language.sendMessage(sender, "language.command.get", "<translation>", target.getLanguage());

        return true;
    }
}
